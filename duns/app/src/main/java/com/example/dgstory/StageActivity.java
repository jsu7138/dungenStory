package com.example.dgstory;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;

import static com.example.dgstory.MyHelper.KEY_FLOOR;
import static com.example.dgstory.MyHelper.KEY_NUM;
import static com.example.dgstory.MyHelper.QUERY_SELECT_ALL;
import static com.example.dgstory.MyHelper.QUERY_SELECT_EASY_CLEAR;
import static com.example.dgstory.MyHelper.QUERY_SELECT_EASY_CLOSE;
import static com.example.dgstory.MyHelper.QUERY_SELECT_EASY_OPEN;
import static com.example.dgstory.MyHelper.QUERY_SELECT_HARD_CLEAR;
import static com.example.dgstory.MyHelper.QUERY_SELECT_HARD_CLOSE;
import static com.example.dgstory.MyHelper.QUERY_SELECT_HARD_OPEN;
import static com.example.dgstory.MyHelper.QUERY_SELECT_NORMAL_CLEAR;
import static com.example.dgstory.MyHelper.QUERY_SELECT_NORMAL_CLOSE;
import static com.example.dgstory.MyHelper.QUERY_SELECT_NORMAL_OPEN;
import static com.example.dgstory.MyHelper.QUERY_SELECT_VERYHARD_OPEN;
import static com.example.dgstory.MyHelper.TABLE_NAME;

