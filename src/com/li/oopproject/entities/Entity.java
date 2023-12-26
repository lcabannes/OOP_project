package com.li.oopproject.entities;
import com.li.oopproject.Board;

import java.awt.image.BufferedImage;

public abstract class Entity {
    //Class Entity variables
    private static boolean isHuman = false;
    private static boolean isAlien = false;
    private static boolean isGhost = false;
    private int damage;
    private int hp;
    private Board board;

    public BufferedImage classIcon; // each entity class must have a picture

    public void setxPos(int xPos) {
        this.xPos = xPos;
    }

    public void setyPos(int yPos) {
        this.yPos = yPos;
    }

    public int getyPos() {
        return yPos;
    }

    private int xPos;
    private int yPos;

    public Entity(int damage, int hp, Board board){
        this.damage = damage;
        this.hp = hp;
        this.board = board;
    }

    // Getter for isHuman
    public static boolean isHuman() {
        return isHuman;
    }

    // Getter for isAlien
    public static boolean isAlien() {
        return isAlien;
    }

    // Getter for isGhost
    public static boolean isGhost() {
        return isGhost;
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
        return className + "(" + hp + ", " + board + ")";
    }

    public int getxPos(){return this.xPos;}
    //Action mode
}
