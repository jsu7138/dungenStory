package com.example.dgstory;

import android.app.Dialog;
import android.content.Context;
import android.view.Window;

/**
 * Created by user on 2017-10-07.
 */

public class service_customPregressDialog extends Dialog {
    public service_customPregressDialog(Context context) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_progress);
    }
}
