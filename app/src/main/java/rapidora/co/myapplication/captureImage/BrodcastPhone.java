package rapidora.co.myapplication.captureImage;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by Administrator on 2/5/2018.
 */

public class BrodcastPhone extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        context.startService(new Intent(context, DemoCamService.class));

    }
}
