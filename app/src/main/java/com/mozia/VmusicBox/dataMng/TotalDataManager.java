package com.mozia.VmusicBox.dataMng;

import android.content.Context;
import android.database.Cursor;
import android.os.Environment;
import android.provider.MediaStore.Audio.Media;

import com.mozia.VmusicBox.DBFragmentActivity;
import com.mozia.VmusicBox.R;
import com.mozia.VmusicBox.app.AppRater;
import com.mozia.VmusicBox.constants.ICloudMusicPlayerConstants;
import com.mozia.VmusicBox.object.GenreObject;
import com.mozia.VmusicBox.object.PlaylistObject;
import com.mozia.VmusicBox.object.TopMusicObject;
import com.mozia.VmusicBox.soundclound.object.TrackObject;
import com.ypyproductions.task.IDBCallback;
import com.ypyproductions.utils.ApplicationUtils;
import com.ypyproductions.utils.DBListExcuteAction;
import com.ypyproductions.utils.IOUtils;
import com.ypyproductions.utils.StringUtils;

import org.json.JSONArray;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

public class TotalDataManager implements ICloudMusicPlayerConstants {
    public static final String TAG = TotalDataManager.class.getSimpleName();
    private static TotalDataManager totalDataManager;
    private ArrayList<TrackObject> listCurrrentTrackObjects;
    private ArrayList<GenreObject> listGenreObjects;
    private ArrayList<TrackObject> listLibraryTrackObjects;
    private ArrayList<PlaylistObject> listPlaylistObjects;
    private ArrayList<TrackObject> listSavedTrackObjects;
    private ArrayList<TopMusicObject> listTopMusicObjects;


    public static TotalDataManager getInstance() {
        if (totalDataManager == null) {
            totalDataManager = new TotalDataManager();
        }
        return totalDataManager;
    }

    private TotalDataManager() {
    }

    public void onDestroy() {
        checkDestroy(this.listLibraryTrackObjects);
        checkDestroy(this.listCurrrentTrackObjects);
        checkDestroy(this.listSavedTrackObjects);
        if (this.listTopMusicObjects != null) {
            this.listTopMusicObjects.clear();
            this.listTopMusicObjects = null;
        }
        if (this.listPlaylistObjects != null) {
            this.listPlaylistObjects.clear();
            this.listPlaylistObjects = null;
        }
        if (this.listGenreObjects != null) {
            this.listGenreObjects.clear();
            this.listGenreObjects = null;
        }
        totalDataManager = null;
    }

    private void checkDestroy(ArrayList<TrackObject> listTrackObjects) {
        if (listTrackObjects != null) {
            //DBLog.m25d(TAG, "===========>da destroy");
            listTrackObjects.clear();
        }
    }

    public ArrayList<TrackObject> getListCurrrentTrackObjects() {
        return this.listCurrrentTrackObjects;
    }

    public void setListCurrrentTrackObjects(ArrayList<TrackObject> listCurrrentTrackObjects) {
        this.listCurrrentTrackObjects = listCurrrentTrackObjects;
    }

    public ArrayList<TopMusicObject> getListTopMusicObjects() {
        return this.listTopMusicObjects;
    }

    public void setListTopMusicObjects(ArrayList<TopMusicObject> listTopMusicObjects) {
        this.listTopMusicObjects = listTopMusicObjects;
    }

    public TrackObject getLibraryTrack(long id) {
        if (this.listLibraryTrackObjects != null && this.listLibraryTrackObjects.size() > 0) {
            Iterator it = this.listLibraryTrackObjects.iterator();
            while (it.hasNext()) {
                TrackObject mTrackObject = (TrackObject) it.next();
                if (mTrackObject.getId() == id) {
                    return mTrackObject;
                }
            }
        }
        return null;
    }

    public ArrayList<GenreObject> getListGenreObjects() {
        return this.listGenreObjects;
    }

    public void setListGenreObjects(ArrayList<GenreObject> listGenreObjects) {
        this.listGenreObjects = listGenreObjects;
    }

    public ArrayList<TrackObject> getListLibraryTrackObjects() {
        return this.listLibraryTrackObjects;
    }

    public void setListLibraryTrackObjects(ArrayList<TrackObject> listDownloadedTrackObjects) {
        this.listLibraryTrackObjects = listDownloadedTrackObjects;
    }

