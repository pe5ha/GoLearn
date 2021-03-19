package ru.johnlayming.goboard;

import java.util.ArrayList;


public class GameState {

    public int size;
    public byte[][] board;
    public int turn; // count turn
    public int whoseTurn;
    public int opponent;
    public int typeofLastMove; // 0 - move, 1 - pass, 2 - return, -1 - suicide, -2 - rule Ko, -3 - resign
    public int[] ko;
    public int koTurn;
    public boolean koRule;
    public int win; // 0, 1 - black win, 2 - white win
    public int moveX;
    public int moveY;
    public int lastMoveX;
    public int lastMoveY;
    public int capturedStones;
    public GameUI gameUI;
    public ArrayList<Stone> group;



    public GameState(GameUI gameUI,int size){
        this.gameUI=gameUI;
        this.size=size;
        this.board = new byte[size][size];
        this.turn=1;
        this.whoseTurn=1;
        this.opponent=2;
        this.win=0;
        ko = new int[2];
        koTurn=-2;
        capturedStones=0;
        group = new ArrayList<>();
    }

    public void setState(byte[][] p){
        this.board=p;
    }


    public boolean attemptMove(int x,int y){ // -1 - пас -2 - сдача
        moveX=x;
        moveY=y;
        koRule=false;
        if(board[y][x]!=0){ //пункт уже занят
            //
        }
        else {


            group.clear();
            board[y][x]= (byte) whoseTurn;
            if(Capture(x,y)){
                makeMove(x, y);
                return true;
            }
            else if(koRule){
                board[lastMoveY][lastMoveX]= (byte) opponent;
                gameUI.addStone(lastMoveX,lastMoveY,opponent);
                board[y][x] = 0;
                typeofLastMove=-2;
                gameUI.koMove();
                //KO!
            }
            else{
                if(searchDame(x,y,whoseTurn)){
                    makeMove(x,y);
                    return true;
                }
                else {
                    board[y][x] = 0;
                    typeofLastMove=-1;
                    gameUI.suicideMove();
                    //SUICIDE MOVE!!!
                }
            }
        }

        group.clear();
        return false;
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

        if(cpt>0) {
            if(koTurn==turn-1){
                if(ko[0]==moveX && ko[1]==moveY){
                    //ko!
                    koRule=true;
                    return false;
                }
            }
            return true;
        }
        else return false;
    }

    public boolean tryCapture(int x,int y){
        group.clear();
        if(!searchDame(x,y,opponent)){
            capturedStones+=group.size();
            if(capturedStones==1){
                if(koTurn==turn-1){

                }
                else {
                    ko[0] = x;
                    ko[1] = y;
                    koTurn=turn;
                }
            }
            else {
                koTurn--;
            }
            for (int i = 0; i < group.size(); i++) {
                board[group.get(i).y][group.get(i).x]=0;
                gameUI.removeStone(group.get(i).x,group.get(i).y);
            }
            return true;
        }
        group.clear();
        return false;

    }

    public boolean searchDame(int x,int y,int who){
        for(int i=0;i<group.size();i++){
            if(group.get(i).x==x&&group.get(i).y==y) return false;
        }
        group.add(new Stone(x,y));

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
        board[y][x] = (byte) (whoseTurn); // чей сейчас ход делается белых или черных на основе количества ходов
        gameUI.addStone(x,y,whoseTurn);
        turn++;
        opponent=whoseTurn;
        whoseTurn = whoseTurn == 1 ? 2 : 1;
        lastMoveX=moveX;
        lastMoveY=moveY;
        typeofLastMove=0;

    }
    public void returnMove(){
        board[lastMoveY][lastMoveX] = 0;
        gameUI.removeStone(lastMoveX,lastMoveY);
        turn--;
        whoseTurn = whoseTurn == 1 ? 2 : 1;
        opponent = opponent == 1 ? 2 : 1;
        typeofLastMove=2; // return
    }


    public void pass(){
        if(typeofLastMove==1){ // два паса, конец игры подсчет очков
            endGame();
        } else {
            typeofLastMove=1;
            turn++;
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

    public void placeStone(int x,int y,int stone){
        this.board[y][x] = (byte) stone;
        gameUI.addStone(x,y,stone);
    }
    public void setWhoseTurn(int _whoseTurn){
        this.whoseTurn=_whoseTurn;
    }

}

class Stone {
    public int x;
    public int y;
    Stone(int x,int y){
        this.x=x;
        this.y=y;
    }
}
