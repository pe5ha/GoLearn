package ru.johnlayming.goboard;

import java.util.ArrayList;
import java.util.List;


public class GameState {

    public int size;
    public byte[][] board;
    public int turn; // count turn
    public int whoseTurn;
    public int opponent;
    public int typeofLastMove; // 0 - move, 1 - pass, 2 - return, -1 - suicide, -2 - rule Ko, -3 - resign
    //public int[] ko;
    public int koTurn;
    public boolean koCanBeNow;
    public boolean lastKoCanBeNow;
    public boolean koRuleNow;
    public Coordinates koCanBeHere;
    public int win; // 0, 1 - black win, 2 - white win
    public int moveX;
    public int moveY;
    public int lastMoveX;
    public int lastMoveY;
    public int capturedStones;
    public GameUI gameUI;
    public ArrayList<Coordinates> group;
    public ArrayList<Stone> stonesHistory;



    public GameState(GameUI gameUI,int size){

        // INITIALIZATION
        this.gameUI=gameUI;
        this.size=size;
        // main of game state
        board = new byte[size][size]; // собственно игровая доска в которой 0 - пусто, 1 - черный камень, 2 - белый
        turn=1; // ход первый
        whoseTurn=1; // первый ход черных
        opponent=2; // оппонент на первом ходу белый
        win=0; // никто пока не победил
        // ko flags
        //ko = new int[2];
        koRuleNow = false;
        koCanBeNow = false;
        koCanBeHere = new Coordinates(-1,-1);
        koTurn=-1;
        capturedStones=0; // чтобы понимать группа из скольких камней была захвачена. если 1 камень - дальше возможно ко
        // Array group for search his dame
        group = new ArrayList<>();
        // Array stonesHistory for save spawn turn and status each stone
        stonesHistory = new ArrayList<>();
    }

    public boolean attemptMove(int x,int y){ // -1 - пас -2 - сдача
        moveX=x;
        moveY=y;
        if(board[y][x]==0){ //если пункт не занят

            group.clear();
            board[y][x]= (byte) whoseTurn;
            if(Capture(x,y)){
                //makeMove(x, y);

                // КАМНИ ЗАХВАЧЕНЫ, СДЕЛАТЬ ХОД
                turn++;
                stonesHistory.add(new Stone(moveX,moveY,whoseTurn,turn));
                board[y][x] = (byte) (whoseTurn); // чей сейчас ход делается белых или черных на основе количества ходов
                gameUI.addStone(x,y,whoseTurn);

                opponent=whoseTurn;
                whoseTurn = whoseTurn == 1 ? 2 : 1;
                lastMoveX=moveX;
                lastMoveY=moveY;
                typeofLastMove=0;
                koRuleNow=false;
                if(capturedStones!=1){
                    lastKoCanBeNow=koCanBeNow;
                    koCanBeNow=false;
                }
                return true; // ХОД СДЕЛАН
            }
            // ПРАВИЛО КО ОПРЕДЕЛЯЕТСЯ НА МОМЕНТЕ ЗАХВАТИТЬ КАМЕНЬ (В tryCapture())
            else if(koRuleNow&&koCanBeHere.x==moveX&&koCanBeHere.y==moveY){
                board[moveY][moveX] = 0;
                typeofLastMove=-2;
                gameUI.koMove();
                //KO!
            }

            else{
                if(searchDame(x,y,whoseTurn)){
                    //makeMove(x, y);

                    // У КАМНЯ ЕСТЬ ДАМЭ, СДЕЛАТЬ ХОД
                    turn++;
                    stonesHistory.add(new Stone(moveX,moveY,whoseTurn,turn));
                    board[y][x] = (byte) (whoseTurn); // чей сейчас ход делается белых или черных на основе количества ходов
                    gameUI.addStone(x,y,whoseTurn);
                    opponent=whoseTurn;
                    whoseTurn = whoseTurn == 1 ? 2 : 1;
                    lastMoveX=moveX;
                    lastMoveY=moveY;
                    typeofLastMove=0;
                    koRuleNow=false;
                    lastKoCanBeNow=koCanBeNow;
                    koCanBeNow=false;
                    return true; // ХОД СДЕЛАН
                }
                else {

                    // У КАМНЯ НЕТ ДАМЭ, SUICIDE MOVE!!!
                    board[y][x] = 0;
                    typeofLastMove=-1;
                    gameUI.suicideMove();
                }
            }
        }

        return false; // ХОД НЕ СДЕЛАН. КО ЛИБО САМОУБИЙСТ
    }

    public boolean Capture(int x,int y){
        int cpt=0;
        capturedStones=0;
        if(x>0){
            if(board[y][x-1]==opponent){
                if(tryCapture(x-1,y))cpt++;
            }
        }
        if(x<size-1){
            if(board[y][x+1]==opponent){
                if(tryCapture(x+1,y))cpt++;
            }
        }
        if(y>0){
            if(board[y-1][x]==opponent){
                if(tryCapture(x,y-1))cpt++;
            }
        }
        if(y<size-1){
            if(board[y+1][x]==opponent){
                if(tryCapture(x,y+1))cpt++;
            }
        }

        if(cpt>1) {
//            if(koTurn==turn-1){
//                if(ko[0]==moveX && ko[1]==moveY){
//                    //ko!
//                    koRule=true;
//                    return false;
//                }
//            }
            return true;  // ГРУППА ЗАХВАЧЕНА
        }
        if(cpt==1&&!(koCanBeNow&&koCanBeHere.x==moveX&&koCanBeHere.y==moveY)){
            return true;
        }
        return false;
    }

