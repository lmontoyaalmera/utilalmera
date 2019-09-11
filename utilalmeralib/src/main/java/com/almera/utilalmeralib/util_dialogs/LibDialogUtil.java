package com.almera.utilalmeralib.util_dialogs;

import android.app.ProgressDialog;
import android.content.Context;

import com.almera.utilalmeralib.R;
import com.forms.sti.progresslitieigb.Inteface.IProgressLoadingIGB;
import com.forms.sti.progresslitieigb.Model.JSetting;
import com.forms.sti.progresslitieigb.ProgressLoadingJIGB;

public class LibDialogUtil {
    public static LibDialogLisener showProgressDialog(final Context context, String message){

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
