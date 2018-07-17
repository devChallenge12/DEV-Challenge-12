package com.devchalange.arfinal.views;

import android.content.Context;
import android.view.MotionEvent;


public class DecorImageView extends android.support.v7.widget.AppCompatImageView {
    private float dx, dy;
    private boolean isMoving = false;

    public DecorImageView(Context context) {
        super(context);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                dx = getX() - event.getRawX();
                dy = getY() - event.getRawY();

                break;

            case MotionEvent.ACTION_MOVE:
                isMoving = true;

                animate()
                        .x(event.getRawX() + dx)
                        .y(event.getRawY() + dy)
                        .setDuration(0)
                        .start();
                break;

            case MotionEvent.ACTION_UP:
                if (isMoving) {
                    isMoving = false;
                } else {
                    performClick();
                    return super.onTouchEvent(event);
                }
                break;

            default:
                performClick();
                return super.onTouchEvent(event);
        }

        return true;
    }

    @Override
    public boolean performClick() {
        return super.performClick();
    }

}
