package com.mozia.VmusicBox.adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mozia.VmusicBox.R;
import com.mozia.VmusicBox.object.SongOfAlbum;

import java.util.List;

public class DiscoverAlbumRecyclerViewAdapter extends RecyclerView.Adapter<DiscoverAlbumRecyclerViewAdapter.DiscoverAlbumRecyclerViewHolder>{
    private List<SongOfAlbum> listDiscover;
    private Context context;
    public DiscoverAlbumRecyclerViewAdapter(Context context,List<SongOfAlbum> listDiscover){
        this.context=context;
        this.listDiscover=listDiscover;
    }

    @Override
    public DiscoverAlbumRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View view=inflater.inflate(R.layout.item_recycler_songofalbum,parent,false);
        return new DiscoverAlbumRecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DiscoverAlbumRecyclerViewHolder holder, int position) {
        holder.image.setImageResource(R.drawable.vu);
        holder.song.setText(listDiscover.get(position).getTittle());
        holder.album.setText(listDiscover.get(position).getAlbum());
    }

    @Override
    public int getItemCount() {
        return listDiscover.size();
    }

    public class DiscoverAlbumRecyclerViewHolder extends RecyclerView.ViewHolder{
        private TextView song;
        private ImageView image;
        private TextView album;
        public DiscoverAlbumRecyclerViewHolder(View itemView)
        {
            super(itemView);
            song=(TextView) itemView.findViewById(R.id.tv_song);
            album=(TextView) itemView.findViewById(R.id.tv_album);
            image=(ImageView)itemView.findViewById(R.id.img_songs);
        }
    }
}

