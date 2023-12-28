package com.li.oopproject;

import com.li.oopproject.entities.DefaultHuman;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

public class test {
    public static JPanel buttonPanel;
    public static void main(String[] args){
        JFrame frame = new JFrame();
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(Board.height+1, Board.length));
        JButton button1 = new JButton();
        button1.setBackground(Color.BLUE);
        buttonPanel.add(button1);
        JButton button2 = new JButton();
        button2.setBackground(new Color(0, 255,0 , 0));

        button2.setOpaque(true);
        buttonPanel.add(button2);


        frame.add(buttonPanel);
        frame.setVisible(true);

    }
}