    public void readLibraryTrack(Context mContext) {

        File mFile = new File(Environment.getExternalStorageDirectory(), ICloudMusicPlayerConstants.NAME_FOLDER_CACHE);
        if (!mFile.exists()) {
            mFile.mkdirs();
        }
        ArrayList<TrackObject> mListSavedTrackObject = getListMusicsFromLibrary(mContext);
        if (mListSavedTrackObject == null) {
            setListLibraryTrackObjects(new ArrayList<TrackObject>());
        } else {
            setListLibraryTrackObjects(mListSavedTrackObject);
        }

        //DBLog.m25d(TAG, "========>mListSavedTrackObject=" + (mListSavedTrackObject != null ? mListSavedTrackObject.size() : 0));
    }

    private ArrayList<TrackObject> getListMusicsFromLibrary(Context mContext) {
        if (!AppRater.isReadExternalSDCard)
            return null;
        Cursor cur = mContext.getContentResolver().query(Media.EXTERNAL_CONTENT_URI, null, "is_music = 1", null, null);
        //DBLog.m25d(TAG, "Query finished. " + (cur == null ? "Returned NULL." : "Returned a cursor."));
        if (cur == null) {
            //DBLog.m25d(TAG, "Failed to retrieve music: cursor is null :-(");
            return null;
        } else if (cur.moveToFirst()) {
            int artistColumn = cur.getColumnIndex("artist");
            int titleColumn = cur.getColumnIndex("title");
            int albumColumn = cur.getColumnIndex("album");
            int durationColumn = cur.getColumnIndex("duration");
            int idColumn = cur.getColumnIndex("_id");
            int dataColumn = cur.getColumnIndex("_data");
            int dateColumn = cur.getColumnIndex("date_modified");
            ArrayList<TrackObject> listTrackObjects = new ArrayList();
            do {
                //DBLog.m25d(TAG, "ID: " + cur.getString(idColumn) + " Title: " + cur.getString(titleColumn));
                long id = cur.getLong(idColumn);
                String singer = cur.getString(artistColumn);
                String title = cur.getString(titleColumn);
                long duration = cur.getLong(durationColumn);
                String album = cur.getString(albumColumn);
                String path = cur.getString(dataColumn);
                Date mDate = new Date(cur.getLong(dateColumn) * 1000);
                if (!StringUtils.isEmptyString(path)) {
                    File file = new File(path);
                    if (file.exists() && file.isFile()) {
                        TrackObject mTrackObject = new TrackObject(id, title, mDate, duration, singer, "", "", album, -1, path);
                        mTrackObject.setStreamable(true);
                        listTrackObjects.add(mTrackObject);
                    }
                }
            } while (cur.moveToNext());
            return listTrackObjects;
        } else {
            //DBLog.m25d(TAG, "Failed to move cursor to first row (no query results).");
            return null;
        }
    }

    public ArrayList<TrackObject> getListSavedTrackObjects() {
        return this.listSavedTrackObjects;
    }

    public void setListSavedTrackObjects(ArrayList<TrackObject> listSavedTrackObjects) {
        this.listSavedTrackObjects = listSavedTrackObjects;
    }

    public void readPlaylistCached(Context mContext, File mFile) {
        ArrayList<PlaylistObject> mListPlaylist = JsonParsingUtils.parsingPlaylistObject(IOUtils.readString(mContext, mFile.getAbsolutePath(), ICloudMusicPlayerConstants.NAME_PLAYLIST_FILE));
        if (mListPlaylist == null || mListPlaylist.size() <= 0) {
            mListPlaylist = new ArrayList();
            setListPlaylistObjects(mListPlaylist);
        } else {
            setListPlaylistObjects(mListPlaylist);
        }
        Iterator it = mListPlaylist.iterator();
        while (it.hasNext()) {
            filterSongOfPlaylistId((PlaylistObject) it.next());
        }
    }

    private void filterSongOfPlaylistId(PlaylistObject mPlaylistObject) {
        if (this.listSavedTrackObjects != null && this.listSavedTrackObjects.size() > 0) {
            ArrayList<Long> mListId = mPlaylistObject.getListTrackIds();
            if (mListId != null && mListId.size() > 0) {
                Iterator it = mListId.iterator();
                while (it.hasNext()) {
                    Long mId = (Long) it.next();
                    Iterator it2 = this.listSavedTrackObjects.iterator();
                    while (it2.hasNext()) {
                        TrackObject mTrackObject = (TrackObject) it2.next();
                        if (mTrackObject.getId() == mId.longValue()) {
                            mPlaylistObject.addTrackObject(mTrackObject, false);
                            break;
                        }
                    }
                }
            }
        }
    }

