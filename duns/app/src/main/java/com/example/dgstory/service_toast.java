package com.example.dgstory;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by user on 2017-10-03.
 * 사용 방법 예시:
 * service_toast st = new service_toast(LobbyActivity.this,"저장 완료!");
 * st.toast_start();
 * st.setDial("내용바꾸기");
 */

public class service_toast {
    public Context context;
    public String dial;

    public void setContext(Context context) {
        this.context = context;
    }

    public void setDial(String dial) {
        this.dial = dial;
    }

    public service_toast(Context context, String dial) {
        //토스트를 띄울 액티비티, 토스트 내용
        this.context = context;
        this.dial = dial;
    }

    public void toast_start(){
        Toast mToast = new Toast(context);
        View view = View.inflate(context, R.layout.toast_custom, null);
        TextView tv = (TextView) view.findViewById(R.id.tv_toast);
        tv.setText(dial);
        mToast.setView(view);
        mToast.setGravity(Gravity.CENTER, 0, 220);
        mToast.setDuration(Toast.LENGTH_SHORT);
        mToast.show();
    }
}
