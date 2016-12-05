package com.example.hcnucai.game2048;
import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.GridLayout;

public class GameView extends GridLayout {
    //用户名
    private  String username;
    //总分 目前有多少分
    private  int totalScore;
    //数据库使用
    private MyDatabaseHelper dbhelper;

    public void setDbhelper(MyDatabaseHelper dbhelper) {
        this.dbhelper = dbhelper;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    //卡片的数组 4*4的数组
    private Card[][] cardsMap = new Card[4][4];
    //记录位置的数组
    private List<Point> emptyPoints = new ArrayList<Point>();
    //初始化方法 都要使用到initGameView这个方法
    public GameView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        initGameView();
    }

    public GameView(Context context) {
        super(context);

        initGameView();
    }

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);

        initGameView();
    }

    private void initGameView(){

        //设置为4行的大小 背景颜色设置好
        setColumnCount(4);
       setBackgroundColor(0xffbbada0);

      //监听用户的触摸事件
        setOnTouchListener(new View.OnTouchListener() {
            //记录下开始点下的位置 和离开后的偏移量
            private float startX,startY,offsetX,offsetY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        startX = event.getX();
                        startY = event.getY();
                        break;
                    case MotionEvent.ACTION_UP:
                        offsetX = event.getX()-startX;
                        offsetY = event.getY()-startY;

                       //水平方向上的移动大于垂直方向上的移动 说明是在左右方向上进行改变
                        if (Math.abs(offsetX)>Math.abs(offsetY)) {
                            //如果偏移量小于-5 自定义的-5 则说明是向左边滑的
                            if (offsetX<-5) {
                                swipeLeft();
                            }else if (offsetX>5) {
                                swipeRight();
                            }
                        }else{
                            if (offsetY<-5) {
                                swipeUp();
                            }else if (offsetY>5) {
                                swipeDown();
                            }
                        }

                        break;
                }
                return true;
            }
        });
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        //当第一次初始化的时候 就增加卡片 这里这个方法只会调用一次 因为在androidManifest.xml文件中规定了方向只能是竖直方向
        super.onSizeChanged(w, h, oldw, oldh);
        //计算宽度 -10 是因为要留边距
        int cardWidth = (w)/4;
        int cardHeight = (h)/4;
     //加入的是一个正方形
        addCards(cardWidth,cardHeight);

        startGame();
    }

    private void addCards(int cardWidth,int cardHeight){

        Card c;

        for (int y = 0; y < 4; y++) {
            for (int x = 0; x < 4; x++) {
                c = new Card(getContext());
                //刚开始的方格里面是没有设值的，因此只需赋初始值为0即可
                c.setNum(0);
                addView(c, cardWidth, cardHeight);
                //添加到视图中并且用卡片数组记录
                cardsMap[x][y] = c;
            }
        }
    }

    public void startGame(){
       //开始游戏 分数清零 随后每张卡片设置值为0
        MainActivity.getMainActivity().clearScore();

        for (int y = 0; y < 4; y++) {
            for (int x = 0; x < 4; x++) {
                cardsMap[x][y].setNum(0);
            }
        }
       //设置两张卡片是有值的 加两个随机数即可
        addRandomNum();
        addRandomNum();
        //重新获取最高分
        MainActivity.getMainActivity().initHighScore();
    }

    private void addRandomNum(){
 //用一个list来记录到底哪里需要显示出来值 哪里不需要
        emptyPoints.clear();

        for (int y = 0; y < 4; y++) {
            for (int x = 0; x < 4; x++) {
                if (cardsMap[x][y].getNum()<=0) {
                    //当当前的值小于等于0的时候 说明当前这个位置是没有值的 就可以有值产生
                    emptyPoints.add(new Point(x, y));
                }
            }
        }
          //移除出一个数组 产生0 - 1之间的一个随机数 随后乘这个list的大小 就表明这个位置要产生数
        Point p = emptyPoints.remove((int)(Math.random()*emptyPoints.size()));
        //已9：1的概率来产生2还是4
        cardsMap[p.x][p.y].setNum(Math.random()>0.1?2:4);
    }

