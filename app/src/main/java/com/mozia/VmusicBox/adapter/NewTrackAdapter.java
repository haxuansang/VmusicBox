package com.mozia.VmusicBox.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.mozia.VmusicBox.R;
import com.mozia.VmusicBox.constants.ICloudMusicPlayerConstants;
import com.mozia.VmusicBox.soundclound.object.TrackObject;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.ypyproductions.abtractclass.DBBaseAdapter;
import com.ypyproductions.utils.StringUtils;

import java.util.ArrayList;
import java.util.Date;

public class NewTrackAdapter extends DBBaseAdapter implements ICloudMusicPlayerConstants {
    public static final String TAG = NewTrackAdapter.class.getSimpleName();
    private DisplayImageOptions mAvatarOptions;
    private Date mDate = new Date();
    private DisplayImageOptions mImgOptions;
    private Typeface mTypefaceBold;
    private Typeface mTypefaceLight;
    private INewTrackAdapterListener trackAdapter;

    public interface INewTrackAdapterListener {
        void onAddToPlaylist(TrackObject trackObject);

        void onDownload(TrackObject trackObject);

        void onListenDemo(TrackObject trackObject);
    }

    private static class ViewHolder {
        public ImageView mImgAdd;
        public ImageView mImgSongs;
        public RelativeLayout mRootLayout;
        public TextView mTvDuration;
        public TextView mTvPlayCount;
        public TextView mTvSongName;

        private ViewHolder() {
        }
    }

    public NewTrackAdapter(Activity mContext, ArrayList<TrackObject> listDrawerObjects, Typeface mTypefaceBold, Typeface mTypefaceLight, DisplayImageOptions mImgOptions, DisplayImageOptions mAvatarOptions) {
        super(mContext, listDrawerObjects);
        this.mTypefaceBold = mTypefaceBold;
        this.mTypefaceLight = mTypefaceLight;
        this.mImgOptions = mImgOptions;
        this.mAvatarOptions = mAvatarOptions;
    }

    public View getAnimatedView(int position, View convertView, ViewGroup parent) {
        return null;
    }

