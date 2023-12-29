package com.li.oopproject;

import javax.swing.*;

public class Main {


    public static void main(String[] args){

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Menu();
            }
        });


    }

    public static void startGame(int selectedMode){
        System.out.println("Mode value before constructor call: " + selectedMode);
        Game game = new Game(0, selectedMode);
        game.startGame();
    }
}
