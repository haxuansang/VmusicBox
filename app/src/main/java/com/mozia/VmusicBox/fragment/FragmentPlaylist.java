package com.mozia.VmusicBox.fragment;

import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.mozia.VmusicBox.MainActivity;
import com.mozia.VmusicBox.R;
import com.mozia.VmusicBox.adapter.DetailPlaylistAdapter;
import com.mozia.VmusicBox.adapter.DetailPlaylistAdapter.IDetailPlaylistAdapterListener;
import com.mozia.VmusicBox.adapter.PlaylistAdapter;
import com.mozia.VmusicBox.adapter.PlaylistAdapter.OnPlaylistListener;
import com.mozia.VmusicBox.constants.ICloudMusicPlayerConstants;
import com.mozia.VmusicBox.dataMng.TotalDataManager;
import com.mozia.VmusicBox.object.PlaylistObject;
import com.mozia.VmusicBox.soundclound.SoundCloundDataMng;
import com.mozia.VmusicBox.soundclound.object.TrackObject;
import com.ypyproductions.abtractclass.fragment.DBFragment;
import com.ypyproductions.task.DBTask;
import com.ypyproductions.task.IDBCallback;
import com.ypyproductions.task.IDBTaskListener;
import com.ypyproductions.utils.ApplicationUtils;

import java.io.File;
import java.util.ArrayList;

public class FragmentPlaylist extends DBFragment implements ICloudMusicPlayerConstants {
    public static final String TAG = FragmentPlaylist.class.getSimpleName();
    private Button mBtnBack;
    private MainActivity mContext;
    private DBTask mDBTask;
    private DetailPlaylistAdapter mDetailPlaylistAdapter;
    private View mHeaderDetailPlaylistView;
    private View mHeaderPlaylistView;
    private ArrayList<PlaylistObject> mListPlaylistObjects;
    private ListView mListViewDetailPlaylist;
    private ListView mListViewPlaylist;
    private PlaylistAdapter mPlaylistAdapter;
    private TextView mTvNamePlaylist;

    class C05381 implements OnClickListener {
        C05381() {
        }

        public void onClick(View v) {
            FragmentPlaylist.this.createDialogPlaylist(false, null);
        }
    }

    class C05392 implements OnClickListener {
        C05392() {
        }

        public void onClick(View v) {
            FragmentPlaylist.this.backToPlaylist();
        }
    }

    class C08214 implements OnPlaylistListener {
        C08214() {
        }

        public void onRenamePlaylist(PlaylistObject mPlaylistObject) {
            FragmentPlaylist.this.createDialogPlaylist(true, mPlaylistObject);
        }

        public void onPlayAllMusic(PlaylistObject mPlaylistObject) {
            ArrayList<TrackObject> mListTrackObjects = mPlaylistObject.getListTrackObjects();
            if (mListTrackObjects == null || mListTrackObjects.size() <= 0) {
                FragmentPlaylist.this.mContext.showToast((int) R.string.info_nosong_playlist);
                return;
            }
            SoundCloundDataMng.getInstance().setListPlayingTrackObjects(mListTrackObjects);
            FragmentPlaylist.this.mContext.setInfoForPlayingTrack((TrackObject) mListTrackObjects.get(0), true, true);
        }

        public void onDeletePlaylist(PlaylistObject mPlaylistObject) {
            FragmentPlaylist.this.showDialogDelete(mPlaylistObject);
        }

        public void onGoToDetail(PlaylistObject mPlaylistObject) {
            FragmentPlaylist.this.showDetailPlaylist(mPlaylistObject);
        }
    }

    class C08257 implements IDBCallback {
        C08257() {
        }

        public void onAction() {
            FragmentPlaylist.this.notifyData();
        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.mRootView = inflater.inflate(R.layout.fragment_playlist_home, container, false);
        return this.mRootView;
    }

    public View onInflateLayout(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.mRootView = inflater.inflate(R.layout.fragment_playlist_home, container, false);
        return this.mRootView;
    }

    public void findView() {
        setAllowFindViewContinous(true);
        this.mContext = (MainActivity) getActivity();
        this.mListViewPlaylist = (ListView) this.mRootView.findViewById(R.id.list_playlist);
        this.mListViewDetailPlaylist = (ListView) this.mRootView.findViewById(R.id.list_detail_playlist);
        setUpHeaderForPlaylist();
        setUpHeaderForDetailPlaylist();
        startGetPlaylist();
    }

    private void setUpHeaderForPlaylist() {
        this.mHeaderPlaylistView = this.mRootView.findViewById(R.id.header_playlist);
        ((TextView) this.mHeaderPlaylistView.findViewById(R.id.tv_add_new_playlist)).setTypeface(this.mContext.mTypefaceBold);
        this.mHeaderPlaylistView.setOnClickListener(new C05381());
    }

    private void setUpHeaderForDetailPlaylist() {
        this.mHeaderDetailPlaylistView = this.mRootView.findViewById(R.id.header_detail_playlist);
        this.mTvNamePlaylist = (TextView) this.mHeaderDetailPlaylistView.findViewById(R.id.tv_name_playlist);
        this.mTvNamePlaylist.setTypeface(this.mContext.mTypefaceBold);
        this.mBtnBack = (Button) this.mHeaderDetailPlaylistView.findViewById(R.id.btn_back);
        this.mBtnBack.setOnClickListener(new C05392());
    }

