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
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;

import com.almera.utilalmeralib.fileChooser.FileUtil;
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
    public static String convertFileToBase64(File file)throws IOException {
        byte[] array = null;
        array = FileUtils.readFileToByteArray(file);
        return Base64.encodeToString(array, Base64.DEFAULT);
    }

    /**
     * This method open file with its repective intent example video, image, document
     * @param context
     * @param path of file to open
     * @param authorities
     */
    public static void openFileWithIntent( Context context,String path,String authorities ) {

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

    public static String getRealPathFromUri(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    public static File getFileTarget(final String imageDir, final String imageName, Context context) {
        ContextWrapper cw = new ContextWrapper(context);
        File directory = cw.getDir(imageDir, Context.MODE_PRIVATE);
        File myImageFile = new File(directory, imageName);
        return myImageFile;
    }

    public static void deleteFileTarget(final String imageDir, final String imageName, Context context) {
        ContextWrapper cw = new ContextWrapper(context);
        File directory = cw.getDir(imageDir, Context.MODE_PRIVATE);
        File myImageFile = new File(directory, imageName);
        if (myImageFile.delete()) {
            Log.d("aa", "image on the disk deleted successfully!");
        }
    }

    public static File cargarArchivoFileName(String filename, Context context) {
        File file = new File(context.getFilesDir() + "/" + filename);
        return file;
    }

    public static Boolean guardarArchivoFil(String fileName, File file, Context context) {
        FileOutputStream fos;
        byte[] array = null;
        try {
            array = FileUtils.readFileToByteArray(file);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        try {
            fos = context.openFileOutput(fileName, Context.MODE_PRIVATE);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        try {
            fos.write(array);
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static Boolean guardarArchivoArra(String fileName, byte[] file, Context context) {
        FileOutputStream fos;
        try {
            fos = context.openFileOutput(fileName, Context.MODE_PRIVATE);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        try {
            fos.write(file);
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static long DownloadData(Uri uri, Context context) {

        long downloadReference;

        // Create request for android download manager

        downloadManager = (DownloadManager) context.getSystemService(context.DOWNLOAD_SERVICE);
        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        //Setting title of request
        request.setTitle("Data Download");

        //Setting description of request
        request.setDescription("Android Data download using DownloadManager.");

        request.setDestinationInExternalFilesDir(context, Environment.DIRECTORY_DOWNLOADS, getNameFile(uri.getPath()));

        //Enqueue download and save into referenceId
        downloadReference = downloadManager.enqueue(request);

        return downloadReference;
    }

    public static void CopiarArchivos(String sourceFile, String destinationFile) {

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

    public static boolean escribirArchivo(byte[] fileBytes, String archivoDestino) {
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


    public static long DownloadData1(String path, Context context) {

        Uri downloadUri = Uri.parse(path);
        DownloadManager.Request request = new DownloadManager.Request(downloadUri);
        request.setDescription("Downloading a file");
        long id = downloadManager.enqueue(request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE)
                .setAllowedOverRoaming(false)
                .setTitle("File Downloading...")
                .setDescription("Image File Download")
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "cm.png"));
        return id;
    }

    private static String DownloadStatus(Cursor cursor, long DownloadId) {

        //column for download  status
        int columnIndex = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS);
        int status = cursor.getInt(columnIndex);
        //column for reason code if the download failed or paused
        int columnReason = cursor.getColumnIndex(DownloadManager.COLUMN_REASON);
        int reason = cursor.getInt(columnReason);
        //get the download filename
        int filenameIndex = cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_FILENAME);
        String filename = cursor.getString(filenameIndex);

        String statusText = "";
        String reasonText = "";

        switch (status) {
            case DownloadManager.STATUS_FAILED:
                statusText = "STATUS_FAILED";
                switch (reason) {
                    case DownloadManager.ERROR_CANNOT_RESUME:
                        reasonText = "ERROR_CANNOT_RESUME";
                        break;
                    case DownloadManager.ERROR_DEVICE_NOT_FOUND:
                        reasonText = "ERROR_DEVICE_NOT_FOUND";
                        break;
                    case DownloadManager.ERROR_FILE_ALREADY_EXISTS:
                        reasonText = "ERROR_FILE_ALREADY_EXISTS";
                        break;
                    case DownloadManager.ERROR_FILE_ERROR:
                        reasonText = "ERROR_FILE_ERROR";
                        break;
                    case DownloadManager.ERROR_HTTP_DATA_ERROR:
                        reasonText = "ERROR_HTTP_DATA_ERROR";
                        break;
                    case DownloadManager.ERROR_INSUFFICIENT_SPACE:
                        reasonText = "ERROR_INSUFFICIENT_SPACE";
                        break;
                    case DownloadManager.ERROR_TOO_MANY_REDIRECTS:
                        reasonText = "ERROR_TOO_MANY_REDIRECTS";
                        break;
                    case DownloadManager.ERROR_UNHANDLED_HTTP_CODE:
                        reasonText = "ERROR_UNHANDLED_HTTP_CODE";
                        break;
                    case DownloadManager.ERROR_UNKNOWN:
                        reasonText = "ERROR_UNKNOWN";
                        break;
                }
                break;
            case DownloadManager.STATUS_PAUSED:
                statusText = "STATUS_PAUSED";
                switch (reason) {
                    case DownloadManager.PAUSED_QUEUED_FOR_WIFI:
                        reasonText = "PAUSED_QUEUED_FOR_WIFI";
                        break;
                    case DownloadManager.PAUSED_UNKNOWN:
                        reasonText = "PAUSED_UNKNOWN";
                        break;
                    case DownloadManager.PAUSED_WAITING_FOR_NETWORK:
                        reasonText = "PAUSED_WAITING_FOR_NETWORK";
                        break;
                    case DownloadManager.PAUSED_WAITING_TO_RETRY:
                        reasonText = "PAUSED_WAITING_TO_RETRY";
                        break;
                }
                break;
            case DownloadManager.STATUS_PENDING:
                statusText = "STATUS_PENDING";
                break;
            case DownloadManager.STATUS_RUNNING:
                statusText = "STATUS_RUNNING";
                break;
            case DownloadManager.STATUS_SUCCESSFUL:
                statusText = "STATUS_SUCCESSFUL";
                reasonText = "Filename:\n" + filename;
                break;
        }

        return statusText;

    }

    public static void Check_Image_Status(long Image_DownloadId) {

        DownloadManager.Query ImageDownloadQuery = new DownloadManager.Query();
        //set the query filter to our previously Enqueued download
        ImageDownloadQuery.setFilterById(Image_DownloadId);

        //Query the download manager about downloads that have been requested.
        Cursor cursor = downloadManager.query(ImageDownloadQuery);
        if (cursor.moveToFirst()) {
            DownloadStatus(cursor, Image_DownloadId);
        }

    }

    public static String getNameFile(String path) {
        int startPosition = path.lastIndexOf("/") + 1;
        return path.substring(startPosition);
    }

    public static void deleteFile(String path) {
        File file = new File(path);
        file.delete();

    }


    public static void deleteArchivo(String filename, Context context) {
        context.deleteFile(filename);
    }


    public static void asyncDownloadSaveImageFromUrl(String imageUrl, Context context, String nameImage) {

        new DownloadImage(nameImage, context).execute(imageUrl);
    }



    public static void loadImageFromDiskbase64(ImageView v, Context context, String base64) {
        byte[] decodedString = Base64.decode(base64, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        v.setImageBitmap(decodedByte);
    }

    public void deleteImageFromDisk(Context context, String nameImage) {
        File file = context.getFileStreamPath(nameImage);
        if (file.exists()) {
            file.delete();
        }
    }

    public static boolean checkIfFileExist(Context context, String nameImage) {
        File file = context.getFileStreamPath(nameImage);
        if (file.exists()) {
            return true;
        } else {
            return false;
        }
    }

    /**********************************************************************************************************
     * Download image and save it to disk
     * <String, Void, Bitmap> String parameter, Void for progress, Bitmap for return
     **********************************************************************************************************/
    public static class DownloadImage extends AsyncTask<String, Void, Bitmap> {
        private String nameImage;
        private Context context;
        private String TAG = "DownloadImage";
        ProgressDialog progressDialog;


        public DownloadImage(String nameImage, Context context) {
            this.nameImage = nameImage;
            this.context = context;
            progressDialog = new ProgressDialog(context);
            progressDialog.setCancelable(false);
            progressDialog.setMessage("Por favor espere ...");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        }

        private Bitmap downloadImageBitmap(String sUrl) {
            Bitmap bitmap = null;
            try {
                InputStream inputStream = new URL(sUrl).openStream();   // Download Image from URL
                bitmap = BitmapFactory.decodeStream(inputStream);       // Decode Bitmap
                inputStream.close();
            } catch (Exception e) {
                Log.d(TAG, "Exception 1, Something went wrong!");
                e.printStackTrace();
            }
            Log.d("saveImage", "Exception 2, Something went wrong!");
            return bitmap;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Bitmap doInBackground(String... params) {
            return downloadImageBitmap(params[0]);
        }

        protected void onPostExecute(Bitmap result) {
            saveImage(context, result, nameImage);
        }
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





}