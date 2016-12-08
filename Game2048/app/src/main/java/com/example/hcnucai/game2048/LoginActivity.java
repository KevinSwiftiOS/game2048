package com.example.hcnucai.game2048;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.widget.Toast;
import android.os.Binder;
import android.os.IBinder;

import android.app.Activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.example.hcnucai.game2048.R;

public class LoginActivity extends Activity {
   //登录
    private EditText usernameEditText;
    private  EditText passwordEditText;
    private  String username,password;
    private  Button loginBtn;
    private  TextView register;
    //数据库使用
    private MyDatabaseHelper dbhelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        dbhelper = new MyDatabaseHelper(this,"Person.db",null,1);
        usernameEditText = (EditText)findViewById(R.id.username);
        passwordEditText = (EditText)findViewById(R.id.password);
        loginBtn = (Button)findViewById(R.id.login);
        register = (TextView)findViewById(R.id.register);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            //注册
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });
        //登录
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //进行数据库的查询
                username = usernameEditText.getText().toString();
                password = passwordEditText.getText().toString();
                SQLiteDatabase db = dbhelper.getWritableDatabase();
                Cursor cursor = db.query("person",new String[]{"password"},"password = ?",new String []{username},null,null,null);
                if(cursor.moveToNext()){
                    String dataBasePassword = cursor.getString(cursor.getColumnIndex("password"));
                    if(dataBasePassword.equals(password)) {
                        //登录成功
                        Intent musicService = new Intent(LoginActivity.this,MusicService.class);
                        startService(musicService);
                        Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                        intent.putExtra("username",username);
                        startActivity(intent);
                    }else{
                        //密码输入失败
                        AlertDialog.Builder dialog = new AlertDialog.Builder(LoginActivity.this);
                        dialog.setTitle("提醒");
                        dialog.setMessage("输入密码不正确");
                        dialog.setCancelable(false);
                        dialog.setPositiveButton("确定",null);
                        dialog.show();
                    }
                }else{
                  //没有这个用户
                    AlertDialog.Builder dialog = new AlertDialog.Builder(LoginActivity.this);
                    dialog.setTitle("提醒");
                    dialog.setMessage("该用户不存在");
                    dialog.setCancelable(false);
                    dialog.setPositiveButton("确定",null);
                    dialog.show();
                }
           }
       });
    }


}
