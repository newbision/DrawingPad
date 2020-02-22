package com.newbision.drawingpad.utils;

import android.content.Context;
import android.widget.Toast;

public class AppToastMessage {
    public static void showMessage(Context context, String message){
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    public static void showShortMessage(Context context, String message){
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}
