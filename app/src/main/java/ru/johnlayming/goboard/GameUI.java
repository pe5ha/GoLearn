package ru.johnlayming.goboard;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class GameUI {
    public ImageView[][] stones;
    public int size;
    public int cellSizeInDp;
    public int stoneSizeInDp;
    private int mode; // 0 - play, 1 - tutorial
    private Tutorial T;


    private ImageView boardView;
    private Activity activity;
    private RelativeLayout boardLayout;
    private RelativeLayout.LayoutParams stoneLayoutParams;
    private ImageView stoneView;

    private GameState GS;
    private int pointX;
    private int pointY;

    private String blackStoneImg;
    private String whiteStoneImg;


    @SuppressLint("ClickableViewAccessibility")
    public GameUI(Activity _activity, int size, int _cellSizeInDp){
//        this.mode=mode;
        this.activity=_activity;

        this.size=size;
        this.cellSizeInDp=_cellSizeInDp;
        this.stoneSizeInDp=_cellSizeInDp;
        stones = new ImageView[size][size];
        boardView = activity.findViewById(R.id.board);

        boardLayout = activity.findViewById(R.id.board_layout);

        // load from file settings
        setBlackStoneImg("stone_black_1");
        setWhiteStoneImg("stone_white_1");


        GS = new GameState(this,size);


        boardView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                float x=event.getX();
                float y=event.getY();

                if(event.getAction()==MotionEvent.ACTION_UP) {
                    pointX=(int)x/dpToPx(cellSizeInDp);
                    pointY=(int)y/dpToPx(cellSizeInDp);

                    if(GS.attemptMove(pointX,pointY)){
                        if(mode==1) {
                            T.moveMaked(pointX,pointY);
                        }
                    };
                }

                return true;
            }
        });



    }

    public void setModeTutorial(Tutorial _T){
        this.T=_T;
        if(T==null) mode=0;
        else mode=1;
    }

    public void addStone(int x, int y,int stone){
        stoneView = new ImageView(activity);
        //видимо леяут парамсы нужно тоже каждый раз новые к каждомо новому вью создавать
        stoneLayoutParams = new RelativeLayout.LayoutParams(dpToPx(stoneSizeInDp), dpToPx(stoneSizeInDp));
        if(stone==1) {
            stoneView.setImageResource(activity.getResources().getIdentifier(blackStoneImg,"drawable",activity.getPackageName()));

        }
        else if(stone==2){
            stoneView.setImageResource(activity.getResources().getIdentifier(whiteStoneImg,"drawable",activity.getPackageName()));

        }

        stoneLayoutParams.leftMargin = x*dpToPx(cellSizeInDp) + (int)boardView.getX();
        stoneLayoutParams.topMargin = y*dpToPx(cellSizeInDp) + (int)boardView.getY();

        stoneView.setLayoutParams(stoneLayoutParams);
        stones[y][x] = stoneView;
        boardLayout.addView(stoneView, -1);


    }
    public void removeStone(int x,int y){
        boardLayout.removeView(stones[y][x]);
        //stones[y][x].setImageDrawable(null); ???? not work
        stones[y][x]=null;
    }


    public void suicideMove(){
        Toast.makeText(activity, "Самоубийственный ход!", Toast.LENGTH_SHORT).show();
    }
    
    public void koMove(){
        Toast.makeText(activity, "Взятие Ко запрещено!", Toast.LENGTH_SHORT).show();
    }





    public int dpToPx(int dp) {
        float density = activity.getResources().getDisplayMetrics().density;
        return Math.round((float) dp * density);
    }



    public GameState clear(){
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                removeStone(j,i);
            }
        }
        GS=null;
        GS=new GameState(this,size);
        return GS;
    }

    public GameState getGameState(){
        return GS;
    }


    public void setBlackStoneImg(String blackStoneImg) {
        this.blackStoneImg = blackStoneImg;
    }

    public void setWhiteStoneImg(String whiteStoneImg) {
        this.whiteStoneImg = whiteStoneImg;
    }
}
