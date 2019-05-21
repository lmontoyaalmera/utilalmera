package com.almera.utilalmeralib.libnetworkutil;

import android.app.Activity;

import java.util.HashMap;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

public class LibRxManager {
    private CompositeDisposable disposable = new CompositeDisposable();
    private Activity activity;
    LibRestClient restCliente;
    String uri;




    public LibRxManager(Activity activity,String uri) {
        this.activity = activity;
        this.uri=uri;
        this.restCliente = LibPeticionesUtil.createClienteRX(uri, activity);
    }


    public void descargarArchivo(String idArchivo, DisposableSingleObserver<ResponseBody> observer) {
        HashMap<String, String> headers = new HashMap<>();

        headers.put("archivoId", idArchivo);
        Disposable disposable_aux = (Disposable) restCliente.descargarArchivo(headers)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribeWith(observer);
        disposable.add(disposable_aux);
    }
}
