package com.newbision.drawingpad.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import com.newbision.drawingpad.R;
import com.newbision.drawingpad.databinding.DialogSelectColorBinding;
import com.newbision.drawingpad.source.OnSelectColor;

public class AppSelectionColor {
    private static OnSelectColor selectColorListener;

    public static void show(Context context, int color, final OnSelectColor selectColor){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        DialogSelectColorBinding viewBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.dialog_select_color, null, false);
        builder.setView(viewBinding.getRoot());

        final AlertDialog alertDialog = builder.create();
        alertDialog.show();

        selectColorListener = selectColor;

        switch (color){
            case AppConstant.colorBlack:
                viewBinding.rlBlack.setBackground(ContextCompat.getDrawable(context, R.drawable.style_border_circle_black));
                break;

            case AppConstant.colorBlue:
                viewBinding.rlBlue.setBackground(ContextCompat.getDrawable(context, R.drawable.style_border_circle_blue));
                break;

            case AppConstant.colorRed:
                viewBinding.rlRed.setBackground(ContextCompat.getDrawable(context, R.drawable.style_border_circle_red));
                break;

            case AppConstant.colorGreen:
                viewBinding.rlGreen.setBackground(ContextCompat.getDrawable(context, R.drawable.style_border_circle_green));
                break;

            case AppConstant.colorYellow:
                viewBinding.rlYellow.setBackground(ContextCompat.getDrawable(context, R.drawable.style_border_circle_yellow));
                break;
        }

        viewBinding.rlContainerBlack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectColor.selectColor(AppConstant.colorBlack);
                alertDialog.dismiss();
            }
        });

        viewBinding.rlContainerBlue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectColor.selectColor(AppConstant.colorBlue);
                alertDialog.dismiss();
            }
        });

        viewBinding.rlContainerRed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectColor.selectColor(AppConstant.colorRed);
                alertDialog.dismiss();
            }
        });

        viewBinding.rlContainerGreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectColor.selectColor(AppConstant.colorGreen);
                alertDialog.dismiss();
            }
        });

        viewBinding.rlContainerYellow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectColor.selectColor(AppConstant.colorYellow);
                alertDialog.dismiss();
            }
        });

    }

}
