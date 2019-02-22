package com.mozia.VmusicBox.analytics;

import android.content.Context;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;
import com.mozia.VmusicBox.R;

import java.util.HashMap;
import java.util.Map;

public final class AnalyticsTrackers {
    private static /* synthetic */ int[] f8x43c96b71;
    private static AnalyticsTrackers sInstance;
    private final Context mContext;
    private final Map<Target, Tracker> mTrackers = new HashMap();

    public enum Target {
        APP
    }

    static /* synthetic */ int[] m31x43c96b71() {
        int[] iArr = f8x43c96b71;
        if (iArr == null) {
            iArr = new int[Target.values().length];
            try {
                iArr[Target.APP.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            f8x43c96b71 = iArr;
        }
        return iArr;
    }

    public static synchronized void initialize(Context context) {
        synchronized (AnalyticsTrackers.class) {
            if (sInstance != null) {
                throw new IllegalStateException("Extra call to initialize analytics trackers");
            }
            sInstance = new AnalyticsTrackers(context);
        }
    }

    public static synchronized AnalyticsTrackers getInstance() {
        AnalyticsTrackers analyticsTrackers;
        synchronized (AnalyticsTrackers.class) {
            if (sInstance == null) {
                throw new IllegalStateException("Call initialize() before getInstance()");
            }
            analyticsTrackers = sInstance;
        }
        return analyticsTrackers;
    }

    private AnalyticsTrackers(Context context) {
        this.mContext = context.getApplicationContext();
    }

    public synchronized Tracker get(Target target) {
        if (!this.mTrackers.containsKey(target)) {
            switch (m31x43c96b71()[target.ordinal()]) {
                case 1:
                    this.mTrackers.put(target, GoogleAnalytics.getInstance(this.mContext).newTracker((int) R.xml.app_tracker));
                    break;
                default:
                    throw new IllegalArgumentException("Unhandled analytics target " + target);
            }
        }
        return (Tracker) this.mTrackers.get(target);
    }
}
