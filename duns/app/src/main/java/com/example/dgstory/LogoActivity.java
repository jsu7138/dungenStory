package com.example.dgstory;

import android.content.Intent;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class LogoActivity extends AppCompatActivity {

    int count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logo);

        count = 3500;

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(LogoActivity.this,TitleActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
                finish();
            }
        },count);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        clearApplicationCache(null);
    }
    //파일 캐시 삭제
    private void clearApplicationCache(java.io.File dir){

        if(dir == null){
            dir = getCacheDir();
            return;
        }

        java.io.File[] dirs = dir.listFiles();

        try{
            for (int i = 0; i < dirs.length ; i++){
                if(dirs[i].isDirectory())
                    clearApplicationCache(dirs[i]);
                else
                    dirs[i].delete();
            }
        }catch (Exception e){
        }
    }
}
