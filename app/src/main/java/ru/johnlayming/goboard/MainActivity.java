package ru.johnlayming.goboard;


import android.app.Application;
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
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.Toast;


/*

. доделать адеватно обучение
. доделать адекватно игру с другом
. сохранение игры в сгф
. подсчет очков
. сделать онлайн игру с другом
. позиция камней на больших досках съехала...
. приятные мелочи:
    . звук
    . задержка ответа бота
. счётчик ходов видимый
. чей ход показаель

 */


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
private DrawerLayout drawer;

///////////////////////////////
public ImageView[][] board;
public int size=9;
public int cellSizeInDp=40;
public int pngBoardSizeInDp=360;
public String  pngBoardPath="board9x9";
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
        game = new GameUI(this,size,cellSizeInDp,pngBoardSizeInDp,pngBoardPath);
        //Tutorial T=new Tutorial(this,game); // start from TUTORIAL
        /*------INITIALIZATE-------*/
        findViewById(R.id.info_layout).setVisibility(View.GONE);
        findViewById(R.id.settings_layout).setVisibility(View.GONE);
        findViewById(R.id.lesson_layout).setVisibility(View.GONE);
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
        /* пока что убрал обучение, так как оно говно. Сделаю потом захардкоченое понятное и красивое. А сейчас сделаю красивым просто игру и движок.
        if(id==R.id.nav_learn){
            game.clear();
            game=null;
            game=new GameUI(this,size,cellSizeInDp,pngBoardSizeInDp,pngBoardPath);

            Tutorial T=new Tutorial(this,game);
        }

         */
        if(id==R.id.nav_play){
            findViewById(R.id.info_layout).setVisibility(View.GONE);
            findViewById(R.id.settings_layout).setVisibility(View.GONE);
            findViewById(R.id.lesson_layout).setVisibility(View.GONE);
            findViewById(R.id.main_layout).setVisibility(View.VISIBLE);
            findViewById(R.id.game_btn_layout).setVisibility(View.VISIBLE);
            setTitle("Игра");
            game.clear();

        }
        /* пока что убрал задачи пока они не работают
        else if(id==R.id.nav_problem){

            game.clear();
            Problems P=new Problems(this,game);
        }

         */
        else if(id==R.id.nav_settings){
            findViewById(R.id.info_layout).setVisibility(View.GONE);
            findViewById(R.id.main_layout).setVisibility(View.GONE);
            findViewById(R.id.game_btn_layout).setVisibility(View.GONE);
            findViewById(R.id.settings_layout).setVisibility(View.VISIBLE);
            setTitle("Настройки");
            RadioGroup boardRadio,stoneRadio, sizeBoardRadio;
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
            sizeBoardRadio=findViewById(R.id.radioBoardSize);
            sizeBoardRadio.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    game.clear();
                    game=null;
                    switch (checkedId){
                        case R.id.radioBoardSize1:

                            game=new GameUI(MainActivity.this,9,40,360,"board9x9");
                            break;
                        case R.id.radioBoardSize2:

                            game=new GameUI(MainActivity.this,13,27,351,"board13x13");
                            break;
                        case R.id.radioBoardSize3:

                            game=new GameUI(MainActivity.this,19,19,361,"board19x19");
                            break;
                    }

                }
            });
            Switch switchCoords;
            switchCoords = findViewById(R.id.switch_coordinate);
            switchCoords.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked){
                        findViewById(R.id.coordinates_imageview).setVisibility(View.VISIBLE);
                    }
                    else {
                        findViewById(R.id.coordinates_imageview).setVisibility(View.GONE);
                    }
                }
            });

        }
        else if(id==R.id.nav_share){
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, "Го играть в Го? Смотри, вот приложение с обучением! \nhttps://sourceforge.net/projects/go-game-learn/files/");
            sendIntent.setType("text/plain");
            startActivity(sendIntent);
        }
        else if(id==R.id.nav_exit){
            finish();
            System.exit(0);
        }
        else if(id==R.id.nav_info){
            findViewById(R.id.main_layout).setVisibility(View.GONE);
            findViewById(R.id.settings_layout).setVisibility(View.GONE);
            findViewById(R.id.game_btn_layout).setVisibility(View.GONE);
            findViewById(R.id.info_layout).setVisibility(View.VISIBLE);
            setTitle("Игра Го - Обучение");
        }


        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

//    public interface OnMoveChangeListener{
//        public void onMoveChanged(int turn);
//    }

}
