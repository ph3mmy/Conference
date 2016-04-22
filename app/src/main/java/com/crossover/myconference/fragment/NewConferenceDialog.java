package com.crossover.myconference.fragment;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;

import com.crossover.myconference.R;

/**
 * Created by oluwafemi.bamisaye on 3/11/2016.
 */
public class NewConferenceDialog extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Choose Role");
        LayoutInflater inflater=getActivity().getLayoutInflater();
        View mView = inflater.inflate(R.layout.fragment_new_conference,null,false);

        builder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        Dialog dialog = builder.create();
        dialog.setCancelable(false);
        return dialog;
    }
}
