package com.newbision.drawingpad.source.custom_view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

import androidx.core.view.ViewCompat;

import com.newbision.drawingpad.model.Drawing;
import com.newbision.drawingpad.source.OnSelectEraser;
import com.newbision.drawingpad.source.OnSetZoomLevel;
import com.newbision.drawingpad.source.OnUndoRedoPaths;
import com.newbision.drawingpad.utils.AppConstant;
import com.newbision.drawingpad.view.MainActivity;

import java.util.ArrayList;
import java.util.List;

public class DrawingPad extends View {
    private static final float TOUCH_TOLERANCE = 4;
    private float mX, mY;
    private Paint mPaint;
    private Path mPath;
    private int currentColor;
    private int strokeWidth;
    private Bitmap savedBitmap;
    private Bitmap mBitmap;
    private Canvas mCanvas;
    private Paint mBitmapPaint = new Paint(Paint.DITHER_FLAG);

    private List<Drawing> drawingList = new ArrayList<>();
    private List<Drawing> drawingUndoList = new ArrayList<>();
    private int drawingType;
    private int width;
    private int height;
    private boolean isUndoRedo;

    private ScaleGestureDetector mScaleDetector;
    private GestureDetector mGestureDetector;

    private Rect clipCanvas;
    private Matrix mMatrix;
    private Drawable mBoard;
    private float scaleFactor = 1.5f;
    private float mBoardWidth;
    private float mBoardHeight;

    private OnSelectEraser selectEraserListener;
    private OnUndoRedoPaths undoRedoPathsListener;
    private OnSetZoomLevel setZoomLevelListener;

    public DrawingPad(Context context) {
        super(context);
    }

    public DrawingPad(Context context, AttributeSet attrs) {
        super(context, attrs);

        //Initializing the paint attributes
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setColor(Color.BLACK);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setXfermode(null);
        mPaint.setAlpha(0xff);

        drawingType = AppConstant.DRAWING_TYPE_PENCIL;
    }

