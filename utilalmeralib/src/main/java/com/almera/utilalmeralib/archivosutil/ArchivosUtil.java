package com.almera.utilalmeralib.archivosutil;

import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.PictureDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.AppCompatTextView;
import android.text.Html;
import android.text.Spannable;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.TextView;

import com.almera.utilalmeralib.fileChooser.FileUtil;
import com.almera.utilalmeralib.picasso.FinishDowload;
import com.almera.utilalmeralib.picasso.ImageDownload;
import com.almera.utilalmeralib.picasso.PicassoImageDownload;
import com.almera.utilalmeralib.picasso.PicassoImageGetter;
import com.larvalabs.svgandroid.SVG;
import com.larvalabs.svgandroid.SVGParser;

import org.apache.commons.io.FileUtils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ArchivosUtil {
    private static DownloadManager downloadManager;

    /**
     * Valid if a size file is less or equal than number
     *
     * @param file   file to check
     * @param tamaño size for validate
     * @return true if size is less or equal than tamaño or false if otherwise
     */
    public static boolean validateFileSize(File file, int tamaño) throws Resources.NotFoundException, IOException {
        if (file.exists()) {
            byte[] array = null;
            array = FileUtils.readFileToByteArray(file);
            if (array.length <= tamaño) {
                return true;
            } else {
                return false;
            }
        } else {
            throw new Resources.NotFoundException();
        }

    }

    /**
     * This method convert whatever file to base 64
     *
     * @param file file for converted
     * @return string of the base 64 converted
     */
    public static String convertFileToBase64(File file) throws IOException {
        byte[] array = null;
        array = FileUtils.readFileToByteArray(file);
        return Base64.encodeToString(array, Base64.DEFAULT);
    }

    /**
     * This method open file with its repective intent example video, image, document
     *
     * @param context
     * @param path        of file to open
     * @param authorities
     */
    public static void openFileWithIntent(Context context, String path, String authorities) {

        File file = new File(path);
        MimeTypeMap map = MimeTypeMap.getSingleton();
        String extension = MimeTypeMap.getFileExtensionFromUrl(file.getName());
        String type = map.getMimeTypeFromExtension(extension);

        if (type == null)
            type = "*/*";

        Intent intent = new Intent(Intent.ACTION_VIEW);

        Uri data = FileProvider.getUriForFile(context, authorities, file);

        intent.setDataAndType(data, type);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        context.startActivity(intent);
    }

    /***
     * This method dowload imagenes png, jpg or svg and save in storage private
     * @param url url imagen,
     * @param context
     * @param filename path when, el file will grooved
     */
    public static void downloadImageToLocalPrivate(String url, Context context, String filename) {
        if (FileUtil.getExtension(url).toLowerCase().equals(".svg")) {
            saveImage(context, drawableToBitmap(url), filename);
        } else {
            saveImage(context, downloadImageBitmap(url), filename);
        }

    }

    // files are saved to /data/data/com.codexpedia.picassosaveimage/files
    public static void saveImage(Context context, Bitmap b, String imageName) {
        FileOutputStream foStream;
        try {
            foStream = context.openFileOutput(imageName, Context.MODE_PRIVATE);
            b.compress(Bitmap.CompressFormat.PNG, 100, foStream);
            foStream.close();
        } catch (Exception e) {
            Log.d("saveImage", "Exception 2, Something went wrong!");
            e.printStackTrace();
        }
    }

    public static Bitmap drawableToBitmap(String sUrl) {
        InputStream inputStream = null;   // Download Image from URL
        Bitmap bm = null;
        try {
            inputStream = new URL(sUrl).openStream();
            SVG svg = SVGParser.getSVGFromInputStream(inputStream);
            Drawable drawable = svg.createPictureDrawable();
            bm = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bm);
            canvas.drawPicture(((PictureDrawable) drawable).getPicture());

        } catch (IOException e) {
            e.printStackTrace();
        }
        return bm;
    }

    private static Bitmap downloadImageBitmap(String sUrl) {
        Bitmap bitmap = null;
        try {
            InputStream inputStream = new URL(sUrl).openStream();   // Download Image from URL
            bitmap = BitmapFactory.decodeStream(inputStream);       // Decode Bitmap
            inputStream.close();
        } catch (Exception e) {
            //  Log.d(TAG, "Exception 1, Something went wrong!");
            e.printStackTrace();
        }
        Log.d("saveImage", "Exception 2, Something went wrong!");
        return bitmap;
    }

    public static void loadImageFromDisk(ImageView v, Context context, String myImageName) {
        v.setImageBitmap(loadImageBitmap(context, myImageName));
    }

    public static Bitmap loadImageBitmap(Context context, String imageName) {
        Bitmap bitmap = null;
        FileInputStream fiStream;
        try {
            fiStream = context.openFileInput(imageName);
            bitmap = BitmapFactory.decodeStream(fiStream);
            WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            Display display = wm.getDefaultDisplay();
            DisplayMetrics metrics = new DisplayMetrics();
            display.getMetrics(metrics);
            int width = metrics.widthPixels;
            int height = metrics.heightPixels;

            if (width < bitmap.getWidth() && height < bitmap.getHeight()) {
                bitmap = Bitmap.createScaledBitmap(bitmap, width, height, false);
            }

            fiStream.close();
        } catch (Exception e) {
            Log.d("saveImage", "Exception 3, Something went wrong!");
            e.printStackTrace();
        }
        return bitmap;
    }

    public static  void DonwloadPicturesFromHtml(Context context, ArrayList<ImageDownload> imageDownloadList) {
        DonwloadPicassoFromHtml donwloadPicassoFromHtml = new DonwloadPicassoFromHtml(context);
        donwloadPicassoFromHtml.execute(imageDownloadList);
    }

    public static void DonwloadPicturesFromHtml(Context context, ArrayList<ImageDownload> imageDownloadList, FinishDowload finishDowload) {
        DonwloadPicassoFromHtml donwloadPicassoFromHtml = new DonwloadPicassoFromHtml(context, finishDowload);
        donwloadPicassoFromHtml.execute(imageDownloadList);
    }

    private static class DonwloadPicassoFromHtml extends AsyncTask<ArrayList<ImageDownload>, Void, Void> {
        private Context context;
        private FinishDowload finishDowload;

        public DonwloadPicassoFromHtml(Context context) {
            this.context = context;
        }

        public DonwloadPicassoFromHtml(Context context, FinishDowload finishDowload) {
            this.context = context;
            this.finishDowload = finishDowload;
        }

        @Override
        protected Void doInBackground(ArrayList<ImageDownload>... imageDownloads) {
            for (int i = 0; i < imageDownloads[0].size(); i++) {
                downloadPictureHTML(context, imageDownloads[0].get(i));
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (this.finishDowload != null) {
                this.finishDowload.onFinish(true);
            }
        }
    }


    public static void downloadPictureHTML(Context context, ImageDownload imageDownload) {
        TextView textView = new AppCompatTextView(context);
        textView.setClickable(true);
        PicassoImageDownload imageGetter = new PicassoImageDownload(context, imageDownload);
        Spannable html;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            html = (Spannable) Html.fromHtml(imageDownload.getText(), Html.FROM_HTML_MODE_LEGACY, imageGetter, null);
        } else {
            html = (Spannable) Html.fromHtml(imageDownload.getText(), imageGetter, null);
        }
        textView.setText(html);
        textView.setFocusable(true);
        textView.setFocusableInTouchMode(true);
    }

    public static String getNameFile(String path) {
        int startPosition = path.lastIndexOf("/") + 1;
        return path.substring(startPosition);
    }

    public static File cargarArchivoFileName(String filename, Context context) {
        File file = new File(context.getFilesDir() + "/" + filename);
        return file;
    }


}