    public View getNormalView(int position, View convertView, ViewGroup parent) {
        ViewHolder mHolder;
        if (convertView == null) {
            mHolder = new ViewHolder();
            convertView = ((LayoutInflater) this.mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.item_new_track, null);
            convertView.setTag(mHolder);
            mHolder.mImgSongs = (ImageView) convertView.findViewById(R.id.img_songs);
            mHolder.mImgAdd = (ImageView) convertView.findViewById(R.id.img_add_playlist);
            mHolder.mTvSongName = (TextView) convertView.findViewById(R.id.tv_song);
            mHolder.mTvDuration = (TextView) convertView.findViewById(R.id.tv_duration);
            mHolder.mTvPlayCount = (TextView) convertView.findViewById(R.id.tv_playcount);
            mHolder.mRootLayout = (RelativeLayout) convertView.findViewById(R.id.layout_root);
            mHolder.mTvSongName.setTypeface(this.mTypefaceBold);
            mHolder.mTvDuration.setTypeface(this.mTypefaceLight);
            mHolder.mTvPlayCount.setTypeface(this.mTypefaceLight);
        } else {
            mHolder = (ViewHolder) convertView.getTag();
        }
        final TrackObject mTrackObject = (TrackObject) this.mListObjects.get(position);
        mHolder.mTvSongName.setText(mTrackObject.getTitle());
        mHolder.mTvPlayCount.setText(String.valueOf(mTrackObject.getFavoriteCount()));
        long duration = mTrackObject.getDuration() / 1000;
        String minute = String.valueOf((int) (duration / 60));
        String seconds = String.valueOf((int) (duration % 60));
        if (minute.length() < 2) {
            minute = "0" + minute;
        }
        if (seconds.length() < 2) {
            seconds = "0" + seconds;
        }
        mHolder.mTvDuration.setText(new StringBuilder(String.valueOf(minute)).append(":").append(seconds).toString());
        String urlTrack = mTrackObject.getArtworkUrl();
        if (StringUtils.isEmptyString(urlTrack) || urlTrack.equals("null")) {
            urlTrack = mTrackObject.getAvatarUrl();
        }
        if (StringUtils.isEmptyString(urlTrack) || !urlTrack.startsWith("http")) {
            mHolder.mImgSongs.setImageResource(R.drawable.music_note);
        } else {
            ImageLoader.getInstance().displayImage(urlTrack.replace("large", "crop"), mHolder.mImgSongs, this.mImgOptions);
        }
        mHolder.mRootLayout.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (NewTrackAdapter.this.trackAdapter != null) {
                    NewTrackAdapter.this.trackAdapter.onListenDemo(mTrackObject);
                }
            }
        });
        mHolder.mImgAdd.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (NewTrackAdapter.this.trackAdapter != null) {
                    NewTrackAdapter.this.trackAdapter.onAddToPlaylist(mTrackObject);
                }
            }
        });
        return convertView;
    }

    public static String formatVisualNumber(long numberValue, String demiter) {
        String value = String.valueOf(numberValue);
        if (value.length() > 3) {
            try {
                int number = (int) Math.floor((double) (value.length() / 3));
                int lenght = value.length();
                String total = "";
                for (int i = 0; i < number; i++) {
                    for (int j = 0; j < 3; j++) {
                        total = value.charAt((lenght - 1) - ((i * 3) + j)) + total;
                    }
                    if (i != number - 1) {
                        total = new StringBuilder(String.valueOf(demiter)).append(total).toString();
                    } else if (lenght - (number * 3) > 0) {
                        total = new StringBuilder(String.valueOf(demiter)).append(total).toString();
                    }
                }
                return value.substring(0, lenght - (number * 3)) + total;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return String.valueOf(value);
    }

    public static String getStringTimeAgo(long second) {
        double minutes = (double) (((float) second) / BitmapDescriptorFactory.HUE_YELLOW);
        if (second < 5) {
            return "Just now";
        }
        if (second < 60) {
            return String.valueOf(second) + " seconds ago";
        }
        if (second < 120) {
            return "A minute ago";
        }
        if (minutes < 60.0d) {
            return new StringBuilder(String.valueOf(String.valueOf((int) minutes))).append(" minutes ago").toString();
        }
        if (minutes < 120.0d) {
            return "A hour ago";
        }
        if (minutes < 1440.0d) {
            return new StringBuilder(String.valueOf(String.valueOf((int) Math.floor(minutes / 60.0d)))).append(" hours ago").toString();
        }
        if (minutes < 2880.0d) {
            return "Yesterday";
        }
        if (minutes < 10080.0d) {
            return new StringBuilder(String.valueOf(String.valueOf((int) Math.floor(minutes / 1440.0d)))).append(" days ago").toString();
        }
        if (minutes < 20160.0d) {
            return "Last week";
        }
        if (minutes < 44640.0d) {
            return new StringBuilder(String.valueOf(String.valueOf((int) Math.floor(minutes / 10080.0d)))).append(" weeks ago").toString();
        }
        if (minutes < 87840.0d) {
            return "Last Month";
        }
        if (minutes < 525960.0d) {
            return new StringBuilder(String.valueOf(String.valueOf((int) Math.floor(minutes / 43200.0d)))).append(" months ago").toString();
        }
        if (minutes < 1052640.0d) {
            return "Last year";
        }
        if (minutes > 1052640.0d) {
            return new StringBuilder(String.valueOf(String.valueOf((int) Math.floor(minutes / 525600.0d)))).append(" years ago").toString();
        }
        return "Unknown";
    }

    public void setNewTrackAdapterListener(INewTrackAdapterListener trackAdapter) {
        this.trackAdapter = trackAdapter;
    }
}
