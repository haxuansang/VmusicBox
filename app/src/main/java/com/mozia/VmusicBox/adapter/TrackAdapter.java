package com.mozia.VmusicBox.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.mozia.VmusicBox.R;
import com.mozia.VmusicBox.constants.ICloudMusicPlayerConstants;
import com.mozia.VmusicBox.setting.SettingManager;
import com.mozia.VmusicBox.soundclound.object.TrackObject;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.ypyproductions.abtractclass.DBBaseAdapter;
import com.ypyproductions.utils.StringUtils;

import java.util.ArrayList;
import java.util.Date;

public class TrackAdapter extends DBBaseAdapter implements ICloudMusicPlayerConstants {
    public static final String TAG = TrackAdapter.class.getSimpleName();
    private DisplayImageOptions mAvatarOptions;
    private Date mDate = new Date();
    private DisplayImageOptions mImgOptions;
    private Typeface mTypefaceBold;
    private Typeface mTypefaceLight;
    private ITrackAdapterListener trackAdapter;

    public interface ITrackAdapterListener {
        void onDownload(TrackObject trackObject);

        void onListenDemo(TrackObject trackObject);
    }

    private static class ViewHolder {
        public Button mBtnDownload;
        public ImageView mImgLogo;
        public ImageView mImgSongs;
        public ImageView mImgUsername;
        public RelativeLayout mRootLayout;
        public TextView mTvDuration;
        public TextView mTvPlayCount;
        public TextView mTvSongName;
        public TextView mTvTag;
        public TextView mTvTime;
        public TextView mTvUsername;

        private ViewHolder() {
        }
    }

    public TrackAdapter(Activity mContext, ArrayList<TrackObject> listDrawerObjects, Typeface mTypefaceBold, Typeface mTypefaceLight, DisplayImageOptions mImgOptions, DisplayImageOptions mAvatarOptions) {
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
            convertView = ((LayoutInflater) this.mContext.getSystemService("layout_inflater")).inflate(R.layout.item_track, null);
            convertView.setTag(mHolder);
            mHolder.mImgSongs = (ImageView) convertView.findViewById(R.id.img_track);
            mHolder.mImgUsername = (ImageView) convertView.findViewById(R.id.img_avatar);
            mHolder.mTvSongName = (TextView) convertView.findViewById(R.id.tv_song);
            mHolder.mTvTag = (TextView) convertView.findViewById(R.id.tv_keyword);
            mHolder.mTvUsername = (TextView) convertView.findViewById(R.id.tv_username);
            mHolder.mTvDuration = (TextView) convertView.findViewById(R.id.tv_duration);
            mHolder.mTvPlayCount = (TextView) convertView.findViewById(R.id.tv_playcount);
            mHolder.mTvTime = (TextView) convertView.findViewById(R.id.tv_time);
            mHolder.mBtnDownload = (Button) convertView.findViewById(R.id.btn_download);
            mHolder.mRootLayout = (RelativeLayout) convertView.findViewById(R.id.layout_root);
            mHolder.mImgLogo = (ImageView) convertView.findViewById(R.id.img_logo);
            mHolder.mTvSongName.setTypeface(this.mTypefaceBold);
            mHolder.mTvUsername.setTypeface(this.mTypefaceBold);
            mHolder.mTvDuration.setTypeface(this.mTypefaceLight);
            mHolder.mTvPlayCount.setTypeface(this.mTypefaceLight);
            mHolder.mTvTime.setTypeface(this.mTypefaceLight);
            mHolder.mTvTag.setTypeface(this.mTypefaceLight);
        } else {
            mHolder = (ViewHolder) convertView.getTag();
        }
        final TrackObject mTrackObject = (TrackObject) this.mListObjects.get(position);
        mHolder.mTvTag.setText("#" + StringUtils.urlDecodeString(SettingManager.getLastKeyword(this.mContext)));
        mHolder.mTvSongName.setText(mTrackObject.getTitle());
        mHolder.mTvUsername.setText(mTrackObject.getUsername());
        mHolder.mTvPlayCount.setText(formatVisualNumber(mTrackObject.getPlaybackCount(), ","));
        Date mTrackDate = mTrackObject.getCreatedDate();
        if (mTrackDate != null) {
            mHolder.mTvTime.setText(getStringTimeAgo(this.mContext, (this.mDate.getTime() - mTrackDate.getTime()) / 1000));
        }
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
        if (StringUtils.isEmptyString(mTrackObject.getAvatarUrl()) || !mTrackObject.getAvatarUrl().startsWith("http")) {
            mHolder.mImgUsername.setImageResource(R.drawable.ic_account_circle_grey);
        } else {
            ImageLoader.getInstance().displayImage(mTrackObject.getAvatarUrl(), mHolder.mImgUsername, this.mAvatarOptions);
        }
        mHolder.mRootLayout.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (TrackAdapter.this.trackAdapter != null) {
                    TrackAdapter.this.trackAdapter.onListenDemo(mTrackObject);
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

    public static String getStringTimeAgo(Context mContext, long second) {
        double minutes = (double) (((float) second) / BitmapDescriptorFactory.HUE_YELLOW);
        if (second < 5) {
            return mContext.getString(R.string.title_just_now);
        }
        if (second < 60) {
            return String.valueOf(second) + " " + mContext.getString(R.string.title_second_ago);
        }
        if (second < 120) {
            return mContext.getString(R.string.title_a_minute_ago);
        }
        if (minutes < 60.0d) {
            return new StringBuilder(String.valueOf(String.valueOf((int) minutes))).append(" ").append(mContext.getString(R.string.title_minute_ago)).toString();
        }
        if (minutes < 120.0d) {
            return mContext.getString(R.string.title_a_hour_ago);
        }
        if (minutes < 1440.0d) {
            return new StringBuilder(String.valueOf(String.valueOf((int) Math.floor(minutes / 60.0d)))).append(" ").append(mContext.getString(R.string.title_hour_ago)).toString();
        }
        if (minutes < 2880.0d) {
            return mContext.getString(R.string.title_yester_day);
        }
        if (minutes < 10080.0d) {
            return new StringBuilder(String.valueOf(String.valueOf((int) Math.floor(minutes / 1440.0d)))).append(" ").append(mContext.getString(R.string.title_day_ago)).toString();
        }
        if (minutes < 20160.0d) {
            return mContext.getString(R.string.title_last_week);
        }
        if (minutes < 44640.0d) {
            return new StringBuilder(String.valueOf(String.valueOf((int) Math.floor(minutes / 10080.0d)))).append(" ").append(mContext.getString(R.string.title_weeks_ago)).toString();
        }
        if (minutes < 87840.0d) {
            return mContext.getString(R.string.title_last_month);
        }
        if (minutes < 525960.0d) {
            return new StringBuilder(String.valueOf(String.valueOf((int) Math.floor(minutes / 43200.0d)))).append(" ").append(mContext.getString(R.string.title_month_ago)).toString();
        }
        if (minutes < 1052640.0d) {
            return mContext.getString(R.string.title_last_year);
        }
        if (minutes > 1052640.0d) {
            return new StringBuilder(String.valueOf(String.valueOf((int) Math.floor(minutes / 525600.0d)))).append(" ").append(mContext.getString(R.string.title_year_ago)).toString();
        }
        return mContext.getString(R.string.title_unknown);
    }

    public void setTrackAdapterListener(ITrackAdapterListener trackAdapter) {
        this.trackAdapter = trackAdapter;
    }
}
