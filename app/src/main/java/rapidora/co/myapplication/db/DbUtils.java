package rapidora.co.myapplication.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import rapidora.co.myapplication.model.OfflineDataModel;


/**
 * Created by azmat.ali.khan on 05/05/16.
 */
public class DbUtils {

    public static void saveOfflineData(Context context, String data, String url) {
        ContentValues value = new ContentValues();
        value.put(DbConstants.COLUMN_DATA, data);
        value.put(DbConstants.COLUMN_URL,
                url);
        DbAccesser.getInstance(context).insertIntoTable(value,
                DbConstants.TABLE_OFFLINEDATA);
    }

    public static List<OfflineDataModel> getOfflineData(Context context) {
        List<OfflineDataModel> offlineDataList = new ArrayList<OfflineDataModel>();
        Cursor cursor = DbAccesser.getInstance(context).query(DbConstants.TABLE_OFFLINEDATA, null, null,
                null, null, null,
                null);

        if (cursor != null && cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                OfflineDataModel offlineDataModel = new OfflineDataModel();
                offlineDataModel.setId(cursor.getInt(cursor.getColumnIndex(DbConstants.COLUMN_ID)));
                offlineDataModel.setData(cursor.getString(cursor
                        .getColumnIndex(DbConstants.COLUMN_DATA)));
                offlineDataModel.setUrl(cursor.getString(cursor
                        .getColumnIndex(DbConstants.COLUMN_URL)));
                offlineDataList.add(offlineDataModel);
                cursor.moveToNext();
            }
        }

        return offlineDataList;
    }

    public static void deleteOfflineRecord(int id, Context context) {
        DbAccesser.getInstance(context).deleteFromTable(DbConstants.TABLE_OFFLINEDATA, DbConstants.COLUMN_ID + "=?",
                new String[]{String.valueOf(id)});
    }
}
