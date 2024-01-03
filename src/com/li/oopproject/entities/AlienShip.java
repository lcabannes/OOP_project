package com.li.oopproject.entities;

import com.li.oopproject.Board;
import com.li.oopproject.GameInterface;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class AlienShip extends Alien {

    private final static int maxHP = 150;
    private final static int speed = 1;
    private final static int damage = 100;
    private final static int reloadTime = 2000;

    private static final int length = 80;
    private static final int height = 80;
    private final static BufferedImage classIcon;
    static {
        BufferedImage classIcon1;
        String path = GameInterface.class.getProtectionDomain().
                getCodeSource().getLocation().getPath() + "com/li/oopproject/assets/Aliens/AlienShip.png";
        try{
            classIcon1 = GameInterface.resizeImage(ImageIO.read(new File(path)), length, height);
        }
        catch (IOException e) {
            System.out.println("No image file found for AlienShip");
            classIcon1 = null;
        }
        classIcon = classIcon1;
    }

    public AlienShip(Board board){
        super(maxHP, damage, board, speed, reloadTime);
        setInstanceIcon(AlienShip.classIcon);
    }
    }
