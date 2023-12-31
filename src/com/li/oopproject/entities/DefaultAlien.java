package com.li.oopproject.entities;

import com.li.oopproject.Board;
import com.li.oopproject.GameInterface;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class DefaultAlien extends Alien{
    private final static int maxHP = 30;
    private final static int speed = 1;
    private final static int damage = 10;
    private final static int reloadTime = 1000;
    private static final int length = 80;
    private static final int height = 80;
    private static BufferedImage classIcon;

    static {
        try{
            DefaultAlien.classIcon = ImageIO.read(new File(GameInterface.class.getProtectionDomain().
                    getCodeSource().getLocation().getPath() + "com/li/oopproject/assets/Aliens/DefaultAlien.png"));
            DefaultAlien.classIcon = GameInterface.resizeImage(DefaultAlien.classIcon, length, height);
        }
        catch (IOException e) {
            System.out.println("No image file found for DefaultAlien");
        }
    }

    public DefaultAlien(Board board){
        super(maxHP, damage, board, speed, reloadTime);
        setInstanceIcon(DefaultAlien.classIcon);
    }
}
