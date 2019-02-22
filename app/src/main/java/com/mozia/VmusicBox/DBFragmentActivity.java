package com.mozia.VmusicBox;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.provider.MediaStore.Audio.Media;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest.Builder;
import com.google.android.gms.ads.InterstitialAd;
import com.mozia.VmusicBox.constants.ICloudMusicPlayerConstants;
import com.mozia.VmusicBox.setting.SettingManager;
import com.ypyproductions.abtractclass.fragment.DBFragment;
import com.ypyproductions.dialog.utils.AlertDialogUtils;
import com.ypyproductions.dialog.utils.AlertDialogUtils.IOnDialogListener;
import com.ypyproductions.dialog.utils.IDialogFragmentListener;
import com.ypyproductions.task.IDBCallback;
import com.ypyproductions.task.IDBConstantURL;
import com.ypyproductions.utils.ResolutionUtils;

import java.util.ArrayList;
import java.util.Random;

public class DBFragmentActivity extends AppCompatActivity implements IDBConstantURL, IDialogFragmentListener, ICloudMusicPlayerConstants {
    private static final String MY_PREFS_NAME = "MIXAGRAMPREF";
    public static final String TAG = DBFragmentActivity.class.getSimpleName();
    private Dialog mDialog;
    private InterstitialAd mInterstitial;
    public ArrayList<Fragment> mListFragments;
    private ProgressDialog mProgressDialog;
    private Random mRando;
    public Typeface mTypefaceBold;
    public Typeface mTypefaceLight;
    public Typeface mTypefaceLogo;
    public Typeface mTypefaceNormal;
    private int screenHeight;
    private int screenWidth;

    class C05017 implements OnKeyListener {
        C05017() {
        }

