package ru.johnlayming.goboard;

import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;


public class Problems {


    private int progress;
    private Activity activity;
    private GameUI gUI;
    private GameState GS;
    private SgfWorker sgf;

    private Button nextBtn;
    private Button retryBtn;

    private TextView tip;
    private boolean waitForNext=false;



    Problems(Activity _activity,GameUI _gUI){
        activity=_activity;
        activity.findViewById(R.id.info_layout).setVisibility(View.GONE);
        activity.findViewById(R.id.settings_layout).setVisibility(View.GONE);
        activity.findViewById(R.id.game_btn_layout).setVisibility(View.GONE);
        activity.findViewById(R.id.lesson_layout).setVisibility(View.VISIBLE);
        activity.findViewById(R.id.main_layout).setVisibility(View.VISIBLE);
        activity.setTitle("Задачи");
        gUI=_gUI;
        gUI.setModeProblem(2);
        gUI.setP(this);
        GS=gUI.getGameState();
        nextBtn=activity.findViewById(R.id.lessonNextBtn);
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextStep();
            }
        });
        retryBtn=activity.findViewById(R.id.lessonRetryBtn);
        retryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progress--;
                nextStep();
            }
        });
        tip = activity.findViewById(R.id.lesson_text);



        progress= 1; // progress[0] - lessons, progress[1] - in lesson progress
        // загрузить урок и прогресс из файла


        sgf = new SgfWorker();
        sgf.loadBoardStateFromSgfProblem(activity,"CAPTURING STONE - ELEMENTARY/CAPTURING STONE - EASY ("+progress+").sgf",GS);
        tip.setText(GS.whoseTurn==1?"Ход чёрных":"Ход белых");

    }




    public void nextStep(){
//        Toast.makeText(activity, "next step progress", Toast.LENGTH_SHORT).show();
        waitForNext=false;
        GS=null;
        GS=gUI.clear();
        progress++;
        //if(progress[0]>numbLessons) ...

        retryBtn.setVisibility(View.GONE);
        nextBtn.setVisibility(View.GONE);
        sgf.loadBoardStateFromSgfProblem(activity,"CAPTURING STONE - ELEMENTARY/CAPTURING STONE - EASY ("+progress+").sgf",GS);
        tip.setText(GS.whoseTurn==1?"Ход чёрных":"Ход белых");

    }



    public void moveMaked(int x,int y){
        if(!waitForNext) {
            int[] ans = sgf.checkAnswerFromSgf(activity, "CAPTURING STONE - ELEMENTARY/CAPTURING STONE - EASY ("+progress+").sgf", x, y);
            switch (ans[0]) {
                case 0:
                    GS.attemptMove(ans[1], ans[2]);
                    break;
                case 1:
                    stepComplete();
                    break;
                case -1:
                    if(ans[1]>=0&&ans[2]>=0) GS.attemptMove(ans[1], ans[2]);
                    stepFailed();
                    break;
            }
        }
    }

    private void stepComplete(){
        tip.setText(R.string.lesson_complete);
        nextBtn.setVisibility(View.VISIBLE);
        waitForNext=true;
    }

    public void stepFailed(){
        tip.setText(R.string.lesson_failed);
        retryBtn.setVisibility(View.VISIBLE);
        waitForNext=true;
    }


}
