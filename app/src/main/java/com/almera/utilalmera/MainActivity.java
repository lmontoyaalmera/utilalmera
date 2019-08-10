package com.almera.utilalmera;

import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.Spannable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.almera.utilalmeralib.archivosutil.LibArchivosUtil;
import com.almera.utilalmeralib.dialogrecoredutil.RecordDialog;
import com.almera.utilalmeralib.dialogrecoredutil.RecordLisener;
import com.almera.utilalmeralib.picasso.ImageDownload;
import com.almera.utilalmeralib.picasso.LibFinishDowload;
import com.almera.utilalmeralib.picasso.LibPicassoImageGetter;
import com.almera.utilalmeralib.picasso.LibPicassoImageDownload;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {
    private TextView imagenGetter;
    private Button recored;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        picassoGetter();
    }

    public void picassoGetter() {
        final String idiamge = "oe_id";
        imagenGetter = findViewById(R.id.picassogetter);
        recored = findViewById(R.id.recored);
        LibPicassoImageGetter imageGetter = new LibPicassoImageGetter(imagenGetter, MainActivity.this, idiamge);
        //String html1="<p> </p>\r\n\r\n<h1>Hola mundo<\/h1>\r\n\r\n<p><img alt=\"\" src=\"http:\/\/192.168.1.28\/sgi\/tmp\/uploads\/almera_pruebasLOCAL\/download.jpeg\" style=\"width: 284px; height: 177px;\" \/><\/p>\r\n";
        final String html1 = "<p> </p>\r\n\r\n<h1>Picasso Campos Etiqueta<\\/h1>\r\n\r\n<p><img alt=\"\" src=\"http://192.168.1.193/sgi/tmp/uploads/almera_pruebasLOCAL/download.jpeg\" style=\"width: 284px; height: 177px;\" /></p>\r\n";
       /* final ImageDownload imageDownload=new ImageDownload(html1,idiamge);
        LibArchivosUtil.DonwloadPicturesFromHtml(getApplicationContext(), new ArrayList<ImageDownload>(Arrays.asList(imageDownload)), new LibFinishDowload() {
            @Override
            public void onFinish(Boolean status) {
                Log.d("a", "onFinish: ");
            }
        });*/
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
                                File.separator + "/utilalmera/audios"+ File.separator + nombreAudio;
                        LibArchivosUtil.copyFile(url, path_audio);
                    }
                });
                recordDialog.createLoginDialogo().show();
            }
        });
    }

}
