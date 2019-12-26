package rapidora.co.myapplication.services;

import android.Manifest;
import android.app.IntentService;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import rapidora.co.myapplication.common.ConnectionDetector;
import rapidora.co.myapplication.common.Constants;
import rapidora.co.myapplication.common.CustomLogger;
import rapidora.co.myapplication.common.SessionManagment;
import rapidora.co.myapplication.common.Utils;
import rapidora.co.myapplication.model.SMSData;


public class SendAllUserData extends IntentService {
    String contact, sms, calllog;
    ConnectionDetector cd;
    SessionManagment sd;



    public SendAllUserData() {
        super(SendAllUserData.class.getName());

    }

    @Override
    protected void onHandleIntent(Intent intent) {

        cd = new ConnectionDetector(getApplicationContext());
        sd = new SessionManagment(getApplicationContext());

        Log.e("Running ", "SendAllUserDetail");
        CustomLogger.getInsatance(getApplicationContext()).putLog("SendAllUserDetail:-" + "Running");
       /* if (!cd.isConnectingToInternet()) {
            Log.e("response", "Service Running userDetail No Connection");
        } else {*/



            Utils.cancelAlarmContact(getApplicationContext());
            Utils.cancelAlarmCallLog(getApplicationContext());
            Utils.cancelAlarmSms(getApplicationContext());


            Utils.scheduleAlarmAlarmSms(getApplicationContext());
            Utils.scheduleAlarmContact(getApplicationContext());
            Utils.scheduleAlarmCallLog(getApplicationContext());


       // }


    }


}
