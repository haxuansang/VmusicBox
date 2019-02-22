package com.mozia.VmusicBox.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mozia.VmusicBox.R;
import com.mozia.VmusicBox.object.TopMusicObject;
import com.squareup.picasso.Picasso;
import com.ypyproductions.utils.StringUtils;

import java.util.List;

public class DiscoverHizRecyclerViewAdapter extends RecyclerView.Adapter<DiscoverHizRecyclerViewAdapter.DiscoverHizRecyclerViewHolder>{
    private List<TopMusicObject> mTopMusic;
    private Context context;
    private OnTopMusicListener mOnTopMusicListener;
    public DiscoverHizRecyclerViewAdapter(Context context,List<TopMusicObject> mTopMusic){
        this.context=context;
        this.mTopMusic=mTopMusic;
    }

    @Override
    public DiscoverHizRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View view=inflater.inflate(R.layout.item_recycler_discover,parent,false);
        return new DiscoverHizRecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DiscoverHizRecyclerViewHolder holder, int position) {
        if (StringUtils.isEmptyString(mTopMusic.get(position).getArtwork()))
            holder.image.setImageResource(R.drawable.vu);
        else
            Picasso.get().load(mTopMusic.get(position).getArtwork()).into(holder.image);
        holder.song.setText(mTopMusic.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return mTopMusic.size();
    }

    public class DiscoverHizRecyclerViewHolder extends RecyclerView.ViewHolder{
        private TextView song;
        private ImageView image;
        public DiscoverHizRecyclerViewHolder(View itemView)
        {
            super(itemView);
            song=(TextView) itemView.findViewById(R.id.tv_song);
            image=(ImageView)itemView.findViewById(R.id.image_view_discover_hiz);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(mOnTopMusicListener!=null){
                        mOnTopMusicListener.onSearchDetail(mTopMusic.get(getLayoutPosition()));
                    }
                }
            });
        }
    }
    public void setOnTopMusicListener(OnTopMusicListener mOnTopMusicListener){
        this.mOnTopMusicListener=mOnTopMusicListener;
    }
    public interface OnTopMusicListener{
        void onSearchDetail(TopMusicObject mTopMusicObject);
    }
}
