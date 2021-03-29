package ru.johnlayming.goboard;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class GameUI {
    public ImageView[][] stones;
    public int size;
    public int cellSizeInDp;
    public int stoneSizeInDp;
    public int pngBoardSizeInDp;
    public String pngBoardPath;
    private int mode; // 0 - play, 1 - tutorial, 2 - problems
    private Tutorial T;
    private Problems P;


    private ImageView boardView;
    private Activity activity;
    private RelativeLayout boardLayout;
    private RelativeLayout.LayoutParams stoneLayoutParams;
    private ImageView stoneView;

    private Button passBtn;
    private Button returnBtn;

    private TextView turnCountView;
    private TextView whiteCapturetView;
    private TextView blackCaptureView;

    private GameState GS;
    private int pointX;
    private int pointY;

    private String blackStoneImg;
    private String whiteStoneImg;


    @SuppressLint("ClickableViewAccessibility")
    public GameUI(Activity _activity, int size, int _cellSizeInDp, int pngBoardSizeInDp,String pngBoardPath){
//        this.mode=mode;
        this.activity=_activity;

        this.size=size;
        this.cellSizeInDp=_cellSizeInDp;
        this.stoneSizeInDp=_cellSizeInDp;
        this.pngBoardSizeInDp=pngBoardSizeInDp;
        this.pngBoardPath=pngBoardPath;

        stones = new ImageView[size][size];
        boardView = activity.findViewById(R.id.board);
        boardView.getLayoutParams().width=dpToPx(pngBoardSizeInDp);
        boardView.getLayoutParams().height=dpToPx(pngBoardSizeInDp);
        boardView.setImageResource(activity.getResources().getIdentifier(pngBoardPath,"drawable",activity.getPackageName()));

        boardLayout = activity.findViewById(R.id.board_layout);
        passBtn = activity.findViewById(R.id.passBtn);
        returnBtn = activity.findViewById(R.id.returnBtn);

        turnCountView = activity.findViewById(R.id.turnCounter_view);
        whiteCapturetView=activity.findViewById(R.id.whiteCapture_view);
        blackCaptureView=activity.findViewById(R.id.blackCapture_view);

        // load from file settings
        setBlackStoneImg("stone_black_1");
        setWhiteStoneImg("stone_white_1");


        GS = new GameState(this,size);


        passBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(activity, "Пас!", Toast.LENGTH_SHORT).show();
                GS.pass();

            }
        });
        returnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GS.returnMove();
                updateGameStatus();
            }
        });
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
                        } else if(mode==2){
                            P.moveMaked(pointX,pointY);
                        }


                    };
                }

                return true;
            }
        });




    }

    public void updateGameStatus(){
        if(GS.turn%2==1) turnCountView.setText("Ход чёрных");
        else turnCountView.setText("Ход белых");
        whiteCapturetView.setText("Захвачено белых\n"+GS.whiteCapture);
        blackCaptureView.setText("Захвачено чёрных\n"+GS.blackCapture);
    }

    public void setModeProblem(int mode){ // 0 - play, 1 - tutorial, 2 - problems
        this.mode=mode;
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
        updateGameStatus();

    }
    public void removeStone(int x,int y){
        boardLayout.removeView(stones[y][x]);
        //stones[y][x].setImageDrawable(null); ???? not work
        stones[y][x]=null;
        updateGameStatus();
    }


    public void suicideMove(){
        Toast.makeText(activity, "Самоубийственный ход запрещён!", Toast.LENGTH_SHORT).show();
    }
    
    public void koMove(){
        Toast.makeText(activity, "Взятие Ко запрещено!", Toast.LENGTH_SHORT).show();
    }
    public void gameEnd(){
        Toast.makeText(activity, "Игра закончена. Подсчёт очков.", Toast.LENGTH_SHORT).show();
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

    public void setT(Tutorial t) {
        T = t;
    }

    public void setP(Problems p) {
        P = p;
    }

    public void setBlackStoneImg(String blackStoneImg) {
        this.blackStoneImg = blackStoneImg;
    }

    public void setWhiteStoneImg(String whiteStoneImg) {
        this.whiteStoneImg = whiteStoneImg;
    }
}
