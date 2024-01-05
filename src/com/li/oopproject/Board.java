package com.li.oopproject;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import com.li.oopproject.entities.*;


public class Board {
    private int boardType;
    Tile[][] tiles = new Tile[height][length];
    private Tile entrance;
    private Tile exit;
    private Human human;
    public static final int length = 8; // fixed length for all Boards
    public static final int height = 5; // fixed height for all board
    private final Game game;
    private Collision collision;//check collision

    public int getBoardType() {
        return boardType;
    }

    private final GoldSystem goldSystem = new GoldSystem();

    public Board(Game game, int boardType) {
        this.boardType = boardType;
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

    // Tile local class is aggregated by a Board, a tile contains at most 1 human and possibly many Aliens
    public class Tile {
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
            if (Game.VERBOSE){
                System.out.println("Gold after deduct: " + goldSystem.getGold());
            }
            return true;
        } else {
            if (Game.VERBOSE){
                System.out.println("Not enough gold to place this human");
            }
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
    public Alien spawnAlien(String alienName, int yposition, int xposition) {
        Alien newAlien;
        switch (alienName) {
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
            case "BossAlien": // Boss has different hp value for different game modes
                newAlien = new BossAlien(this, game.getBossHP());
                break;
            default:
                System.out.println("Tried to spawn unknown alien name");
                newAlien = new DefaultAlien(this);
        }
        newAlien.setxPos(xposition * 100);
        newAlien.setyPos(yposition * 100);
        this.tiles[yposition][xposition - 1].aliens.add(newAlien);
        return newAlien;
    }

    // check each tile, if it has aliens then print "A"， if it has human, print "H"
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
                updateHuman(tile, elapsedTime, row, col);

                // make all the aliens move
                hpLost = updateAliens(tile, elapsedTime, row, col);

                // make all the projectiles move
                updateProjectiles(tile, elapsedTime, row, col);
            }
        }
        return hpLost;
    }

    public int updateAliens(Tile tile, int elapsedTime, int row, int col){
        int hpLost = 0;
        int i = 0;
        while (i < tile.aliens.size()) {
            Alien alien = tile.aliens.get(i);
            alien.updateSpeed(); // Update the Alien's speed (considering IceLaser effect)

            if (tile.human != null && collision.isHumanHittingAlien(tile.human, alien)){
                alien.reload(elapsedTime);
                i++;
                continue;
            }
            alien.move(); // Modify the move method to take speed into account
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
        return hpLost;
    }
    public void updateHuman(Tile tile, int elapsedTime, int row, int col){
        if (tile.human != null) {
            tile.human.reload(elapsedTime);
            if (tile.human.getReloadTimeRemaining() <= 0 & (!noAlien(row))) {
                Projectile projectile = tile.human.attack();
                tile.projectiles.add(projectile);
                game.getGameInterface().addEntity(projectile);
            }
        }
    }

    public void updateProjectiles(Tile tile, int elapsedTime, int row, int col){
        int j = 0;
        while (j < tile.projectiles.size()) {
            Projectile projectile = tile.projectiles.get(j);
            projectile.move();

            if (projectile.getxPos() >= 800) {
                removeEntity(projectile);
            }
            // if a projectile has crossed a tile boundary, move it to the next tile, eventually,
            // all projectiles should maybe be stored as a single list for each row, not each Tile, although
            // it might be fine as is
            else if (projectile.getxPos() >= (col + 1) * 100) {
                tile.projectiles.remove(projectile);
                tiles[row][col + 1].projectiles.add(projectile);
            } else {
                j++;
            }
        }
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


