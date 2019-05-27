package com.almera.utilalmeralib.libnetworkutil;

import android.app.Activity;
import android.content.Context;

import java.util.HashMap;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

public class LibRxManager {
    private CompositeDisposable disposable = new CompositeDisposable();

    LibRestClient restCliente;
    String uri;




    public LibRxManager(String uri) {

        this.uri=uri;
        this.restCliente = LibPeticionesUtil.createClienteRX(uri);
    }


    public void descargarArchivo(String idArchivo,String conexion, DisposableSingleObserver<ResponseBody> observer) {
        HashMap<String, String> headers = new HashMap<>();

        headers.put("archivoId", idArchivo);
        headers.put("conexion", conexion);
        Disposable disposable_aux = (Disposable) restCliente.descargarArchivo(headers)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribeWith(observer);
        disposable.add(disposable_aux);
    }
}