    public void initialize(DisplayMetrics metrics, Bitmap oldBitmap, View view, MainActivity mainScreen) {
        height = metrics.heightPixels;
        width = metrics.widthPixels;

        //If old bitmap is null them it means there is saved bitmap before.
        if(oldBitmap != null){
            savedBitmap = oldBitmap;
            Bitmap loadedBitmap = Bitmap.createBitmap(savedBitmap);
            mBitmap = loadedBitmap.copy(Bitmap.Config.ARGB_8888, true);
            mCanvas = new Canvas(mBitmap);
        }
        else {
            mBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            mCanvas = new Canvas(mBitmap);
        }

        currentColor = Color.BLACK;
        strokeWidth = AppConstant.STROKE_WIDTH_1;

        selectEraserListener = (OnSelectEraser) mainScreen;
        undoRedoPathsListener = (OnUndoRedoPaths) mainScreen;
        setZoomLevelListener = mainScreen;

        mBoard = new BitmapDrawable(mainScreen.getResources(), mBitmap);
        mBoardWidth = mBoard.getIntrinsicWidth();
        mBoardHeight = mBoard.getIntrinsicHeight();
        mBoard.setBounds(0, 0, (int) mBoardWidth, (int) mBoardHeight);

        mMatrix = new Matrix();
        //mMatrix.setScale(scaleFactor, scaleFactor);
        //float scale = ((float)((int)(scaleFactor * 100))) / 100;
        float centreX = view.getX() + width  / 2;
        float centreY = view.getY() + height / 2;
        mMatrix.setScale(scaleFactor, scaleFactor, centreX, centreY);

        mScaleDetector = new ScaleGestureDetector(mainScreen, scaleListener);
        mGestureDetector = new GestureDetector(mainScreen, listener);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //canvas.save();
        canvas.concat(mMatrix);
        clipCanvas = canvas.getClipBounds();
        //mBoard.draw(canvas);

        //mCanvas.drawColor(backgroundColor);

        for (Drawing drawing : drawingList) {
            mPaint.setColor(drawing.getColor());
            mPaint.setStrokeWidth(drawing.getStrokeWidth());

            switch (drawing.getType()) {
                case AppConstant.DRAWING_TYPE_PENCIL:
                    //mPaint.setMaskFilter(null);
                    mPaint.setStyle(Paint.Style.STROKE);

                    mCanvas.drawPath(drawing.getPath(), mPaint);
                    Log.d("check_drawing", "Pencil");
                    break;

                case AppConstant.DRAWING_TYPE_ERASER:
                    //mPaint.setMaskFilter(null);
                    mPaint.setStyle(Paint.Style.STROKE);
                    mPaint.setColor(Color.WHITE);
                    mPaint.setStrokeWidth(AppConstant.STROKE_WIDTH_5);

                    mCanvas.drawPath(drawing.getPath(), mPaint);
                    Log.d("check_drawing", "Eraser");
                    break;

                case AppConstant.DRAWING_TYPE_LINE:
                    mPaint.setStyle(Paint.Style.STROKE);

                    if(!isUndoRedo){
                        canvas.drawLine(drawing.getStartX(), drawing.getStartY(), drawing.getStopX(), drawing.getStopY(), mPaint);
                    }
                    else {
                        mCanvas.drawLine(drawing.getStartX(), drawing.getStartY(), drawing.getStopX(), drawing.getStopY(), mPaint);
                    }

                    Log.d("check_drawing", "Line");
                    break;

                case AppConstant.DRAWING_TYPE_LINE_ARROW:
                    mPaint.setStyle(Paint.Style.STROKE);
                    float x = drawing.getStartX();
                    float y = drawing.getStartY();
                    float x1 = drawing.getStopX();
                    float y1 = drawing.getStopY();

                    if(!isUndoRedo){
                        canvas.drawLine(drawing.getStartX(), drawing.getStartY(), drawing.getStopX(), drawing.getStopY(), mPaint);
                    }
                    else {
                        mCanvas.drawLine(drawing.getStartX(), drawing.getStartY(), drawing.getStopX(), drawing.getStopY(), mPaint);
                    }

                    double degree = calculateDegree(x, x1, y, y1);
                    float endX1 = (float) (x1 + ((50) * Math.cos(Math.toRadians((degree-30)+90))));
                    float endY1 = (float) (y1 + ((50) * Math.sin(Math.toRadians(((degree-30)+90)))));

                    float endX2 = (float) (x1 + ((50) * Math.cos(Math.toRadians((degree-60)+180))));
                    float endY2 = (float) (y1 + ((50) * Math.sin(Math.toRadians(((degree-60)+180)))));

                    if(!isUndoRedo){
                        canvas.drawLine(x1, y1, endX1, endY1, mPaint);
                        canvas.drawLine(x1, y1, endX2, endY2, mPaint);
                    }
                    else {
                        mCanvas.drawLine(x1, y1, endX1, endY1, mPaint);
                        mCanvas.drawLine(x1, y1, endX2, endY2, mPaint);
                    }

                    break;

                case AppConstant.DRAWING_TYPE_RECTANGLE:
                    mPaint.setStyle(Paint.Style.STROKE);

                    if(!isUndoRedo){
                        canvas.drawRect(drawing.getStartX(), drawing.getStartY(), drawing.getStopX(), drawing.getStopY(), mPaint);
                    }
                    else {
                        mCanvas.drawRect(drawing.getStartX(), drawing.getStartY(), drawing.getStopX(), drawing.getStopY(), mPaint);
                    }

                    Log.d("check_drawing", "Rectangle");
                    break;

                case AppConstant.DRAWING_TYPE_RECTANGLE_FILLED:
                    mPaint.setStyle(Paint.Style.FILL);

                    if(!isUndoRedo){
                        canvas.drawRect(drawing.getStartX(), drawing.getStartY(), drawing.getStopX(), drawing.getStopY(), mPaint);
                    }
                    else {
                        mCanvas.drawRect(drawing.getStartX(), drawing.getStartY(), drawing.getStopX(), drawing.getStopY(), mPaint);
                    }

                    Log.d("check_drawing", "Rectangle Filled");
                    break;

                case AppConstant.DRAWING_TYPE_CIRCLE:
                    mPaint.setStyle(Paint.Style.STROKE);

                    if(!isUndoRedo){
                       canvas.drawOval(new RectF(drawing.getStartX(), drawing.getStartY(), drawing.getStopX(), drawing.getStopY()), mPaint);
                    }
                    else {
                        mCanvas.drawOval(new RectF(drawing.getStartX(), drawing.getStartY(), drawing.getStopX(), drawing.getStopY()), mPaint);
                    }

                    Log.d("check_drawing", "Circle");
                    break;

                case AppConstant.DRAWING_TYPE_CIRCLE_FILLED:
                    mPaint.setStyle(Paint.Style.FILL);

                    if(!isUndoRedo){
                        canvas.drawOval(new RectF(drawing.getStartX(), drawing.getStartY(), drawing.getStopX(), drawing.getStopY()), mPaint);
                    }
                    else {
                        mCanvas.drawOval(new RectF(drawing.getStartX(), drawing.getStartY(), drawing.getStopX(), drawing.getStopY()), mPaint);
                    }

                    Log.d("check_drawing", "Circle Filled");
                    break;
            }
        }

        canvas.drawBitmap(mBitmap, 0, 0, mBitmapPaint);
        //canvas.restore();
    }

