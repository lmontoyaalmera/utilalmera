package com.almera.utilalmeralib.alertUtils;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Vibrator;

import com.almera.utilalmeralib.R;

public class AlertUtil {

public static void alertVibrate(Context context){

    Vibrator v = (Vibrator)context.getSystemService(Context.VIBRATOR_SERVICE);
    AudioManager au= (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
    v.vibrate(500);


}
public static void alertSound(Context context){
    MediaPlayer mp= MediaPlayer.create(context, R.raw.beep);
    mp.start();
}

}