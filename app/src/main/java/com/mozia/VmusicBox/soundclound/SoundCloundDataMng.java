package com.mozia.VmusicBox.soundclound;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.audiofx.Equalizer;

import com.mozia.VmusicBox.setting.SettingManager;
import com.mozia.VmusicBox.soundclound.object.TrackObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

public class SoundCloundDataMng {
    public static final String TAG = SoundCloundDataMng.class.getSimpleName();
    private static SoundCloundDataMng instance;
    private int currentIndex = -1;
    private TrackObject currentTrackObject;
    private Equalizer equalizer;
    private ArrayList<TrackObject> listTrackObjects;
    private Random mRandom = new Random();
    private MediaPlayer player;

    private SoundCloundDataMng() {
    }

    public static SoundCloundDataMng getInstance() {
        if (instance == null) {
            instance = new SoundCloundDataMng();
        }
        return instance;
    }

    public void onDestroy() {
        if (this.listTrackObjects != null) {
            this.listTrackObjects.clear();
            this.listTrackObjects = null;
        }
        this.mRandom = null;
        instance = null;
    }

    public void onResetMedia() {
        try {
            if (this.equalizer != null) {
                this.equalizer.release();
                this.equalizer = null;
            }
            if (this.player != null) {
                if (this.player.isPlaying()) {
                    this.player.stop();
                }
                this.player.release();
                this.player = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setListPlayingTrackObjects(ArrayList<TrackObject> listTrackObjects) {
        if (listTrackObjects != null) {
            if (listTrackObjects.size() > 0) {
                boolean isNeedToReset = true;
                if (this.currentTrackObject != null) {
                    Iterator it = listTrackObjects.iterator();
                    while (it.hasNext()) {
                        TrackObject mTrackObject = (TrackObject) it.next();
                        if (this.currentTrackObject.getId() == mTrackObject.getId()) {
                            isNeedToReset = false;
                            this.currentIndex = listTrackObjects.indexOf(mTrackObject);
                            break;
                        }
                    }
                }
                if (isNeedToReset) {
                    this.currentIndex = 0;
                }
            } else {
                this.currentIndex = -1;
            }
        }
        this.listTrackObjects = listTrackObjects;
    }

    public TrackObject getTrackObject(long id) {
        if (this.listTrackObjects != null && this.listTrackObjects.size() > 0) {
            Iterator it = this.listTrackObjects.iterator();
            while (it.hasNext()) {
                TrackObject mTrackObject = (TrackObject) it.next();
                if (mTrackObject.getId() == id) {
                    return mTrackObject;
                }
            }
        }
        return null;
    }

    public int getCurrentIndex() {
        return this.currentIndex;
    }

    public void setCurrentIndex(int currentIndex) {
        this.currentIndex = currentIndex;
        if (this.listTrackObjects != null && this.listTrackObjects.size() > 0 && currentIndex >= 0 && currentIndex < this.listTrackObjects.size()) {
            this.currentTrackObject = (TrackObject) this.listTrackObjects.get(currentIndex);
        }
    }

    public boolean setCurrentIndex(TrackObject mTrackObject) {
        if (this.listTrackObjects == null || this.listTrackObjects.size() <= 0 || mTrackObject == null) {
            return false;
        }
        this.currentTrackObject = mTrackObject;
        this.currentIndex = this.listTrackObjects.indexOf(mTrackObject);
        if (this.currentIndex >= 0) {
            return true;
        }
        this.currentIndex = 0;
        return false;
    }
    public boolean setCurrentIndex(TrackObject mTrackObject,int position) {
        if (this.listTrackObjects == null || this.listTrackObjects.size() <= 0 || mTrackObject == null) {
            return false;
        }
        this.currentTrackObject = mTrackObject;
        this.currentIndex = position;
       return true;
    }

    public ArrayList<TrackObject> getListPlayingTrackObjects() {
        return this.listTrackObjects;
    }

    public TrackObject getCurrentTrackObject() {
        return this.currentTrackObject;
    }

    public TrackObject getNextTrackObject(Context mContext) {
        if (this.listTrackObjects != null) {
            int size = this.listTrackObjects.size();
            //DBLog.m25d(TAG, "==========>currentIndex=" + this.currentIndex);
            if (size > 0 && this.currentIndex >= 0 && this.currentIndex <= size) {
                if (SettingManager.getShuffle(mContext)) {
                    this.currentIndex = this.mRandom.nextInt(size);
                    this.currentTrackObject = (TrackObject) this.listTrackObjects.get(this.currentIndex);
                    return this.currentTrackObject;
                }
                this.currentIndex++;
                if (this.currentIndex >= size) {
                    this.currentIndex = 0;
                }
                this.currentTrackObject = (TrackObject) this.listTrackObjects.get(this.currentIndex);
                return this.currentTrackObject;
            }
        }
        return null;
    }

    public TrackObject getPrevTrackObject(Context mContext) {
        if (this.listTrackObjects != null) {
            int size = this.listTrackObjects.size();
            if (size > 0 && this.currentIndex >= 0 && this.currentIndex <= size) {
                if (SettingManager.getShuffle(mContext)) {
                    this.currentIndex = this.mRandom.nextInt(size);
                    this.currentTrackObject = (TrackObject) this.listTrackObjects.get(this.currentIndex);
                    return this.currentTrackObject;
                }
                this.currentIndex--;
                if (this.currentIndex < 0) {
                    this.currentIndex = size - 1;
                }
                this.currentTrackObject = (TrackObject) this.listTrackObjects.get(this.currentIndex);
                return this.currentTrackObject;
            }
        }
        return null;
    }

    public MediaPlayer getPlayer() {
        return this.player;
    }

    public void setPlayer(MediaPlayer player) {
        this.player = player;
    }

    public Equalizer getEqualizer() {
        return this.equalizer;
    }

    public void setEqualizer(Equalizer equalizer) {
        this.equalizer = equalizer;
    }
}
