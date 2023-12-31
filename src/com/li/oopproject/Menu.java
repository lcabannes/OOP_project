package com.li.oopproject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Menu extends JFrame {
    private int selectedMode;

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

        // Add components to the main panel
        mainPanel.add(modeDropdown, BorderLayout.NORTH);
        mainPanel.add(newGameButton, BorderLayout.CENTER);

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
        Main.startGame(selectedMode); // Pass the selected mode to the game
    }
}
