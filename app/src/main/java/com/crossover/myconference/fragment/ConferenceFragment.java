package com.crossover.myconference.fragment;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.CalendarContract;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.crossover.myconference.R;
import com.crossover.myconference.adapter.ConferenceCursorAdapter;
import com.crossover.myconference.model.Conference;
import com.crossover.myconference.provider.EventDataContract;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by oluwafemi.bamisaye on 3/8/2016.
 */
public class ConferenceFragment extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor>{


    private static final String TAG = ConferenceFragment.class.getSimpleName();
    ListView listView;
    private ConferenceCursorAdapter adapter;
    private static final String LOADER_ID = "loader_id";
    private static final String SAVED_DATE = "saved_date";
    private TextView tvError;
    public static final int NORMAL_LOADER_KEY = 0;
    public static final int APPROVED_LOADER_KEY = 3;

    public int currentLoaderId = 0;
    Listener listener;
    private String selectedDate = "";
    private ArrayList<Conference> cursorList = new ArrayList<>();

    public ConferenceFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        adapter = new ConferenceCursorAdapter( getActivity(), null);

        setListAdapter(adapter);
        setHasOptionsMenu(true);

        if ( savedInstanceState != null){
            currentLoaderId = savedInstanceState.getInt(LOADER_ID);
            selectedDate = savedInstanceState.getString(SAVED_DATE);
            Log.e(TAG, currentLoaderId + " current loader id saved ");
        } else {
            currentLoaderId = 0;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view =  inflater.inflate(R.layout.fragment_conference_list, container, false);
        listView = (ListView) view.findViewById(android.R.id.list);
        listView.setItemsCanFocus(true);
        listView.setCacheColorHint(getResources().getColor(R.color.white));
        listView.setVerticalScrollBarEnabled(true);
        tvError = (TextView) view.findViewById(R.id.tvErrorMag);

        FloatingActionButton fab = (FloatingActionButton)view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onAddEventClicked(v);
                /*Snackbar.make(view, "I'm a Snackbar", Snackbar.LENGTH_LONG).setAction("Action", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getActivity(), "Snackbar Action", Toast.LENGTH_LONG).show();

                    }
                }).show();*/
            }
        });

        return view;
    }

    public void onAddEventClicked(View view){
        Intent intent = new Intent(Intent.ACTION_INSERT);
        intent.setType("vnd.android.cursor.item/event");

        Calendar cal = Calendar.getInstance();
        long startTime = cal.getTimeInMillis();
        long endTime = cal.getTimeInMillis()  + 60 * 60 * 1000;

        intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, startTime);
        intent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME,endTime);
        intent.putExtra(CalendarContract.EXTRA_EVENT_ALL_DAY, true);

        intent.putExtra(CalendarContract.Events.TITLE, "Neel Birthday");
        intent.putExtra(CalendarContract.Events.DESCRIPTION, "This is a sample description");
        intent.putExtra(CalendarContract.Events.EVENT_LOCATION, "My Guest House");
        intent.putExtra(CalendarContract.Events.RRULE, "FREQ=YEARLY");

        startActivity(intent);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        Log.e(TAG, currentLoaderId + " current loader id");
        outState.putInt(LOADER_ID, currentLoaderId);
        outState.putString(SAVED_DATE, selectedDate);
        super.onSaveInstanceState(outState);
    }


    public interface Listener {

        void onFragmentAttached(ListFragment listFragment);
        void onFragmentDetached(ListFragment listFragment);
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        CursorLoader cursorLoader = null;
        switch (id) {
            case NORMAL_LOADER_KEY:
                currentLoaderId = NORMAL_LOADER_KEY;
                cursorList.clear();

                cursorLoader = new CursorLoader(
                        getContext(),
                        EventDataContract.Conference.CONTENT_URI,
                        EventDataContract.Conference.PROJECTION,
                        null,
                        null,
                        null );
                break;
        }
        return  cursorLoader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Bundle bundle = new Bundle(); // bunch of Submission  id s
        long submissionId;
        int count = 0;

        if (data.moveToFirst()) {
            adapter.swapCursor(data);
            adapter.notifyDataSetChanged();

            data.moveToFirst();

            while (!data.isAfterLast()) {

                data.moveToNext();
            }
        } else {
            adapter.swapCursor(null);
            tvError.setVisibility(View.VISIBLE);
            tvError.setText("No Conference Created yet");
            tvError.setTextColor(getActivity().getResources().getColor(R.color.accent));

        }


    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adapter.swapCursor(null);
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return false;
    }

    public void restartLoaderForId(int id){
        getLoaderManager().restartLoader(id, null, ConferenceFragment.this);
    }

    private final ContentObserver mObserver = new ContentObserver(new Handler()) {
        @Override
        public void onChange(boolean selfChange) {
            if (!isAdded()) {
                return;
            }
            restartLoaderForId(NORMAL_LOADER_KEY);
            //getLoaderManager().restartLoader(NORMAL_LOADER_KEY, null, CarApprovalHistoryFragment.this);
        }
    };


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if(activity instanceof Listener){
            listener = (Listener) activity;
            listener.onFragmentAttached(this);
        }else {
            throw new ClassCastException(activity.toString()
                    + " must implement "+TAG +".Listener");
        }
        activity.getContentResolver().registerContentObserver(
                EventDataContract.Conference.CONTENT_URI, true, mObserver);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (getActivity() instanceof Listener) {
            ((Listener) getActivity()).onFragmentDetached(this);
        }
        getActivity().getContentResolver().unregisterContentObserver(mObserver);

    }

    @Override
    public void onResume() {
        super.onResume();
        if (!isAdded()) {
            return;
        }
        restartLoaderForId(currentLoaderId);
        //getLoaderManager().restartLoader(currentLoaderId, null, CarApprovalHistoryFragment.this);
    }

    public Conference convertCursorToCar(Cursor cursor){
//        String id = cursor.getString( cursor.getColumnIndex(EventDataContract.Conference._ID));
        String topic = cursor.getString( cursor.getColumnIndex(EventDataContract.Conference.TITLE));
        String location = cursor.getString( cursor.getColumnIndex(EventDataContract.Conference.LOCATION));
        String event_date= cursor.getString( cursor.getColumnIndex(EventDataContract.Conference.DATE));
        String event_time= cursor.getString( cursor.getColumnIndex(EventDataContract.Conference.TIME));


        return new Conference(topic,location,event_date,event_time);

    }

    public static long pushEventToCalender(Activity curActivity,
                                           String title, String addInfo, String place, int status,
                                           long startDate, int reminderTime, boolean needReminder,
                                           boolean needMailService) {

        String eventUriString = "content://com.android.calendar/events";
        ContentValues eventValues = new ContentValues();

        eventValues.put("calendar_id", 1);
        eventValues.put("title", title);
        eventValues.put("description", addInfo);
        eventValues.put("eventLocation", place);

        long endDate = startDate + 1000 * 60 * 60; // For next 1hr

        eventValues.put("dtstart", startDate);
        eventValues.put("dtend", endDate);

        // values.put("allDay", 1); //If it is bithday alarm or such
        // kind (which should remind me for whole day) 0 for false, 1
        // for true
        eventValues.put("eventStatus", status); // This information is
        // sufficient for most
        // entries tentative (0),
        // confirmed (1) or canceled
        // (2):
        /*
         * eventValues.put("visibility", 3); // visibility to default (0), //
         * confidential (1), private // (2), or public (3):
         */
        // eventValues.put("transparency", 0); // You can control whether
        // an event consumes time
        // opaque (0) or transparent
        // (1).
        eventValues.put("hasAlarm", 1); // 0 for false, 1 for true

        eventValues.put("eventTimezone", TimeZone.getDefault().getID());
        Uri eventUri = curActivity.getApplicationContext().getContentResolver()
                .insert(Uri.parse(eventUriString), eventValues);
        long eventID = Long.parseLong(eventUri.getLastPathSegment());

        if (needReminder) {
            /***************** Event: Reminder(with alert) Adding reminder to event *******************/

            String reminderUriString = "content://com.android.calendar/reminders";

            ContentValues reminderValues = new ContentValues();

            reminderValues.put("event_id", eventID);
            reminderValues.put("minutes", reminderTime); // Default value of the
            // system. Minutes is a
            // integer
            reminderValues.put("method", 1); // Alert Methods: Default(0),
            // Alert(1), Email(2),
            // SMS(3)

            Uri reminderUri = curActivity.getApplicationContext()
                    .getContentResolver()
                    .insert(Uri.parse(reminderUriString), reminderValues);
                Log.e("URI", reminderUri.toString());
        }

        /***************** Event: Meeting(without alert) Adding Attendies to the meeting *******************/

        if (needMailService) {
            String attendeuesesUriString = "content://com.android.calendar/attendees";

            /********
             * To add multiple attendees need to insert ContentValues multiple
             * times
             ***********/
            ContentValues attendeesValues = new ContentValues();

            attendeesValues.put("event_id", eventID);
            attendeesValues.put("attendeeName", "xxxxx"); // Attendees name
            attendeesValues.put("attendeeEmail", "yyyy@gmail.com");// Attendee
            // E
            // mail
            // id
            attendeesValues.put("attendeeRelationship", 0); // Relationship_Attendee(1),
            // Relationship_None(0),
            // Organizer(2),
            // Performer(3),
            // Speaker(4)
            attendeesValues.put("attendeeType", 0); // None(0), Optional(1),
            // Required(2), Resource(3)
            attendeesValues.put("attendeeStatus", 0); // NOne(0), Accepted(1),
            // Decline(2),
            // Invited(3),
            // Tentative(4)

            Uri attendeuesesUri = curActivity.getApplicationContext()
                    .getContentResolver()
                    .insert(Uri.parse(attendeuesesUriString), attendeesValues);
                Log.e("URI", attendeuesesUri.toString());
        }

        return eventID;

    }
}
