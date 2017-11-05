package com.example.cpodariu.intelipark_android.Models;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import com.example.cpodariu.intelipark_android.R;

/**
 * Created by cpodariu on 04.11.2017.
 */

@SuppressLint("ValidFragment")
public class MessageDialog extends DialogFragment {

    String message;

    public MessageDialog(String message) {
        super();
        this.message = message;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(message)
                .setPositiveButton("Take me there!", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // FIRE ZE MISSILES!
                    }
                }).setNegativeButton("Ok",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // FIRE ZE MISSILES!
                    }});
            // Create the AlertDialog object and return it
        return builder.create();
    }
}