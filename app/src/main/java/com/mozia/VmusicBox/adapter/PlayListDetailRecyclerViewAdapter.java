package com.mozia.VmusicBox.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

public class PlayListDetailRecyclerViewAdapter extends RecyclerView.Adapter<PlayListDetailRecyclerViewAdapter.PlayListDetailRVHolder>{
    @Override
    public PlayListDetailRVHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(PlayListDetailRVHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class PlayListDetailRVHolder extends RecyclerView.ViewHolder{

        public PlayListDetailRVHolder(View itemView) {
            super(itemView);
        }
    }
}
