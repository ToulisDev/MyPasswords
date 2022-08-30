package com.familyprotection.djasdatabase;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;

public class LoadingDialog {
    Activity activity;
    AlertDialog dialog;

    LoadingDialog(Activity myActivity){
        activity = myActivity;
    }

    void startLoadingDialog(){
        activity.runOnUiThread(() ->{
            AlertDialog.Builder builder = new AlertDialog.Builder(activity);

            LayoutInflater inflater = activity.getLayoutInflater();
            builder.setView(inflater.inflate(R.layout.refresh_popup, null));
            builder.setCancelable(false);

            dialog = builder.create();
            dialog.show();
        });
    }

    void dismissDialog(){
        activity.runOnUiThread(() -> {
            dialog.dismiss();
        });
    }
}
