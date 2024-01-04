package com.li.oopproject;

import com.li.oopproject.entities.Alien;
import com.li.oopproject.entities.BossAlien;
import com.li.oopproject.entities.Human;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Random;
import java.io.*;

public class Game {
    private final static int TICKDELAY = 20; // delay between each game-state update in the game

    private final static int[] waveDuration = {10000, 20000, 20000, 20000, 20000, 20000, 20000}; // waves duration in ms
    private int timeSinceWaveStart = 0;
    private GameInterface gameInterface;
    public static final boolean VERBOSE = false;

    private int level;

    private int mode;
    private int currentWave;
    private int waveNum;

    public static final int EASY_MODE = 1;
    public static final int NORMAL_MODE = 2;
    public static final int HARD_MODE = 3;
    public static final int MARATHON_MODE = 4;
    private final Board board;
    private int hp = 3;
    private final Timer AlienSpawner;
    private final ArrayList<String> spawnableAliens = new ArrayList<>();
    private final Scores bestScores;
    private boolean bossSpawned = false;
    private int bossHP;


    public Board getBoard() {
        return board;
    }

    public Game(int level, int mode, int boardType, Scores bestScores){
        this.bestScores = bestScores;
        this.level = level;
        this.currentWave = 1;
        this.board = new Board(this, boardType);
        //variables to handle different mode(game mode)
        this.mode = mode;

        // Set wave number based on mode
        switch(this.mode) {
            case EASY_MODE:
                this.waveNum = 3;
                bossHP = 100; // Set lower HP for easy mode
                break;
            case NORMAL_MODE:
                this.waveNum = 5;
                bossHP = 150;
                break;
            case HARD_MODE:
                this.waveNum = 7;
                bossHP = 200;
                break;
            case MARATHON_MODE:
                this.waveNum = 100;
                bossHP = 250;
                break;
            default:
                // Default to easy mode if an invalid mode is provided
                System.out.print("Default mode, easy mode\n");
                this.waveNum = 3;
                break;
        }

        // use the eventQueue invoke later to execute the Graphical interface update in the EDT

        gameInterface = new GameInterface(Game.this);

        // define a basic alien Spawner that randomly spawns an alien every 8 seconds (8000ms)
        // and prints the board every time
        AlienSpawner = new Timer(6000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                spawnAlien(); // Now chooses alien type based on the wave
                if (VERBOSE) {
                    System.out.printf("Hp left: %d\n", hp);
                    board.display();
                }
            }
        });

    }

    public boolean isGameOver(){
        return this.hp <= 0;
    }
    public boolean isGameWon(){
        // The game is won if it's the final wave, the boss has spawned, and the boss alien is not alive
        return currentWave == waveNum && bossSpawned && !board.isBossAlienAlive();
    }
    public void startGame(){
        System.out.println("Game Starting");

        Timer baseTimer = new Timer(TICKDELAY, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                updateGame();
                if (isGameOver() || isGameWon()) {
                    ((Timer) e.getSource()).stop();  // Stop the timer when the game is finished
                    if (isGameOver()){
                        System.out.println("You lost!");
                    }
                    if (isGameWon()){
                        System.out.println("you won!!!");
                    }
                    bestScores.insertScore(currentWave-1, board.getBoardType());
                    saveScores();
                }
            }
        });
        baseTimer.start(); // create a base clock that will update the game state every TICKDELAY milliseconds
    }

    public void spawnAlien() {
        // Only spawn the boss in the final wave
        if (currentWave == waveNum) {
            if (!bossSpawned) {
                int middleX = Board.length; // Assuming Board.length is the width of the board
                int middleY = Board.height / 2 - 1; // Assuming Board.height is the height of the board
                Alien bossAlien = board.spawnAlien("BossAlien", middleY, middleX); // Set the position of the boss alien
                gameInterface.addEntity(bossAlien);
                bossSpawned = true;
            }
        } else {
            Random randomInt = new Random();
            int ySpawnPosition = randomInt.nextInt(Board.height);
            int xSpawnPosition = Board.length;
            if (board.getBoardType() == 1) {
                xSpawnPosition -= randomInt.nextInt(3);
            }
            Alien alien = board.spawnAlien(spawnableAliens.get(randomInt.nextInt(spawnableAliens.size())), ySpawnPosition, xSpawnPosition);
            gameInterface.addEntity(alien);
        }
    }

    public boolean placeHuman(Human human, int row, int col, GoldSystem goldSystem){

        if (board.placeHuman(human, row, col, goldSystem)){
            gameInterface.addEntity(human);
            return true;
        }
        return false;
    }

    public void saveScores(){
        String filePath = GameInterface.class.getProtectionDomain().
                getCodeSource().getLocation().getPath() + "com/li/oopproject/bestScores.ser";

        try (FileOutputStream fileOutputStream = new FileOutputStream(filePath);
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream)) {

            // Serialize and write the object to the file
            objectOutputStream.writeObject(bestScores);
            System.out.println(bestScores.getBest());

            System.out.println("Scores saved to " + filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void updateGame() {
        // Game logic
        // keep track of time elapsed to know when to end a wave
        if (timeSinceWaveStart == 0) {
            if (currentWave == waveNum) {
                // Logic for the final wave
                AlienSpawner.stop();
                spawnAlien(); // Spawn the boss alien
            } else {
                switch (currentWave) {
                    case 0: // wave 0 : for a fun easter egg
                        spawnableAliens.add("DefaultAlien");
                        AlienSpawner.setDelay(100);
                    case 1: // Wave 1
                        //spawn OctopusAlien in wave 1
                        spawnableAliens.add("OctopusAlien");
                        AlienSpawner.setDelay(4000);
                        break;
                    case 2: // Wave 2
                        //spawn OctopusAlien, GhostAlien since wave 2
                        spawnableAliens.add("GhostAlien");
                        AlienSpawner.setDelay(3500);
                        break;
                    case 3: // Wave 3
                        //spawn OctopusAlien, GhostAlien, AlienShip since wave 3
                        spawnableAliens.add("AlienShip");
                        AlienSpawner.setDelay(3000);
                        break;
                    case 4: // Wave 4
                        AlienSpawner.setDelay(2500);
                        break;
                    case 5: // Wave 5
                        AlienSpawner.setDelay(2000);
                        break;
                    case 6: // Wave 6
                        AlienSpawner.setDelay(1500);
                        break;
                    case 7: // Wave 7
                        AlienSpawner.setDelay(1000);
                        break;
                    default:
                        if (AlienSpawner.getDelay() > 100) {
                            AlienSpawner.setDelay(AlienSpawner.getDelay() - 100);
                        }
                }
            AlienSpawner.start();
            AlienSpawner.setInitialDelay(4000);
            }
        }


        // here several updates, like position update, etc..., will be done every TICK
        int hpLost = board.updateEntities(TICKDELAY);
        hp -= hpLost;

        // use evenQueue to update the display in a thread-safe manner
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                gameInterface.display();
                gameInterface.updateGameInfo(currentWave, hp, board.getGoldSystem().getGold());
            }
        });
        // TODO

        timeSinceWaveStart += TICKDELAY;
        int curWaveDuration = (currentWave <= 7) ? waveDuration[currentWave - 1] : 30000; // Marathon mode logic

        if (timeSinceWaveStart >= curWaveDuration) {
            AlienSpawner.stop();
            if (currentWave < waveNum || board.noAlien()) {
                System.out.println("Good job, you finished Wave " + currentWave);
                timeSinceWaveStart = 0;
                currentWave += 1;

                if (currentWave > waveNum) {
                    System.out.println("All waves completed!");
                    // Additional code for game completion
                }
            }
        }
    }
    // Getter for bossHP
    public int getBossHP() {
        return bossHP;
    }

    public GameInterface getGameInterface() {
        return gameInterface;
    }
}



