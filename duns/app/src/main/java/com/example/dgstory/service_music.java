package com.example.dgstory;

import android.app.Activity;
import android.media.MediaPlayer;

/**
 * Created by user on 2017-10-02.
 */

public class service_music {
    MediaPlayer mediaPlayer;
    public static boolean setting = true;   //배경음 on/off

    public service_music(Activity at, Integer musicNmae){
            //액티비티 및 R.raw.파일이름 설정
            mediaPlayer = MediaPlayer.create(at, musicNmae);
            mediaPlayer.setLooping(true);
    }

    public void start_music(){
        mediaPlayer.start();
    }

    public void pause_music(){
        mediaPlayer.pause();
    }

    public void stop_music(){
        mediaPlayer.release();
    }
}