    @Override
    @SuppressLint("ClickableViewAccessibility")
    public boolean onTouchEvent(MotionEvent event) {
        isUndoRedo = false;
        //In order to get the exact position after scaling and translate, divide the scale factor into event's X and Y
        float x = event.getX() / scaleFactor + clipCanvas.left;
        float y = event.getY() / scaleFactor + clipCanvas.top;

        //onTouch listener for Pencil and Eraser
        if (drawingType == AppConstant.DRAWING_TYPE_PENCIL || drawingType == AppConstant.DRAWING_TYPE_ERASER) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    //This action is called when the user first touch the screen.
                    touchStart(x, y);
                    invalidate();

                    Log.d("check_process", "onTouchEvent: Action Down X: " + x + " Y: " + y);
                    break;
                case MotionEvent.ACTION_MOVE:
                    //This action is called when the user move the finger on the screen.
                    touchMove(x, y);
                    invalidate();

                    Log.d("check_process", "onTouchEvent: Action Move X: " + x + " Y: " + y);
                    break;
                case MotionEvent.ACTION_UP:
                    //This action is called when the user un-touch the screen
                    touchUp();
                    invalidate();

                    Log.d("check_process", "onTouchEvent: Action Up");
                    break;
            }
            return true;
        }

        //onTouch listener for shapes
        if (drawingType == AppConstant.DRAWING_TYPE_LINE || drawingType == AppConstant.DRAWING_TYPE_LINE_ARROW || drawingType == AppConstant.DRAWING_TYPE_RECTANGLE
                || drawingType == AppConstant.DRAWING_TYPE_RECTANGLE_FILLED || drawingType == AppConstant.DRAWING_TYPE_CIRCLE || drawingType == AppConstant.DRAWING_TYPE_CIRCLE_FILLED) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                Drawing currentDrawing = new Drawing(currentColor, strokeWidth, drawingType, x, y);
                currentDrawing.setColor(currentColor);
                drawingList.add(currentDrawing);
                undoRedoPathsListener.enableDisableUndo(true);
                return true;
            }
            else if ((event.getAction() == MotionEvent.ACTION_MOVE || event.getAction() == MotionEvent.ACTION_UP) && drawingList.size() > 0) {
                Drawing currentDrawing = drawingList.get(drawingList.size() - 1);
                currentDrawing.setStopX(x);
                currentDrawing.setStopY(y);

                //drawing the line, rect, circle etc inside the canvas bitmap directly
                if(event.getAction() == MotionEvent.ACTION_UP){
                    drawCanvasBitmap(currentDrawing);
                }

                invalidate();
                return true;
            }
        }

        //onTouch listener For zooming
        if(drawingType == AppConstant.DRAWING_TYPE_ZOOMING){
            mGestureDetector.onTouchEvent(event);
            mScaleDetector.onTouchEvent(event);
            return true;
        }

        return false;
    }

    private void touchStart(float x, float y) {
        mPath = new Path();
        //adding the drawn path into the path list array.
        Drawing fp = new Drawing(currentColor, strokeWidth, drawingType, mPath);
        drawingList.add(fp);

        mPath.reset();
        mPath.moveTo(x, y);
        mX = x;
        mY = y;

        undoRedoPathsListener.enableDisableUndo(true);
        Log.d("check_process", "touchStart: mX: " + mX + " mY: " + mY);
    }

    private void touchMove(float x, float y) {
        float dx = Math.abs(x - mX);
        float dy = Math.abs(y - mY);

        if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
            mPath.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2);
            mX = x;
            mY = y;
        }

        Log.d("check_process", "touchMove: mX: " + mX + " mY: " + mY);
    }

    private void touchUp() {
        mPath.lineTo(mX, mY);

        //int index = drawingList.size() - 1;
        //Drawing drawing = drawingList.get(index);
        //drawing.setPath(mPath);
        //drawingList.set(index, drawing);

        Log.d("check_process", "touchUp");
    }

    private void drawCanvasBitmap(Drawing drawing) {
        switch (drawing.getType()){
            case AppConstant.DRAWING_TYPE_LINE:
                mPaint.setStyle(Paint.Style.STROKE);
                mCanvas.drawLine(drawing.getStartX(), drawing.getStartY(), drawing.getStopX(), drawing.getStopY(), mPaint);
                break;

            case AppConstant.DRAWING_TYPE_LINE_ARROW:
                mPaint.setStyle(Paint.Style.STROKE);
                float x = drawing.getStartX();
                float y = drawing.getStartY();
                float x1 = drawing.getStopX();
                float y1 = drawing.getStopY();

                mCanvas.drawLine(drawing.getStartX(), drawing.getStartY(), drawing.getStopX(), drawing.getStopY(), mPaint);

                double degree = calculateDegree(x, x1, y, y1);
                float endX1 = (float) (x1 + ((50) * Math.cos(Math.toRadians((degree-30)+90))));
                float endY1 = (float) (y1 + ((50) * Math.sin(Math.toRadians(((degree-30)+90)))));

                float endX2 = (float) (x1 + ((50) * Math.cos(Math.toRadians((degree-60)+180))));
                float endY2 = (float) (y1 + ((50) * Math.sin(Math.toRadians(((degree-60)+180)))));

                mCanvas.drawLine(x1, y1, endX1, endY1, mPaint);
                mCanvas.drawLine(x1, y1, endX2, endY2, mPaint);
                break;

            case AppConstant.DRAWING_TYPE_RECTANGLE:
                mPaint.setStyle(Paint.Style.STROKE);

                mCanvas.drawRect(drawing.getStartX(), drawing.getStartY(), drawing.getStopX(), drawing.getStopY(), mPaint);
                break;

            case AppConstant.DRAWING_TYPE_RECTANGLE_FILLED:
                mPaint.setStyle(Paint.Style.FILL);

                mCanvas.drawRect(drawing.getStartX(), drawing.getStartY(), drawing.getStopX(), drawing.getStopY(), mPaint);
                break;

            case AppConstant.DRAWING_TYPE_CIRCLE:
                mPaint.setStyle(Paint.Style.STROKE);

                mCanvas.drawOval(new RectF(drawing.getStartX(), drawing.getStartY(), drawing.getStopX(), drawing.getStopY()), mPaint);
                break;

            case AppConstant.DRAWING_TYPE_CIRCLE_FILLED:
                mPaint.setStyle(Paint.Style.FILL);

                mCanvas.drawOval(new RectF(drawing.getStartX(), drawing.getStartY(), drawing.getStopX(), drawing.getStopY()), mPaint);
                break;
        }
    }

    public void setDrawing(int drawingType) {
        this.drawingType = drawingType;
    }

    //setting the paint color
    public void setPaintColor(int color) {
        currentColor = color;
    }

    //setting the stroke width
    public void setPaintStroke(int stroke) {
        strokeWidth = stroke;
    }

    //setting the stroke and width for eraser
    public void setEraserMode(int color, int stroke) {
        this.currentColor = color;
        this.strokeWidth = stroke;
    }

    public int getDrawingCount(){
        return (drawingList == null)? 0:drawingList.size();
    }

    public void enableDisableEraser() {
        //selectEraserListener.selectEraser(drawingList);
    }

    public void setUndo() {
        if(savedBitmap != null){
            Bitmap loadedBitmap = Bitmap.createBitmap(savedBitmap);
            mBitmap = loadedBitmap.copy(Bitmap.Config.ARGB_8888, true);
            mCanvas = new Canvas(mBitmap);
        }
        else {
            mBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            mCanvas = new Canvas(mBitmap);
        }
        isUndoRedo = true;

        /*
         * First checking if the pencilList array contains path or not. If path is available then removing the last path from pencilList array
         * and adding that removed path into another array pencilUndoList.
         * */
        if (drawingList.size() > 0) {
            drawingUndoList.add(drawingList.remove(drawingList.size() - 1));
            invalidate();
        }

        //If pencilList is less than or equal to zero then make the undo disable
        if (drawingList.size() <= 0) {
            undoRedoPathsListener.enableDisableUndo(false);
        }

        //If pencilUndoList is greater than zero then make the redo enable
        if (drawingUndoList.size() > 0) {
            undoRedoPathsListener.enableDisableRedo(true);
        }
    }

    public void setRedo() {
        /*
         * First checking if the pencilUndoList array contains path or not. If path is available then removing the last path from pencilUndoList array
         * and adding that removed path into array pencilList.
         * */
        if (drawingUndoList.size() > 0) {
            drawingList.add(drawingUndoList.remove(drawingUndoList.size() - 1));
            invalidate();
        }

        //If pencilUndoList is less than or equal to zero then make the redo disable
        if (drawingUndoList.size() <= 0) {
            undoRedoPathsListener.enableDisableRedo(false);
        }

        //If pencilList is greater than zero then make the undo enable otherwise disable
        if (drawingList.size() > 0) {
            undoRedoPathsListener.enableDisableUndo(true);
        } else {
            undoRedoPathsListener.enableDisableUndo(false);
        }
    }

    public List<Drawing> getDrawingList(){
        return drawingList;
    }

    public void setDrawingList(List<Drawing> drawingList){
        this.drawingList = drawingList;
        invalidate();
    }

    public Bitmap getCanvasBitmap(){
        return mBitmap;
    }

    public void deleteDrawing(){
        savedBitmap = null;
        mBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);

        drawingList.clear();
        drawingUndoList.clear();
        invalidate();
    }

    //Removing all drawn path
    public void clear() {
        if(drawingList.size() > 0 || drawingUndoList.size() > 0){
            if(savedBitmap != null){
                Bitmap loadedBitmap = Bitmap.createBitmap(savedBitmap);
                mBitmap = loadedBitmap.copy(Bitmap.Config.ARGB_8888, true);
                mCanvas = new Canvas(mBitmap);
            }
            else {
                mBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
                mCanvas = new Canvas(mBitmap);
            }
            isUndoRedo = true;

            drawingList.clear();
            drawingUndoList.clear();
            invalidate();
        }
    }

    //This is used to calculate the degree for showing arrow head
    public double calculateDegree(float x1, float x2, float y1, float y2) {
        float startRadians = (float) Math.atan((y2 - y1) / (x2 - x1));
        //System.out.println("radian=====" + Math.toDegrees(startRadians));
        startRadians += ((x2 >= x1) ? 90 : -90) * Math.PI / 180;
        return Math.toDegrees(startRadians);
    }

    //This method is used to reset the zooming on canvas.
    private void resetScaling(){
        mMatrix = new Matrix();
        scaleFactor = 1f;
        invalidate();

        setZoomLevelListener.setZoomLevel(scaleFactor);
    }

    //This listener is used to measure the scale factor for Zoom In/Out
    ScaleGestureDetector.OnScaleGestureListener scaleListener = new ScaleGestureDetector.SimpleOnScaleGestureListener() {
        @Override
        public boolean onScale(ScaleGestureDetector scaleDetector) {
            scaleFactor *= scaleDetector.getScaleFactor();

            if(scaleFactor > 2.2){
                scaleFactor = 2.2f;
            }
            else if(scaleFactor < 0.3f){
                scaleFactor = 0.3f;
            }
            scaleFactor = ((float)((int)(scaleFactor * 100))) / 100;//jitter-protection
            mMatrix.setScale(scaleFactor, scaleFactor, scaleDetector.getFocusX(), scaleDetector.getFocusY());

            /*scaleFactor = scaleDetector.getScaleFactor();
            mMatrix.postScale(scaleFactor, scaleFactor, getWidth(), getHeight());
            ViewCompat.postInvalidateOnAnimation(DrawingPad.this);*/
            Log.d("check_zooming", "Value: "+scaleFactor);
            setZoomLevelListener.setZoomLevel(scaleFactor);
            return true;
        }
    };

    //This listener is use to detect gesture of scrolling into the screen. This listener is also used to handle double touch on screen.
    GestureDetector.OnGestureListener listener = new GestureDetector.SimpleOnGestureListener() {
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float dX, float dY) {
            if(scaleFactor <= 1.0f){
                return true;
            }

            mMatrix.postTranslate(-dX, -dY);
            ViewCompat.postInvalidateOnAnimation(DrawingPad.this);
            Log.d("check_touch", "SimpleOnGestureListener");
            return true;
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            resetScaling();
            Log.d("check_touch", "onDoubleTap");
            return super.onDoubleTap(e);
        }
    };

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        float scale = Math.max(w / mBoardWidth, h / mBoardHeight);
        //scaleFactor = ((float)((int)(scaleFactor * 100))) / 100;//jitter-protection
        //mMatrix.setScale(scaleFactor, scaleFactor);
        //mMatrix.postTranslate((w - scale * mBoardWidth) / 2f, (h - scale * mBoardHeight) / 2f);
        Log.d("check_touch", "OnSizeChange");
    }
}
