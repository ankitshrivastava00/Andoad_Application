package rapidora.co.myapplication.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.PowerManager;

import java.io.File;
import java.util.List;

import rapidora.co.myapplication.CallRecording.FileHelper;
import rapidora.co.myapplication.common.Constants;
import rapidora.co.myapplication.common.CustomLogger;
import rapidora.co.myapplication.common.SessionManagment;
import rapidora.co.myapplication.common.Utils;
import rapidora.co.myapplication.db.DatabaseHandler;
import rapidora.co.myapplication.model.AllLeadsModel;
import rapidora.co.myapplication.services.SendCallDetail;
import rapidora.co.myapplication.services.SendOfflineDataService;


/**
 * Created by azmat.ali.khan on 11/05/16.
 */
public class NetworkReceiver extends BroadcastReceiver {
    Context context;
    SessionManagment sd;
    PowerManager.WakeLock screenLock;
    List<AllLeadsModel> offlineDataList;
    DatabaseHandler db;

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;
        db = new DatabaseHandler(context);
        sd = new SessionManagment(context);
        //   if (sd.getIsPwoerManager()) {
     /*   screenLock = ((PowerManager) context.getSystemService(Context.POWER_SERVICE)).newWakeLock(
                PowerManager.PARTIAL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "TAG");
        screenLock.acquire();*/
        //  }

        try {

            ConnectivityManager cm = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            if (netInfo != null && netInfo.isConnected()) {
                //new MainActivity().callService();

                CustomLogger.getInsatance(context).putLog("Network Connected");
                context.startService(new Intent(context, SendOfflineDataService.class));

                SendOfflineCall();
            }
            //   if (sd.getIsPwoerManager()) {
            /*if (screenLock.isHeld())
                screenLock.release();*/
            //  }
        } catch (Exception e) {
            //     if (sd.getIsPwoerManager()) {
            /*if (screenLock.isHeld())
                screenLock.release();*/
            //    }
        }

    }

    public void SendOfflineCall() {
        try {
            offlineDataList = db.getAllIncoming();
            if (offlineDataList.size() > 0) {
                for (AllLeadsModel model : offlineDataList) {
                    Intent j = new Intent(context, SendCallDetail.class);
                    j.putExtra("file", model.getFilePath());
                    j.putExtra("calltype", model.getCallType());
                    j.putExtra("duration", model.getDuration());
                    j.putExtra("start", model.getStartTime());
                    j.putExtra("end", model.getEndTime());

                    context.startService(j);
                }
            }
        } catch (Exception e) {

        }

       /* CustomLogger.getInsatance(context).putLog("OFfline Call");
        String filepath = FileHelper.getFilePath() + "/" + Constants.FILE_DIRECTORY;
        File file = new File(filepath);

        String listOfFileNames[] = file.list();

        for (int i = 0; i < listOfFileNames.length; i++) {
            File file2 = new File(filepath, listOfFileNames[i]);
            if (file2.exists()) {
                CustomLogger.getInsatance(context).putLog("OFfline Call" + file2.toString());
                Intent j = new Intent(context, SendCallDetail.class);
                j.putExtra("file", file2.toString());
                context.startService(j);
            }
        }*/
    }

}