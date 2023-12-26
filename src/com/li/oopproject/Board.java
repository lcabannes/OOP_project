package com.li.oopproject;
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

    public Board(){
        this.name = "default_board";
        for (int row = 0; row < Board.height; row++){
            for (int col = 0; col<Board.length; col++){
                this.tiles[row][col] = new Tile();
            }
        }
    }

    public class Tile{ // Tile local class is aggregated by a Board, a tile contains at most 1 human and possibly many Aliens
        private ArrayList<Alien> aliens;
        private Human human;

        public Tile() { // Aliens are stored in each tile
            this.aliens = new ArrayList<Alien>();
            this.human = null;

        }
    }

    /**
     * check for every tile to find the entity to erase
     * @param entity
     */
    public void removeEntity(Entity entity){
        for (int row=0; row<height; row++){
            for (int col=0; col<length; col++){

                Tile tile = this.tiles[row][col];
                if (tile.human == entity){
                    tile.human = null;
                    return;
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
    public void spawnAlien(String alienName, int yposition) {
        // check for which zombie to spawn based on name passed to the method
        Alien newAlien;
        if (alienName.equals("DefaultAlien")) {
            newAlien = new DefaultAlien(this);
            // set default position of new Zombie to rightmost (each tile is 100 units so rightmost = 100 * tile_num)
            newAlien.setxPos(Board.length * 100);
            newAlien.setyPos(yposition * 100);
        } else {
            System.out.println("Tried to spawn unknown zombie name");
            return;
        }
        this.tiles[yposition][Board.length - 1].aliens.add(newAlien);

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

    public int moveEntities(){
        // this method returns how many aliens have passed the final line after moving this update
        int hpLost = 0;
        for (int row = 0; row < Board.height; row++){
            for (int col = 0; col<Board.length; col++){
                int i = 0;
                while (i < tiles[row][col].aliens.size()) {
                    Alien alien = tiles[row][col].aliens.get(i);
                    alien.move();
                    // if an Alien's position is negative it means it breached the Human's defense and
                    // one HP should be deducted from the player's HP
                    if (alien.getxPos() < 0){
                        hpLost++;
                        tiles[row][col].aliens.remove(alien);
                    }
                    // if an Alien's position is less than it should be given its current tile, we remove it
                    // we move it to the tile on the left
                    else if (alien.getxPos() < col * 100){
                        tiles[row][col].aliens.remove(alien);
                        tiles[row][col-1].aliens.add(alien);
                    }
                    else{
                        i++;
                    }
                }
            }
        }
        return hpLost;
    }
}
