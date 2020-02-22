package com.newbision.drawingpad.model;

import android.graphics.Path;

import androidx.room.Ignore;

public class Drawing {
    private int id;
    private int color;
    private int strokeWidth;
    private int type;
    private float startX;
    private float startY;
    private float stopX;
    private float stopY;
    private Path path;

    @Ignore
    public Drawing(int color, int strokeWidth, int type, Path path) {
        this.color = color;
        this.strokeWidth = strokeWidth;
        this.type = type;
        this.path = path;
    }

    @Ignore
    public Drawing(int color, int strokeWidth, int type, float startX, float startY) {
        this(color, strokeWidth, type, startX, startY, startX, startY);
    }

    @Ignore
    public Drawing(int color, int strokeWidth, int type, float startX, float startY, float stopX, float stopY) {
        this.color = color;
        this.strokeWidth = strokeWidth;
        this.type = type;
        this.startX = startX;
        this.startY = startY;
        this.stopX = stopX;
        this.stopY = stopY;
    }

    //Constructor for saving data locally
    public Drawing(int color, int strokeWidth, int type, float startX, float startY, float stopX, float stopY, Path path) {
        this.color = color;
        this.strokeWidth = strokeWidth;
        this.type = type;
        this.startX = startX;
        this.startY = startY;
        this.stopX = stopX;
        this.stopY = stopY;
        this.path = path;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public int getStrokeWidth() {
        return strokeWidth;
    }

    public void setStrokeWidth(int strokeWidth) {
        this.strokeWidth = strokeWidth;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public float getStartX() {
        return startX;
    }

    public void setStartX(float startX) {
        this.startX = startX;
    }

    public float getStartY() {
        return startY;
    }

    public void setStartY(float startY) {
        this.startY = startY;
    }

    public float getStopX() {
        return stopX;
    }

    public void setStopX(float stopX) {
        this.stopX = stopX;
    }

    public float getStopY() {
        return stopY;
    }

    public void setStopY(float stopY) {
        this.stopY = stopY;
    }

    public Path getPath() {
        return path;
    }

    public void setPath(Path path) {
        this.path = path;
    }
}
