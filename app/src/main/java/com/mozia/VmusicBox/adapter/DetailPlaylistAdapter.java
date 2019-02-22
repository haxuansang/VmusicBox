package com.mozia.VmusicBox.adapter;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Typeface;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mozia.VmusicBox.R;
import com.mozia.VmusicBox.constants.ICloudMusicPlayerConstants;
import com.mozia.VmusicBox.soundclound.object.TrackObject;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.ypyproductions.abtractclass.DBBaseAdapter;
import com.ypyproductions.utils.StringUtils;

import java.util.ArrayList;

public class DetailPlaylistAdapter extends DBBaseAdapter implements ICloudMusicPlayerConstants {
    public static final String TAG = DetailPlaylistAdapter.class.getSimpleName();
    private DisplayImageOptions mImgOptions;
    private Typeface mTypefaceBold;
    private Typeface mTypefaceLight;
    private IDetailPlaylistAdapterListener trackAdapter;

    class C05224 implements OnClickListener {
        C05224() {
        }

        public void onClick(DialogInterface dialog, int which) {
        }
    }

    public interface IDetailPlaylistAdapterListener {
        void onPlayingTrack(TrackObject trackObject);

        void onRemoveToPlaylist(TrackObject trackObject);
    }

    private static class ViewHolder {
        public ImageView mImgMenu;
        public de.hdodenhof.circleimageview.CircleImageView mImgSongs;
        public RelativeLayout mRootLayout;
        public TextView mTvSinger;
        public TextView mTvSongName;

        private ViewHolder() {
        }
    }

    public DetailPlaylistAdapter(Activity mContext, ArrayList<TrackObject> listDrawerObjects, Typeface mTypefaceBold, Typeface mTypefaceLight, DisplayImageOptions mImgOptions) {
        super(mContext, listDrawerObjects);
        this.mTypefaceBold = mTypefaceBold;
        this.mTypefaceLight = mTypefaceLight;
        this.mImgOptions = mImgOptions;
    }

    public View getAnimatedView(int position, View convertView, ViewGroup parent) {
        return null;
    }

    public View getNormalView(int position, View convertView, ViewGroup parent) {
        ViewHolder mHolder;
        if (convertView == null) {
            mHolder = new ViewHolder();
            convertView = ((LayoutInflater) this.mContext.getSystemService("layout_inflater")).inflate(R.layout.item_detail_playlist, null);
            convertView.setTag(mHolder);
            mHolder.mImgSongs = (de.hdodenhof.circleimageview.CircleImageView) convertView.findViewById(R.id.img_songs);
            mHolder.mImgMenu = (ImageView) convertView.findViewById(R.id.img_menu);
            mHolder.mTvSongName = (TextView) convertView.findViewById(R.id.tv_song);
            mHolder.mTvSinger = (TextView) convertView.findViewById(R.id.tv_singer);
            mHolder.mRootLayout = (RelativeLayout) convertView.findViewById(R.id.layout_root);
            mHolder.mTvSongName.setTypeface(this.mTypefaceBold);
            mHolder.mTvSinger.setTypeface(this.mTypefaceLight);
        } else {
            mHolder = (ViewHolder) convertView.getTag();
        }
        final TrackObject mTrackObject = (TrackObject) this.mListObjects.get(position);
        mHolder.mTvSongName.setText(mTrackObject.getTitle());
        mHolder.mTvSinger.setText(mTrackObject.getUsername());
        String urlTrack = mTrackObject.getArtworkUrl();
        if (StringUtils.isEmptyString(urlTrack) || urlTrack.equals("null")) {
            urlTrack = mTrackObject.getAvatarUrl();
        }
        if (!StringUtils.isEmptyString(mTrackObject.getPath())) {
            Uri mUri = mTrackObject.getURI();
            if (mUri != null) {
                ImageLoader.getInstance().displayImage(mUri.toString(), mHolder.mImgSongs, this.mImgOptions);
            } else {
                mHolder.mImgSongs.setImageResource(R.drawable.music_note);
            }
        } else if (StringUtils.isEmptyString(urlTrack) || !urlTrack.startsWith("http")) {
            mHolder.mImgSongs.setImageResource(R.drawable.music_note);
        } else {
            ImageLoader.getInstance().displayImage(urlTrack.replace("large", "crop"), mHolder.mImgSongs, this.mImgOptions);
        }
        mHolder.mRootLayout.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (DetailPlaylistAdapter.this.trackAdapter != null) {
                    DetailPlaylistAdapter.this.trackAdapter.onPlayingTrack(mTrackObject);
                }
            }
        });
        mHolder.mImgMenu.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                DetailPlaylistAdapter.this.showDiaglogOptions(mTrackObject);
            }
        });
        return convertView;
    }

    public void showDiaglogOptions(final TrackObject mTrackObject) {
        new Builder(this.mContext).setTitle(R.string.title_options).setItems(R.array.list_track_playlist, new OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0 && DetailPlaylistAdapter.this.trackAdapter != null) {
                    DetailPlaylistAdapter.this.trackAdapter.onRemoveToPlaylist(mTrackObject);
                }
            }
        }).setPositiveButton(R.string.title_cancel, new C05224()).create().show();
    }

    public void setDetailPlaylistAdapterListener(IDetailPlaylistAdapterListener trackAdapter) {
        this.trackAdapter = trackAdapter;
    }
}
