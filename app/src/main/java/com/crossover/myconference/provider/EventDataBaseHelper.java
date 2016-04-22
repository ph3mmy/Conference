package com.crossover.myconference.provider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

/**
 * Created by oluwafemi.bamisaye on 3/9/2016.
 */
public class EventDataBaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "conference_db";
    private static final String TAG = EventDataBaseHelper.class.getName();
    private static final int DATABASE_VERSION = 1;
    Context mContext;

    public EventDataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mContext=context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_CONFERENCE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public static void deleteDatabase(Context context) {
        context.deleteDatabase(DATABASE_NAME);
    }

    final static String SQL_CREATE_CONFERENCE_TABLE = " CREATE TABLE "
            + Table.CONFERENCE + "("
            + BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + EventDataContract.Conference.TITLE + " VARCHAR, "
            + EventDataContract.Conference.LOCATION + " VARCHAR, "
            + EventDataContract.Conference.CREATOR + " VARCHAR, "
            + EventDataContract.Conference.DATE + " VARCHAR, "
            + EventDataContract.Conference.TIME + " VARCHAR " + ")";


    public interface Table {
        String CONFERENCE ="conference";
    }
}
