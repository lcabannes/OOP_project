package com.li.oopproject.entities;

import com.li.oopproject.Board;
import com.li.oopproject.GameInterface;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Upgrade extends Consumable {

    public static final int GOLD_COST = 400;
    private static final int length = 80;
    private static final int height = 80;
    private final static BufferedImage classIcon;

    static {
        BufferedImage classIcon1;
        String path = GameInterface.class.getProtectionDomain().
                getCodeSource().getLocation().getPath() + "com/li/oopproject/assets/Consumables/Upgrade.png";
        try{
            classIcon1 = GameInterface.resizeImage(ImageIO.read(new File(path)), length, height);
        }
        catch (IOException e) {
            System.out.println("No image file found for upgrade-icon");
            classIcon1 = null;
        }
        classIcon = classIcon1;
    }
    public Upgrade(Board board){
        super(board);
        setInstanceIcon(Upgrade.classIcon);
    }

    public int getGoldCost(){
        return GOLD_COST;
    }
}
