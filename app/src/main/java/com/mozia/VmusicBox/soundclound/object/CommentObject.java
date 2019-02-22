package com.mozia.VmusicBox.soundclound.object;

public class CommentObject {
    private String avatarUrl;
    private String body;
    private String createdAt;
    private long id;
    private int timeStamp;
    private long trackid;
    private long userId;
    private String username;
    public CommentObject(){}
    public CommentObject(long id, String createdAt, long userId, long trackid, int timeStamp, String body, String username, String avatarUrl) {
        this.id = id;
        this.createdAt = createdAt;
        this.userId = userId;
        this.trackid = trackid;
        this.timeStamp = timeStamp;
        this.body = body;
        this.username = username;
        this.avatarUrl = avatarUrl;
    }

    public int getTimeStamp() {
        return this.timeStamp;
    }

    public void setTimeStamp(int timeStamp) {
        this.timeStamp = timeStamp;
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCreatedAt() {
        return this.createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public long getUserId() {
        return this.userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getTrackid() {
        return this.trackid;
    }

    public void setTrackid(long trackid) {
        this.trackid = trackid;
    }

    public String getBody() {
        return this.body;
    }

    public void setBody(String body) {
        this.body = body;
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
}
