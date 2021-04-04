package ru.johnlayming.goboard;

import java.util.ArrayList;

public class CountTerritory {

    private GameState GS;
    ArrayList<Coordinates> emptyGroup;


    public CountTerritory(GameState GS){
        this.GS=GS;
        emptyGroup = new ArrayList<>();

    }

    public void countTerritory() {
//        for (int i = 0; i < GS.size; i++) {
//            for (int j = 0; j < GS.size; j++) {
                emptyGroup.clear();
                searchSpaces(0,0);


                GS.gameUI.drawTerritory(emptyGroup);
//            }
//        }

    }

    public boolean searchSpaces(int x, int y){
        for(int i=0;i<emptyGroup.size();i++){ // проверка, а не проверяли ли мы уже этот самый камень ранее
            if(emptyGroup.get(i).x==x&&emptyGroup.get(i).y==y) return false;
        }
        emptyGroup.add(new Coordinates(x,y));

        if(x>0){
            if(GS.board[y][x-1]==0) {
                searchSpaces(x-1,y);
            }
            else {
                //если это камень...
            }
        }
        if(x<GS.size-1){
            if(GS.board[y][x+1]==0) {
                searchSpaces(x+1,y);
            }
            else {
                //если это камень...
            }
        }
        if(y>0){
            if(GS.board[y-1][x]==0) {
                searchSpaces(x,y-1);
            }
            else {
                //если это камень...
            }
        }
        if(y<GS.size-1){
            if(GS.board[y+1][x]==0) {
                searchSpaces(x,y+1);
            }
            else {
                //если это камень...
            }
        }

        return true;
    }

    public void statusTerritory(){


        int b=0,w=0;
        int x,y;
        ArrayList<Coordinates> adjacentStones = new ArrayList<>();
        for (int i = 0; i < emptyGroup.size(); i++) {
            x=emptyGroup.get(i).x;
            y=emptyGroup.get(i).y;

            if(emptyGroup.get(i).x>0){
                if(GS.board[y][x-1]!=0) adjacentStones.add(new Coordinates(x-1,y));
            }
            if(x<GS.size-1){
                if(GS.board[y][x+1]!=0) adjacentStones.add(new Coordinates(x+1,y));
            }
            if(y>0){
                if(GS.board[y-1][x]!=0) adjacentStones.add(new Coordinates(x,y-1));
            }
            if(y<GS.size-1){
                if(GS.board[y+1][x]!=0) adjacentStones.add(new Coordinates(x,y+1));
            }
        } // collect adjacentStones

        for (int i = 0; i < adjacentStones.size(); i++) {
            if(GS.board[adjacentStones.get(i).y][adjacentStones.get(i).x]==1) b++;
            if(GS.board[adjacentStones.get(i).y][adjacentStones.get(i).x]==2) w++;
        }

        // 1. Случай первый: "чистая территория"
        if(b==0||w==0){

        }








        // 2. Случай второй: мёртвая группа внутри.


    }


    public void eyeSearch(int x, int y){
        // 1. либо у группы есть 2 глаза
        // 2. либо у группы есть возможность создать 2 глаза



    }





}
