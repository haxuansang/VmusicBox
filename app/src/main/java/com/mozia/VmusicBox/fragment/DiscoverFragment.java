package com.mozia.VmusicBox.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mozia.VmusicBox.MainActivity;
import com.mozia.VmusicBox.R;
import com.mozia.VmusicBox.adapter.DiscoverHizRecyclerViewAdapter;
import com.mozia.VmusicBox.adapter.DiscoverVerRecyclerViewAdapter;
import com.mozia.VmusicBox.constants.ICloudMusicPlayerConstants;
import com.mozia.VmusicBox.dataMng.JsonParsingUtils;
import com.mozia.VmusicBox.dataMng.TotalDataManager;
import com.mozia.VmusicBox.object.GenreObject;
import com.mozia.VmusicBox.object.TopMusicObject;
import com.mozia.VmusicBox.setting.SettingManager;
import com.ypyproductions.task.DBTask;
import com.ypyproductions.task.IDBTaskListener;
import com.ypyproductions.utils.ApplicationUtils;
import com.ypyproductions.utils.IOUtils;
import com.ypyproductions.utils.StringUtils;

import java.util.ArrayList;
import java.util.Locale;

public class DiscoverFragment extends Fragment {
    private RecyclerView recyclerViewTop;
    private ArrayList<TopMusicObject> mTopMusic;
    private ArrayList<GenreObject> mGenreMusic;
    private RecyclerView recyclerViewBottom;
    private DiscoverHizRecyclerViewAdapter hizRecyclerViewAdapter;
    private DiscoverVerRecyclerViewAdapter verRecyclerViewAdapter;
    private MainActivity mContext;
    private DBTask mDBTask;
    public DiscoverFragment() {
    }

    public static DiscoverFragment newInstance() {
        DiscoverFragment fragment = new DiscoverFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView=inflater.inflate(R.layout.fragment_discover, container, false);
        recyclerViewTop=(RecyclerView) rootView.findViewById(R.id.recycler_discover_tophit);
        recyclerViewBottom=(RecyclerView) rootView.findViewById(R.id.recycler_discover_genres);
        mContext=(MainActivity)getActivity();
        getDataTopMusic();
        getDataGenresMusic();
        return rootView;
    }

    private void getDataGenresMusic() {
        mGenreMusic=TotalDataManager.getInstance().getListGenreObjects();
        if (mGenreMusic==null) mGenreMusic = new ArrayList<>();
        if(mGenreMusic!=null || mGenreMusic.size()<=0 )
            setUpAdapterGenreMusic(mGenreMusic);
        else{
            this.mDBTask = new DBTask(new IDBTaskListener() {
                @Override
                public void onPreExcute() {

                }

                @Override
                public void onPostExcute() {
                    setUpAdapterGenreMusic(mGenreMusic);
                }

                @Override
                public void onDoInBackground() {
                    String fileName = "";
                    if (Locale.getDefault().getCountry().toLowerCase(Locale.US).equalsIgnoreCase("BR")) {
                        fileName = String.format(ICloudMusicPlayerConstants.FILE_GENRE, new Object[]{Locale.getDefault().getCountry().toLowerCase(Locale.US)});
                    } else {
                        fileName = String.format(ICloudMusicPlayerConstants.FILE_GENRE, new Object[]{"en"});
                    }
                    ArrayList<GenreObject> mListGenres = JsonParsingUtils.parsingGenreObject(IOUtils.readStringFromAssets(mContext, fileName));
                    if (mListGenres != null) {
                        TotalDataManager.getInstance().setListGenreObjects(mListGenres);
                        setUpAdapterGenreMusic(mListGenres);
                    }
                }
            });
            this.mDBTask.execute(new Void[0]);
        }
    }

    private void setUpAdapterGenreMusic(final ArrayList<GenreObject> mGenreMusic) {
        verRecyclerViewAdapter=new DiscoverVerRecyclerViewAdapter(getContext(),mGenreMusic);
        verRecyclerViewAdapter.setOnGenresMusicListener(new DiscoverVerRecyclerViewAdapter.OnclickListener() {
            @Override
            public void onClick(int position) {
                mContext.processSearchData(mGenreMusic.get(position).getKeyword(),true);
            }
        });
        RecyclerView.LayoutManager layoutManager=new GridLayoutManager(getContext(),2,GridLayoutManager.HORIZONTAL,false);
        recyclerViewBottom.setLayoutManager(layoutManager);
        recyclerViewBottom.hasFixedSize();
        recyclerViewBottom.setAdapter(verRecyclerViewAdapter);
    }

    private void getDataTopMusic(){
        mTopMusic = TotalDataManager.getInstance().getListTopMusicObjects();
        if (mTopMusic!=null){
            setUpAdapterTopMusic(mTopMusic);
        }
        if (ApplicationUtils.isOnline(mContext)) {
            this.mDBTask = new DBTask(new IDBTaskListener() {
                private ArrayList<TopMusicObject> mListNewTopObjects;

                public void onDoInBackground() {
                    this.mListNewTopObjects = mContext.mSoundClound.getListTopMusic(SettingManager.getLanguage(mContext), 80);
                    if (this.mListNewTopObjects != null && this.mListNewTopObjects.size() > 0) {
                        TotalDataManager.getInstance().setListTopMusicObjects(this.mListNewTopObjects);
                    }

                }

                @Override
                public void onPreExcute() {

                }

                public void onPostExcute() {
                    setUpAdapterTopMusic(mListNewTopObjects);
                }
            });
            this.mDBTask.execute(new Void[0]);
            return;
        }


    }
    private void setUpAdapterTopMusic(ArrayList<TopMusicObject> mTopMusic){
        hizRecyclerViewAdapter=new DiscoverHizRecyclerViewAdapter(getContext(),mTopMusic);
        RecyclerView.LayoutManager layoutManager=new GridLayoutManager(getContext(),2,GridLayoutManager.HORIZONTAL,false);
        recyclerViewTop.setLayoutManager(layoutManager);
        recyclerViewTop.hasFixedSize();
        recyclerViewTop.setAdapter(hizRecyclerViewAdapter);
        hizRecyclerViewAdapter.setOnTopMusicListener(new DiscoverHizRecyclerViewAdapter.OnTopMusicListener() {
        @Override
            public void onSearchDetail(TopMusicObject mTopMusicObject) {
            String name=mTopMusicObject.getName()+ " " +mTopMusicObject.getArtist();
            if(!StringUtils.isEmptyString(name))
                mContext.processSearchData(name,false);
            }
        });
    }
}
