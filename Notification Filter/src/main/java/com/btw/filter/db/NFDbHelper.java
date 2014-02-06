package com.btw.filter.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bbille on 05.02.14.
 */
public class NFDbHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "notificationFilterDB";
    private static final String TABLE_WHITELIST = "whitelistTable";
    private static final int DB_VERSION = 1;
    private static final String COLUMN_PK = "_id";
    private static final String COLUMN_WHITELIST_PACKAGE = "c_notificationPackage";
    private static final String COLUMN_WHITELIST_ENTRY = "c_noficationDTM";

    private static final String DATABASE_CREATE = "CREATE TABLE " + TABLE_WHITELIST + " (" +
            COLUMN_PK + " INTEGER PRIMARY KEY, " + COLUMN_WHITELIST_PACKAGE + " TEXT, " +
            COLUMN_WHITELIST_ENTRY + " TEXT)";

    private static final String SELECT_BY_PACKAGE = "SELECT * FROM " + TABLE_WHITELIST + " WHERE " + COLUMN_WHITELIST_PACKAGE + "= ?";

    public NFDbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(DATABASE_CREATE);
        insertTestData(sqLiteDatabase);
    }

    private void insertTestData(SQLiteDatabase sqLiteDatabase) {

        String[] locations = new String[]{"Neufahrn", "Garching", "Eching"};

        for (String name : locations) {
            ContentValues values = new ContentValues();
            values.put(COLUMN_WHITELIST_PACKAGE, "ingress");
            values.put(COLUMN_WHITELIST_ENTRY, name.toLowerCase());

            sqLiteDatabase.insert(TABLE_WHITELIST, null, values);
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_WHITELIST);

        // Create tables again
        onCreate(sqLiteDatabase);

    }

    public void addWhitelistEntry(WhitelistEntry entry) {

        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_WHITELIST_PACKAGE, entry.getNotificationPackage());
        values.put(COLUMN_WHITELIST_ENTRY, entry.getWhitelistEntry());

        db.insert(TABLE_WHITELIST, null, values);
        db.close();
    }

    public void dropTable() {

        SQLiteDatabase db = getWritableDatabase();

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_WHITELIST);

        onCreate(db);
        db.close();
    }

    public List<WhitelistEntry> getEntriesForPackage(String packageName) {

        SQLiteDatabase db = getReadableDatabase();

        List<WhitelistEntry> output = new ArrayList<WhitelistEntry>();

        String[] whereArgs = new String[]{packageName};

        Cursor c = db.rawQuery(SELECT_BY_PACKAGE, whereArgs);

        if (c.moveToFirst()) {
            do {
                WhitelistEntry we = new WhitelistEntry();
                we.setPrimaryKey(Integer.parseInt(c.getString(0)));
                we.setNotificationPackage(c.getString(1));
                we.setWhitelistEntry(c.getString(2));
                output.add(we);
            } while (c.moveToNext());
        }

        db.close();

        return output;
    }
}
