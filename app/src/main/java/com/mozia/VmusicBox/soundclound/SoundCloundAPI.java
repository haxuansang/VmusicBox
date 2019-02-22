package com.mozia.VmusicBox.soundclound;

import android.util.Log;

import com.mozia.VmusicBox.object.TopMusicObject;
import com.mozia.VmusicBox.soundclound.object.CommentObject;
import com.mozia.VmusicBox.soundclound.object.TrackObject;
import com.ypyproductions.utils.StringUtils;
import com.ypyproductions.webservice.DownloadUtils;

import java.io.InputStream;
import java.sql.Array;
import java.util.ArrayList;
import java.util.Locale;

public class SoundCloundAPI implements ISoundCloundConstants {
    private static final String TAG = SoundCloundAPI.class.getSimpleName();
    private String clientId;
    private String clientSecret;
    private String mPrefixClientId;

    public SoundCloundAPI(String clientId, String clientSecret) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.mPrefixClientId = String.format(ISoundCloundConstants.FORMAT_CLIENT_ID, new Object[]{clientId});
    }

    public ArrayList<TrackObject> getListTrackObjectsByGenre(String genre, int offset, int limit) {
        StringBuilder mStringBuilder = new StringBuilder();
        mStringBuilder.append(ISoundCloundConstants.URL_API);
        mStringBuilder.append(ISoundCloundConstants.METHOD_TRACKS);
        mStringBuilder.append(ISoundCloundConstants.JSON_PREFIX);
        mStringBuilder.append(this.mPrefixClientId);
        mStringBuilder.append(String.format(ISoundCloundConstants.FILTER_GENRE, new Object[]{genre}));
        mStringBuilder.append(String.format(ISoundCloundConstants.OFFSET, new Object[]{String.valueOf(offset), String.valueOf(limit)}));
        String url = mStringBuilder.toString();
        //DBLog.m25d(TAG, "==============>getListTrackObjectsByGenre=" + url);

        InputStream data = DownloadUtils.download(url);

        if (data != null) {
            return SoundCloundJsonParsingUtils.parsingListTrackObject(data);
        }
        return null;
    }

    public ArrayList<TopMusicObject> getListTopMusic(String codeCountry, int limit) {
        StringBuilder mStringBuilder = new StringBuilder();
        mStringBuilder.append(String.format(ISoundCloundConstants.URL_TOP_MUSIC, new Object[]{codeCountry.toLowerCase(Locale.US), String.valueOf(limit)}));
        String url = mStringBuilder.toString();
        //DBLog.m25d(TAG, "==============>getListTopMusic=" + url);
        InputStream data = DownloadUtils.download(url);
        if (data != null) {
            return SoundCloundJsonParsingUtils.parsingListTopMusicObject(data);
        }
        return null;
    }

    public ArrayList<TrackObject> getListTrackObjectsByQuery(String query, int offset, int limit) {
        StringBuilder mStringBuilder = new StringBuilder();
        mStringBuilder.append(ISoundCloundConstants.URL_API);
        mStringBuilder.append(ISoundCloundConstants.METHOD_TRACKS);
        mStringBuilder.append(ISoundCloundConstants.JSON_PREFIX);
        mStringBuilder.append(this.mPrefixClientId);
        mStringBuilder.append(String.format(ISoundCloundConstants.FILTER_QUERY, new Object[]{query}));
        mStringBuilder.append(String.format(ISoundCloundConstants.OFFSET, new Object[]{String.valueOf(offset), String.valueOf(limit)}));
        String url = mStringBuilder.toString();
//        DBLog.m25d(TAG, "==============>getListTrackObjectsByQuery=" + url);
        Log.d(TAG, "getListTrackObjectsByQuery: "+url);
        InputStream data = DownloadUtils.download(url);
        if (data != null) {
            Log.d(TAG, "getListTrackObjectsByQuery: d"+data.toString());
            return SoundCloundJsonParsingUtils.parsingListTrackObject(data);
        }
        return null;
    }
    public ArrayList<TrackObject> getPlaylistSuggestion(int offset, int limit)
    {
        StringBuilder mStringBuilder = new StringBuilder();
        mStringBuilder.append(ISoundCloundConstants.URL_API);
        mStringBuilder.append(ISoundCloundConstants.METHOD_PLAYLISTS);
        mStringBuilder.append("544742856");
        mStringBuilder.append(ISoundCloundConstants.JSON_PREFIX);
        mStringBuilder.append("?client_id=");
        mStringBuilder.append("a3e059563d7fd3372b49b37f00a00bcf");
        mStringBuilder.append(String.format(ISoundCloundConstants.OFFSET, new Object[]{String.valueOf(offset), String.valueOf(limit)}));
        String url = mStringBuilder.toString();

        Log.d(TAG, "getListTrackObjectsByQuery: "+url);
        InputStream data = DownloadUtils.download(url);
        if (data != null) {
            Log.d(TAG, "getListTrackObjectsByQuery: d"+data.toString());
            return SoundCloundJsonParsingUtils.parsingListTrackObject(data);
        }
        return null;
    }

    public ArrayList<TrackObject> getListTrackObjectsByTypes(String types, int offset, int limit) {
        StringBuilder mStringBuilder = new StringBuilder();
        mStringBuilder.append(ISoundCloundConstants.URL_API);
        mStringBuilder.append(ISoundCloundConstants.METHOD_TRACKS);
        mStringBuilder.append(ISoundCloundConstants.JSON_PREFIX);
        mStringBuilder.append(this.mPrefixClientId);
        mStringBuilder.append(String.format(ISoundCloundConstants.FILTER_TYPES, new Object[]{types}));
        mStringBuilder.append(String.format(ISoundCloundConstants.OFFSET, new Object[]{String.valueOf(offset), String.valueOf(limit)}));
        String url = mStringBuilder.toString();
        //DBLog.m25d(TAG, "==============>getListTrackObjectsByQuery=" + url);
        InputStream data = DownloadUtils.download(url);
        if (data != null) {
            return SoundCloundJsonParsingUtils.parsingListTrackObject(data);
        }
        return null;
    }

    public ArrayList<TrackObject> getListTrackObjectsOfUser(long userId) {
        StringBuilder mStringBuilder = new StringBuilder();
        mStringBuilder.append(ISoundCloundConstants.URL_API);
        mStringBuilder.append("users/");
        mStringBuilder.append(String.valueOf(userId) + "/");
        mStringBuilder.append(ISoundCloundConstants.METHOD_TRACKS);
        mStringBuilder.append(ISoundCloundConstants.JSON_PREFIX);
        mStringBuilder.append(this.mPrefixClientId);
        String url = mStringBuilder.toString();
        //DBLog.m25d(TAG, "==============>getListTrackObjectsOfUser=" + url);
        InputStream data = DownloadUtils.download(url);
        if (data != null) {
            return SoundCloundJsonParsingUtils.parsingListTrackObject(data);
        }
        return null;
    }

    public ArrayList<CommentObject> getListCommentObject(long trackId) {
        StringBuilder mStringBuilder = new StringBuilder();
        mStringBuilder.append(ISoundCloundConstants.URL_API);
        mStringBuilder.append("tracks/");
        mStringBuilder.append(String.valueOf(trackId) + "/");
        mStringBuilder.append(ISoundCloundConstants.METHOD_COMMENTS);
        mStringBuilder.append(ISoundCloundConstants.JSON_PREFIX);
        mStringBuilder.append(this.mPrefixClientId);
        String url = mStringBuilder.toString();
        //DBLog.m25d(TAG, "==============>getListCommentObject=" + url);
        InputStream data = DownloadUtils.download(url);
        if (data != null) {
            return SoundCloundJsonParsingUtils.parsingListCommentObject(data);
        }
        return null;
    }

    public TrackObject getTrackObject(long id) {
        StringBuilder mStringBuilder = new StringBuilder();
        mStringBuilder.append(ISoundCloundConstants.URL_API);
        mStringBuilder.append(ISoundCloundConstants.METHOD_TRACKS);
        mStringBuilder.append("/");
        mStringBuilder.append(String.valueOf(id));
        mStringBuilder.append(ISoundCloundConstants.JSON_PREFIX);
        mStringBuilder.append(this.mPrefixClientId);
        String url = mStringBuilder.toString();
        //DBLog.m25d(TAG, "==============>getTrackObject=" + url);
        String data = DownloadUtils.downloadString(url);
        if (StringUtils.isEmptyString(data)) {
            return null;
        }
        return SoundCloundJsonParsingUtils.parsingTrackObject(data);
    }

    public String getClientId() {
        return this.clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getClientSecret() {
        return this.clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }
}
