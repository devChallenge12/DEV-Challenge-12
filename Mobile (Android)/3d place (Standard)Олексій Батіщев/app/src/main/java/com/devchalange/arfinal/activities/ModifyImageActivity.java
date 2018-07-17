package com.devchalange.arfinal.activities;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.Toast;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.devchalange.arfinal.GlideApp;
import com.devchalange.arfinal.R;
import com.devchalange.arfinal.views.DecorImageView;
import com.devchalange.arfinal.views.FaceFrameView;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.FaceDetector;
import com.google.android.gms.vision.face.Landmark;

public class ModifyImageActivity extends AppCompatActivity {
    public static final String TAG = ModifyImageActivity.class.getSimpleName();

    public static final String IMAGE_URI_EXTRA_KEY = "ModifyImageActivity.ImageUri";
    private Uri photoURI;
    private ImageView photoImageView;
    private Switch debugModeSwitcher;
    private RelativeLayout parentContainer;
    private FaceFrameView faceFrameView;
    private FaceDetector detector;
    private Frame frame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_image);

        photoURI = getIntent().getParcelableExtra(IMAGE_URI_EXTRA_KEY);

        photoImageView = findViewById(R.id.photoImageView);
        debugModeSwitcher = findViewById(R.id.debugModeSwitcher);
        parentContainer = findViewById(R.id.parentContainer);

        debugModeSwitcher.setOnCheckedChangeListener((compoundButton, active) -> {
            if (faceFrameView != null) {
                faceFrameView.setVisibility(active ? View.VISIBLE : View.GONE);
            }
        });

        detector = new FaceDetector.Builder(this)
                .setTrackingEnabled(false)
                .setLandmarkType(FaceDetector.ALL_LANDMARKS)
                .build();

        loadImage();
    }

    @Override
    protected void onStop() {
        super.onStop();

        detector.release();
        detector = null;
        frame = null;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }

    private void loadImage() {
        GlideApp.with(this)
                .asBitmap()
                .load(photoURI)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .fitCenter()
                .listener(new RequestListener<Bitmap>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                        photoImageView.setImageBitmap(resource);
                        detectFaces(resource);
                        return true;
                    }
                })
                .into(photoImageView);

    }

    private void detectFaces(Bitmap bitmap) {
        frame = new Frame.Builder().setBitmap(bitmap).build();
        SparseArray<Face> faces = detector.detect(frame);

        Log.i(TAG, "detected faces number: " + faces.size());

        if (faces.size() > 0) {
            generateDebugBorderView(faces.get(0));

            loadDecorResBitmap(faces.get(0));

            Log.d(TAG, "Faces detected: " + String.valueOf(faces.size()));
        } else {
            Toast.makeText(this, R.string.error_no_one_face_was_detected, Toast.LENGTH_LONG).show();
        }

    }

    private void loadDecorResBitmap(Face face) {
        if (face == null) {
            return;
        }

        int drawableId = R.drawable.glasses10;

        GlideApp.with(this)
                .asBitmap()
                .load(drawableId)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        addGrassesImage(face, resource);
                    }
                });

    }

    private void addGrassesImage(Face face, Bitmap bitmap) {
        DecorImageView imageView = new DecorImageView(this);
        imageView.setImageBitmap(bitmap);

        int requiredGrassesWidth = (int) (face.getWidth() / 1.5);
        float scale = requiredGrassesWidth / bitmap.getWidth();

        int requiredGrassesHeight = (int) (bitmap.getHeight() * scale);

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(requiredGrassesWidth, requiredGrassesHeight);
        params.leftMargin = (int) (face.getPosition().x + face.getWidth() / 6);
        params.topMargin = (int) (face.getPosition().y + face.getHeight() / 2.4);
        parentContainer.addView(imageView, params);
    }

    private void generateDebugBorderView(Face face) {
        faceFrameView = new FaceFrameView(this, face);
        parentContainer.addView(faceFrameView);
        faceFrameView.update(face);
        faceFrameView.setVisibility(debugModeSwitcher.isActivated() ? View.VISIBLE : View.GONE);
    }

}
