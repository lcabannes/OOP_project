package com.li.oopproject.entities;

import com.li.oopproject.Board;
import com.li.oopproject.GameInterface;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Laser extends Projectile{

    public Laser(int damage, Board board){
        super(1, damage, 5, board);
        setInstanceIcon(Laser.classIcon);
    }
    private static final int length = 50;
    private static final int height = 25;
    private final static BufferedImage classIcon;
    // this initialization block, like all the others, tries to find the image corresponding to the laser
    static {
        BufferedImage classIcon1;
        String path = GameInterface.class.getProtectionDomain().
                getCodeSource().getLocation().getPath() + "com/li/oopproject/assets/Projectiles/Laser.jpg";
        try{
            classIcon1 = GameInterface.resizeImage(ImageIO.read(new File(path)), length, height);
        }
        catch (IOException e) {
            System.out.println("No image file found for Laser");
            classIcon1 = null;
        }
        classIcon = classIcon1;
    }
    public int getLength(){
        return length;
    }

}


