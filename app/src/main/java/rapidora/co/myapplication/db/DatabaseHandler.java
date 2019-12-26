package rapidora.co.myapplication.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

import rapidora.co.myapplication.common.SessionManagment;
import rapidora.co.myapplication.model.AllLeadsModel;


public class DatabaseHandler extends SQLiteOpenHelper {
    SessionManagment sd;
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "Call_Master";

    private static final String TABLEALLINCOMINGCALL = "All_incoming_call_DB";

    private static final String KEY_START_TIME = "start_time";
    private static final String KEY_END_TIME = "end_time";
    private static final String KEY_DURATION = "duration";

    private static final String KEY_MOBILE_RECIEVER = "mobile";
    private static final String KEY_PUBLIC_OR_PRIVATE = "type";
    private static final String KEY_EXIST_IN_PHONEBOOK = "isexist";
    private static final String KEY_NATURE_OF_CALL = "callNature";
    private static final String KEY_NAME_OF_REC = "name";

    private static final String FILE_PATH = "file_path";


    private static final String TABLE_CONTACT = "Contact_List";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_PHONE = "phone";
    private static final String COLUMN_EMAIL = "email";
    private static final String COLUMN_TYPE = "type";
    private static final String COLUMN_ENTRY = "entry";


    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        sd = new SessionManagment(context);
    }


    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {


        String CREATE_INCOMING_CAll = "CREATE TABLE " + TABLEALLINCOMINGCALL + "("

                + KEY_START_TIME + " TEXT,"
                + KEY_END_TIME + " TEXT,"
                + KEY_PUBLIC_OR_PRIVATE + " TEXT,"
                + KEY_NATURE_OF_CALL + " TEXT,"
                + KEY_EXIST_IN_PHONEBOOK + " TEXT,"
                + KEY_NAME_OF_REC + " TEXT,"
                + KEY_DURATION + " TEXT,"
                + FILE_PATH + " TEXT,"
                + KEY_MOBILE_RECIEVER + " TEXT" + ")";
        db.execSQL(CREATE_INCOMING_CAll);

        String DATABASE_USER_LIST = "create table "
                + TABLE_CONTACT + "("
                + COLUMN_NAME + " text  , "
                + COLUMN_EMAIL + " text  , "
                + COLUMN_TYPE + " text  , "
                + COLUMN_ENTRY + " text  , "
                + COLUMN_PHONE + " text PRIMARY KEY );";

        db.execSQL(DATABASE_USER_LIST);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed

        db.execSQL("DROP TABLE IF EXISTS " + TABLEALLINCOMINGCALL);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACT);
        onCreate(db);
    }


    public void addIncomingCall(AllLeadsModel contact) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(KEY_START_TIME, contact.getStartTime());
            values.put(KEY_NAME_OF_REC, contact.getName());
            values.put(KEY_END_TIME, contact.getEndTime());

            values.put(KEY_DURATION, contact.getDuration());
            values.put(FILE_PATH, contact.getFilePath());
            values.put(KEY_MOBILE_RECIEVER, contact.getMobileReciever());
            values.put(KEY_PUBLIC_OR_PRIVATE, contact.getCallType());
            values.put(KEY_EXIST_IN_PHONEBOOK, contact.getAvailableInPhoneBook());
            values.put(KEY_NATURE_OF_CALL, contact.getNatureOfCall());
            db.insert(TABLEALLINCOMINGCALL, null, values);
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public ArrayList<AllLeadsModel> getAllIncoming() {

        ArrayList<AllLeadsModel> contactList = new ArrayList<AllLeadsModel>();
        // Select All Query
        try {
            String selectQuery = "SELECT  * FROM " + TABLEALLINCOMINGCALL;
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);
            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {


                    AllLeadsModel model = new AllLeadsModel();
                    model.setStartTime(cursor.getString(cursor.getColumnIndex(KEY_START_TIME)));
                    model.setEndTime(cursor.getString(cursor.getColumnIndex(KEY_END_TIME)));
                    model.setName(cursor.getString(cursor.getColumnIndex(KEY_NAME_OF_REC)));

                    model.setDuration(cursor.getString(cursor.getColumnIndex(KEY_DURATION)));
                    model.setFilePath(cursor.getString(cursor.getColumnIndex(FILE_PATH)));
                    model.setMobileReciever(cursor.getString(cursor.getColumnIndex(KEY_MOBILE_RECIEVER)));
                    model.setCallType(cursor.getString(cursor.getColumnIndex(KEY_PUBLIC_OR_PRIVATE)));
                    ;
                    model.setAvailableInPhoneBook(cursor.getString(cursor.getColumnIndex(KEY_EXIST_IN_PHONEBOOK)));
                    ;
                    model.setNatureOfCall(cursor.getString(cursor.getColumnIndex(KEY_NATURE_OF_CALL)));
                    ;

                    contactList.add(model);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        // return contact list
        return contactList;
    }

    public void deleteAllIncomingCall() {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            db.execSQL("delete from " + TABLEALLINCOMINGCALL);
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void deleteAllContact() {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            db.execSQL("delete from " + TABLE_CONTACT);
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public String getAllIContact() {

        String contactList = "";
        // Select All Query
        try {
            String selectQuery = "SELECT  * FROM " + TABLE_CONTACT;
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);
            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    contactList = contactList + "***" + cursor.getString(cursor.getColumnIndex(COLUMN_PHONE));
                    ;
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        // return contact list
        return contactList;
    }

    public void deleteDataFileRecord(String file) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            db.delete(TABLEALLINCOMINGCALL, FILE_PATH + "=" + file, null);
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}