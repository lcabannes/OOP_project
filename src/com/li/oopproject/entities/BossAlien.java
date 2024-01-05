package com.li.oopproject.entities;

import com.li.oopproject.Board;
import com.li.oopproject.GameInterface;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class BossAlien extends Alien {

    private int maxHP;
    private final static int speed = 1;
    private final static int damage = 100;
    private final static int reloadTime = 2500;
    private static final int length = 150;
    private static final int height = 150;
    private final static BufferedImage classIcon;
    static {
        BufferedImage classIcon1;
        String path = GameInterface.class.getProtectionDomain().
                getCodeSource().getLocation().getPath() + "com/li/oopproject/assets/Aliens/BossAlien.png";
        try{
            classIcon1 = GameInterface.resizeImage(ImageIO.read(new File(path)), length, height);
        }
        catch (IOException e) {
            System.out.println("No image file found for BossAlien");
            classIcon1 = null;
        }
        classIcon = classIcon1;
    }

    public BossAlien(Board board, int hp){
        super(hp, damage, board, speed, reloadTime);
        this.maxHP = hp; // Set the HP based on the provided value
        setInstanceIcon(BossAlien.classIcon);
    }
}

