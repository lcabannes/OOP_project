package com.li.oopproject;

import com.li.oopproject.entities.*;

import java.util.ArrayList;

public class Collision {

    private GoldSystem goldSystem;

    private Board board;

    public Collision(Board board) {
        this.board = board;
        this.goldSystem = board.getGoldSystem();
    }

    // Main method to check for all types of collisions
    public void checkCollisions() {
        Board.Tile[][] tiles = board.getTiles();
        int length = Board.length;
        int height = Board.height;

        for (int row = 0; row < height; row++) {
            for (int col = 0; col < length; col++) {
                Board.Tile tile = tiles[row][col];
                checkProjectileCollisions(tile, row, col);
                checkHumanCollisions(tile);
            }
        }
    }

    // Method to check for projectile collisions
    private void checkProjectileCollisions(Board.Tile tile, int row, int col) {
        int i = 0;
        while (i < tile.projectiles.size()) {
            Projectile projectile = tile.projectiles.get(i);
            if (checkProjectileAlienCollision(tile, projectile, row, col)) {
                i--;
            }
            i++;
        }
    }

    // Helper method to handle projectile-alien collisions
    private boolean checkProjectileAlienCollision(Board.Tile tile, Projectile projectile, int row, int col) {
        ArrayList<Alien> aliensToConsider = new ArrayList<>(tile.aliens);
        if (col + 1 < Board.length) {
            aliensToConsider.addAll(board.getTiles()[row][col + 1].aliens);
        }

        for (Alien alien : aliensToConsider) {
            // Check if the alien is a GhostAlien and the projectile is a Bullet
            if (alien instanceof GhostAlien && !(projectile instanceof Laser)) {
                continue; // Skip collision check for Bullet and GhostAlien
            }

            if (isProjectileHittingAlien(projectile, alien)) {
                handleProjectileAlienImpact(projectile, alien);
                return true; // Collision occurred
            }
        }
        return false; // No collision
    }

    // Method to determine if a projectile is hitting an alien
    public boolean isProjectileHittingAlien(Projectile projectile, Alien alien) {
        float distance = alien.getxPos() - projectile.getxPos();
        return Math.abs(distance) < projectile.getLength();
    }

    // Method to handle the impact of a projectile on an alien
    protected void handleProjectileAlienImpact(Projectile projectile, Alien alien) {
        alien.reduceHp(projectile.getDamage());
        if (projectile instanceof IceLaser iceLaser) {
            alien.reduceSpeed(iceLaser.getSlowdownEffect(), iceLaser.getSlowdownDuration());
        }
        if (!alien.isAlive()) {
            board.removeEntity(alien);
            goldSystem.addGold(goldSystem.getGoldForAlienType(alien));
        }
        projectile.reduceHp(1);
        if (!projectile.isAlive()) {
            board.removeEntity(projectile);
        }
    }

    // Method to check for human collisions
    private void checkHumanCollisions(Board.Tile tile) {
        Human human = tile.human;
        if (human != null) {
            for (Alien alien : tile.aliens) {
                if (isHumanHittingAlien(human, alien)) {
                    handleHumanAlienCollision(human, alien, tile);
                }
            }
        }
    }

    // Method to determine if a human is hitting an alien
    public boolean isHumanHittingAlien(Human human, Alien alien) {
        return Math.abs(alien.getxPos() - human.getxPos()) < human.getLength();
    }

    // Method to handle the collision between a human and an alien
    private void handleHumanAlienCollision(Human human, Alien alien, Board.Tile tile) {
        alien.Attack(human);
        if (!human.isAlive()) {
            board.removeEntity(human);
            tile.human = null;
        }
    }

}

