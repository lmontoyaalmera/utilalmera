package com.almera.utilalmera;

import android.os.Bundle;
import android.os.Environment;
import android.text.Html;
import android.text.Spannable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.almera.utilalmeralib.archivosutil.LibArchivosUtil;
import com.almera.utilalmeralib.dialogrecoredutil.RecordDialog;
import com.almera.utilalmeralib.dialogrecoredutil.RecordLisener;
import com.almera.utilalmeralib.editTextUtil.LibTextWatcherNumericSeparator;
import com.almera.utilalmeralib.picasso.ImageDownload;
import com.almera.utilalmeralib.picasso.LibFinishDowload;
import com.almera.utilalmeralib.picasso.LibPicassoImageGetter;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;

public class MainActivity extends AppCompatActivity {
    private TextView imagenGetter;
    private Button recored;
    private EditText editTextPrueba;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //picassoGetter();
        bindEditText();


    }


    public void bindEditText() {
        editTextPrueba = findViewById(R.id.editTextprueba);

        LibTextWatcherNumericSeparator textWatcherNumericSeparator = new LibTextWatcherNumericSeparator(editTextPrueba,
                ',', '.', 0, "^-+?0123456789,.$", 4);
        textWatcherNumericSeparator.getObservableValueEditTextNumeric().subscribe(new SingleObserver<Double>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onSuccess(Double aDouble) {
                Log.d("123", "onSuccess: " + aDouble);

            }

            @Override
            public void onError(Throwable e) {
                Log.d("123", "onError: " + e.getMessage());
            }
        });
        editTextPrueba.setText(textWatcherNumericSeparator.doubleToFormat("120.32"));
        editTextPrueba.addTextChangedListener(textWatcherNumericSeparator);
    }

    public void picassoGetter() {
        final String idiamge = "oe_id";
        imagenGetter = findViewById(R.id.picassogetter);
        recored = findViewById(R.id.recored);
        LibPicassoImageGetter imageGetter = new LibPicassoImageGetter(imagenGetter, MainActivity.this, idiamge);
        //String html1="<p> </p>\r\n\r\n<h1>Hola mundo<\/h1>\r\n\r\n<p><img alt=\"\" src=\"http:\/\/192.168.1.28\/sgi\/tmp\/uploads\/almera_pruebasLOCAL\/download.jpeg\" style=\"width: 284px; height: 177px;\" \/><\/p>\r\n";
        final String html1 = "<p> </p>\r\n\r\n<h1>Picasso Campos Etiqueta<\\/h1>\r\n\r\n<p><img alt=\"\" src=\"http://192.168.1.153/sgi/tmp/multimedia/big_9501.svg\" style=\"width: 284px; height: 177px;\" /></p>\r\n";
        final ImageDownload imageDownload = new ImageDownload(html1, idiamge);
        LibArchivosUtil.DonwloadPicturesFromHtml(getApplicationContext(), new ArrayList<>(Arrays.asList(imageDownload)), new LibFinishDowload() {
            @Override
            public void onFinish(Boolean status) {
                Log.d("a", "onFinish: ");
            }
        });
        Spannable html;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            html = (Spannable) Html.fromHtml(html1, Html.FROM_HTML_MODE_LEGACY, imageGetter, null);
        } else {
            html = (Spannable) Html.fromHtml(html1, imageGetter, null);
        }
        imagenGetter.setText(html);
        recored.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RecordDialog recordDialog = new RecordDialog(MainActivity.this, new RecordLisener() {
                    @Override
                    public void onRecordAudio(String url) {
                        File fileAudios = new File(Environment.getExternalStorageDirectory(), "/utilalmera/audios");
                        boolean isCreada = fileAudios.exists();
                        String nombreAudio = "";
                        if (isCreada == false) {
                            isCreada = fileAudios.mkdirs();
                        }
                        if (isCreada == true) {
                            nombreAudio = (System.currentTimeMillis() / 1000) + ".ogg";
                        }
                        String path_audio = Environment.getExternalStorageDirectory() +
                                File.separator + "/utilalmera/audios" + File.separator + nombreAudio;
                        LibArchivosUtil.copyFile(url, path_audio);
                    }
                });
                recordDialog.createLoginDialogo().show();
            }
        });

       /* LibDialogLisener libDialogLisener= LibDialogUtil.showProgressDialog(this,"hoa");
        libDialogLisener.showDialog();
        libDialogLisener.changeMessage("asdasd");
        libDialogLisener.changeMessage("asdasd1");
        libDialogLisener.changeMessage("asdasd121");
        libDialogLisener.changeMessage("hola122");*/

    }

}
