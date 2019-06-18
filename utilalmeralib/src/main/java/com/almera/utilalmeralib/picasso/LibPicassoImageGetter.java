package com.almera.utilalmeralib.picasso;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v4.content.FileProvider;
import android.text.Html;
import android.util.DisplayMetrics;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.TextView;

import com.almera.utilalmeralib.archivosutil.LibArchivosUtil;
import com.almera.utilalmeralib.libnetworkutil.LibRxManager;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.IOException;

import io.reactivex.observers.DisposableSingleObserver;
import okhttp3.ResponseBody;

public class LibPicassoImageGetter implements Html.ImageGetter {

    private TextView textView = null;
    private Activity context;
    private String idFile;


    public LibPicassoImageGetter(Activity context) {
        this.context = context;
    }

    public LibPicassoImageGetter(TextView target, Activity context, String idFile) {
        textView = target;
        this.context = context;
        this.idFile = idFile;
    }

    @Override
    public Drawable getDrawable(final String source) {
        final BitmapDrawablePlaceHolder drawable = new BitmapDrawablePlaceHolder();
        context.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Picasso.with(context)
                        .load(LibArchivosUtil.loadFileWithFileName(idFile + "_" + LibArchivosUtil.getNameFile(source), context))
                        .into(drawable);
            }
        });


        return drawable;
    }

    private class BitmapDrawablePlaceHolder extends BitmapDrawable implements Target {

        protected Drawable drawable;

        @Override
        public void draw(final Canvas canvas) {
            if (drawable != null) {
                drawable.draw(canvas);
            }
        }

        public void setDrawable(Drawable drawable) {
            this.drawable = drawable;
            int width = drawable.getIntrinsicWidth();
            int height = drawable.getIntrinsicHeight();
            DisplayMetrics metrics = new DisplayMetrics();
            context.getWindowManager().getDefaultDisplay().getMetrics(metrics);
            float metric = context.getResources().getDisplayMetrics().density;
            int heightPixels = metrics.heightPixels;

            int widthPixels = (int) (metrics.widthPixels -30 * metric);
            int heightn = (height * widthPixels) / width;
            drawable.setBounds(0, 0, widthPixels, (int) (heightn));
            setBounds(0, 0, widthPixels, (int) (heightn));
            if (textView != null) {
                textView.setText(textView.getText());
            }
        }

        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
            setDrawable(new BitmapDrawable(context.getResources(), bitmap));
        }

        @Override
        public void onBitmapFailed(Drawable errorDrawable) {
        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {

        }

    }




}