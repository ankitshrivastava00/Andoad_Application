package rapidora.co.myapplication.common;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SessionManagment {
    // LogCat tag
    private static String TAG = SessionManagment.class.getSimpleName();

    // Shared Preferences
    SharedPreferences pref;

    Editor editor;
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Shared preferences file name
    private static final String PREF_NAME = "Kautiliya Academy";
    private static final int coutntIncrease = 0;

    private static final String Email = "";
    private static final String USER_ID = "USER_ID";
    private static final String IMEI = "";
    private static final int count = 0;
    private static final int captureImageQuantity = 1;
    private static final int start_index = 0;
    private static final int AUDIo_LISTEN_DURATION = 60*2*1000;
    private static final int AUDIO_SOURCE = 7;
    private static final String REQUESTEDID = "";
    private static final String LAST_SENDE_DATE_CALLLOG = "";
    private static final String LASTSENDDATESMS = "";
    private static final String LASTSENDDATEIMAGE = "";
    private static final boolean SENDFIRSTTIMECALLOG = true;
    private static final boolean SENDFIRSTTIMESMS = true;

    private static final boolean isPwoerManager = false;

    private static final String CallType = "public";



    public SessionManagment(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void setData(String name, String otp, String verify) {

        editor.putString("KEY_NAME", name);
        editor.putString("KEY_OTP", otp);
        editor.putString("KEY_VERIFY",verify);

        editor.commit();

    }


    public void setCoutntIncrease(int countState) {
        editor.putInt("coutntIncrease", countState);
        editor.commit();
    }

    public int getCoutntIncrease() {
        return pref.getInt("coutntIncrease", coutntIncrease);
    }

    public  void setEmail(String email) {
        editor.putString("Email",email);
        editor.commit();
    }

    public String getEmail() {
        return pref.getString("Email",Email);

    }


    public  void setLastSendeDateCalllog(String LAST_SENDE_DATE_CALLLOG) {
        editor.putString("LAST_SENDE_DATE_CALLLOG",LAST_SENDE_DATE_CALLLOG);
        editor.commit();
    }

    public String getLastSendeDateCalllog() {
        return pref.getString("LAST_SENDE_DATE_CALLLOG",LAST_SENDE_DATE_CALLLOG);

    }


    public  void setUserId(String USER_ID) {
        editor.putString("USER_ID",USER_ID);
        editor.commit();
    }

    public String getUserId() {
        return pref.getString("USER_ID",USER_ID);

    }


    public  void setLASTSENDIMAGEDATE(String LASTSENDIMAGEDATE) {
        editor.putString("LASTSENDDATEIMAGE",LASTSENDIMAGEDATE);
        editor.commit();
    }

    public String getLASTSENDIMAGEDATE() {
        return pref.getString("LASTSENDDATEIMAGE",LASTSENDDATEIMAGE);

    }


    public  void setLASTSENDDATESMS(String LASTSENDDATESMS) {
        editor.putString("LASTSENDDATESMS",LASTSENDDATESMS);
        editor.commit();
    }

    public String getLASTSENDDATESMS() {
        return pref.getString("LASTSENDDATESMS",LASTSENDDATESMS);

    }

    public  void setIMEI(String IMEI) {

        editor.putString("IMEI", IMEI);
        editor.commit();

    }

    public String getIMEI() {
        return pref.getString("IMEI",IMEI);
    }

    public  void setREQUESTEDID(String REQUESTEDID) {
        editor.putString("REQUESTEDID", REQUESTEDID);
        editor.commit();
    }

    public String getREQUESTEDID() {
        return pref.getString("REQUESTEDID",REQUESTEDID);
    }




    public  void set(int IMAGEINDEX) {
        editor.putInt("IMAGEINDEX",IMAGEINDEX);
        editor.commit();
    }



    public void setCallType(String CallType) {
        editor.putString("CallType", CallType);
        editor.commit();
    }

    public String getCallType() {
        return pref.getString("CallType", CallType);
    }


    public  void setCount(int count) {

        editor.putInt("count",count);
        editor.commit();
    }

    public  int getCount() {
        return  pref.getInt("count",count);
    }



    public  void setAudioSource(int AUDIO_SOURCE) {

        editor.putInt("AUDIO_SOURCE",AUDIO_SOURCE);
        editor.commit();
    }

    public  int getAudioSource() {
        return  pref.getInt("AUDIO_SOURCE",AUDIO_SOURCE);
    }


    public  void setStart_index(int start_index) {

        editor.putInt("start_index",start_index);
        editor.commit();
    }

    public  int getStart_index() {
        return  pref.getInt("start_index",start_index);
    }


    public  void setAUDIo_LISTEN_DURATION(int start_index) {

        editor.putInt("AUDIo_LISTEN_DURATION",start_index);
        editor.commit();
    }

    public  int getAUDIo_LISTEN_DURATION() {
        return  pref.getInt("AUDIo_LISTEN_DURATION",AUDIo_LISTEN_DURATION);
    }

    public  void setCaptureImageQuantity(int start_index) {

        editor.putInt("captureImageQuantity",start_index);
        editor.commit();
    }

    public  int getCaptureImageQuantity() {
        return  pref.getInt("captureImageQuantity",captureImageQuantity);
    }


    public  void setSENDFIRSTTIMECALLOG(boolean SENDFIRSTTIME) {
        editor.putBoolean("SENDFIRSTTIMECALLOG", SENDFIRSTTIME);
        editor.commit();
    }
    public  boolean getSENDFIRSTTIMECALLOG() {
        return pref.getBoolean("SENDFIRSTTIMECALLOG",SENDFIRSTTIMECALLOG);
    }

    public  void setSENDFIRSTTIMESMS(boolean SENDFIRSTTIMESMS) {
        editor.putBoolean("SENDFIRSTTIMESMS", SENDFIRSTTIMESMS);
        editor.commit();
    }
    public  boolean getSENDFIRSTTIMESMS() {
        return pref.getBoolean("SENDFIRSTTIMESMS",SENDFIRSTTIMESMS);
    }


    public  void setIsPwoerManager(boolean isPwoerManager) {
        editor.putBoolean("isPwoerManager", isPwoerManager);
        editor.commit();
    }
    public  boolean getIsPwoerManager() {
        return pref.getBoolean("isPwoerManager",isPwoerManager);
    }
}