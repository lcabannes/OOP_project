package com.li.oopproject;
import java.util.ArrayList;
import java.util.List;
import com.li.oopproject.entities.*;


public class Board {
    private String name;
    private Tile[][] tiles;
    private Tile entrance;
    private Tile exit;
    private Human human;
    private final int length = 8;
    private final int height = 5;

    public Board(){
        this.name = "default_board";
        this.tiles = new Tile[length][height]; // size of the board
    }

    public class Tile{ // Tile local class is aggregated by a Board, a tile contains at most 1 human and possibly many Aliens
        private ArrayList<Alien> aliens;
        private Human human;

        public Tile() {
            this.aliens = new ArrayList<Alien>();
            this.human = null;

        }
    }
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


    
}
