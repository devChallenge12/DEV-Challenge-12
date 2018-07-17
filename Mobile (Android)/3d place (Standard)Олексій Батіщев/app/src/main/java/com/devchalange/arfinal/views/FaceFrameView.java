package com.devchalange.arfinal.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.view.View;

import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.Landmark;


public class FaceFrameView extends View {
    Face face;

    public FaceFrameView(Context context, Face face) {
        super(context);
        this.face = face;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Paint paint = new Paint();
        paint.setColor(Color.CYAN);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(10);

        for (Landmark landmark : face.getLandmarks()) {
            int cx = (int) (landmark.getPosition().x);
            int cy = (int) (landmark.getPosition().y);
            canvas.drawCircle(cx, cy, 10, paint);
        }

        Path path = new Path();
        path.moveTo(face.getPosition().x, face.getPosition().y);
        path.lineTo(face.getPosition().x + face.getWidth(), face.getPosition().y);
        path.lineTo(face.getPosition().x + face.getWidth(), face.getPosition().y + face.getHeight());
        path.lineTo(face.getPosition().x, face.getPosition().y + face.getHeight());
        path.close();

        Paint border = new Paint();
        border.setColor(Color.BLUE);
        border.setStyle(Paint.Style.STROKE);
        border.setStrokeWidth(8.0f);
        canvas.drawPath(path, border);
    }

    public void update(Face face) {
        this.face = face;
        postInvalidate();
    }

}
