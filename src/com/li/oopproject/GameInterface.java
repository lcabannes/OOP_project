package com.li.oopproject;

import com.li.oopproject.entities.Alien;
import com.li.oopproject.entities.DefaultHuman;
import com.li.oopproject.entities.Entity;
import com.li.oopproject.entities.Human;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class GameInterface extends JFrame{

    private Game game;
    private JPanel buttonPanel;
    private JPanel backGroundPanel;
    private JPanel foreGroundPanel;
    private ArrayList<Entity> displayedEntities = new ArrayList<>();
    private Human clickedIcon = null;
    public GameInterface(Game game) {
        this.game = game;
        setSize(800, 600);
        // use a layered Pane to deal with the different layers of panels
        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(new Dimension(800, 600));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        Human DefaultHumanButton = new DefaultHuman(game.getBoard());
        DefaultHumanButton.setyPos(-100);
        DefaultHumanButton.setxPos(0);
        displayedEntities.add(DefaultHumanButton);
        // button panels contains transparent buttons and one additional row to choose which human to select to place
        // TODO: top selection row
        buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(Board.height+1, Board.length));
        buttonPanel.setBackground(new Color(0, 0, 0, 0));
        buttonPanel.setOpaque(false);
        for (int i=0; i< Board.height+1; i++){
            for (int j=0; j<Board.length; j++){
                // use effectively final variables to allow the actionListener to know its position
                final int finalI = i;
                final int finalJ = j;
                JButton button = new JButton();
                button.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (finalI > 0) {
                            // when clicked try to place a human at this position, message the user about the outcome
                            if (clickedIcon != null) {
                                if (game.placeHuman(clickedIcon, (finalI - 1), finalJ)) {
                                    System.out.println("Successfully place a turret at: " + (finalI - 1) + " " + finalJ);
                                } else {
                                    System.out.println("You cannot place this turret here");
                                }
                                displayedEntities.remove(clickedIcon);
                                clickedIcon = null;
                            }
                        }
                        else{
                            displayedEntities.remove(clickedIcon);
                            clickedIcon = null;
                            if (finalJ == 0){
                                System.out.println("selected DefaultHuman");
                                clickedIcon = new DefaultHuman(game.getBoard());
                                clickedIcon.setxPos(0);
                                clickedIcon.setyPos(-100);
                                displayedEntities.add(clickedIcon);
                            }
                        }
                    }
                });

                button.addMouseMotionListener(new MouseMotionListener() {
                    @Override
                    public void mouseDragged(MouseEvent e) {
                        if (clickedIcon != null){
                            clickedIcon.setyPos(e.getY() + ((finalI-1) * 100));
                            clickedIcon.setxPos(e.getX() + (finalJ * 100));
                        }
                    }

                    @Override
                    public void mouseMoved(MouseEvent e) {
                        if (clickedIcon != null){
                            clickedIcon.setyPos(e.getY() + ((finalI-1) * 100));
                            clickedIcon.setxPos(e.getX() + (finalJ * 100));
                        }
                    }

                });
                // Set the button's background color to transparent
                button.setBackground(new Color(0, 0, 0, 0)); // Transparent color (alpha = 0)
                // Make the button's content area transparent
                button.setOpaque(false);
                buttonPanel.add(button);
            }
        }
        // background panel to hold the background image
        backGroundPanel = new JPanel();


        try {
            BufferedImage myPicture = ImageIO.read(new File(GameInterface.class.getProtectionDomain().
                    getCodeSource().getLocation().getPath() + "com/li/oopproject/assets/backGround/Background1.png"));
            JLabel picLabel = new JLabel(new ImageIcon(myPicture));
            backGroundPanel.add(picLabel);
        }
        catch (IOException e) {
            System.out.println("No file found");
            return;
        }
        // foreground panel is where all entities are placed
        foreGroundPanel = new JPanel();
        foreGroundPanel.setBackground(new Color(0, 0, 0, 0));
        foreGroundPanel.setOpaque(false);
        foreGroundPanel.setLayout(null);
        //set all bounds of panels to the same so that they do overlap
        backGroundPanel.setBounds(0, 0, 800, 600);
        layeredPane.setBounds(0, 0, 800, 600);

        // Set bounds and other properties for the buttonPanel
        buttonPanel.setBounds(0, 0, 800, 600); // Set the size and position
        // ... [Button initialization and adding to buttonPanel]

        // Set bounds and other properties for the foreGroundPanel
        foreGroundPanel.setBounds(0, 0, 800, 600);

        // add all panels to the layeredPane in order (lowest value = lowest panel)
        layeredPane.add(backGroundPanel, Integer.valueOf(1));
        layeredPane.add(foreGroundPanel, Integer.valueOf(2));
        layeredPane.add(buttonPanel, Integer.valueOf(3));

        //add layeredPane to main GameInterface

        this.add(layeredPane);
        setResizable(false);
        setVisible(true);


    }
    // method to resize an image
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
        foreGroundPanel.setDoubleBuffered(true);
        foreGroundPanel.repaint(); // repaint to clear background

        int i = 0;
        while (i < displayedEntities.size()){
            Entity entity = displayedEntities.get(i);
            // if an entity has passed all defenses stop displaying it
            // TODO: change this so that it is the Game class/board class that removes entities
            //  that should no longer be tracked
            if (entity.getxPos() < 0){
                displayedEntities.remove(i);
                continue;
            }
            BufferedImage entityIcon = entity.getClassIcon();
            // otherwise display it
            if (entityIcon != null) {
                JLabel imageLabel = new JLabel(new ImageIcon(entityIcon));
                imageLabel.setOpaque(false);
                foreGroundPanel.add(imageLabel);
                //place it at its current position
                imageLabel.setBounds(entity.getxPos(), entity.getyPos()+100, entityIcon.getWidth(), entityIcon.getHeight());
            } else {
                System.out.println("Failed to load the image.");
            }
            i++;
        }

    }

    public void removeEntity(Entity entity){
        displayedEntities.remove(entity);
    }
}
