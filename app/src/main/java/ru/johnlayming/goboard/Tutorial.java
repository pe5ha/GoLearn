package ru.johnlayming.goboard;

import android.app.Activity;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class Tutorial {


    private int[] progress;
    private Activity activity;
    private GameUI gUI;
    private GameState GS;
    private SgfWorker sgf;

    private int[] numbStepsInLesson;

    private Button nextBtn;
    private Button retryBtn;

    private Button passBtn;
    private Button returnBtn;

    private TextView tip;
    private LinearLayout tutorialLayout;
    private boolean waitForNext=false; // для появления кнопки next



    Tutorial(Activity _activity,GameUI _gUI){
        activity=_activity;
        activity.findViewById(R.id.info_layout).setVisibility(View.GONE);
        activity.findViewById(R.id.settings_layout).setVisibility(View.GONE);
        activity.findViewById(R.id.game_btn_layout).setVisibility(View.GONE);
        activity.findViewById(R.id.main_layout).setVisibility(View.VISIBLE);
        activity.findViewById(R.id.lesson_layout).setVisibility(View.VISIBLE);
        activity.setTitle("Обучение");
        gUI=_gUI;
        gUI.setModeProblem(1);
        gUI.setT(this);
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
                progress[1]--;
                nextStep();
            }
        });
        passBtn=activity.findViewById(R.id.passBtn);
        passBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stepComplete();
            }
        });
        returnBtn=activity.findViewById(R.id.returnBtn);
        returnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                GS.returnMove();
            }
        });
        tip = activity.findViewById(R.id.lesson_text);

        progress= new int[]{0, 0}; // progress[0] - lessons, progress[1] - in lesson progress
        // загрузить урок и прогресс из файла
        //progress[0]=3;
        numbStepsInLesson = new int[]{1,8,4,1}; //количество шагов в каждом из уроков (разделов)
        sgf = new SgfWorker();
        if(progress[0]+progress[1]>0) sgf.loadBoardStateFromSgf(activity,"lesson"+progress[0]+"_state"+progress[1]+".sgf",GS);
        tip.setText(activity.getResources().getIdentifier("lesson" + progress[0] + "_text" + progress[1], "string", activity.getPackageName()));
        nextBtn.setVisibility(View.GONE);
        retryBtn.setVisibility(View.GONE);
    }




    public void nextStep(){
//        Toast.makeText(activity, "next step progress", Toast.LENGTH_SHORT).show();
        waitForNext=false;
        GS=null;
        GS=gUI.clear();
        progress[1]++;
        if(progress[1]>=numbStepsInLesson[progress[0]]){
            progress[0]++;
            progress[1]=0;
        }
        //if(progress[0]>numbLessons) ...

        retryBtn.setVisibility(View.GONE);
        nextBtn.setVisibility(View.GONE);
        if(progress[0]>3){ // 3 урок (раздела) всего, если больше то все заданий нет просто доска
            activity.findViewById(R.id.game_btn_layout).setVisibility(View.GONE);
            tip.setText(activity.getResources().getIdentifier("allLessonsComplete","string",activity.getPackageName()));
            gUI.clear();
        }
        else {  // соответтсвенно здесь если урок не последний
            tip.setText(activity.getResources().getIdentifier("lesson" + progress[0] + "_text" + progress[1], "string", activity.getPackageName()));
            sgf.loadBoardStateFromSgf(activity, "lesson" + progress[0] + "_state" + progress[1] + ".sgf", GS);
        }
        if(progress[0]==3&&progress[1]==0){ // если это  урок про кнопку ПАС
            activity.findViewById(R.id.game_btn_layout).setVisibility(View.VISIBLE);
            activity.findViewById(R.id.returnBtn).setVisibility(View.GONE);
        }
    }



    public void moveMaked(int x,int y){
        if(progress[0]>3) return; // всего 3 урока пока
        if(progress[0]+progress[1]==0) stepComplete();

        if(!waitForNext) {
            int[] ans = sgf.checkAnswerFromSgf(activity, "lesson" + progress[0] + "_state" + progress[1] + ".sgf", x, y);
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

    private void learnComplete(){

    }

}
