package rapidora.co.myapplication.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;


/**
 * Created by PnP on 14-09-2017.
 */

public class RecieverforAlwaysRunning extends BroadcastReceiver {

    public static long INTERVAL = 2 * 60 * 1000;

    PowerManager.WakeLock screenLock;

    @Override
    public void onReceive(Context context, Intent intent) {


           /* screenLock = ((PowerManager) context.getSystemService(Context.POWER_SERVICE)).newWakeLock(
                    PowerManager.PARTIAL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "TAG");
            screenLock.acquire();*/
       // }

        try {


        /*    KeyguardManager manager = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
            KeyguardManager.KeyguardLock lock = manager.newKeyguardLock("abc");
            lock.disableKeyguard();
*/

           // Intent background = new Intent(context, UpdateService.class);
          //  context.startService(background);
           // if (sd.getIsPwoerManager()) {
           /* if (screenLock.isHeld())
                screenLock.release();*/
            //}
        } catch (Exception e) {
           // if (sd.getIsPwoerManager()) {
           /* if (screenLock.isHeld())
                screenLock.release();*/
          //  }
        }
    }
}
