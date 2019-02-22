package com.mozia.VmusicBox;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.google.android.gms.ads.AdRequest.Builder;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.mozia.VmusicBox.constants.ICloudMusicPlayerConstants;
import com.mozia.VmusicBox.setting.SettingManager;
import com.ypyproductions.abtractclass.fragment.IDBFragmentConstants;
import com.ypyproductions.utils.ApplicationUtils;
import com.ypyproductions.utils.StringUtils;

public class ShowUrlActivity extends DBFragmentActivity implements ICloudMusicPlayerConstants, IDBFragmentConstants {
    public static final String TAG = ShowUrlActivity.class.getSimpleName();
    private AdView adView;
    private String mNameHeader;
    private ProgressBar mProgressBar;
    private String mUrl;
    private WebView mWebViewShowPage;

    class C05171 extends WebViewClient {
        C05171() {
        }

        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            ShowUrlActivity.this.mProgressBar.setVisibility(8);
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
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
        setContentView(R.layout.layout_show_url);
        Intent args = getIntent();
        if (args != null) {
            this.mUrl = args.getStringExtra("url");
            this.mNameHeader = args.getStringExtra(ICloudMusicPlayerConstants.KEY_HEADER);
            //DBLog.m25d(TAG, "===========>url=" + this.mUrl);
        }
        if (!StringUtils.isEmptyString(this.mNameHeader)) {
            setTitle(this.mNameHeader);
        }
        this.mProgressBar = (ProgressBar) findViewById(R.id.progressBar1);
        this.mProgressBar.setVisibility(0);
        this.mWebViewShowPage = (WebView) findViewById(R.id.webview);
        this.mWebViewShowPage.getSettings().setJavaScriptEnabled(true);
        this.mWebViewShowPage.setWebViewClient(new C05171());
        this.mWebViewShowPage.loadUrl(this.mUrl);
        setUpLayoutAdmob();
    }

    private void setUpLayoutAdmob() {
        RelativeLayout layout = (RelativeLayout) findViewById(R.id.layout_ad);
        if (ApplicationUtils.isOnline(this)) {
            this.adView = new AdView(this);
            this.adView.setAdUnitId(ICloudMusicPlayerConstants.ADMOB_ID_BANNER);
            this.adView.setAdSize(AdSize.BANNER);
            layout.addView(this.adView);
            this.adView.loadAd(new Builder().build());
            return;
        }
        layout.setVisibility(8);
    }

    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (this.mWebViewShowPage != null) {
            this.mWebViewShowPage.destroy();
        }
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 16908332:
                backToHome();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void backToHome() {
        if (SettingManager.getOnline(this)) {
            finish();
            return;
        }
        startActivity(new Intent(this, SplashActivity.class));
        finish();
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode != 4) {
            return super.onKeyDown(keyCode, event);
        }
        if (this.mWebViewShowPage.canGoBack()) {
            this.mWebViewShowPage.goBack();
        } else {
            backToHome();
        }
        return true;
    }
}
