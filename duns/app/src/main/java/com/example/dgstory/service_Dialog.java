package com.example.dgstory;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by user on 2017-10-07.
 */

public class service_Dialog {
    service_sound music_se;
    public Context context;

    public service_Dialog(Context context) {
        this.context = context;
        music_se = new service_sound(context);
    }

    public void start(String TextWord){
        DialogInterface dialogInterface = null;
        View view = View.inflate(context, R.layout.dialog_custom, null);
        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setView(view);
        alert.setCancelable(false);
        alert.create();
        dialogInterface = alert.show();
        TextView tv = (TextView) view.findViewById(R.id.tv_dialog);
        ImageView btn_ok = (ImageView) view.findViewById(R.id.btn_dialog_ok);
        ImageView btn_cancel = (ImageView) view.findViewById(R.id.btn_dialog_cancel);
        btn_ok.setVisibility(View.INVISIBLE);
        btn_cancel.setImageResource(R.drawable.btn_ok);
        tv.setText(TextWord);
        final DialogInterface finalDialogInterface = dialogInterface;
        btn_cancel.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    ((ImageView) view).setColorFilter(0xaa111111);
                } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    music_se.start_clicked();
                    ((ImageView) view).setColorFilter(0x00000000);
                    finalDialogInterface.dismiss();
                }
                return true;
            }
        });
    }
}
