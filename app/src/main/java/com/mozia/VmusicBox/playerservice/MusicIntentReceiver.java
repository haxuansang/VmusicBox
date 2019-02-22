package com.mozia.VmusicBox.playerservice;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.view.KeyEvent;

import com.google.android.gms.location.places.Place;
import com.mozia.VmusicBox.setting.SettingManager;
import com.mozia.VmusicBox.soundclound.SoundCloundDataMng;
import com.mozia.VmusicBox.soundclound.object.TrackObject;
import com.ypyproductions.utils.StringUtils;

import java.util.ArrayList;

public class MusicIntentReceiver extends BroadcastReceiver implements IMusicConstant {
    public static final String TAG = MusicIntentReceiver.class.getSimpleName();
    private ArrayList<TrackObject> mListTrack;

    public void onReceive(Context context, Intent intent) {
        if (intent != null) {
            String action = intent.getAction();
            if (!StringUtils.isEmptyString(action)) {
                this.mListTrack = SoundCloundDataMng.getInstance().getListPlayingTrackObjects();
                String packageName = context.getPackageName();
                if (action.equals("android.media.AUDIO_BECOMING_NOISY")) {
                    Intent intent1 =  new Intent(new StringBuilder(String.valueOf(packageName)).append(IMusicConstant.ACTION_PAUSE).toString());
                    intent1.setPackage(String.valueOf(packageName));
                    context.startService(intent1);
                } else if (action.equals(new StringBuilder(String.valueOf(packageName)).append(IMusicConstant.ACTION_NEXT).toString())) {
                    Intent intent1 = new Intent(new StringBuilder(String.valueOf(packageName)).append(IMusicConstant.ACTION_NEXT).toString());
                    intent1.setPackage(String.valueOf(packageName));
                    context.startService(intent1);

                } else if (action.equals(new StringBuilder(String.valueOf(packageName)).append(IMusicConstant.ACTION_TOGGLE_PLAYBACK).toString())) {
                    Intent mIntent =  new Intent(new StringBuilder(String.valueOf(packageName)).append(IMusicConstant.ACTION_TOGGLE_PLAYBACK).toString());
                    mIntent.setPackage(String.valueOf(packageName));
                    context.startService(mIntent);
                } else if (action.equals(new StringBuilder(String.valueOf(packageName)).append(IMusicConstant.ACTION_PREVIOUS).toString())) {
                    Intent mIntent = new Intent(new StringBuilder(String.valueOf(packageName)).append(IMusicConstant.ACTION_PREVIOUS).toString());
                    mIntent.setPackage(String.valueOf(packageName));
                    context.startService(mIntent);
                } else if (action.equals(new StringBuilder(String.valueOf(packageName)).append(IMusicConstant.ACTION_STOP).toString())) {
                    Intent mIntent = new Intent(new StringBuilder(String.valueOf(packageName)).append(IMusicConstant.ACTION_STOP).toString());
                    mIntent.setPackage(String.valueOf(packageName));
                    context.startService(mIntent);
                } else if (!action.equals("android.intent.action.MEDIA_BUTTON")) {
                } else {
                    if (this.mListTrack == null || this.mListTrack.size() == 0) {
                        SoundCloundDataMng.getInstance().onDestroy();
                        Intent mIntent = new Intent(new StringBuilder(String.valueOf(packageName)).append(IMusicConstant.ACTION_STOP).toString());
                        mIntent.setPackage(String.valueOf(packageName));
                        context.startService(new Intent(new StringBuilder(String.valueOf(packageName)).append(IMusicConstant.ACTION_STOP).toString()));
                        return;
                    }
                    KeyEvent keyEvent = (KeyEvent) intent.getExtras().get("android.intent.extra.KEY_EVENT");
                    if (keyEvent.getAction() == 0) {
                        switch (keyEvent.getKeyCode()) {
                            case Place.TYPE_RESTAURANT /*79*/:
                            case Place.TYPE_SPA /*85*/:
                                if (SettingManager.getOnline(context)) {
                                    Intent mIntent =  new Intent(new StringBuilder(String.valueOf(packageName)).append(IMusicConstant.ACTION_TOGGLE_PLAYBACK).toString());
                                    mIntent.setPackage(String.valueOf(packageName));
                                    context.startService(new Intent(new StringBuilder(String.valueOf(packageName)).append(IMusicConstant.ACTION_TOGGLE_PLAYBACK).toString()));
                                    return;
                                } else {
                                    context.startService(new Intent(new StringBuilder(String.valueOf(packageName)).append(IMusicConstant.ACTION_STOP).toString()));
                                    return;
                                }
                            case Place.TYPE_STADIUM /*86*/:
                                context.startService(new Intent(new StringBuilder(String.valueOf(packageName)).append(IMusicConstant.ACTION_STOP).toString()));
                                return;
                            case Place.TYPE_STORAGE /*87*/:
                                context.startService(new Intent(new StringBuilder(String.valueOf(packageName)).append(IMusicConstant.ACTION_NEXT).toString()));
                                return;
                            case Place.TYPE_STORE /*88*/:
                                context.startService(new Intent(new StringBuilder(String.valueOf(packageName)).append(IMusicConstant.ACTION_PREVIOUS).toString()));
                                return;
                            case 126: /*126*/
                                context.startService(new Intent(new StringBuilder(String.valueOf(packageName)).append(IMusicConstant.ACTION_PLAY).toString()));
                                return;
                            case 127: /*127*/
                                if (SettingManager.getOnline(context)) {
                                    context.startService(new Intent(new StringBuilder(String.valueOf(packageName)).append(IMusicConstant.ACTION_PAUSE).toString()));
                                    return;
                                } else {
                                    context.startService(new Intent(new StringBuilder(String.valueOf(packageName)).append(IMusicConstant.ACTION_STOP).toString()));
                                    return;
                                }
                            default:
                                return;
                        }
                    }
                }
            }
        }
    }
}
