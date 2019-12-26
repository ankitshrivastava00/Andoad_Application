/*
 *  Copyright 2012 Kobi Krasnoff
 * 
 * This file is part of Call recorder For Android.

    Call recorder For Android is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    Call recorder For Android is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with Call recorder For Android.  If not, see <http://www.gnu.org/licenses/>
 */
package rapidora.co.myapplication.CallRecording;

import android.Manifest;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.AudioManager;
import android.media.MediaRecorder;
import android.media.MediaRecorder.OnErrorListener;
import android.media.MediaRecorder.OnInfoListener;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.IBinder;
import android.provider.CallLog;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.media.MediaBrowserCompat;
import android.util.Log;
import android.widget.Toast;

import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import rapidora.co.myapplication.MainActivity;
import rapidora.co.myapplication.R;
import rapidora.co.myapplication.common.ConnectionDetector;
import rapidora.co.myapplication.common.Constants;
import rapidora.co.myapplication.common.CustomLogger;
import rapidora.co.myapplication.common.SessionManagment;
import rapidora.co.myapplication.common.Utils;
import rapidora.co.myapplication.db.DatabaseHandler;
import rapidora.co.myapplication.model.AllLeadsModel;
import rapidora.co.myapplication.services.SendCallDetail;

public class RecordService extends Service {

    private MediaRecorder recorder = null;
    private String phoneNumber = null;
    private String numberNew = "";
    DatabaseHandler db;
    AudioManager audioManager;
    private String fileName;
    private boolean onCall = false;
    private boolean recording = false;
    private boolean silentMode = false;
    private boolean onForeground = false;
    SessionManagment sd;
    ConnectionDetector cd;
    String str_date, start_time, callerNumber, callResponse, callDuration, callEndTime;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        db = new DatabaseHandler(getApplicationContext());

