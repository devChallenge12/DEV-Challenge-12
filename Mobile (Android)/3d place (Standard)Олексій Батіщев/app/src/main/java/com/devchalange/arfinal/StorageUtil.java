package com.devchalange.arfinal;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class StorageUtil {
    public static final String TAG = StorageUtil.class.getSimpleName();

    private static final String TEMP_PATH = "temp";
    private static final String IMAGE_EXT = ".png";

    private static File tempDir;

    public static File getTempDir(Context context) {
        if (tempDir == null) {
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                tempDir = context.getExternalFilesDir(TEMP_PATH);
            } else {
                tempDir = context.getCacheDir();
            }
        }

        if (tempDir != null && !tempDir.exists()) {
            tempDir.mkdirs();
        }

        return tempDir;
    }

    private static String generateName() {
        return new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
    }

    public static File createTempImageFile(Context context) throws IOException {
        String imageName = generateName();
        File storageDir = StorageUtil.getTempDir(context);
        File image = null;
        if (storageDir != null) {
            image = File.createTempFile(
                    imageName,
                    IMAGE_EXT,
                    storageDir
            );
        }

        return image;
    }

}