    public void readSavedTrack(Context mContext, File mFile) {
        //rrayList arrayList;
        ArrayList<TrackObject> mListSavedTrackObject = JsonParsingUtils.parsingListSavingTrackObject(IOUtils.readString(mContext, mFile.getAbsolutePath(), ICloudMusicPlayerConstants.NAME_SAVED_TRACK));
        TotalDataManager instance = getInstance();
        if (mListSavedTrackObject == null) {
            mListSavedTrackObject = new ArrayList();
        }
        instance.setListSavedTrackObjects(mListSavedTrackObject);
        //DBLog.m25d(TAG, "========>mListSavedTrackObject=" + (mListSavedTrackObject != null ? mListSavedTrackObject.size() : 0));
    }

    public ArrayList<PlaylistObject> getListPlaylistObjects() {
        return this.listPlaylistObjects;
    }

    public void setListPlaylistObjects(ArrayList<PlaylistObject> listPlaylistObjects) {
        this.listPlaylistObjects = listPlaylistObjects;
    }

    public void addPlaylistObject(final Context mContext, PlaylistObject mPlaylistObject) {
        if (this.listPlaylistObjects != null && mPlaylistObject != null) {
            this.listPlaylistObjects.add(mPlaylistObject);
            DBListExcuteAction.getInstance().queueAction(new IDBCallback() {
                public void onAction() {
                    TotalDataManager.this.savePlaylistObjects(mContext);
                }
            });
        }
    }

    public void editPlaylistObject(final Context mContext, PlaylistObject mPlaylistObject, String newName) {
        if (this.listPlaylistObjects != null && mPlaylistObject != null && !StringUtils.isEmptyString(newName)) {
            mPlaylistObject.setName(newName);
            DBListExcuteAction.getInstance().queueAction(new IDBCallback() {
                public void onAction() {
                    TotalDataManager.this.savePlaylistObjects(mContext);
                }
            });
        }
    }

    public void removePlaylistObject(final Context mContext, PlaylistObject mPlaylistObject) {
        if (this.listPlaylistObjects != null && this.listPlaylistObjects.size() > 0) {
            this.listPlaylistObjects.remove(mPlaylistObject);
            ArrayList<TrackObject> mListTrack = mPlaylistObject.getListTrackObjects();
            boolean isNeedToSaveTrack = false;
            if (mListTrack != null && mListTrack.size() > 0) {
                Iterator it = mListTrack.iterator();
                while (it.hasNext()) {
                    TrackObject mTrackObject = (TrackObject) it.next();
                    boolean isAllowRemoveToList = true;
                    Iterator it2 = this.listPlaylistObjects.iterator();
                    while (it2.hasNext()) {
                        if (((PlaylistObject) it2.next()).isSongAlreadyExited(mTrackObject.getId())) {
                            isAllowRemoveToList = false;
                            break;
                        }
                    }
                    if (isAllowRemoveToList) {
                        this.listSavedTrackObjects.remove(mTrackObject);
                        isNeedToSaveTrack = true;
                    }
                }
                mListTrack.clear();
            }
            final boolean isGlobal = isNeedToSaveTrack;
            DBListExcuteAction.getInstance().queueAction(new IDBCallback() {
                public void onAction() {
                    TotalDataManager.this.savePlaylistObjects(mContext);
                    if (isGlobal) {
                        TotalDataManager.this.saveTrackObjects(mContext);
                    }
                }
            });
        }
    }

    public void removePlaylistObject(final Context mContext, int playlistId) {
        if (this.listPlaylistObjects != null && this.listPlaylistObjects.size() > 0) {
            Iterator<PlaylistObject> mListIterator = this.listPlaylistObjects.iterator();
            while (mListIterator.hasNext()) {
                if (((PlaylistObject) mListIterator.next()).getId() == ((long) playlistId)) {
                    mListIterator.remove();
                    break;
                }
            }
            DBListExcuteAction.getInstance().queueAction(new IDBCallback() {
                public void onAction() {
                    TotalDataManager.this.savePlaylistObjects(mContext);
                }
            });
        }
    }

