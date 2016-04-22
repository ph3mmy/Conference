package com.crossover.myconference.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import com.crossover.myconference.helper.SelectionBuilder;

/**
 * Created by oluwafemi.bamisaye on 3/9/2016.
 */
public class EventDataProvider extends ContentProvider {

    private static final String TAG = EventDataProvider.class.getName();

    EventDataBaseHelper mOpenHelper = null;
    SQLiteDatabase dbr, dbw;

    private static final int CONFERENCE_ID = 101;
    private static final int CONFERENCE_LIST = 102;

    private static UriMatcher sUriMatcher = buildUriMatcher();

    private static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = EventDataContract.CONTENT_AUTHORITY;

        matcher.addURI(authority, EventDataContract.PATH_CONFERENCE, CONFERENCE_LIST);
        matcher.addURI(authority, EventDataContract.PATH_CONFERENCE + "/#", CONFERENCE_ID);
        return matcher;
    }

    @Override
    public boolean onCreate() {
        mOpenHelper = new com.crossover.myconference.provider.EventDataBaseHelper(getContext());
        dbr = mOpenHelper.getReadableDatabase();
        dbw = mOpenHelper.getWritableDatabase();
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        int match = sUriMatcher.match(uri);
        SelectionBuilder builder = buildSelection(uri, match);

        switch (match) {
            case CONFERENCE_LIST:
                if (TextUtils.isEmpty(sortOrder)) {
                    sortOrder = EventDataContract.Conference.DEFAULT_SORT_ORDER;
                }
                break;
        }
        return builder.where(selection, selectionArgs).query(dbr, projection, sortOrder);
    }

    @Override
    public String getType(Uri uri) {
        switch (sUriMatcher.match(uri)) {

            case CONFERENCE_LIST:
                return EventDataContract.Conference.CONTENT_TYPE;
            case CONFERENCE_ID:
                return EventDataContract.Conference.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        Log.d(TAG, "insert(uri=" + uri + ")");
        //SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        int match = sUriMatcher.match(uri);
        long id = 0;
        switch (match) {

            case CONFERENCE_LIST: {
                id = dbw.insertOrThrow(EventDataBaseHelper.Table.CONFERENCE, null, values);
                notifyChange(uri);
                return getUriForId(id, uri);
            }default: {
                throw new UnsupportedOperationException("Unknown uri: " + uri);
            }
        }
    }

    private static Uri getUriForId(long id, Uri uri) {
        if (id > 0) {
            Uri itemUri = ContentUris.withAppendedId(uri, id);
            return itemUri;
        }
        throw new SQLException("Problem inserting into uri: " + uri);
    }

    private void notifyChange(Uri uri) {
        Context context = getContext();
        assert context != null;
        context.getContentResolver().notifyChange(uri, null);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        if (uri == EventDataContract.BASE_CONTENT_URI) {
            // Handle whole database deletes (e.g. when signing out)
            deleteDatabase();
            notifyChange(uri);
            return 1;
        }

        final SelectionBuilder builder = buildSelection(uri, sUriMatcher.match(uri));
        int retVal = builder.where(selection, selectionArgs).delete(dbw);
        notifyChange(uri);
        return retVal;
    }

    private void deleteDatabase() {

        mOpenHelper.close();
        Context context = getContext();
        EventDataBaseHelper.deleteDatabase(context);
        mOpenHelper = new EventDataBaseHelper(getContext());
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = dbw; // mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        final SelectionBuilder builder = buildSelection(uri, match);
        int retVal = builder.where(selection, selectionArgs).update(db, values);
        notifyChange(uri);
        return retVal;
    }

    private SelectionBuilder buildSelection(Uri uri, int match) {
        final SelectionBuilder builder = new SelectionBuilder();
        switch (match) {

            case CONFERENCE_LIST: {
                return builder.table(EventDataBaseHelper.Table.CONFERENCE);
            }
            case CONFERENCE_ID: {
                final String conferenceId = uri.getPathSegments().get(1);
                return builder.table(EventDataBaseHelper.Table.CONFERENCE)
                        .where(EventDataContract.Conference._ID + "=?", conferenceId);
            }

            default: {
                throw new UnsupportedOperationException("Unknown uri for " + match + ": " + uri);
            }
        }
    }
}
