package com.li.oopproject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;

public class Scores implements Serializable {
    private LinkedList<Integer> default_board_scores;
    private LinkedList<Integer> portal_board_scores;
    private static int MAX_SIZE = 10;

    // Initialize two list for scores of two maps
    public Scores(){
        default_board_scores = new LinkedList<Integer>();
        portal_board_scores = new LinkedList<Integer>();
    }

    // Getter to get best scores
    public int getBest(){
        if (default_board_scores.isEmpty() || portal_board_scores.isEmpty()){
            if (!portal_board_scores.isEmpty()){
                return portal_board_scores.getLast();
            }
            else if  (!default_board_scores.isEmpty()) {
                return default_board_scores.getLast();
            }
            return 0;
        }
        if (portal_board_scores.getLast() > default_board_scores.getLast()){
            return portal_board_scores.getLast();
        }
        return default_board_scores.getLast();
    }

    public LinkedList<Integer> getDefault_board_scores() {
        return default_board_scores;
    }

    public LinkedList<Integer> getPortal_board_scores() {
        return portal_board_scores;
    }

    public void insertScore(int score, int boardType){
        LinkedList<Integer> list;
        if (boardType == 0) {
            list = default_board_scores;
        }
        else{
            list = portal_board_scores;
        }
        int index = 0;
        while (index < list.size() && list.get(index) < score) {
            index++;
        }
        list.add(index, score);
        if (list.size() > MAX_SIZE){
            list.removeFirst();
        }
    }
}
