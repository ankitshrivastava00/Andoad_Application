package rapidora.co.myapplication.services;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
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

import rapidora.co.myapplication.CallRecording.FileHelper;
import rapidora.co.myapplication.common.ConnectionDetector;
import rapidora.co.myapplication.common.Constants;
import rapidora.co.myapplication.common.CustomLogger;
import rapidora.co.myapplication.common.SessionManagment;
import rapidora.co.myapplication.common.Utils;
import rapidora.co.myapplication.db.DatabaseHandler;


public class SendCallDetail extends IntentService {
    String contact, sms, calllog;
    ConnectionDetector cd;
    SessionManagment sd;

    String current_date;
    Context context;
    File file;
    String filename, calltype, duration, start, end;
    String result = "";

    DatabaseHandler db;

    public SendCallDetail() {
        super(SendCallDetail.class.getName());

    }

    @Override
    protected void onHandleIntent(Intent intent) {
        db = new DatabaseHandler(getApplicationContext());
        cd = new ConnectionDetector(getApplicationContext());
        sd = new SessionManagment(getApplicationContext());
        filename = intent.getStringExtra("file");
        calltype = intent.getStringExtra("calltype");
        duration = intent.getStringExtra("duration");
        start = intent.getStringExtra("start");
        end = intent.getStringExtra("end");
        Log.e("Running ", "CALLRECORDING SEND");
        CustomLogger.getInsatance(getApplicationContext()).putLog("CALLRECORDING SEND:-" + "Running");
        if (!cd.isConnectingToInternet()) {
            Log.e("response", "Service Running userDetail No Connection");
        } else {

    /*        try {
                File calllogfile = new File(filename);
                MultipartEntity reqEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
                reqEntity.addPart("file", new FileBody(calllogfile));
                reqEntity.addPart("duration", new StringBody(duration));
                reqEntity.addPart("type", new StringBody(calltype));
                reqEntity.addPart("start_time", new StringBody(start));
                reqEntity.addPart("end_time", new StringBody(end));

                reqEntity.addPart("gmail", new StringBody(sd.getEmail()));
                String response = Utils.multipost1(Constants.UPLOADRECORDING, reqEntity);
                if (response != null) {
                    FileHelper.deleteAllRecords(getApplicationContext(), filename);
                    db.deleteDataFileRecord(filename);
                    stopSelf();
                }
                CustomLogger.getInsatance(getApplicationContext()).putLog("SEND CALL RECORDING:-" + "response" + response);
            } catch (Exception e) {
                CustomLogger.getInsatance(getApplicationContext()).putLog("SEND CALL RECORDING:-" + "Exception" + e.toString());
                result = e.toString();
                Log.e("", "doInBackground: " + e);
            }*/

        }


    }
}
