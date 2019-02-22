package com.mozia.VmusicBox.constants;

import com.mozia.VmusicBox.R;
import com.mozia.VmusicBox.app.Navigator;

public interface ICloudMusicPlayerConstants {
    public static final String ADMOB_ID_BANNER = "ca-app-pub-9501118552890731/2331724806";
    public static final String ADMOB_ID_INTERTESTIAL = "ca-app-pub-9501118552890731/3808458002";
    public static final int BUFFER_SIZE = 1024;
    public static final String DATE_PATTERN_CONVERT = "MMM d, yyyy";
    public static final String DATE_PATTERN_ORI = "yyyy/MM/dd hh:mm:ss Z";
    public static final boolean DEBUG = false;
    public static final String FILE_GENRE = "genres/genre_%1$s.dat";
    public static final String FORMAT_URI = "content://media/external/audio/media/%1$s";
    public static final int GENRE_INDEX = 4;
    public static final int HOME_INDEX = 0;
    public static final String KEY_HEADER = "KEY_HEADER";
    public static final String KEY_SONG_ID = "songId";
    public static final String KEY_TYPE = "type";
    public static final int LIBRARY_INDEX = 2;
    public static final int[] LIST_ICON_MENU = new int[]{R.drawable.ic_home_white_24dp, R.drawable.ic_download};
    public static final int[] LIST_MENU_STRING = new int[]{R.string.title_home, R.string.title_download_songs};
    public static final int MAX_PAGE = 14;
    public static final int MAX_RESULT_PER_PAGE = 10;
    public static final int MAX_SONG_TOP = 80;
    public static final String NAME_FOLDER_CACHE = "MP3 Songs";
    public static final String NAME_PLAYLIST_FILE = "list_playlists.dat";
    public static final String NAME_SAVED_TRACK = "list_tracks.dat";
    public static final int NOTIFICATION_ID = 1000;
    public static final int PLAYLIST_INDEX = 3;
    public static final boolean SHOW_ADVERTISEMENT = true;

    public static final String[] SOUNDCLOUND_CLIENT_ID_ = new String[]{"a3e059563d7fd3372b49b37f00a00bcf"};

    public static final String[] SOUNDCLOUND_CLIENT_SECRET_ = new String[]{"40a06aaf50d400770348b376458c54ef", "6bfc764ac5e17e7c0d5a0e7a2354eb39", "4d555e5ac1a4da9d2feb5a655b8f5c5d"};
    public static final int TOP_HIT_INDEX = 1;
    public static final String URL_FORMAT_LINK_APP = "https://play.google.com/store/apps/details?id=com.mozia.VmusicBox";
    public static final String URL_FORMAT_SUGESSTION = "http://suggestqueries.google.com/complete/search?ds=yt&output=toolbar&hl=en-US&q=%1$s";
    public static final String URL_MORE_APPS = "https://play.google.com/store/search?q=moziasoft";
    public static final String URL_YOUR_FACE_BOOK = "https://www.facebook.com/LuSoMusicMP3Free";
    public static final String URL_YOUR_WEBSITE = "http://moziasoft.com/";
    public static final String YOUR_EMAIL_CONTACT = "contact@moziasoft.com";
    public static final int f9i = Navigator.randInt(0, SOUNDCLOUND_CLIENT_ID_.length);
    public static final String SOUNDCLOUND_CLIENT_ID = SOUNDCLOUND_CLIENT_ID_[f9i];
    public static final String SOUNDCLOUND_CLIENT_SECRET = SOUNDCLOUND_CLIENT_SECRET_[f9i];
    public static final String idOfPlaylist="544742856.json";
    public static final String idOfClient="a3e059563d7fd3372b49b37f00a00bcf";
    public static final String maxItems="10";



}
