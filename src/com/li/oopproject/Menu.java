package com.li.oopproject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Menu extends JFrame {
    private int selectedMode;
    private int selectedBoard;

    public Menu() {

        // Create a main panel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        // Create a dropdown for game modes
        String[] gameModes = {"Easy", "Normal", "Hard", "Marathon"};
        JComboBox<String> modeDropdown = new JComboBox<>(gameModes);
        modeDropdown.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JComboBox cb = (JComboBox)e.getSource();
                String selected = (String)cb.getSelectedItem();
                switch (selected) {
                    case "Easy":
                        selectedMode = Game.EASY_MODE;
                        break;
                    case "Normal":
                        selectedMode = Game.NORMAL_MODE;
                        break;
                    case "Hard":
                        selectedMode = Game.HARD_MODE;
                        break;
                    case "Marathon":
                        selectedMode = Game.MARATHON_MODE;
                        break;
                }
            }
        });

        // Create a button for starting a new game
        JButton newGameButton = new JButton("New Game");
        newGameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                startGame();
            }
        });

        String[] boards = {"default board", "portal board"};

        JComboBox<String> boardDropdown = new JComboBox<>(boards);
        boardDropdown.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JComboBox cb = (JComboBox)e.getSource();
                String selected = (String)cb.getSelectedItem();
                switch (selected) {
                    case "default board":
                        selectedBoard = 0;
                        break;
                    case "portal board":
                        selectedBoard = 1;
                }
            }
        });

        // Add components to the main panel
        mainPanel.add(modeDropdown, BorderLayout.NORTH);
        mainPanel.add(newGameButton, BorderLayout.CENTER);
        mainPanel.add(boardDropdown, BorderLayout.SOUTH);


        // Frame properties
        setTitle("Game Menu");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        add(mainPanel);
        setVisible(true);
    }

    private void startGame() {
        System.out.println("Game is starting in mode: " + selectedMode);
        Main.startGame(selectedMode, selectedBoard); // Pass the selected mode to the game
    }
}
