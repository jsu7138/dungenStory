package com.example.dgstory;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;

public class gameOverActivity extends AppCompatActivity {

    service_music m;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);

        MyHelper mHelper = new MyHelper(getApplicationContext());
        SQLiteDatabase db = mHelper.getWritableDatabase();
        mHelper.delete();
        SharedPreferences pref = getSharedPreferences("myGame", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("myName","");
        editor.commit();

            m = new service_music(this, R.raw.game_over);
            m.start_music();

        if(!m.setting){
            m.pause_music();
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(gameOverActivity.this, TitleActivity.class);
                m.stop_music();
                m = null;
                startActivity(i);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                finish();
            }
        },10000);
    }
}
