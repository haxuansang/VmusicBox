package com.mozia.VmusicBox.app;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders.EventBuilder;
import com.google.android.gms.analytics.HitBuilders.ExceptionBuilder;
import com.google.android.gms.analytics.HitBuilders.ScreenViewBuilder;
import com.google.android.gms.analytics.StandardExceptionParser;
import com.google.android.gms.analytics.Tracker;
import com.mozia.VmusicBox.analytics.AnalyticsTrackers;
import com.mozia.VmusicBox.analytics.AnalyticsTrackers.Target;
import com.onesignal.OneSignal;

import java.util.Random;

public class Navigator extends Application {
    public static final String TAG = Navigator.class.getSimpleName();
    public static int clicksForFullScreenAds = 3;
    private static Navigator mInstance;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    public void onCreate() {
        super.onCreate();
        mInstance = this;
        OneSignal.startInit(this).init();
        AnalyticsTrackers.initialize(this);
        AnalyticsTrackers.getInstance().get(Target.APP);
        Fresco.initialize(this);
    }

    public static int randInt(int min, int max) {
        return new Random().nextInt(max - min) + min;
    }

    public static synchronized Navigator getInstance() {
        Navigator navigator;
        synchronized (Navigator.class) {
            navigator = mInstance;
        }
        return navigator;
    }

    public synchronized Tracker getGoogleAnalyticsTracker() {
        return AnalyticsTrackers.getInstance().get(Target.APP);
    }

    public void trackScreenView(String screenName) {
        Tracker t = getGoogleAnalyticsTracker();
        t.setScreenName(screenName);
        t.send(new ScreenViewBuilder().build());
        GoogleAnalytics.getInstance(this).dispatchLocalHits();
    }

    public void trackException(Exception e) {
        if (e != null) {
            getGoogleAnalyticsTracker().send(new ExceptionBuilder().setDescription(new StandardExceptionParser(this, null).getDescription(Thread.currentThread().getName(), e)).setFatal(false).build());
        }
    }

    public void trackEvent(String category, String action, String label) {
        getGoogleAnalyticsTracker().send(new EventBuilder().setCategory(category).setAction(action).setLabel(label).build());
    }
}
