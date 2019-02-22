package com.mozia.VmusicBox.fragment;

import android.content.ContentValues;
import android.database.Cursor;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore.Audio.Media;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.mozia.VmusicBox.MainActivity;
import com.mozia.VmusicBox.R;
import com.mozia.VmusicBox.adapter.LibraryAdapter;
import com.mozia.VmusicBox.adapter.LibraryAdapter.OnDownloadedListener;
import com.mozia.VmusicBox.constants.ICloudMusicPlayerConstants;
import com.mozia.VmusicBox.dataMng.TotalDataManager;
import com.mozia.VmusicBox.soundclound.ISoundCloundConstants;
import com.mozia.VmusicBox.soundclound.SoundCloundDataMng;
import com.mozia.VmusicBox.soundclound.object.TrackObject;
import com.ypyproductions.abtractclass.fragment.DBFragment;
import com.ypyproductions.task.DBTask;
import com.ypyproductions.task.IDBCallback;
import com.ypyproductions.task.IDBTaskListener;
import com.ypyproductions.utils.DBListExcuteAction;
import com.ypyproductions.utils.StringUtils;

import java.io.File;
import java.util.ArrayList;

import static android.R.attr.id;

public class FragmentLibrary extends DBFragment implements ICloudMusicPlayerConstants, ISoundCloundConstants {
    public static final String TAG = FragmentLibrary.class.getSimpleName();
    private LibraryAdapter mAdapter;
    private MainActivity mContext;
    private DBTask mDBTask;
    private ArrayList<TrackObject> mListTrackObjects;
    private ListView mListView;
    private TextView mTvNoResult;

    class C08161 implements IDBTaskListener {
        C08161() {
        }

        public void onPreExcute() {
            FragmentLibrary.this.mContext.showProgressDialog();
        }

        public void onDoInBackground() {
            TotalDataManager.getInstance().readLibraryTrack(FragmentLibrary.this.mContext);
        }

        public void onPostExcute() {
            FragmentLibrary.this.mContext.dimissProgressDialog();
            FragmentLibrary.this.setUpInfo(TotalDataManager.getInstance().getListLibraryTrackObjects());
        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.mRootView = inflater.inflate(R.layout.fragment_downloaded, container, false);
        return this.mRootView;
    }

    public View onInflateLayout(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.mRootView = inflater.inflate(R.layout.fragment_downloaded, container, false);
        return this.mRootView;
    }

    public void findView() {
        setAllowFindViewContinous(true);
        this.mContext = (MainActivity) getActivity();
        this.mListView = (ListView) this.mRootView.findViewById(R.id.list_tracks);
        this.mTvNoResult = (TextView) this.mRootView.findViewById(R.id.tv_no_result);
        this.mTvNoResult.setTypeface(this.mContext.mTypefaceNormal);
        ArrayList<TrackObject> mListDownloaded = TotalDataManager.getInstance().getListLibraryTrackObjects();
        if (mListDownloaded == null) {
            startLoadFromLibrary();
        } else {
            setUpInfo(mListDownloaded);
        }
    }

    public void startLoadFromLibrary() {
        this.mDBTask = new DBTask(new C08161());
        this.mDBTask.execute(new Void[0]);
    }

    private void setUpInfo(final ArrayList<TrackObject> mListNewTrackObjects) {
        this.mListTrackObjects = mListNewTrackObjects;
        if (mListNewTrackObjects != null) {
            this.mTvNoResult.setVisibility(mListNewTrackObjects.size() > 0 ? 8 : 0);
            this.mListView.setVisibility(0);
            this.mAdapter = new LibraryAdapter(this.mContext, mListNewTrackObjects, this.mContext.mTypefaceBold, this.mContext.mTypefaceLight);
            this.mListView.setAdapter(this.mAdapter);
            this.mAdapter.setOnDownloadedListener(new OnDownloadedListener() {
                public void onPlayItem(TrackObject mTrackObject) {
                    SoundCloundDataMng.getInstance().setListPlayingTrackObjects(mListNewTrackObjects);
                    FragmentLibrary.this.mContext.setInfoForPlayingTrack(mTrackObject, true, true);
                }

                public void onDeleteItem(final TrackObject mTrackObject) {
                    FragmentLibrary.this.mContext.showProgressDialog();
                    final File mOutPutFile = new File(mTrackObject.getPath());
                    if (mOutPutFile.exists() && mOutPutFile.isFile()) {
                        DBListExcuteAction.getInstance().queueAction(new IDBCallback() {

                            class C05361 implements Runnable {
                                C05361() {
                                }

                                public void run() {
                                    FragmentLibrary.this.mContext.dimissProgressDialog();
                                    FragmentLibrary.this.notifyData();
                                }
                            }

                            public void onAction() {
                                if (mOutPutFile.delete()) {
                                    FragmentLibrary.this.mContext.deleteSongFromMediaStore(mTrackObject.getId());
                                    FragmentLibrary.this.mListTrackObjects.remove(mTrackObject);
                                }
                                FragmentLibrary.this.mContext.runOnUiThread(new C05361());
                            }
                        });
                    }
                }

                public void onSetAsRingtone(TrackObject mTrackObject) {
                    FragmentLibrary.this.saveAsRingtone(mTrackObject);
                }

                public void onSetAsNotification(TrackObject mTrackObject) {
                    FragmentLibrary.this.saveAsNotification(mTrackObject);
                }

                public void onAddToPlaylist(TrackObject mTrackObject) {
                    FragmentLibrary.this.mContext.showDialogPlaylist(mTrackObject);
                }
            });
            return;
        }
        this.mTvNoResult.setVisibility(0);
    }

