package com.example.dgstory;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;

import static com.example.dgstory.MyHelper.QUERY_SELECT_ALL;
import static com.example.dgstory.MyHelper.TABLE_NAME;

public class LodingActivity extends AppCompatActivity {

    service_CharacterInfo myData;
    boolean selectedCharacter = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loding);

        ImageView img = (ImageView) findViewById(R.id.img_loding);

        Animation anim = AnimationUtils.loadAnimation(LodingActivity.this, R.anim.fade_lotation);
        img.setAnimation(anim);

        myData = new service_CharacterInfo();
        loadDb();
        loadFile();

    }

    //*정보 불러오기------------------------------------------------------------------------------
    public void loadFile(){
        SharedPreferences pref = getSharedPreferences("myGame", Activity.MODE_PRIVATE);
        myData.setMyName(pref.getString("myName",""));
        myData.setMyCharacterNum(pref.getInt("myCharacterNum",1));
        myData.setMyWeaponNum(pref.getInt("myWeaponNum",1));
        myData.setMyArmorNum(pref.getInt("myArmorNum",1));
        myData.setMyArtefactNum(pref.getInt("myArtefactNum",0));

        myData.setMyATK(pref.getInt("myATK",20));
        myData.setMyF_ATK(pref.getInt("myF_ATK",0));
        myData.setMyDEF(pref.getInt("myDEF",8));
        myData.setMySpeed(pref.getInt("mySpeed",100));
        myData.setMyA_Speed(pref.getInt("myA_Speed",110));
        myData.setMyLevel(pref.getInt("myLevel",1));
        myData.setMyHit(pref.getInt("myHit",95));
        myData.setMyMaxHp(pref.getInt("myMaxHp",100));
        myData.setMyMaxMp(pref.getInt("myMaxMp",5));
        myData.setMyMaxSp(pref.getInt("myMaxSp",20));
        myData.setMyCurrentHp(pref.getInt("myCurrentHp",100));
        myData.setMyCurrentMp(pref.getInt("myCurrentMp",5));
        myData.setMyCurrentSp(pref.getInt("myCurrentSp",20));

        myData.setMySkill1(pref.getInt("mySkill1",2));
        myData.setMySkill2(pref.getInt("mySkill2",3));
        myData.setMySkill3(pref.getInt("mySkill3",0));

        myData.skill1_1 = pref.getInt("mySkill1_1",0);
        myData.skill1_2 = pref.getInt("mySkill1_2",0);
        myData.skill1_3 = pref.getInt("mySkill1_3",0);
        myData.skill1_4 = pref.getInt("mySkill1_4",0);
        myData.skill1_5 = pref.getInt("mySkill1_5",0);
        myData.skill1_6 = pref.getInt("mySkill1_6",0);
        myData.skill2_1 = pref.getInt("mySkill2_1",0);
        myData.skill2_2 = pref.getInt("mySkill2_2",0);
        myData.skill2_3 = pref.getInt("mySkill2_3",0);
        myData.skill2_4 = pref.getInt("mySkill2_4",0);
        myData.skill2_5 = pref.getInt("mySkill2_5",0);
        myData.skill2_6 = pref.getInt("mySkill2_6",0);
        myData.skill3_1 = pref.getInt("mySkill3_1",0);
        myData.skill3_2 = pref.getInt("mySkill3_2",0);
        myData.skill3_3 = pref.getInt("mySkill3_3",0);

        myData.setMyExp(pref.getInt("myMaxExp",0));
        myData.setMyMoney(pref.getInt("myMoney",99999));
        myData.setMyStage(pref.getInt("myMyStage",15));
        myData.setMyDays(pref.getInt("myDays",1));

        myData.setMyAtkLevel(pref.getInt("myAtkLevel",0));
        myData.setMyDefLevel(pref.getInt("myDefLevel",0));
        myData.setMySpeedLevel(pref.getInt("mySpeedLevel",1));
        myData.setMyHpLevel(pref.getInt("myHpLevel",1));
        myData.setMyMpLevel(pref.getInt("myMpLevel",1));

        //계정 생성
        if("".equals(myData.myName)){
            newAacount();
        }
        //계정 로드
        else {
            loadingData();
        }
    }

    public void  loadDb(){
        MyHelper mHelper = new MyHelper(LodingActivity.this);
        SQLiteDatabase db = mHelper.getWritableDatabase();

        Cursor cursor = db.rawQuery(QUERY_SELECT_ALL+"",null);
        cursor.moveToFirst();

        //DB첫 생성시
        if(cursor.getInt(0)==0){
            String query = String.format("INSERT INTO %s VALUES ('%d','%s','%s');", TABLE_NAME, 1, 1, "open");
            db.execSQL(query);
            for(int i=2;i<=40;i++) {
                int j=i;
                j= j%10;
                if(j==0){
                    query = String.format("INSERT INTO %s VALUES ('%d','%s','%s');", TABLE_NAME, i, 10, "close");
                    db.execSQL(query);
                }
                else {
                    query = String.format("INSERT INTO %s VALUES ('%d','%s','%s');", TABLE_NAME, i, j, "close");
                    db.execSQL(query);
                }
            }
        }
    }
    //*새로 만들기--------------------------------------------------------------------------------
    public void newAacount(){
        DialogInterface dialogInterface = null;
        final View dialogView = View.inflate(LodingActivity.this,R.layout.dialog_createid,null);
        final AlertDialog.Builder alert = new AlertDialog.Builder(LodingActivity.this);
        alert.setView(dialogView);
        alert.setCancelable(false);
        alert.create();

        dialogInterface = alert.show();

        ImageView btn = (ImageView) dialogView.findViewById(R.id.btn_userDialog);
        final DialogInterface finalDialogInterface = dialogInterface;
        btn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    ((ImageView) view).setColorFilter(0xaa111111);
                }
                else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    ((ImageView) view).setColorFilter(0x00000000);
                    EditText edt = (EditText) dialogView.findViewById(R.id.edt_userDialog);
                    service_toast st = new service_toast(LodingActivity.this,"");
                    String MyName = edt.getText().toString();
                    if("".equals(MyName)){
                        st.setDial("입력되지 않았습니다!");
                        st.toast_start();
                    }else if(!("".equals(MyName))) {
                        finalDialogInterface.dismiss();
                        newAacount(MyName);
                    }

                    return false;
                }
                return true;
            }
        });
    }

    public void newAacount(final String checkedName){
        DialogInterface dialogInterface = null;
        final View dialogView = View.inflate(LodingActivity.this,R.layout.dialog_createid2,null);
        final AlertDialog.Builder alert = new AlertDialog.Builder(LodingActivity.this);
        alert.setView(dialogView);
        alert.setCancelable(false);
        alert.create();

        dialogInterface = alert.show();
        ImageView btn_right = (ImageView) dialogView.findViewById(R.id.btn_create_right);
        ImageView btn_left = (ImageView) dialogView.findViewById(R.id.btn_create_left);
        ImageView btn_ok = (ImageView) dialogView.findViewById(R.id.btn_create_ok);
        final ImageView warrior = (ImageView) dialogView.findViewById(R.id.img_warrior) ;
        final ImageView archer = (ImageView) dialogView.findViewById(R.id.img_archer) ;
        final ImageView tv_warrior = (ImageView) dialogView.findViewById(R.id.tv_warrior);
        final ImageView tv_archer = (ImageView) dialogView.findViewById(R.id.tv_archer);

        btn_left.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    ((ImageView) view).setColorFilter(0xaa111111);
                }
                else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    ((ImageView) view).setColorFilter(0x00000000);
                    selectedCharacter = !selectedCharacter;
                    if(selectedCharacter == true) {
                        warrior.setVisibility(View.VISIBLE);
                        archer.setVisibility(View.INVISIBLE);
                        tv_warrior.setVisibility(View.VISIBLE);
                        tv_archer.setVisibility(View.INVISIBLE);
                    }else{
                        warrior.setVisibility(View.INVISIBLE);
                        archer.setVisibility(View.VISIBLE);
                        tv_warrior.setVisibility(View.INVISIBLE);
                        tv_archer.setVisibility(View.VISIBLE);
                    }
                }
                return true;
            }
        });

        btn_right.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    ((ImageView) view).setColorFilter(0xaa111111);
                }
                else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    ((ImageView) view).setColorFilter(0x00000000);
                    selectedCharacter = !selectedCharacter;
                    if(selectedCharacter == true) {
                        warrior.setVisibility(View.VISIBLE);
                        archer.setVisibility(View.INVISIBLE);
                        tv_warrior.setVisibility(View.VISIBLE);
                        tv_archer.setVisibility(View.INVISIBLE);
                    }else{
                        warrior.setVisibility(View.INVISIBLE);
                        archer.setVisibility(View.VISIBLE);
                        tv_warrior.setVisibility(View.INVISIBLE);
                        tv_archer.setVisibility(View.VISIBLE);
                    }
                }
                return true;
            }
        });

        final DialogInterface finalDialogInterface = dialogInterface;
        btn_ok.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    ((ImageView) view).setColorFilter(0xaa111111);
                }
                else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    ((ImageView) view).setColorFilter(0x00000000);
                    SharedPreferences pref = getSharedPreferences("myGame", Activity.MODE_PRIVATE);
                    if(selectedCharacter == true) {
                        myData.setMyCharacterNum(1);
                        //전사 기본 스텟
                        myData.setMyATK(20);
                        myData.setMyF_ATK(0);
                        myData.setMyDEF(8);
                        myData.setMySpeed(100);
                        myData.setMyA_Speed(0);
                        myData.setMyLevel(1);
                        myData.setMyHit(95);
                        myData.setMyMaxHp(100);
                        myData.setMyMaxMp(5);
                        myData.setMyMaxSp(20);
                        myData.setMyCurrentHp(100);
                        myData.setMyCurrentMp(5);
                        myData.setMyCurrentSp(20);
                        myData.setMyMoney(99999);


                    }else{
                        myData.setMyCharacterNum(2);
                        //궁수 기본 스텟
                        myData.setMyATK(pref.getInt("myATK",35));
                        myData.setMyF_ATK(pref.getInt("myF_ATK",0));
                        myData.setMyDEF(pref.getInt("myDEF",3));
                        myData.setMySpeed(pref.getInt("mySpeed",110));
                        myData.setMyA_Speed(pref.getInt("myA_Speed",100));
                        myData.setMyLevel(pref.getInt("myLevel",1));
                        myData.setMyHit(pref.getInt("myHit",99));
                        myData.setMyMaxHp(pref.getInt("myMaxHp",80));
                        myData.setMyMaxMp(pref.getInt("myMaxMp",10));
                        myData.setMyMaxSp(pref.getInt("myMaxSp",20));
                        myData.setMyCurrentHp(pref.getInt("myCurrentHp",80));
                        myData.setMyCurrentMp(pref.getInt("myCurrentMp",10));
                        myData.setMyCurrentSp(pref.getInt("myCurrentSp",20));
                    }
                    myData.skill1_1 = 0;
                    myData.skill1_2 = 0;
                    myData.skill1_3 = 3;
                    myData.skill1_4 = 0;
                    myData.skill1_5 = 5;
                    myData.skill1_6 = 0;
                    myData.skill2_1 = 0;
                    myData.skill2_2 = 0;
                    myData.skill2_3 = 1;
                    myData.skill2_4 = 0;
                    myData.skill2_5 = 3;
                    myData.skill2_6 =0;
                    myData.skill3_1 = 0;
                    myData.skill3_2 = 0;
                    myData.skill3_3 = 0;
                    myData.setMyName(checkedName);
                    finalDialogInterface.dismiss();
                    loadingData();
                }
                return true;
            }
        });
    }

    //*불러오기 및 계정 생성 성공-----------------------------------------------------------------
    public void loadingData(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                Intent i = new Intent(LodingActivity.this,LobbyActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
                finish();
            }
        }, 3000);
    }

    //*파일 캐시 삭제-----------------------------------------------------------------------------
    @Override
    public void onDestroy() {
        super.onDestroy();
        clearApplicationCache(null);
    }

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
