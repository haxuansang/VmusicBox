package com.mozia.VmusicBox.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.mozia.VmusicBox.MainActivity;
import com.mozia.VmusicBox.R;
import com.mozia.VmusicBox.adapter.GenreAdapter;
import com.mozia.VmusicBox.adapter.GernerAdapterN;
import com.mozia.VmusicBox.adapter.GridSpacingItemDecoration;
import com.mozia.VmusicBox.constants.ICloudMusicPlayerConstants;
import com.mozia.VmusicBox.dataMng.JsonParsingUtils;
import com.mozia.VmusicBox.dataMng.TotalDataManager;
import com.mozia.VmusicBox.object.GenreObject;
import com.mozia.VmusicBox.setting.ISettingConstants;
import com.ypyproductions.abtractclass.fragment.DBFragment;
import com.ypyproductions.task.DBTask;
import com.ypyproductions.task.IDBTaskListener;
import com.ypyproductions.utils.IOUtils;

import java.util.ArrayList;
import java.util.Locale;

public class FragmentMusicGenre extends DBFragment implements ICloudMusicPlayerConstants, ISettingConstants {
    public static final String TAG = FragmentMusicGenre.class.getSimpleName();
    private MainActivity mContext;
    private DBTask mDBTask;
//    private GenreAdapter mGenreAdapter;
    private GernerAdapterN mGenreAdapter;
    private Handler mHandler = new Handler();
    private ArrayList<GenreObject> mListGenreObjects;
    private RecyclerView mListView;
    public static boolean isCheck = false;
    private boolean checkUseUi=false ;
    public   void checkHomeEnable()
    {
//        GenreObject mGenreObject = (GenreObject) FragmentMusicGenre.this.mListGenreObjects.get(1);
//        if (mGenreObject != null) {
            FragmentMusicGenre.this.mContext.processSearchData("disco", true);
//        }
    }
    class C05372 implements OnItemClickListener {
        C05372() {
        }

        public void onItemClick(AdapterView<?> adapterView, View arg1, int postion, long id) {
            GenreObject mGenreObject = (GenreObject) FragmentMusicGenre.this.mListGenreObjects.get(postion);
            if (mGenreObject != null) {
                FragmentMusicGenre.this.mContext.processSearchData(mGenreObject.getKeyword(), true);
            }
        }
    }

    class C08191 implements IDBTaskListener {
        C08191() {
        }

        public void onPreExcute() {
            FragmentMusicGenre.this.mContext.showProgressDialog();
        }

        public void onDoInBackground() {
            String fileName = "";
            if (Locale.getDefault().getCountry().toLowerCase(Locale.US).equalsIgnoreCase("BR")) {
                fileName = String.format(ICloudMusicPlayerConstants.FILE_GENRE, new Object[]{Locale.getDefault().getCountry().toLowerCase(Locale.US)});
            } else {
                fileName = String.format(ICloudMusicPlayerConstants.FILE_GENRE, new Object[]{"en"});
            }
            ArrayList<GenreObject> mListGenres = JsonParsingUtils.parsingGenreObject(IOUtils.readStringFromAssets(FragmentMusicGenre.this.mContext, fileName));
            if (mListGenres != null) {
                TotalDataManager.getInstance().setListGenreObjects(mListGenres);
                FragmentMusicGenre.this.mListGenreObjects = mListGenres;
            }
        }

        public void onPostExcute() {
            FragmentMusicGenre.this.mContext.dimissProgressDialog();
            FragmentMusicGenre.this.setUpInfo();
        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.mRootView = inflater.inflate(R.layout.fragment_genre, container, false);
        return this.mRootView;
    }

    public View onInflateLayout(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.mRootView = inflater.inflate(R.layout.fragment_genre, container, false);
        return this.mRootView;
    }

    public void findView() {
        this.mContext = (MainActivity) getActivity();
        this.mListView = (RecyclerView) this.mRootView.findViewById(R.id.list_genres);
        setAllowFindViewContinous(true);
        startLoadGenre();
    }

    private void startLoadGenre() {
        this.mListGenreObjects = TotalDataManager.getInstance().getListGenreObjects();
        if (this.mListGenreObjects == null || this.mListGenreObjects.size() < 0) {
            startGetData();
        } else {
            setUpInfo();
        }
    }

    private void startGetData() {
        this.mDBTask = new DBTask(new C08191());
        this.mDBTask.execute(new Void[0]);
    }

    public void onDestroy() {
        super.onDestroy();
        this.mHandler.removeCallbacksAndMessages(null);
        if (this.mDBTask != null) {
            this.mDBTask.cancel(true);
            this.mDBTask = null;
        }
    }

    private void setUpInfo() {
        this.mListView.setAdapter(null);
        if (this.mGenreAdapter != null) {
            this.mGenreAdapter = null;
        }
        mGenreAdapter=new GernerAdapterN(getContext(),this.mListGenreObjects);
        mGenreAdapter.setOnItemClickListener(new GernerAdapterN.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int position) {
                GenreObject mGenreObject = (GenreObject) FragmentMusicGenre.this.mListGenreObjects.get(position);
                if (mGenreObject != null) {
                    FragmentMusicGenre.this.mContext.processSearchData(mGenreObject.getKeyword(), true);
                }
            }
        });
        mListView.setAdapter(mGenreAdapter);
        mListView.setLayoutManager(new GridLayoutManager(mContext, 3));
        int spanCount = 3; // 3 columns
        int spacing = 10; // 50px
        boolean includeEdge = false;
        Log.d(TAG, "setUpInfo: ");
        if(!checkUseUi) mListView.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, includeEdge));
        //this.mGenreAdapter = new GenreAdapter(this.mContext, this.mListGenreObjects, this.mContext.mTypefaceNormal);
       // this.mListView.setAdapter(this.mGenreAdapter);
        checkUseUi=true;
        final String welcomeScreenShownPref = "welcomeScreenShown";

        SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        Boolean welcomeScreenShown = mPrefs.getBoolean(welcomeScreenShownPref, false);
        if (!welcomeScreenShown) {
            // here you can launch another activity if you like
            if (isCheck == false)
            {
                checkHomeEnable();
                isCheck= true;

            }
            SharedPreferences.Editor editor = mPrefs.edit();
            editor.putBoolean(welcomeScreenShownPref, true);
            editor.commit(); // Very important to save the preference

        }


       // this.mListView.setOnItemClickListener(new C05372());
    }
}
