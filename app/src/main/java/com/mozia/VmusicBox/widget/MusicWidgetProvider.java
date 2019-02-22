package com.mozia.VmusicBox.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.widget.RemoteViews;

import com.google.android.gms.drive.DriveFile;
import com.mozia.VmusicBox.MainActivity;
import com.mozia.VmusicBox.R;
import com.mozia.VmusicBox.constants.ICloudMusicPlayerConstants;
import com.mozia.VmusicBox.playerservice.IMusicConstant;
import com.mozia.VmusicBox.soundclound.SoundCloundDataMng;
import com.mozia.VmusicBox.soundclound.object.TrackObject;
import com.ypyproductions.utils.StringUtils;

public class MusicWidgetProvider extends AppWidgetProvider implements ICloudMusicPlayerConstants, IMusicConstant {
    public static final String TAG = MusicWidgetProvider.class.getSimpleName();

    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        updateWidget(context, appWidgetManager, appWidgetIds, false, false, (int) R.drawable.ic_music_default);
    }

    public static void updateWidget(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds, boolean isPlaying, boolean isLoading, Bitmap mBitmap) {
        TrackObject mCurrentTrack = SoundCloundDataMng.getInstance().getCurrentTrackObject();
        String packageName = context.getPackageName();
        //if (appWidgetIds.length > 0) {
            for (int appWidgetId : appWidgetIds) {
                RemoteViews remoteViews = new RemoteViews(packageName, R.layout.layout_widget);
                if (mCurrentTrack != null) {
                    remoteViews.setTextViewText(R.id.tv_song, mCurrentTrack.getTitle() + " - " + (StringUtils.isEmptyString(mCurrentTrack.getUsername()) ? context.getString(R.string.title_unknown) : mCurrentTrack.getUsername()));
                } else {
                    remoteViews.setTextViewText(R.id.tv_song, "");
                }
                remoteViews.setImageViewResource(R.id.btn_play, isPlaying ? R.drawable.ic_pause_white_36dp : R.drawable.ic_play_arrow_white_36dp);
                remoteViews.setOnClickPendingIntent(R.id.btn_play, PendingIntent.getBroadcast(context, 100, new Intent(new StringBuilder(String.valueOf(packageName)).append(IMusicConstant.ACTION_TOGGLE_PLAYBACK).toString()), 0));
                remoteViews.setOnClickPendingIntent(R.id.btn_next, PendingIntent.getBroadcast(context, 100, new Intent(new StringBuilder(String.valueOf(packageName)).append(IMusicConstant.ACTION_NEXT).toString()), 0));
                remoteViews.setOnClickPendingIntent(R.id.btn_prev, PendingIntent.getBroadcast(context, 100, new Intent(new StringBuilder(String.valueOf(packageName)).append(IMusicConstant.ACTION_PREVIOUS).toString()), 0));
                Intent playerIntent = new Intent(context, MainActivity.class);
                if (mCurrentTrack != null) {
                    playerIntent.putExtra(ICloudMusicPlayerConstants.KEY_SONG_ID, mCurrentTrack.getId());
                    playerIntent.addFlags(DriveFile.MODE_READ_ONLY);
                }
                remoteViews.setOnClickPendingIntent(R.id.img_play, PendingIntent.getActivity(context, 1000, playerIntent, DriveFile.MODE_READ_ONLY));
                remoteViews.setImageViewBitmap(R.id.img_play, mBitmap);
                remoteViews.setViewVisibility(R.id.btn_play, !isLoading ? 0 : 4);
                remoteViews.setViewVisibility(R.id.btn_next, !isLoading ? 0 : 4);
                remoteViews.setViewVisibility(R.id.btn_prev, !isLoading ? 0 : 4);
                remoteViews.setViewVisibility(R.id.progressBar1, isLoading ? 0 : 4);
                appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
            }
        //}
    }

    public static void updateWidget(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds, boolean isPlaying, boolean isLoading, int resId) {
        TrackObject mCurrentTrack = SoundCloundDataMng.getInstance().getCurrentTrackObject();
        String packageName = context.getPackageName();
        //if (appWidgetIds.length > 0) {
            for (int appWidgetId : appWidgetIds) {
                RemoteViews remoteViews = new RemoteViews(packageName, R.layout.layout_widget);
                if (mCurrentTrack != null) {
                    remoteViews.setTextViewText(R.id.tv_song, mCurrentTrack.getTitle() + " - " + (StringUtils.isEmptyString(mCurrentTrack.getUsername()) ? context.getString(R.string.title_unknown) : mCurrentTrack.getUsername()));
                } else {
                    remoteViews.setTextViewText(R.id.tv_song, "");
                }
                remoteViews.setImageViewResource(R.id.btn_play, isPlaying ? R.drawable.ic_pause_white_36dp : R.drawable.ic_play_arrow_white_36dp);
                remoteViews.setOnClickPendingIntent(R.id.btn_play, PendingIntent.getBroadcast(context, 100, new Intent(new StringBuilder(String.valueOf(packageName)).append(IMusicConstant.ACTION_TOGGLE_PLAYBACK).toString()), 0));
                remoteViews.setOnClickPendingIntent(R.id.btn_next, PendingIntent.getBroadcast(context, 100, new Intent(new StringBuilder(String.valueOf(packageName)).append(IMusicConstant.ACTION_NEXT).toString()), 0));
                remoteViews.setOnClickPendingIntent(R.id.btn_prev, PendingIntent.getBroadcast(context, 100, new Intent(new StringBuilder(String.valueOf(packageName)).append(IMusicConstant.ACTION_PREVIOUS).toString()), 0));
                Intent playerIntent = new Intent(context, MainActivity.class);
                if (mCurrentTrack != null) {
                    playerIntent.putExtra(ICloudMusicPlayerConstants.KEY_SONG_ID, mCurrentTrack.getId());
                    playerIntent.addFlags(DriveFile.MODE_READ_ONLY);
                }
                remoteViews.setOnClickPendingIntent(R.id.img_play, PendingIntent.getActivity(context, 1000, playerIntent, DriveFile.MODE_READ_ONLY));
                remoteViews.setImageViewResource(R.id.img_play, resId);
                remoteViews.setViewVisibility(R.id.btn_play, !isLoading ? 0 : 4);
                remoteViews.setViewVisibility(R.id.btn_next, !isLoading ? 0 : 4);
                remoteViews.setViewVisibility(R.id.btn_prev, !isLoading ? 0 : 4);
                remoteViews.setViewVisibility(R.id.progressBar1, isLoading ? 0 : 4);
                appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
            }
        //}
    }

    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);
    }

    public void onEnabled(Context context) {
        super.onEnabled(context);
    }

    public void onDisabled(Context context) {
        super.onDisabled(context);
    }
}
