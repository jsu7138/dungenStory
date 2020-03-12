package com.example.dgstory;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class TitleActivity extends AppCompatActivity {

    ImageView img,img2;
    service_music m;

    int count,block;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_title);

        count = 2000;       //게임 시작 최소 대기 시간
        block = 1;          //중복 터치로 인한 에러방지
        img2 = (ImageView) findViewById(R.id.main_title);   //DunGeonStroy 이름
        img = (ImageView) findViewById(R.id.main_start);    //Start 버튼
        m = new service_music(this,R.raw.bgm_intro);
        m.start_music();

        setAnime();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                img.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if(block == 1) {
                            Intent i = new Intent(TitleActivity.this, LodingActivity.class);
                            m.stop_music();
                            m = null;

                            startActivity(i);
                            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                            finish();
                            block++;
                        }

                    }
                });
            }
        },count);
    }

    /*----------------------------------------------------------------------------------------------*/
    //애니메이션 설정
    /*----------------------------------------------------------------------------------------------*/
    public void setAnime(){
        Animation animation = AnimationUtils.loadAnimation(TitleActivity.this, R.anim.fade_tn);
        img2.setAnimation(animation);

        Animation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(1000);
        anim.setStartOffset(20);
        anim.setRepeatMode(Animation.REVERSE);
        anim.setRepeatCount(Animation.INFINITE);
        img.startAnimation(anim);


    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        clearApplicationCache(null);
    }
    /*----------------------------------------------------------------------------------------------*/
    //파일 캐시 삭제
     /*----------------------------------------------------------------------------------------------*/
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
