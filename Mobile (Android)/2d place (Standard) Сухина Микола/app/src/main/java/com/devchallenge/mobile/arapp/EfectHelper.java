package com.devchallenge.mobile.arapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.util.SparseArray;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.FaceDetector;

import java.util.LinkedList;
import java.util.List;

public class EfectHelper {
    private boolean debugMode;

    public boolean isDebugMode() {
        return debugMode;
    }

    public void setDebugMode(boolean mDebugMode) {
        debugMode = mDebugMode;
    }

    private Bitmap face;
    private List<Efect> efects;
    private Efect efect;
    private SparseArray<Face> faces;
    private Paint myRectPaint;


    public void addEfect(Efect newEfect) {
        efect = newEfect;
        efects.add(efect);

        //можна лише для одного обличчя ефект
        if (faces.size() != 1)
            return;

        Face face = faces.get(0);

        switch (efect.getType()) {
            case MainActivity.GLASS_ID:
                float efectW = face.getWidth() * 0.8f * (face.getWidth() / efect.getWidth());

                efect.setScale(0.8f * (face.getWidth() / efect.getWidth()));
                float x = face.getPosition().x + face.getWidth()/4 - efectW / 2;
                float y = face.getPosition().y + face.getHeight() / 3;
                efect.setX(x);
                efect.setY(y);
                break;
            case MainActivity.MUS_ID:
                efectW = face.getWidth() * 0.8f * (face.getWidth() / efect.getWidth());

                efect.setScale(0.8f * (face.getWidth() / efect.getWidth()));
                x = face.getPosition().x + face.getWidth()/4 - efectW / 2;
                y = face.getPosition().y + face.getHeight() / 12 * 7;
                efect.setX(x);
                efect.setY(y);
                break;
            case MainActivity.LIP_ID:
                efectW = face.getWidth() * 0.6f * (face.getWidth() / efect.getWidth());

                efect.setScale(0.6f * (face.getWidth() / efect.getWidth()));
                x = face.getPosition().x + face.getWidth()/4 - efectW / 2;
                y = face.getPosition().y + face.getHeight() / 3 * 2;
                efect.setX(x);
                efect.setY(y);
                break;
        }


    }

    public void undo() {
        if (efect == null)
            return;

        int curEfectPos = efects.indexOf(efect);

        if (curEfectPos > 0) {
            efect = efects.get(curEfectPos - 1);
        }

    }

    public void redo() {
        if (efect == null)
            return;

        int curEfectPos = efects.indexOf(efect);


        if (curEfectPos < efects.size() - 1) {
            efect = efects.get(curEfectPos + 1);
        }

    }

    public Bitmap getResultBitmap() {
        Bitmap tempBitmap = Bitmap.createBitmap(face.getWidth(), face.getHeight(), Bitmap.Config.RGB_565);
        Canvas tempCanvas = new Canvas(tempBitmap);
        tempCanvas.drawBitmap(face, 0, 0, null);

        if (debugMode) {

            for (int i = 0; i < faces.size(); i++) {
                Face thisFace = faces.valueAt(i);

                float x1 = thisFace.getPosition().x;
                float y1 = thisFace.getPosition().y;
                float x2 = x1 + thisFace.getWidth();
                float y2 = y1 + thisFace.getHeight();
                tempCanvas.drawRoundRect(new RectF(x1, y1, x2, y2), 2, 2, myRectPaint);
            }
        }

        if (efect != null) {


            int curEfectPos = efects.indexOf(efect);

            for (int i = 0; i <= curEfectPos; i++) {
                Efect efect = efects.get(i);
                Bitmap b = Bitmap.createScaledBitmap(efect.getBitmap(), (int) (efect.getWidth() * efect.getScale()), (int) (efect.getHeight() * efect.getScale()), false);
                tempCanvas.drawBitmap(b, efect.getX(), efect.getY(), null);
            }
        }

        return tempBitmap;
    }

    public EfectHelper(Bitmap mBitmap, Context c) {
        this.face = mBitmap;

        this.efects = new LinkedList<>();

        FaceDetector faceDetector = new
                FaceDetector.Builder(c).setTrackingEnabled(false)
                .build();
        if (!faceDetector.isOperational()) {
            new AlertDialog.Builder(c).setMessage("Could not set up the face detector!").show();
            return;
        }

        Frame frame = new Frame.Builder().setBitmap(face).build();
        faces = faceDetector.detect(frame);

        myRectPaint = new Paint();
        myRectPaint.setStrokeWidth(1);
        myRectPaint.setColor(Color.RED);
        myRectPaint.setStyle(Paint.Style.STROKE);
    }

    public void moveEfect(float x, float y) {
        Log.i("TAG", "x = " + x + " y = " + y);
        if (efect == null)
            return;

        efect.setX(x * face.getWidth());
        efect.setY(y * face.getHeight());
    }

    public void scaleEfect(float coef) {

        if (efect == null)
            return;

        efect.setScale(efect.getScale() * coef);

        Log.i("TAG", coef + " coef");
        Log.i("TAG", efect.getScale() + " scale");
    }


}
