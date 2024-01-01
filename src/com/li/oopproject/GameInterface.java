package com.li.oopproject;

import com.li.oopproject.entities.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class GameInterface extends JFrame{
    public static final int TILESIZE = 100;
    public static final int WINDOWLENGTH = Board.length * TILESIZE;
    public static final int WINDOWHEIGHT = (Board.height+1) * TILESIZE;
    private Game game;
    private JPanel buttonPanel;
    private JPanel backGroundPanel;
    private JPanel foreGroundPanel;
    private ArrayList<Entity> displayedEntities = new ArrayList<>();
    private Entity clickedIcon = null;
    private GoldSystem goldSystem;

    private JLabel waveLabel, hpLabel, goldLabel; // For update game info

    public GameInterface(Game game) {
        this.game = game;
        this.goldSystem = game.getBoard().getGoldSystem();


        // small extension to the frame to account for some unknown offset probably due to frame/button borders
        setSize(WINDOWLENGTH+15, WINDOWHEIGHT+40);
        // use a layered Pane to deal with the different layers of panels
        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(new Dimension(WINDOWLENGTH, WINDOWHEIGHT));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        initializeButtons();

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
                                if (clickedIcon instanceof Human){
                                    if (game.placeHuman((Human) clickedIcon, (finalI - 1), finalJ, goldSystem)) {
                                        System.out.println("Successfully place a turret at: " + (finalI - 1) + " " + finalJ);
                                    } else {
                                        System.out.println("You cannot place this turret here");
                                    }
                                }
                                else{
                                    Human human = game.getBoard().tiles[finalI-1][finalJ].human;
                                    if (clickedIcon instanceof Upgrade){
                                        if (human != null & ((Upgrade) clickedIcon).getGoldCost() <= goldSystem.getGold()) {
                                            human.upgrade();
                                            goldSystem.addGold(-((Upgrade) clickedIcon).getGoldCost());
                                            System.out.println("Successfully upgraded a turret at: " + (finalI - 1) + " " + finalJ);
                                        } else {
                                            System.out.println("You cannot upgrade this turret");
                                        }
                                    }
                                }
                            }
                            displayedEntities.remove(clickedIcon);
                            clickedIcon = null;

                        }
                        else {
                            // Top row button logic for selecting an entity
                            displayedEntities.remove(clickedIcon);
                            clickedIcon = null;

                            switch(finalJ) {
                                case 0: // DefaultHuman button
                                    System.out.println("Selected DefaultHuman");
                                    clickedIcon = new Tank(game.getBoard());
                                    break;
                                case 1: // Gunner button
                                    System.out.println("Selected Gunner");
                                    clickedIcon = new Gunner(game.getBoard());
                                    break;
                                case 2: // GhostBuster button
                                    System.out.println("Selected GhostBuster");
                                    clickedIcon = new GhostBuster(game.getBoard());
                                    break;
                                case 3: // Freezer
                                    System.out.println("Selected Freezer");
                                    clickedIcon = new Freezer(game.getBoard());
                                    break;
                                case 5: // upgrade button
                                    clickedIcon = new Upgrade(game.getBoard());
                                default:
                                    // Handle other cases or do nothing
                                    break;
                            }

                            if (clickedIcon != null) {
                                clickedIcon.setxPos(0);
                                clickedIcon.setyPos(-TILESIZE);
                                displayedEntities.add(clickedIcon);
                            }
                        }
                    }
                });

                button.addMouseMotionListener(new MouseMotionListener() {
                    @Override
                    public void mouseDragged(MouseEvent e) {
                        if (clickedIcon != null){
                            clickedIcon.setyPos(e.getY() + ((finalI-1) * TILESIZE)-40);
                            clickedIcon.setxPos(e.getX() + (finalJ * TILESIZE)-40);
                        }
                    }

                    // the finalI-1 comes from the fact that we are giving position to entities based on the
                    // the board but there is an aditional row of buttons in the graphical interface
                    // so the indexation is different, the positions also, we might want to armonize
                    // the position later
                    @Override
                    public void mouseMoved(MouseEvent e) {
                        if (clickedIcon != null){
                            clickedIcon.setyPos(e.getY() + ((finalI-1) * TILESIZE) - 40);
                            clickedIcon.setxPos(e.getX() + (finalJ * TILESIZE) - 40);
                        }
                    }

                });
                // Set the button's background color to transparent
                button.setBackground(new Color(0, 0, 0, 0)); // Transparent color (alpha = 0)
                // Make the button's content area transparent
                button.setOpaque(false);
                button.setBorder(null); // set no border for aesthetics
                buttonPanel.add(button);
            }
        }

        // background panel to hold the background image
        backGroundPanel = new JPanel();

        // try to load the backGround image
        try {

            String path = "";
            if (game.getBoard().getBoardType() == 0){
                path = GameInterface.class.getProtectionDomain().
                        getCodeSource().getLocation().getPath() + "com/li/oopproject/assets/backGround/default_background.png";
            }
            else if (game.getBoard().getBoardType() == 1){
                path = GameInterface.class.getProtectionDomain().
                        getCodeSource().getLocation().getPath() + "com/li/oopproject/assets/backGround/portal_background.png";
            }
            BufferedImage myPicture = ImageIO.read(new File(path));
            JLabel picLabel = new JLabel(new ImageIcon(myPicture));
            backGroundPanel.add(picLabel);
        }
        catch (IOException e) {
            System.out.println("No file found");
        }

        // Initialize the game info panel
        JPanel gameInfoPanel = new JPanel();
        gameInfoPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        int panelWidth = 180;
        int panelHeight = 30;
        int rightMargin = 20;
        int topMargin = 10;

        // Set bounds to position it at the top right corner
        gameInfoPanel.setBounds(WINDOWLENGTH - panelWidth - rightMargin, topMargin, panelWidth, panelHeight);

        // Create a Font object for the labels
        Font labelFont = new Font("Arial", Font.BOLD, 14);

        // Initialize labels for wave, HP, and gold
        waveLabel = new JLabel("Wave: 0");
        hpLabel = new JLabel("HP: 3"); // Assuming starting HP is 100
        goldLabel = new JLabel("Gold: 100");

        // Set the font for all labels
        setFontForLabels(labelFont, waveLabel, hpLabel, goldLabel);

        // Add labels to the panel
        gameInfoPanel.add(waveLabel);
        gameInfoPanel.add(hpLabel);
        gameInfoPanel.add(goldLabel);

        // foreground panel is where all entities are placed
        foreGroundPanel = new JPanel();
        foreGroundPanel.setBackground(new Color(0, 0, 0, 0));
        foreGroundPanel.setOpaque(false);
        foreGroundPanel.setLayout(null);
        //set all bounds of panels to the same so that they do overlap
        backGroundPanel.setBounds(0, 0, WINDOWLENGTH, WINDOWHEIGHT);


        // Set bounds and other properties for the buttonPanel
        buttonPanel.setBounds(0, 0, WINDOWLENGTH, WINDOWHEIGHT); // Set the size and position
        // ... [Button initialization and adding to buttonPanel]

        // Set bounds and other properties for the foreGroundPanel
        foreGroundPanel.setBounds(0, 0, WINDOWLENGTH, WINDOWHEIGHT);

        // add all panels to the layeredPane in order (lowest value = lowest panel)
        layeredPane.add(backGroundPanel, Integer.valueOf(1));
        layeredPane.add(foreGroundPanel, Integer.valueOf(2));
        layeredPane.add(buttonPanel, Integer.valueOf(3));
        layeredPane.add(gameInfoPanel, Integer.valueOf(4));

        //add layeredPane to main GameInterface

        this.add(layeredPane);
        setResizable(false);

        // Play bgmusic just before showing the screen
        AudioManage audioManage = new AudioManage();
        audioManage.loadBGMusic();
        setVisible(true);

    }

    public void initializeButtons(){
        Human TankButton = new Tank(game.getBoard());
        TankButton.setyPos(-TILESIZE);
        TankButton.setxPos(0);
        displayedEntities.add(TankButton);

        // Create Gunner and GhostBuster buttons
        Human gunnerButton = new Gunner(game.getBoard());
        gunnerButton.setyPos(-TILESIZE);
        gunnerButton.setxPos(TILESIZE); // Adjust X position for Gunner
        displayedEntities.add(gunnerButton);

        Human ghostBusterButton = new GhostBuster(game.getBoard());
        ghostBusterButton.setyPos(-TILESIZE);
        ghostBusterButton.setxPos(2 * TILESIZE); // Adjust X position for GhostBuster
        displayedEntities.add(ghostBusterButton);

        Human freezerButton = new Freezer(game.getBoard());
        freezerButton.setyPos(-TILESIZE);
        freezerButton.setxPos(3 * TILESIZE); // Adjust X position for GhostBuster
        displayedEntities.add(freezerButton);

        Entity UpgradeButton = new Upgrade(game.getBoard());
        UpgradeButton.setyPos(-TILESIZE);
        UpgradeButton.setxPos(5 * TILESIZE); // Adjust X position for Upgrade button
        displayedEntities.add(UpgradeButton);
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

            BufferedImage entityIcon = entity.getInstanceIcon();
            // otherwise display it
            if (entityIcon != null) {
                JLabel imageLabel = new JLabel(new ImageIcon(entityIcon));
                //idk if the next line is useful or not, might need more testing
                //   imageLabel.setOpaque(false);
                foreGroundPanel.add(imageLabel);
                //place it at its current position + some offset so that it appears in the middle of the tile
                imageLabel.setBounds(entity.getxPos()+10, entity.getyPos()+TILESIZE+15, entityIcon.getWidth(), entityIcon.getHeight());
            } else {
                System.out.println("Failed to load the image.");
            }
            i++;
        }

    }
    private JButton getButtonAt(int row, int col) {
        Component[] components = buttonPanel.getComponents();
        int index = row * ((GridLayout) buttonPanel.getLayout()).getColumns() + col;
        if (index >= 0 && index < components.length && components[index] instanceof JButton) {
            return (JButton) components[index];
        } else {
            throw new IllegalArgumentException("Invalid row or column specified.");
        }
    }
    //stop tracking an entity
    public void removeEntity(Entity entity){
        displayedEntities.remove(entity);
    }

    // Set label font for all the game info label
    public void setFontForLabels(Font font, JLabel... labels) {
        for (JLabel label : labels) {
            label.setFont(font);
        }
    }

    public void updateGameInfo(int wave, int hp, int gold) {
        waveLabel.setText("Wave: " + wave);
        hpLabel.setText("HP: " + hp);
        goldLabel.setText("Gold: " + gold);
    }
}



