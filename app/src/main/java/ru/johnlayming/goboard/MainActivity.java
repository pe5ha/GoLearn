package ru.johnlayming.goboard;


import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import com.google.android.material.navigation.NavigationView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Toast;


/*
- только 1 правильный ответ на задачи и ВСЁ окейЁ
-
-
 */


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
private DrawerLayout drawer;

///////////////////////////////
public ImageView[][] board;
public int size=9;
public int cellSizeInDp=40;
public GameUI game;


private String boardTexture;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawer,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        ////////////////////////////////////////////////
        boardTexture="board_texture_0";
       game = new GameUI(this,size,cellSizeInDp);
//        findViewById(R.id.board_texture).setBackgroundResource(getResources().getIdentifier(boardTexture,"drawable",getPackageName()));


    }


    // три кружопеля открывающие настройки и тд
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main, menu);
////        Toast.makeText(this, "TEST", Toast.LENGTH_SHORT).show();
//        return true;
//    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id = menuItem.getItemId();
        if(id==R.id.nav_learn){
            findViewById(R.id.settings_layout).setVisibility(View.GONE);
            findViewById(R.id.main_layout).setVisibility(View.VISIBLE);
            game.clear();
            Tutorial T=new Tutorial(this,game);
        }
        else if(id==R.id.nav_play){
            findViewById(R.id.settings_layout).setVisibility(View.GONE);
            findViewById(R.id.main_layout).setVisibility(View.VISIBLE);
            game.clear();
            findViewById(R.id.lesson_layout).setVisibility(View.GONE);
        }
        else if(id==R.id.nav_problem){
            findViewById(R.id.settings_layout).setVisibility(View.GONE);
            findViewById(R.id.main_layout).setVisibility(View.VISIBLE);
            SgfWorker sgf = new SgfWorker();
            game.clear();
            sgf.loadBoardStateFromSgf(this,"lesson1_state0.sgf",game.getGameState());
        }
        else if(id==R.id.nav_settings){
            findViewById(R.id.main_layout).setVisibility(View.GONE);
            findViewById(R.id.settings_layout).setVisibility(View.VISIBLE);
            RadioGroup boardRadio,stoneRadio;
            boardRadio=findViewById(R.id.radiosBoard);
            boardRadio.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    switch (checkedId){
                        case R.id.radioButtonBoard1:
                            boardTexture="board_texture_8";
                            break;
                        case R.id.radioButtonBoard2:
                            boardTexture="board_texture_5";
                            break;
                        case R.id.radioButtonBoard3:
                            boardTexture="board_texture_2";
                            break;
                    }
                    ImageView boardTextureView = findViewById(R.id.board_texture);
                    boardTextureView.setImageResource(getResources().getIdentifier(boardTexture,"drawable",getPackageName()));

                }
            });
            stoneRadio=findViewById(R.id.stonesRadios);
            stoneRadio.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    switch (checkedId){
                        case R.id.radioStones1:
                            game.setBlackStoneImg("stone_white_1");
                            game.setWhiteStoneImg("stone_black_1");
                            break;
                        case R.id.radioStones2:
                            game.setBlackStoneImg("stone_white_2");
                            game.setWhiteStoneImg("stone_black_2");
                            break;
                        case R.id.radioStones3:
                            game.setBlackStoneImg("stone_white_3");
                            game.setWhiteStoneImg("stone_black_3");
                            break;
                    }

                }
            });
        }
        else if(id==R.id.nav_share){
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, "Го играть в Го? Смотри, вот приложение с обучением! \nhttp://pe5ha.me/");
            sendIntent.setType("text/plain");
            startActivity(sendIntent);
        }


        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

//    public interface OnMoveChangeListener{
//        public void onMoveChanged(int turn);
//    }

}
