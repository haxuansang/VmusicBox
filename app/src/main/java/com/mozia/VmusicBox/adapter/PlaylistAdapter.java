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
import com.mozia.VmusicBox.object.PlaylistObject;
import com.ypyproductions.abtractclass.DBBaseAdapter;

import java.util.ArrayList;

public class PlaylistAdapter extends DBBaseAdapter implements ICloudMusicPlayerConstants {
    public static final String TAG = PlaylistAdapter.class.getSimpleName();
    private Typeface mTypefaceBold;
    private Typeface mTypefaceLight;
    private OnPlaylistListener onPlaylistListener;

    class C05324 implements OnClickListener {
        C05324() {
        }

        public void onClick(DialogInterface dialog, int which) {
        }
    }

    public interface OnPlaylistListener {
        void onDeletePlaylist(PlaylistObject playlistObject);

        void onGoToDetail(PlaylistObject playlistObject);

        void onPlayAllMusic(PlaylistObject playlistObject);

        void onRenamePlaylist(PlaylistObject playlistObject);
    }

    private static class ViewHolder {
        public ImageView mBtnMenu;
        public RelativeLayout mLayoutRoot;
        public TextView mTvNumberMusic;
        public TextView mTvOrder;
        public TextView mTvPlaylistName;

        private ViewHolder() {
        }
    }

    public PlaylistAdapter(Activity mContext, ArrayList<PlaylistObject> listPlaylistObjects, Typeface mTypefaceBold, Typeface mTypefaceLight) {
        super(mContext, listPlaylistObjects);
        this.mTypefaceBold = mTypefaceBold;
        this.mTypefaceLight = mTypefaceLight;
    }

    public View getAnimatedView(int position, View convertView, ViewGroup parent) {
        return null;
    }

    public View getNormalView(int position, View convertView, ViewGroup parent) {
        ViewHolder mHolder;
        int size;
        if (convertView == null) {
            mHolder = new ViewHolder();
            convertView = ((LayoutInflater) this.mContext.getSystemService("layout_inflater")).inflate(R.layout.item_playlist, null);
            convertView.setTag(mHolder);
            mHolder.mTvPlaylistName = (TextView) convertView.findViewById(R.id.tv_playlist_name);
            mHolder.mTvPlaylistName.setTypeface(this.mTypefaceBold);
            mHolder.mTvNumberMusic = (TextView) convertView.findViewById(R.id.tv_number_music);
            mHolder.mTvNumberMusic.setTypeface(this.mTypefaceLight);
            mHolder.mTvOrder = (TextView) convertView.findViewById(R.id.tv_number);
            mHolder.mTvOrder.setTypeface(this.mTypefaceLight);
            mHolder.mBtnMenu = (ImageView) convertView.findViewById(R.id.img_menu);
            mHolder.mLayoutRoot = (RelativeLayout) convertView.findViewById(R.id.layout_root);
        } else {
            mHolder = (ViewHolder) convertView.getTag();
        }
        final PlaylistObject mPlaylistObject = (PlaylistObject) this.mListObjects.get(position);
        mHolder.mTvPlaylistName.setText(mPlaylistObject.getName());
        mHolder.mTvOrder.setText(new StringBuilder(String.valueOf(String.valueOf(position + 1))).append(".").toString());
        if (mPlaylistObject.getListTrackObjects() != null) {
            size = mPlaylistObject.getListTrackObjects().size();
        } else {
            size = 0;
        }
        String data = "";
        if (size <= 1) {
            data = String.format(this.mContext.getString(R.string.format_number_music), new Object[]{Integer.valueOf(size)});
        } else {
            data = String.format(this.mContext.getString(R.string.format_number_musics), new Object[]{Integer.valueOf(size)});
        }
        mHolder.mTvNumberMusic.setText(data);
        mHolder.mLayoutRoot.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                if (PlaylistAdapter.this.onPlaylistListener != null) {
                    PlaylistAdapter.this.onPlaylistListener.onGoToDetail(mPlaylistObject);
                }
            }
        });
        mHolder.mBtnMenu.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                PlaylistAdapter.this.showDiaglogOptions(mPlaylistObject);
            }
        });
        return convertView;
    }

    public void showDiaglogOptions(final PlaylistObject mPlaylistObject) {
        new Builder(this.mContext).setTitle(R.string.title_options).setItems(R.array.list_options_playlist, new OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    if (PlaylistAdapter.this.onPlaylistListener != null) {
                        PlaylistAdapter.this.onPlaylistListener.onPlayAllMusic(mPlaylistObject);
                    }
                } else if (which == 1) {
                    if (PlaylistAdapter.this.onPlaylistListener != null) {
                        PlaylistAdapter.this.onPlaylistListener.onRenamePlaylist(mPlaylistObject);
                    }
                } else if (which == 2 && PlaylistAdapter.this.onPlaylistListener != null) {
                    PlaylistAdapter.this.onPlaylistListener.onDeletePlaylist(mPlaylistObject);
                }
            }
        }).setPositiveButton(R.string.title_cancel, new C05324()).create().show();
    }

    public void seOnPlaylistListener(OnPlaylistListener onPlaylistListener) {
        this.onPlaylistListener = onPlaylistListener;
    }
}
