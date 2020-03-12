package com.example.dgstory;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

/**
 * Created by user on 2017-10-06.
 */

public class service_sound {
    SoundPool soundPool;
    int filename1,filename2,filename3,filename4;
    public static boolean setting;

    public service_sound(Context context) {
        setting = true;
        soundPool = new SoundPool(2, AudioManager.STREAM_MUSIC,0);
        filename1 = soundPool.load(context,R.raw.effect_click,1);
        filename2 = soundPool.load(context,R.raw.effect_up ,1);
        filename3 = soundPool.load(context,R.raw.effect_down,1);
        filename4 = soundPool.load(context,R.raw.effect_save,1);
    }

    public void start_clicked(){
        if(setting == true){
          soundPool.play(filename1,1,1,0,0,1);
        }
    }

    public void start_up(){
        if(setting == true){
            soundPool.play(filename2,1,1,0,0,0.0f);
        }
    }

    public void start_down(){
        if(setting == true){
            soundPool.play(filename3,1,1,0,0,0.0f);
        }
    }

    public void start_svae(){
        if(setting == true){
            soundPool.play(filename4,1,1,0,0,0.0f);
        }
    }
}
