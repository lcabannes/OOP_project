package com.li.oopproject;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import com.li.oopproject.entities.*;


public class Board {
    private String name;
    Tile[][] tiles = new Tile[height][length];
    private Tile entrance;
    private Tile exit;
    private Human human;
    public static final int length = 8; // fixed length for all Boards
    public static final int height = 5; // fixed height for all board
    private final Game game;
    private Collision collision;//check collision

    private final GoldSystem goldSystem = new GoldSystem();

    public Board(Game game) {
        this.name = "default_board";
        this.game = game;
        for (int row = 0; row < Board.height; row++) {
            for (int col = 0; col < Board.length; col++) {
                this.tiles[row][col] = new Tile();
            }
        }
        this.collision = new Collision(this); // Initializing the Collision object
    }

    // Add getter methods for tiles and game if not already present
    public Tile[][] getTiles() {
        return this.tiles;
    }


    public class Tile { // Tile local class is aggregated by a Board, a tile contains at most 1 human and possibly many Aliens
        ArrayList<Alien> aliens;
        Human human;

        ArrayList<Projectile> projectiles;

        public Tile() { // Aliens are stored in each tile
            this.aliens = new ArrayList<Alien>();
            this.human = null;
            this.projectiles = new ArrayList<Projectile>();

        }
    }

    // Place human when not human in current tile with enough gold
    // Cannot place if already human on tile or without enough gold
    public boolean placeHuman(Human human, int row, int col, GoldSystem goldSystem) {

        int goldCost = human.getGoldCost();

        if (goldSystem.getGold() >= goldCost) {
            if (tiles[row][col].human != null) {
                return false;
            }
            human.setxPos(col * 100);
            human.setyPos(row * 100);
            tiles[row][col].human = human;
            goldSystem.addGold(-goldCost); // Deduct gold
            System.out.println("Gold after deduct: " + goldSystem.getGold());
            return true;
        } else {
            System.out.println("Not enough gold to place this human");
            return false;
        }
    }

    public GoldSystem getGoldSystem() {
        return this.goldSystem;
    }

    /**
     * check for every tile to find the entity to erase
     *
     */
    public void removeEntity(Entity entity) {
        game.getGameInterface().removeEntity(entity);
        // search for said entity in all tiles and all entity fields of those tiles, stop when we find it
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < length; col++) {
                Tile tile = this.tiles[row][col];
                if (tile.human == entity) {
                    tile.human = null;
                    return;
                }
                for (Projectile projectile : tile.projectiles) {
                    if (projectile == entity) {
                        tile.projectiles.remove(projectile);
                        return;
                    }
                }
                for (Alien alien : tile.aliens) {
                    if (alien == entity) {
                        tile.aliens.remove(alien);
                        return;
                    }
                }

            }
        }
    }

    // Spawn different types of alien
    public Alien spawnAlien(String alienName, int yposition) {
        Alien newAlien;
        switch (alienName) {
            // Spawn different type of Aliens
            case "DefaultAlien":
                newAlien = new DefaultAlien(this);
                break;
            case "OctopusAlien":
                newAlien = new OctopusAlien(this);
                break;
            case "GhostAlien":
                newAlien = new GhostAlien(this);
                break;
            case "AlienShip":
                newAlien = new AlienShip(this);
                break;
            default:
                System.out.println("Tried to spawn unknown alien name");
                newAlien = new DefaultAlien(this);
        }
        newAlien.setxPos(Board.length * 100);
        newAlien.setyPos(yposition * 100);
        this.tiles[yposition][Board.length - 1].aliens.add(newAlien);
        return newAlien;
    }

    // check each tile, if it has aliens then print "A"
    public void display() {
        System.out.println("Current Board:");
        for (int row = 0; row < Board.height; row++) {
            for (int col = 0; col < Board.length; col++) {
                if (this.tiles[row][col].human != null) {
                    System.out.printf("|H");
                } else if (this.tiles[row][col].aliens.isEmpty()) {
                    System.out.printf("|_");
                } else {
                    System.out.printf("|A");
                }
            }
            System.out.printf("|\n");
        }
    }

    public int updateEntities(int elapsedTime) {
        // this method returns how many aliens have passed the final line after moving this update
        // it also reloads all the humans guns
        int hpLost = 0;

        // Check for collisions at the beginning of each update
        collision.checkCollisions();

        for (int row = 0; row < Board.height; row++) {
            for (int col = 0; col < Board.length; col++) {
                Tile tile = tiles[row][col];
                // if there is a human in this tile, reload the human
                if (tile.human != null) {
                    tile.human.reload(elapsedTime);
                    if (tile.human.getReloadTimeRemaining() <= 0 & (!noAlien(row))) {
                        Projectile projectile = tile.human.attack();
                        tile.projectiles.add(projectile);
                        game.getGameInterface().addEntity(projectile);
                    }
                }
                // make all the aliens move
                int i = 0;
                while (i < tile.aliens.size()) {
                    Alien alien = tile.aliens.get(i);
                    if (tile.human != null && collision.isHumanHittingAlien(tile.human, alien)){
                        alien.reload(elapsedTime);
                        i++;
                        continue;
                    }
                    alien.move();
                    // if an Alien's position is negative it means it breached the Human's defense and
                    // one HP should be deducted from the player's HP
                    if (alien.getxPos() < 0) {
                        hpLost++;
                        tile.aliens.remove(alien);
                        game.getGameInterface().removeEntity(alien);
                    }
                    // if an Alien's position is less than it should be given its current tile, we remove it
                    // we move it to the tile on the left
                    else if (alien.getxPos() < col * 100) {
                        tile.aliens.remove(alien);
                        tiles[row][col - 1].aliens.add(alien);
                    } else {
                        i++;
                    }
                }
                // make all the projectiles move
                int j = 0;
                while (j < tile.projectiles.size()) {
                    Projectile projectile = tile.projectiles.get(j);
                    projectile.move();

                    if (projectile.getxPos() >= 800) {
                        removeEntity(projectile);
                    }
                    // if a projectile has crossed a tile boundary, move it to the next tile, eventually,
                    // all projectiles should maybe be stored as a single list for each row, not each Tile, altough
                    // it might be fine as is
                    else if (projectile.getxPos() >= (col + 1) * 100) {
                        tile.projectiles.remove(projectile);
                        tiles[row][col + 1].projectiles.add(projectile);
                    } else {
                        j++;
                    }
                }
            }
        }
        return hpLost;
    }

    public boolean noAlien(){
        for (int row = 0; row < height; row++){
            if (!noAlien(row)){
                return false;
            }
        }
        return true;
    }
    public boolean noAlien(int row){
        for (Tile tile:tiles[row]){
            if (!tile.aliens.isEmpty()){
                    return false;
            }
        }
        return true;
    }
}


