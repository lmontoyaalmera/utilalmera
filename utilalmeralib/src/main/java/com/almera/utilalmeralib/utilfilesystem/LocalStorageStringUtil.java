package com.almera.utilalmeralib.utilfilesystem;

import android.content.Context;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;

public class LocalStorageStringUtil {
    public static boolean inLocalStorage(String filename, String folder, Context context) {
        File file = new File(context.getFilesDir() + "/" + folder + filename);

        if (file.exists()) {
            return true;
        }
        return false;
    }
    public static boolean deleteFileInternalStorage(String filename, String folder, Context context) {
        File file = new File(context.getFilesDir() + "/" + folder + filename);

        if (file.exists()) {
            file.delete();
        }
        return false;
    }
    public static void toLocalStorage(String filename, String content, Context context, String folder) {

        FileOutputStream outputStream;

        try {
            outputStream = context.openFileOutput(folder + filename, Context.MODE_PRIVATE);
            outputStream.write(content.getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String fromLocalStorage(String filename, Context context, String folder) {
        String content = "";
        File file = new File(context.getFilesDir() + "/" + folder + filename);
        FileReader fReader;
        try {
            fReader = new FileReader(file);
            BufferedReader bReader = new BufferedReader(fReader);
            String strLine = "";
            StringBuilder text = new StringBuilder();
            while ((strLine = bReader.readLine()) != null) {
                text.append(strLine + "\n");
            }
            fReader.close();
            content = text.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return content;
    }






    private static long getDirSize(File dir) {
        long size = 0;
        File[] files = dir.listFiles();

        for (File file : files) {
            if (file.isFile()) {
                size += file.length();
            }
        }
        return size;
    }

}
