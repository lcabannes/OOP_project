package com.li.oopproject;

import com.li.oopproject.entities.Alien;
import com.li.oopproject.entities.Entity;

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
import java.util.ArrayList;

public class GameInterface extends JFrame{
    private JPanel backGroundPanel;

    private JPanel foreGroundPanel;
    private ArrayList<Entity> displayedEntities = new ArrayList<>();
    public GameInterface() {
        setSize(800, 800);

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        backGroundPanel = new JPanel();
        backGroundPanel.setLayout(new GridLayout(Board.height, Board.length));
        backGroundPanel.setBackground(new Color(0, 0, 255, 0));
        this.add(backGroundPanel);
        try {
            BufferedImage myPicture = ImageIO.read(new File(GameInterface.class.getProtectionDomain().
                    getCodeSource().getLocation().getPath() + "com/li/oopproject/assets/Aliens/alien_spacecraft.png"));
            JLabel picLabel = new JLabel(new ImageIcon(myPicture));
         //   this.add(picLabel);
        }
        catch (IOException e) {
            System.out.println("No file found");
            return;
        }
        foreGroundPanel = new JPanel();
        foreGroundPanel.setLayout(null);
        this.add(foreGroundPanel);
        setResizable(false);
        setVisible(true);


    }
    public static BufferedImage resizeImage(BufferedImage originalImage, int targetWidth, int targetHeight) {
        Image resultingImage = originalImage.getScaledInstance(targetWidth, targetHeight, Image.SCALE_SMOOTH);
        BufferedImage resizedImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2d = resizedImage.createGraphics();
        g2d.drawImage(resultingImage, 0, 0, null);
        g2d.dispose();
        return resizedImage;
    }
    // add entity to track it during display
    public void addEntity(Entity entity){
        displayedEntities.add(entity);
    }
    public void display(){
        // remove all Entities (because they have changed position but also appearance (red/attacking/damaged)
        foreGroundPanel.removeAll();
        foreGroundPanel.repaint(); // repaint to clear background
        int i = 0;
        while (i < displayedEntities.size()){
            Entity entity = displayedEntities.get(i);
            // if an entity has passed all defenses stop desplaying it
            if (entity.getxPos() < 0){
                displayedEntities.remove(i);
                continue;
            }
            BufferedImage entityIcon = entity.classIcon;
            // otherwise display it
            if (entityIcon != null) {
                JLabel imageLabel = new JLabel(new ImageIcon(entityIcon));
                imageLabel.setOpaque(false);
                foreGroundPanel.add(imageLabel);
                //place it at its current position
                imageLabel.setBounds(entity.getxPos(), entity.getyPos(), entityIcon.getWidth(), entityIcon.getHeight());
            } else {
                System.out.println("Failed to load the image.");
            }
            i++;
        }
        this.setVisible(true);
    }
}
