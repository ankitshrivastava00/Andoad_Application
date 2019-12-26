package rapidora.co.myapplication.receiver;

import android.app.AlarmManager;
import android.app.KeyguardManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.PowerManager;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.widget.Toast;

import rapidora.co.myapplication.common.CustomLogger;
import rapidora.co.myapplication.common.SessionManagment;
import rapidora.co.myapplication.services.CallLogService;
import rapidora.co.myapplication.services.ContactService;

import static android.content.Context.ALARM_SERVICE;
import static android.support.v4.content.WakefulBroadcastReceiver.startWakefulService;

/**
 * Created by PnP on 14-09-2017.
 */

public class ContactReciever extends BroadcastReceiver {

    public static long INTERVAL = 2 * 60 * 1000;
    SessionManagment sd;
    PowerManager.WakeLock screenLock;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onReceive(Context context, Intent intent) {


        sd = new SessionManagment(context);
      //  if (sd.getIsPwoerManager()) {
          /*  screenLock = ((PowerManager) context.getSystemService(Context.POWER_SERVICE)).newWakeLock(
                    PowerManager.PARTIAL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "TAG");
            screenLock.acquire();*/
      //  }
        try {
            String service = intent.getStringExtra("service");
            Log.e("service reciever", service);
            CustomLogger.getInsatance(context).putLog("Reciever:-" + service);
            switch (service) {

                case "ContactService": {
                 /*   Intent background = new Intent(context, ContactService.class);
                    context.startService(background);*/



                    if (Build.VERSION.SDK_INT >= 23) {
                        Intent serviceIntent = new Intent(context, ContactService.class);
                        Intent receiverIntent = new Intent(context, ContactReciever.class);

                        PendingIntent alarmIntent = PendingIntent.getBroadcast(context, 11, receiverIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                        AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
                        alarmManager.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,System.currentTimeMillis(),alarmIntent);
                        startWakefulService(context, serviceIntent);
                    } else {
                        Intent i = new Intent(context ,ContactService.class);
                        context.startService(i);
                    }
                    break;
                }

            }

            //wake up release
          //  if (sd.getIsPwoerManager()) {
           /* if (screenLock.isHeld())
                screenLock.release();*/
           // }
        } catch (Exception e) {
         //   if (sd.getIsPwoerManager()) {
           /* if (screenLock.isHeld())
                screenLock.release();*/
         //   }
        }
    }
}
