package rapidora.co.myapplication.services;

import android.Manifest;
import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.PowerManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.ExecutionException;

import rapidora.co.myapplication.common.ConnectionDetector;
import rapidora.co.myapplication.common.CustomLogger;
import rapidora.co.myapplication.common.LocationTracker;
import rapidora.co.myapplication.common.Utils;


public class UpdateLocationService extends IntentService {
    String locationRequestString;
    Location location;
   // PowerManager.WakeLock screenLock;
    ConnectionDetector cd;
    String current_date;

    public UpdateLocationService() {
        super(UpdateLocationService.class.getName());

    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.e("onHandleIntent", "onHandleIntent");
        /*if(!Utils.isTrackerValid(this)){
            return;
        }*/
       /* if (!Utils.isDeviceLocationOn(this)) {
            CustomLogger.getInsatance(this).putLog(" Location is off");
            return;
        }*/
        CustomLogger.getInsatance(this).putLog("Location HANDLEINTENT");

        if (Build.VERSION.SDK_INT > 23) {
            location = getLastLocationNewMethod();

        } else {

      /*  screenLock = ((PowerManager) getApplicationContext().getSystemService(Context.POWER_SERVICE)).newWakeLock(
                PowerManager.PARTIAL_WAKE_LOCK, "TAG");
        screenLock.acquire();*/
            location = LocationTracker.getLocationTrackerInstance(this)
                    .getLocation();


            // Log.e("Error update location", location.toString());
            if (location == null) {
                CustomLogger.getInsatance(this).putLog("Location not Found");
                return;
            } else {
                Log.e("Service run", "ing");
                CustomLogger.getInsatance(this).putLog("Location sERVICE RUNNING SENDlOCATION");
                Utils.sendLocation(UpdateLocationService.this, location);

//                if (screenLock.isHeld()) screenLock.release();
                //executeMethod();


            }
        }
    }


    private Location getLastLocationNewMethod() {
        try {
            FusedLocationProviderClient mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return null;
            }


        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location1) {
                        // GPS location can be null if GPS is switched off
                       // CustomLogger.getInsatance(UpdateLocationService.this).putLog("Location HANDLEINTENT longitude" + location1.getLongitude());
                       Log.e("location", String.valueOf(location1.getLatitude()) + "longitude" + location1.getLongitude());
                        location = location1;
                        if (location == null) {
                            CustomLogger.getInsatance(UpdateLocationService.this).putLog("Location not Found");
                            return;
                        } else {
                            Log.e("Service run", "ing");
                            CustomLogger.getInsatance(UpdateLocationService.this).putLog("Location sERVICE RUNNING SENDlOCATION");
                            new Locationtask().execute();
                            // if (screenLock.isHeld()) screenLock.release();
                            //executeMethod();


                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("MapDemoActivity", "Error trying to get last GPS location");
                        e.printStackTrace();
                    }
                });
        return location;
    } catch(
    Exception e)

    {
        CustomLogger.getInsatance(UpdateLocationService.this).putLog("Location Error" + e.toString());
        Log.e("Error update location", e.toString());
        return null;
    }
}


public class Locationtask extends AsyncTask<Void, Void, Void> {
    @Override
    protected Void doInBackground(Void... params) {

        try {

            Utils.sendLocation(UpdateLocationService.this, location);


        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
}
