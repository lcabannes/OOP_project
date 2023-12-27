package com.li.oopproject;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import com.li.oopproject.entities.*;


public class Board {
    private String name;
    private Tile[][] tiles = new Tile[height][length];
    private Tile entrance;
    private Tile exit;
    private Human human;
    public static final int length = 8; // fixed length for all Boards
    public static final int height = 5; // fixed height for all board
    private final Game game;

    public Board(Game game){
        this.name = "default_board";
        this.game = game;
        for (int row = 0; row < Board.height; row++){
            for (int col = 0; col<Board.length; col++){
                this.tiles[row][col] = new Tile();
            }
        }
    }

    public class Tile{ // Tile local class is aggregated by a Board, a tile contains at most 1 human and possibly many Aliens
        private ArrayList<Alien> aliens;
        private Human human;

        private ArrayList<Projectile> projectiles;

        public Tile() { // Aliens are stored in each tile
            this.aliens = new ArrayList<Alien>();
            this.human = null;
            this.projectiles = new ArrayList<Projectile>();

        }
    }
    // return true if a human was place, false otherwise
    public boolean placeHuman(Human human, int row, int col){
        if (tiles[row][col].human != null){
            return false;
        }
        human.setxPos(col*100);
        human.setyPos(row*100);
        tiles[row][col].human = human;


        return true;
    }

    /**
     * check for every tile to find the entity to erase
     * @param entity
     */
    public void removeEntity(Entity entity){
        game.getGameInterface().removeEntity(entity);
        for (int row=0; row<height; row++){
            for (int col=0; col<length; col++){

                Tile tile = this.tiles[row][col];
                if (tile.human == entity){
                    tile.human = null;
                    return;
                }
                for (Projectile projectile: tile.projectiles){
                    if (projectile == entity){
                        tile.projectiles.remove(projectile);
                        return;
                    }
                }
                for (Alien alien:tile.aliens){
                        if (alien == entity){
                            tile.aliens.remove(alien);
                            return;
                        }
                }

            }
        }
    }
    public Alien spawnAlien(String alienName, int yposition) {
        // check for which zombie to spawn based on name passed to the method
        Alien newAlien;
        if (alienName.equals("DefaultAlien")) {
            newAlien = new DefaultAlien(this);
            // set default position of new Zombie to rightmost (each tile is 100 units so rightmost = 100 * tile_num)
            newAlien.setxPos(Board.length * 100);
            newAlien.setyPos(yposition * 100);

        } else {
            System.out.println("Tried to spawn unknown zombie name");
            newAlien = new DefaultAlien(this);

        }
        this.tiles[yposition][Board.length - 1].aliens.add(newAlien);
        return newAlien;
    }

    // check each tile, if it has aliens then print "A"
    public void display(){
        System.out.println("Current Board: \n");
        for (int row = 0; row < Board.height; row++){
            for (int col = 0; col<Board.length; col++){
                if (this.tiles[row][col].aliens.isEmpty()){
                    System.out.printf("|_");
                }
                else{
                    System.out.printf("|A");

                }

            }
            System.out.printf("|\n");
        }
        System.out.printf("\n");
    }

    public int updateEntities(int elapsedTime){
        // this method returns how many aliens have passed the final line after moving this update
        // it also reloads all the humans guns
        int hpLost = 0;
        for (int row = 0; row < Board.height; row++){
            for (int col = 0; col<Board.length; col++){
                // reload the human
                Tile tile = tiles[row][col];
                if (tile.human!=null){
                    tile.human.reload(elapsedTime);
                    if (tile.human.getReloadTimeRemaining() <= 0){
                        Projectile projectile = tile.human.attack();
                        tile.projectiles.add(projectile);
                        game.getGameInterface().addEntity(projectile);
                        EventQueue.invokeLater(new Runnable() {
                            @Override
                            public void run() {

                            }
                        });
                    }
                }
                int i = 0;
                while (i < tile.aliens.size()) {
                    Alien alien = tile.aliens.get(i);
                    alien.move();
                    // if an Alien's position is negative it means it breached the Human's defense and
                    // one HP should be deducted from the player's HP
                    if (alien.getxPos() < 0){
                        hpLost++;
                        tile.aliens.remove(alien);
                    }
                    // if an Alien's position is less than it should be given its current tile, we remove it
                    // we move it to the tile on the left
                    else if (alien.getxPos() < col * 100){
                        tile.aliens.remove(alien);
                        tiles[row][col-1].aliens.add(alien);
                    }
                    else{
                        i++;
                    }
                }
                int j = 0;
                while (j < tile.projectiles.size()){
                    Projectile projectile = tile.projectiles.get(j);
                    projectile.move();
              //      System.out.println("projectile at y : " + projectile.getyPos() + "x: " + projectile.getxPos());
                    if (projectile.getxPos() >= 800){
                        removeEntity(projectile);
                    }
                    else if (projectile.getxPos() >= (col+1) * 100){
                        tile.projectiles.remove(projectile);
                        tiles[row][col+1].projectiles.add(projectile);
                    }
                    else{
                        j++;
                    }
                }
            }
        }
        return hpLost;
    }

    public void checkCollisions(){
        for (int row = 0; row < Board.height; row++) {
            for (int col = 0; col < Board.length; col++) {
                // reload the human
                Tile tile = tiles[row][col];
                int i = 0;
                while (i < tile.projectiles.size()){
                    Projectile projectile = tile.projectiles.get(i);
                    ArrayList<Alien> aliensToConsider = new ArrayList<Alien>();
                    aliensToConsider.addAll(tile.aliens);
                    if (col+1 < Board.length){
                        aliensToConsider.addAll(tiles[row][col+1].aliens);
                    }
                    Alien closestAlien = null;
                    int closestDist = 10000; // set an initial distance to infinite
                    for (Alien alien:aliensToConsider){
                        int dist = alien.getxPos() - projectile.getxPos();
                        if (Math.abs(dist) < 50 & dist < closestDist){
                            closestAlien = alien;
                            closestDist = dist;
                        }
                    }
                    if (closestAlien != null){
                        closestAlien.reduceHp(projectile.getDamage());
                        if (!closestAlien.isAlive()){
                            removeEntity(closestAlien);
                        }
                        projectile.reduceHp(1);
                        if (!projectile.isAlive()){
                            removeEntity(projectile);
                            i--;
                        }
                    }
                    i++;
                }
            }
        }
    }
}
