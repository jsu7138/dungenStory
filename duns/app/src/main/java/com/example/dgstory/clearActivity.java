package com.example.dgstory;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import androidx.appcompat.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import static com.badlogic.gdx.math.MathUtils.random;

public class clearActivity extends AppCompatActivity {
    //클리어하 맵 값을 넘겨받는다
    int clearnum,exp,money;int hand=0;int count=0;
    TextView tvmoney;
    TextView tvexp;
    ImageView imgbox;
    ImageView imgok;
    boolean boxon = false;
    int boxi1=0,boxi2=0;
    ImageView myimg,trimg;
    TextView myinfo,trinfo;
    service_CharacterInfo saveInfo = new service_CharacterInfo();
    Integer[][] equipment = {{1,0,5},{1,1,10},{1,2,15},{1,3,25},{1,4,50},{1,5,75},{1,6,100},{1,7,150},{1,8,200},{1,9,300},{2,0,5},{2,1,10},{2,2,20},{2,3,30},{2,4,40},{2,5,50},{2,6,60},{2,7,70},{2,8,100},{2,9,150},
            {3,0,5},{3,1,10,10},{3,2,20,20},{3,3,30,30},{3,4,40,40},{3,5,50,50},{3,6,60,60},{3,7,100,100},{3,8,200,200},{3,9,300,300}};
    Integer[] imageweapon = {R.drawable.icon_weapon0,R.drawable.icon_weapon1,R.drawable.icon_weapon2,R.drawable.icon_weapon3,R.drawable.icon_weapon4,R.drawable.icon_weapon5,R.drawable.icon_weapon6,R.drawable.icon_weapon7,
            R.drawable.icon_weapon8,R.drawable.icon_weapon9};
    Integer[] imagearmor = {R.drawable.icon_armor0,R.drawable.icon_armor1,R.drawable.icon_armor2,R.drawable.icon_armor3,R.drawable.icon_armor4,R.drawable.icon_armor5,R.drawable.icon_armor6,R.drawable.icon_armor7,
            R.drawable.icon_armor8,R.drawable.icon_armor9};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clear);

        final View dialogView = View.inflate(clearActivity.this, R.layout.dialog_clearinfo, null);
        final AlertDialog.Builder alert = new AlertDialog.Builder(clearActivity.this);
        alert.setView(dialogView);
        alert.setCancelable(false);
        alert.create();
        DialogInterface dialogInterface = alert.show();

        Intent i = getIntent();
        clearnum = i.getIntExtra("i",1);

        money= clearnum *1000 + ((clearnum *1000)*random(101)/100);
        exp= ((clearnum * 3) + clearnum)/2;

        saveInfo.setMyMoney(saveInfo.getMyMoney()+money);
        saveInfo.setMyExp(saveInfo.getMyExp()+exp);

        saveMyData();

        saveInfo.levelcheck(clearActivity.this);

        TextView tvnum = (TextView) dialogView.findViewById(R.id.clearinfo_num);
        tvmoney = (TextView) dialogView.findViewById(R.id.clearinfo_money);
        tvexp = (TextView) dialogView.findViewById(R.id.clearinfo_exp);
        imgok = (ImageView) dialogView.findViewById(R.id.clearinfo_ok);
        imgbox = (ImageView) dialogView.findViewById(R.id.clearinfo_box);

        tvnum.setText(String.valueOf(clearnum));

        dialogView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hand =2;
            }
        });



        for(int x = 0; x < clearnum ; x++){
            int y = clearnum/5;

            int dice = 0;
            //int dice = random.nextInt(1000);
            boxi1 = random.nextInt(2) + 1;




            switch(y){
                case 0 :
                    if(dice < (5 - (y * 0.5))){
                        boxon = true;
                        boxi2 = y;
                        x = clearnum;
                    } break;
                case 1 :
                    if(dice < (5 - (y * 0.5))){
                        boxon = true;
                        boxi2 = y;
                        x = clearnum;
                    } break;
                case 2 :
                    if(dice < (5 - (y * 0.5))){
                        boxon = true;
                        boxi2 = y;
                        x = clearnum;
                    } break;
                case 3 :
                    if(dice < (5 - (y * 0.5))){
                        boxon = true;
                        boxi2 = y;
                        x = clearnum;
                    } break;
                case 4 :
                    if(dice < (5 - (y * 0.5))){
                        boxon = true;
                        boxi2 = y;
                        x = clearnum;
                    } break;
                case 5 :
                    if(dice < (5 - (y * 0.5))){
                        boxon = true;
                        boxi2 = y;
                        x = clearnum;
                    } break;
                case 6 :
                    if(dice < (5 - (y * 0.5))){
                        boxon = true;
                        boxi2 = y;
                        x = clearnum;
                    } break;
                case 7 :
                    if(dice < (5 - (y * 0.5))){
                        boxon = true;
                        boxi2 = y;
                        x = clearnum;
                    } break;
                case 8 :
                    if(dice < (5 - (y * 0.5))){
                        boxon = true;
                        boxi2 = y;
                        x = clearnum;
                    } break;
                case 9 :
                    if(dice < (5 - (y * 0.5))){
                        boxon = true;
                        boxi2 = y;
                        x = clearnum;
                    } break;
            }
            if(boxon == true) imgbox.setVisibility(View.VISIBLE);

        }





        mHandler.sendEmptyMessage(0);
    }

    //핸들러 작동
    Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            if(hand==0 && count+100 < exp){
                count+=100;
                tvexp.setText(String.valueOf(count));
            }
            else if(hand==0 && count+10 < exp) {
                count+=10;
                tvexp.setText(String.valueOf(count));
            }
            else if(hand==0 && count < exp) {
                count++;
                tvexp.setText(String.valueOf(count));
            }
            else if(hand ==0 && count >= exp){
                hand =1;
                count=0;
            }
            else if(hand == 1 && count+100 < money)
            {
                count+=100;
                tvmoney.setText(String.valueOf(count));
            }
            else if(hand == 1 && count+10 < money)
            {
                count+=10;
                tvmoney.setText(String.valueOf(count));
            }
            else if(hand == 1 && count < money)
            {
                count++;
                tvmoney.setText(String.valueOf(count));
            }
            else if(hand == 1 && count >= money && boxon == false){
                mHandler.removeMessages(0);
                imgok.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent motionEvent) {
                        if(motionEvent.getAction() == MotionEvent.ACTION_DOWN){
                            ((ImageView) view).setColorFilter(0xaa111111);
                        }
                        else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                            ((ImageView) view).setColorFilter(0x00000000);
                            Intent i = new Intent(clearActivity.this,StageActivity.class);
                            i.putExtra("sum",clearnum);
                            startActivity(i);
                            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                            finish();
                            return false;

                        }
                        return true;
                    }
                });
            }
            else if(hand == 1 && count >= money && boxon == true){
                //아이템을 획득할시

                mHandler.removeMessages(0);
                imgbox.setImageResource(R.drawable.icon_box2);
                hand=3;

                getequipment();
            }
            else if(hand ==2 && boxon ==false){
                tvmoney.setText(String.valueOf(money));
                tvexp.setText(String.valueOf(exp));
                mHandler.removeMessages(0);
                imgok.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent motionEvent) {
                        if(motionEvent.getAction() == MotionEvent.ACTION_DOWN){
                            ((ImageView) view).setColorFilter(0xaa111111);
                        }
                        else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                            ((ImageView) view).setColorFilter(0x00000000);
                            Intent i = new Intent(clearActivity.this,StageActivity.class);
                            i.putExtra("sum",clearnum);
                            startActivity(i);
                            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                            finish();
                            return false;

                        }
                        return true;
                    }
                });
            }
            else if(hand ==2 && boxon ==true){
                //아이템을 획득할시
                tvmoney.setText(String.valueOf(money));
                tvexp.setText(String.valueOf(exp));
                mHandler.removeMessages(0);
                hand=3;
                imgbox.setImageResource(R.drawable.icon_box2);

                getequipment();
            }
            else{
                mHandler.removeMessages(0);
            }

            // 메세지를 처리하고 또다시 핸들러에 메세지 전달 (1000ms 지연)
            if(hand==0)mHandler.sendEmptyMessageDelayed(0,100);
            if(hand==1)mHandler.sendEmptyMessageDelayed(0,100);
            if(hand==2)mHandler.sendEmptyMessageDelayed(0,1000);
            if(hand==3)mHandler.sendEmptyMessageDelayed(0,1000);
        }
    };
    private void saveMyData(){
        SharedPreferences pref = getSharedPreferences("myGame", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("myName",saveInfo.getMyName());
        editor.putInt("myCharacterNum",saveInfo.getMyCharacterNum());
        editor.putInt("myWeaponNum",saveInfo.getMyWeaponNum());
        editor.putInt("myArmorNum",saveInfo.getMyArmorNum());
        editor.putInt("myArtefactNum",saveInfo.getMyArtefactNum());

        editor.putInt("myATK",saveInfo.getMyATK());
        editor.putInt("myF_ATK",saveInfo.getMyF_ATK());
        editor.putInt("myDEF",saveInfo.getMyDEF());
        editor.putInt("mySpeed",saveInfo.getMySpeed());
        editor.putInt("myA_Speed",saveInfo.getMyA_Speed());
        editor.putInt("myHit",saveInfo.getMyHit());

        editor.putInt("myLevel",saveInfo.getMyLevel());
        editor.putInt("myMaxHp",saveInfo.getMyMaxHp());
        editor.putInt("myMaxMp",saveInfo.getMyMaxMp());
        editor.putInt("myMaxSp",saveInfo.getMyMaxSp());

        editor.putInt("mySkill1",saveInfo.getMySkill1());
        editor.putInt("mySkill2",saveInfo.getMySkill2());
        editor.putInt("mySkill3",saveInfo.getMySkill3());

        editor.putInt("mySkill1_1",saveInfo.skill1_1);
        editor.putInt("mySkill1_2",saveInfo.skill1_2);
        editor.putInt("mySkill1_3",saveInfo.skill1_3);
        editor.putInt("mySkill1_4",saveInfo.skill1_4);
        editor.putInt("mySkill1_5",saveInfo.skill1_5);
        editor.putInt("mySkill1_6",saveInfo.skill1_6);
        editor.putInt("mySkill2_1",saveInfo.skill2_1);
        editor.putInt("mySkill2_2",saveInfo.skill2_2);
        editor.putInt("mySkill2_3",saveInfo.skill2_3);
        editor.putInt("mySkill2_4",saveInfo.skill2_4);
        editor.putInt("mySkill2_5",saveInfo.skill2_5);
        editor.putInt("mySkill2_6",saveInfo.skill2_6);
        editor.putInt("mySkill3_1",saveInfo.skill3_1);
        editor.putInt("mySkill3_2",saveInfo.skill3_2);
        editor.putInt("mySkill3_3",saveInfo.skill3_3);

        editor.putInt("myCurrentHp",saveInfo.getMyCurrentHp());
        editor.putInt("myCurrentMp",saveInfo.getMyCurrentMp());
        editor.putInt("myCurrentSp",saveInfo.getMyCurrentSp());
        editor.putInt("myMaxExp",saveInfo.getMyExp());
        editor.putInt("myMoney",saveInfo.getMyMoney());
        editor.putInt("myMyStage",saveInfo.getMyStage());
        editor.putInt("myDays",saveInfo.getMyDays());

        editor.putInt("myAtkLevel",saveInfo.getMyAtkLevel());
        editor.putInt("myDefLevel",saveInfo.getMyDefLevel());
        editor.putInt("mySpeedLevel",saveInfo.getMySpeedLevel());
        editor.putInt("myHpLevel",saveInfo.getMyHpLevel());
        editor.putInt("myMpLevel",saveInfo.getMyMpLevel());
        editor.commit();

    }

    private void setequipment(int type,int num) {
        //전사일때 검
        if(type==1) {

            saveInfo.setMyATK(saveInfo.getMyATK()-equipment[saveInfo.getMyWeaponNum()][2]);
            saveInfo.setMyATK(saveInfo.getMyATK()+equipment[num][2]);
            saveInfo.setMyWeaponNum(num);
        }// 갑옷
        else if(type==2){

            saveInfo.setMyDEF(saveInfo.getMyDEF()-equipment[saveInfo.getMyArmorNum()+10][2]);
            saveInfo.setMyDEF(saveInfo.getMyDEF()+equipment[num+10][2]);
            saveInfo.setMyArmorNum(num);
        }

    }

    //보물 발견시 이미지이동
    private void getequipment(){

        final View dialogView1 = View.inflate(clearActivity.this, R.layout.dialog_acquireitem, null);
        final AlertDialog.Builder alert = new AlertDialog.Builder(clearActivity.this);
        alert.setView(dialogView1);
        alert.setCancelable(false);
        alert.create();
        DialogInterface dialogInterface = alert.show();

        trimg = (ImageView) dialogView1.findViewById(R.id.acqitem_trimg);
        myimg = (ImageView) dialogView1.findViewById(R.id.acqitem_myimg);
        trinfo = (TextView) dialogView1.findViewById(R.id.acqitem_trinfo);
        myinfo = (TextView) dialogView1.findViewById(R.id.acqitem_myinfo);

        ImageView imgtr = (ImageView) dialogView1.findViewById(R.id.acqitem_trade);
        ImageView imgcancle = (ImageView) dialogView1.findViewById(R.id.acqitem_cancle);

        //이미지 설정
        //이미지 설정 검
        if(boxi1 == 1){
            myimg.setImageResource(imageweapon[saveInfo.getMyWeaponNum()]);
            trimg.setImageResource(imageweapon[boxi2]);
            myinfo.setText("공격력 "+ String.valueOf(equipment[saveInfo.getMyWeaponNum()][2]));
            trinfo.setText("공격력 "+ String.valueOf(equipment[boxi2][2]));
        }
        else if(boxi1 == 2){
            myimg.setImageResource(imagearmor[saveInfo.getMyArmorNum()]);
            trimg.setImageResource(imagearmor[boxi2]);
            myinfo.setText("방어력 "+ String.valueOf(equipment[saveInfo.getMyArmorNum()+10][2]));
            trinfo.setText("방어력 "+ String.valueOf(equipment[boxi2+10][2]));
        }


        imgtr.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction() == MotionEvent.ACTION_DOWN){
                    ((ImageView) view).setColorFilter(0xaa111111);
                }
                else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    ((ImageView) view).setColorFilter(0x00000000);
                    //교체를 누르면
                    setequipment(boxi1,boxi2);
                    Intent i = new Intent(clearActivity.this,StageActivity.class);
                    i.putExtra("sum",clearnum);
                    startActivity(i);
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                    finish();
                    return false;

                }
                return true;
            }
        });
        imgcancle.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction() == MotionEvent.ACTION_DOWN){
                    ((ImageView) view).setColorFilter(0xaa111111);
                }
                else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    ((ImageView) view).setColorFilter(0x00000000);
                    Intent i = new Intent(clearActivity.this,StageActivity.class);
                    i.putExtra("sum",clearnum);
                    startActivity(i);
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                    finish();
                    return false;

                }
                return true;
            }
        });
    }

}