    public boolean tryCapture(int x,int y){
        group.clear();
        if(!searchDame(x,y,opponent)){
            capturedStones+=group.size();

            // ОПРЕДЕЛЕНИЕ КО
            if(capturedStones==1){
                if(koCanBeNow){
                    if(koCanBeHere.x==moveX&&koCanBeHere.y==moveY){
                        koRuleNow=true;

                        return false; // ГРУППА НЕ ЗАХВАЧЕНА: ПРАВИЛО КО
                    }
                    else{
                        // флаг koCanBeNow сбрасывается при каждом успешном ходе(не ко и не суицд)
                    }
                }

                // заряжжаем триггер
                koCanBeNow=true;
                koCanBeHere.x=x;
                koCanBeHere.y=y;
            }


            for (int i = 0; i < group.size(); i++) {
                stonesHistory.get(findStoneByXY(group.get(i).x,group.get(i).y)).turnOfDie=turn;
                board[group.get(i).y][group.get(i).x]=0;
                gameUI.removeStone(group.get(i).x,group.get(i).y);
            }
            return true; // ГРУППА ЗАХВАЧЕНА: У ГРУППЫ ПРОТИВНИКА НЕТ ДАМЭ, ПРАВИЛО КО НЕ ВОЗНИКЛО
        }
        group.clear();
        return false; // ГРУППА НЕ ЗАХВАЧЕНА: У ГРУППЫ ПРОТИВНИКА ЕСТЬ ДАМЭ
    }

    public boolean searchDame(int x,int y,int who){ //СУПЕР ПУПЕР РЕКУРСИВНЫЙ АЛГОРИТМ
        for(int i=0;i<group.size();i++){ // проверка, а не проверяли ли мы уже этот самый камень ранее
            if(group.get(i).x==x&&group.get(i).y==y) return false;
        }
        group.add(new Coordinates(x,y));

        if(x>0){
            if(board[y][x-1]==0) return true;
            else if(board[y][x-1]==who) if(searchDame(x-1,y,who)) return true;

        }
        if(x<size-1){
            if(board[y][x+1]==0) return true;
            else if(board[y][x+1]==who) if(searchDame(x+1,y,who)) return true;
        }
        if(y>0){
            if(board[y-1][x]==0) return true;
            else if(board[y-1][x]==who) if(searchDame(x,y-1,who)) return true;
        }
        if(y<size-1){
            if(board[y+1][x]==0) return true;
            else if(board[y+1][x]==who) if(searchDame(x,y+1,who)) return true;
        }

        return false;
    }


    public void makeMove(int x,int y){
        stonesHistory.add(new Stone(moveX,moveY,whoseTurn,turn));
        board[y][x] = (byte) (whoseTurn); // чей сейчас ход делается белых или черных на основе количества ходов
        gameUI.addStone(x,y,whoseTurn);
        turn++;
        gameUI.updateGameStatus();
        opponent=whoseTurn;
        whoseTurn = whoseTurn == 1 ? 2 : 1;
        lastMoveX=moveX;
        lastMoveY=moveY;
        typeofLastMove=0;

    }
    public void returnMove(){
        if(typeofLastMove!=2) {
            board[lastMoveY][lastMoveX] = 0;
            gameUI.removeStone(lastMoveX, lastMoveY);

            for (int i = 0; i < stonesHistory.size(); i++) {
                if(stonesHistory.get(i).turnOfDie==turn-1) {
                    board[stonesHistory.get(i).y][stonesHistory.get(i).x] = (byte)stonesHistory.get(i).color;
                    stonesHistory.get(i).turnOfDie=0;
                    gameUI.addStone(stonesHistory.get(i).x, stonesHistory.get(i).y,stonesHistory.get(i).color);
                }
            }

            stonesHistory.remove(stonesHistory.size()-1);

            koCanBeNow=lastKoCanBeNow;
            turn--;
            whoseTurn = whoseTurn == 1 ? 2 : 1;
            opponent = opponent == 1 ? 2 : 1;
            typeofLastMove = 2; // return
        }
    }

    public void pass(){
        if(typeofLastMove==1){ // два паса, конец игры подсчет очков
            endGame();
            gameUI.gameEnd();
        } else {
            koCanBeNow=false;
            koCanBeHere.x=-1;koCanBeHere.y=-1;
            opponent=whoseTurn;
            whoseTurn = whoseTurn == 1 ? 2 : 1;
            typeofLastMove=1;
            turn++;
            gameUI.updateGameStatus();
        }
    }

    public void giveUp(){
        win = (turn%2==1?2:1);

    }

    public void resetTurnCount(){
        this.turn=1;
        this.whoseTurn=1;
        this.opponent=2;
    }

    public boolean moveCheck(int x,int y){

        return true;
    }

    public void endGame(){

    }

    public void setState(byte[][] p){

        this.board=p;
    }

    public void placeStone(int x,int y,int stone){
        this.board[y][x] = (byte) stone;
        gameUI.addStone(x,y,stone);
    }
    public void setWhoseTurn(int _whoseTurn){
        this.whoseTurn=_whoseTurn;
    }
    public int findStoneByXY(int x, int y){
        for (int i = 0; i < stonesHistory.size(); i++) {
            if(stonesHistory.get(i).x==x&&stonesHistory.get(i).y==y&&stonesHistory.get(i).turnOfDie<=0) return i;
        }
        return -1;
    }

}
class Coordinates{
    public int x;
    public int y;
    Coordinates(int x,int y) {
        this.x = x;
        this.y = y;
    }
}

class Stone {
    public int x;
    public int y;
    public int color;
    public int turnOfSpawn;
    public int turnOfDie;
    Stone(int x,int y,int color, int turnOfSpawn){
        this.x=x;
        this.y=y;
        this.color=color;
        this.turnOfSpawn=turnOfSpawn;
        this.turnOfDie=0;
    }
}