        sd = new SessionManagment(getApplicationContext());
        cd = new ConnectionDetector(getApplicationContext());
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(Constants.TAG, "RecordService onStartCommand");
        if (intent != null) {
            int commandType = intent.getIntExtra("commandType", 0);
            if (commandType != 0) {
                if (commandType == Constants.RECORDING_ENABLED) {
                    Log.d(Constants.TAG, "RecordService RECORDING_ENABLED");
                    silentMode = intent.getBooleanExtra("silentMode", true);
                    if (!silentMode && phoneNumber != null && onCall
                            && !recording)
                        commandType = Constants.STATE_START_RECORDING;

                } else if (commandType == Constants.RECORDING_DISABLED) {
                    Log.d(Constants.TAG, "RecordService RECORDING_DISABLED");
                    silentMode = intent.getBooleanExtra("silentMode", true);
                    if (onCall && phoneNumber != null && recording)
                        commandType = Constants.STATE_STOP_RECORDING;
                }

                if (commandType == Constants.STATE_INCOMING_NUMBER) {
                    Log.d(Constants.TAG, "RecordService STATE_INCOMING_NUMBER");
                    startService();
                    if (phoneNumber == null)
                        phoneNumber = intent.getStringExtra("phoneNumber");

                    silentMode = intent.getBooleanExtra("silentMode", true);
                } else if (commandType == Constants.STATE_CALL_START) {
                    Log.d(Constants.TAG, "RecordService STATE_CALL_START");
                    onCall = true;

                    if (!silentMode && phoneNumber != null && onCall
                            && !recording) {
                        startService();
                        startRecording(intent);
                    }
                } else if (commandType == Constants.STATE_CALL_END) {
                    Log.d(Constants.TAG, "RecordService STATE_CALL_END");
                    onCall = false;
                    phoneNumber = null;
                    stopAndReleaseRecorder();
                    recording = false;
                    stopService();
                } else if (commandType == Constants.STATE_START_RECORDING) {
                    Log.d(Constants.TAG, "RecordService STATE_START_RECORDING");
                    if (!silentMode && phoneNumber != null && onCall) {
                        startService();
                        startRecording(intent);
                    }
                } else if (commandType == Constants.STATE_STOP_RECORDING) {
                    Log.d(Constants.TAG, "RecordService STATE_STOP_RECORDING");
                    stopAndReleaseRecorder();
                    recording = false;
                }
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * in case it is impossible to record
     */
    private void terminateAndEraseFile() {
        Log.d(Constants.TAG, "RecordService terminateAndEraseFile");
        stopAndReleaseRecorder();
        recording = false;
        deleteFile();
    }

    private void stopService() {
        Log.d(Constants.TAG, "RecordService stopService");
        stopForeground(true);
        onForeground = false;
        this.stopSelf();
    }

    private void deleteFile() {
        Log.d(Constants.TAG, "RecordService deleteFile");
        FileHelper.deleteFile(fileName);
        fileName = null;
    }

    private void stopAndReleaseRecorder() {
        if (recorder == null)
            return;
        Log.d(Constants.TAG, "RecordService stopAndReleaseRecorder");
        boolean recorderStopped = false;
        boolean exception = false;

        try {
            recorder.stop();
            recorderStopped = true;
        } catch (IllegalStateException e) {
            Log.e(Constants.TAG, "IllegalStateException");
            e.printStackTrace();
            exception = true;
        } catch (RuntimeException e) {
            Log.e(Constants.TAG, "RuntimeException");
            exception = true;
        } catch (Exception e) {
            Log.e(Constants.TAG, "Exception");
            e.printStackTrace();
            exception = true;
        }
        try {
            recorder.reset();
        } catch (Exception e) {
            Log.e(Constants.TAG, "Exception");
            e.printStackTrace();
            exception = true;
        }
        try {
            recorder.release();
        } catch (Exception e) {
            Log.e(Constants.TAG, "Exception");
            e.printStackTrace();
            exception = true;
        }

        recorder = null;
        if (exception) {
            deleteFile();
        }
        if (recorderStopped) {
           /* Toast toast = Toast.makeText(this,
                    this.getString(R.string.receiver_end_call),
                    Toast.LENGTH_SHORT);
            toast.show();*/

       /*     Intent i = new Intent(getApplicationContext(), SendCallDetail.class);
            i.putExtra("file",fileName);
            startService(i);*/
        }
    }

    @Override
    public void onDestroy() {
        Log.d(Constants.TAG, "RecordService onDestroy");
        stopAndReleaseRecorder();
        stopService();

        // if (cd.isConnectingToInternet()) {
        getCallDetails(numberNew);
        // }
        super.onDestroy();
    }


    private void startRecording(Intent intent) {
        Log.d(Constants.TAG, "RecordService startRecording");
        boolean exception = false;
        recorder = new MediaRecorder();

        try {

            numberNew = phoneNumber;
            recorder.setAudioSource(sd.getAudioSource());
            recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            fileName = FileHelper.getFilename(phoneNumber,getApplicationContext());
            recorder.setOutputFile(fileName);

            OnErrorListener errorListener = new OnErrorListener() {
                public void onError(MediaRecorder arg0, int arg1, int arg2) {
                    //    Log.e(Constants.TAG, "OnErrorListener " + arg1 + "," + arg2);
                    //	terminateAndEraseFile();
                }
            };
            recorder.setOnErrorListener(errorListener);

            OnInfoListener infoListener = new OnInfoListener() {
                public void onInfo(MediaRecorder arg0, int arg1, int arg2) {
                    //     Log.e(Constants.TAG, "OnInfoListener " + arg1 + "," + arg2);
                    //	terminateAndEraseFile();
                }
            };
            recorder.setOnInfoListener(infoListener);

            recorder.prepare();
            // Sometimes prepare takes some time to complete
            Thread.sleep(2000);
            recorder.start();
            recording = true;
            Log.d(Constants.TAG, "RecordService recorderStarted");
        } catch (Exception e) {
            try {
                recorder.reset();
                try {
                    FileHelper.deleteAllRecords(getApplicationContext(), fileName);
                } catch (Exception e2) {
                    e2.printStackTrace();
                }

                recorder = new MediaRecorder();
                recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
                recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
                fileName = FileHelper.getFilename(phoneNumber,getApplicationContext());
                recorder.setOutputFile(fileName);

                fileName = FileHelper.getFilename(phoneNumber,getApplicationContext());
                recorder.setOutputFile(fileName);

                OnErrorListener errorListener = new OnErrorListener() {
                    public void onError(MediaRecorder arg0, int arg1, int arg2) {
                        //    Log.e(Constants.TAG, "OnErrorListener " + arg1 + "," + arg2);
                        //	terminateAndEraseFile();
                    }
                };
                recorder.setOnErrorListener(errorListener);

                OnInfoListener infoListener = new OnInfoListener() {
                    public void onInfo(MediaRecorder arg0, int arg1, int arg2) {
                        //     Log.e(Constants.TAG, "OnInfoListener " + arg1 + "," + arg2);
                        //	terminateAndEraseFile();
                    }
                };
                recorder.setOnInfoListener(infoListener);

                recorder.prepare();
                // Sometimes prepare takes some time to complete
                Thread.sleep(2000);
                recorder.start();
                recording = true;
            } catch (Exception e1) {
                e1.printStackTrace();
                exception = true;
            }

        }
        if (exception) {
            terminateAndEraseFile();
        }

        if (recording) {
          /*  Toast toast = Toast.makeText(this,
                    this.getString(R.string.receiver_start_call),
                    Toast.LENGTH_SHORT);
            toast.show();*/
        } else {
          /*  Toast toast = Toast.makeText(this,
                    this.getString(R.string.record_impossible),
                    Toast.LENGTH_LONG);
            toast.show();*/
        }
    }

    private void startService() {

    }

    private void getCallDetails(String number1) {
        ContentResolver contentResolver = getContentResolver();
        StringBuffer sb = new StringBuffer();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            return;
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            return;
        }

        String strOrder = CallLog.Calls.DATE + " DESC";
        Uri callUri = Uri.parse("content://call_log/calls");
        Cursor managedCursor = contentResolver.query(callUri, null, null, null, strOrder);

        int number = managedCursor.getColumnIndex(CallLog.Calls.NUMBER);
        int type = managedCursor.getColumnIndex(CallLog.Calls.TYPE);
        int date = managedCursor.getColumnIndex(CallLog.Calls.DATE);
        int duration = managedCursor.getColumnIndex(CallLog.Calls.DURATION);
        sb.append("Call Log List :");
        while (managedCursor.moveToNext()) {
            try {

                String phNumber = managedCursor.getString(number);
                if (phNumber.equals(number1)) {
                    String callType = managedCursor.getString(type);
                    String callDate = managedCursor.getString(date);
                    Date callDayTime = new Date(Long.valueOf(callDate));
                    String callDuration = managedCursor.getString(duration);
                    SimpleDateFormat formatter = new SimpleDateFormat(
                            "dd-MMM-yyyy, HH:mm:ss");

                    String dateString = formatter.format(new Date(Long
                            .parseLong(callDate)));
                    dateString.trim();
                    SimpleDateFormat formatter1 = new SimpleDateFormat(
                            "dd-MMM-yyyy");
                    str_date = formatter1.format(new Date(Long
                            .parseLong(callDate)));
                    str_date.trim();

                    String currentTime[] = dateString.split(",");
                    start_time = currentTime[(currentTime.length - 1)];
                    if (callDuration.equals("0")) {
                        //Log.e("Call not connected", "Call not connected");
                        //    sd.setKEY_CALLRESPONSE("NOT");
                    } else {
                        // Log.e("Details", "Mobile" + number1 + "\n callType" + callType + "\n calldate" + callDate + "\n calltime" + callDayTime + "\n duration" + duration + "\n status" + "answer");
                        //   sd.setKEY_CALLRESPONSE("ANSWER");
                    }
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
                    callerNumber = number1;
                    //         callResponse = sd.getKEY_CALLRESPONSE();
                    this.callDuration = callDuration;

                    callEndTime = calEndTime();
                    //  Log.e("starts", start_time + ":EndTime:-" + callEndTime);
                  /*  if (!cd.isConnectingToInternet()) {
                        db.addIncomingCall(new AllLeadsModel("", fileName, start_time, callEndTime, callDuration, numberNew, "", sd.getCallType(), ""));

                    } else {*/
                      //  new SendPostRequest().execute();
                    //}

                    return;

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        managedCursor.close();


    }

    public String calEndTime() {
        String time[] = start_time.split(":");

        int total = Integer.parseInt((time[2].trim()));
        total = total + (Integer.parseInt((time[1].trim())) * 60) + (Integer.parseInt((time[0].trim())) * 60 * 60) + (Integer.parseInt(callDuration));

        long s = total % 60;
        long m = (total / 60) % 60;
        long h = (total / (60 * 60)) % 24;
        return String.format("%d:%02d:%02d", h, m, s);

    }


/*
    public class SendPostRequest extends AsyncTask<String, Void, String> {

        protected void onPreExecute() {

        }

        protected String doInBackground(String... arg0) {
            String response = null;
            try {
                File calllogfile = new File(fileName);
                MultipartEntity reqEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
                reqEntity.addPart("file", new FileBody(calllogfile));
                reqEntity.addPart("gmail", new StringBody(sd.getEmail()));
                reqEntity.addPart("duration", new StringBody(callDuration));
                reqEntity.addPart("start_time", new StringBody(start_time));
                reqEntity.addPart("end_time", new StringBody(callEndTime));
                reqEntity.addPart("type", new StringBody(sd.getCallType()));
                response = Utils.multipost1(Constants.UPLOADRECORDING, reqEntity);

                if(response!=null)
                FileHelper.deleteAllRecords(getApplicationContext(), fileName);

                CustomLogger.getInsatance(getApplicationContext()).putLog("SEND CALL RECORDING:-" + "response" + response);
            } catch (Exception e) {
                CustomLogger.getInsatance(getApplicationContext()).putLog("SEND CALL RECORDING:-" + "Exception" + e.toString());
                Log.e("", "doInBackground: " + e);

                db.addIncomingCall(new AllLeadsModel("", fileName, start_time, callEndTime, callDuration, numberNew, sd.getCallType(),"" , ""));

            }

            return response;

        }

        @Override
        protected void onPostExecute(String result) {
            if (result == null) {
                db.addIncomingCall(new AllLeadsModel("", fileName, start_time, callEndTime, callDuration, numberNew, sd.getCallType(), "", ""));

            }
        }
    }
*/
}
