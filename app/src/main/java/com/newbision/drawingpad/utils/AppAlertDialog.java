package com.newbision.drawingpad.utils;

import android.content.Context;
import android.content.DialogInterface;

import androidx.appcompat.app.AlertDialog;

import com.newbision.drawingpad.R;
import com.newbision.drawingpad.source.OnClickAlertDialogTwoButtons;

public class AppAlertDialog {
    private static OnClickAlertDialogTwoButtons clickDialogTwoBtns;

    public static void showAlertMessage(Context context, String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message);

        builder.setPositiveButton(context.getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public static void showAlertMessageWithTwoButtons(Context context, OnClickAlertDialogTwoButtons clickListener,
                                                      final String dialog_name, String title, String message,
                                                      String btn_pos_name, String btn_neg_name){

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message);
        builder.setTitle(title);

        clickDialogTwoBtns = clickListener;

        builder.setPositiveButton(btn_pos_name, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                clickDialogTwoBtns.clickPositiveDialogButton(dialog_name);
            }
        });

        builder.setNegativeButton(btn_neg_name, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                clickDialogTwoBtns.clickNegativeDialogButton(dialog_name);
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setCancelable(false);
        alertDialog.show();
    }
}
