package com.mozia.VmusicBox;

import android.Manifest;
import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mozia.VmusicBox.app.AppRater;
import com.mozia.VmusicBox.constants.ICloudMusicPlayerConstants;
import com.mozia.VmusicBox.dataMng.FLMUtils;
import com.mozia.VmusicBox.dataMng.JsonParsingUtils;
import com.mozia.VmusicBox.dataMng.TotalDataManager;
import com.mozia.VmusicBox.object.GenreObject;
import com.mozia.VmusicBox.setting.SettingManager;
import com.ypyproductions.task.DBTask;
import com.ypyproductions.task.IDBCallback;
import com.ypyproductions.task.IDBTaskListener;
import com.ypyproductions.utils.DBLog;
import com.ypyproductions.utils.IOUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Locale;

@SuppressLint({"NewApi"})
@TargetApi(25)
public class SplashActivity extends DBFragmentActivity {
    public static final String TAG = SplashActivity.class.getSimpleName();
    private boolean isLoading;
    private boolean isPressBack;
    protected boolean isShowingDialog;
    private boolean isStartAnimation;
    private DBTask mDBTask;
    private Handler mHandler = new Handler();
    private ImageView mImgLogo;
    private ProgressBar mProgressBar;
    private TextView mTvAppName;
    private final int requestcode = 100;

    public /* bridge */ /* synthetic */ View onCreateView(View view, String str, Context context, AttributeSet attributeSet) {
        return super.onCreateView(view, str, context, attributeSet);
    }

    public /* bridge */ /* synthetic */ View onCreateView(String str, Context context, AttributeSet attributeSet) {
        return super.onCreateView(str, context, attributeSet);
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(1);
        getWindow().setFlags(1024, 1024);
        setContentView(R.layout.splash);

        this.mProgressBar = (ProgressBar) findViewById(R.id.progressBar1);
        this.mTvAppName = (TextView) findViewById(R.id.tv_app_name);
        this.mImgLogo = (ImageView) findViewById(R.id.img_logo);
        this.mTvAppName.setTypeface(this.mTypefaceNormal);
        this.mProgressBar.setVisibility(View.VISIBLE);
        this.mTvAppName.setVisibility(View.VISIBLE);
        DBLog.setDebug(false);
        SettingManager.setOnline(this, true);

        int permissionCheck = ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE,android.Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, MY_PERMISSIONS_REQUEST_READ_MEDIA);
        } else {

            AppRater.isReadExternalSDCard = true;
            startLoadFavorite();
        }

    }




    final int MY_PERMISSIONS_REQUEST_READ_MEDIA = 1000;

    protected void onResume() {
        super.onResume();

    }

    private File getDiskCacheDir(Context context, String uniqueName) {
        String cachePath;
        if ("mounted".equals(Environment.getExternalStorageState()) || !FLMUtils.isExternalStorageRemovable()) {
            cachePath = FLMUtils.getExternalCacheDir(context).getPath();
        } else {
            cachePath = context.getCacheDir().getPath();
        }
        return new File(new StringBuilder(String.valueOf(cachePath)).append(File.separator).append(uniqueName).toString());
    }

    public void startLoadFavorite() {
        final File mFile = new File(Environment.getExternalStorageDirectory(), ICloudMusicPlayerConstants.NAME_FOLDER_CACHE);
        if (!mFile.exists()) {
            mFile.mkdirs();
        }
        if (!(mFile == null || mFile.exists())) {
            mFile.mkdirs();
        }
        mDBTask = new DBTask(new IDBTaskListener() {
            public void onPreExcute() {
            }

            public void onDoInBackground() {
                TotalDataManager.getInstance().readLibraryTrack(SplashActivity.this);
                String fileName = "";
                // TODO: 8/10/17  set language splash 
                if (Locale.getDefault().getCountry().toLowerCase(Locale.US).equalsIgnoreCase("VN")) {
                    SettingManager.setLanguage(SplashActivity.this, "VN");
                    fileName = String.format(ICloudMusicPlayerConstants.FILE_GENRE, new Object[]{"VN"});
                } else {
                    SettingManager.setLanguage(SplashActivity.this, "US");
                    fileName = String.format(ICloudMusicPlayerConstants.FILE_GENRE, new Object[]{"en"});
                }
                String data = IOUtils.readStringFromAssets(SplashActivity.this, fileName);
                //DBLog.m25d(SplashActivity.TAG, "=========>data=" + data);
                ArrayList<GenreObject> mListGenres = JsonParsingUtils.parsingGenreObject(data);
                if (mListGenres != null) {
                    TotalDataManager.getInstance().setListGenreObjects(mListGenres);
                }
                TotalDataManager.getInstance().readSavedTrack(SplashActivity.this, mFile);
                TotalDataManager.getInstance().readPlaylistCached(SplashActivity.this, mFile);
            }

            public void onPostExcute() {
                new Handler().postDelayed(new Runnable(){
                    @Override
                    public void run() {
                        SplashActivity.this.mProgressBar.setVisibility(View.INVISIBLE);
                        SplashActivity.this.startActivity(new Intent(SplashActivity.this, MainActivity.class));
                        SplashActivity.this.finish();
                    }
                }, 1000);

            }
        });
        mDBTask.execute();
    }

    @TargetApi(12)
    private void startAnimationLogo(final IDBCallback mCallback) {
        if (!this.isStartAnimation) {
            this.isStartAnimation = true;
            this.mProgressBar.setVisibility(View.INVISIBLE);
            this.mImgLogo.setRotationY(-180.0f);
            ViewPropertyAnimator localViewPropertyAnimator = this.mImgLogo.animate().rotationY(0.0f).setDuration(1000).setInterpolator(new AccelerateDecelerateInterpolator());
            localViewPropertyAnimator.setListener(new AnimatorListener() {
                public void onAnimationStart(Animator animation) {
                }

                public void onAnimationRepeat(Animator animation) {
                }

                public void onAnimationEnd(Animator animation) {
                    if (mCallback != null) {
                        mCallback.onAction();
                    }
                }

                public void onAnimationCancel(Animator animation) {
                    if (mCallback != null) {
                        mCallback.onAction();
                    }
                }
            });
            localViewPropertyAnimator.start();
        } else if (mCallback != null) {
            mCallback.onAction();
        }
    }

    protected void onDestroy() {
        super.onDestroy();
        this.mHandler.removeCallbacksAndMessages(null);
    }

    public void onDestroyData() {
        super.onDestroyData();
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode != 4) {
            return super.onKeyDown(keyCode, event);
        }
        if (this.isPressBack) {
            finish();
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_MEDIA: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission granted and now can proceed
                    if (!this.isLoading) {
                        this.isLoading = true;
                        this.mProgressBar.setVisibility(View.VISIBLE);
                        AppRater.isReadExternalSDCard = true;
                        startLoadFavorite();
                    }

                } else {
                    AppRater.isReadExternalSDCard = false;
                    new Handler().postDelayed(new Runnable(){
                        @Override
                        public void run() {
                            SplashActivity.this.mProgressBar.setVisibility(View.INVISIBLE);
                            SplashActivity.this.startActivity(new Intent(SplashActivity.this, MainActivity.class));
                            SplashActivity.this.finish();
                        }
                    }, 1000);

//                    SplashActivity.this.mProgressBar.setVisibility(4);
//                    SplashActivity.this.startActivity(new Intent(SplashActivity.this, MainActivity.class));
//                    SplashActivity.this.finish();
                }
                return;
            }
        }
    }
}
