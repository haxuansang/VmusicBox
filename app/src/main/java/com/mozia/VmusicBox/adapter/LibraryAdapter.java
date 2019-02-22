package com.mozia.VmusicBox.adapter;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mozia.VmusicBox.R;
import com.mozia.VmusicBox.constants.ICloudMusicPlayerConstants;
import com.mozia.VmusicBox.soundclound.object.TrackObject;
import com.ypyproductions.abtractclass.DBBaseAdapter;

import java.util.ArrayList;

public class LibraryAdapter extends DBBaseAdapter implements ICloudMusicPlayerConstants {
    public static final String TAG = LibraryAdapter.class.getSimpleName();
    private Typeface mTypefaceBold;
    private Typeface mTypefaceLight;
    private OnDownloadedListener onTrackListener;

    class C05264 implements OnClickListener {
        C05264() {
        }

        public void onClick(DialogInterface dialog, int which) {
        }
    }

    public interface OnDownloadedListener {
        void onAddToPlaylist(TrackObject trackObject);

        void onDeleteItem(TrackObject trackObject);

        void onPlayItem(TrackObject trackObject);

        void onSetAsNotification(TrackObject trackObject);

        void onSetAsRingtone(TrackObject trackObject);
    }

    private static class ViewHolder {
        public ImageView mImgMenu;
        public RelativeLayout mLayoutRoot;
        public TextView mTvDuration;
        public TextView mTvNumber;
        public TextView mTvSongName;

        private ViewHolder() {
        }
    }

    public LibraryAdapter(Activity mContext, ArrayList<TrackObject> listTrackObjects, Typeface mTypefaceBold, Typeface mTypefaceLight) {
        super(mContext, listTrackObjects);
        this.mTypefaceBold = mTypefaceBold;
        this.mTypefaceLight = mTypefaceLight;
    }

    public View getAnimatedView(int position, View convertView, ViewGroup parent) {
        return null;
    }

    public View getNormalView(int position, View convertView, ViewGroup parent) {
        ViewHolder mHolder;
        if (convertView == null) {
            mHolder = new ViewHolder();
            convertView = ((LayoutInflater) this.mContext.getSystemService("layout_inflater")).inflate(R.layout.item_local_track, null);
            convertView.setTag(mHolder);
            mHolder.mImgMenu = (ImageView) convertView.findViewById(R.id.img_menu);
            mHolder.mTvSongName = (TextView) convertView.findViewById(R.id.tv_song);
            mHolder.mTvDuration = (TextView) convertView.findViewById(R.id.tv_duration);
            mHolder.mTvNumber = (TextView) convertView.findViewById(R.id.tv_number);
            mHolder.mLayoutRoot = (RelativeLayout) convertView.findViewById(R.id.layout_root);
            mHolder.mTvSongName.setTypeface(this.mTypefaceBold);
            mHolder.mTvDuration.setTypeface(this.mTypefaceLight);
            mHolder.mTvNumber.setTypeface(this.mTypefaceLight);
        } else {
            mHolder = (ViewHolder) convertView.getTag();
        }
        final TrackObject mTrackObject = (TrackObject) this.mListObjects.get(position);
        mHolder.mTvSongName.setText(mTrackObject.getTitle());
        mHolder.mTvNumber.setText(new StringBuilder(String.valueOf(String.valueOf(position + 1))).append(".").toString());
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
        mHolder.mLayoutRoot.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (LibraryAdapter.this.onTrackListener != null) {
                    LibraryAdapter.this.onTrackListener.onPlayItem(mTrackObject);
                }
            }
        });
        mHolder.mImgMenu.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                LibraryAdapter.this.showDialogOptions(mTrackObject);
            }
        });
        return convertView;
    }

    public void showDialogOptions(final TrackObject mTrackObject) {
        new Builder(this.mContext).setTitle(R.string.title_options).setItems(R.array.list_options, new OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    if (LibraryAdapter.this.onTrackListener != null) {
                        LibraryAdapter.this.onTrackListener.onAddToPlaylist(mTrackObject);
                    }
                } else if (which == 1) {
                    if (LibraryAdapter.this.onTrackListener != null) {
                        LibraryAdapter.this.onTrackListener.onDeleteItem(mTrackObject);
                    }
                } else if (which == 2) {
                    if (LibraryAdapter.this.onTrackListener != null) {
                        LibraryAdapter.this.onTrackListener.onSetAsRingtone(mTrackObject);
                    }
                } else if (which == 3 && LibraryAdapter.this.onTrackListener != null) {
                    LibraryAdapter.this.onTrackListener.onSetAsNotification(mTrackObject);
                }
            }
        }).setPositiveButton(R.string.title_cancel, new C05264()).create().show();
    }

    public void setOnDownloadedListener(OnDownloadedListener onDownloadedListener) {
        this.onTrackListener = onDownloadedListener;
    }
}
