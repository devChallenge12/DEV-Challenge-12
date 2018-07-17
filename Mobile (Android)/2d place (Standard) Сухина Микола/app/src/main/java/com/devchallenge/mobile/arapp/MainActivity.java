package com.devchallenge.mobile.arapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnTouchListener, EfectsAdapter.EffectCallback, CallbackFile {

    public static final int MUS_ID = 1;
    public static final int GLASS_ID = 2;
    public static final int LIP_ID = 3;

    private ScaleGestureDetector mScaleDetector;

    public static final String PICTURE = "PICTURE";
    private static final String SAVE_DIALOG = "save_dialog";

    private ImageView faceView;
    private EfectHelper mEfectHelper;

    private RecyclerView efectsList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mScaleDetector = new ScaleGestureDetector(this, new ScaleListener());

        faceView = findViewById(R.id.face_view);
        efectsList = findViewById(R.id.efects_list);
        efectsList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        findViewById(R.id.face_layout).setOnTouchListener(this);

        Bitmap myBitmap = (Bitmap) getIntent().getExtras().get(PICTURE);

        // faceView.setImageBitmap(bitmap);


        mEfectHelper = new EfectHelper(myBitmap,this);


        update();


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.new_face:
                finish();
                break;
            case R.id.save_face:
                SaveDialog.newInstance().show(getSupportFragmentManager(), SAVE_DIALOG);
                break;

            case R.id.undo:
                mEfectHelper.undo();
                update();
                break;
            case R.id.redo:
                mEfectHelper.redo();
                update();
                break;

            case R.id.mus:
                showEfects(MUS_ID);
                break;
            case R.id.lips:
                showEfects(LIP_ID);
                break;
            case R.id.glasses:
                showEfects(GLASS_ID);
                break;
            case R.id.debug_mode:
                mEfectHelper.setDebugMode(!mEfectHelper.isDebugMode());
                update();
                break;

        }

    }

    private void showEfects(int mMusId) {


        RecyclerView.Adapter adapter = null;


        switch (mMusId) {

            case MUS_ID:

                adapter = new EfectsAdapter(this, Arrays.asList(R.drawable.mustache1, R.drawable.mustache6), MUS_ID);
                break;
            case GLASS_ID:
                adapter = new EfectsAdapter(this, Arrays.asList(R.drawable.glasses1, R.drawable.glasses7), GLASS_ID);

                break;

            case LIP_ID:
                adapter = new EfectsAdapter(this, Arrays.asList(R.drawable.lips1, R.drawable.lips3), LIP_ID);

                break;
        }

        efectsList.setVisibility(View.VISIBLE);
        efectsList.setAdapter(adapter);
    }

    private void update() {
        faceView.setImageDrawable(new BitmapDrawable(getResources(), mEfectHelper.getResultBitmap()));
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getPointerCount() > 1) {
            mScaleDetector.onTouchEvent(event);
        } else {

            float screenX = event.getX();
            float screenY = event.getY();
            float viewX = screenX - v.getLeft();
            float viewY = screenY - v.getTop();

            mEfectHelper.moveEfect(viewX / v.getWidth(), viewY / v.getHeight());

            update();
        }

        return true;
    }

    @Override
    public void select(int res, int type) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inMutable = true;

        Bitmap myBitmap = BitmapFactory.decodeResource(
                getResources(),
                res,
                options);

        Efect efec = new Efect(myBitmap, type);
        mEfectHelper.addEfect(efec);

        update();
        efectsList.setVisibility(View.GONE);
    }

    @Override
    public void save(String filename) {
        FileOutputStream out = null;
        try {
            File sdPath = Environment.getExternalStorageDirectory();
            out = new FileOutputStream(sdPath.getAbsolutePath() + "/"+filename+".png");
            mEfectHelper.getResultBitmap().compress(Bitmap.CompressFormat.PNG, 100, out);
            Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Save Error", Toast.LENGTH_SHORT).show();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            mEfectHelper.scaleEfect(detector.getScaleFactor());
            update();
            return true;
        }
    }


}
