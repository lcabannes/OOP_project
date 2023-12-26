package com.li.oopproject;

import com.li.oopproject.entities.Alien;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

public class Game {
    private final static int TICKDELAY = 50; // delay between each game-state update in the game
    private GameInterface gameInterface;
    private int level;
    private int currentWave;
    private int waveNum;
    private int mode;
    private Board board;
    private int hp = 3;

    public Game(int level, int mode){
        this.level = level;
        this.currentWave = 0;
        this.board = new Board();
        this.waveNum = level + 3;
        this.mode = mode;
        // use the eventQueue invoke later to execute the Graphical interface update in the EDT
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                gameInterface = new GameInterface();
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

                    // here several updates, like position update, etc, will be done every TICK
                    int hpLost = board.moveEntities();
                    hp -= hpLost;
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
        baseTimer.start(); // create a base clock that will update the game state every TICKDELAY miliseconds
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

}
