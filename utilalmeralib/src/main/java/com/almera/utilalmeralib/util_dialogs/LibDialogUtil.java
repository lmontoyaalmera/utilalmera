package com.almera.utilalmeralib.util_dialogs;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.almera.utilalmeralib.R;
import com.almera.utilalmeralib.viewUtil.LibKeyboardUtil;
import com.forms.sti.progresslitieigb.Inteface.IProgressLoadingIGB;
import com.forms.sti.progresslitieigb.Model.JSetting;
import com.forms.sti.progresslitieigb.ProgressLoadingJIGB;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textview.MaterialTextView;

public class LibDialogUtil {
    public static LibDialogLisener showProgressDialog(final Context context, String message){


        MaterialAlertDialogBuilder build=new MaterialAlertDialogBuilder(context);
        ConstraintLayout constraintLayout= (ConstraintLayout) LayoutInflater.from(context).inflate(R.layout.progress_dialog,null);
        AppCompatTextView textView=constraintLayout.findViewById(R.id.textview);
        textView.setText(message);
        build.setView(constraintLayout);
        Dialog progressDialog=build.create();
        return  new LibDialogLisener() {
            @Override
            public void hideDialog() {
                progressDialog.dismiss();
            }

            @Override
            public void showDialog() {
                progressDialog.show();
            }

            @Override
            public void changeMessage(String message) {
                textView.setText(message);
            }
        };
    }
    public static LibDialogLisener showProgressDialogOver(final Context context, String message){

        ProgressLoadingJIGB.setupLoading = (setup) ->  {
            setup.srcLottieJson =R.raw.loading; // Tour Source JSON Lottie
            setup.message = message;//  Center Message
            setup.timer = 0;   // Time of live for progress.
            setup.width = 200; // Optional
            setup.hight = 200; // Optional
        };

        return  new LibDialogLisener() {
            @Override
            public void hideDialog() {
                ProgressLoadingJIGB.finishLoadingJIGB(context);
            }

            @Override
            public void showDialog() {
                ProgressLoadingJIGB.startLoading(context);
                LibKeyboardUtil.hideKeyboard(context);
            }

            @Override
            public void changeMessage(String message) {
                ProgressLoadingJIGB.finishLoadingJIGB(context);
                ProgressLoadingJIGB.setupLoading = (setup) ->  {
                    setup.srcLottieJson =R.raw.loading; // Tour Source JSON Lottie
                    setup.message = message;//  Center Message
                    setup.timer = 0;   // Time of live for progress.
                    setup.width = 200; // Optional
                    setup.hight = 200; // Optional
                };
                ProgressLoadingJIGB.startLoading(context);
            }
        };
    }
}