    public synchronized void addTrackToPlaylist(final DBFragmentActivity mContext, TrackObject mParentTrackObject, PlaylistObject mPlaylistObject, boolean isShowMsg, IDBCallback mCallback) {
        if (!(mParentTrackObject == null || mPlaylistObject == null)) {
            if (!mPlaylistObject.isSongAlreadyExited(mParentTrackObject.getId())) {
                TrackObject mTrackObject = mParentTrackObject.clone();
                mPlaylistObject.addTrackObject(mTrackObject, true);
                boolean isAllowAddToList = true;
                Iterator it = this.listSavedTrackObjects.iterator();
                while (it.hasNext()) {
                    if (((TrackObject) it.next()).getId() == mTrackObject.getId()) {
                        isAllowAddToList = false;
                    }
                }
                if (isAllowAddToList) {
                    this.listSavedTrackObjects.add(mTrackObject);
                }
                if (mCallback != null) {
                    mCallback.onAction();
                }
                DBListExcuteAction.getInstance().queueAction(new IDBCallback() {
                    public void onAction() {
                        TotalDataManager.this.savePlaylistObjects(mContext);
                        TotalDataManager.this.saveTrackObjects(mContext);
                    }
                });
            } else if (isShowMsg) {
                mContext.runOnUiThread(new Runnable() {
                    public void run() {
                        mContext.showToast((int) R.string.info_song_already_playlist);
                    }
                });
            }
        }
    }

    public synchronized void savePlaylistObjects(Context mContext) {
        if (ApplicationUtils.hasSDcard()) {
            File mFile = new File(new StringBuilder(String.valueOf(Environment.getExternalStorageDirectory().toString())).append("/").append(ICloudMusicPlayerConstants.NAME_FOLDER_CACHE).toString());
            if (!mFile.exists()) {
                mFile.mkdirs();
            }
            JSONArray mJsArray = new JSONArray();
            if (this.listPlaylistObjects != null) {
                Iterator it = this.listPlaylistObjects.iterator();
                while (it.hasNext()) {
                    mJsArray.put(((PlaylistObject) it.next()).toJson());
                }
            }
            //DBLog.m25d(TAG, "=============>savePlaylistObjects=" + mJsArray.toString());
            IOUtils.writeString(mFile.getAbsolutePath(), ICloudMusicPlayerConstants.NAME_PLAYLIST_FILE, mJsArray.toString());
        }
    }

    public synchronized void saveTrackObjects(Context mContext) {
        if (ApplicationUtils.hasSDcard()) {
            File mFile = new File(new StringBuilder(String.valueOf(Environment.getExternalStorageDirectory().toString())).append("/").append(ICloudMusicPlayerConstants.NAME_FOLDER_CACHE).toString());
            if (!mFile.exists()) {
                mFile.mkdirs();
            }
            JSONArray mJsArray = new JSONArray();
            if (this.listSavedTrackObjects != null && this.listSavedTrackObjects.size() > 0) {
                Iterator it = this.listSavedTrackObjects.iterator();
                while (it.hasNext()) {
                    mJsArray.put(((TrackObject) it.next()).toJsonObject());
                }
            }
            //DBLog.m25d(TAG, "=============>saveTrackObjects=" + mJsArray.toString());
            IOUtils.writeString(mFile.getAbsolutePath(), ICloudMusicPlayerConstants.NAME_SAVED_TRACK, mJsArray.toString());
        }
    }

    public synchronized void removeTrackToPlaylist(final DBFragmentActivity mContext, TrackObject mTrackObject, PlaylistObject mPlaylistObject, IDBCallback mCallback) {
        if (!(mTrackObject == null || mPlaylistObject == null)) {
            mPlaylistObject.removeTrackObject(mTrackObject);
            boolean isAllowRemoveToList = true;
            Iterator it = this.listPlaylistObjects.iterator();
            while (it.hasNext()) {
                if (((PlaylistObject) it.next()).isSongAlreadyExited(mTrackObject.getId())) {
                    isAllowRemoveToList = false;
                    break;
                }
            }
            //DBLog.m25d(TAG, "============>removeTrackToPlaylist=" + isAllowRemoveToList);
            if (isAllowRemoveToList) {
                this.listSavedTrackObjects.remove(mTrackObject);
            }
            if (mCallback != null) {
                mCallback.onAction();
            }
            DBListExcuteAction.getInstance().queueAction(new IDBCallback() {
                public void onAction() {
                    TotalDataManager.this.savePlaylistObjects(mContext);
                    TotalDataManager.this.saveTrackObjects(mContext);
                }
            });
        }
    }
}
