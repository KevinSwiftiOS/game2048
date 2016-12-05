package com.example.hcnucai.game2048;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class RankActivity extends AppCompatActivity {
    private TextView totalPersons;
    private ListView listView;
    private List<Person> persons = new ArrayList<>();
    private PersonAdapter adapter;
    //数据库使用
    private MyDatabaseHelper dbhelper;
    private  int cnt = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rank);
       //进行初始化

        dbhelper = new MyDatabaseHelper(this,"Person.db",null,1);
        this.persons.clear();
        initPerson();
        adapter = new PersonAdapter(RankActivity.this,R.layout.person_item,persons);
        //listView赋值
        listView = (ListView)findViewById(R.id.listView);
        listView.setAdapter(adapter);
        totalPersons = (TextView)findViewById(R.id.totalPersons);
        totalPersons.setText("当前共有"+ String.valueOf(cnt) + "位玩家");
    }
    private void initPerson() {
        //查询数据库 随后添加到person中
        SQLiteDatabase db = dbhelper.getWritableDatabase();
        //查询联系人的所有数据
        Cursor cursor = db.query("person", null, null, null, null, null, "highscore desc");
        if (cursor.moveToFirst()) {
            do {
                //遍历cursor对象 取出数据随后添加到person中
                String username = cursor.getString(cursor.getColumnIndex("username"));
                String highscore = cursor.getString(cursor.getColumnIndex("highscore"));

                Person person = new Person();
              person.setUsername(username);
              person.setHighScore(highscore);
                cnt++;
                this.persons.add(person);
            } while (cursor.moveToNext());
        }
    }
}
