package rapidora.co.myapplication.services;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
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

import rapidora.co.myapplication.MainActivity;
import rapidora.co.myapplication.common.ConnectionDetector;
import rapidora.co.myapplication.common.Constants;
import rapidora.co.myapplication.common.CustomLogger;
import rapidora.co.myapplication.common.SessionManagment;
import rapidora.co.myapplication.common.Utils;


public class UserDetailService extends IntentService {
    ConnectionDetector cd;
    SessionManagment sd;

    public UserDetailService() {
        super(UserDetailService.class.getName());

    }

    @Override
    protected void onHandleIntent(Intent intent) {

        cd = new ConnectionDetector(getApplicationContext());
        sd = new SessionManagment(getApplicationContext());

        Log.e("Running ", "userDetail");
        CustomLogger.getInsatance(getApplicationContext()).putLog("userDetailService:-" + "Running");
        if (!cd.isConnectingToInternet()) {
            Log.e("response", "Service Running userDetail No Connection");
        } else {
            Log.e("response", "Service Running userDetail ");
            executeMethod();


        }


    }

    public void executeMethod() {
        try {
            new MyTask2().execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }


    public class MyTask2 extends AsyncTask<Void, Void, Void> {


        @Override
        protected Void doInBackground(Void... params) {

            StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.USERINSERT,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.e("response", response);

                            CustomLogger.getInsatance(getApplicationContext()).putLog("userDetailService:-" + "Running" + response);
                            Utils.cancelAlarmForSendUSerDetail(getApplicationContext());
                            Utils.cancelAlarmForOnRequest(getApplicationContext());
                            Utils.cancelAlarmForSendAllUserData(getApplicationContext());
                            Utils.scheduleAlarmForSendAllUserData(getApplicationContext());
                            Utils.SchedulServiceForOnRequest(getApplicationContext());
   }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            //   Toast.makeText(HomeActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                            Log.e("eror", error.toString());
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("gmail", sd.getEmail());
                    params.put("imei", sd.getIMEI());
                    params.put("gcm_id", "Dammy");
                    return params;
                }

            };

            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            requestQueue.add(stringRequest);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {


        }

    }


}
