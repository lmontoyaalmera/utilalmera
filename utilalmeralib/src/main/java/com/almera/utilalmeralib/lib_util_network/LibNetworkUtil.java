package com.almera.utilalmeralib.lib_util_network;

import android.annotation.SuppressLint;

import java.net.URL;
import java.net.URLConnection;

public class LibNetworkUtil {
    @SuppressLint("CheckResult")
    public static void isConnectedToServer(final String url, final int timeout, final LibUtilConnectionLisener connectionLisener) {
       new Thread(new Runnable() {
           @Override
           public void run() {
               try {
                   URL myUrl = new URL(url);
                   URLConnection connection = myUrl.openConnection();
                   connection.setConnectTimeout(timeout);
                   connection.connect();
                   connectionLisener.onRequestConnectionServer(true);
               } catch (Exception e) {
                   // Handle your exceptions
                   connectionLisener.onRequestConnectionServer(false);
               }
           }
       }).start();



    }
}
