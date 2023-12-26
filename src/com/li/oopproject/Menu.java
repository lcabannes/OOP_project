package com.li.oopproject;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.io.File;
import java.io.IOException;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Menu extends JFrame {
    public Menu() {
        // Create a panel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        // Create a button for starting a new game
        JButton newGameButton = new JButton("New Game");
        newGameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Code to start a new game
                dispose();
                startGame();
            }
        });

        // Add the button to the panel
        mainPanel.add(newGameButton, BorderLayout.CENTER);

        // Set frame properties
        setTitle("Game Menu");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center the frame

        // Add the panel to the frame
        add(mainPanel);

        // Set frame visibility
        setVisible(true);
    }


    private void startGame() {
        // Code to start the game
        // This method can be customized based on your game's requirements
        System.out.println("Game is starting...");

        Main.startGame();
    }

}
