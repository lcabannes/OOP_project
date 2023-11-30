package com.li.oopproject;

public class Game {
    private GameInterface gameInterface;
    public void startGame(){
        this.gameInterface = new GameInterface();
        System.out.println("Game Starting");
    }
}
