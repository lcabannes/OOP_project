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
    public static void startGame(){
        Game game = new Game(1, 0);
        game.startGame();
    }
}
