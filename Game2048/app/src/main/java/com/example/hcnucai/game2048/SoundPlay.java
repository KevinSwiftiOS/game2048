package com.example.hcnucai.game2048;

import java.util.HashMap;
import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

public class SoundPlay {

    SoundPool soundPool;//创建一个SoundPool的类
    HashMap<Integer,Integer> soundPoolMap;//HashMap类将音乐对应到键值
    int streamVolume;

    public SoundPlay(Context context){
        initSoundPool(context);
    }//context为调用该SoundPlay的类，调用SoundPlay类的这个类继承自View

    public void initSoundPool(Context context){
        soundPool = new SoundPool(100,AudioManager.STREAM_MUSIC,1);
        soundPoolMap=new HashMap<Integer,Integer>();
        AudioManager audioManager=(AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
        streamVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        addSoundToHashMap(context);
    }//使用context获得当前手机的声音

    public void addSoundToHashMap(Context context){


    }//将你的音乐文件添加到HashMap中，并且使SoundPool加载该音乐文件
    public void playSound(int sound,int num){
        soundPool.play(soundPoolMap.get(sound), streamVolume, streamVolume, 1, num, 1);
    }//使用HashMap中的键值来播放音乐
}

