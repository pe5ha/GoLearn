package ru.johnlayming.goboard;

import android.app.Activity;
import android.content.res.AssetManager;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;

public class SgfWorker {

    private int cursor;
    private Activity activity;
    private String sgfString;
    private String filename;
    private String XY;
    private byte[] array;
    private char s;


    public boolean loadBoardStateFromSgf(Activity activity,String filename,GameState GS){
            String sgfString = new String(readSgf(activity,filename));
            cursor=2;
            int x,y;
            while(sgfString.charAt(cursor)!=';') cursor++;
            while (sgfString.charAt(cursor)==';'&&sgfString.charAt(cursor+1)!='B'){
                x=sgfString.charAt(cursor+4)-'a';
                y=sgfString.charAt(cursor+5)-'a';
                switch (sgfString.substring(cursor+1,cursor+3)){
                    case "AB":
                        GS.placeStone(x,y,1);
                        break;
                    case "AW":
                        GS.placeStone(x,y,2);
                        break;
                }
                cursor+=8;
            }
            return true;
    }

    public void loadBoardStateFromSgfProblem(Activity activity,String filename,GameState GS){
        String sgfString = new String(readSgf(activity,filename));
        cursor=2;
        int x,y;
        while(sgfString.charAt(cursor-1)!='A'&&sgfString.charAt(cursor)!='B') cursor++;
        for (int i = 0; i==0; ) {
            x=sgfString.charAt(cursor+2)-'a';
            y=sgfString.charAt(cursor+3)-'a';
            GS.placeStone(x,y,1);
            if(sgfString.charAt(cursor+5)!='[') break;
            else cursor+=4;
        }
        while(sgfString.charAt(cursor-1)!='A'&&sgfString.charAt(cursor)!='W') cursor++;
        for (int i = 0; i==0; ) {
            x=sgfString.charAt(cursor+2)-'a';
            y=sgfString.charAt(cursor+3)-'a';
            GS.placeStone(x,y,2);
            if(sgfString.charAt(cursor+5)!='[') break;
            else cursor+=4;
        }
        GS.setWhoseTurn(sgfString.charAt(cursor+4)=='B'?1:2);


    }

    public int[] checkAnswerFromSgf(Activity activity,String filename,int x,int y){
        this.activity=activity;
        this.filename=filename;
        array=readSgf(activity, filename);
        sgfString = new String(array);
        char xChar = (char) ('a' + x);
        char yChar = (char) ('a' + y);
        XY = "" + String.valueOf(xChar) + String.valueOf(yChar);

        return searchAnswer(x,y);
    }


    private int[] searchAnswer(int x,int y){
        int i;
        while (sgfString.charAt(cursor) != 'B') {
            cursor++;if(cursor<sgfString.length())s=sgfString.charAt(cursor);
            if(cursor>=sgfString.length()) return new int[]{-1,-1,-1};
        }
        if (sgfString.substring(cursor + 2, cursor + 4).equals(XY) || sgfString.substring(cursor + 1, cursor + 3).equals("[]")) {

            for (i = 0; i == 0; ) {
                cursor++;if(cursor<sgfString.length())s=sgfString.charAt(cursor);
                if (sgfString.charAt(cursor) == ')') i++;
                else if (sgfString.charAt(cursor) == '('||sgfString.charAt(cursor) ==  'W') i--;
            }
            if (i > 0) {
                //win!!
                return new int[]{1, 0, 0}; // [0]: -1-lose, 0-continue, 1-win; [1]-x; [2]-y;
            } else {
                //white answer?
                while (sgfString.charAt(cursor) !=  'W') cursor++;if(cursor<sgfString.length())s=sgfString.charAt(cursor);
                int answerX = sgfString.charAt(cursor + 2) - 'a';
                int answerY = sgfString.charAt(cursor + 3) - 'a';
                for (i = 0; i == 0; ) {
                    if (sgfString.charAt(cursor) == ')'){
                        i++;
                        break;
                    }
                    else if (sgfString.charAt(cursor) == '('||sgfString.charAt(cursor) == 'B'){
                        i--;
                        break;
                    }
                    cursor++;if(cursor<sgfString.length())s=sgfString.charAt(cursor);
                }
                if (i > 0) {
                    return new int[]{-1, answerX, answerY}; //lose
                } else {
                    return new int[]{0, answerX, answerY}; //continue
                }
            }
        } else {
            for (i = 0; i <= 0; ) {
                if (sgfString.charAt(cursor) == ')') i++;
                else if (sgfString.charAt(cursor) == '(') i--;
                cursor++;if(cursor<sgfString.length())s=sgfString.charAt(cursor);
            }
            if (cursor >= sgfString.length()) return new int[]{-1, -1, -1};
            return searchAnswer(x, y);
        }
    }


    private byte[] readSgf(Activity activity,String filename){
        byte[] buffer = null;
        InputStream is;
        try {
            is = activity.getAssets().open(filename);
            int size = is.available();
            buffer = new byte[size];
            is.read(buffer);
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return buffer;
    }







}
