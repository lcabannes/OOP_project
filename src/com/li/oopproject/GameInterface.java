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

public class GameInterface extends JFrame{
    JPanel gameScreen;

    public GameInterface() {
        setSize(640, 640);

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        this.gameScreen = new JPanel();
        this.gameScreen.setLayout(new GridLayout(5, 8));
        this.gameScreen.setBackground(Color.BLUE);

        this.add(this.gameScreen);
        try {
            BufferedImage myPicture = ImageIO.read(new File(GameInterface.class.getProtectionDomain().getCodeSource().getLocation().getPath() + "/assets/Aliens/alien_spacecraft.png"));
            JLabel picLabel = new JLabel(new ImageIcon(myPicture));
            this.add(picLabel);
        }
        catch (IOException e) {
            System.out.println("No file found");
            return;
        }

        setVisible(true);

    }
}
