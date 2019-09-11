package com.almera.utilalmeralib.dialogrecoredutil;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.SystemClock;
import androidx.appcompat.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.almera.utilalmeralib.R;
import com.skyfishjy.library.RippleBackground;

import java.io.IOException;

public class RecordDialog {
    private Activity activity;
    private RecordLisener recordLisener;
    private MediaRecorder mediaRecorder;
    private MediaPlayer mediaPlayer;
    private LinearLayout linearLayoutRecored;
    private LinearLayout linearLayoutQuestiom;
    private Chronometer chronometer;
    private String url_base;

    public RecordDialog(Activity activity, RecordLisener recordLisener) {

        this.activity = activity;
        this.recordLisener = recordLisener;
    }

    @SuppressLint("ClickableViewAccessibility")
    public AlertDialog createLoginDialogo() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(this.activity);
        LayoutInflater inflater = this.activity.getLayoutInflater();
        View v = inflater.inflate(R.layout.dialog_record, null);
        final TextView textView=v.findViewById(R.id.text_dialog_record);
        ImageButton imgbtnRecorder = v.findViewById(R.id.btnRecored);
        ImageButton imgbtnAceptar = v.findViewById(R.id.btnAceptarRecored);
        ImageButton imgbtnCancelar = v.findViewById(R.id.btnCancelarRecored);
        final RippleBackground rippleBackground=(RippleBackground)v.findViewById(R.id.content);
        chronometer = v.findViewById(R.id.chronometerRecored);
        ImageButton imgbtnPlay = v.findViewById(R.id.btnPlayerRecored);
        linearLayoutRecored = v.findViewById(R.id.layoutRecored);
        linearLayoutQuestiom = v.findViewById(R.id.layoutQuestion);
        imgbtnRecorder.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    mediaRecorder = new MediaRecorder();
                    mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                    mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
                    mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
                    chronometer.setBase(SystemClock.elapsedRealtime());
                    chronometer.start();
                    url_base = activity.getCacheDir() + "audioPrueba.ogg";
                    mediaRecorder.setOutputFile(url_base);
                    try {
                        mediaRecorder.prepare();
                        mediaRecorder.start();
                        rippleBackground.startRippleAnimation();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    try {
                        mediaRecorder.stop();
                        mediaRecorder.release();
                        mediaRecorder = null;
                        chronometer.stop();
                        linearLayoutRecored.setVisibility(View.GONE);
                        linearLayoutQuestiom.setVisibility(View.VISIBLE);
                        textView.setVisibility(View.GONE);
                    } catch (Exception e) {
                        rippleBackground.stopRippleAnimation();
                        chronometer.setBase(SystemClock.elapsedRealtime());
                        chronometer.stop();
                        e.printStackTrace();
                    }
                }
                return true;
            }
        });


        imgbtnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    mediaPlayer = new MediaPlayer();
                    mediaPlayer.setDataSource(url_base);
                    mediaPlayer.prepare();
                    mediaPlayer.start();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        builder.setView(v);
        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {

                if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                    mediaPlayer.stop();
                }

            }
        });
        final AlertDialog alertDialog = builder.create();
        imgbtnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.cancel();
            }
        });
        imgbtnAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recordLisener.onRecordAudio(url_base);
                alertDialog.cancel();
            }
        });
        return alertDialog;
    }

    public void setRecordLisener(RecordLisener recordLisener) {
        this.recordLisener = recordLisener;
    }
}

