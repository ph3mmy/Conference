package com.crossover.myconference.provider;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

import java.net.URI;

/**
 * Created by oluwafemi.bamisaye on 3/9/2016.
 */
public class EventDataContract {

    //define authority for the provider
    public static final String CONTENT_AUTHORITY ="com.crossover.myconference.provider";

    //authority of Base Uri
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    //path or table
    public static final String PATH_CONFERENCE = "conference_table";

    public static class Conference implements BaseColumns, ConferenceColumns{

        public static final Uri CONTENT_URI  =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_CONFERENCE).build();


        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE
                        + "/vnd.com.crossover.myconference.provider.conference_table";


        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE
                        + "/vnd.com.splasherstech.TripsMobile.provider.car_reports";

        public static final String[] PROJECTION = {
                _ID, TITLE,TIME,LOCATION,DATE,CREATOR
        };
        public static final String DEFAULT_SORT_ORDER = _ID + " ASC";


    }


    interface ConferenceColumns{
        String TITLE = "title";
        String LOCATION = "location";
        String DATE = "date";
        String TIME = "time";
        String CREATOR = "creator";

    }

}
