package com.li.oopproject.entities;

import com.li.oopproject.Board;

public abstract class Consumable extends Entity {
    public Consumable(Board board){
        super(0, 0, board);
    }
}
