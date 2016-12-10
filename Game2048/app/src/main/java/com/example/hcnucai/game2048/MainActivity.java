package com.example.hcnucai.game2048;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
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
    //soundPool 实时音乐播放器
    private SoundPool soundPool;
    private  boolean flag = false;
    private int soundId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       soundPool = new SoundPool(5, AudioManager.STREAM_MUSIC,0);
     //加载声音
      soundId = soundPool.load(this,R.raw.merge,1);
        //为声音池设定加载完成的监听事件
       soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
           @Override
           public void onLoadComplete(SoundPool soundPool, int i, int i1) {
               flag = true;
           }
       });
        //背景音乐的设置
        backGroundService = new Intent(MainActivity.this,MusicService.class);
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
        //得分榜的设置
        rankBtn = (Button)findViewById(R.id.rank);
        rankBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,RankActivity.class);
                startActivity(intent);
            }
        });
        //背景音乐是否开启
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
        //碰撞声音是否开启
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

    }
//增加分数
    public void addScore(int s){
        if(isPlayCrash){
            if(flag)
                soundPool.play(soundId,1.0f, 0.5f, 1, 0, 1.0f);
            else
                Log.d("soundPlay","正在加载中");
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
    //6.使用完全后，应该释放资源
    @Override
    protected void onDestroy() {
        soundPool.release();
        soundPool = null;
        super.onDestroy();
    }
}
