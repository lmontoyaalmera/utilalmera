package com.almera.utilalmeralib.archivosutil;

import android.app.DownloadManager;
import android.content.ContentResolver;
import android.content.Context;
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
import android.provider.MediaStore;
import androidx.core.content.FileProvider;
import androidx.appcompat.widget.AppCompatTextView;
import android.text.Html;
import android.text.Spannable;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.TextView;

import com.almera.utilalmeralib.fileChooser.LibFileUtil;
import com.almera.utilalmeralib.libnetworkutil.LibRxManager;
import com.almera.utilalmeralib.picasso.LibFinishDowload;
import com.almera.utilalmeralib.picasso.ImageDownload;
import com.almera.utilalmeralib.picasso.LibPicassoImageDownload;
import com.almera.utilalmeralib.util_dialogs.LibDialogLisener;
import com.almera.utilalmeralib.util_dialogs.LibDialogUtil;
import com.larvalabs.svgandroid.SVG;
import com.larvalabs.svgandroid.SVGParseException;
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

import io.reactivex.observers.DisposableSingleObserver;
import okhttp3.ResponseBody;


public class LibArchivosUtil {
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
     * @param path    of file to open
     */
    public static void openFileWithIntent(Context context, String path) {

        File file = new File(path);
        MimeTypeMap map = MimeTypeMap.getSingleton();
        String extension = MimeTypeMap.getFileExtensionFromUrl(file.getName());
        String type = map.getMimeTypeFromExtension(extension);

        if (type == null)
            type = "*/*";

        Intent intent = new Intent(Intent.ACTION_VIEW);
        String authorities = context.getPackageName() + ".provider";
        Uri data = FileProvider.getUriForFile(context, authorities, file);

        intent.setDataAndType(data, type);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        context.startActivity(intent);
    }

    public static void openFileWithIntent(View view, String path, Context context) {
        File file = new File(path);
        MimeTypeMap map = MimeTypeMap.getSingleton();
        String extension = MimeTypeMap.getFileExtensionFromUrl(file.getName());
        String type = map.getMimeTypeFromExtension(extension);

        if (type == null)
            type = "*/*";

        Intent intent = new Intent(Intent.ACTION_VIEW);
        //Uri data = Uri.fromFile(file);
        String authorities = context.getPackageName() + ".provider";
        Uri data = FileProvider.getUriForFile(view.getContext(), authorities, file);

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
        if (LibFileUtil.getExtension(url).toLowerCase().equals(".svg")) {
            saveImage(context, drawableToBitmap(url), filename);
        } else {
            saveImage(context, downloadImageBitmap(url), filename);
        }
    }

