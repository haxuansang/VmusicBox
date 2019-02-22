package com.mozia.VmusicBox.soundclound;

public interface ISoundCloundConstants {
    public static final String FILTER_GENRE = "&genres=%1$s";
    public static final String FILTER_LICENSE = "&license=%1$s";
    public static final String FILTER_QUERY = "&q=%1$s";
    public static final String FILTER_TYPES = "&types=%1$s";
    public static final String FORMAT_CLIENT_ID = "?client_id=%1$s";
    public static final String FORMAT_URL_DOWNLOAD_SONG = "https://api.soundcloud.com/tracks/%1$s/stream?client_id=%2$s";
    public static final String FORMAT_URL_SONG = "http://api.soundcloud.com/tracks/%1$s/stream?client_id=%2$s";
    public static final String JSON_PREFIX = ".json";
    public static final String METHOD_COMMENTS = "comments";
    public static final String METHOD_ME = "me";
    public static final String METHOD_PLAYLISTS = "playlists/";
    public static final String METHOD_TRACKS = "tracks";
    public static final String METHOD_USER = "users";
    public static final String OFFSET = "&offset=%1$s&limit=%2$s";
    public static final String URL_API = "https://api.soundcloud.com/";

    public static final String URL_TOP_MUSIC = "https://itunes.apple.com/%1$s/rss/topsongs/limit=%2$s/json";
}
