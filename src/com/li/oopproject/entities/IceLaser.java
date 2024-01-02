package com.li.oopproject.entities;

import com.li.oopproject.Board;
import com.li.oopproject.GameInterface;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class IceLaser extends Projectile{
    private float slowdownEffect; // The amount by which the Alien's speed is reduced
    private long slowdownDuration; // The duration for which the slowdown effect lasts

    public IceLaser(int damage, float slowdownEffect, long slowdownDuration, Board board){
        super(1, damage, 5, board);
        this.slowdownEffect = slowdownEffect;
        this.slowdownDuration = slowdownDuration;
        setInstanceIcon(IceLaser.classIcon);
    }

    private static final int length = 50;
    private static final int height = 25;
    private final static BufferedImage classIcon;
    // this initialization block, like all the others, tries to find the image corresponding to the laser
    static {
        BufferedImage classIcon1;
        String path = GameInterface.class.getProtectionDomain().
                getCodeSource().getLocation().getPath() + "com/li/oopproject/assets/Projectiles/IceLaser.png";
        try{
            classIcon1 = GameInterface.resizeImage(ImageIO.read(new File(path)), length, height);
        }
        catch (IOException e) {
            System.out.println("No image file found for IceLaser");
            classIcon1 = null;
        }
        classIcon = classIcon1;
    }
    public int getLength(){
        return length;
    }

    // Getters for the slowdown effect, duration
    public float getSlowdownEffect() {
        return slowdownEffect;
    }

    public long getSlowdownDuration() {
        return slowdownDuration;
    }

}