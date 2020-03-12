package com.example.dgstory;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;

import static com.badlogic.gdx.math.MathUtils.random;

public class LobbyActivity extends AppCompatActivity {
    //아이템의 정보와 이미지를 저장하는 배열
    Integer[][] equipment = {{1,0,5},{1,1,10},{1,2,15},{1,3,25},{1,4,50},{1,5,75},{1,6,100},{1,7,150},{1,8,200},{1,9,300},{2,0,5},{2,1,10},{2,2,20},{2,3,30},{2,4,40},{2,5,50},{2,6,60},{2,7,70},{2,8,100},{2,9,150},
            {3,0,5,5},{3,1,10,10},{3,2,20,20},{3,3,30,30},{3,4,40,40},{3,5,50,50},{3,6,60,60},{3,7,100,100},{3,8,200,200},{3,9,300,300}};
    Integer[] imagering = {R.drawable.icon_ring0,R.drawable.icon_ring1,R.drawable.icon_ring2,R.drawable.icon_ring3,R.drawable.icon_ring4,R.drawable.icon_ring5,R.drawable.icon_ring6,R.drawable.icon_ring7,
            R.drawable.icon_ring8,R.drawable.icon_ring9};
    //랜덤함수 갱신을 위한 num
    double num;
    service_cost_upgrade cost_calculate; //업그레이드 계산기
    service_CharacterInfo saveInfo; //캐릭터 정보로드
    service_music music_bgm; //음악 정보 로드
    service_sound music_se; //효과음 정보 로드
    service_Dialog dialog_check; //확인창 다이얼로그 로드

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lobby);

        saveInfo = new service_CharacterInfo();
        cost_calculate = new service_cost_upgrade();
        music_bgm = new service_music(LobbyActivity.this,R.raw.bgm_lobby_a);
        music_se = new service_sound(LobbyActivity.this);
        dialog_check = new service_Dialog(LobbyActivity.this);

        lobbyreset();

        if(music_bgm.setting == true) {
            music_bgm.start_music();
        }

        //탐험 버튼 -------------------------------------------------------------------------------
        ImageView btn_Explo = (ImageView) findViewById(R.id.btn_explo);
        btn_Explo.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    ((ImageView) view).setColorFilter(0xaa111111);
                }
                else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    ((ImageView) view).setColorFilter(0x00000000);
                    Intent i = new Intent(LobbyActivity.this,StageActivity.class);
                    startActivity(i);
                    finish();
                    return false;
                }
                return true;
            }
        });

        //저장 버튼 -------------------------------------------------------------------------------
        ImageView btn_save = (ImageView) findViewById(R.id.btn_save);
        btn_save.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    ((ImageView) view).setColorFilter(0xaa111111);
                }
                else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    music_se.start_svae();
                    saveMyData();
                    ((ImageView) view).setColorFilter(0x00000000);
                    return false;
                }
                return true;
            }
        });

        //옵션 버튼 -------------------------------------------------------------------------------
        ImageView btn_option = (ImageView) findViewById(R.id.btn_option);
        btn_option.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    ((ImageView) view).setColorFilter(0xaa111111);
                }
                else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    music_se.start_clicked();
                    ((ImageView) view).setColorFilter(0x00000000);
                    myOptionSetter();
                    return false;
                }
                return true;
            }
        });

        //제련 버튼 -------------------------------------------------------------------------------
        ImageView btn_smelting = (ImageView) findViewById(R.id.btn_smelting);
        btn_smelting.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    ((ImageView) view).setColorFilter(0xaa111111);
                }
                else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    music_se.start_clicked();
                    ((ImageView) view).setColorFilter(0x00000000);
                    showDialog((int)0);
                    return false;
                }
                return true;
            }
        });

        //휴식 버튼 ------------------------------------------------------------------------------
        ImageView btn_sleep = (ImageView) findViewById(R.id.btn_sleep);
        btn_sleep.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    ((ImageView) view).setColorFilter(0xaa111111);
                }
                else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    music_se.start_clicked();
                    ((ImageView) view).setColorFilter(0x00000000);
                    gameRestTimeDialog();
                    return false;
                }
                return true;
            }
        });

        //내정보 버튼 ----------------------------------------------------------------------------
        ImageView btn_myinfo = (ImageView) findViewById(R.id.btn_info);
        btn_myinfo.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction() == MotionEvent.ACTION_DOWN){
                    ((ImageView) view).setColorFilter(0xaa111111);
                }
                else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    music_se.start_clicked();
                    ((ImageView) view).setColorFilter(0x00000000);
                    myinfo();
                    return false;

                }
                return true;
            }
        });

        ImageView btn_inven = (ImageView)findViewById(R.id.btn_inven);
        btn_inven.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction() == MotionEvent.ACTION_DOWN){
                    ((ImageView) view).setColorFilter(0xaa111111);
                }
                else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    ((ImageView) view).setColorFilter(0x00000000);

                    myinven();


                    return false;

                }
                return true;
            }
        });



        //발굴버튼---------------------------------------------------------------------------------
        ImageView btn_mydig = (ImageView) findViewById(R.id.btn_dig);
        btn_mydig.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction() == MotionEvent.ACTION_DOWN){
                    ((ImageView) view).setColorFilter(0xaa111111);
                }
                else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    ((ImageView) view).setColorFilter(0x00000000);
                    excavation();
                    return false;
                }
                return true;
            }
        });
        lobbyreset();
    }



    /**-----------------------------------------------------------------------------------------****
     * 아래 부터는 각 메소드 관리 부분 ---------------------------------------------------------****
     * -----------------------------------------------------------------------------------------***/



    //*--------------------------------------------------------------------------------------------
    //*제련 메소드 --------------------------------------------------------------------------------
    //*--------------------------------------------------------------------------------------------
    @Override
    protected Dialog onCreateDialog(int id) {
        //제련번호 == 0
        if(id == 0){
            View dialogView = View.inflate(LobbyActivity.this,R.layout.dialog_upgrade,null);
            AlertDialog.Builder alert = new AlertDialog.Builder(LobbyActivity.this);
            alert.setView(dialogView);
            alert.setCancelable(false);

            TextView tv_money = (TextView) dialogView.findViewById(R.id.tv_upgrade_money);
            TextView tv_atk = (TextView) dialogView.findViewById(R.id.tv_upgrade_atk);
            TextView tv_def = (TextView) dialogView.findViewById(R.id.tv_upgrade_def);
            TextView tv_speed = (TextView) dialogView.findViewById(R.id.tv_upgrade_speed);
            TextView tv_hp = (TextView) dialogView.findViewById(R.id.tv_upgrade_hp);
            TextView tv_mp = (TextView) dialogView.findViewById(R.id.tv_upgrade_mp);
            ImageView btn_cancel = (ImageView) dialogView.findViewById(R.id.btn_upgrade_cancel);
            Button btn_atk = (Button) dialogView.findViewById(R.id.button_upgrade_atk);
            Button btn_def = (Button) dialogView.findViewById(R.id.button_upgrade_def);
            Button btn_speed = (Button) dialogView.findViewById(R.id.button_upgrade_speed);
            Button btn_hp = (Button) dialogView.findViewById(R.id.button_upgrade_hp);
            Button btn_mp = (Button) dialogView.findViewById(R.id.button_upgrade_mp);

            //첫 시작 각종 레벨,금액 셋팅
            tv_money.setText(String.valueOf(saveInfo.myMoney));
            tv_atk.setText(String.valueOf(saveInfo.getMyAtkLevel()));
            tv_def.setText(String.valueOf(saveInfo.getMyDefLevel()));
            tv_speed.setText(String.valueOf(saveInfo.getMySpeedLevel()));
            tv_hp.setText(String.valueOf(saveInfo.getMyHpLevel()));
            tv_mp.setText(String.valueOf(saveInfo.getMyMpLevel()));

            /**
             * 업글 비용 셋팅 (업글 비용 수정시 아래의 버튼 내역도 수정요망)
             */
            btn_hp.setText(String.valueOf(cost_calculate.setHpLevel(saveInfo.myHpLevel)));
            btn_mp.setText(String.valueOf((saveInfo.myMpLevel)*200));
            btn_atk.setText(String.valueOf((saveInfo.myAtkLevel)*200));
            btn_def.setText(String.valueOf((saveInfo.myDefLevel)*200));
            btn_speed.setText(String.valueOf((saveInfo.mySpeedLevel)*200));

            btn_cancel.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                        ((ImageView) view).setColorFilter(0xaa111111);
                    }
                    else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                        ((ImageView) view).setColorFilter(0x00000000);
                        music_se.start_clicked();
                        dismissDialog(0);
                    }
                    return true;
                }
            });
            btn_hp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(saveInfo.myMoney > cost_calculate.setHpLevel(saveInfo.myHpLevel)){
                        music_se.start_up();
                        saveInfo.setMyMoney(saveInfo.myMoney - cost_calculate.setHpLevel(saveInfo.myHpLevel));
                        saveInfo.setMyHpLevel(saveInfo.myHpLevel+1);
                        saveInfo.setMyMaxHp(saveInfo.getMyMaxHp()+1);
                        lobbyreset();
                    }else {
                        music_se.start_down();
                        service_toast st = new service_toast(LobbyActivity.this, "광물이 부족합니다");
                        st.toast_start();
                    }
                    showDialog((int)0);
                }
            });
            btn_mp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(saveInfo.myMoney > (saveInfo.myMpLevel*200)){
                        music_se.start_up();
                        saveInfo.setMyMoney(saveInfo.myMoney - (saveInfo.myMpLevel*200));
                        saveInfo.setMyMpLevel(saveInfo.myMpLevel+1);
                        saveInfo.setMyMaxMp(saveInfo.getMyMaxMp()+1);

                        lobbyreset();
                    }else {
                        music_se.start_down();
                        service_toast st = new service_toast(LobbyActivity.this, "광물이 부족합니다");
                        st.toast_start();
                    }
                    showDialog((int)0);
                }
            });
            btn_atk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(saveInfo.myMoney > (saveInfo.myAtkLevel*200)){
                        music_se.start_up();
                        saveInfo.setMyMoney(saveInfo.myMoney - (saveInfo.myAtkLevel*200));
                        saveInfo.setMyAtkLevel(saveInfo.myAtkLevel+1);
                        saveInfo.setMyATK(saveInfo.getMyATK()+1);
                        lobbyreset();
                    }else {
                        music_se.start_down();
                        service_toast st = new service_toast(LobbyActivity.this, "광물이 부족합니다");
                        st.toast_start();
                    }
                    showDialog((int)0);
                }
            });
            btn_def.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(saveInfo.myMoney > (saveInfo.myDefLevel*200)){
                        music_se.start_up();
                        saveInfo.setMyMoney(saveInfo.myMoney - (saveInfo.myDefLevel*200));
                        saveInfo.setMyDefLevel(saveInfo.myDefLevel+1);
                        saveInfo.setMyDEF(saveInfo.getMyDEF()+1);
                        lobbyreset();
                    }else {
                        music_se.start_down();
                        service_toast st = new service_toast(LobbyActivity.this, "광물이 부족합니다");
                        st.toast_start();
                    }
                    showDialog((int)0);
                }
            });
            btn_speed.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(saveInfo.myMoney > (saveInfo.myMpLevel*200)){
                        music_se.start_up();
                        saveInfo.setMyMoney(saveInfo.myMoney - (saveInfo.myMpLevel*200));
                        saveInfo.setMySpeedLevel(saveInfo.mySpeedLevel+1);
                        saveInfo.setMySpeed(saveInfo.getMySpeed()+1);
                        lobbyreset();
                    }else {
                        music_se.start_down();
                        service_toast st = new service_toast(LobbyActivity.this, "광물이 부족합니다");
                        st.toast_start();
                    }
                    showDialog((int)0);
                }
            });
            lobbyreset();

            return alert.create();
        }

        lobbyreset();
        return super.onCreateDialog(id);
    }

    @Override
    protected void onPrepareDialog(int id, Dialog dialog) {
        if(id == 0){
            TextView tv_money = (TextView) dialog.findViewById(R.id.tv_upgrade_money);
            TextView tv_atk = (TextView) dialog.findViewById(R.id.tv_upgrade_atk);
            TextView tv_def = (TextView) dialog.findViewById(R.id.tv_upgrade_def);
            TextView tv_speed = (TextView) dialog.findViewById(R.id.tv_upgrade_speed);
            TextView tv_hp = (TextView) dialog.findViewById(R.id.tv_upgrade_hp);
            TextView tv_mp = (TextView) dialog.findViewById(R.id.tv_upgrade_mp);
            Button btn_atk = (Button) dialog.findViewById(R.id.button_upgrade_atk);
            Button btn_def = (Button) dialog.findViewById(R.id.button_upgrade_def);
            Button btn_speed = (Button) dialog.findViewById(R.id.button_upgrade_speed);
            Button btn_hp = (Button) dialog.findViewById(R.id.button_upgrade_hp);
            Button btn_mp = (Button) dialog.findViewById(R.id.button_upgrade_mp);

            tv_money.setText(String.valueOf(saveInfo.myMoney));
            tv_atk.setText(String.valueOf(saveInfo.getMyAtkLevel()));
            tv_def.setText(String.valueOf(saveInfo.getMyDefLevel()));
            tv_speed.setText(String.valueOf(saveInfo.getMySpeedLevel()));
            tv_hp.setText(String.valueOf(saveInfo.getMyHpLevel()));
            tv_mp.setText(String.valueOf(saveInfo.getMyMpLevel()));

            //다이얼로그 업그레이드 비용 업데이트
            btn_hp.setText(String.valueOf(cost_calculate.setHpLevel(saveInfo.myHpLevel)));
            btn_mp.setText(String.valueOf((saveInfo.myMpLevel)*200));
            btn_atk.setText(String.valueOf((saveInfo.myAtkLevel)*200));
            btn_def.setText(String.valueOf((saveInfo.myDefLevel)*200));
            btn_speed.setText(String.valueOf((saveInfo.mySpeedLevel)*200));
        }
        super.onPrepareDialog(id, dialog);
    }

    //*--------------------------------------------------------------------------------------------
    //*옵션 다이얼로그 메소드 ---------------------------------------------------------------------
    //*--------------------------------------------------------------------------------------------
    private void myOptionSetter(){
        DialogInterface dialogInterface = null;
        final View dialogView = View.inflate(LobbyActivity.this,R.layout.dialog_option,null);
        final AlertDialog.Builder alert = new AlertDialog.Builder(LobbyActivity.this);
        alert.setView(dialogView);
        alert.setCancelable(false);
        alert.create();
        dialogInterface = alert.show();

        ImageView op_delete = (ImageView) dialogView.findViewById(R.id.btn_option_datadelete);
        ImageView op_sound = (ImageView) dialogView.findViewById(R.id.btn_option_sound);
        ImageView op_help = (ImageView) dialogView.findViewById(R.id.btn_option_help);
        ImageView op_end = (ImageView) dialogView.findViewById(R.id.btn_option_end);
        ImageView op_cancel = (ImageView) dialogView.findViewById(R.id.btn_option_cancel);

        //데이터 삭제
        final DialogInterface finalDialogInterface = dialogInterface;
        op_delete.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    ((ImageView) view).setColorFilter(0xaa111111);
                }
                else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    music_se.start_clicked();
                    ((ImageView) view).setColorFilter(0x00000000);
                    deleteDataDialog(LobbyActivity.this,"데이터를 정말 삭제하시겠습니까?\n삭제시 타이틀 화면으로 돌아갑니다.");
                    finalDialogInterface.dismiss();
                }
                return true;
            }
        });
        //소리 끄기 켜기
        op_sound.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    ((ImageView) view).setColorFilter(0xaa111111);
                }
                else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    music_se.start_clicked();
                    ((ImageView) view).setColorFilter(0x00000000);
                    musicSoundsetter(LobbyActivity.this);
                    finalDialogInterface.dismiss();
                }
                return true;
            }
        });
        //도움말
        op_help.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    ((ImageView) view).setColorFilter(0xaa111111);
                }
                else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    music_se.start_clicked();
                    ((ImageView) view).setColorFilter(0x00000000);
                    service_toast st = new service_toast(getApplicationContext(),"아직 준비중인 컨텐츠입니다.");
                    st.toast_start();
                }
                return true;
            }
        });
        //게임 종료
        op_end.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    ((ImageView) view).setColorFilter(0xaa111111);

                }
                else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    music_se.start_clicked();
                    ((ImageView) view).setColorFilter(0x00000000);
                    gameExitDialog(LobbyActivity.this,"게임이 저장 후 종료됩니다");
                    finalDialogInterface.dismiss();
                }
                return true;
            }
        });
        //취소
        op_cancel.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    ((ImageView) view).setColorFilter(0xaa111111);
                }
                else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    music_se.start_clicked();
                    ((ImageView) view).setColorFilter(0x00000000);
                    finalDialogInterface.dismiss();
                }
                return true;
            }
        });
    }

    //*--------------------------------------------------------------------------------------------
    //*데이터 삭제 다이얼로그 메소드---------------------------------------------------------------
    //*--------------------------------------------------------------------------------------------
    private void deleteDataDialog(Context context, String content){
        DialogInterface dialogInterface = null;
        View view = View.inflate(context,R.layout.dialog_custom,null);
        final AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setView(view);
        alert.setCancelable(false);
        alert.create();
        dialogInterface = alert.show();
        TextView tv = (TextView) view.findViewById(R.id.tv_dialog);
        ImageView btn_ok = (ImageView) view.findViewById(R.id.btn_dialog_ok);
        ImageView btn_cancel = (ImageView) view.findViewById(R.id.btn_dialog_cancel);
        tv.setText(content);
        final DialogInterface finalDialogInterface = dialogInterface;
        btn_ok.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    ((ImageView) view).setColorFilter(0xaa111111);
                }
                else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    ((ImageView) view).setColorFilter(0x00000000);
                    finalDialogInterface.dismiss();
                    SharedPreferences pref = getSharedPreferences("myGame", Activity.MODE_PRIVATE);
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putString("myName","");
                    editor.commit();

                    MyHelper mHelper = new MyHelper(LobbyActivity.this);
                    SQLiteDatabase db = mHelper.getWritableDatabase();
                    mHelper.delete();

                    Intent i = new Intent(LobbyActivity.this,TitleActivity.class);
                    startActivity(i);
                    overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
                    finish();

                }
                return true;
            }
        });

        btn_cancel.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    ((ImageView) view).setColorFilter(0xaa111111);
                }
                else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    ((ImageView) view).setColorFilter(0x00000000);
                    finalDialogInterface.dismiss();
                }
                return true;
            }
        });
    }

    //*--------------------------------------------------------------------------------------------
    //*소리 온오프 메소드--------------------------------------------------------------------------
    //*--------------------------------------------------------------------------------------------
    private void musicSoundsetter(Context context){
        music_bgm.setting = !music_bgm.setting;
        service_toast st = new service_toast(context,"Error!");
        if(music_bgm.setting == true){
            music_bgm.start_music();
            music_se.setting = true;
            st.setDial("Sound On");
            st.toast_start();
        }else{
            music_bgm.pause_music();
            music_se.setting = false;
            st.setDial("Sound Off");
            st.toast_start();
        }
    }

    //*--------------------------------------------------------------------------------------------
    //*휴식 메소드 다이얼로그,데이터,랜덤값뽑기----------------------------------------------------
    //*--------------------------------------------------------------------------------------------
    private void gameRestTimeDialog(){
        DialogInterface dialogInterface = null;
        View view = View.inflate(LobbyActivity.this,R.layout.dialog_custom,null);
        final AlertDialog.Builder alert = new AlertDialog.Builder(LobbyActivity.this);
        alert.setView(view);
        alert.setCancelable(false);
        alert.create();
        dialogInterface = alert.show();

        TextView tv = (TextView) view.findViewById(R.id.tv_dialog);
        ImageView btn_ok = (ImageView) view.findViewById(R.id.btn_dialog_ok);
        ImageView btn_cancel = (ImageView) view.findViewById(R.id.btn_dialog_cancel);

        tv.setText("잠이 올 것 같다..\n잠시 휴식을 할까?");

        final DialogInterface finalDialogInterface = dialogInterface;
        btn_ok.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    music_se.start_clicked();
                    ((ImageView) view).setColorFilter(0xaa111111);
                }
                else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    music_se.start_clicked();
                    ((ImageView) view).setColorFilter(0x00000000);
                    finalDialogInterface.dismiss();

                    final service_customPregressDialog pd = new service_customPregressDialog(LobbyActivity.this);
                    pd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    pd.show();

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            pd.dismiss();
                            restTimeData();
                        }
                    },3000);
                }
                return true;
            }
        });

        btn_cancel.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    ((ImageView) view).setColorFilter(0xaa111111);
                }
                else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    ((ImageView) view).setColorFilter(0x00000000);
                    music_se.start_clicked();
                    finalDialogInterface.dismiss();
                }
                return true;
            }
        });
    }


    private void restTimeData() {
        double percentage;
        percentage = rand();

        if (percentage < (90.0 - (saveInfo.myDays/2))) {
            double dice1 = rand();
            if(dice1 <= 20){
                dialog_check.start("아버지와 함께하던 때를 떠올리며 의지를 다잡는다.");
                saveInfo.eventcheck(30,30,10,0,0,00,0,00,LobbyActivity.this);
            }
            else if(dice1 >20 && dice1 <=40){
                dialog_check.start("요정들이 뛰놀며 지친 당신을 위로한다.");
                saveInfo.eventcheck(30,30,10,0,0,00,0,00,LobbyActivity.this);
                double dice2 = rand();

                if(dice2 <= 3){
                    dialog_check.start("요정의 여왕이 당신을 축복하였다");
                    saveInfo.eventcheck(500,500,100,0,0,00,0,00,LobbyActivity.this);
                }
            }
            else if(dice1 >40 && dice1 <=60){
                dialog_check.start("아무 일도 생기지 않아 모처럼의 평화를 즐긴다.");
                saveInfo.eventcheck(30,30,10,0,0,00,0,00,LobbyActivity.this);
            }
            else if(dice1 >60 && dice1 <=80){
                dialog_check.start("기분좋은 바람이 지쳐서 달궈진 몸을 식힌다..");
                saveInfo.eventcheck(30,30,10,0,0,00,0,00,LobbyActivity.this);
            }
            else if(dice1 >60 && dice1 <=80){
                dialog_check.start("마물들이 발견하지 못한 안전한 곳에서 편히 쉬었다.");
                saveInfo.eventcheck(50,50,10,0,0,00,0,00,LobbyActivity.this);
            }
            else if(dice1 >80 && dice1 <=95){
                dialog_check.start("운마저 따라 주는 발군의 상태, 지금이라면 수많은 마물이라도 해치울수 있을 것 같다.");
                saveInfo.eventcheck(100,100,10,0,0,00,0,00,LobbyActivity.this);
            }
            else{
                dialog_check.start("잊혀진 고대신의 성소를 발견하였다\n기도를 올리자 잊혀진 자신을 위해 기원해준 모험가를 축복했다.");
                saveInfo.eventcheck(500,500,100,0,0,00,0,00,LobbyActivity.this);
            }
        } else {
            double dice1 =rand();
            int upbad = 10 + saveInfo.myDays/2;

            if(dice1 <= upbad ){
                dialog_check.start("기습을당했다!!!");
                Intent i = new Intent(LobbyActivity.this,Stage_3_Activity.class);
                startActivity(i);
                finish();
            }if(dice1 > upbad  && dice1 <98 ){
                double dice2 = rand();
                if(dice2 <= 20) {
                    dialog_check.start("마물들에게 소중한 사람들이 죽어나가는 악몽을 꿨다");
                    saveInfo.eventcheck(0,0,0,0,10,00,3,00,LobbyActivity.this);
                    if (dice2 <=2) {
                        dialog_check.start("몸에 기운이 없어진다.");
                        saveInfo.eventcheck(0,0,0,0,50,00,0,00,LobbyActivity.this);
                    }
                }
                else if(dice2 >20 && dice2 <= 40){
                    dialog_check.start("밤새도록 마물의 눈동자와 울음소리가 당신의 신경을 날카롭게 한다.");
                    saveInfo.eventcheck(0,0,0,0,0,00,5,00,LobbyActivity.this);
                    if(dice2<=40 && dice2 >=38){
                        dialog_check.start("깊이 잠들지 못해, 몸상태가 최악이다.");
                        saveInfo.eventcheck(0,0,0,0,50,00,0,00,LobbyActivity.this);
                    }
                }
                else if(dice2 >40 && dice2 <= 60){
                    dialog_check.start("던전의 음습한 기운이 당신의 활기를 빼았는다");
                    saveInfo.eventcheck(0,0,0,0,0,00,5,00,LobbyActivity.this);
                    if(dice2<=60 && dice2>= 58){
                        dialog_check.start("잠을 자기 전보다 몸상태가 안좋아졌다.");
                        saveInfo.eventcheck(0,0,0,0,50,00,5,00,LobbyActivity.this);
                    }
                }
                else if(dice2 >60 && dice2 <= 80){
                    dialog_check.start("당신이 벤 마물의 피에 감응한 원혼들이 당신을 괴롭힌다.");
                    saveInfo.eventcheck(0,0,0,0,20,00,5,00,LobbyActivity.this);
                    if(dice2 <=80 && dice2 >= 78){
                        dialog_check.start("지독한 고통이 느껴진다.");
                        saveInfo.eventcheck(0,0,0,0,100,00,0,00,LobbyActivity.this);
                    }
                }
                else{
                    dialog_check.start("저주 받은 신의 사당이다.");
                    saveInfo.eventcheck(0,0,0,0,100,00,10,00,LobbyActivity.this);
                    if(dice2 <=100 && dice2 >= 99){
                        dialog_check.start("악신이 저주를 내렸다.");
                        saveInfo.eventcheck(0,0,0,0,300,3000,300,5000,LobbyActivity.this);
                    }

                }


            }if(dice1 <= 100 && dice1 >=10){
                // 스킬습득
                skillup();
            }
        }
    }

    private double rand(){
        Random random = new Random();
        int r = random.nextInt(1000);
        r = r / 10;
        double percentage = (double) r / 100.0 * 100.0;
        return percentage;

    }

    //*--------------------------------------------------------------------------------------------
    //*게임 종료 다이얼로그 메소드-----------------------------------------------------------------
    //*--------------------------------------------------------------------------------------------
    private void gameExitDialog(Context context, String content){
        DialogInterface dialogInterface = null;
        View view = View.inflate(context,R.layout.dialog_custom,null);
        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setView(view);
        alert.setCancelable(false);
        alert.create();
        dialogInterface = alert.show();
        TextView tv = (TextView) view.findViewById(R.id.tv_dialog);
        ImageView btn_ok = (ImageView) view.findViewById(R.id.btn_dialog_ok);
        ImageView btn_cancel = (ImageView) view.findViewById(R.id.btn_dialog_cancel);
        tv.setText(content);
        final DialogInterface finalDialogInterface = dialogInterface;
        btn_ok.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    music_se.start_clicked();
                    ((ImageView) view).setColorFilter(0xaa111111);
                }
                else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    music_se.start_clicked();
                    ((ImageView) view).setColorFilter(0x00000000);
                    finalDialogInterface.dismiss();
                    saveMyData();
                    finish();
                }
                return true;
            }
        });

        btn_cancel.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    ((ImageView) view).setColorFilter(0xaa111111);
                }
                else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    ((ImageView) view).setColorFilter(0x00000000);
                    finalDialogInterface.dismiss();
                }
                return true;
            }
        });
    }

    //*--------------------------------------------------------------------------------------------
    //*저장 메소드 --------------------------------------------------------------------------------
    //*--------------------------------------------------------------------------------------------
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

        service_toast st = new service_toast(LobbyActivity.this,"저장 완료!");
        st.toast_start();
    }

    //*--------------------------------------------------------------------------------------------
    //*내 정보 --------------------------------------------------------------------------------
    //*--------------------------------------------------------------------------------------------
    private void myinfo(){
        DialogInterface dialogInterface = null;
        final View dialogView = View.inflate(LobbyActivity.this,R.layout.dialog_myinfo1,null);
        final AlertDialog.Builder alert = new AlertDialog.Builder(LobbyActivity.this);
        alert.setView(dialogView);
        alert.setCancelable(false);
        alert.create();
        dialogInterface = alert.show();

        TextView nicname = (TextView) dialogView.findViewById(R.id.mystate_nickname);
        ImageView btn_cancel = (ImageView) dialogView.findViewById(R.id.mystate_cancle);
        TextView level = (TextView) dialogView.findViewById(R.id.mystate_Level);
        TextView curhp = (TextView) dialogView.findViewById(R.id.mystate_mycurHP);
        TextView maxhp = (TextView) dialogView.findViewById(R.id.mystate_mymaxHP);
        TextView curmp = (TextView) dialogView.findViewById(R.id.mystate_mycurMP);
        TextView maxmp = (TextView) dialogView.findViewById(R.id.mystate_mymaxMP);
        TextView cursp = (TextView) dialogView.findViewById(R.id.mystate_mycurSP);
        TextView maxsp = (TextView) dialogView.findViewById(R.id.mystate_mymaxSP);
        TextView exp = (TextView) dialogView.findViewById(R.id.mystate_mycurEXP);
        TextView weaponspeed  = (TextView) dialogView.findViewById(R.id.mystate_myWeapnSpeed);
        TextView movespeed = (TextView) dialogView.findViewById(R.id.mystate_mySpeed);
        TextView atk  = (TextView) dialogView.findViewById(R.id.mystate_ATK);
        TextView fatk = (TextView) dialogView.findViewById(R.id.mystate_FATK);
        TextView def = (TextView) dialogView.findViewById(R.id.mystate_DEF);
        TextView hit = (TextView) dialogView.findViewById(R.id.mystate_Hit);
        TextView skill1 = (TextView) dialogView.findViewById(R.id.mystate_skill1);
        TextView skill2 = (TextView) dialogView.findViewById(R.id.mystate_skill2);
        TextView skill3 = (TextView) dialogView.findViewById(R.id.mystate_skill3);

        atk.setText(String.valueOf(service_CharacterInfo.getMyATK()+5));
        fatk.setText(String.valueOf(service_CharacterInfo.getMyF_ATK()));
        def.setText(String.valueOf(service_CharacterInfo.getMyDEF()+5));

        if(saveInfo.getMySkill1()==1) {
            skill1.setText("더블크래쉬");
        }else if(saveInfo.getMySkill1()==2){
            skill1.setText("차지크래쉬");
        }else{
            skill1.setText("스워드스로우");
        }
        if(saveInfo.getMySkill2()==1) {
            skill1.setText("더블크래쉬");
        }else if(saveInfo.getMySkill2()==2){
            skill1.setText("차지크래쉬");
        }else{
            skill1.setText("스워드스로우");
        }



        nicname.setText(String.valueOf(service_CharacterInfo.getMyName()));
        level.setText(String.valueOf(service_CharacterInfo.getMyLevel()));
        curhp.setText(String.valueOf(service_CharacterInfo.getMyCurrentHp()));
        maxhp.setText(String.valueOf(service_CharacterInfo.getMyMaxHp()));
        curmp.setText(String.valueOf(service_CharacterInfo.getMyCurrentMp()));
        maxmp.setText(String.valueOf(service_CharacterInfo.getMyMaxMp()));
        cursp.setText(String.valueOf(service_CharacterInfo.getMyCurrentSp()));
        maxsp.setText(String.valueOf(service_CharacterInfo.getMyMaxSp()));
        exp.setText(String.valueOf(service_CharacterInfo.getMyExp()));

        weaponspeed.setText(String.valueOf(service_CharacterInfo.getMyA_Speed())); // 공속맞지?
        movespeed.setText(String.valueOf(service_CharacterInfo.getMySpeed()));
        hit.setText(String.valueOf(service_CharacterInfo.getMyHit()));

        lobbyreset();

        final DialogInterface finalDialogInterface = dialogInterface;
        btn_cancel.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    ((ImageView) view).setColorFilter(0xaa111111);
                }
                else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    ((ImageView) view).setColorFilter(0x00000000);
                    finalDialogInterface.dismiss();
                }
                return true;
            }
        });



    }

    //인벤
    private void myinven() {
        DialogInterface dialogInterface = null;
        final View dialogView = View.inflate(LobbyActivity.this, R.layout.dialog_inven, null);
        final AlertDialog.Builder alert = new AlertDialog.Builder(LobbyActivity.this);
        alert.setView(dialogView);
        alert.setCancelable(false);
        alert.create();
        dialogInterface = alert.show();

        Integer[] imageweapon = {R.drawable.icon_weapon0,R.drawable.icon_weapon1,R.drawable.icon_weapon2,R.drawable.icon_weapon3,R.drawable.icon_weapon4,R.drawable.icon_weapon5,R.drawable.icon_weapon6,R.drawable.icon_weapon7,
                R.drawable.icon_weapon8,R.drawable.icon_weapon9};
        Integer[] imagearmor = {R.drawable.icon_armor0,R.drawable.icon_armor1,R.drawable.icon_armor2,R.drawable.icon_armor3,R.drawable.icon_armor4,R.drawable.icon_armor5,R.drawable.icon_armor6,R.drawable.icon_armor7,
                R.drawable.icon_armor8,R.drawable.icon_armor9};

        ImageView cancle = (ImageView) dialogView.findViewById(R.id.inven_cancle);
        ImageView weaponimg = (ImageView) dialogView.findViewById(R.id.inven_weaponimg);
        ImageView armorimg = (ImageView) dialogView.findViewById(R.id.inven_armorimg);
        ImageView ringimg = (ImageView) dialogView.findViewById(R.id.inven_ringimg);
        TextView weaponinfo = (TextView) dialogView.findViewById(R.id.inven_weaponinfo);
        TextView armorinfo = (TextView) dialogView.findViewById(R.id.inven_armorinfo);
        TextView ringinfo = (TextView) dialogView.findViewById(R.id.inven_ringinfo);

        lobbyreset();
        final DialogInterface finaldialog = dialogInterface;
        cancle.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction() == MotionEvent.ACTION_DOWN){
                    ((ImageView) view).setColorFilter(0xaa111111);
                }
                else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    ((ImageView) view).setColorFilter(0x00000000);
                    finaldialog.dismiss();
                    return false;

                }
                return true;
            }
        });

        weaponimg.setImageResource(imageweapon[saveInfo.getMyWeaponNum()]);
        armorimg.setImageResource(imagearmor[saveInfo.getMyArmorNum()]);
        ringimg.setImageResource(imagering[saveInfo.getMyArtefactNum()]);
        weaponinfo.setText("공격력  "+ String.valueOf(equipment[saveInfo.getMyWeaponNum()][2]));
        armorinfo.setText("방어력  "+ String.valueOf(equipment[saveInfo.getMyArmorNum()+10][2]));
        ringinfo.setText("HP/MP  +"+ String.valueOf(equipment[saveInfo.getMyArtefactNum()+20][2]));



    }

    //발굴
    private void excavation(){
        final View dialogView = View.inflate(LobbyActivity.this, R.layout.dialog_excavation, null);
        final AlertDialog.Builder alert = new AlertDialog.Builder(LobbyActivity.this);
        alert.setView(dialogView);
        alert.setCancelable(false);
        alert.create();
        DialogInterface dialogInterface = alert.show();

        Integer[] imagearmor = {R.drawable.icon_ring0,R.drawable.icon_ring1,R.drawable.icon_ring2,R.drawable.icon_ring3,R.drawable.icon_ring4,R.drawable.icon_ring5,R.drawable.icon_ring6,R.drawable.icon_ring7,
                R.drawable.icon_ring8,R.drawable.icon_ring9};

        ImageView cancle = (ImageView) dialogView.findViewById(R.id.excavation_cancle);
        ImageView pick = (ImageView) dialogView.findViewById(R.id.excavation_pick);

        num = rand();

        final DialogInterface finalDialogInterface = dialogInterface;
        cancle.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction() == MotionEvent.ACTION_DOWN){
                    ((ImageView) view).setColorFilter(0xaa111111);
                }
                else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    ((ImageView) view).setColorFilter(0x00000000);
                    finalDialogInterface.dismiss();
                    return false;

                }
                return true;
            }
        });
        pick.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction() == MotionEvent.ACTION_DOWN){
                    ((ImageView) view).setColorFilter(0xaa111111);
                }
                else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    ((ImageView) view).setColorFilter(0x00000000);
                    if(saveInfo.getMyMoney()-5000>=0) {
                        if (num <= 30.0) {
                            num = rand();
                            getequipment();
                            saveInfo.setMyMoney(saveInfo.getMyMoney()-5000);
                            lobbyreset();
                        } else {
                            num = rand();
                            saveInfo.setMyMoney(saveInfo.getMyMoney()-5000);
                            lobbyreset();
                            service_toast st = new service_toast(LobbyActivity.this,"꽝");
                            st.toast_start();
                        }
                    }else{
                        service_toast st = new service_toast(LobbyActivity.this,"돈이 부족합니다");
                        st.toast_start();
                    }

                    return false;

                }
                return true;
            }
        });



    }

    private void getequipment(){

        final View dialogView1 = View.inflate(LobbyActivity.this, R.layout.dialog_acquireitem, null);
        final AlertDialog.Builder alert = new AlertDialog.Builder(LobbyActivity.this);
        alert.setView(dialogView1);
        alert.setCancelable(false);
        alert.create();
        DialogInterface dialogInterface = alert.show();

        ImageView trimg = (ImageView) dialogView1.findViewById(R.id.acqitem_trimg);
        ImageView myimg = (ImageView) dialogView1.findViewById(R.id.acqitem_myimg);
        TextView trinfo = (TextView) dialogView1.findViewById(R.id.acqitem_trinfo);
        TextView myinfo = (TextView) dialogView1.findViewById(R.id.acqitem_myinfo);

        ImageView imgtr = (ImageView) dialogView1.findViewById(R.id.acqitem_trade);
        ImageView imgcancle = (ImageView) dialogView1.findViewById(R.id.acqitem_cancle);

        final int boxi1 = 3;

        final int boxi2 = random(saveInfo.getMyStage()/5);

        myimg.setImageResource(imagering[saveInfo.getMyArtefactNum()]);
        trimg.setImageResource(imagering[boxi2]);
        myinfo.setText("HP/MP  +"+ String.valueOf(equipment[saveInfo.getMyArtefactNum()+20][2]));
        trinfo.setText("HP/MP  +"+ String.valueOf(equipment[boxi2+20][2]));

        final DialogInterface finalDialogInterface = dialogInterface;
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
                    finalDialogInterface.dismiss();
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
                    finalDialogInterface.dismiss();
                    return false;

                }
                return true;
            }
        });
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
        }else{
            saveInfo.setMyMaxHp(saveInfo.getMyMaxHp()-equipment[saveInfo.getMyArtefactNum()+20][2]);
            saveInfo.setMyMaxHp(saveInfo.getMyMaxHp()+equipment[num+20][2]);
            saveInfo.setMyMaxMp(saveInfo.getMyMaxMp()-equipment[saveInfo.getMyArtefactNum()+20][3]);
            saveInfo.setMyMaxMp(saveInfo.getMyMaxMp()+equipment[num+20][3]);
            saveInfo.setMyArtefactNum(num);

        }
        lobbyreset();

    }
    //로비의 money sp 갱신 골고루 넣어줘야함 특히 업그레이드나 휴식에는 꼭
    private void lobbyreset(){
        TextView tvmoney = (TextView) findViewById(R.id.lobby_money);
        TextView tvsp = (TextView) findViewById(R.id.lobby_sp);

        tvmoney.setText(String.valueOf(saveInfo.getMyMoney()));
        tvsp.setText(String.valueOf(saveInfo.getMyCurrentSp()));

        if(saveInfo.getMyCurrentSp()<=10){
            tvsp.setTextColor(Color.parseColor("#ff0000"));
        }else{
            tvsp.setTextColor(Color.parseColor("#FFFFFF"));
        }
    }
    private void skillup(){
        final View dialogView1 = View.inflate(LobbyActivity.this, R.layout.dialog_skillup, null);
        final AlertDialog.Builder alert = new AlertDialog.Builder(LobbyActivity.this);
        alert.setView(dialogView1);
        alert.setCancelable(false);
        alert.create();
        DialogInterface dialogInterface = alert.show();

        ImageView skill1 = (ImageView) dialogView1.findViewById(R.id.skillup_skill1);
        ImageView skill2 = (ImageView) dialogView1.findViewById(R.id.skillup_skill2);
        ImageView cancle = (ImageView) dialogView1.findViewById(R.id.skillup_cancle);


        final int skillnumber = random.nextInt(3)+1;

        final DialogInterface finalDialogInterface = dialogInterface;
        cancle.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction() == MotionEvent.ACTION_DOWN){
                    ((ImageView) view).setColorFilter(0xaa111111);
                }
                else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    ((ImageView) view).setColorFilter(0x00000000);
                    finalDialogInterface.dismiss();
                    return false;

                }
                return true;
            }
        });
        skill1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveInfo.setMySkill1(skillnumber);
                saveInfo.setSkill1_3(2);
                saveInfo.setSkill1_5(2);
                saveInfo.setSkill1_6(2);
                finalDialogInterface.dismiss();
            }
        });
        skill2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveInfo.setMySkill2(skillnumber);
                saveInfo.setSkill2_3(1);
                saveInfo.setSkill2_5(4);
                saveInfo.setSkill2_6(4);
                finalDialogInterface.dismiss();
            }
        });



    }
}

