package com.mozia.VmusicBox.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.mozia.VmusicBox.APISuggestion.Track;
import com.mozia.VmusicBox.R;

import com.mozia.VmusicBox.listener.AddToPlaying;
import com.mozia.VmusicBox.soundclound.object.TrackObject;
import com.squareup.picasso.Picasso;

import java.util.Date;
import java.util.List;


import de.hdodenhof.circleimageview.CircleImageView;

public class SuggestAdapter extends RecyclerView.Adapter<SuggestAdapter.SuggestViewHolder> {

    Context mContext;
    List<Track> mlistTracks;
    AddToPlaying addToPlaying;
    public SuggestAdapter(Context context, List<Track> listTracks) {
        this.mContext= context;
        this.mlistTracks=listTracks;
    }

    public void registerListener(AddToPlaying addToPlaying)
    {
            this.addToPlaying = addToPlaying;
    }

    @Override
    public SuggestViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_suggestion_playlist,parent,false);

        return new SuggestViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final SuggestViewHolder holder, final int position) {
        holder.tvNameSong.setText(this.mlistTracks.get(position).getTitle());
        Picasso.get().load(this.mlistTracks.get(position).getArtworkUrl()).into(holder.avatarOfSong);
        holder.itemView.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                TrackObject mTrackObject;
                mTrackObject=convertDataToTrackObject(mlistTracks.get(position));
                if(SuggestAdapter.this.addToPlaying!=null)
                {
                    SuggestAdapter.this.addToPlaying.addToPlaying(mTrackObject,position);
                }

            }
        });
    }

    private TrackObject convertDataToTrackObject(Track track) {
        return new TrackObject(track.getId(),track.getDuration(),track.getTitle(),track.getDescription(),
                track.getUser().getUsername(),track.getUser().getAvatarUrl(),track.getPermalinkUrl(),
                track.getArtworkUrl(),track.getPlaybackCount());
    }


    @Override
    public int getItemCount() {
        return this.mlistTracks.size() ;
    }

    class SuggestViewHolder extends RecyclerView.ViewHolder{
        CircleImageView avatarOfSong;
        TextView tvNameSong;
        public SuggestViewHolder(View itemView) {
            super(itemView);
            avatarOfSong=(CircleImageView)itemView.findViewById(R.id.avatar_song_suggestion);
            tvNameSong= (TextView)itemView.findViewById(R.id.name_song_suggestion);

        }


    }
}
