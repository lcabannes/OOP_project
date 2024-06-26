package com.li.oopproject;

import com.li.oopproject.entities.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
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
    private BufferedImage winImage;
    private BufferedImage loseImage;

    public GameInterface(Game game) {
        this.game = game;
        this.goldSystem = game.getBoard().getGoldSystem();

        // small extension to the frame to account for some unknown offset probably due to frame/button borders
        setSize(WINDOWLENGTH+15, WINDOWHEIGHT+40);
        // use a layered Pane to deal with the different layers of panels
        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(new Dimension(WINDOWLENGTH, WINDOWHEIGHT));
        setLocationRelativeTo(null);

        initializeSelectionButtons();

        // button panels contains transparent buttons and one additional row to choose which human to select to place
        // TODO: top selection row
        buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(Board.height+1, Board.length));
        buttonPanel.setBackground(new Color(0, 0, 0, 0));
        buttonPanel.setOpaque(false);
        initializeButtonPanel();

        // background panel to hold the background image
        backGroundPanel = new JPanel();

        // try to load the backGround image
        loadBackgroundImage();

        // Initialize the game info panel
        JPanel gameInfoPanel = initializeGameInfoPanel();

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
        // set
        setSpecialCloseOperation();


        setVisible(true);

    }

    public void setSpecialCloseOperation(){
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                game.getBaseTimer().stop();
                System.out.println("game exited");
                dispose();
                // Optionally, you can still call System.exit or dispose the frame
                // System.exit(0); // to exit the application
                // frame.dispose(); // to just close the window
            }
        });
    }
    public void loadBackgroundImage(){
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
            System.out.println("No background file found");
        }
    }

    // Method to load image when game won
    private void loadWinImage() {
        try {
            String path = GameInterface.class.getProtectionDomain().
                    getCodeSource().getLocation().getPath() + "com/li/oopproject/assets/GameState/YouWon.png";
            BufferedImage originalImage = ImageIO.read(new File(path));

            // Desired dimensions
            int desiredWidth = 800;
            int desiredHeight = 600;

            // Resize the image while maintaining aspect ratio
            Image resizedImage = originalImage.getScaledInstance(desiredWidth, desiredHeight, Image.SCALE_SMOOTH);
            BufferedImage bufferedResizedImage = new BufferedImage(desiredWidth, desiredHeight, BufferedImage.TYPE_INT_ARGB);

            bufferedResizedImage.getGraphics().drawImage(resizedImage, 0, 0, null);

            // Assign the resized image to winImage
            winImage = bufferedResizedImage;
        } catch (IOException e) {
            System.out.println("Error loading win image: " + e.getMessage());
        }
    }

    // Method to load image when game lose
    private void loadLoseImage() {
        try {
            String path = GameInterface.class.getProtectionDomain().
                    getCodeSource().getLocation().getPath() + "com/li/oopproject/assets/GameState/YouLost.png";
            loseImage = ImageIO.read(new File(path));
        } catch (IOException e) {
            System.out.println("Error loading lose image: " + e.getMessage());
        }
    }

    // Method to display win/lose image when game ends
    public void displayEndGameImage(boolean isWin) {
        loadWinImage();
        loadLoseImage();
        BufferedImage image = isWin ? winImage : loseImage;
        if (image != null) {
            SwingUtilities.invokeLater(() -> {
                JLabel imageLabel = new JLabel(new ImageIcon(image));
                imageLabel.setHorizontalAlignment(JLabel.CENTER);
                foreGroundPanel.removeAll(); // Clear existing components

                // Set the size and position of the imageLabel to cover the foreGroundPanel
                imageLabel.setBounds(0, 0, WINDOWLENGTH, WINDOWHEIGHT);

                foreGroundPanel.add(imageLabel);
                foreGroundPanel.revalidate();
                foreGroundPanel.repaint();
            });
        } else {
            System.out.println("End game image is null.");
        }
    }

    // Method to set game info panel
    public JPanel initializeGameInfoPanel(){
        JPanel gameInfoPanel = new JPanel();
        gameInfoPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        int panelWidth = 200;
        int panelHeight = 30;
        int rightMargin = 5;
        int topMargin = 10;

        // Set bounds to position it at the top right corner
        gameInfoPanel.setBounds(WINDOWLENGTH - panelWidth - rightMargin, topMargin, panelWidth, panelHeight);

        // Create a Font object for the labels
        Font labelFont = new Font("Arial", Font.BOLD, 14);

        // Initialize labels for wave, HP, and gold
        waveLabel = new JLabel("Wave: 0");
        hpLabel = new JLabel("HP: 3");
        goldLabel = new JLabel("Gold: 100");

        // Set the font for all labels
        setFontForLabels(labelFont, waveLabel, hpLabel, goldLabel);

        // Add labels to the panel
        gameInfoPanel.add(waveLabel);
        gameInfoPanel.add(hpLabel);
        gameInfoPanel.add(goldLabel);
        return gameInfoPanel;
    }

    // Method to set all buttons in game window
    public void initializeSelectionButtons(){
        for (int xpos = 0; xpos<Board.length; xpos++){
            Entity selectionButton = null;
            switch(xpos){
                case 0:
                    selectionButton = new Gunner(game.getBoard());
                    break;
                case 1:
                    selectionButton = new GhostBuster(game.getBoard());
                    break;
                case 2:
                    selectionButton = new Freezer(game.getBoard());
                    break;
                case 3:
                    selectionButton = new Tank(game.getBoard());
                    break;
                case 4:
                    selectionButton = new Undeploy(game.getBoard());
                    break;
                case 5:
                    selectionButton = new Upgrade(game.getBoard());
                    break;
            }
            if (selectionButton != null){
                selectionButton.setyPos(-TILESIZE);
                selectionButton.setxPos(xpos * TILESIZE);
                displayedEntities.add(selectionButton);
            }
        }

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

    // Method to handle mouse event with button panel
    public void initializeButtonPanel(){
        for (int i=0; i< Board.height+1; i++){
            for (int j=0; j<Board.length; j++){
                // use effectively final variables to allow the actionListener to know its position
                final int finalI = i;
                final int finalJ = j;
                JButton button = new JButton();
                button.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (finalI == 0) {
                            topRowButtonLogic(finalI, finalJ);
                        }
                        else {
                            boardButtonLogic(finalI, finalJ);
                        }
                    }
                });

                // button tracking logic
                button.addMouseMotionListener(new MouseMotionListener() {
                    @Override
                    public void mouseDragged(MouseEvent e) {
                        if (clickedIcon != null){
                            // -40 offset to make the icon centered on the mouse
                            clickedIcon.setyPos(e.getY() + ((finalI-1) * TILESIZE)-40);
                            clickedIcon.setxPos(e.getX() + (finalJ * TILESIZE)-40);
                        }
                    }
                    // the finalI-1 comes from the fact that we are giving position to entities based on the
                    // board but there is an additional row of buttons in the graphical interface
                    // so the indexation is different
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
    }

    public void topRowButtonLogic(int finalI, int finalJ){
        // Top row button logic for selecting an entity
        displayedEntities.remove(clickedIcon);
        clickedIcon = null;

        switch(finalJ) {
            case 0: // Gunner button
                clickedIcon = new Gunner(game.getBoard());
                break;
            case 1: // GhostBuster button
                clickedIcon = new GhostBuster(game.getBoard());
                break;
            case 2: // Freezer button
                clickedIcon = new Freezer(game.getBoard());
                break;
            case 3: // Tank button
                clickedIcon = new Tank(game.getBoard());
                break;
            case 4: // Undeploy button
                clickedIcon = new Undeploy(game.getBoard());
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

    public void  boardButtonLogic(int finalI, int finalJ){
        // If in Undeploy mode and a human is clicked, remove it
        // when clicked try to place a human at this position, message the user about the outcome
        if (clickedIcon == null){
            return;
        }
        if (clickedIcon instanceof Human){
            if (game.placeHuman((Human) clickedIcon, (finalI - 1), finalJ, goldSystem) & Game.VERBOSE) {
                System.out.println("Successfully place a turret at: " + (finalI - 1) + " " + finalJ);
            } else if (Game.VERBOSE){
                System.out.println("You cannot place this turret here");
            }
        }
        else if (clickedIcon instanceof Undeploy){
            Human human = game.getBoard().tiles[finalI - 1][finalJ].human;
            if (human != null) {
                game.getBoard().removeEntity(human);
                if (Game.VERBOSE) {
                    System.out.println("Human removed from: " + (finalI - 1) + " " + finalJ);
                }
            }
        }
        else if (clickedIcon instanceof Upgrade){
            Human human = game.getBoard().tiles[finalI-1][finalJ].human;
            if (human != null && (!human.isUpgraded()) & ((Upgrade) clickedIcon).getGoldCost() <= goldSystem.getGold()) {
                human.upgrade();
                goldSystem.addGold(-((Upgrade) clickedIcon).getGoldCost());
                if (Game.VERBOSE){
                    System.out.println("Successfully upgraded a turret at: " + (finalI - 1) + " " + finalJ);
                }
            } else if (Game.VERBOSE){
                System.out.println("You cannot upgrade this turret");
            }
        }
        displayedEntities.remove(clickedIcon);
        clickedIcon = null;
    }
    public void display(){
        // Check if the game is won or over
        if (game.isGameWon() || game.isGameOver()) {
            displayEndGameImage(game.isGameWon());
            return; // Exit early as we don't need to render other entities
        }

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
                imageLabel.setBounds((int)(entity.getxPos())+10, entity.getyPos()+TILESIZE+15, entityIcon.getWidth(), entityIcon.getHeight());
            } else {
                System.out.println("Failed to Entity load the image.");
              //  System.exit(1);
            }
            i++;
        }
        addCostLabels(); // Display the  cost labels
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

    // Keep updating game info during the game
    public void updateGameInfo(int wave, int hp, int gold) {
        waveLabel.setText("Wave: " + wave);
        hpLabel.setText("HP: " + hp);
        goldLabel.setText("Gold: " + gold);
    }

    // Match the cost labels with buttons
    private void addCostLabels() {
        // Assuming you have a method getCost() in your Human classes
        addCostLabelForButton(new Gunner(game.getBoard()), 0);
        addCostLabelForButton(new GhostBuster(game.getBoard()), 1);
        addCostLabelForButton(new Freezer(game.getBoard()), 2);
        addCostLabelForButton(new Tank(game.getBoard()), 3);
        addCostLabelForButton(new Undeploy(game.getBoard()), 4);
        addCostLabelForButton(new Upgrade(game.getBoard()), 5);
    }

    // Create cost label for each human, upgrade, undeploy button
    private void addCostLabelForButton(Object object, int buttonPosition) {
        int goldCost;
        String labelText;

        // Check the type of the object and cast it to access getGoldCost
        if (object instanceof Human) {
            goldCost = ((Human) object).getGoldCost();
            labelText = "Cost: " + goldCost; // Standard cost message
        } else if (object instanceof Upgrade) {
            goldCost = ((Upgrade) object).getGoldCost();
            labelText = "Cost: " + goldCost; // Standard cost message
        } else if (object instanceof Undeploy) {
            goldCost = ((Undeploy) object).getGoldCost();
            labelText = "Undeploy: " + goldCost; // Specific message for undeploy
        } else {
            throw new IllegalArgumentException("Unsupported object type");
        }

        JLabel costLabel = new JLabel(labelText);
        costLabel.setFont(new Font("Arial", Font.BOLD, 12));
        costLabel.setForeground(Color.WHITE); // Set the text color

        int labelWidth = TILESIZE;
        int labelHeight = 30;
        int labelX = buttonPosition * TILESIZE + 20;
        int labelY = TILESIZE - labelHeight + 20; // Position under the button

        costLabel.setBounds(labelX, labelY, labelWidth, labelHeight);
        foreGroundPanel.add(costLabel);
    }
}





