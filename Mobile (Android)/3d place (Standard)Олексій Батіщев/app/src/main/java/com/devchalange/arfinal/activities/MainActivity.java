package com.devchalange.arfinal.activities;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.devchalange.arfinal.R;
import com.devchalange.arfinal.StorageUtil;

import java.io.File;
import java.io.IOException;


public class MainActivity extends AppCompatActivity {
    public static final String TAG = MainActivity.class.getSimpleName();

    public static final int REQUEST_PICK_IMAGE = 116;
    public static final int REQUEST_TAKE_PHOTO = 117;

    public static final int READ_STORAGE_PERM = 1001;
    public static final int CAMERA_PERM = 1001;

    private Uri photoURI;
    private Button takePhotoButton;
    private Button pickImageButton;
    private Button modifyButton;
    private ImageView photoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        modifyButton = findViewById(R.id.modifyButton);
        pickImageButton = findViewById(R.id.pickImageButton);
        takePhotoButton = findViewById(R.id.takePhotoButton);
        photoView = findViewById(R.id.photoView);

        pickImageButton.setOnClickListener(view -> pickImageWithPermissions());
        takePhotoButton.setOnClickListener(view -> takePhotoWithPermissions());
        modifyButton.setOnClickListener(view -> {
                openModifyImageActivity();
        });

    }

    private void openModifyImageActivity() {
        Intent intent = new Intent(this, ModifyImageActivity.class);
        intent.putExtra(ModifyImageActivity.IMAGE_URI_EXTRA_KEY, photoURI);
        startActivity(intent);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent resultData) {
        super.onActivityResult(requestCode, resultCode, resultData);
        if (requestCode == REQUEST_PICK_IMAGE && resultCode == Activity.RESULT_OK) {
            Uri uri = null;
            if (resultData != null && resultData.getData() != null) {
                uri = resultData.getData();
                Log.i(TAG, "Pick image success, Uri: " + uri.toString());
                onImagePicked(uri);
            } else {
                Log.i(TAG, "Pick image failed");
            }
        }

        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
            onImagePicked(photoURI);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {

            case CAMERA_PERM: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                        && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    doTakePhoto();
                } else {
                    Log.d(TAG, "REQUEST_CAMERA_PERMISSION Permission has been denied by user");
                }

                break;
            }

            case REQUEST_PICK_IMAGE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    doPickImage();
                } else {
                    Log.d(TAG, "Permissions has been denied by user");
                }

                break;
            }
        }
    }


    private void pickImageWithPermissions() {
        if (checkReadStoragePermissions()) {
            doPickImage();
        } else {
            requestReadStoragePermissions(READ_STORAGE_PERM);
        }
    }

    private void takePhotoWithPermissions() {
        if (checkCamePermissions()) {
           doTakePhoto();
        } else {
            requestCameraPermissions(CAMERA_PERM);
        }
    }


    private boolean checkWriteStoragePermissions() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED;
    }

    private boolean checkReadStoragePermissions() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED;
    }

    private void requestReadStoragePermissions(int requestCode) {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, requestCode);
    }

    private void requestWriteStoragePermissions(int requestCode) {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, requestCode);
    }

    private boolean checkCamePermissions() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED;
    }

    private void requestCameraPermissions(int requestCode) {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, requestCode);
    }

    private void doTakePhoto() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;

            try {
                photoFile = StorageUtil.createTempImageFile(this);
            } catch (IOException e) {
                Log.e(TAG, "doTakePhoto()", e);
            }

            if (photoFile != null) {
                photoURI = FileProvider.getUriForFile(this, "com.devchalange.arfinal", photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            } else {

                Toast.makeText(this, R.string.error_fail_to_create_photo_file, Toast.LENGTH_SHORT).show();
            }

        } else {
            Toast.makeText(this, R.string.error_no_camera_app, Toast.LENGTH_SHORT).show();
        }
    }


    private void doPickImage() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_PICK_IMAGE);
    }

    public void onImagePicked(Uri uri) {
        if (uri != null) {
            photoURI = uri;

            Glide.with(this).load(photoURI).into(photoView);
        }

        modifyButton.setEnabled(photoURI != null);
    }
}
