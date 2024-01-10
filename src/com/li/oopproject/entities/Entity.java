package com.li.oopproject.entities;
import com.li.oopproject.Board;
import com.li.oopproject.GameInterface;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public abstract class Entity {
    //Class Entity variables
    private int damage;
    private int hp;
    private Board board;
    private int length = 80;
    private int height = 80;
    public void setInstanceIcon(BufferedImage Icon) {
        this.instanceIcon = Icon;
    }
    private BufferedImage instanceIcon; // each entity has its own current icon
    private BufferedImage prevInstanceIcon;
    public static final BufferedImage damagedIcon; // each entity has its own current icon
    private int timeUntilReset = 60;



    static {
        BufferedImage tempIcon = null;
        try{
            tempIcon = ImageIO.read(new File(GameInterface.class.getProtectionDomain().
                    getCodeSource().getLocation().getPath() + "com/li/oopproject/assets/damagedIcon.png"));
            tempIcon = GameInterface.resizeImage(tempIcon, 80, 80);
        }
        catch (IOException e) {
            System.out.println("No image file found for damagedIcon");

        }
        damagedIcon = tempIcon;
    }

    public void setxPos(float xPos) {
        this.xPos = xPos;
    }

    public void setyPos(int yPos) {
        this.yPos = yPos;
    }

    public int getyPos() {
        return yPos;
    }

    private float xPos = 0;
    private int yPos = 0;

    public Entity(int hp, int damage, Board board){
        this.damage = damage;
        this.hp = hp;
        this.board = board;
    }

    public void resetIcon(int timeElapsed){
        timeUntilReset -=  timeElapsed;
        if (timeUntilReset <= 0){
            timeUntilReset = 60;
            setInstanceIcon(prevInstanceIcon);
        }
    }

    // Getter for damage
    public int getDamage() {
        return damage;
    }

    // Getter for board
    public Board getBoard() {
        return this.board;
    }

    // Setter for damage
    public void setDamage(int damage) {
        this.damage = damage;
    }

    // Setter for hp
    public void setHp(int hp) {
        this.hp = hp;
    }

    public void setBoard(Board board){
        this.board = board;
    }

    // Method to reduce hp
    public void reduceHp(int amount) {
        this.prevInstanceIcon = this.instanceIcon;
        this.instanceIcon = Entity.damagedIcon;
        this.hp -= amount;
        if (hp <= 0) {
            board.removeEntity(this);
        }
    }

    // toString method
    @Override
    public String toString() {
        String className = getClass().getSimpleName();
        return className + " (hp: " + hp + ")";
    }

    public float getxPos(){return this.xPos;}
    //Action mode

    public boolean isAlive(){
        return hp>0;
    }

    public BufferedImage getInstanceIcon() {
        return instanceIcon;
    }
}

