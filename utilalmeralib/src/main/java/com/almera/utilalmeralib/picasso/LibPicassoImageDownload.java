package com.almera.utilalmeralib.picasso;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.Html;

import com.almera.utilalmeralib.archivosutil.LibArchivosUtil;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

public class LibPicassoImageDownload implements Html.ImageGetter {


    private Context context;
    private ImageDownload imageDownload;

    public LibPicassoImageDownload(Context context) {
        this.context = context;
    }

    public LibPicassoImageDownload(Context context, ImageDownload imageDownload) {
        this.context = context;
        this.imageDownload=imageDownload;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            Html.fromHtml(imageDownload.getText(), Html.FROM_HTML_MODE_LEGACY, this, null);
        } else {
            Html.fromHtml(imageDownload.getText(), this, null);
        }

    }

    @Override
    public Drawable getDrawable(final String source) {
        final BitmapDrawablePlaceHolder drawable = new BitmapDrawablePlaceHolder();

        LibArchivosUtil.downloadImageToLocalPrivate(source, context, imageDownload.getFilename() + "_" + LibArchivosUtil.getNameFile(source + ""));
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
            drawable.setBounds(0, 0, width, height);
            setBounds(0, 0, width, height);

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