    public void notifyData() {
        if (this.mAdapter != null) {
            this.mAdapter.notifyDataSetChanged();
        }
        TextView textView = this.mTvNoResult;
        int i = (this.mListTrackObjects == null || this.mListTrackObjects.size() <= 0) ? 0 : 8;
        textView.setVisibility(i);
    }

    private void saveAsRingtone(TrackObject mSongObject) {
        File mOutPutFile = new File(mSongObject.getPath());
        if (mOutPutFile != null && mOutPutFile.isFile()) {
            Uri mUri;
            ContentValues values = new ContentValues();
            values.put("_data", mOutPutFile.getAbsolutePath());
            values.put("title", mSongObject.getTitle());
            values.put("mime_type", "audio/*");
            values.put("is_ringtone", Boolean.valueOf(true));
            if (StringUtils.isEmptyString(getIdFromContentUri(mOutPutFile.getAbsolutePath()))) {
                mUri = this.mContext.getContentResolver().insert(Media.getContentUriForPath(mOutPutFile.getAbsolutePath()), values);
            } else {
                this.mContext.getContentResolver().update(Media.EXTERNAL_CONTENT_URI, values, "_id = ?", new String[]{getIdFromContentUri(mOutPutFile.getAbsolutePath())});
                mUri = Uri.parse(String.format(ICloudMusicPlayerConstants.FORMAT_URI, new Object[]{id}));
            }
            RingtoneManager.setActualDefaultRingtoneUri(this.mContext, 1, mUri);
            this.mContext.showToast((int) R.string.info_set_ringtone_successfully);
        }
    }

    private void saveAsNotification(TrackObject mSongObject) {
        File mOutPutFile = new File(mSongObject.getPath());
        if (mOutPutFile != null && mOutPutFile.isFile()) {
            Uri mUri;
            ContentValues values = new ContentValues();
            values.put("_data", mOutPutFile.getAbsolutePath());
            values.put("title", mSongObject.getTitle());
            values.put("mime_type", "audio/*");
            values.put("is_notification", Boolean.valueOf(true));
            if (StringUtils.isEmptyString(getIdFromContentUri(mOutPutFile.getAbsolutePath()))) {
                mUri = this.mContext.getContentResolver().insert(Media.getContentUriForPath(mOutPutFile.getAbsolutePath()), values);
            } else {
                this.mContext.getContentResolver().update(Media.EXTERNAL_CONTENT_URI, values, "_id = ?", new String[]{getIdFromContentUri(mOutPutFile.getAbsolutePath())});
                mUri = Uri.parse(String.format(ICloudMusicPlayerConstants.FORMAT_URI, new Object[]{id}));
            }
            RingtoneManager.setActualDefaultRingtoneUri(this.mContext, 2, mUri);
            this.mContext.showToast((int) R.string.info_set_notification_successfully);
        }
    }

    private String getIdFromContentUri(String path) {
        if (path != null) {
            try {
                String[] filePathColumn = new String[]{"_id"};
                Cursor cursor = this.mContext.getContentResolver().query(Media.EXTERNAL_CONTENT_URI, filePathColumn, "_data = ?", new String[]{path}, null);
                if (cursor != null && cursor.moveToFirst()) {
                    String id = cursor.getString(cursor.getColumnIndex(filePathColumn[0]));
                    cursor.close();
                    return id;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
