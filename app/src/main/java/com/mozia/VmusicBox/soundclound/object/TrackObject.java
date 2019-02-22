package com.mozia.VmusicBox.soundclound.object;

import android.content.ContentUris;
import android.net.Uri;
import android.provider.MediaStore.Audio.Media;

import com.mozia.VmusicBox.DBAlertFragment;
import com.mozia.VmusicBox.soundclound.SoundCloundJsonParsingUtils;
import com.ypyproductions.utils.DateTimeUtils;
import com.ypyproductions.utils.StringUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

public class TrackObject {
    private String artworkUrl;
    private String avatarUrl;
    private long commentCount;
    private Date createdDate;
    private String description;
    private boolean downloadable;
    private long duration;
    private long favoriteCount;
    private String genre;
    private long id;
    private boolean isLocalMusic;
    private boolean isStreamable;
    private String linkStream;
    private String path;
    private String permalinkUrl;
    private long playbackCount;
    private String sharing;
    private String tagList;
    private String title;
    private long userId;
    private String username;
    private String waveForm;

    public TrackObject(){}
    public TrackObject(long id, Date createdDate, long duration, String title, String description, String username, String avatarUrl, String permalinkUrl, String artworkUrl, long playbackCount) {
        this.id = id;
        this.createdDate = createdDate;
        this.duration = duration;
        this.title = title;
        this.description = description;
        this.username = username;
        this.avatarUrl = avatarUrl;
        this.permalinkUrl = permalinkUrl;
        this.artworkUrl = artworkUrl;
        this.playbackCount = playbackCount;
    }

    public TrackObject(long id, String title, Date createdDate, long duration, String username, String avatarUrl, String permalinkUrl, String artworkUrl, long playbackCount, String path) {
        this.id = id;
        this.title = title;
        this.createdDate = createdDate;
        this.duration = duration;
        this.username = username;
        this.avatarUrl = avatarUrl;
        this.permalinkUrl = permalinkUrl;
        this.artworkUrl = artworkUrl;
        this.playbackCount = playbackCount;
        this.path = path;
    }

    public TrackObject(long id, long duration, String title, String description, String username, String avatarUrl, String permalinkUrl, String artworkUrl, Integer playbackCount) {
        this.id = id;
        this.duration = duration;
        this.title = title;
        this.description = description;
        this.username = username;
        this.avatarUrl = avatarUrl;
        this.permalinkUrl = permalinkUrl;
        this.artworkUrl = artworkUrl;
        this.playbackCount = playbackCount;
        this.isStreamable=true;

    }


    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getCreatedDate() {
        return this.createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public long getUserId() {
        return this.userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getDuration() {
        return this.duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public String getSharing() {
        return this.sharing;
    }

    public void setSharing(String sharing) {
        this.sharing = sharing;
    }

    public String getTagList() {
        return this.tagList;
    }

    public void setTagList(String tagList) {
        this.tagList = tagList;
    }

    public String getGenre() {
        return this.genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAvatarUrl() {
        return this.avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getPermalinkUrl() {
        return this.permalinkUrl;
    }

    public void setPermalinkUrl(String permalinkUrl) {
        this.permalinkUrl = permalinkUrl;
    }

    public String getArtworkUrl() {
        return this.artworkUrl;
    }

    public void setArtworkUrl(String artworkUrl) {
        this.artworkUrl = artworkUrl;
    }

    public String getWaveForm() {
        return this.waveForm;
    }

    public void setWaveForm(String waveForm) {
        this.waveForm = waveForm;
    }

    public long getPlaybackCount() {
        return this.playbackCount;
    }

    public void setPlaybackCount(long playbackCount) {
        this.playbackCount = playbackCount;
    }

    public long getFavoriteCount() {
        return this.favoriteCount;
    }

    public void setFavoriteCount(long favoriteCount) {
        this.favoriteCount = favoriteCount;
    }

    public long getCommentCount() {
        return this.commentCount;
    }

    public void setCommentCount(long commentCount) {
        this.commentCount = commentCount;
    }

    public String getPath() {
        return this.path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public boolean isLocalMusic() {
        return this.isLocalMusic;
    }

    public void setLocalMusic(boolean isLocalMusic) {
        this.isLocalMusic = isLocalMusic;
    }

    public Uri getURI() {
        return ContentUris.withAppendedId(Media.EXTERNAL_CONTENT_URI, this.id);
    }

    public JSONObject toJsonObject() {
        JSONObject mJsonObject = new JSONObject();
        try {
            mJsonObject.put(DBAlertFragment.KEY_ID_DIALOG, this.id);
            mJsonObject.put("title", this.title);
            mJsonObject.put("username", this.username);
            mJsonObject.put("created_at", DateTimeUtils.convertDateToString(this.createdDate, SoundCloundJsonParsingUtils.DATE_PATTERN));
            mJsonObject.put("duration", this.duration);
            mJsonObject.put("artwork_url", this.artworkUrl == null ? "" : this.artworkUrl);
            if (StringUtils.isEmptyString(this.path)) {
                mJsonObject.put("avatar_url", this.avatarUrl);
                mJsonObject.put("permalink_url", this.permalinkUrl);
                mJsonObject.put("playback_count", this.playbackCount);
                return mJsonObject;
            }
            mJsonObject.put("path", this.path);
            return mJsonObject;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean isDownloadable() {
        return this.downloadable;
    }

    public boolean isStreamable() {
        return this.isStreamable;
    }

    public void setStreamable(boolean isStreamable) {
        this.isStreamable = isStreamable;
    }

    public String getLinkStream() {
        return this.linkStream;
    }

    public void setLinkStream(String linkStream) {
        this.linkStream = linkStream;
    }

    public void setDownloadable(boolean downloadable) {
        this.downloadable = downloadable;
    }

    public TrackObject clone() {
        TrackObject mTrackObject = new TrackObject(this.id, this.title, this.createdDate, this.duration, this.username, this.avatarUrl, this.permalinkUrl, this.artworkUrl, this.playbackCount, this.path);
        mTrackObject.setCreatedDate(new Date());
        return mTrackObject;
    }
}