    // files are saved to /data/data/com.codexpedia.picassosaveimage/files
    public static void saveImage(Context context, Bitmap b, String imageName) {
        if(b!=null) {
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
    }
    // files are saved to /data/data/com.codexpedia.picassosaveimage/files
    public static void saveFile(Context context, InputStream b, String imageName) {
        if(b!=null) {
            FileOutputStream foStream;
            try {
                foStream = context.openFileOutput(imageName, Context.MODE_PRIVATE);
                byte[] buffer = new byte[1024];
                int len1 = 0;
                while ( (len1 = b.read(buffer)) > 0 ) {
                    foStream.write(buffer);
                }
                foStream.close();
                foStream.close();
            } catch (Exception e) {
                Log.d("saveImage", "Exception 2, Something went wrong!");
                e.printStackTrace();
            }
        }
    }

    public static File saveBase64Temp(final Context context, final String imageData, String name) throws IOException {
        final byte[] imgBytesData = android.util.Base64.decode(imageData,
                android.util.Base64.DEFAULT);

        final File file = new File(context.getCacheDir(), name);
        final FileOutputStream fileOutputStream;
        try {
            fileOutputStream = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }

        final BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(
                fileOutputStream);
        try {
            bufferedOutputStream.write(imgBytesData);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                bufferedOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file;
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
        }catch (SVGParseException e) {
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

    public static void DonwloadPicturesFromHtml(Context context, ArrayList<ImageDownload> imageDownloadList) {
        DonwloadPicassoFromHtml donwloadPicassoFromHtml = new DonwloadPicassoFromHtml(context);
        donwloadPicassoFromHtml.execute(imageDownloadList);
    }

    public static void DonwloadPicturesFromHtml(Context context, ArrayList<ImageDownload> imageDownloadList, LibFinishDowload finishDowload) {
        DonwloadPicassoFromHtml donwloadPicassoFromHtml = new DonwloadPicassoFromHtml(context, finishDowload);
        donwloadPicassoFromHtml.execute(imageDownloadList);
    }

    private static class DonwloadPicassoFromHtml extends AsyncTask<ArrayList<ImageDownload>, Void, Void> {
        private Context context;
        private LibFinishDowload finishDowload;

        public DonwloadPicassoFromHtml(Context context) {
            this.context = context;
        }

        public DonwloadPicassoFromHtml(Context context, LibFinishDowload finishDowload) {
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
        LibPicassoImageDownload imageGetter = new LibPicassoImageDownload(context, imageDownload);
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

    public static File loadFileWithFileName(String filename, Context context) {
        File file = new File(context.getFilesDir() + "/" + filename);
        return file;
    }


    public static void openFileOrDownload(final Context context, String uri, String conexion,String token,final String dir, String id,final String nombre) {

        File file = new File(context.getCacheDir(),dir);
        final LibDialogLisener progressDialogIntentArchivo = LibDialogUtil.showProgressDialog(context, "Por favor espere...");
        MimeTypeMap map = MimeTypeMap.getSingleton();
        String extension = MimeTypeMap.getFileExtensionFromUrl(file.getName());
        String type = map.getMimeTypeFromExtension(extension);

        if (type == null)
            type = "*/*";
        if (file.exists()) {
            openFileWithIntent(context, context.getCacheDir()+"/"+dir);
        } else {
            progressDialogIntentArchivo.showDialog();

            final String finalType = type;
            LibRxManager rxManager = new LibRxManager( uri);
            rxManager.descargarArchivo(id,conexion,token, new DisposableSingleObserver<ResponseBody>() {
                @Override
                public void onSuccess(ResponseBody responseBody) {
                    String base64 = null;
                    try {
                        base64 = responseBody.string();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    try {
                        File file = LibArchivosUtil.saveBase64Temp(context, base64, nombre);
                        openFileWithIntent(context, file.getPath());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    progressDialogIntentArchivo.hideDialog();
                }

                @Override
                public void onError(Throwable e) {
                    progressDialogIntentArchivo.hideDialog();

                }
            });
        }

    }
    public static void deleteFile(String path) {
        File file = new File(path);
        file.delete();

    }
    public static void copyFile(String sourceFile, String destinationFile) {

        try {

            File inFile = new File(sourceFile);
            File outFile = new File(destinationFile);

            FileInputStream in = new FileInputStream(inFile);
            FileOutputStream out = new FileOutputStream(outFile);

            byte[] buffer = new byte[1024];
            int c;


            while ((c = in.read(buffer)) != -1)
                out.write(buffer, 0, c);

            out.flush();
            in.close();
            out.close();

        } catch (IOException e) {

            Log.e("Archvio util", "Hubo un error de entrada/salida!!!");

        }
    }

    public static boolean createFile(byte[] fileBytes, String archivoDestino) {
        boolean correcto = false;
        try {
            OutputStream out = new FileOutputStream(archivoDestino);
            out.write(fileBytes);
            out.close();
            correcto = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return correcto;

    }
    public static int getIdLastPhoto(Context context) {
        final String[] imageColumns = {MediaStore.Images.Media._ID, MediaStore.Images.Media.DATA};
        final String imageOrderBy = MediaStore.Images.Media._ID + " DESC";
        Cursor imageCursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, imageColumns, null, null, imageOrderBy);
        if (imageCursor.moveToFirst()) {
            int id = imageCursor.getInt(imageCursor.getColumnIndex(MediaStore.Images.Media._ID));
            String fullPath = imageCursor.getString(imageCursor.getColumnIndex(MediaStore.Images.Media.DATA));
            //Log.d("GF", "getLastImageId::id " + id);
            Log.d("GF", "getLastImageId::path " + fullPath);
            imageCursor.close();
            return id;
        } else {
            return 0;
        }
    }
    public static String getRealPathLastPhoto(Context context) {
        final String[] imageColumns = {MediaStore.Images.Media._ID, MediaStore.Images.Media.DATA};
        final String imageOrderBy = MediaStore.Images.Media._ID + " DESC";
        Cursor imageCursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, imageColumns, null, null, imageOrderBy);
        if (imageCursor.moveToFirst()) {
            int id = imageCursor.getInt(imageCursor.getColumnIndex(MediaStore.Images.Media._ID));
            String fullPath = imageCursor.getString(imageCursor.getColumnIndex(MediaStore.Images.Media.DATA));
            //Log.d("GF", "getLastImageId::id " + id);
            Log.d("GF", "getLastImageId::path " + fullPath);
            imageCursor.close();
            return fullPath;
        } else {
            return "";
        }
    }

    public static  void removeImageFromGallery(Context context,int id) {
        ContentResolver cr = context.getContentResolver();
        cr.delete(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, MediaStore.Images.Media._ID + "=?", new String[]{Long.toString(id)});
    }



}