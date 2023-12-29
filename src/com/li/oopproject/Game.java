package com.li.oopproject;

import com.li.oopproject.entities.Alien;
import com.li.oopproject.entities.Human;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

public class Game {
    private final static int TICKDELAY = 20; // delay between each game-state update in the game

    private final static int[] waveDuration = {10000, 20000, 20000}; // waves duration in ms
    private int timeSinceWaveStart = 0;
    private GameInterface gameInterface;
    private boolean VERBOSE = true;
    private int level;
    private int currentWave;
    private int waveNum;
    private int mode;
    private Board board;
    private int hp = 3;

    private Timer DefaultAlienSpawner;

    public Board getBoard() {
        return board;
    }

    public Game(int level, int mode){
        this.level = level;
        this.currentWave = 0;
        this.board = new Board(this);
        this.waveNum = level + 3;
        this.mode = mode;
        // use the eventQueue invoke later to execute the Graphical interface update in the EDT
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                gameInterface = new GameInterface(Game.this);
            }

        });

        // define a basic alien Spawner that randomly spawns an alien every 8 seconds (8000ms)
        // and prints the board every time
        DefaultAlienSpawner = new Timer(8000, new ActionListener() {
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
        return this.currentWave == this.waveNum;
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

                }
            }
        });

        baseTimer.start(); // create a base clock that will update the game state every TICKDELAY milliseconds

    }
    public void spawnAlien() {
        Random randomInt = new Random();
        int ySpawnPosition = randomInt.nextInt(Board.height);
        Alien alien;

        //spawn OctopusAlien in wave 1
        //spawn OctopusAlien, GhostAlien in wave 2
        //spawn OctopusAlien, GhostAlien, AlienShip in wave 3
        if (currentWave == 1) {
            alien = board.spawnAlien("OctopusAlien", ySpawnPosition);
        } else if (currentWave == 2) {
            String[] aliens = {"OctopusAlien", "GhostAlien"};
            alien = board.spawnAlien(aliens[randomInt.nextInt(aliens.length)], ySpawnPosition);
        } else if (currentWave >= 3) {
            String[] aliens = {"OctopusAlien", "GhostAlien", "AlienShip"};
            alien = board.spawnAlien(aliens[randomInt.nextInt(aliens.length)], ySpawnPosition);
        } else {
            alien = board.spawnAlien("DefaultAlien", ySpawnPosition);
        }

        gameInterface.addEntity(alien);
    }

    public boolean placeHuman(Human human, int row, int col){

        if (board.placeHuman(human, row, col)){
            gameInterface.addEntity(human);
            return true;
        }
        return false;
    }

    public void updateGame(){
        // Game logic
        // keep track of time elapsed to know when to end a wave
        if (timeSinceWaveStart == 0){
            if (currentWave == 1){
                DefaultAlienSpawner.setDelay(4000);

            }
            if (currentWave == 2){
                DefaultAlienSpawner.setDelay(2000);
            }
            DefaultAlienSpawner.start();
        }

        // here several updates, like position update, etc..., will be done every TICK
        int hpLost = board.updateEntities(TICKDELAY);
        hp -= hpLost;

        // use evenQueue to update the display in a thread-safe manner
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                gameInterface.display();
            }
        });
        // TODO

        timeSinceWaveStart += TICKDELAY;
        if (timeSinceWaveStart >= waveDuration[currentWave]){
            DefaultAlienSpawner.stop();
            if (board.noAlien()){
                System.out.println("Good job, you finished Wave " + currentWave);
                timeSinceWaveStart = 0;
                currentWave += 1;
            }

        }
    }
    public GameInterface getGameInterface() {
        return gameInterface;
    }
}