        public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
            return false;
        }
    }

    class C05028 implements OnKeyListener {
        C05028() {
        }

        public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
            if (keyCode == 4) {
                return true;
            }
            return false;
        }
    }

    class C07991 extends AdListener {
        C07991() {
        }

        public void onAdLoaded() {
            super.onAdLoaded();
            DBFragmentActivity.this.mInterstitial.show();
        }
    }

    class C08046 implements IOnDialogListener {
        C08046() {
        }

        public void onClickButtonPositive() {
            DBFragmentActivity.this.onDestroyData();
            DBFragmentActivity.this.finish();
            showIntertestialAds();
        }

        public void onClickButtonNegative() {
            DBFragmentActivity.this.mDialog.dismiss();
        }
    }

    public /* bridge */ /* synthetic */ View onCreateView(View view, String str, Context context, AttributeSet attributeSet) {
        return super.onCreateView(view, str, context, attributeSet);
    }

    public /* bridge */ /* synthetic */ View onCreateView(String str, Context context, AttributeSet attributeSet) {
        return super.onCreateView(str, context, attributeSet);
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFormat(1);
        getWindow().setSoftInputMode(3);
        createProgressDialog();
        this.mTypefaceNormal = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf");
        this.mTypefaceLight = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Light.ttf");
        this.mTypefaceBold = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Bold.ttf");
        this.mTypefaceLogo = Typeface.createFromAsset(getAssets(), "fonts/Biko_Regular.otf");
        int[] mRes = ResolutionUtils.getDeviceResolution(this);
        if (mRes != null && mRes.length == 2) {
            this.screenWidth = mRes[0];
            this.screenHeight = mRes[1];
        }
        this.mRando = new Random();
    }

    public void createIntertestialAds() {
        if (SettingManager.getOnline(this)) {
            this.mInterstitial = new InterstitialAd(getApplicationContext());
            this.mInterstitial.setAdUnitId(ICloudMusicPlayerConstants.ADMOB_ID_INTERTESTIAL);
            this.mInterstitial.loadAd(new Builder().build());
        }
    }

    public void saveSharedPreferences(String title, int value) {
        Editor editor = getSharedPreferences(MY_PREFS_NAME, 0).edit();
        editor.putInt(title, value);
        editor.commit();
    }

    public int getSharedPref(String title) {
        return getSharedPreferences(MY_PREFS_NAME, 0).getInt(title, 0);
    }

    public void showIntertestialAds() {
        if (this.mInterstitial != null) {
            if (this.mInterstitial.isLoaded()) {
                this.mInterstitial.show();
            } else {
                this.mInterstitial.setAdListener(new C07991());
            }
        }
        createIntertestialAds();
    }

    public void deleteSongFromMediaStore(long id) {
        try {
            getContentResolver().delete(ContentUris.withAppendedId(Media.EXTERNAL_CONTENT_URI, id), null, null);
            showIntertestialAds();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode != 4) {
            return super.onKeyDown(keyCode, event);
        }
        showQuitDialog();
        return true;
    }

    public void showDialogFragment(int idDialog) {
        FragmentManager mFragmentManager = getSupportFragmentManager();
        switch (idDialog) {
            case 1:
                createWarningDialog(1, R.string.title_warning, R.string.info_lose_internet).show(mFragmentManager, "DIALOG_LOSE_CONNECTION");
                return;
            case 2:
                createWarningDialog(2, R.string.title_warning, R.string.info_empty).show(mFragmentManager, "DIALOG_EMPTY");
                return;
            case 19:
                createWarningDialog(19, R.string.title_warning, R.string.info_server_error).show(mFragmentManager, "DIALOG_SEVER_ERROR");
                return;
            default:
                return;
        }
    }

    public DialogFragment createWarningDialog(int idDialog, int titleId, int messageId) {
        return DBAlertFragment.newInstance(idDialog, 17301543, titleId, 17039370, messageId);
    }

    public void showWarningDialog(int titleId, int messageId) {
        AlertDialogUtils.createInfoDialog((Context) this, 0, titleId, (int) R.string.title_ok, messageId, null).show();
    }

    public void showWarningDialog(int titleId, String message) {
        AlertDialogUtils.createInfoDialog((Context) this, 0, titleId, (int) R.string.title_ok, message, null).show();
    }

    public void showInfoDialog(int titleId, String message) {
        AlertDialogUtils.createInfoDialog((Context) this, 0, titleId, (int) R.string.title_ok, message, null).show();
    }

    public void showInfoDialog(int titleId, String message, final IDBCallback mDBCallback) {
        AlertDialogUtils.createInfoDialog((Context) this, 0, titleId, (int) R.string.title_ok, message, new IOnDialogListener() {
            public void onClickButtonPositive() {
                if (mDBCallback != null) {
                    mDBCallback.onAction();
                }
            }

            public void onClickButtonNegative() {
                if (mDBCallback != null) {
                    mDBCallback.onAction();
                }
            }
        }).show();
    }

    public void showFullDialog(int titleId, int message, int idPositive, int idNegative, final IDBCallback mDBCallback) {
        AlertDialogUtils.createFullDialog((Context) this, -1, titleId, idPositive, idNegative, message, new IOnDialogListener() {
            public void onClickButtonPositive() {
                if (mDBCallback != null) {
                    mDBCallback.onAction();
                }
            }

            public void onClickButtonNegative() {
            }
        }).show();
    }

    public void showFullDialog(int titleId, String message, int idPositive, int idNegative, final IDBCallback mDBCallback) {
        AlertDialogUtils.createFullDialog((Context) this, -1, titleId, idPositive, idNegative, message, new IOnDialogListener() {
            public void onClickButtonPositive() {
                if (mDBCallback != null) {
                    mDBCallback.onAction();
                }
            }

            public void onClickButtonNegative() {
            }
        }).show();
    }

    public void showInfoDialog(int titleId, int message, final IDBCallback mDBCallback) {
        AlertDialogUtils.createInfoDialog((Context) this, 0, titleId, (int) R.string.title_ok, message, new IOnDialogListener() {
            public void onClickButtonPositive() {
                if (mDBCallback != null) {
                    mDBCallback.onAction();
                }
            }

            public void onClickButtonNegative() {
                if (mDBCallback != null) {
                    mDBCallback.onAction();
                }
            }
        }).show();
    }

    public void showQuitDialog() {
        int index = this.mRando.nextInt(5);
        this.mDialog = AlertDialogUtils.createFullDialog((Context) this, (int) R.drawable.ic_launcher, (int) R.string.title_confirm, (int) R.string.title_yes, (int) R.string.title_cancel, (int) R.string.info_close_app, new C08046());
        this.mDialog.setCanceledOnTouchOutside(true);
        this.mDialog.setOnKeyListener(new C05017());
        this.mDialog.setCancelable(true);
        this.mDialog.show();
    }

    private void createProgressDialog() {
        this.mProgressDialog = new ProgressDialog(this, R.style.MyThemeProgressBar);
        this.mProgressDialog.setIndeterminate(false);
//        this.mProgressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        this.mProgressDialog.setIndeterminateDrawable(getResources().getDrawable(R.drawable.progress_dialog_icon));

//        this.mProgressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Large);
        this.mProgressDialog.setCancelable(false);
        this.mProgressDialog.setOnKeyListener(new C05028());
    }

    public void showProgressDialog() {
        if (this.mProgressDialog != null) {
            // TODO: 8/9/17  nhan rem 
//            this.mProgressDialog.setMessage(getString(R.string.loading));
            if (!this.mProgressDialog.isShowing()) {
                this.mProgressDialog.show();
            }
        }
    }

    public void showProgressDialog(int messageId) {
        if (this.mProgressDialog != null) {
            this.mProgressDialog.setMessage(getString(messageId));
            if (!this.mProgressDialog.isShowing()) {
                this.mProgressDialog.show();
            }
        }
    }

    public void showProgressDialog(String message) {
        if (this.mProgressDialog != null) {
            this.mProgressDialog.setMessage(message);
            if (!this.mProgressDialog.isShowing()) {
                this.mProgressDialog.show();
            }
        }
    }

    public void dimissProgressDialog() {
        try {
            if (this.mProgressDialog != null) {
                this.mProgressDialog.dismiss();
            }
        } catch (Exception e) {
        }
    }

    public int getScreenWidth() {
        return this.screenWidth;
    }

    public int getScreenHeight() {
        return this.screenHeight;
    }

    public void showToast(int resId) {
        showToast(getString(resId));
    }

    public void showToast(String message) {
        Toast toast = Toast.makeText(this, message, 0);
        toast.setGravity(17, 0, 0);
        toast.show();
    }

    public void showToastWithLongTime(int resId) {
        showToastWithLongTime(getString(resId));
    }

    public void showToastWithLongTime(String message) {
        Toast toast = Toast.makeText(this, message, 1);
        toast.setGravity(17, 0, 0);
        toast.show();
    }

    public void doPositiveClick(int idDialog) {
        switch (idDialog) {
            case 8:
                onDestroyData();
                finish();
                return;
            default:
                return;
        }
    }

    public void doNegativeClick(int idDialog) {
    }

    public void onDestroyData() {
    }

    public void createArrayFragment() {
        this.mListFragments = new ArrayList();
    }

    public void addFragment(Fragment mFragment) {
        if (mFragment != null && this.mListFragments != null) {
            synchronized (this.mListFragments) {
                this.mListFragments.add(mFragment);
            }
        }
    }

    public void showDialogTurnOnInternetConnection(final IDBCallback mCallback) {
        AlertDialogUtils.createFullDialog((Context) this, 0, (int) R.string.title_warning, (int) R.string.title_settings, (int) R.string.title_cancel, (int) R.string.info_lose_internet, new IOnDialogListener() {
            public void onClickButtonPositive() {
                DBFragmentActivity.this.startActivity(new Intent("android.settings.WIFI_SETTINGS"));
                if (mCallback != null) {
                    mCallback.onAction();
                }
            }

            public void onClickButtonNegative() {
            }
        }).show();
    }

    public boolean backStack(IDBCallback mCallback) {
        if (this.mListFragments != null && this.mListFragments.size() > 0) {
            int count = this.mListFragments.size();
            if (count > 0) {
                synchronized (this.mListFragments) {
                    Fragment mFragment = (Fragment) this.mListFragments.remove(count - 1);
                    if (mFragment == null || !(mFragment instanceof DBFragment)) {
                    } else {
                        ((DBFragment) mFragment).backToHome(this);
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
