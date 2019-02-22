package com.mozia.VmusicBox.adapter;

import android.app.Activity;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mozia.VmusicBox.R;
import com.mozia.VmusicBox.constants.ICloudMusicPlayerConstants;
import com.mozia.VmusicBox.object.TopMusicObject;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.ypyproductions.abtractclass.DBBaseAdapter;
import com.ypyproductions.utils.StringUtils;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class TopMusicAdapter extends DBBaseAdapter implements ICloudMusicPlayerConstants {
    public static final String TAG = TopMusicAdapter.class.getSimpleName();
    private DisplayImageOptions mOptions;
    private Typeface mTypefaceBold;
    private Typeface mTypefaceLight;
    private OnTopMusicListener onTrackListener;

    public interface OnTopMusicListener {
        void onSearchDetail(TopMusicObject topMusicObject);
    }

    private static class ViewHolder {
        public CircleImageView mImgTrack;
        public RelativeLayout mLayoutRoot;
        public TextView mTvArtist;
        public TextView mTvSongName;

        private ViewHolder() {
        }
    }

    public TopMusicAdapter(Activity mContext, ArrayList<TopMusicObject> listTrackObjects, Typeface mTypefaceBold, Typeface mTypefaceLight, DisplayImageOptions mOptions) {
        super(mContext, listTrackObjects);
        this.mTypefaceBold = mTypefaceBold;
        this.mTypefaceLight = mTypefaceLight;
        this.mOptions = mOptions;
    }

    public View getAnimatedView(int position, View convertView, ViewGroup parent) {
        return null;
    }

    public View getNormalView(int position, View convertView, ViewGroup parent) {
        ViewHolder mHolder;
        if (convertView == null) {
            mHolder = new ViewHolder();
            convertView = ((LayoutInflater) this.mContext.getSystemService("layout_inflater")).inflate(R.layout.item_hot_music, null);
            convertView.setTag(mHolder);
            mHolder.mTvSongName = (TextView) convertView.findViewById(R.id.tv_song);
            mHolder.mTvArtist = (TextView) convertView.findViewById(R.id.tv_artist);
            mHolder.mImgTrack = (CircleImageView) convertView.findViewById(R.id.img_songs);
            mHolder.mLayoutRoot = (RelativeLayout) convertView.findViewById(R.id.layout_root);
            mHolder.mTvSongName.setTypeface(this.mTypefaceBold);
            mHolder.mTvArtist.setTypeface(this.mTypefaceLight);
        } else {
            mHolder = (ViewHolder) convertView.getTag();
        }
        final TopMusicObject mTrackObject = (TopMusicObject) this.mListObjects.get(position);
        mHolder.mTvSongName.setText(mTrackObject.getName());
        mHolder.mTvArtist.setText(mTrackObject.getArtist());
        if (StringUtils.isEmptyString(mTrackObject.getArtwork())) {
            mHolder.mImgTrack.setImageResource(R.drawable.ic_music_default);
        } else {
            ImageLoader.getInstance().displayImage(mTrackObject.getArtwork(), mHolder.mImgTrack, this.mOptions);
        }
        mHolder.mLayoutRoot.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (TopMusicAdapter.this.onTrackListener != null) {
                    TopMusicAdapter.this.onTrackListener.onSearchDetail(mTrackObject);
                }
            }
        });
        return convertView;
    }

    public void setOnTopMusicListener(OnTopMusicListener onTrackListener) {
        this.onTrackListener = onTrackListener;
    }
}
