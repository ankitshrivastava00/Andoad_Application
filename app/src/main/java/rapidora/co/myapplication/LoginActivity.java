package rapidora.co.myapplication;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rapidora.co.myapplication.common.ConnectionDetector;
import rapidora.co.myapplication.common.Constants;
import rapidora.co.myapplication.common.CustomLogger;
import rapidora.co.myapplication.common.SessionManagment;
import rapidora.co.myapplication.common.Utils;
import rapidora.co.myapplication.db.DbUtils;
import rapidora.co.myapplication.model.LoginModel;
import rapidora.co.myapplication.model.OfflineDataModel;
import rapidora.co.myapplication.model.UpdateLocationRequestModel;
import rapidora.co.myapplication.services.SendOfflineDataService;

public class LoginActivity extends AppCompatActivity {
    RequestQueue queue;
   // private RequestQueue requestQueue;
   static final String REQ_TAG = "VACTIVITY";

    ConnectionDetector cd;
    SessionManagment sd;
    private EditText editNumber;
    private Button btnSubmit;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        cd = new ConnectionDetector(LoginActivity.this);
        sd = new SessionManagment(LoginActivity.this);
        queue = Volley.newRequestQueue(LoginActivity.this);
        btnSubmit=(Button)findViewById(R.id.btnSubmit);
        editNumber=(EditText) findViewById(R.id.editNumber);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String s = editNumber.getText().toString().trim();
                if (s.isEmpty()){
                    editNumber.setError("Please Enter Mobile Number");
                }else {
                    makeJsonObjReq(s);
                }
            }
        });
    }

    private void makeJsonObjReq(String str) {

        if (!cd.isConnectingToInternet()){
            Toast.makeText(LoginActivity.this, "Internet Connection not available", Toast.LENGTH_LONG).show();

        }else {
            JSONObject json = new JSONObject();
            try {
                json.put("mobile", str);
            //    json.put("category","fashion");
             //   json.put("type","coupons");
            } catch (JSONException e) {
                e.printStackTrace();
            }

           // String url = getResources().getString(R.string.json_get_url);
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, "http://dolphintechnosolution.com/index.php/webservices/userLogin", json,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                String id = response.getJSONObject("responce").getString("id");
                                sd.setUserId(id);
                                Intent i =new Intent(LoginActivity.this,MainActivity.class);
                                startActivity(i);
                                finish();

                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(LoginActivity.this, e.toString(), Toast.LENGTH_LONG).show();

                            }                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                   // serverResp.setText("Error getting response");
                    Toast.makeText(LoginActivity.this, error.toString(), Toast.LENGTH_LONG).show();

                }
            });
            jsonObjectRequest.setTag(REQ_TAG);
            queue.add(jsonObjectRequest);

    }
    }

/*
    private void makeJsonObjReq(String str) {

        if (!cd.isConnectingToInternet()){
            Toast.makeText(LoginActivity.this, "Internet Connection not available", Toast.LENGTH_LONG).show();

        }else {
        Map<String, String> postParam= new HashMap<String, String>();
        postParam.put("mobile", str);


        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                Constants.USERINSERT, new JSONObject(postParam),
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        //Log.d(TAG, response.toString());
                        //msgResponse.setText(response.toString());
                       // hideProgressDialog();
                        try {
                            String id = response.getJSONObject("responce").getString("id");
                            sd.setUserId(id);
                            Intent i =new Intent(LoginActivity.this,MainActivity.class);
                            startActivity(i);
                            finish();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
           //     VolleyLog.d(TAG, "Error: " + error.getMessage());
               // hideProgressDialog();
            }
        }) {

            */
/**
             * Passing some request headers
             * *//*

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }



        };

      //  jsonObjReq.setTag(TAG);
        // Adding request to request queue
        queue.add(jsonObjReq);

        // Cancelling request
    */
/* if (queue!= null) {
    queue.cancelAll(TAG);
    } *//*


    }
    }
*/
}
