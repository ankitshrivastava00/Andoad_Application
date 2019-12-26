package rapidora.co.myapplication.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by azmat.ali.khan on 05/05/16.
 */
public class DbHelper extends SQLiteOpenHelper {

    public DbHelper(Context pContext) {
        super(pContext,DbConstants.DATABASE_INFINITY, null,
                DbConstants.DATABASE_VERSION);

    }

    private final String DATABASE_CREATE_TABLE_OFFLINEDATA = "CREATE TABLE IF NOT EXISTS "
            + DbConstants.TABLE_OFFLINEDATA
            + "("
            + DbConstants.COLUMN_ID
            + " integer primary key, "
            + DbConstants.COLUMN_DATA
            + " text, "
            + DbConstants.COLUMN_URL + " text );";


    @Override
    public void onCreate(SQLiteDatabase pDb) {
       pDb.execSQL(DATABASE_CREATE_TABLE_OFFLINEDATA);
    }

    @Override
    public void onUpgrade(SQLiteDatabase pDb, int oldVersion, int newVersion) {

    }
}
