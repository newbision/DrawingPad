package com.newbision.drawingpad.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import com.newbision.drawingpad.R;
import com.newbision.drawingpad.databinding.DialogSelectDrawingBinding;
import com.newbision.drawingpad.source.OnSelectDrawing;

public class AppSelectionDrawing {
    private static OnSelectDrawing selectDrawingListener;

    public static void show(Context context, int drawing, final OnSelectDrawing selectDrawing){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        final DialogSelectDrawingBinding viewBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.dialog_select_drawing, null, false);
        builder.setView(viewBinding.getRoot());

        final AlertDialog alertDialog = builder.create();
        alertDialog.show();

        selectDrawingListener = selectDrawing;

        switch (drawing){
            case AppConstant.DRAWING_TYPE_PENCIL:
                viewBinding.rlPencil.setBackground(ContextCompat.getDrawable(context, R.drawable.style_border_rect_width_3));
                break;

            case AppConstant.DRAWING_TYPE_ERASER:
                viewBinding.rlEraser.setBackground(ContextCompat.getDrawable(context, R.drawable.style_border_rect_width_3));
                break;

            case AppConstant.DRAWING_TYPE_LINE:
                viewBinding.rlLine.setBackground(ContextCompat.getDrawable(context, R.drawable.style_border_rect_width_3));
                break;

            case AppConstant.DRAWING_TYPE_LINE_ARROW:
                viewBinding.rlLineArrow.setBackground(ContextCompat.getDrawable(context, R.drawable.style_border_rect_width_3));
                break;

            case AppConstant.DRAWING_TYPE_RECTANGLE:
                viewBinding.rlRect.setBackground(ContextCompat.getDrawable(context, R.drawable.style_border_rect_width_3));
                break;

            case AppConstant.DRAWING_TYPE_RECTANGLE_FILLED:
                viewBinding.rlRectFilled.setBackground(ContextCompat.getDrawable(context, R.drawable.style_border_rect_width_3));
                break;

            case AppConstant.DRAWING_TYPE_CIRCLE:
                viewBinding.rlCircle.setBackground(ContextCompat.getDrawable(context, R.drawable.style_border_rect_width_3));
                break;

            case AppConstant.DRAWING_TYPE_CIRCLE_FILLED:
                viewBinding.rlCircleFilled.setBackground(ContextCompat.getDrawable(context, R.drawable.style_border_rect_width_3));
                break;

            case AppConstant.DRAWING_TYPE_ZOOMING:
                viewBinding.rlZooming.setBackground(ContextCompat.getDrawable(context, R.drawable.style_border_rect_width_3));
                break;
        }

        viewBinding.rlContainerPencil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectDrawingListener.selectDrawing(AppConstant.DRAWING_TYPE_PENCIL);
                alertDialog.dismiss();
            }
        });

        viewBinding.rlContainerEraser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectDrawingListener.selectDrawing(AppConstant.DRAWING_TYPE_ERASER);
                alertDialog.dismiss();
            }
        });

        viewBinding.rlContainerLine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectDrawingListener.selectDrawing(AppConstant.DRAWING_TYPE_LINE);
                alertDialog.dismiss();
            }
        });

        viewBinding.rlContainerLineArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectDrawingListener.selectDrawing(AppConstant.DRAWING_TYPE_LINE_ARROW);
                alertDialog.dismiss();
            }
        });

        viewBinding.rlContainerRect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectDrawingListener.selectDrawing(AppConstant.DRAWING_TYPE_RECTANGLE);
                alertDialog.dismiss();
            }
        });

        viewBinding.rlContainerRectFilled.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectDrawingListener.selectDrawing(AppConstant.DRAWING_TYPE_RECTANGLE_FILLED);
                alertDialog.dismiss();
            }
        });

        viewBinding.rlContainerCircle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectDrawingListener.selectDrawing(AppConstant.DRAWING_TYPE_CIRCLE);
                alertDialog.dismiss();
            }
        });

        viewBinding.rlContainerCircleFilled.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectDrawingListener.selectDrawing(AppConstant.DRAWING_TYPE_CIRCLE_FILLED);
                alertDialog.dismiss();
            }
        });

        viewBinding.rlContainerZooming.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectDrawingListener.selectDrawing(AppConstant.DRAWING_TYPE_ZOOMING);
                alertDialog.dismiss();
            }
        });
    }

}
