package com.example.dgstory;

import android.content.Context;
import android.content.Intent;

import java.util.ArrayList;

/**
 * Created by user on 2017-10-19.
 */

public class service_BattleScense {
    Context context;
    ArrayList<Class> classes;

    public service_BattleScense(Context context) {
        this.context = context;
        classes = new ArrayList<>();
        classes.add(Stage_1_Activity.class);
        classes.add(Stage_2_Activity.class);
        classes.add(Stage_3_Activity.class);
        classes.add(Stage_4_Activity.class);
        classes.add(Stage_5_Activity.class);
        classes.add(Stage_6_Activity.class);
        classes.add(Stage_7_Activity.class);
        classes.add(Stage_8_Activity.class);
        classes.add(Stage_9_Activity.class);
        classes.add(Stage_10_Activity.class);
        classes.add(Stage_11_Activity.class);
        classes.add(Stage_12_Activity.class);
        classes.add(Stage_13_Activity.class);
        classes.add(Stage_14_Activity.class);
        classes.add(Stage_15_Activity.class);
    }

    public void intentScense(int num){
        Intent i = new Intent(context,classes.get(num-1));
        i.putExtra("i",num);
        context.startActivity(i);
    }
}
