package rapidora.co.myapplication.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import rapidora.co.myapplication.common.Utils;
import rapidora.co.myapplication.services.AlwaysRunning;


public class BootBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        Log.e("Booet", "Brodcast");

        Utils.cancelAlarmForSendUSerDetail(context);
        Utils.scheduleAlarmForSendUSerDetail(context);
        // Utils.scheduleAlarmForSendUSerDetail(getApplicationContext());
        Utils.scheduleAlarmLocation(context);


    }

}