    public boolean backToPlaylist() {
        if (this.mListViewDetailPlaylist.getVisibility() != View.VISIBLE) {
            return false;
        }
        this.mListViewDetailPlaylist.setVisibility(View.GONE);
        this.mListViewPlaylist.setVisibility(View.VISIBLE);
        this.mHeaderDetailPlaylistView.setVisibility(View.GONE);
        this.mHeaderPlaylistView.setVisibility(View.VISIBLE);
        return true;
    }

    private void showDetailPlaylist(PlaylistObject mPlaylistObject) {
        this.mListViewDetailPlaylist.setVisibility(View.VISIBLE);
        this.mListViewPlaylist.setVisibility(View.GONE);
        this.mHeaderDetailPlaylistView.setVisibility(View.VISIBLE);
        this.mHeaderPlaylistView.setVisibility(View.GONE);
        this.mTvNamePlaylist.setText(mPlaylistObject.getName());
        setUpInfoDetailPlaylist(mPlaylistObject);
    }

    private void startGetPlaylist() {
        if (ApplicationUtils.hasSDcard()) {
            final File mFile = new File(new StringBuilder(String.valueOf(Environment.getExternalStorageDirectory().toString())).append("/").append(ICloudMusicPlayerConstants.NAME_FOLDER_CACHE).toString());
            if (!mFile.exists()) {
                mFile.mkdirs();
            }
            this.mDBTask = new DBTask(new IDBTaskListener() {
                private ArrayList<PlaylistObject> mListPlaylist;

                public void onPreExcute() {
                    FragmentPlaylist.this.mContext.showProgressDialog();
                }

                public void onDoInBackground() {
                    TotalDataManager.getInstance().readSavedTrack(FragmentPlaylist.this.mContext, mFile);
                    TotalDataManager.getInstance().readPlaylistCached(FragmentPlaylist.this.mContext, mFile);
                    this.mListPlaylist = TotalDataManager.getInstance().getListPlaylistObjects();
                }

                public void onPostExcute() {
                    FragmentPlaylist.this.mContext.dimissProgressDialog();
                    FragmentPlaylist.this.setUpInfoListPlaylist(this.mListPlaylist);
                }
            });
            this.mDBTask.execute(new Void[0]);
        }
    }

    private void setUpInfoListPlaylist(ArrayList<PlaylistObject> mListPlaylistObjects) {
        this.mListPlaylistObjects = mListPlaylistObjects;
        if (this.mListPlaylistObjects != null) {
            this.mPlaylistAdapter = new PlaylistAdapter(this.mContext, mListPlaylistObjects, this.mContext.mTypefaceBold, this.mContext.mTypefaceLight);
            this.mListViewPlaylist.setAdapter(this.mPlaylistAdapter);
            this.mPlaylistAdapter.seOnPlaylistListener(new C08214());
        }
    }

    private void setUpInfoDetailPlaylist(final PlaylistObject mPlaylistObject) {
        ArrayList<TrackObject> mListTrackObjects = mPlaylistObject.getListTrackObjects();
        if (mListTrackObjects == null) {
            return;
        }
        if (this.mDetailPlaylistAdapter == null) {
            this.mDetailPlaylistAdapter = new DetailPlaylistAdapter(this.mContext, mListTrackObjects, this.mContext.mTypefaceBold, this.mContext.mTypefaceLight, this.mContext.mImgTrackOptions);
            this.mListViewDetailPlaylist.setAdapter(this.mDetailPlaylistAdapter);
            this.mDetailPlaylistAdapter.setDetailPlaylistAdapterListener(new IDetailPlaylistAdapterListener() {

                class C08221 implements IDBCallback {
                    C08221() {
                    }

                    public void onAction() {
                        if (FragmentPlaylist.this.mDetailPlaylistAdapter != null) {
                            FragmentPlaylist.this.mDetailPlaylistAdapter.notifyDataSetChanged();
                        }
                        if (FragmentPlaylist.this.mPlaylistAdapter != null) {
                            FragmentPlaylist.this.mPlaylistAdapter.notifyDataSetChanged();
                        }
                    }
                }

                public void onRemoveToPlaylist(TrackObject mTrackObject) {
                    TotalDataManager.getInstance().removeTrackToPlaylist(FragmentPlaylist.this.mContext, mTrackObject, mPlaylistObject, new C08221());
                }

                public void onPlayingTrack(TrackObject mTrackObject) {
                    SoundCloundDataMng.getInstance().setListPlayingTrackObjects((ArrayList<TrackObject>) mDetailPlaylistAdapter.getListObjects());
                    FragmentPlaylist.this.mContext.setInfoForPlayingTrack(mTrackObject, true, true);
                }
            });
            return;
        }
        this.mDetailPlaylistAdapter.setListObjects(mListTrackObjects, false);
    }

    private void showDialogDelete(final PlaylistObject mPlaylistObject) {
        this.mContext.showFullDialog((int) R.string.title_confirm, (int) R.string.info_delete_playlist, (int) R.string.title_ok, (int) R.string.title_cancel, new IDBCallback() {
            public void onAction() {
                TotalDataManager.getInstance().removePlaylistObject(FragmentPlaylist.this.mContext, mPlaylistObject);
                if (FragmentPlaylist.this.mPlaylistAdapter != null) {
                    FragmentPlaylist.this.mPlaylistAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    private void createDialogPlaylist(boolean isEdit, PlaylistObject mPlaylistObject) {
        this.mContext.createDialogPlaylist(isEdit, mPlaylistObject, new C08257());
    }

    public void notifyData() {
        if (this.mPlaylistAdapter != null) {
            this.mPlaylistAdapter.notifyDataSetChanged();
        }
    }
}
