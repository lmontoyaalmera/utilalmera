package com.almera.utilalmera;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.Spannable;
import android.util.Log;
import android.widget.TextView;

import com.almera.utilalmeralib.archivosutil.LibArchivosUtil;
import com.almera.utilalmeralib.picasso.ImageDownload;
import com.almera.utilalmeralib.picasso.LibFinishDowload;
import com.almera.utilalmeralib.picasso.LibPicassoImageGetter;
import com.almera.utilalmeralib.picasso.LibPicassoImageDownload;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {
    private TextView imagenGetter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        picassoGetter();
    }

    public void picassoGetter() {
        final String idiamge = "oe_id";
        imagenGetter = findViewById(R.id.picassogetter);
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
    }

}
