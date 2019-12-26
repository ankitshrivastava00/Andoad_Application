package rapidora.co.myapplication.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.List;

/**
 * Created by azmat.ali.khan on 05/05/16.
 */
public class DbAccesser {
    private final DbHelper dbHelper;
    private SQLiteDatabase mdb;
    private static DbAccesser mIinstance;

    private DbAccesser(Context pcontext) {
        dbHelper = new DbHelper(pcontext);
    }

    public static DbAccesser getInstance(Context pcontext) {
        synchronized (DbAccesser.class) {
            if (mIinstance == null) {
                mIinstance = new DbAccesser(pcontext);
            }
        }
        return mIinstance;
    }

    /********************************************************************
     * @function open()
     * @return void
     * @description Open the database
     *******************************************************************/
    public SQLiteDatabase open() {
        mdb = dbHelper.getWritableDatabase();
        return mdb;
    }

    /********************************************************************
     * @function open()
     * @return void
     * @description Open the database
     *******************************************************************/
    public SQLiteDatabase getDb() {
        if (mdb == null || !mdb.isOpen()) {
            open();
        }
        return mdb;
    }

    /********************************************************************
     * @function insertIntoTable()
     *            , String
     * @return long
     * @description insert into the table.
     *******************************************************************/
    public long insertIntoTable(ContentValues pInitialValues, String pTableName) {
        long id = 0;
        try {
            id = getDb().insert(pTableName, null, pInitialValues);
        } catch (Exception e) {

        }
        return id;
    }

    /**
     * insert into table in bulk i.e. multiple rows
     *
     * @param pInitialValues
     * @param pTableName
     */
    public void bulkInsertIntoTable(List<ContentValues> pInitialValues,
                                    String pTableName) {
        if (pInitialValues == null || pInitialValues.isEmpty()) {
            return;
        }
        SQLiteDatabase lDb = getDb();
        lDb.beginTransaction();
        try {
            for (ContentValues lContentValue : pInitialValues) {
                lDb.insert(pTableName, null, lContentValue);
            }
            lDb.setTransactionSuccessful();
        } finally {
            lDb.endTransaction();
        }
    }

    /**
     * querying table
     *
     * @param pTableName
     * @param pColums
     * @param pSelection
     * @param pSelectionArgs
     * @param pGroupBy
     * @param pHaving
     * @param pOrderBy
     * @return Cursor
     */
    public Cursor query(String pTableName, String[] pColums, String pSelection,
                        String[] pSelectionArgs, String pGroupBy, String pHaving,
                        String pOrderBy) {
        Cursor cursor = null;
        try {
            cursor = getDb().query(pTableName, pColums, pSelection,
                    pSelectionArgs, pGroupBy, pHaving, pOrderBy);
        } catch (Exception e) {

        }
        return cursor;

    }

    public Cursor limitQuery(String pTableName, String[] pColums,
                             String pSelection, String[] pSelectionArgs, String pGroupBy,
                             String pHaving, String pOrderBy, String limit) {
        Cursor cursor = null;
        try {
            cursor=getDb().query(true, pTableName, pColums, pSelection,
                    pSelectionArgs, pGroupBy, pHaving, pOrderBy, limit);
        } catch (Exception e) {

        }
        return cursor;
    }

    /********************************************************************
     * @function deleteFromTable()
     * @param pTableName
     *            tableName, String where
     * @return int
     * @description Delete from the given table.
     *******************************************************************/
    public int deleteFromTable(String pTableName, String pWhere,
                               String[] pWhereArgs) {

        int id = 0;

        try {
            id = getDb().delete(pTableName, pWhere, pWhereArgs);
        } catch (Exception ex) {

        }
        return id;
    }

    public int updateFromTable(String pTableName, ContentValues pValues,
                               String pWhereClause, String[] pWhereArgs) {
        int id = 0;
        try {
            id = getDb().update(pTableName, pValues, pWhereClause, pWhereArgs);
        } catch (Exception ex) {

        }
        return id;
    }

    /********************************************************************
     * @function getTableCursor()
     * @param tableName
     *            tableName, String where, String sortType
     * @return Cursor
     * @description Get Cursor.
     *******************************************************************/
    public Cursor getTableCursor(String tableName, String[] columns,
                                 String where, String sortType) {
        try {
            return getDb().query(tableName, columns, where, null, null, null,
                    sortType, null);
        } catch (Exception ex) {

        }
        return null;
    }

    /********************************************************************
     * @function close()
     * @return void
     * @description Closes the database
     *******************************************************************/
    public void close() {
        try {
            if (mdb != null) {
                mdb.close();
            }
        } catch (Exception e) {

        } finally {
            if (dbHelper != null) {
                dbHelper.close();
            }

            mIinstance = null;
        }
    }

    /**
     * query the database
     *
     * @param sql
     * @param selectionArgs
     * @return Cursor
     */
    public Cursor rawQuery(String sql, String[] selectionArgs) {
        try {
            return getDb().rawQuery(sql, selectionArgs);
        } catch (Exception e) {

        }
        return null;
    }


}
