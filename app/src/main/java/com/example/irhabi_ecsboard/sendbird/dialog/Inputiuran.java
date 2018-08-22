package com.example.irhabi_ecsboard.sendbird.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.Window;

import com.example.irhabi_ecsboard.sendbird.R;

public class Inputiuran {

    public void showInput(Activity activity, String msg, final Context mContext, final int idbarang){
        dialogView(activity);
    }

    public void dialogView(Activity activity){
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_inputiuran);
        dialog.show();
    }
}
