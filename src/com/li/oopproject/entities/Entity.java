package com.li.oopproject.entities;
import com.li.oopproject.Board;

import java.awt.image.BufferedImage;

public abstract class Entity {
    //Class Entity variables
    private int damage;
    private int hp;
    private Board board;
    private int length = 80;
    private int height = 80;
    public void setClassIcon(BufferedImage classIcon) {
        this.classIcon = classIcon;
    }
    private BufferedImage classIcon; // each entity class must have a picture

    public BufferedImage getClassIcon() {
        return classIcon;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getLength() {
        return length;
    }

    public int getHeight() {
        return height;
    }

    public void setxPos(int xPos) {
        this.xPos = xPos;
    }

    public void setyPos(int yPos) {
        this.yPos = yPos;
    }

    public int getyPos() {
        return yPos;
    }

    private int xPos = 0;
    private int yPos = 0;

    public Entity(int hp, int damage, Board board){
        this.damage = damage;
        this.hp = hp;
        this.board = board;
    }

    // Getter for damage
    public int getDamage() {
        return damage;
    }

    // Getter for hp
    public int getHp() {
        return hp;
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

    public int getxPos(){return this.xPos;}
    //Action mode

    public boolean isAlive(){
        return hp>0;
    }
}
