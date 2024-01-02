package com.li.oopproject.entities;

import com.li.oopproject.Board;
import com.li.oopproject.GameInterface;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Freezer extends Human{

    public static final int GOLD_COST = 50;
    private final static int reloadTime = 2000;  // reload delay in ms

    private final static int maxHP = 100;
    private final static int damage = 10;

    private final static BufferedImage classIcon;
    private final static BufferedImage upgradedClassIcon;



    static {
        BufferedImage classIcon1;
        String path = GameInterface.class.getProtectionDomain().
                getCodeSource().getLocation().getPath() + "com/li/oopproject/assets/Humans/Freezer.png";
        try{
            classIcon1 = GameInterface.resizeImage(ImageIO.read(new File(path)), getLength(), getHeight());
        }
        catch (IOException e) {
            System.out.println("No image file found for Freezer");
            classIcon1 = null;
        }
        classIcon = classIcon1;


        path = GameInterface.class.getProtectionDomain().
                getCodeSource().getLocation().getPath() + "com/li/oopproject/assets/Humans/upgradedFreezer.png";
        BufferedImage upgradedClassIcon1;
        try{
            upgradedClassIcon1 = GameInterface.resizeImage(ImageIO.read(new File(path)), getLength(), getHeight());
        }
        catch (IOException e) {
            System.out.println("No image file found for upgraded Freezer");
            upgradedClassIcon1 = null;
        }
        upgradedClassIcon = upgradedClassIcon1;
    }

    public Freezer(Board board){
        super (maxHP, damage, board, reloadTime);
        setInstanceIcon(Freezer.classIcon);
        setGoldCost(GOLD_COST);
    }
    @Override
    public void upgrade(){
        super.upgrade();
        setInstanceIcon(upgradedClassIcon);
    }

    @Override
    public Projectile attack(){
        this.setReloadTimeRemaining(reloadTime);

        // Determine the slowdown effect based on whether the Freezer is upgraded
        float slowdownEffect = isUpgraded() ? 100.0F : 0.75F;

        // Create a new IceLaser with the appropriate slowdown effect
        Projectile iceLaser = new IceLaser(damage, slowdownEffect, 1000, this.getBoard());

        if (isUpgraded()){
            setReloadTimeRemaining(1000); // Optionally adjust the reload time for the upgraded state
        }

        iceLaser.setxPos(this.getxPos() + 50);
        iceLaser.setyPos(this.getyPos());
        return iceLaser;
    }


}