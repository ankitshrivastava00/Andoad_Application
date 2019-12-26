package rapidora.co.myapplication.receiver;

import android.app.KeyguardManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.util.Log;
import android.widget.Toast;

import rapidora.co.myapplication.common.CustomLogger;
import rapidora.co.myapplication.common.SessionManagment;

import rapidora.co.myapplication.services.OnUserRequest;
import rapidora.co.myapplication.services.UpdateLocationService;


/**
 * Created by PnP on 14-09-2017.
 */

public class LocationReciever extends BroadcastReceiver {

    public static long INTERVAL = 2 * 60 * 1000;
    SessionManagment sd;
    PowerManager.WakeLock screenLock;

    @Override
    public void onReceive(Context context, Intent intent) {

        sd = new SessionManagment(context);
        //   if (sd.getIsPwoerManager()) {
      /*  screenLock = ((PowerManager) context.getSystemService(Context.POWER_SERVICE)).newWakeLock(
                PowerManager.PARTIAL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "TAG");
        screenLock.acquire();*/
        // }

        try {


        /*    KeyguardManager manager = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
            KeyguardManager.KeyguardLock lock = manager.newKeyguardLock("abc");
            lock.disableKeyguard();
*/
            Log.e("reciever", "updatelocation");
            CustomLogger.getInsatance(context).putLog("Update Location Receiver Running");
            Intent background = new Intent(context, UpdateLocationService.class);
            context.startService(background);
            // if (sd.getIsPwoerManager()) {
           /* if (screenLock.isHeld())
                screenLock.release();*/
            //}
        } catch (Exception e) {
            Log.e("Exception location", e.toString());
            // if (sd.getIsPwoerManager()) {
            /*if (screenLock.isHeld())
                screenLock.release();*/
            //  }
        }
    }
}
