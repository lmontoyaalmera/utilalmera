package com.almera.utilalmeralib.libnetworkutil;


import android.app.Activity;
import android.content.Intent;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;


import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LibPeticionesUtil {


    /**
     * @param url
     * @return
     * @throws
     */
    public static LibRestClient createClienteRX(String url, Activity activity) {


        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.connectTimeout(120, TimeUnit.SECONDS);
        httpClient.readTimeout(120, TimeUnit.SECONDS);
        httpClient.writeTimeout(120, TimeUnit.SECONDS);

        Gson gson = new GsonBuilder()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(httpClient.build())
                .build();

        LibRestClient restClient = retrofit.create(LibRestClient.class);

        return restClient;
    }
}
