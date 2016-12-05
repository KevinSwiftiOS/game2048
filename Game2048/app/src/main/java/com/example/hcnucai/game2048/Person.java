package com.example.hcnucai.game2048;

/**
 * Created by hcnucai on 2016/12/5.
 */

public class Person {
    //有3个属性 用户名 密码 最高分
    private String username;
    private String password;
    private String highScore;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getHighScore() {
        return highScore;
    }

    public void setHighScore(String highScore) {
        this.highScore = highScore;
    }
}
