package com.li.oopproject;

import com.li.oopproject.entities.Alien;
import com.li.oopproject.entities.Human;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

public class Game {
    private final static int TICKDELAY = 30; // delay between each game-state update in the game



    private GameInterface gameInterface;
    private int level;
    private int currentWave;
    private int waveNum;
    private int mode;
    private Board board;
    private int hp = 3;

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
                // Game loop logic
                if (!Game.this.isGameOver() & !Game.this.isGameWon()) {

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

                } else {
                    ((Timer) e.getSource()).stop();  // Stop the timer when the game is finished
                    System.out.println("Game is finished!!!");

                }
            }
        });
        baseTimer.start(); // create a base clock that will update the game state every TICKDELAY milliseconds

        // define a baisc alien Spawner that randomly spawns an alien every 8 seconds (8000ms)
        Timer alienSpawner = new Timer(8000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Random randomInt = new Random();
                int ySpawnPosition = randomInt.nextInt(Board.height);
                Alien alien = board.spawnAlien("DefaultAlien", ySpawnPosition); // use spawn() method of Board to spawn a new Zombie
                System.out.printf("Hp left: %d\n", hp);
                board.display();
                gameInterface.addEntity(alien);

            }
        });
        alienSpawner.start();

    }

    public boolean placeHuman(Human human, int row, int col){

        if (board.placeHuman(human, row, col)){
            gameInterface.addEntity(human);
            return true;
        }
        return false;
    }

    public GameInterface getGameInterface() {
        return gameInterface;
    }
}
