package com.almera.utilalmeralib.utilfilesystem;

import android.content.Context;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class LibCacheUtil {

    public static boolean inCache(String filename, String folder, Context context) {
        File file= new File(context.getCacheDir(), folder);
        File tempFile = new File(file.getPath() + "/" + filename);
        if (tempFile.exists()) {
            return true;
        }
        return false;
    }
    public static void toCache(String filename, String content, Context context, String folder) {
        File dir = context.getCacheDir();
        File fileAudios = new File(context.getCacheDir(), folder);
        boolean isCreada = fileAudios.exists();
        if (isCreada == false) {
            isCreada = fileAudios.mkdirs();
        }

        File tempFile = new File(fileAudios.getPath() + "/" + filename);
        FileWriter writer;
        try {
            if (tempFile.exists()) {
                tempFile.delete();
                tempFile.createNewFile();
            }
            writer = new FileWriter(tempFile);
            writer.write(content);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static String fromCache(String filename, Context context, String folder) {
        String content = "";
        File file= new File(context.getCacheDir(), folder);
        File tempFile = new File(file.getPath() + "/" + filename);
        FileReader fReader;
        try {
            fReader = new FileReader(tempFile);
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


    public static void cleanCacheCampos(String filepath , Context context) {

        File fileCache = new File(context.getCacheDir(), filepath);
        if (fileCache.exists()) {
           fileCache.delete();
        }
    }
    public static void cleanCacheDir(String url, Context context) {
        File fileCache = new File(context.getCacheDir(), url);
        if (fileCache.exists()) {
        File[] files = fileCache.listFiles();
        for (File file : files) {
            file.delete();
        }
    }


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
