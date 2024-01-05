package com.li.oopproject.entities;

import com.li.oopproject.Board;
import com.li.oopproject.GameInterface;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class GhostBuster extends Human{
    public static final int GOLD_COST = 50;
    private final static int reloadTime = 2000;  // reload delay in ms
    private final static int maxHP = 100;
    private final static int damage = 10;

    private static BufferedImage classIcon;

    private static BufferedImage upgradedClassIcon;

    static {
        try{
            GhostBuster.classIcon = ImageIO.read(new File(GameInterface.class.getProtectionDomain().
                    getCodeSource().getLocation().getPath() + "com/li/oopproject/assets/Humans/GhostBuster.png"));
            GhostBuster.classIcon = GameInterface.resizeImage(GhostBuster.classIcon, getLength(), getHeight());
        }
        catch (IOException e) {
            System.out.println("No image file found for GhostBuster");
        }

        String path = GameInterface.class.getProtectionDomain().
                getCodeSource().getLocation().getPath() + "com/li/oopproject/assets/Humans/GhostBusterUpgraded.png";
        try{
            upgradedClassIcon = GameInterface.resizeImage(ImageIO.read(new File(path)), getLength(), getHeight());
        }
        catch (IOException e) {
            System.out.println("No image file found for upgraded GhostBuster");
            upgradedClassIcon = null;
        }
    }

    public GhostBuster(Board board){
        super (maxHP, damage, board, reloadTime);
        setInstanceIcon(GhostBuster.classIcon);
        setGoldCost(GOLD_COST);
    }

    @Override
    public void upgrade(){
        super.upgrade();
        setDamage(getDamage() * 3); // 3 times damage if GhostBuster is upgraded
        System.out.println(this.getDamage());
        setInstanceIcon(upgradedClassIcon);
    }

    public Projectile attack(){
        this.setReloadTimeRemaining(reloadTime);

        Projectile laser = new Laser(getDamage(), this.getBoard());
        laser.setxPos(this.getxPos()+getLength());
        laser.setyPos(this.getyPos());
        return laser;
    }

}

