package com.mozia.VmusicBox.soundclound.object;

public class UserObject {
    private String avatarUrl;
    private String city;
    private String country;
    private String description;
    private int followers;
    private int following;
    private String fullName;
    private long id;
    private String permalink;
    private String permalinkUrl;
    private int playlistCount;
    private int trackCount;
    private String username;

    public UserObject(long id, String permalink, String username, String permalinkUrl, String avatarUrl, String country, String fullName, String description, String city, int trackCount, int playlistCount, int followers, int following) {
        this.id = id;
        this.permalink = permalink;
        this.username = username;
        this.permalinkUrl = permalinkUrl;
        this.avatarUrl = avatarUrl;
        this.country = country;
        this.fullName = fullName;
        this.description = description;
        this.city = city;
        this.trackCount = trackCount;
        this.playlistCount = playlistCount;
        this.followers = followers;
        this.following = following;
    }

    public UserObject(long id, String permalink, String username, String permalinkUrl, String avatarUrl) {
        this.id = id;
        this.permalink = permalink;
        this.username = username;
        this.permalinkUrl = permalinkUrl;
        this.avatarUrl = avatarUrl;
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPermalink() {
        return this.permalink;
    }

    public void setPermalink(String permalink) {
        this.permalink = permalink;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPermalinkUrl() {
        return this.permalinkUrl;
    }

    public void setPermalinkUrl(String permalinkUrl) {
        this.permalinkUrl = permalinkUrl;
    }

    public String getAvatarUrl() {
        return this.avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getCountry() {
        return this.country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getFullName() {
        return this.fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCity() {
        return this.city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getTrackCount() {
        return this.trackCount;
    }

    public void setTrackCount(int trackCount) {
        this.trackCount = trackCount;
    }

    public int getPlaylistCount() {
        return this.playlistCount;
    }

    public void setPlaylistCount(int playlistCount) {
        this.playlistCount = playlistCount;
    }

    public int getFollowers() {
        return this.followers;
    }

    public void setFollowers(int followers) {
        this.followers = followers;
    }

    public int getFollowing() {
        return this.following;
    }

    public void setFollowing(int following) {
        this.following = following;
    }
}
