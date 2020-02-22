package com.newbision.drawingpad.view;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;

import com.newbision.drawingpad.R;
import com.newbision.drawingpad.databinding.ActivityMainBinding;
import com.newbision.drawingpad.model.Drawing;
import com.newbision.drawingpad.source.OnClickAlertDialogTwoButtons;
import com.newbision.drawingpad.source.OnSelectDrawing;
import com.newbision.drawingpad.source.OnSelectEraser;
import com.newbision.drawingpad.source.OnSetZoomLevel;
import com.newbision.drawingpad.source.OnUndoRedoPaths;
import com.newbision.drawingpad.source.custom_view.DrawingPad;
import com.newbision.drawingpad.source.OnSelectColor;
import com.newbision.drawingpad.source.OnSelectStroke;
import com.newbision.drawingpad.utils.AppAlertDialog;
import com.newbision.drawingpad.utils.AppCalculateZoomLevel;
import com.newbision.drawingpad.utils.AppConstant;
import com.newbision.drawingpad.utils.AppSelectionColor;
import com.newbision.drawingpad.utils.AppSelectionDrawing;
import com.newbision.drawingpad.utils.AppSelectionStroke;
import com.newbision.drawingpad.utils.AppSharedPreference;
import com.newbision.drawingpad.utils.AppToastMessage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, OnSelectEraser, OnUndoRedoPaths, OnSetZoomLevel {
    private ActivityMainBinding viewBinding;
    private DrawingPad drawingPad;

    private int selectedColor;
    private int selectedStrokeWidth;
    private boolean isEraserModeActive;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        initializations();

        //Initialing the height and width of the custom drawing pad view
        drawingPad = viewBinding.drawingPad;
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        Bitmap oldBitmap = null;
        String filePath = AppSharedPreference.getStringPreference(this, AppConstant.PREFERENCE_FILE_PATH, "");
        if(!filePath.isEmpty()){
            oldBitmap = loadImageFromStorage(filePath);
            Log.d("check_file", ""+filePath);

            AppSharedPreference.addIntegerPreference(this, AppConstant.PREFERENCE_SELECTED_DRAWING_TYPE, AppConstant.DRAWING_TYPE_ZOOMING);
            updateDrawingType(AppConstant.DRAWING_TYPE_ZOOMING);
        }
        drawingPad.initialize(metrics, oldBitmap, viewBinding.drawingPad, this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_drawing:
                int defaultDrawing = AppSharedPreference.getIntegerPreference(this, AppConstant.PREFERENCE_SELECTED_DRAWING_TYPE, AppConstant.DRAWING_TYPE_PENCIL);

                AppSelectionDrawing.show(this, defaultDrawing, new OnSelectDrawing() {
                    @Override
                    public void selectDrawing(int drawing) {

                        if(drawing == AppConstant.DRAWING_TYPE_ERASER){
                            //isEraserModeActive = true;

                            if(drawingPad.getDrawingCount() > 0){
                                updateDrawingType(drawing);
                            }
                            else {
                                AppToastMessage.showMessage(MainActivity.this, getString(R.string.no_path_drawn_yet));
                            }
                        }
                        else {
                            updateDrawingType(drawing);
                        }
                    }
                });

                break;

            case R.id.tv_paint_color:
                int defaultColor = AppSharedPreference.getIntegerPreference(this, AppConstant.PREFERENCE_SELECTED_PAINT_COLOR, AppConstant.colorBlack);

                AppSelectionColor.show(this, defaultColor, new OnSelectColor() {
                    @Override
                    public void selectColor(int color) {
                        drawingPad.setPaintColor(color);
                        AppSharedPreference.addIntegerPreference(MainActivity.this, AppConstant.PREFERENCE_SELECTED_PAINT_COLOR, color);

                        changeSelectionColor(color);
                        selectedColor = color;
                    }
                });

                disableEraser();
                break;

            case R.id.iv_paint_width:
                int defaultStrokeWidth = AppSharedPreference.getIntegerPreference(this, AppConstant.PREFERENCE_SELECTED_PAINT_STROKE, AppConstant.STROKE_WIDTH_3);

                AppSelectionStroke.show(this, defaultStrokeWidth, new OnSelectStroke() {
                    @Override
                    public void selectStroke(int width) {
                        drawingPad.setPaintStroke(width);
                        AppSharedPreference.addIntegerPreference(MainActivity.this, AppConstant.PREFERENCE_SELECTED_PAINT_STROKE, width);

                        selectedStrokeWidth = width;
                    }
                });

                disableEraser();
                break;

            case R.id.iv_eraser:
                drawingPad.enableDisableEraser();
                break;

            case R.id.iv_undo:
                drawingPad.setUndo();

                disableEraser();
                break;

            case R.id.iv_redo:
                drawingPad.setRedo();

                disableEraser();
                break;

            case R.id.iv_clear_all:
                drawingPad.clear();

                disableEraser();
                viewBinding.ivUndo.setAlpha(0.4f);
                viewBinding.ivRedo.setAlpha(0.4f);
                break;

            case R.id.iv_save:
                if(drawingPad.getDrawingList().size() <= 0){
                    AppToastMessage.showMessage(MainActivity.this, "No Drawing Available.");
                    return;
                }

                saveDrawing(drawingPad.getCanvasBitmap());
                break;

            case R.id.iv_delete:
                AppAlertDialog.showAlertMessageWithTwoButtons(this, new OnClickAlertDialogTwoButtons() {
                    @Override
                    public void clickPositiveDialogButton(String dialog_name) {
                        AppSharedPreference.addStringPreference(MainActivity.this, AppConstant.PREFERENCE_FILE_PATH, "");

                        drawingPad.deleteDrawing();
                        updateDrawingType(AppConstant.DRAWING_TYPE_PENCIL);
                    }

                    @Override
                    public void clickNegativeDialogButton(String dialog_name) {

                    }
                }, "", "", "Are you sure to delete?", getString(R.string.yes), getString(R.string.no));
                break;
        }
    }

    @Override
    public void enableDisableUndo(boolean isEnable) {
        if (isEnable) {
            viewBinding.ivUndo.setAlpha(1.0f);
        }
        else {
            viewBinding.ivUndo.setAlpha(0.4f);
        }
    }

    @Override
    public void enableDisableRedo(boolean isEnable) {
        if (isEnable) {
            viewBinding.ivRedo.setAlpha(1.0f);
        }
        else {
            viewBinding.ivRedo.setAlpha(0.4f);
        }
    }

    @Override
    public void selectEraser(List<Drawing> drawingList) {
        if(drawingList.size() > 0){
            if (!isEraserModeActive) {
                enableEraser();
            } else {
                disableEraser();
            }
        }
        else {
            AppToastMessage.showMessage(MainActivity.this, getString(R.string.no_path_drawn_yet));
        }
    }

    @Override
    public void setZoomLevel(float scaleFactor) {
        Log.d("check_scale", ""+scaleFactor);
        viewBinding.tvToolbarTitle.setText(getText(R.string.app_name)+" ("+ AppCalculateZoomLevel.getZoomPercentage(scaleFactor) +"%)");
    }

    private void initializations() {
        setSupportActionBar(viewBinding.toolbarScreen);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        viewBinding.ivDrawing.setOnClickListener(this);
        viewBinding.tvPaintColor.setOnClickListener(this);
        viewBinding.ivPaintWidth.setOnClickListener(this);
        viewBinding.ivEraser.setOnClickListener(this);
        viewBinding.ivUndo.setOnClickListener(this);
        viewBinding.ivRedo.setOnClickListener(this);
        viewBinding.ivClearAll.setOnClickListener(this);
        viewBinding.ivSave.setOnClickListener(this);
        viewBinding.ivDelete.setOnClickListener(this);

        //Setting the undo/redo buttons not selectable.
        viewBinding.ivUndo.setAlpha(0.4f);
        viewBinding.ivRedo.setAlpha(0.4f);

        //Setting the default values for paint color and stroke width
        selectedColor = AppConstant.colorBlack;
        selectedStrokeWidth = AppConstant.STROKE_WIDTH_1;
        AppSharedPreference.addIntegerPreference(this, AppConstant.PREFERENCE_SELECTED_DRAWING_TYPE, AppConstant.DRAWING_TYPE_PENCIL);
        AppSharedPreference.addIntegerPreference(this, AppConstant.PREFERENCE_SELECTED_PAINT_COLOR, selectedColor);
        AppSharedPreference.addIntegerPreference(this, AppConstant.PREFERENCE_SELECTED_PAINT_STROKE, selectedStrokeWidth);

        //setting default zoom level
        setZoomLevel(1.5f);
    }

    private void saveDrawing(Bitmap canvasBitmap) {
        if(canvasBitmap != null){
            String filePath = saveToInternalStorage(canvasBitmap);
            AppSharedPreference.addStringPreference(this, AppConstant.PREFERENCE_FILE_PATH, filePath);

            AppToastMessage.showMessage(MainActivity.this, getString(R.string.drawing_save));
        }
    }

    private void setSelectedDrawing(int drawing) {
        switch (drawing) {
            case AppConstant.DRAWING_TYPE_PENCIL:
                drawingPad.setPaintStroke(AppConstant.STROKE_WIDTH_1);
                AppSharedPreference.addIntegerPreference(MainActivity.this, AppConstant.PREFERENCE_SELECTED_PAINT_STROKE, AppConstant.STROKE_WIDTH_1);

                viewBinding.ivDrawing.setImageDrawable(ContextCompat.getDrawable(MainActivity.this, R.drawable.ic_pencil));
                break;

            case AppConstant.DRAWING_TYPE_ERASER:
                //drawingPad.setPaintStroke(AppConstant.STROKE_WIDTH_5);
                viewBinding.ivDrawing.setImageDrawable(ContextCompat.getDrawable(MainActivity.this, R.drawable.ic_eraser));
                break;

            case AppConstant.DRAWING_TYPE_LINE:
                viewBinding.ivDrawing.setImageDrawable(ContextCompat.getDrawable(MainActivity.this, R.drawable.ic_line));
                break;

            case AppConstant.DRAWING_TYPE_LINE_ARROW:
                viewBinding.ivDrawing.setImageDrawable(ContextCompat.getDrawable(MainActivity.this, R.drawable.ic_line_arrow));
                break;

            case AppConstant.DRAWING_TYPE_RECTANGLE:
                viewBinding.ivDrawing.setImageDrawable(ContextCompat.getDrawable(MainActivity.this, R.drawable.ic_rect));
                break;

            case AppConstant.DRAWING_TYPE_RECTANGLE_FILLED:
                viewBinding.ivDrawing.setImageDrawable(ContextCompat.getDrawable(MainActivity.this, R.drawable.ic_rect_filled));
                break;

            case AppConstant.DRAWING_TYPE_CIRCLE:
                viewBinding.ivDrawing.setImageDrawable(ContextCompat.getDrawable(MainActivity.this, R.drawable.ic_circle));
                break;

            case AppConstant.DRAWING_TYPE_CIRCLE_FILLED:
                viewBinding.ivDrawing.setImageDrawable(ContextCompat.getDrawable(MainActivity.this, R.drawable.ic_circle_filled));
                break;

            case AppConstant.DRAWING_TYPE_ZOOMING:
                viewBinding.ivDrawing.setImageDrawable(ContextCompat.getDrawable(MainActivity.this, R.drawable.ic_hand));
                break;
        }
    }

    private void changeSelectionColor(int color) {
        switch (color) {
            case AppConstant.colorBlack:
                viewBinding.tvPaintColor.setBackground(ContextCompat.getDrawable(this, R.drawable.style_circle_black));
                break;

            case AppConstant.colorBlue:
                viewBinding.tvPaintColor.setBackground(ContextCompat.getDrawable(this, R.drawable.style_circle_blue));
                break;

            case AppConstant.colorRed:
                viewBinding.tvPaintColor.setBackground(ContextCompat.getDrawable(this, R.drawable.style_circle_red));
                break;

            case AppConstant.colorGreen:
                viewBinding.tvPaintColor.setBackground(ContextCompat.getDrawable(this, R.drawable.style_circle_green));
                break;

            case AppConstant.colorYellow:
                viewBinding.tvPaintColor.setBackground(ContextCompat.getDrawable(this, R.drawable.style_circle_yellow));
                break;
        }
    }

    private void updateDrawingType(int drawing) {
        drawingPad.setDrawing(drawing);

        setSelectedDrawing(drawing);

        AppSharedPreference.addIntegerPreference(MainActivity.this, AppConstant.PREFERENCE_SELECTED_DRAWING_TYPE, drawing);
    }

    private void enableEraser() {
        drawingPad.setEraserMode(Color.WHITE, AppConstant.STROKE_WIDTH_5);
        isEraserModeActive = true;
        viewBinding.ivEraser.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_eraser_selected));
    }

    private void disableEraser() {
        drawingPad.setEraserMode(selectedColor, selectedStrokeWidth);
        isEraserModeActive = false;
        viewBinding.ivEraser.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_eraser));
    }

    private String saveToInternalStorage(Bitmap bitmapImage){
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        // Create imageDir
        File mypath=new File(directory,"drawing.png");

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return directory.getAbsolutePath();
    }

    private Bitmap loadImageFromStorage(String path) {
        Bitmap bitmap = null;
        try {
            File f=new File(path, "drawing.png");
            bitmap = BitmapFactory.decodeStream(new FileInputStream(f));
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return bitmap;
    }
}
