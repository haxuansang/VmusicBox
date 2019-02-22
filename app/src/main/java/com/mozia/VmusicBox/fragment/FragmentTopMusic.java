package com.mozia.VmusicBox.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.mozia.VmusicBox.MainActivity;
import com.mozia.VmusicBox.R;
import com.mozia.VmusicBox.adapter.TopMusicAdapter;
import com.mozia.VmusicBox.adapter.TopMusicAdapter.OnTopMusicListener;
import com.mozia.VmusicBox.constants.ICloudMusicPlayerConstants;
import com.mozia.VmusicBox.dataMng.TotalDataManager;
import com.mozia.VmusicBox.object.TopMusicObject;
import com.mozia.VmusicBox.setting.ISettingConstants;
import com.mozia.VmusicBox.setting.SettingManager;
import com.mozia.VmusicBox.soundclound.ISoundCloundConstants;
import com.ypyproductions.abtractclass.fragment.DBFragment;
import com.ypyproductions.task.DBTask;
import com.ypyproductions.task.IDBTaskListener;
import com.ypyproductions.utils.ApplicationUtils;
import com.ypyproductions.utils.StringUtils;

import java.util.ArrayList;

public class FragmentTopMusic extends DBFragment implements ICloudMusicPlayerConstants, ISoundCloundConstants, ISettingConstants {
    public static final String TAG = FragmentTopMusic.class.getSimpleName();
    private TopMusicAdapter mAdapter;
    private MainActivity mContext;
    private DBTask mDBTask;
    private ArrayList<TopMusicObject> mListHotObjects;
    private PullToRefreshListView mListView;
    private TextView mTvNoResult;
    protected ProgressDialog progressDialog;
    private View rootViewBack;

    class C08301 implements OnRefreshListener<ListView> {
        C08301() {
        }

        public void onRefresh(PullToRefreshBase<ListView> pullToRefreshBase) {
            FragmentTopMusic.this.startGetData(true);
        }
    }

    class C08312 implements OnTopMusicListener {
        C08312() {
        }

        public void onSearchDetail(TopMusicObject mTrackObject) {
            String name = mTrackObject.getName() + " - " + mTrackObject.getArtist();
            if (!StringUtils.isEmptyString(name)) {
                FragmentTopMusic.this.mContext.processSearchData(name, false);
            }
        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.rootViewBack = inflater.inflate(R.layout.fragment_hot, container, false);
        return this.rootViewBack;
    }

    public View onInflateLayout(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.rootViewBack = inflater.inflate(R.layout.fragment_hot, container, false);
        return this.rootViewBack;
    }

    public void findView() {
        intialize();
        startGetData(false);
    }

    private void intialize() {
        setAllowFindViewContinous(true);
        if (this.mRootView == null) {
            this.mRootView = this.rootViewBack;
        }
        this.mContext = (MainActivity) getActivity();
        this.mListView = (PullToRefreshListView) this.mRootView.findViewById(R.id.list_hot_music);
        this.mTvNoResult = (TextView) this.mRootView.findViewById(R.id.tv_no_result);
        this.mTvNoResult.setTypeface(this.mContext.mTypefaceNormal);
        this.mListView.setOnRefreshListener(new C08301());
    }

    public void setUpInfo(boolean isRefresh, ArrayList<TopMusicObject> mListNewTrackObjects) {
        if (this.mListView == null) {
            intialize();
        }
        this.mListView.setAdapter(null);
        if (isRefresh && this.mListHotObjects != null) {
            this.mListHotObjects.clear();
            this.mListHotObjects = null;
        }
        this.mListHotObjects = mListNewTrackObjects;
        if (mListNewTrackObjects == null || mListNewTrackObjects.size() <= 0) {
            this.mTvNoResult.setVisibility(0);
            return;
        }
        this.mTvNoResult.setVisibility(8);
        this.mListView.setVisibility(0);
        this.mAdapter = new TopMusicAdapter(this.mContext, mListNewTrackObjects, this.mContext.mTypefaceBold, this.mContext.mTypefaceLight, this.mContext.mImgTrackOptions);
        this.mListView.setAdapter(this.mAdapter);
        this.mAdapter.setOnTopMusicListener(new C08312());
    }

    public void startGetData(final boolean isRefresh) {
        if (!isRefresh) {
            ArrayList<TopMusicObject> mListTopMusic = TotalDataManager.getInstance().getListTopMusicObjects();
            if (mListTopMusic != null && mListTopMusic.size() > 0) {
                this.mListView.onRefreshComplete();
                setUpInfo(isRefresh, mListTopMusic);
                return;
            }
        }
        if (ApplicationUtils.isOnline(this.mContext)) {
            this.mDBTask = new DBTask(new IDBTaskListener() {
                private ArrayList<TopMusicObject> mListNewTopObjects;

                public void onPreExcute() {
                    FragmentTopMusic.this.mTvNoResult.setVisibility(8);
                    if (!isRefresh) {
                        FragmentTopMusic.this.mContext.showProgressDialog();
                    }
                }

                public void onDoInBackground() {
                    this.mListNewTopObjects = FragmentTopMusic.this.mContext.mSoundClound.getListTopMusic(SettingManager.getLanguage(FragmentTopMusic.this.mContext), 80);
                    if (this.mListNewTopObjects != null && this.mListNewTopObjects.size() > 0) {
                        TotalDataManager.getInstance().setListTopMusicObjects(this.mListNewTopObjects);
                    }
                }

                public void onPostExcute() {
                    FragmentTopMusic.this.mContext.dimissProgressDialog();
                    FragmentTopMusic.this.mListView.onRefreshComplete();
                    FragmentTopMusic.this.mContext.hiddenVirtualKeyBoard();
                    FragmentTopMusic.this.setUpInfo(true, this.mListNewTopObjects);
                }
            });
            this.mDBTask.execute(new Void[0]);
            return;
        }
        this.mListView.onRefreshComplete();
        this.mContext.showToast((int) R.string.info_lose_internet);
        if (this.mAdapter == null) {
            this.mTvNoResult.setVisibility(0);
        }
    }
}
