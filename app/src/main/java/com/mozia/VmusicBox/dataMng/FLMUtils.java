package com.mozia.VmusicBox.dataMng;

import android.content.Context;
import android.os.Build.VERSION;
import android.os.Environment;

import java.io.File;

public class FLMUtils {
    public static final int IO_BUFFER_SIZE = 8192;

    private FLMUtils() {
    }

    public static boolean isExternalStorageRemovable() {
        if (VERSION.SDK_INT >= 9) {
            return Environment.isExternalStorageRemovable();
        }
        return true;
    }

    public static File getExternalCacheDir(Context context) {
        if (hasExternalCacheDir()) {
            return context.getExternalCacheDir();
        }
        return new File(new StringBuilder(String.valueOf(Environment.getExternalStorageDirectory().getPath())).append("/Android/data/" + context.getPackageName() + "/cache/").toString());
    }

    public static boolean hasExternalCacheDir() {
        return VERSION.SDK_INT >= 8;
    }
}