///上下左右逻辑相同 只需注释一个即可
    private void swipeLeft(){
        //数据库插入







                //判断是合并了 还是移动位置了 这时候就要产生新的数
        boolean merge = false;

        for (int y = 0; y < 4; y++) {
            for (int x = 0; x < 4; x++) {

                for (int x1 = x+1; x1 < 4; x1++) {
                    //访问到当前有一个大于0的数
                    if (cardsMap[x1][y].getNum()>0) {
                         //当前这个位置为空 则移过来
                        if (cardsMap[x][y].getNum()<=0) {
                            cardsMap[x][y].setNum(cardsMap[x1][y].getNum());
                            cardsMap[x1][y].setNum(0);

                            x--;

                            merge = true;
                            //两个值相同 可以合并
                        }else if (cardsMap[x][y].equals(cardsMap[x1][y])) {
                            cardsMap[x][y].setNum(cardsMap[x][y].getNum()*2);
                            cardsMap[x1][y].setNum(0);
                            //合并的时候 就可以加上新的分数
                            MainActivity.getMainActivity().addScore(cardsMap[x][y].getNum());
                            totalScore += cardsMap[x][y].getNum();
                            merge = true;
                        }

                        break;
                    }
                }
            }
        }
         //可以产生新的数了 就要判断是不是可以游戏结束了
        if (merge) {
            addRandomNum();
            checkComplete();
        }
    }
    private void swipeRight(){

        boolean merge = false;

        for (int y = 0; y < 4; y++) {
            for (int x = 3; x >=0; x--) {

                for (int x1 = x-1; x1 >=0; x1--) {
                    if (cardsMap[x1][y].getNum()>0) {

                        if (cardsMap[x][y].getNum()<=0) {
                            cardsMap[x][y].setNum(cardsMap[x1][y].getNum());
                            cardsMap[x1][y].setNum(0);

                            x++;
                            merge = true;
                        }else if (cardsMap[x][y].equals(cardsMap[x1][y])) {
                            cardsMap[x][y].setNum(cardsMap[x][y].getNum()*2);
                            cardsMap[x1][y].setNum(0);
                            MainActivity.getMainActivity().addScore(cardsMap[x][y].getNum());
                            totalScore += cardsMap[x][y].getNum();
                            merge = true;
                        }

                        break;
                    }
                }
            }
        }

        if (merge) {
            addRandomNum();
            checkComplete();
        }
    }
    private void swipeUp(){

        boolean merge = false;

        for (int x = 0; x < 4; x++) {
            for (int y = 0; y < 4; y++) {

                for (int y1 = y+1; y1 < 4; y1++) {
                    if (cardsMap[x][y1].getNum()>0) {

                        if (cardsMap[x][y].getNum()<=0) {
                            cardsMap[x][y].setNum(cardsMap[x][y1].getNum());
                            cardsMap[x][y1].setNum(0);

                            y--;

                            merge = true;
                        }else if (cardsMap[x][y].equals(cardsMap[x][y1])) {
                            cardsMap[x][y].setNum(cardsMap[x][y].getNum()*2);
                            cardsMap[x][y1].setNum(0);
                            MainActivity.getMainActivity().addScore(cardsMap[x][y].getNum());
                            totalScore += cardsMap[x][y].getNum();
                            merge = true;
                        }

                        break;

                    }
                }
            }
        }

        if (merge) {
            addRandomNum();
            checkComplete();
        }
    }
    private void swipeDown(){

        boolean merge = false;

        for (int x = 0; x < 4; x++) {
            for (int y = 3; y >=0; y--) {

                for (int y1 = y-1; y1 >=0; y1--) {
                    if (cardsMap[x][y1].getNum()>0) {

                        if (cardsMap[x][y].getNum()<=0) {
                            cardsMap[x][y].setNum(cardsMap[x][y1].getNum());
                            cardsMap[x][y1].setNum(0);

                            y++;
                            merge = true;
                        }else if (cardsMap[x][y].equals(cardsMap[x][y1])) {
                            cardsMap[x][y].setNum(cardsMap[x][y].getNum()*2);
                            cardsMap[x][y1].setNum(0);
                            MainActivity.getMainActivity().addScore(cardsMap[x][y].getNum());
                            totalScore += cardsMap[x][y].getNum();
                            merge = true;
                        }

                        break;
                    }
                }
            }
        }

        if (merge) {
            addRandomNum();
            checkComplete();
        }
    }

    private void checkComplete(){

        boolean complete = true;

        ALL:
        for (int y = 0; y < 4; y++) {
            for (int x = 0; x < 4; x++) {
                //游戏不能结束的原因是当前位置为空 或者当前位置与上下左右其中有一个相同可以合并
                if (cardsMap[x][y].getNum()==0||
                        (x>0&&cardsMap[x][y].equals(cardsMap[x-1][y]))||
                        (x<3&&cardsMap[x][y].equals(cardsMap[x+1][y]))||
                        (y>0&&cardsMap[x][y].equals(cardsMap[x][y-1]))||
                        (y<3&&cardsMap[x][y].equals(cardsMap[x][y+1]))) {

                    complete = false;
                    //跳出两个循环
                    break ALL;
                }
            }
        }

        if (complete) {

//判断当前得分是否比以前高了
            SQLiteDatabase db = dbhelper.getWritableDatabase();
        Cursor cursor = db.query("person",new String[]{"highscore"},"username = ?",new String []{username},null,null,null);



        if(cursor.moveToNext()){
            int dataBasehighScore = cursor.getInt(cursor.getColumnIndex("highscore"));
          if(totalScore > dataBasehighScore){
              //数据库的跟新
              ContentValues values = new ContentValues();
              values.put("highscore",totalScore);
              db.update("person",values,"username = ?",new String[]{username});

              new AlertDialog.Builder(getContext()).setTitle("恭喜您").setMessage("创造新纪录").setPositiveButton("再来一局吧", new DialogInterface.OnClickListener() {

                  @Override
                  public void onClick(DialogInterface dialog, int which) {
                      startGame();



                  }
              }).show();
          }else{
              new AlertDialog.Builder(getContext()).setTitle("你好").setMessage("游戏结束").setPositiveButton("重来", new DialogInterface.OnClickListener() {

                  @Override
                  public void onClick(DialogInterface dialog, int which) {
                      startGame();



                  }
              }).show();
          }
            }
            //游戏是可以结束了 还可以重来

        }

    }
}
