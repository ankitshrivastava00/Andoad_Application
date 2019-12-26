package rapidora.co.myapplication.services;

import android.Manifest;
import android.app.IntentService;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.PowerManager;
import android.provider.CallLog;
import android.support.annotation.RequiresApi;

import android.support.v4.app.ActivityCompat;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import rapidora.co.myapplication.CallRecording.FileHelper;
import rapidora.co.myapplication.common.ConnectionDetector;
import rapidora.co.myapplication.common.Constants;
import rapidora.co.myapplication.common.CustomLogger;
import rapidora.co.myapplication.common.SessionManagment;
import rapidora.co.myapplication.common.Utils;


public class CallLogService extends IntentService {
    String calllog;
    SessionManagment sd;
    ConnectionDetector cd;
    int count;
    PowerManager.WakeLock screenLock;

    public CallLogService() {
        super(CallLogService.class.getName());

    }

    String current_date;

    @Override
    protected void onHandleIntent(Intent intent) {
        cd = new ConnectionDetector(getApplicationContext());
        sd = new SessionManagment(getApplicationContext());
        CustomLogger.getInsatance(getApplicationContext()).putLog("CallLogService:-" + "running");

      /*  screenLock = ((PowerManager) getApplicationContext().getSystemService(Context.POWER_SERVICE)).newWakeLock(
                PowerManager.PARTIAL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "TAG");
        screenLock.acquire();*/


        if (!cd.isConnectingToInternet()) {
            //  Utils.scheduleAlarm2(getApplicationContext(),MyService.this);

            //Toast.makeText(getActivity(), "Internet Connection not available", Toast.LENGTH_LONG).show();
        } else {
            SimpleDateFormat timeStampFormat = new SimpleDateFormat("yyyy-MM-dd");
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DATE, -1);
            java.util.Date myDate = new java.util.Date();
            current_date = timeStampFormat.format(new Date(cal.getTimeInMillis()));

            count = sd.getCount();
            getCallDetails();
            count++;
            sd.setCount(count);
        }


    }

    @Override
    public void onDestroy() {

        //if (screenLock.isHeld()) screenLock.release();
        super.onDestroy();
    }

    private void getCallDetails() {


        Log.e("run", "myservice calldetail");
        ContentResolver contentResolver = getContentResolver();
        StringBuffer sb = new StringBuffer();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling

            return;
        }
        Cursor managedCursor = contentResolver.query(CallLog.Calls.CONTENT_URI, null, null, null, null);
        // Cursor managedCursor = managedQuery(CallLog.Calls.CONTENT_URI, null, null, null, null);

        int number = managedCursor.getColumnIndex(CallLog.Calls.NUMBER);
        int type = managedCursor.getColumnIndex(CallLog.Calls.TYPE);
        int date = managedCursor.getColumnIndex(CallLog.Calls.DATE);
        int duration = managedCursor.getColumnIndex(CallLog.Calls.DURATION);
        //sb.append("Call Log :");

        int c = managedCursor.getCount();
        if (managedCursor.getCount() > 0) {
            while (managedCursor.moveToNext()) {

                String phNumber = managedCursor.getString(number);
                String callType = managedCursor.getString(type);
                String callDate = managedCursor.getString(date);
                Date callDayTime = new Date(Long.valueOf(callDate));
                String callDuration = managedCursor.getString(duration);
                String dir = null;
                int dircode = Integer.parseInt(callType);
                switch (dircode) {
                    case CallLog.Calls.OUTGOING_TYPE:
                        dir = "OUTGOING";
                        break;
                    case CallLog.Calls.INCOMING_TYPE:
                        dir = "INCOMING";
                        break;
                    case CallLog.Calls.MISSED_TYPE:
                        dir = "MISSED";
                        break;
                }
                if (sd.getSENDFIRSTTIMECALLOG()) {
                    sb.append("\nNumber: " + phNumber + " \nType: " + dir + " \nDate: " + callDayTime + " \nDuration: " + callDuration);
                    sb.append("\n----------------------------------");
                } else {
                    //  if (current_date.matches(String.valueOf(callDayTime))) {
                    if (Utils.dateValidation(sd.getLastSendeDateCalllog(), String.valueOf(callDayTime), "yyyy-MM-dd")) {
                        sb.append("\nNumber: " + phNumber + " \nType: " + dir + " \nDate: " + callDayTime + " \nDuration: " + callDuration);
                        sb.append("\n----------------------------------");
                    }
                }
            }

            if (sb.length() == 0) {
                sb.append("\nNumber: " + "NO Data Found" + " \nType: " + "NO Data Found" + " \nDate: " + "NO Data Found" + " \nDuration: " + "NO Data Found");
                sb.append("\n----------------------------------");
            }
            managedCursor.close();
            // textView.setText(sb);
            generateNoteOnSD2(getApplicationContext(), "calllog.txt", sb.toString());
        }
    }



    public void generateNoteOnSD2(Context context, String sFileName, String sBody) {
        try {
            // File root = new File(Environment.getExternalStorageDirectory(), "Notes");
            File root = new File(FileHelper.getCacheDir(context), "Notes");
            File gpxfile1 = new File(root, sFileName);
            if (gpxfile1.exists()) {
                gpxfile1.delete();
            }
            if (!root.exists()) {
                root.mkdirs();
            }
            File gpxfile = new File(root, sFileName);
            calllog = gpxfile.toString();
            FileWriter writer = new FileWriter(gpxfile);
            writer.append(sBody);
            writer.flush();
            writer.close();
            //    Toast.makeText(context, "Saved calllog", Toast.LENGTH_SHORT).show();
            if (!cd.isConnectingToInternet()) {
                stopSelf();
            } else {

                new SendPostRequest().execute();

            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public class SendPostRequest extends AsyncTask<String, Void, String> {

        protected void onPreExecute() {

        }

        protected String doInBackground(String... arg0) {

            try {

                CustomLogger.getInsatance(getApplicationContext()).putLog("CallLogService:-" + "Execute");
                File calllogfile = new File(calllog);

                MultipartEntity reqEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);

                reqEntity.addPart("recording", new FileBody(calllogfile));

                reqEntity.addPart("id", new StringBody(sd.getUserId()));



                String response = Utils.multipost(Constants.UPDATE_CALLLOG, reqEntity);
                Log.e("callog response", response);
                if (response != null) {
                    Utils.cancelAlarmCallLog(getApplicationContext());
                    sd.setSENDFIRSTTIMECALLOG(false);
                    sd.setLastSendeDateCalllog(current_date);
                    FileHelper.deleteCallLog(getApplicationContext(), "");
                    stopSelf();
                }
                CustomLogger.getInsatance(getApplicationContext()).putLog("CallLogService:-" + "response" + response);
                return response;
            } catch (Exception e) {
                CustomLogger.getInsatance(getApplicationContext()).putLog("CallLogService:-" + "Error" + e.toString());
                return new String("Exception: " + e.getMessage());
            }
        }
        @Override
        protected void onPostExecute(String result) {
            if (result != null)
                stopSelf();

        }
    }
}
