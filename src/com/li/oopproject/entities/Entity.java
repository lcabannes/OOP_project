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
    public void setInstanceIcon(BufferedImage Icon) {
        this.instanceIcon = Icon;
    }
    private BufferedImage instanceIcon; // each entity has its own current icon

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

