package com.mozia.VmusicBox.setting;

import android.content.Context;
import android.content.SharedPreferences.Editor;
import android.util.Log;

public class SettingManager implements ISettingConstants {
    public static final String DOBAO_SHARPREFS = "dobao_prefs";
    public static final String TAG = SettingManager.class.getSimpleName();

    public static void saveSetting(Context mContext, String mKey, String mValue) {
        Editor editor = mContext.getSharedPreferences(DOBAO_SHARPREFS, 0).edit();
        editor.putString(mKey, mValue);
        editor.commit();
    }

    public static int getSearchType(Context mContext) {
        return Integer.parseInt(getSetting(mContext, "type", String.valueOf(1)));
    }

    public static void setSearchType(Context mContext, int mValue) {
        saveSetting(mContext, "type", String.valueOf(mValue));
    }

    public static String getSetting(Context mContext, String mKey, String mDefValue) {
        return mContext.getSharedPreferences(DOBAO_SHARPREFS, 0).getString(mKey, mDefValue);

    }

    public static boolean getFirstTime(Context mContext) {
        return Boolean.parseBoolean(getSetting(mContext, ISettingConstants.KEY_FIRST_TIME, "false"));
    }

    public static void setFirstTime(Context mContext, boolean mValue) {
        saveSetting(mContext, ISettingConstants.KEY_FIRST_TIME, String.valueOf(mValue));
    }

    public static String getLastKeyword(Context mContext) {
        return getSetting(mContext, ISettingConstants.KEY_LAST_KEYWORD, "");
    }

    public static void setLastKeyword(Context mContext, String mValue) {
        saveSetting(mContext, ISettingConstants.KEY_LAST_KEYWORD, mValue);
    }

    public static String getLanguage(Context mContext) {
        return getSetting(mContext, ISettingConstants.KEY_LANGUAGE, "VN");
    }

    public static void setLanguage(Context mContext, String mValue) {
        saveSetting(mContext, ISettingConstants.KEY_LANGUAGE, mValue);
    }

    public static void setOnline(Context mContext, boolean mValue) {
        saveSetting(mContext, "online", String.valueOf(mValue));
    }

    public static boolean getOnline(Context mContext) {
        return Boolean.parseBoolean(getSetting(mContext, "online", "false"));
    }

    public static boolean getEqualizer(Context mContext) {
        return Boolean.parseBoolean(getSetting(mContext, ISettingConstants.KEY_EQUALIZER_ON, "false"));
    }

    public static void setEqualizer(Context mContext, boolean mValue) {
        saveSetting(mContext, ISettingConstants.KEY_EQUALIZER_ON, String.valueOf(mValue));
    }

    public static String getEqualizerPreset(Context mContext) {
        return getSetting(mContext, ISettingConstants.KEY_EQUALIZER_PRESET, "0");
    }

    public static void setEqualizerPreset(Context mContext, String mValue) {
        saveSetting(mContext, ISettingConstants.KEY_EQUALIZER_PRESET, mValue);
    }

    public static String getEqualizerParams(Context mContext) {
        return getSetting(mContext, ISettingConstants.KEY_EQUALIZER_PARAMS, "");
    }

    public static void setEqualizerParams(Context mContext, String mValue) {
        saveSetting(mContext, ISettingConstants.KEY_EQUALIZER_PARAMS, mValue);
    }

    public static boolean getPlayingState(Context mContext) {
        return Boolean.parseBoolean(getSetting(mContext, ISettingConstants.KEY_STATE, "false"));
    }

    public static void setPlayingState(Context mContext, boolean mValue) {
        saveSetting(mContext, ISettingConstants.KEY_STATE, String.valueOf(mValue));
    }

    public static void setRepeat(Context mContext, boolean mValue) {
        saveSetting(mContext, ISettingConstants.KEY_REPEAT, String.valueOf(mValue));
    }

    public static boolean getRepeat(Context mContext) {
        return Boolean.parseBoolean(getSetting(mContext, ISettingConstants.KEY_REPEAT, "false"));
    }

    public static void setShuffle(Context mContext, boolean mValue) {
        saveSetting(mContext, ISettingConstants.KEY_SHUFFLE, String.valueOf(mValue));
    }

    public static boolean getShuffle(Context mContext) {
        return Boolean.parseBoolean(getSetting(mContext, ISettingConstants.KEY_SHUFFLE, "false"));
    }
}
