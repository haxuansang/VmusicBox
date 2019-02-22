package com.mozia.VmusicBox.soundclound;

import android.util.JsonReader;
import android.util.JsonToken;

import com.google.android.gms.plus.PlusShare;
import com.mozia.VmusicBox.DBAlertFragment;
import com.mozia.VmusicBox.object.TopMusicObject;
import com.mozia.VmusicBox.soundclound.object.CommentObject;
import com.mozia.VmusicBox.soundclound.object.TrackObject;
import com.ypyproductions.utils.DateTimeUtils;
import com.ypyproductions.utils.StringUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class SoundCloundJsonParsingUtils {
    public static final String DATE_PATTERN = "yyyy/MM/dd HH:mm:ss";
    public static final String DATE_PATTERN_ORI = "yyyy/MM/dd hh:mm:ss Z";
    public static final String TAG = SoundCloundJsonParsingUtils.class.getSimpleName();

    private static TrackObject parsingTrackObject(JSONObject mJsonObject) {
        if (mJsonObject != null) {
            try {
                TrackObject mTrackObject = new TrackObject(mJsonObject.getLong(DBAlertFragment.KEY_ID_DIALOG), mJsonObject.getString("title"), DateTimeUtils.getDateFromString(mJsonObject.getString("created_at"), DATE_PATTERN), mJsonObject.getLong("duration"), mJsonObject.getString("username"), mJsonObject.getString("avatar_url"), mJsonObject.getString("permalink_url"), mJsonObject.getString("artwork_url"), mJsonObject.getLong("playback_count"), mJsonObject.getString("path"));
                mTrackObject.setStreamable(true);
                return mTrackObject;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private static TrackObject parsingNewTrackObject(JsonReader reader) {
        if (reader != null) {
            try {
                TrackObject mTrackObject = new TrackObject();
                while (reader.hasNext()) {
                    String name = reader.nextName();
                    if (name.equals(DBAlertFragment.KEY_ID_DIALOG)) {
                        mTrackObject.setId(reader.nextLong());
                    } else if (name.equals("created_at")) {
                        if (mTrackObject != null) {
                            mTrackObject.setCreatedDate(DateTimeUtils.getDateFromString(reader.nextString(), "yyyy/MM/dd hh:mm:ss Z"));
                        }
                    } else if (name.equals("user_id")) {
                        if (mTrackObject != null) {
                            mTrackObject.setUserId(reader.nextLong());
                        }
                    } else if (name.equals("duration")) {
                        if (mTrackObject != null) {
                            mTrackObject.setDuration(reader.nextLong());
                        }
                    } else if (name.equals("sharing")) {
                        if (mTrackObject != null) {
                            mTrackObject.setSharing(reader.nextString());
                        }
                    } else if (!name.equals("tag_list") || reader.peek() == JsonToken.NULL) {
                        if (!name.equals("streamable") || reader.peek() == JsonToken.NULL) {
                            if (!name.equals("downloadable") || reader.peek() == JsonToken.NULL) {
                                if (!name.equals("genre") || reader.peek() == JsonToken.NULL) {
                                    if (!name.equals("title") || reader.peek() == JsonToken.NULL) {
                                        if (!name.equals(PlusShare.KEY_CONTENT_DEEP_LINK_METADATA_DESCRIPTION) || reader.peek() == JsonToken.NULL) {
                                            if (name.equals("user")) {
                                                reader.beginObject();
                                                while (reader.hasNext()) {
                                                    String nameTagUser = reader.nextName();
                                                    if (nameTagUser.equals("username")) {
                                                        if (mTrackObject != null) {
                                                            mTrackObject.setUsername(reader.nextString());
                                                        }
                                                    } else if (!nameTagUser.equals("avatar_url") || reader.peek() == JsonToken.NULL) {
                                                        reader.skipValue();
                                                    } else if (mTrackObject != null) {
                                                        mTrackObject.setAvatarUrl(reader.nextString());
                                                    }
                                                }
                                                reader.endObject();
                                            } else if (name.equals("permalink_url")) {
                                                if (mTrackObject != null) {
                                                    mTrackObject.setPermalinkUrl(reader.nextString());
                                                }
                                            } else if (!name.equals("artwork_url") || reader.peek() == JsonToken.NULL) {
                                                if (name.equals("waveform_url")) {
                                                    if (mTrackObject != null) {
                                                        mTrackObject.setWaveForm(reader.nextString());
                                                    }
                                                } else if (name.equals("playback_count")) {
                                                    if (mTrackObject != null) {
                                                        mTrackObject.setPlaybackCount(reader.nextLong());
                                                    }
                                                } else if (name.equals("likes_count")) {
                                                    if (mTrackObject != null) {
                                                        mTrackObject.setFavoriteCount(reader.nextLong());
                                                    }
                                                } else if (!name.equals("comment_count")) {
                                                    reader.skipValue();
                                                } else if (mTrackObject != null) {
                                                    mTrackObject.setCommentCount(reader.nextLong());
                                                }
                                            } else if (mTrackObject != null) {
                                                mTrackObject.setArtworkUrl(reader.nextString());
                                            }
                                        } else if (mTrackObject != null) {
                                            mTrackObject.setDescription(reader.nextString());
                                        }
                                    } else if (mTrackObject != null) {
                                        mTrackObject.setTitle(reader.nextString());
                                    }
                                } else if (mTrackObject != null) {
                                    mTrackObject.setGenre(reader.nextString());
                                }
                            } else if (mTrackObject != null) {
                                mTrackObject.setDownloadable(reader.nextBoolean());
                            }
                        } else if (mTrackObject != null) {
                            mTrackObject.setStreamable(reader.nextBoolean());
                        }
                    } else if (mTrackObject != null) {
                        mTrackObject.setTagList(reader.nextString());
                    }
                }
                return mTrackObject;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static ArrayList<TrackObject> parsingListTrackObject(InputStream in) {
        if (in == null) {
            new Exception(TAG + " data can not null").printStackTrace();
            return null;
        }
        try {
            JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));
            ArrayList<TrackObject> listTrackObjects = new ArrayList();
            reader.beginArray();
            while (reader.hasNext()) {
                reader.beginObject();
                TrackObject mTrackObject = parsingNewTrackObject(reader);
                if (mTrackObject != null && mTrackObject.isStreamable()) {
                    listTrackObjects.add(mTrackObject);
                }
                reader.endObject();
            }
            reader.endArray();
            reader.close();
            //DBLog.m25d(TAG, "================>listTrackObjects size=" + listTrackObjects.size());
            if (in == null) {
                return listTrackObjects;
            }
            try {
                in.close();
                return listTrackObjects;
            } catch (IOException e) {
                e.printStackTrace();
                return listTrackObjects;
            }
        } catch (Exception e2) {
            e2.printStackTrace();
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e3) {
                    e3.printStackTrace();
                }
            }
            return null;
        } catch (Throwable th) {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e32) {
                    e32.printStackTrace();
                }
            }
        }
        return null;
    }

    public static ArrayList<TrackObject> parsingListTrackObject(String data) {
        if (!StringUtils.isEmptyString(data)) {
            try {
                JSONArray mJsonArray = new JSONArray(data);
                int size = mJsonArray.length();
                if (size > 0) {
                    ArrayList<TrackObject> listTrackObjects = new ArrayList();
                    for (int i = 0; i < size; i++) {
                        TrackObject mTrackObject = parsingTrackObject(mJsonArray.getJSONObject(i));
                        if (mTrackObject != null) {
                            listTrackObjects.add(mTrackObject);
                        }
                    }
                    //DBLog.m25d(TAG, "================>listTrackObjects size=" + listTrackObjects.size());
                    return listTrackObjects;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private static CommentObject parsingCommentObject(JsonReader reader) {
        if (reader != null) {
            CommentObject mCommentObject = null;
            try {
                while (reader.hasNext()) {
                    try {
                        String name = reader.nextName();
                        if (name.equals(DBAlertFragment.KEY_ID_DIALOG)) {
                            CommentObject mCommentObject2 = new CommentObject();
                            try {
                                mCommentObject2.setId(reader.nextLong());
                                mCommentObject = mCommentObject2;
                            } catch (IOException e) {
                                IOException e2 = e;
                            }
                        } else if (name.equals("created_at")) {
                            if (mCommentObject != null) {
                                mCommentObject.setCreatedAt(reader.nextString());
                            }
                        } else if (name.equals("user_id")) {
                            if (mCommentObject != null) {
                                mCommentObject.setUserId(reader.nextLong());
                            }
                        } else if (name.equals("track_id")) {
                            if (mCommentObject != null) {
                                mCommentObject.setTrackid(reader.nextLong());
                            }
                        } else if (!name.equals("timestamp") || reader.peek() == JsonToken.NULL) {
                            if (name.equals("body")) {
                                if (mCommentObject != null) {
                                    mCommentObject.setBody(reader.nextString());
                                }
                            } else if (name.equals("user")) {
                                reader.beginObject();
                                while (reader.hasNext()) {
                                    String nameTagUser = reader.nextName();
                                    if (nameTagUser.equals("username")) {
                                        if (mCommentObject != null) {
                                            mCommentObject.setUsername(reader.nextString());
                                        }
                                    } else if (!nameTagUser.equals("avatar_url") || reader.peek() == JsonToken.NULL) {
                                        reader.skipValue();
                                    } else if (mCommentObject != null) {
                                        mCommentObject.setAvatarUrl(reader.nextString());
                                    }
                                }
                                reader.endObject();
                            } else {
                                reader.skipValue();
                            }
                        } else if (mCommentObject != null) {
                            mCommentObject.setTimeStamp(reader.nextInt());
                        }
                    } catch (IOException e3) {

                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return mCommentObject;
        }
        return null;
    }

    public static TrackObject parsingTrackObject(String data) {
        if (!StringUtils.isEmptyString(data)) {
            try {
                return parsingTrackObject(new JSONObject(data));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static ArrayList<CommentObject> parsingListCommentObject(InputStream in) {
        if (in == null) {
            new Exception(TAG + " data can not null").printStackTrace();
            return null;
        }
        try {
            JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));
            ArrayList<CommentObject> listCommentObjects = new ArrayList();
            reader.beginArray();
            while (reader.hasNext()) {
                reader.beginObject();
                CommentObject mComment = parsingCommentObject(reader);
                if (mComment != null) {
                    listCommentObjects.add(mComment);
                }
                reader.endObject();
            }
            reader.endArray();
            reader.close();
            //DBLog.m25d(TAG, "================>listCommentObjects size=" + listCommentObjects.size());
            if (in == null) {
                return listCommentObjects;
            }
            try {
                in.close();
                return listCommentObjects;
            } catch (IOException e) {
                e.printStackTrace();
                return listCommentObjects;
            }
        } catch (Exception e2) {
            e2.printStackTrace();
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e3) {
                    e3.printStackTrace();
                }
            }
            return null;
        } catch (Throwable th) {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e32) {
                    e32.printStackTrace();
                }
            }
        }
        return null;
    }

    private static TopMusicObject parsingTopMusicObject(JsonReader reader) {
        if (reader != null) {
            TopMusicObject mTopMusicObject = null;
            try {
                while (reader.hasNext()) {
                    try {
                        String name = reader.nextName();
                        if (name.equals("im:name")) {
                            TopMusicObject mTopMusicObject2 = new TopMusicObject();
                            reader.beginObject();
                            while (reader.hasNext()) {
                                if (reader.nextName().equals(PlusShare.KEY_CALL_TO_ACTION_LABEL)) {
                                    mTopMusicObject2.setName(reader.nextString());
                                } else {
                                    try {
                                        reader.skipValue();
                                    } catch (IOException e) {
                                        IOException e2 = e;
                                    }
                                }
                            }
                            reader.endObject();
                            mTopMusicObject = mTopMusicObject2;
                        } else if (name.equals("im:image")) {
                            reader.beginArray();
                            while (reader.hasNext()) {
                                reader.beginObject();
                                while (reader.hasNext()) {
                                    if (reader.nextName().equals(PlusShare.KEY_CALL_TO_ACTION_LABEL)) {
                                        mTopMusicObject.setArtwork(reader.nextString());
                                    } else {
                                        reader.skipValue();
                                    }
                                }
                                reader.endObject();
                            }
                            reader.endArray();
                        } else if (name.equals("im:artist")) {
                            reader.beginObject();
                            while (reader.hasNext()) {
                                if (reader.nextName().equals(PlusShare.KEY_CALL_TO_ACTION_LABEL)) {
                                    mTopMusicObject.setArtist(reader.nextString());
                                } else {
                                    reader.skipValue();
                                }
                            }
                            reader.endObject();
                        } else {
                            reader.skipValue();
                        }
                    } catch (IOException e3) {

                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return mTopMusicObject;
        }
        return null;

    }

    public static ArrayList<TopMusicObject> parsingListTopMusicObject(InputStream in) {
        if (in == null) {
            new Exception(TAG + " data can not null").printStackTrace();
            return null;
        }
        JsonReader reader = null;
        ArrayList<TopMusicObject> listTopObjects = new ArrayList();
        try {
            reader = new JsonReader(new InputStreamReader(in, "UTF-8"));


        reader.beginObject();
        while (reader.hasNext()) {
            try {
                if (reader.nextName().equals("feed")) {
                    reader.beginObject();
                    while (reader.hasNext()) {
                        if (reader.nextName().equals("entry")) {
                            reader.beginArray();
                            while (reader.hasNext()) {
                                reader.beginObject();
                                TopMusicObject mTrackObject = parsingTopMusicObject(reader);
                                if (mTrackObject != null) {
                                    listTopObjects.add(mTrackObject);
                                }
                                reader.endObject();
                            }
                            reader.endArray();
                        } else {
                            reader.skipValue();
                        }
                    }
                    reader.endObject();
                } else {
                    reader.skipValue();
                }
            } catch (Exception e) {
                e.printStackTrace();
                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException e2) {
                        e2.printStackTrace();
                    }
                }
                return null;
            } catch (Throwable th) {
                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException e22) {
                        e22.printStackTrace();
                    }
                }
            }
        }
            reader.endObject();
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        //DBLog.m25d(TAG, "================>listTopObjects size=" + listTopObjects.size());
        if (in == null) {
            return listTopObjects;
        }
        try {
            in.close();
            return listTopObjects;
        } catch (IOException e222) {
            e222.printStackTrace();
            return listTopObjects;
        }
    }
}
