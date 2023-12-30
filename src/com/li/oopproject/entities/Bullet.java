package com.li.oopproject.entities;

import com.li.oopproject.Board;
import com.li.oopproject.GameInterface;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class Bullet extends Projectile{
    // the hps of a bullet is how many times it can collide before dying

    // this initialization block, like all the others, tries to find the image corresponding to the laser
    {
        try{
            this.setClassIcon(ImageIO.read(new File(GameInterface.class.getProtectionDomain().
                    getCodeSource().getLocation().getPath() + "com/li/oopproject/assets/Projectiles/Bullet.png")));
            this.setClassIcon(GameInterface.resizeImage(this.getClassIcon(), getLength(), getHeight()));
        }
        catch (IOException e) {
            System.out.println("No image file found for Bullet");
        }
    }
    public Bullet(int damage, Board board){
        super(1, damage, 5, board);
    }
}
