package com.newbision.drawingpad.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import com.newbision.drawingpad.R;
import com.newbision.drawingpad.databinding.DialogSelectStrokeBinding;
import com.newbision.drawingpad.source.OnSelectStroke;

public class AppSelectionStroke {
    private static OnSelectStroke selectStrokeListener;

    public static void show(Context context, int width, final OnSelectStroke selectStroke){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        DialogSelectStrokeBinding viewBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.dialog_select_stroke, null, false);
        builder.setView(viewBinding.getRoot());

        final AlertDialog alertDialog = builder.create();
        alertDialog.show();

        selectStrokeListener = selectStroke;

        switch (width){
            case AppConstant.STROKE_WIDTH_1:
                viewBinding.rlWidth1.setBackground(ContextCompat.getDrawable(context, R.drawable.style_border_rect_width_1));
                break;

            case AppConstant.STROKE_WIDTH_2:
                viewBinding.rlWidth2.setBackground(ContextCompat.getDrawable(context, R.drawable.style_border_rect_width_2));
                break;

            case AppConstant.STROKE_WIDTH_3:
                viewBinding.rlWidth3.setBackground(ContextCompat.getDrawable(context, R.drawable.style_border_rect_width_3));
                break;

            case AppConstant.STROKE_WIDTH_4:
                viewBinding.rlWidth4.setBackground(ContextCompat.getDrawable(context, R.drawable.style_border_rect_width_4));
                break;

            case AppConstant.STROKE_WIDTH_5:
                viewBinding.rlWidth5.setBackground(ContextCompat.getDrawable(context, R.drawable.style_border_rect_width_5));
                break;
        }

        viewBinding.rlContainerWidth1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectStroke.selectStroke(AppConstant.STROKE_WIDTH_1);
                alertDialog.dismiss();
            }
        });

        viewBinding.rlContainerWidth2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectStroke.selectStroke(AppConstant.STROKE_WIDTH_2);
                alertDialog.dismiss();
            }
        });

        viewBinding.rlContainerWidth3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectStroke.selectStroke(AppConstant.STROKE_WIDTH_3);
                alertDialog.dismiss();
            }
        });

        viewBinding.rlContainerWidth4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectStroke.selectStroke(AppConstant.STROKE_WIDTH_4);
                alertDialog.dismiss();
            }
        });

        viewBinding.rlContainerWidth5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectStroke.selectStroke(AppConstant.STROKE_WIDTH_5);
                alertDialog.dismiss();
            }
        });
    }
}
