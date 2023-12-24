package com.li.oopproject;

import javax.swing.*;

public class Game {
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
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                gameInterface = new GameInterface();
            }
        });

        this.startGame();


        //    while (!this.gameOver() & !this.gameWon()){
        //    System.out.println("game launched");
        // }



    }

    public boolean gameOver(){
        return this.hp <= 0;
    }
    public boolean gameWon(){
        return this.currentWave == this.waveNum;
    }
    public void startGame(){
        System.out.println("Game Starting");
        this.currentWave += 1;
    }

}
