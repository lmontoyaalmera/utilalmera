package com.almera.utilalmeralib.picasso;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.util.DisplayMetrics;
import android.widget.TextView;

import com.almera.utilalmeralib.archivosutil.LibArchivosUtil;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

public class LibPicassoImageGetterOnline implements Html.ImageGetter {

    private TextView textView = null;
    private Activity context;
    private String idFile;


    public LibPicassoImageGetterOnline(Activity context) {
        this.context = context;
    }

    public LibPicassoImageGetterOnline(TextView target, Activity context) {
        textView = target;
        this.context = context;
    }

    @Override
    public Drawable getDrawable(final String source) {
        final BitmapDrawablePlaceHolder drawable = new BitmapDrawablePlaceHolder();
        context.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    Picasso.with(context)
                            .load(source)
                            .into(drawable);
                }catch (Exception e){}
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