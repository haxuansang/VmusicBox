package com.mozia.VmusicBox.dataMng;

import com.mozia.VmusicBox.DBAlertFragment;
import com.mozia.VmusicBox.constants.ICloudMusicPlayerConstants;
import com.mozia.VmusicBox.object.GenreObject;
import com.mozia.VmusicBox.object.PlaylistObject;
import com.mozia.VmusicBox.soundclound.ISoundCloundConstants;
import com.mozia.VmusicBox.soundclound.SoundCloundJsonParsingUtils;
import com.mozia.VmusicBox.soundclound.object.TrackObject;
import com.ypyproductions.abtractclass.fragment.IDBFragmentConstants;
import com.ypyproductions.utils.DateTimeUtils;
import com.ypyproductions.utils.StringUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;

public class JsonParsingUtils implements ICloudMusicPlayerConstants {
    public static final String TAG = JsonParsingUtils.class.getSimpleName();
    public static final String TAG_IMAGE = "image";
    public static final String TAG_NAME = "name";
    public static final String TAG_STATUS = "status";

    public static ArrayList<GenreObject> parsingGenreObject(String data) {
        if (!StringUtils.isEmptyString(data)) {
            try {
                JSONArray mJsonArray = new JSONArray(data);
                int size = mJsonArray.length();
                if (size > 0) {
                    ArrayList<GenreObject> mListGenreObjects = new ArrayList();
                    for (int i = 0; i < size; i++) {
                        JSONObject mJs = mJsonArray.getJSONObject(i);
                        mListGenreObjects.add(new GenreObject(mJs.getString("type"), mJs.getString(TAG_NAME), mJs.getString(IDBFragmentConstants.KEY_NAME_KEYWORD)));
                    }
                    return mListGenreObjects;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static ArrayList<PlaylistObject> parsingPlaylistObject(String data) {
        if (!StringUtils.isEmptyString(data)) {
            try {
                JSONArray mJsonArray = new JSONArray(data);
                int size = mJsonArray.length();
                if (size > 0) {
                    ArrayList<PlaylistObject> mListPlaylistObjects = new ArrayList();
                    for (int i = 0; i < size; i++) {
                        JSONObject mJs = mJsonArray.getJSONObject(i);
                        PlaylistObject mPlaylistObject = new PlaylistObject(mJs.getLong(DBAlertFragment.KEY_ID_DIALOG), mJs.getString(TAG_NAME));
                        JSONArray mJsonArrayId = mJs.getJSONArray(ISoundCloundConstants.METHOD_TRACKS);
                        int len = mJsonArrayId.length();
                        ArrayList<Long> listIds = new ArrayList();
                        if (len > 0) {
                            for (int t = 0; t < len; t++) {
                                listIds.add(Long.valueOf(mJsonArrayId.getLong(t)));
                            }
                        }
                        mPlaylistObject.setListTrackIds(listIds);
                        mListPlaylistObjects.add(mPlaylistObject);
                    }
                    return mListPlaylistObjects;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static ArrayList<TrackObject> parsingListSavingTrackObject(String data) {
        if (!StringUtils.isEmptyString(data)) {
            try {
                JSONArray jSONArray = new JSONArray(data);
                int size = jSONArray.length();
                if (size > 0) {
                    ArrayList<TrackObject> mListTrackObjects = new ArrayList();
                    for (int i = 0; i < size; i++) {
                        JSONObject mJsonObject = jSONArray.getJSONObject(i);
                        long id = mJsonObject.getLong(DBAlertFragment.KEY_ID_DIALOG);
                        long duration = mJsonObject.getLong("duration");
                        String title = mJsonObject.getString("title");
                        String username = mJsonObject.getString("username");
                        Date mDate = DateTimeUtils.getDateFromString(mJsonObject.getString("created_at"), SoundCloundJsonParsingUtils.DATE_PATTERN);
                        String artworkUrl = mJsonObject.getString("artwork_url");
                        if (mJsonObject.opt("path") != null) {
                            TrackObject mTrackObject = new TrackObject(id, title, mDate, duration, username, "", "", artworkUrl, -1, mJsonObject.getString("path"));
                            mTrackObject.setStreamable(true);
                            mListTrackObjects.add(mTrackObject);
                        } else {
                            String str = "";
                            long j = id;
                            Date date = mDate;
                            long j2 = duration;
                            String str2 = title;
                            String str3 = username;
                            String str4 = artworkUrl;
                            TrackObject trackObject = new TrackObject(j, date, j2, str2, str, str3, mJsonObject.getString("avatar_url"), mJsonObject.getString("permalink_url"), str4, mJsonObject.getLong("playback_count"));
                            trackObject.setStreamable(true);
                            mListTrackObjects.add(trackObject);
                        }
                    }
                    return mListTrackObjects;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
