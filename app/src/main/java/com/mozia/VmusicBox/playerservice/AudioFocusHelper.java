package com.mozia.VmusicBox.playerservice;

import android.content.Context;
import android.media.AudioManager;
import android.media.AudioManager.OnAudioFocusChangeListener;

public class AudioFocusHelper implements OnAudioFocusChangeListener {
    private AudioManager mAM;
    private IMusicFocusableListener mFocusable;

    public AudioFocusHelper(Context ctx, IMusicFocusableListener focusable) {
        this.mAM = (AudioManager) ctx.getSystemService("audio");
        this.mFocusable = focusable;
    }

    public boolean requestFocus() {
        return 1 == this.mAM.requestAudioFocus(this, 3, 1);
    }

    public boolean abandonFocus() {
        return 1 == this.mAM.abandonAudioFocus(this);
    }

    public void onAudioFocusChange(int focusChange) {
        if (this.mFocusable != null) {
            switch (focusChange) {
                case -3:
                    this.mFocusable.onLostAudioFocus(true);
                    return;
                case -2:
                case -1:
                    this.mFocusable.onLostAudioFocus(false);
                    return;
                case 1:
                    this.mFocusable.onGainedAudioFocus();
                    return;
                default:
                    return;
            }
        }
    }
}
