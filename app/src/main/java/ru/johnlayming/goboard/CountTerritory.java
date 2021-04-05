package ru.johnlayming.goboard;

import java.util.ArrayList;

public class CountTerritory {

    private GameState GS;
    ArrayList<Coordinates> emptyGroup= new ArrayList<>();
    ArrayList<Coordinates> adjacentStones = new ArrayList<>();
    ArrayList<ArrayList<Coordinates>> territoryPartsWhite = new ArrayList<>();
    ArrayList<ArrayList<Coordinates>> territoryPartsBlack = new ArrayList<>();
    ArrayList<ArrayList<Coordinates>> territoryPartsNeutral = new ArrayList<>();


    public CountTerritory(GameState GS){
        this.GS=GS;

    }

    public void countTerritory() {
        for (int i = 0; i < GS.size; i++) {
            for (int j = 0; j < GS.size; j++) {
                if(GS.board[i][j]==0){
                    if(checkOverlap(j,i)){
                        emptyGroup.clear();
                        adjacentStones.clear();
                        searchSpaces(j,i);
                        statusTerritory();
                    }
                }



            }
        }
        GS.gameUI.drawTerritory(territoryPartsWhite,2);
        GS.gameUI.drawTerritory(territoryPartsBlack,1);
        // territoryPartsNeutral

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
                collectAdjacentStones(x-1,y);
            }
        }
        if(x<GS.size-1){
            if(GS.board[y][x+1]==0) {
                searchSpaces(x+1,y);
            }
            else {
                //если это камень...
                collectAdjacentStones(x+1,y);
            }
        }
        if(y>0){
            if(GS.board[y-1][x]==0) {
                searchSpaces(x,y-1);
            }
            else {
                //если это камень...
                collectAdjacentStones(x,y-1);
            }
        }
        if(y<GS.size-1){
            if(GS.board[y+1][x]==0) {
                searchSpaces(x,y+1);
            }
            else {
                //если это камень...
                collectAdjacentStones(x,y+1);
            }
        }

        return true;
    } // collect emptyGroup

    public boolean collectAdjacentStones(int x, int y){
        for(int i=0;i<adjacentStones.size();i++){ // проверка, а не проверяли ли мы уже этот самый камень ранее
            if(adjacentStones.get(i).x==x&&adjacentStones.get(i).y==y) return false;
        }
        adjacentStones.add(new Coordinates(x,y));
        return true;
    }

    public void statusTerritory(){
        int b=0,w=0;
//        int x,y;
////        ArrayList<Coordinates> adjacentStones = new ArrayList<>();
//        for (int i = 0; i < emptyGroup.size(); i++) {
//            x=emptyGroup.get(i).x;
//            y=emptyGroup.get(i).y;
//
//            if(emptyGroup.get(i).x>0){
//                if(GS.board[y][x-1]!=0) adjacentStones.add(new Coordinates(x-1,y));
//            }
//            if(x<GS.size-1){
//                if(GS.board[y][x+1]!=0) adjacentStones.add(new Coordinates(x+1,y));
//            }
//            if(y>0){
//                if(GS.board[y-1][x]!=0) adjacentStones.add(new Coordinates(x,y-1));
//            }
//            if(y<GS.size-1){
//                if(GS.board[y+1][x]!=0) adjacentStones.add(new Coordinates(x,y+1));
//            }
//        } // collect adjacentStones
        for (int i = 0; i < adjacentStones.size(); i++) {
            if(GS.board[adjacentStones.get(i).y][adjacentStones.get(i).x]==1) b++;
            if(GS.board[adjacentStones.get(i).y][adjacentStones.get(i).x]==2) w++;
        }

        // 1. Случай первый: "чистая территория"
        if(b==0||w==0){
            if(b==0) territoryPartsWhite.add(emptyGroup);
            if(w==0) territoryPartsBlack.add(emptyGroup);

            emptyGroup = new ArrayList<>(emptyGroup);
        }
        else {
            territoryPartsNeutral.add(emptyGroup);
            emptyGroup = new ArrayList<>(emptyGroup);
        }


        // 2. Случай второй: мёртвая группа внутри.

    }

    private boolean checkOverlap(int x,int y){
        for (int i = 0; i < territoryPartsBlack.size(); i++) {
            for (int j = 0; j < territoryPartsBlack.get(i).size(); j++) {
                if(territoryPartsBlack.get(i).get(j).x==x&&territoryPartsBlack.get(i).get(j).y==y) return false;
            }
        }
        for (int i = 0; i < territoryPartsWhite.size(); i++) {
            for (int j = 0; j < territoryPartsWhite.get(i).size(); j++) {
                if(territoryPartsWhite.get(i).get(j).x==x&&territoryPartsWhite.get(i).get(j).y==y) return false;
            }
        }
        for (int i = 0; i < territoryPartsNeutral.size(); i++) {
            for (int j = 0; j < territoryPartsNeutral.get(i).size(); j++) {
                if(territoryPartsNeutral.get(i).get(j).x==x&&territoryPartsNeutral.get(i).get(j).y==y) return false;
            }
        }

        return true;
    }

    public void eyeSearch(int x, int y){
        // 1. либо у группы есть 2 глаза
        // 2. либо у группы есть возможность создать 2 глаза



    }





}
