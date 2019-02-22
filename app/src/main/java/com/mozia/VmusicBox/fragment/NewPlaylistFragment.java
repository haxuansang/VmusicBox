package com.mozia.VmusicBox.fragment;


import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mozia.VmusicBox.MainActivity;
import com.mozia.VmusicBox.R;
import com.mozia.VmusicBox.adapter.PlaylistRecyclerViewAdapter;
import com.mozia.VmusicBox.constants.ICloudMusicPlayerConstants;
import com.mozia.VmusicBox.dataMng.TotalDataManager;
import com.mozia.VmusicBox.object.PlaylistObject;
import com.ypyproductions.task.DBTask;
import com.ypyproductions.task.IDBCallback;
import com.ypyproductions.task.IDBTaskListener;
import com.ypyproductions.utils.ApplicationUtils;

import java.io.File;
import java.util.ArrayList;


public class NewPlaylistFragment extends Fragment {
    private RecyclerView recyclerView;
    private PlaylistRecyclerViewAdapter recyclerViewAdapter;
    private ArrayList<PlaylistObject>mPlayList;
    private FloatingActionButton fab;
    private DBTask mDBTask;
    private MainActivity mContext;
    public NewPlaylistFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_new_playlist, container, false);
        mContext=(MainActivity)getContext();
        recyclerView=(RecyclerView)view.findViewById(R.id.recycler);
        fab=(FloatingActionButton)view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mContext.createDialogPlaylist(false, null, new IDBCallback() {
                    @Override
                    public void onAction() {
                        if(recyclerViewAdapter!=null)
                            recyclerViewAdapter.notifyDataSetChanged();
                    }
                });
            }
        });
        getListPlaylist();
        return view;
    }

    private void setRecyclerViewData(ArrayList<PlaylistObject> list) {
        if(list!=null)
        {
            RecyclerView.LayoutManager layoutManager=new GridLayoutManager(getContext(),3);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setHasFixedSize(true);
            recyclerViewAdapter=new PlaylistRecyclerViewAdapter(getContext(),list);
            recyclerView.setAdapter(recyclerViewAdapter);
            recyclerViewAdapter.setOnPlaylistListener(new PlaylistRecyclerViewAdapter.OnItemClick() {
                @Override
                public void onClick(PlaylistObject object) {
                    showDetailPlaylist(object);
                }
            });

        }
    }

    private void showDetailPlaylist(PlaylistObject object) {

    }

    private void getListPlaylist(){
        if(ApplicationUtils.hasSDcard()){
            final File mFile = new File(new StringBuilder(String.valueOf(Environment.getExternalStorageDirectory().toString())).append("/").append(ICloudMusicPlayerConstants.NAME_FOLDER_CACHE).toString());
            if(!mFile.exists())
                mFile.mkdir();
            mDBTask=new DBTask(new IDBTaskListener() {
                @Override
                public void onPreExcute() {
                }

                @Override
                public void onDoInBackground() {
                    TotalDataManager.getInstance().readPlaylistCached(getContext(),mFile);
                    TotalDataManager.getInstance().readSavedTrack(getContext(),mFile);
                    mPlayList=TotalDataManager.getInstance().getListPlaylistObjects();
                }
                @Override
                public void onPostExcute() {
                    setRecyclerViewData(mPlayList);
                }
            });
            mDBTask.execute(new Void[0]);
        }
    }
}
