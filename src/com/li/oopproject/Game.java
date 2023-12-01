package com.li.oopproject;

public class Game {
    private GameInterface gameInterface;
    private int level;
    private int currentWave;
    private int waveNum;
    private int mode;
    private Board board;
    private Player player;
    private int hp = 3;

    public Game(int level, int mode, Board board, Player player){
        this.level = level;
        this.currentWave = 0;
        this.board = board;
        this.player = player;
        this.waveNum = level + 3;
        this.mode = mode;
        this.gameInterface = new GameInterface();

        this.startGame();
        while (!this.gameOver() & !this.gameWon()){

        }
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
