package com.example.hcnucai.game2048;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteAbortException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterActivity extends Activity {
      private EditText usernameEditText;
      private EditText passwordEditText;
      private EditText configPasswordEditText;
      private String username,password,configPassword;
      //注册按钮
    private Button registerBtn;
    //数据库使用
    private MyDatabaseHelper dbhelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        usernameEditText = (EditText)findViewById(R.id.userName);
        passwordEditText = (EditText)findViewById(R.id.password);
        configPasswordEditText = (EditText)findViewById(R.id.configPassword);
        registerBtn = (Button) findViewById(R.id.register);
        dbhelper = new MyDatabaseHelper(this,"Person.db",null,1);
        //点击事件
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                username = usernameEditText.getText().toString();
                password = passwordEditText.getText().toString();
                configPassword = configPasswordEditText.getText().toString();
                if(password.equals(configPassword)){
                 //进行数据库的插入
                    SQLiteDatabase db = dbhelper.getReadableDatabase();
                    ContentValues values = new ContentValues();
                    values.put("username",username);
                    values.put("password",password);
                    values.put("highscore",0);
                    //查看是否插入正确
                    Long l = db.insert("person", null, values);
                         if(l != -1) {
                             AlertDialog.Builder dialog = new AlertDialog.Builder(RegisterActivity.this);
                             dialog.setTitle("恭喜您");
                             dialog.setMessage("注册成功");
                             dialog.setCancelable(false);
                             dialog.setPositiveButton("确定", null);
                             dialog.show();
                             //到mainActivity中
                             Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                             intent.putExtra("username", username);

                        startActivity(intent);
                    }else{
                        AlertDialog.Builder dialog = new AlertDialog.Builder(RegisterActivity.this);
                        dialog.setTitle("提醒");
                        dialog.setMessage("该用户已经存在");
                        dialog.setCancelable(false);
                        dialog.setPositiveButton("确定",null);
                        dialog.show();
                    }

                }else{
                    //提醒用户输入不相同
                    AlertDialog.Builder dialog = new AlertDialog.Builder(RegisterActivity.this);
                    dialog.setTitle("提醒");
                    dialog.setMessage("两次密码输入不相同");
                    dialog.setCancelable(false);
                    dialog.setPositiveButton("确定",null);
                    dialog.show();
                }
            }
        });




    }
}
