package com.example.hcnucai.game2048;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.os.CpuUsageInfo;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import com.example.hcnucai.game2048.R;

import java.io.IOException;

public class MainActivity extends Activity {
    //分数
    private int score = 0;
    //分数textView
    private TextView tvScore;
   //静态activity实例 方便gameView中进行访问 已进行分数的改变
    private static MainActivity mainActivity = null;
    //初始化方法
    public MainActivity() {
        mainActivity = this;
    }
    GameView gameView;
    //新游戏按钮
    private  Button newGameBtn;
    //数据库使用
    private MyDatabaseHelper dbhelper;
    private String username;
    //最高分
    private TextView highScoreTextView;
    //查看得分榜
    private  Button rankBtn;
    //背景音乐的按钮
    private  Button backMusicBtn;
    //撞击声音的添加
    private Button crashMusicBtn;
    private boolean isPlayCrash = true;
    private boolean isPlay = true;
    private Intent musicService;
    private Intent backGroundService;
    //音乐播放器
   private MediaPlayer mediaPlayer = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        backGroundService = new Intent(MainActivity.this,MusicService.class);
        mediaPlayer = MediaPlayer.create(this, R.raw.merge);
        if(mediaPlayer == null){
            return;
        }
        mediaPlayer.stop();
        mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                mp.release();
                return false;
            }
        });
        try{
            mediaPlayer.prepare();

        } catch (IOException e) {
            e.printStackTrace();

        }
        setContentView(R.layout.activity_main);
        gameView = (GameView)findViewById(R.id.gameView);

        tvScore = (TextView) findViewById(R.id.tvScore);
        newGameBtn = (Button)findViewById(R.id.newGame);
        newGameBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearScore();
                gameView.startGame();
            }
        });
        //将username拿到 随后从数据库中进行查找最高分
        Intent intent = getIntent();
        username = intent.getStringExtra("username");
        gameView.setUsername(username);

        dbhelper = new MyDatabaseHelper(this,"Person.db",null,1);
        gameView.setDbhelper(dbhelper);
        SQLiteDatabase db = dbhelper.getWritableDatabase();

        highScoreTextView = (TextView)findViewById(R.id.highScore);

        Cursor cursor = db.query("person",new String[]{"highscore"},"username = ?",new String []{username},null,null,null);
        if(cursor.moveToNext()){
            int highScore = cursor.getInt(cursor.getColumnIndex("highscore"));
            highScoreTextView.setText(String.valueOf(highScore));
        }
        rankBtn = (Button)findViewById(R.id.rank);
        rankBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,RankActivity.class);
                startActivity(intent);
            }
        });
       backMusicBtn = (Button)findViewById(R.id.playBackMusic);
        backMusicBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isPlay){
                    isPlay = false;

                    stopService(backGroundService);
                }else{
                    isPlay = true;
                    startService(backGroundService);
                }
            }
        });
        crashMusicBtn = (Button)findViewById(R.id.crashBtn);
    crashMusicBtn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            //撞击声音是否有
            isPlayCrash = !isPlayCrash;
        }
    });
    }


  //清除分数的方法
    public void clearScore(){
        score = 0;
        showScore();
    }
//显示分数
    public void showScore(){

        tvScore.setText(score+"");
  mediaPlayer.stop();

    }
//增加分数
    public void addScore(int s){


        if(isPlayCrash){
            

        }
        score+=s;
        showScore();
    }
    //设置最高分 当重置的时候
    public void initHighScore(){
        SQLiteDatabase db = dbhelper.getWritableDatabase();



        Cursor cursor = db.query("person",new String[]{"highscore"},"username = ?",new String []{username},null,null,null);
        if(cursor.moveToNext()){
            int highScore = cursor.getInt(cursor.getColumnIndex("highscore"));
            highScoreTextView.setText(String.valueOf(highScore));
        }
    }

//返回mainActivity的实例化方法
    public static MainActivity getMainActivity() {
        return mainActivity;
    }

}
