package com.almera.utilalmeralib.lib_util_network;

import android.annotation.SuppressLint;
import android.os.AsyncTask;

import java.net.URL;
import java.net.URLConnection;

public class LibNetworkUtil {
    @SuppressLint("CheckResult")
    public static void isConnectedToServer(final String url, final int timeout,  LibUtilConnectionLisener connectionLisener) {
        TestConnection testConnection=new TestConnection(connectionLisener);
        testConnection.execute(url,timeout+"");


    }
    public static class TestConnection extends AsyncTask<String, Void,Boolean>{
        LibUtilConnectionLisener libUtilConnectionLisener;
        public TestConnection(LibUtilConnectionLisener libUtilConnectionLisener) {
            this.libUtilConnectionLisener=libUtilConnectionLisener;
        }

        @Override
        protected Boolean doInBackground(String... strings) {
            try{
                URL myUrl = new URL(strings[0]);
                URLConnection connection = myUrl.openConnection();
                connection.setConnectTimeout(Integer.parseInt(strings[1]));
                connection.connect();
                return true;
            }catch (Exception e){
                return false;
            }


        }

        @Override
        protected void onPostExecute(Boolean result) {
          libUtilConnectionLisener.onRequestConnectionServer(result);
        }
    }
}
