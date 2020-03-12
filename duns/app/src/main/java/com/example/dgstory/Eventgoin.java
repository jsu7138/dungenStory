package com.example.dgstory;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;

public class Eventgoin extends AppCompatActivity {
    TextView tv;
    String explanation;
    int count=0,hand=0,stageNum=0;
    service_BattleScense intentBattle;
    service_CharacterInfo saveInfo = new service_CharacterInfo();;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eventgoin);

        Intent i = getIntent();
        stageNum = i.getIntExtra("num",0);

        tv = (TextView) findViewById(R.id.eventgoin_text);
        intentBattle = new service_BattleScense(Eventgoin.this);

        //랜덤좀
        double dice = rand();
        //좋은일 sp 덜소모
        if(dice <= 33.4){
            explanation="뭔가 좋은 일이 일어날것 같다.\n";
            tv.setText(explanation);
            double dice2 = rand();
            if(dice2 <= 33.0){
                explanation="먼지로 덮힌 신상을 발견했다.\n여신이 그대를 축복한다.";
                saveInfo.eventcheck(10,0,0,0,0,0,0,0,Eventgoin.this);
            }
            else if(dice2 > 33.0 && dice2 <= 66.0 ){
                explanation="깨끗한 샘을 발견했다. \n 물을 마시며,잠시 쉬자 피로가 치유된다.";
                saveInfo.eventcheck(10,0,0,0,0,0,0,0,Eventgoin.this);

            }
            else{
                explanation="최적의 몸상태가 되었다.\n가벼운 발걸음으로 앞으로 나아간다";
                saveInfo.eventcheck(10,0,0,0,0,0,0,0,Eventgoin.this);
            }


        }
        //나쁜일 sp 더소모
        else if(dice >33.4 && dice <=66.7){
            explanation="뭔가 안 좋은 일이 일어날것 같다.\n";
            tv.setText(explanation);

            double dice2 = rand();
            if(dice2 <= 33.0){
                explanation="축축한 기운과 귀를 찌르는 비명소리가 당신을 괴롭힌다.\n피로해지는것을 느끼며 앞으로 나아간다";
                saveInfo.eventcheck(0,0,0,0,0,0,3,0,Eventgoin.this);
            }
            else if(dice2 > 33.0 && dice2 <= 66.0 ){
                explanation="고약한 냄새가 코를 찌른다.\n머리가 어지러워진다.";
                saveInfo.eventcheck(0,0,0,0,0,0,3,0,Eventgoin.this);
            }
            else{
                explanation="전투의 피로를 느낀다\n발걸음이 무거워진다.";
                saveInfo.eventcheck(0,0,0,0,10,0,1,0,Eventgoin.this);
            }
        }
        //무슨일이
        else{
            explanation="무슨일이 일어날것 같다.\n";
            tv.setText(explanation);

            double dice2 = rand();
            if(dice2 <= 20.0){
                explanation="구석에서 잠시 눈을 붙였다.\n 그 틈에 고블린이 금화를 훔쳐 달아났다";
                service_CharacterInfo.setMyMoney(service_CharacterInfo.getMyMoney()*80/100);
            }
            else if(dice2 > 20.0 && dice2 <= 30.0 ){
                explanation="구석에서 잠시 휴식을 취한다\n구석에서 오래된 돈주머니를 발견했다";
                service_CharacterInfo.setMyMoney(service_CharacterInfo.getMyMoney()+5000);
            }
            else if(dice2 > 30.0 && dice2 <= 50.0){
                explanation="먼지로 덮힌 이름모를 신상에 기도를 드렸다\n소름끼치는 기운이 신체로 퍼진다";
                saveInfo.eventcheck(0,0,0,0,0,20,0,100,Eventgoin.this);
            }
            else if(dice2 > 50.0 && dice2 <= 70.0){
                explanation="구석에서 잠시 휴식을 취한다\n갑작스런 마물의 습격으로 금화주머니를 잃어버렸다.";
                service_CharacterInfo.setMyMoney(service_CharacterInfo.getMyMoney()*30/100);
            }
            else if(dice2 > 70.0 && dice2 <= 80.0){
                explanation="잠시 휴식을 취한다\n마물의 습격이 있었지만 경계를 한 덕에 모두 쓰러트릴수 있었다";
                saveInfo.eventcheck(0,0,0,0,500,20,0,00,Eventgoin.this);
            }
            else if(dice2 > 80.0 && dice2 <= 95.0){
                explanation="오래된 항아리를 발견했다\n항아리를 열자 뱀이 팔을 물었다";
                saveInfo.eventcheck(0,0,0,0,0,50,0,00,Eventgoin.this);
            }
            else{
                explanation="오래된 항아리를 발견했다\n항아리를 열려고하자 바닥이 열리면서 함정에 떨어졌다";
                saveInfo.eventcheck(0,0,0,0,0,100,0,100,Eventgoin.this);
            }
        }

        mHandler.sendEmptyMessage(0);
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

    // 중요한 메소드 1부터 100 랜덤함수염
    private double rand(){
        Random random = new Random();
        int r = random.nextInt(1000);
        r = r / 10;
        double percentage = (double) r / 100.0 * 100.0;
        return percentage;
    }

    //핸들러 작동
    Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            if(count>=explanation.length() && hand==0) {
                hand=1;
            }
            else if(hand ==1){
                hand = 2;
                intentBattle.intentScense(stageNum);
                finish();
                mHandler.removeMessages(0);
            }
            else if(count<explanation.length() && hand==0)
            {
                String s1 = explanation.substring(count,count+1);

                tv.append(String.valueOf(s1));
                count=count+1;
            }
            // 메세지를 처리하고 또다시 핸들러에 메세지 전달 (1000ms 지연)
            if(hand==0)mHandler.sendEmptyMessageDelayed(0,200);
            if(hand==1)mHandler.sendEmptyMessageDelayed(0,2000);;
            if(hand==2);;
        }
    };
}
