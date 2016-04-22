package com.crossover.myconference.adapter;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.database.Cursor;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.crossover.myconference.R;
import com.crossover.myconference.provider.EventDataContract;

import java.util.Date;

/**
 * Created by oluwafemi.bamisaye on 3/9/2016.
 */

public class ConferenceCursorAdapter extends CursorAdapter {
    private int lastAnimatedPosition = -1;

    private boolean animationsLocked = false;
    private boolean delayEnterAnimation = true;
    private Context mContext;


    public ConferenceCursorAdapter(Context context, Cursor c) {
        super(context, c, 0);
        mContext = context;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.conference_row_item, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        int position = cursor.getPosition();
        runEnterAnimation(view, position);

        TextView textViewTitle = (TextView) view.findViewById(R.id.tvEventTitle);
        TextView textViewLocation = (TextView) view.findViewById(R.id.tvEventLocation);
        TextView textViewDate = (TextView) view.findViewById(R.id.tvEventDate);
        TextView textViewTime = (TextView) view.findViewById(R.id.tvEventTime);


        final String title = cursor.getString(cursor.getColumnIndex(EventDataContract.Conference.TITLE));
        textViewTitle.setText(title);

        final String location = cursor.getString(cursor.getColumnIndex(EventDataContract.Conference.LOCATION));
        textViewLocation.setText(location);

        final String date = cursor.getString(cursor.getColumnIndex(EventDataContract.Conference.DATE));
        textViewDate.setText(date);

        final String time = cursor.getString(cursor.getColumnIndex(EventDataContract.Conference.TIME));
        textViewTime.setText(time);


    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    private void runEnterAnimation(View view, int position) {
        if (animationsLocked) return;

        if (position > lastAnimatedPosition) {
            lastAnimatedPosition = position;
            view.setTranslationY(100);
            view.setAlpha(0.f);
            view.animate()
                    .translationY(0).alpha(1.f)
                    .setStartDelay(delayEnterAnimation ? 20 * (position) : 0)
                    .setInterpolator(new DecelerateInterpolator(2.f))
                    .setDuration(300)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            animationsLocked = true;
                        }
                    })
                    .start();
        }
    }

}