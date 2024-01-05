package com.li.oopproject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

public class Menu extends JFrame {
    private int selectedMode = Game.EASY_MODE;
    private int selectedBoard;

    private Scores bestScores;

    public final int UNLOCK_BOARD_LEVEL = 5;

    public Menu() {
        loadScores();

        // Create a main panel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        // Create a dropdown for game modes
        String[] gameModes = {"Easy", "Normal", "Hard", "Marathon"};
        JComboBox<String> modeDropdown = initModeDropDown(gameModes);

        // Create a button for starting a new game
        JButton newGameButton = new JButton("New Game");
        newGameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                startGame();
            }
        });

        JComboBox<String> boardDropdown = initBoardDropdown();

        JButton bestScoresButton = new JButton("Best Scores");
        bestScoresButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                displayScores();
            }
        });

        // Add components to the main panel
        mainPanel.add(modeDropdown, BorderLayout.NORTH);
        mainPanel.add(newGameButton, BorderLayout.CENTER);
        mainPanel.add(boardDropdown, BorderLayout.SOUTH);
        mainPanel.add(bestScoresButton, BorderLayout.BEFORE_LINE_BEGINS);


        // Frame properties
        setTitle("Game Menu");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        add(mainPanel);
        setVisible(true);
    }

    public JComboBox<String> initBoardDropdown(){
        String[] boards = {"default board", "portal board"};
        if (bestScores.getBest() <= UNLOCK_BOARD_LEVEL){
            boards[1] = "FINISH WAVE " + UNLOCK_BOARD_LEVEL + " TO UNLOCK NEW BOARD";
        }
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
                        break;
                    default:
                        selectedBoard = 0;
                }
            }
        });
        return boardDropdown;
    }

    public JComboBox<String> initModeDropDown(String[] gameModes){
        // Create a dropdown for game modes
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
        return modeDropdown;
    }
    public void loadScores(){
        String filePath = GameInterface.class.getProtectionDomain().
                getCodeSource().getLocation().getPath() + "com/li/oopproject/bestScores.ser";
        if (fileExists(filePath)) {
            Scores savedScores = deserializeObject(filePath);
            if (savedScores != null) {
                // Use the deserialized object as needed
                this.bestScores = savedScores;
            } else {
                System.out.println("Failed to deserialize the scores.");
            }
        } else {
            this.bestScores = new Scores();
        }

    }
    private void startGame() {
        System.out.println("Game is starting in mode: " + selectedMode);
        Main.startGame(selectedMode, selectedBoard, bestScores); // Pass the selected mode to the game
    }

    // Deserialize an object from a file
    private static <T> T deserializeObject(String filePath) {
        try (FileInputStream fileIn = new FileInputStream(filePath);
             ObjectInputStream objectIn = new ObjectInputStream(fileIn)) {
            return (T) objectIn.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static boolean fileExists(String filePath) {
        File file = new File(filePath);
        return file.exists() && !file.isDirectory();
    }

    public void displayScores() {

        StringBuilder sb = new StringBuilder();
        sb.append("Default Board 10 best scores:\n");
        for (Integer score : bestScores.getDefault_board_scores()) {
            sb.append(score).append("\n");
        }

        sb.append("\nPortal Board 10 best scores:\n");
        for (Integer score : bestScores.getPortal_board_scores()) {
            sb.append(score).append("\n");
        }

        JOptionPane.showMessageDialog(null, sb.toString(), "Best Scores", JOptionPane.INFORMATION_MESSAGE);
    }
}
