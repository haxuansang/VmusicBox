package com.mozia.VmusicBox.playerservice;

public interface IMusicFocusableListener {
    void onGainedAudioFocus();

    void onLostAudioFocus(boolean z);
}