public class StageActivity extends AppCompatActivity {
    //DB선언
    public static MyHelper mHelper;
    SQLiteDatabase db;
    Cursor cursor;
    ImageView floor1, floor2, floor3, floor4, floor5, floor6, floor7, floor8, floor9, floor10;
    int check = 0;
    //클리어한 던전값을 리턴받으면 처리하는 함수를 판별용으로 함수선언
    int clearstage = 0;
    //난이도 선택한것을 밝게 해주기위해 상요
    ImageView stage_easy, stage_normal, stage_hard, stage_veryhard;
    //클릭한 난이도로 다시 돌아와도 위치하도록 표현하기 위해 사용
    ScrollView stage_scroll, stage_scroll2;
    service_BattleScense intentBattle;
    service_BattleScense intentBattlemap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stage);

        intentBattle = new service_BattleScense(StageActivity.this);
        intentBattlemap = new service_BattleScense(StageActivity.this);


        mHelper = new MyHelper(this);
        db = mHelper.getWritableDatabase();

        cursor = db.rawQuery(QUERY_SELECT_ALL + "", null);
        cursor.moveToFirst();

        //난이도 이미지찾기
        stage_easy = (ImageView) findViewById(R.id.stage_easy);
        stage_normal = (ImageView) findViewById(R.id.stage_normal);
        stage_hard = (ImageView) findViewById(R.id.stage_hard);
        stage_veryhard = (ImageView) findViewById(R.id.stage_veryhard);

        //각각에 위치한 층 찾아주기
        floor1 = (ImageView) findViewById(R.id.floor1);
        floor2 = (ImageView) findViewById(R.id.floor2);
        floor3 = (ImageView) findViewById(R.id.floor3);
        floor4 = (ImageView) findViewById(R.id.floor4);
        floor5 = (ImageView) findViewById(R.id.floor5);
        floor6 = (ImageView) findViewById(R.id.floor6);
        floor7 = (ImageView) findViewById(R.id.floor7);
        floor8 = (ImageView) findViewById(R.id.floor8);
        floor9 = (ImageView) findViewById(R.id.floor9);
        floor10 = (ImageView) findViewById(R.id.floor10);

        //스크롤뷰 찾기
        stage_scroll = (ScrollView) findViewById(R.id.stage_scroll);
        stage_scroll2 = (ScrollView) findViewById(R.id.stage_scroll2);


        Intent aa = getIntent();
        int asd = aa.getIntExtra("sum",0);

        if(asd > 0) {
            resultsetstage(asd);
        }
        resultsetstage(1);
        resultsetstage(2);
        resultsetstage(3);
        resultsetstage(4);
        resultsetstage(5);
        resultsetstage(6);
        resultsetstage(7);
        resultsetstage(8);
        //처음 실행시 1스테이지를 기준으로 갱신
        refreshCheck(1);
        imagefilter(0);

        // 난이도 클릭에 따른 이벤트
        stage_easy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                refreshCheck(1);
                imagefilter(0);
            }
        });
        stage_normal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                refreshCheck(2);
                imagefilter(10);
            }
        });
        stage_hard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                refreshCheck(3);
                imagefilter(20);
            }
        });
        stage_veryhard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                refreshCheck(4);
                imagefilter(30);
            }
        });

        //취소버튼 동작구현
        ImageView cancle = (ImageView) findViewById(R.id.stage_cancle);
        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(StageActivity.this, LobbyActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                finish();
            }
        });


        //clearstage에 값이 존재하면 동작 확인해야함
        if (clearstage != 0) {
            resultsetstage(clearstage);
            clearstage = 0;
        }
    }


    //화면에보여주는 스테이지를 난이도별로 새로고침, 이걸 실행하면 setstageLevel도 같이돌아감
    public void refreshCheck(int setstage) {
        if (setstage == 1) {
            cursor = db.rawQuery(QUERY_SELECT_EASY_OPEN + "", null);
            cursor.moveToFirst();
            while (cursor.getPosition() < cursor.getCount()) {
                if (cursor.getInt(0) == 1) {
                    floor1.setImageResource(R.drawable.icon_cave);
                    floor1.setVisibility(View.VISIBLE);
                } else if (cursor.getInt(0) == 2) {
                    floor2.setImageResource(R.drawable.icon_cave);
                    floor2.setVisibility(View.VISIBLE);
                } else if (cursor.getInt(0) == 3) {
                    floor3.setImageResource(R.drawable.icon_cave);
                    floor3.setVisibility(View.VISIBLE);
                } else if (cursor.getInt(0) == 4) {
                    floor4.setImageResource(R.drawable.icon_cave);
                    floor4.setVisibility(View.VISIBLE);
                } else if (cursor.getInt(0) == 5) {
                    floor5.setImageResource(R.drawable.icon_cave);
                    floor5.setVisibility(View.VISIBLE);
                } else if (cursor.getInt(0) == 6) {
                    floor6.setImageResource(R.drawable.icon_cave);
                    floor6.setVisibility(View.VISIBLE);
                } else if (cursor.getInt(0) == 7) {
                    floor7.setImageResource(R.drawable.icon_cave);
                    floor7.setVisibility(View.VISIBLE);
                } else if (cursor.getInt(0) == 8) {
                    floor8.setImageResource(R.drawable.icon_cave);
                    floor8.setVisibility(View.VISIBLE);
                } else if (cursor.getInt(0) == 9) {
                    floor9.setImageResource(R.drawable.icon_cave);
                    floor9.setVisibility(View.VISIBLE);
                } else if (cursor.getInt(0) == 10) {
                    floor10.setImageResource(R.drawable.icon_cave);
                    floor10.setVisibility(View.VISIBLE);
                }
                cursor.moveToNext();
            }
            cursor = db.rawQuery(QUERY_SELECT_EASY_CLOSE + "", null);
            cursor.moveToFirst();
            while (cursor.getPosition() < cursor.getCount()) {
                if (cursor.getInt(0) == 1) {
                    floor1.setImageResource(R.drawable.icon_cave);
                    floor1.setVisibility(View.INVISIBLE);
                } else if (cursor.getInt(0) == 2) {
                    floor2.setImageResource(R.drawable.icon_cave);
                    floor2.setVisibility(View.INVISIBLE);
                } else if (cursor.getInt(0) == 3) {
                    floor3.setImageResource(R.drawable.icon_cave);
                    floor3.setVisibility(View.INVISIBLE);
                } else if (cursor.getInt(0) == 4) {
                    floor4.setImageResource(R.drawable.icon_cave);
                    floor4.setVisibility(View.INVISIBLE);
                } else if (cursor.getInt(0) == 5) {
                    floor5.setImageResource(R.drawable.icon_cave);
                    floor5.setVisibility(View.INVISIBLE);
                } else if (cursor.getInt(0) == 6) {
                    floor6.setImageResource(R.drawable.icon_cave);
                    floor6.setVisibility(View.INVISIBLE);
                } else if (cursor.getInt(0) == 7) {
                    floor7.setImageResource(R.drawable.icon_cave);
                    floor7.setVisibility(View.INVISIBLE);
                } else if (cursor.getInt(0) == 8) {
                    floor8.setImageResource(R.drawable.icon_cave);
                    floor8.setVisibility(View.INVISIBLE);
                } else if (cursor.getInt(0) == 9) {
                    floor9.setImageResource(R.drawable.icon_cave);
                    floor9.setVisibility(View.INVISIBLE);
                } else if (cursor.getInt(0) == 10) {
                    floor10.setImageResource(R.drawable.icon_cave);
                    floor10.setVisibility(View.INVISIBLE);
                }
                cursor.moveToNext();
            }
            cursor = db.rawQuery(QUERY_SELECT_EASY_CLEAR + "", null);
            cursor.moveToFirst();
            while (cursor.getPosition() < cursor.getCount()) {
                if (cursor.getInt(0) == 1) {
                    floor1.setImageResource(R.drawable.icon_clear);
                    floor1.setVisibility(View.VISIBLE);
                } else if (cursor.getInt(0) == 2) {
                    floor2.setImageResource(R.drawable.icon_clear);
                    floor2.setVisibility(View.VISIBLE);
                } else if (cursor.getInt(0) == 3) {
                    floor3.setImageResource(R.drawable.icon_clear);
                    floor3.setVisibility(View.VISIBLE);
                } else if (cursor.getInt(0) == 4) {
                    floor4.setImageResource(R.drawable.icon_clear);
                    floor4.setVisibility(View.VISIBLE);
                } else if (cursor.getInt(0) == 5) {
                    floor5.setImageResource(R.drawable.icon_clear);
                    floor5.setVisibility(View.VISIBLE);
                } else if (cursor.getInt(0) == 6) {
                    floor6.setImageResource(R.drawable.icon_clear);
                    floor6.setVisibility(View.VISIBLE);
                } else if (cursor.getInt(0) == 7) {
                    floor7.setImageResource(R.drawable.icon_clear);
                    floor7.setVisibility(View.VISIBLE);
                } else if (cursor.getInt(0) == 8) {
                    floor8.setImageResource(R.drawable.icon_clear);
                    floor8.setVisibility(View.VISIBLE);
                } else if (cursor.getInt(0) == 9) {
                    floor9.setImageResource(R.drawable.icon_clear);
                    floor9.setVisibility(View.VISIBLE);
                } else if (cursor.getInt(0) == 10) {
                    floor10.setImageResource(R.drawable.icon_clear);
                    floor10.setVisibility(View.VISIBLE);
                }
                cursor.moveToNext();
            }
        } else if (setstage == 2) {
            cursor = db.rawQuery(QUERY_SELECT_NORMAL_OPEN + "", null);
            cursor.moveToFirst();
            while (cursor.getPosition() < cursor.getCount()) {
                if (cursor.getInt(0) == 1) {
                    floor1.setImageResource(R.drawable.icon_cave);
                    floor1.setVisibility(View.VISIBLE);
                } else if (cursor.getInt(0) == 2) {
                    floor2.setImageResource(R.drawable.icon_cave);
                    floor2.setVisibility(View.VISIBLE);
                } else if (cursor.getInt(0) == 3) {
                    floor3.setImageResource(R.drawable.icon_cave);
                    floor3.setVisibility(View.VISIBLE);
                } else if (cursor.getInt(0) == 4) {
                    floor4.setImageResource(R.drawable.icon_cave);
                    floor4.setVisibility(View.VISIBLE);
                } else if (cursor.getInt(0) == 5) {
                    floor5.setImageResource(R.drawable.icon_cave);
                    floor5.setVisibility(View.VISIBLE);
                } else if (cursor.getInt(0) == 6) {
                    floor6.setImageResource(R.drawable.icon_cave);
                    floor6.setVisibility(View.VISIBLE);
                } else if (cursor.getInt(0) == 7) {
                    floor7.setImageResource(R.drawable.icon_cave);
                    floor7.setVisibility(View.VISIBLE);
                } else if (cursor.getInt(0) == 8) {
                    floor8.setImageResource(R.drawable.icon_cave);
                    floor8.setVisibility(View.VISIBLE);
                } else if (cursor.getInt(0) == 9) {
                    floor9.setImageResource(R.drawable.icon_cave);
                    floor9.setVisibility(View.VISIBLE);
                } else if (cursor.getInt(0) == 10) {
                    floor10.setImageResource(R.drawable.icon_cave);
                    floor10.setVisibility(View.VISIBLE);
                }
                cursor.moveToNext();
            }
            cursor = db.rawQuery(QUERY_SELECT_NORMAL_CLOSE + "", null);
            cursor.moveToFirst();
            while (cursor.getPosition() < cursor.getCount()) {
                if (cursor.getInt(0) == 1) {
                    floor1.setImageResource(R.drawable.icon_cave);
                    floor1.setVisibility(View.INVISIBLE);
                } else if (cursor.getInt(0) == 2) {
                    floor2.setImageResource(R.drawable.icon_cave);
                    floor2.setVisibility(View.INVISIBLE);
                } else if (cursor.getInt(0) == 3) {
                    floor3.setImageResource(R.drawable.icon_cave);
                    floor3.setVisibility(View.INVISIBLE);
                } else if (cursor.getInt(0) == 4) {
                    floor4.setImageResource(R.drawable.icon_cave);
                    floor4.setVisibility(View.INVISIBLE);
                } else if (cursor.getInt(0) == 5) {
                    floor5.setImageResource(R.drawable.icon_cave);
                    floor5.setVisibility(View.INVISIBLE);
                } else if (cursor.getInt(0) == 6) {
                    floor6.setImageResource(R.drawable.icon_cave);
                    floor6.setVisibility(View.INVISIBLE);
                } else if (cursor.getInt(0) == 7) {
                    floor7.setImageResource(R.drawable.icon_cave);
                    floor7.setVisibility(View.INVISIBLE);
                } else if (cursor.getInt(0) == 8) {
                    floor8.setImageResource(R.drawable.icon_cave);
                    floor8.setVisibility(View.INVISIBLE);
                } else if (cursor.getInt(0) == 9) {
                    floor9.setImageResource(R.drawable.icon_cave);
                    floor9.setVisibility(View.INVISIBLE);
                } else if (cursor.getInt(0) == 10) {
                    floor10.setImageResource(R.drawable.icon_cave);
                    floor10.setVisibility(View.INVISIBLE);
                }
                cursor.moveToNext();
            }
            cursor = db.rawQuery(QUERY_SELECT_NORMAL_CLEAR + "", null);
            cursor.moveToFirst();
            while (cursor.getPosition() < cursor.getCount()) {
                if (cursor.getInt(0) == 1) {
                    floor1.setImageResource(R.drawable.icon_clear);
                    floor1.setVisibility(View.VISIBLE);
                } else if (cursor.getInt(0) == 2) {
                    floor2.setImageResource(R.drawable.icon_clear);
                    floor2.setVisibility(View.VISIBLE);
                } else if (cursor.getInt(0) == 3) {
                    floor3.setImageResource(R.drawable.icon_clear);
                    floor3.setVisibility(View.VISIBLE);
                } else if (cursor.getInt(0) == 4) {
                    floor4.setImageResource(R.drawable.icon_clear);
                    floor4.setVisibility(View.VISIBLE);
                } else if (cursor.getInt(0) == 5) {
                    floor5.setImageResource(R.drawable.icon_clear);
                    floor5.setVisibility(View.VISIBLE);
                } else if (cursor.getInt(0) == 6) {
                    floor6.setImageResource(R.drawable.icon_clear);
                    floor6.setVisibility(View.VISIBLE);
                } else if (cursor.getInt(0) == 7) {
                    floor7.setImageResource(R.drawable.icon_clear);
                    floor7.setVisibility(View.VISIBLE);
                } else if (cursor.getInt(0) == 8) {
                    floor8.setImageResource(R.drawable.icon_clear);
                    floor8.setVisibility(View.VISIBLE);
                } else if (cursor.getInt(0) == 9) {
                    floor9.setImageResource(R.drawable.icon_clear);
                    floor9.setVisibility(View.VISIBLE);
                } else if (cursor.getInt(0) == 10) {
                    floor10.setImageResource(R.drawable.icon_clear);
                    floor10.setVisibility(View.VISIBLE);
                }
                cursor.moveToNext();
            }
        } else if (setstage == 3) {
            cursor = db.rawQuery(QUERY_SELECT_HARD_OPEN + "", null);
            cursor.moveToFirst();
            while (cursor.getPosition() < cursor.getCount()) {
                if (cursor.getInt(0) == 1) {
                    floor1.setImageResource(R.drawable.icon_cave);
                    floor1.setVisibility(View.VISIBLE);
                } else if (cursor.getInt(0) == 2) {
                    floor2.setImageResource(R.drawable.icon_cave);
                    floor2.setVisibility(View.VISIBLE);
                } else if (cursor.getInt(0) == 3) {
                    floor3.setImageResource(R.drawable.icon_cave);
                    floor3.setVisibility(View.VISIBLE);
                } else if (cursor.getInt(0) == 4) {
                    floor4.setImageResource(R.drawable.icon_cave);
                    floor4.setVisibility(View.VISIBLE);
                } else if (cursor.getInt(0) == 5) {
                    floor5.setImageResource(R.drawable.icon_cave);
                    floor5.setVisibility(View.VISIBLE);
                } else if (cursor.getInt(0) == 6) {
                    floor6.setImageResource(R.drawable.icon_cave);
                    floor6.setVisibility(View.VISIBLE);
                } else if (cursor.getInt(0) == 7) {
                    floor7.setImageResource(R.drawable.icon_cave);
                    floor7.setVisibility(View.VISIBLE);
                } else if (cursor.getInt(0) == 8) {
                    floor8.setImageResource(R.drawable.icon_cave);
                    floor8.setVisibility(View.VISIBLE);
                } else if (cursor.getInt(0) == 9) {
                    floor9.setImageResource(R.drawable.icon_cave);
                    floor9.setVisibility(View.VISIBLE);
                } else if (cursor.getInt(0) == 10) {
                    floor10.setImageResource(R.drawable.icon_cave);
                    floor10.setVisibility(View.VISIBLE);
                }
                cursor.moveToNext();
            }
            cursor = db.rawQuery(QUERY_SELECT_HARD_CLOSE + "", null);
            cursor.moveToFirst();
            while (cursor.getPosition() < cursor.getCount()) {
                if (cursor.getInt(0) == 1) {
                    floor1.setImageResource(R.drawable.icon_cave);
                    floor1.setVisibility(View.INVISIBLE);
                } else if (cursor.getInt(0) == 2) {
                    floor2.setImageResource(R.drawable.icon_cave);
                    floor2.setVisibility(View.INVISIBLE);
                } else if (cursor.getInt(0) == 3) {
                    floor3.setImageResource(R.drawable.icon_cave);
                    floor3.setVisibility(View.INVISIBLE);
                } else if (cursor.getInt(0) == 4) {
                    floor4.setImageResource(R.drawable.icon_cave);
                    floor4.setVisibility(View.INVISIBLE);
                } else if (cursor.getInt(0) == 5) {
                    floor5.setImageResource(R.drawable.icon_cave);
                    floor5.setVisibility(View.INVISIBLE);
                } else if (cursor.getInt(0) == 6) {
                    floor6.setImageResource(R.drawable.icon_cave);
                    floor6.setVisibility(View.INVISIBLE);
                } else if (cursor.getInt(0) == 7) {
                    floor7.setImageResource(R.drawable.icon_cave);
                    floor7.setVisibility(View.INVISIBLE);
                } else if (cursor.getInt(0) == 8) {
                    floor8.setImageResource(R.drawable.icon_cave);
                    floor8.setVisibility(View.INVISIBLE);
                } else if (cursor.getInt(0) == 9) {
                    floor9.setImageResource(R.drawable.icon_cave);
                    floor9.setVisibility(View.INVISIBLE);
                } else if (cursor.getInt(0) == 10) {
                    floor10.setImageResource(R.drawable.icon_cave);
                    floor10.setVisibility(View.INVISIBLE);
                }
                cursor.moveToNext();
            }
            cursor = db.rawQuery(QUERY_SELECT_HARD_CLEAR + "", null);
            cursor.moveToFirst();
            while (cursor.getPosition() < cursor.getCount()) {
                if (cursor.getInt(0) == 1) {
                    floor1.setImageResource(R.drawable.icon_clear);
                    floor1.setVisibility(View.VISIBLE);
                } else if (cursor.getInt(0) == 2) {
                    floor2.setImageResource(R.drawable.icon_clear);
                    floor2.setVisibility(View.VISIBLE);
                } else if (cursor.getInt(0) == 3) {
                    floor3.setImageResource(R.drawable.icon_clear);
                    floor3.setVisibility(View.VISIBLE);
                } else if (cursor.getInt(0) == 4) {
                    floor4.setImageResource(R.drawable.icon_clear);
                    floor4.setVisibility(View.VISIBLE);
                } else if (cursor.getInt(0) == 5) {
                    floor5.setImageResource(R.drawable.icon_clear);
                    floor5.setVisibility(View.VISIBLE);
                } else if (cursor.getInt(0) == 6) {
                    floor6.setImageResource(R.drawable.icon_clear);
                    floor6.setVisibility(View.VISIBLE);
                } else if (cursor.getInt(0) == 7) {
                    floor7.setImageResource(R.drawable.icon_clear);
                    floor7.setVisibility(View.VISIBLE);
                } else if (cursor.getInt(0) == 8) {
                    floor8.setImageResource(R.drawable.icon_clear);
                    floor8.setVisibility(View.VISIBLE);
                } else if (cursor.getInt(0) == 9) {
                    floor9.setImageResource(R.drawable.icon_clear);
                    floor9.setVisibility(View.VISIBLE);
                } else if (cursor.getInt(0) == 10) {
                    floor10.setImageResource(R.drawable.icon_clear);
                    floor10.setVisibility(View.VISIBLE);
                }
                cursor.moveToNext();
            }
        } else if (setstage == 4) {
            cursor = db.rawQuery(QUERY_SELECT_VERYHARD_OPEN + "", null);
            cursor.moveToFirst();
            while (cursor.getPosition() < cursor.getCount()) {
                if (cursor.getInt(0) == 1) {
                    floor1.setImageResource(R.drawable.icon_cave);
                    floor1.setVisibility(View.VISIBLE);
                } else if (cursor.getInt(0) == 2) {
                    floor2.setImageResource(R.drawable.icon_cave);
                    floor2.setVisibility(View.VISIBLE);
                } else if (cursor.getInt(0) == 3) {
                    floor3.setImageResource(R.drawable.icon_cave);
                    floor3.setVisibility(View.VISIBLE);
                } else if (cursor.getInt(0) == 4) {
                    floor4.setImageResource(R.drawable.icon_cave);
                    floor4.setVisibility(View.VISIBLE);
                } else if (cursor.getInt(0) == 5) {
                    floor5.setImageResource(R.drawable.icon_cave);
                    floor5.setVisibility(View.VISIBLE);
                } else if (cursor.getInt(0) == 6) {
                    floor6.setImageResource(R.drawable.icon_cave);
                    floor6.setVisibility(View.VISIBLE);
                } else if (cursor.getInt(0) == 7) {
                    floor7.setImageResource(R.drawable.icon_cave);
                    floor7.setVisibility(View.VISIBLE);
                } else if (cursor.getInt(0) == 8) {
                    floor8.setImageResource(R.drawable.icon_cave);
                    floor8.setVisibility(View.VISIBLE);
                } else if (cursor.getInt(0) == 9) {
                    floor9.setImageResource(R.drawable.icon_cave);
                    floor9.setVisibility(View.VISIBLE);
                } else if (cursor.getInt(0) == 10) {
                    floor10.setImageResource(R.drawable.icon_cave);
                    floor10.setVisibility(View.VISIBLE);
                }
                cursor.moveToNext();
            }
            cursor = db.rawQuery(QUERY_SELECT_HARD_CLOSE + "", null);
            cursor.moveToFirst();
            while (cursor.getPosition() < cursor.getCount()) {
                if (cursor.getInt(0) == 1) {
                    floor1.setImageResource(R.drawable.icon_cave);
                    floor1.setVisibility(View.INVISIBLE);
                } else if (cursor.getInt(0) == 2) {
                    floor2.setImageResource(R.drawable.icon_cave);
                    floor2.setVisibility(View.INVISIBLE);
                } else if (cursor.getInt(0) == 3) {
                    floor3.setImageResource(R.drawable.icon_cave);
                    floor3.setVisibility(View.INVISIBLE);
                } else if (cursor.getInt(0) == 4) {
                    floor4.setImageResource(R.drawable.icon_cave);
                    floor4.setVisibility(View.INVISIBLE);
                } else if (cursor.getInt(0) == 5) {
                    floor5.setImageResource(R.drawable.icon_cave);
                    floor5.setVisibility(View.INVISIBLE);
                } else if (cursor.getInt(0) == 6) {
                    floor6.setImageResource(R.drawable.icon_cave);
                    floor6.setVisibility(View.INVISIBLE);
                } else if (cursor.getInt(0) == 7) {
                    floor7.setImageResource(R.drawable.icon_cave);
                    floor7.setVisibility(View.INVISIBLE);
                } else if (cursor.getInt(0) == 8) {
                    floor8.setImageResource(R.drawable.icon_cave);
                    floor8.setVisibility(View.INVISIBLE);
                } else if (cursor.getInt(0) == 9) {
                    floor9.setImageResource(R.drawable.icon_cave);
                    floor9.setVisibility(View.INVISIBLE);
                } else if (cursor.getInt(0) == 10) {
                    floor10.setImageResource(R.drawable.icon_cave);
                    floor10.setVisibility(View.INVISIBLE);
                }
                cursor.moveToNext();
            }
            cursor = db.rawQuery(QUERY_SELECT_HARD_CLEAR + "", null);
            cursor.moveToFirst();
            while (cursor.getPosition() < cursor.getCount()) {
                if (cursor.getInt(0) == 1) {
                    floor1.setImageResource(R.drawable.icon_clear);
                    floor1.setVisibility(View.VISIBLE);
                } else if (cursor.getInt(0) == 2) {
                    floor2.setImageResource(R.drawable.icon_clear);
                    floor2.setVisibility(View.VISIBLE);
                } else if (cursor.getInt(0) == 3) {
                    floor3.setImageResource(R.drawable.icon_clear);
                    floor3.setVisibility(View.VISIBLE);
                } else if (cursor.getInt(0) == 4) {
                    floor4.setImageResource(R.drawable.icon_clear);
                    floor4.setVisibility(View.VISIBLE);
                } else if (cursor.getInt(0) == 5) {
                    floor5.setImageResource(R.drawable.icon_clear);
                    floor5.setVisibility(View.VISIBLE);
                } else if (cursor.getInt(0) == 6) {
                    floor6.setImageResource(R.drawable.icon_clear);
                    floor6.setVisibility(View.VISIBLE);
                } else if (cursor.getInt(0) == 7) {
                    floor7.setImageResource(R.drawable.icon_clear);
                    floor7.setVisibility(View.VISIBLE);
                } else if (cursor.getInt(0) == 8) {
                    floor8.setImageResource(R.drawable.icon_clear);
                    floor8.setVisibility(View.VISIBLE);
                } else if (cursor.getInt(0) == 9) {
                    floor9.setImageResource(R.drawable.icon_clear);
                    floor9.setVisibility(View.VISIBLE);
                } else if (cursor.getInt(0) == 10) {
                    floor10.setImageResource(R.drawable.icon_clear);
                    floor10.setVisibility(View.VISIBLE);
                }
                cursor.moveToNext();
            }
        }
        setstageLevel(setstage);
    }

    // 1~40까지의 스테이지중에 클리어한 값을받아 db에서 검사후 값이 있으면 테이블수정D
    public void resultsetstage(int clearstage) {
        int ia = clearstage;

        final String QUERY_SELECT_STAGE = String.format("select %s from %s WHERE %s = '%s'", KEY_FLOOR, TABLE_NAME, KEY_NUM, clearstage);
        cursor = db.rawQuery(QUERY_SELECT_STAGE + "", null);
        cursor.moveToFirst();
        String QUERY_SET_CLEAR = String.format("UPDATE %s SET _type = '%s' where %s = '%s' and _type = '%s'", TABLE_NAME, "clear", KEY_NUM, clearstage, "open");
        String QUERY_SET_OPEN = null;
        if (cursor.getCount() == 1) {
            if (cursor.getInt(0) == 1) {
                db.execSQL(QUERY_SET_CLEAR);

                QUERY_SET_OPEN = String.format("UPDATE %s SET _type = '%s' where _type LIKE '%s' and _num = '%s'", TABLE_NAME, "open", "close", ia + 1);
                db.execSQL(QUERY_SET_OPEN);
                QUERY_SET_OPEN = String.format("UPDATE %s SET _type = '%s' where _type LIKE '%s' and _num = '%s'", TABLE_NAME, "open", "close", ia + 3);
                db.execSQL(QUERY_SET_OPEN);


            } else if (cursor.getInt(0) == 2) {
                db.execSQL(QUERY_SET_CLEAR);

                QUERY_SET_OPEN = String.format("UPDATE %s SET _type = '%s' where _type LIKE '%s' and _num = '%s'", TABLE_NAME, "open", "close", ia - 1);
                db.execSQL(QUERY_SET_OPEN);
                QUERY_SET_OPEN = String.format("UPDATE %s SET _type = '%s' where _type LIKE '%s' and _num = '%s'", TABLE_NAME, "open", "close", ia + 1);
                db.execSQL(QUERY_SET_OPEN);
                QUERY_SET_OPEN = String.format("UPDATE %s SET _type = '%s' where _type LIKE '%s' and _num = '%s'", TABLE_NAME, "open", "close", ia + 3);
                db.execSQL(QUERY_SET_OPEN);
            } else if (cursor.getInt(0) == 3) {
                db.execSQL(QUERY_SET_CLEAR);

                QUERY_SET_OPEN = String.format("UPDATE %s SET _type = '%s' where _type LIKE '%s' and _num = '%s'", TABLE_NAME, "open", "close", ia - 1);
                db.execSQL(QUERY_SET_OPEN);
                QUERY_SET_OPEN = String.format("UPDATE %s SET _type = '%s' where _type LIKE '%s' and _num = '%s'", TABLE_NAME, "open", "close", ia + 3);
                db.execSQL(QUERY_SET_OPEN);
            } else if (cursor.getInt(0) == 4) {
                db.execSQL(QUERY_SET_CLEAR);

                QUERY_SET_OPEN = String.format("UPDATE %s SET _type = '%s' where _type LIKE '%s' and _num = '%s'", TABLE_NAME, "open", "close", ia + 1);
                db.execSQL(QUERY_SET_OPEN);
                QUERY_SET_OPEN = String.format("UPDATE %s SET _type = '%s' where _type LIKE '%s' and _num = '%s'", TABLE_NAME, "open", "close", ia + 3);
                db.execSQL(QUERY_SET_OPEN);
            } else if (cursor.getInt(0) == 5) {
                db.execSQL(QUERY_SET_CLEAR);

                QUERY_SET_OPEN = String.format("UPDATE %s SET _type = '%s' where _type LIKE '%s' and _num = '%s'", TABLE_NAME, "open", "close", ia - 3);
                db.execSQL(QUERY_SET_OPEN);
                QUERY_SET_OPEN = String.format("UPDATE %s SET _type = '%s' where _type LIKE '%s' and _num = '%s'", TABLE_NAME, "open", "close", ia - 1);
                db.execSQL(QUERY_SET_OPEN);
                QUERY_SET_OPEN = String.format("UPDATE %s SET _type = '%s' where _type LIKE '%s' and _num = '%s'", TABLE_NAME, "open", "close", ia + 1);
                db.execSQL(QUERY_SET_OPEN);
                QUERY_SET_OPEN = String.format("UPDATE %s SET _type = '%s' where _type LIKE '%s' and _num = '%s'", TABLE_NAME, "open", "close", ia + 3);
                db.execSQL(QUERY_SET_OPEN);
            } else if (cursor.getInt(0) == 6) {
                db.execSQL(QUERY_SET_CLEAR);

                QUERY_SET_OPEN = String.format("UPDATE %s SET _type = '%s' where _type LIKE '%s' and _num = '%s'", TABLE_NAME, "open", "close", ia - 3);
                db.execSQL(QUERY_SET_OPEN);
                QUERY_SET_OPEN = String.format("UPDATE %s SET _type = '%s' where _type LIKE '%s' and _num = '%s'", TABLE_NAME, "open", "close", ia - 1);
                db.execSQL(QUERY_SET_OPEN);
                QUERY_SET_OPEN = String.format("UPDATE %s SET _type = '%s' where _type LIKE '%s' and _num = '%s'", TABLE_NAME, "open", "close", ia + 3);
                db.execSQL(QUERY_SET_OPEN);
            } else if (cursor.getInt(0) == 7) {
                db.execSQL(QUERY_SET_CLEAR);

                QUERY_SET_OPEN = String.format("UPDATE %s SET _type = '%s' where _type LIKE '%s' and _num = '%s'", TABLE_NAME, "open", "close", ia - 3);
                db.execSQL(QUERY_SET_OPEN);
                QUERY_SET_OPEN = String.format("UPDATE %s SET _type = '%s' where _type LIKE '%s' and _num = '%s'", TABLE_NAME, "open", "close", ia + 1);
                db.execSQL(QUERY_SET_OPEN);
            } else if (cursor.getInt(0) == 8) {
                db.execSQL(QUERY_SET_CLEAR);

                QUERY_SET_OPEN = String.format("UPDATE %s SET _type = '%s' where _type LIKE '%s' and _num = '%s'", TABLE_NAME, "open", "close", ia - 3);
                db.execSQL(QUERY_SET_OPEN);
                QUERY_SET_OPEN = String.format("UPDATE %s SET _type = '%s' where _type LIKE '%s' and _num = '%s'", TABLE_NAME, "open", "close", ia - 1);
                db.execSQL(QUERY_SET_OPEN);
                QUERY_SET_OPEN = String.format("UPDATE %s SET _type = '%s' where _type LIKE '%s' and _num = '%s'", TABLE_NAME, "open", "close", ia + 1);
                db.execSQL(QUERY_SET_OPEN);
                QUERY_SET_OPEN = String.format("UPDATE %s SET _type = '%s' where _type LIKE '%s' and _num = '%s'", TABLE_NAME, "open", "close", ia + 2);
                db.execSQL(QUERY_SET_OPEN);
            } else if (cursor.getInt(0) == 9) {
                db.execSQL(QUERY_SET_CLEAR);

                QUERY_SET_OPEN = String.format("UPDATE %s SET _type = '%s' where _type LIKE '%s' and _num = '%s'", TABLE_NAME, "open", "close", ia - 3);
                db.execSQL(QUERY_SET_OPEN);
                QUERY_SET_OPEN = String.format("UPDATE %s SET _type = '%s' where _type LIKE '%s' and _num = '%s'", TABLE_NAME, "open", "close", ia - 1);
                db.execSQL(QUERY_SET_OPEN);
            } else if (cursor.getInt(0) == 10) {
                db.execSQL(QUERY_SET_CLEAR);

                QUERY_SET_OPEN = String.format("UPDATE %s SET _type = '%s' where _type LIKE '%s' and _num = '%s'", TABLE_NAME, "open", "close", ia + 1);
                db.execSQL(QUERY_SET_OPEN);
            }

            //한번에 처리해주기위해서 여기서 사용
            imagefilter(clearstage);
        } else {
            Toast.makeText(this, "잘못된점이 있습니다" + cursor.getCount(), Toast.LENGTH_SHORT).show();
        }

    }

    //난이도 선택창에 클릭한 효과를 주기위해서 사용 보스클리어하면 다음난이도로 자동으로 넘어감
    public void imagefilter(int num) {
        if (num < 10) {
            stage_easy.setColorFilter(0x00000000);
            stage_normal.setColorFilter(0xaa111111);
            stage_hard.setColorFilter(0xaa111111);
            stage_veryhard.setColorFilter(0xaa111111);
            stage_scroll2.scrollTo(0, 0);
            refreshCheck(1);
        } else if (num < 20) {
            stage_easy.setColorFilter(0xaa111111);
            stage_normal.setColorFilter(0x00000000);
            stage_hard.setColorFilter(0xaa111111);
            stage_veryhard.setColorFilter(0xaa111111);
            stage_scroll.scrollTo(0, 400);
            stage_scroll2.scrollTo(0, 0);
            refreshCheck(2);
        } else if (num < 30) {
            stage_easy.setColorFilter(0xaa111111);
            stage_normal.setColorFilter(0xaa111111);
            stage_hard.setColorFilter(0x00000000);
            stage_veryhard.setColorFilter(0xaa111111);
            stage_scroll.scrollTo(0, 900);
            stage_scroll2.scrollTo(0, 0);
            refreshCheck(3);
        } else if (num <= 40) {
            stage_easy.setColorFilter(0xaa111111);
            stage_normal.setColorFilter(0xaa111111);
            stage_hard.setColorFilter(0xaa111111);
            stage_veryhard.setColorFilter(0x00000000);
            stage_scroll.scrollTo(0, 1400);
            stage_scroll2.scrollTo(0, 0);
            refreshCheck(4);
        }


    }

    //난이도별 인텐트를 위한 온클릭 리스너
    public void setstageLevel(int difficulty) {
        if (difficulty == 1) {
            floor1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialogresult(1);
                }
            });
            floor2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialogresult(2);
                }
            });
            floor3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialogresult(3);
                }
            });
            floor4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialogresult(4);
                }
            });
            floor5.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialogresult(5);
                }
            });
            floor6.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialogresult(6);
                }
            });
            floor7.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialogresult(7);
                }
            });
            floor8.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialogresult(8);
                }
            });
            floor9.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialogresult(9);
                }
            });
            floor10.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialogresult(10);
                }
            });
        } else if (difficulty == 2) {
            floor1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialogresult(11);
                }
            });
            floor2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialogresult(12);
                }
            });
            floor3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialogresult(13);
                }
            });
            floor4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialogresult(14);
                }
            });
            floor5.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialogresult(15);
                }
            });
            floor6.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialogresult(16);
                }
            });
            floor7.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialogresult(17);
                }
            });
            floor8.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialogresult(18);
                }
            });
            floor9.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialogresult(19);
                }
            });
            floor10.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialogresult(20);
                }
            });
        } else if (difficulty == 3) {
            floor1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialogresult(21);
                }
            });
            floor2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialogresult(22);
                }
            });
            floor3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialogresult(23);
                }
            });
            floor4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialogresult(24);
                }
            });
            floor5.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialogresult(25);
                }
            });
            floor6.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialogresult(26);
                }
            });
            floor7.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialogresult(27);
                }
            });
            floor8.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialogresult(28);
                }
            });
            floor9.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialogresult(29);
                }
            });
            floor10.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialogresult(30);
                }
            });
        } else if (difficulty == 4) {
            floor1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialogresult(31);
                }
            });
            floor2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialogresult(32);
                }
            });
            floor3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialogresult(33);
                }
            });
            floor4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialogresult(34);
                }
            });
            floor5.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialogresult(35);
                }
            });
            floor6.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialogresult(36);
                }
            });
            floor7.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialogresult(37);
                }
            });
            floor8.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialogresult(38);
                }
            });
            floor9.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialogresult(39);
                }
            });
            floor10.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialogresult(40);
                }
            });
        } else {
            Toast.makeText(this, "error 인텐트부여", Toast.LENGTH_SHORT).show();
        }

    }

    //던전 선택 확인창    setstageLevel에서 인텐트 부여후 던전 stage 값 넘겨줌 확인눌르면 행동
    public void dialogresult(int num) {
        final View dialogView = View.inflate(StageActivity.this, R.layout.dialog_askagain, null);
        final AlertDialog.Builder alert = new AlertDialog.Builder(StageActivity.this);
        alert.setView(dialogView);
        alert.setCancelable(false);
        alert.create();
        DialogInterface dialogInterface = alert.show();

        final int stage = num;

        ImageView imgcancle = (ImageView) dialogView.findViewById(R.id.askagain_cancle);
        ImageView imgok = (ImageView) dialogView.findViewById(R.id.askagain_ok);

        final DialogInterface finaldialogInterface = dialogInterface;
        imgcancle.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    ((ImageView) view).setColorFilter(0xaa111111);
                } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    ((ImageView) view).setColorFilter(0x00000000);
                    finaldialogInterface.dismiss();
                }
                return true;
            }
        });

        // 2가지 상황 50퍼 확률 50퍼 확률로 던전에 그냥 입장하거나 이벤트발생 엑티비티로 이동
        imgok.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    ((ImageView) view).setColorFilter(0xaa111111);
                } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    ((ImageView) view).setColorFilter(0x00000000);
                    double dice = rand();

                    if (dice < 80.0) {
                        intentBattle.intentScense(stage);
                        finish();
                    } else {
                        //던전 번호 넘기기
                        Intent I = new Intent(StageActivity.this, Eventgoin.class);
                        I.putExtra("num", stage);
                        startActivity(I);
                        finish();

                    }

                }
                return true;
            }
        });


        //                    intentBattle.intentScense(4);
    }

    private double rand() {
        Random random = new Random();
        int r = random.nextInt(1000);
        r = r / 10;
        double percentage = (double) r / 100.0 * 100.0;
        return percentage;
    }
}