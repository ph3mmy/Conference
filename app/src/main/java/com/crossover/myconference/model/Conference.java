package com.crossover.myconference.model;

import java.util.ArrayList;

/**
 * Created by oluwafemi.bamisaye on 3/9/2016.
 */
public class Conference {
        private String title;
        private String location;
        private String time;
        private String date;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Conference(String mTitle, String mLocation,String mDate, String mTime) {
            title = mTitle;
            location = mLocation;
            time=mTime;
            date=mDate;
        }


        private static int lastConferenceId = 0;

        public static ArrayList<Conference> createConferenceList(int numContacts) {
            ArrayList<Conference> conferences = new ArrayList<Conference>();

            for (int i = 1; i <= numContacts; i++) {
                conferences.add(new Conference("Person " + ++lastConferenceId, "location","time","date"));
            }

            return conferences;
        }
    }

