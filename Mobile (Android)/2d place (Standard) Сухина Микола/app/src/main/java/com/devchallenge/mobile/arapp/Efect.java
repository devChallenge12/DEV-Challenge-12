package com.devchallenge.mobile.arapp;

import android.graphics.Bitmap;

public class Efect {
    private float x, y;
    private int width, height;
    private float scale;
    private Bitmap bitmap;
    private int type;

    public int getType() {
        return type;
    }

    public Efect(Bitmap mBitmap, int mType) {
        type = mType;
        bitmap = mBitmap;
        width = bitmap.getWidth();
        height = bitmap.getHeight();
        scale = 1;

    }

    public void setX(float mX) {
        x = mX;
    }

    public void setY(float mY) {
        y = mY;
    }

    public float getX() {
        return x;

    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int mWidth) {
        width = mWidth;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int mHeight) {
        height = mHeight;
    }


    public float getScale() {
        return scale;
    }

    public void setScale(float mScale) {
        scale = mScale;
    }

    public float getY() {

        return y;
    }

    public Bitmap getBitmap() {

        return bitmap;
    }

    public void setBitmap(Bitmap mBitmap) {
        bitmap = mBitmap;
    }
}
