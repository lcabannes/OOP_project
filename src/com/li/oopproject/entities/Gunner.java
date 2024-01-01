package com.li.oopproject.entities;

import com.li.oopproject.Board;
import com.li.oopproject.GameInterface;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Gunner extends Human{

    public static final int GOLD_COST = 50;
    private final static int reloadTime = 2000;  // reload delay in ms

    private final static int maxHP = 100;
    private final static int damage = 10;

    private final static BufferedImage classIcon;
    private static BufferedImage upgradedClassIcon;

    static {
        BufferedImage classIcon1;
        String path = GameInterface.class.getProtectionDomain().
                getCodeSource().getLocation().getPath() + "com/li/oopproject/assets/Humans/Gunner.png";
        try{
            classIcon1 = GameInterface.resizeImage(ImageIO.read(new File(path)), getLength(), getHeight());
        }
        catch (IOException e) {
            System.out.println("No image file found for Gunner");
            classIcon1 = null;
        }
        classIcon = classIcon1;

        path = GameInterface.class.getProtectionDomain().
                getCodeSource().getLocation().getPath() + "com/li/oopproject/assets/Humans/upgradedGunner.png";
        try{
            upgradedClassIcon = GameInterface.resizeImage(ImageIO.read(new File(path)), getLength(), getHeight());
        }
        catch (IOException e) {
            System.out.println("No image file found for upgraded Gunner");
            upgradedClassIcon = null;
        }
    }

    public Gunner(Board board){
        super (maxHP, damage, board, reloadTime);
        setInstanceIcon(Gunner.classIcon);
        setGoldCost(GOLD_COST);
    }
    @Override
    public void upgrade(){
        super.upgrade();
        setInstanceIcon(upgradedClassIcon);
    }

    public Projectile attack(){
        this.setReloadTimeRemaining(reloadTime);
        Projectile bullet = new Bullet(damage, this.getBoard());
        if (isUpgraded()){
            setReloadTimeRemaining(500);
        }
        bullet.setxPos(this.getxPos()+50);
        bullet.setyPos(this.getyPos());
        return bullet;
    }

}

