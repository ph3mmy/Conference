package com.crossover.myconference.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.crossover.myconference.DashBoardActivity;
import com.crossover.myconference.R;
import com.crossover.myconference.helper.PrefUtils;

/**
 * Created by oluwafemi.bamisaye on 3/8/2016.
 */
public class RoleDialogFragment extends DialogFragment {


/*    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
//        communicator= (Communicator) activity;
    }*/
    String roleString;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Choose Role");
        builder.setSingleChoiceItems(R.array.Role_array, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which==0){
                    roleString = "Administrator";
                }else if (which==1){
                roleString= "Doctor";
                }
            }
        });

        builder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                PrefUtils.setRole(getActivity(),roleString);
                PrefUtils.setFirstTime(getActivity(),false);
                Intent homeIntent = new Intent(getActivity(), DashBoardActivity.class);
                startActivity(homeIntent);
            }
        });

        Dialog dialog = builder.create();
        dialog.setCancelable(false);
        return dialog;
    }

}
