package com.almera.utilalmeralib.libnetworkutil;

import java.util.Map;

import io.reactivex.Single;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;

public interface LibRestClient {
    @GET("descargarArchivo")
    Single<ResponseBody> descargarArchivo(
            @HeaderMap Map<String, String> headers
    );
}
