package com.almera.utilalmeralib.util_dialogs;

import android.app.ProgressDialog;
import android.content.Context;

public class DialogUtil {
    public static DialogLisener showProgressDialog(Context context,String message){

        final ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);
        progressDialog.setMessage(message);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        return  new DialogLisener() {
            @Override
            public void hideDialog() {
                progressDialog.dismiss();
            }

            @Override
            public void showDialog() {
                progressDialog.show();
            }
        };
    }
}
