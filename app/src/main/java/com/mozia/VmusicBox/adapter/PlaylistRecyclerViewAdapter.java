package com.mozia.VmusicBox.adapter;

import android.content.Context;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mozia.VmusicBox.R;
import com.mozia.VmusicBox.object.PlaylistObject;

import java.util.ArrayList;

public class PlaylistRecyclerViewAdapter extends RecyclerView.Adapter<PlaylistRecyclerViewAdapter.PlaylistRecyclerViewHolder>{
    private ArrayList<PlaylistObject> mPlaylist;
    private Context mContext;
    private OnItemClick listener;
    public void setOnPlaylistListener(OnItemClick listener){
        this.listener=listener;
    }
    public PlaylistRecyclerViewAdapter(Context mContext,ArrayList<PlaylistObject> mPlaylist){
        this.mContext=mContext;
        this.mPlaylist=mPlaylist;
    }
    @Override
    public PlaylistRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View view=inflater.inflate(R.layout.item_recycler_playlist,parent,false);
        return new PlaylistRecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PlaylistRecyclerViewHolder holder, int position) {
        int currentPosition=mPlaylist.size()-position-1;
        holder.button.setText(mPlaylist.get(currentPosition).getName());
    }

    @Override
    public int getItemCount() {
        return mPlaylist.size();
    }

    public class PlaylistRecyclerViewHolder extends RecyclerView.ViewHolder{
        AppCompatButton button;
        public PlaylistRecyclerViewHolder(View itemView) {
            super(itemView);
            button=(AppCompatButton) itemView.findViewById(R.id.button);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(listener!=null){

                                                listener.onClick(mPlaylist.get(mPlaylist.size()-getLayoutPosition()-1));
                    }


                }
            });
        }
    }
    public interface OnItemClick{
        void onClick(PlaylistObject object);
    }
}
