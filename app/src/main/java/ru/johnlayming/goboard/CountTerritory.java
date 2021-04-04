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


}
