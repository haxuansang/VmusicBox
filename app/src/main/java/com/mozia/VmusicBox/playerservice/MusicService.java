package com.mozia.VmusicBox.playerservice;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.media.audiofx.Equalizer;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiManager.WifiLock;
import android.os.Handler;
import android.os.IBinder;
import android.widget.RemoteViews;

import com.google.android.gms.cast.TextTrackStyle;
import com.google.android.gms.drive.DriveFile;
import com.mozia.VmusicBox.MainActivity;
import com.mozia.VmusicBox.R;
import com.mozia.VmusicBox.constants.ICloudMusicPlayerConstants;
import com.mozia.VmusicBox.dataMng.TotalDataManager;
import com.mozia.VmusicBox.dataMng.YPYNetUtils;
import com.mozia.VmusicBox.setting.ISettingConstants;
import com.mozia.VmusicBox.setting.SettingManager;
import com.mozia.VmusicBox.soundclound.ISoundCloundConstants;
import com.mozia.VmusicBox.soundclound.SoundCloundAPI;
import com.mozia.VmusicBox.soundclound.SoundCloundDataMng;
import com.mozia.VmusicBox.soundclound.object.TrackObject;
import com.mozia.VmusicBox.widget.MusicWidgetProvider;
import com.ypyproductions.task.DBTask;
import com.ypyproductions.task.IDBCallback;
import com.ypyproductions.task.IDBTaskListener;
import com.ypyproductions.utils.ApplicationUtils;
import com.ypyproductions.utils.ImageProcessingUtils;
import com.ypyproductions.utils.StringUtils;
import com.ypyproductions.webservice.DownloadUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class MusicService extends Service implements OnCompletionListener, OnPreparedListener, OnErrorListener, IMusicFocusableListener, IMusicConstant, ISoundCloundConstants, ISettingConstants, ICloudMusicPlayerConstants {
    public static final String TAG = MusicService.class.getSimpleName();
    private AudioFocus mAudioFocus = AudioFocus.NO_FOCUS_NO_DUCK;
    private AudioFocusHelper mAudioFocusHelper = null;
    private AudioManager mAudioManager;
    private Bitmap mBitmapTrack;
    private TrackObject mCurrentTrack;
    private Equalizer mEqualizer;
    private Handler mHandler = new Handler();
    private ComponentName mMediaButtonReceiverComponent;
    private Notification mNotification = null;
    private NotificationManager mNotificationManager;
    private MediaPlayer mPlayer = null;
    private RemoteControlClientCompat mRemoteControlClientCompat;
    private String mSongTitle = "";
    private State mState = State.STOPPED;
    private WifiLock mWifiLock;
    private RemoteViews notificationView;
    public boolean isNewSong=false;

    class C05414 implements Runnable {
        C05414() {
        }

        public void run() {
            if (MusicService.this.mPlayer != null && MusicService.this.mCurrentTrack != null) {
                int current = MusicService.this.mPlayer.getCurrentPosition();
                Intent mIntent = new Intent(new StringBuilder(String.valueOf(MusicService.this.getPackageName())).append(IMusicConstant.ACTION_BROADCAST_PLAYER).toString());
                mIntent.putExtra(IMusicConstant.KEY_POSITION, current);
                mIntent.putExtra(IMusicConstant.KEY_ACTION, new StringBuilder(String.valueOf(MusicService.this.getPackageName())).append(IMusicConstant.ACTION_UPDATE_POS).toString());
                MusicService.this.sendBroadcast(mIntent);
                if (((long) current) < MusicService.this.mCurrentTrack.getDuration()) {
                    MusicService.this.startUpdatePosition();
                }
            }
        }
    }

    private enum AudioFocus {
        NO_FOCUS_NO_DUCK,
        NO_FOCUS_CAN_DUCK,
        FOCUSED
    }

    private enum State {
        STOPPED,
        PREPARING,
        PLAYING,
        PAUSED
    }

    class C08331 implements IDBCallback {
        C08331() {
        }

        public void onAction() {
            MusicService.this.processPlayRequest();
        }
    }

    private void createMediaPlayerIfNeeded() {
        if (this.mPlayer == null) {
            this.mPlayer = new MediaPlayer();
            this.mPlayer.setWakeMode(getApplicationContext(), 1);
            this.mPlayer.setOnPreparedListener(this);
            this.mPlayer.setOnCompletionListener(this);
            this.mPlayer.setOnErrorListener(this);
            this.mEqualizer = new Equalizer(0, this.mPlayer.getAudioSessionId());
            this.mEqualizer.setEnabled(SettingManager.getEqualizer(this));
            setUpParams();
            SoundCloundDataMng.getInstance().setPlayer(this.mPlayer);
            SoundCloundDataMng.getInstance().setEqualizer(this.mEqualizer);
            return;
        }
        this.mPlayer.reset();
        intializeEqualizer();
        setUpParams();
        setEqualizerToSoundCloud();
    }

    private void setEqualizerToSoundCloud() {
        SoundCloundDataMng.getInstance().setEqualizer(this.mEqualizer);
    }

    private void intializeEqualizer() {
        if (this.mEqualizer != null) {
            this.mEqualizer.release();
            this.mEqualizer = null;
        }
        this.mEqualizer = new Equalizer(0, this.mPlayer.getAudioSessionId());
        this.mEqualizer.setEnabled(SettingManager.getEqualizer(this));
    }

    private void setUpParams() {
        if (this.mEqualizer != null) {
            String presetStr = SettingManager.getEqualizerPreset(this);
            if (!StringUtils.isEmptyString(presetStr) && StringUtils.isNumber(presetStr)) {
                short preset = Short.parseShort(presetStr);
                short numberPreset = this.mEqualizer.getNumberOfPresets();
                if (numberPreset > (short) 0 && preset < numberPreset - 1 && preset >= (short) 0) {
                    try {
                        this.mEqualizer.usePreset(preset);
                        return;
                    } catch (Exception e) {
                        intializeEqualizer();
                        setEqualizerToSoundCloud();
                        this.mEqualizer.usePreset(preset);
                        return;
                    }
                }
            }
            setUpEqualizerCustom();
        }
    }

    private void setUpEqualizerCustom() {
        if (this.mEqualizer != null) {
            String params = SettingManager.getEqualizerParams(this);
            if (!StringUtils.isEmptyString(params)) {
                String[] mEqualizerParams = params.split(":");
                if (mEqualizerParams != null && mEqualizerParams.length > 0) {
                    int size = mEqualizerParams.length;
                    for (int i = 0; i < size; i++) {
                        try {
                            this.mEqualizer.setBandLevel((short) i, Short.parseShort(mEqualizerParams[i]));
                        } catch (Exception e) {
                            intializeEqualizer();
                            setEqualizerToSoundCloud();
                            this.mEqualizer.setBandLevel((short) i, Short.parseShort(mEqualizerParams[i]));
                        }
                    }
                    SettingManager.setEqualizerPreset(this, String.valueOf(this.mEqualizer.getNumberOfPresets()));
                }
            }
        }
    }

    public void onCreate() {
        this.mWifiLock = ((WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE)).createWifiLock(1, IMusicConstant.WIFI_LOCK_TAG);
        //TODO
        this.mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        this.mAudioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        this.mAudioFocusHelper = new AudioFocusHelper(getApplicationContext(), this);
        this.mMediaButtonReceiverComponent = new ComponentName(this, MusicIntentReceiver.class);
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        String action = intent.getAction();
        String packageName = getPackageName();
        this.isNewSong = intent.getBooleanExtra("newsong",false);


        if (!StringUtils.isEmptyString(action)) {
            if (action.equals(new StringBuilder(String.valueOf(packageName)).append(IMusicConstant.ACTION_TOGGLE_PLAYBACK).toString())) {
                processTogglePlaybackRequest();
            } else if (action.equals(new StringBuilder(String.valueOf(packageName)).append(IMusicConstant.ACTION_PLAY).toString())) {
                processPlayRequest();
            } else if (action.equals(new StringBuilder(String.valueOf(packageName)).append(IMusicConstant.ACTION_PAUSE).toString())) {
                processPauseRequest();
            } else if (action.equals(new StringBuilder(String.valueOf(packageName)).append(IMusicConstant.ACTION_NEXT).toString())) {
                processNextRequest();
            } else if (action.equals(new StringBuilder(String.valueOf(packageName)).append(IMusicConstant.ACTION_STOP).toString())) {
                processStopRequest();
            } else if (action.equals(new StringBuilder(String.valueOf(packageName)).append(IMusicConstant.ACTION_PREVIOUS).toString())) {
                processPreviousRequest();
            } else if (action.equals(new StringBuilder(String.valueOf(packageName)).append(IMusicConstant.ACTION_SEEK).toString())) {
                processSeekBar(intent.getIntExtra(IMusicConstant.KEY_POSITION, -1));
            }
        }
        return 2;
    }

    private void processTogglePlaybackRequest() {
        if (this.mState == State.PAUSED || this.mState == State.STOPPED) {
            processPlayRequest();
        } else {
            processPauseRequest();
        }
    }

    private void processSeekBar(int currentPos) {
        if ((this.mState == State.PLAYING || this.mState == State.PAUSED) && currentPos > 0 && this.mPlayer != null) {
            //DBLog.m25d(TAG, "================>currentPos=" + currentPos);
            this.mPlayer.seekTo(currentPos);
        }
    }

    private void processPlayRequest() {
        if (SoundCloundDataMng.getInstance().getListPlayingTrackObjects() == null) {
            startGetListData(new C08331());
            return;
        }
        this.mCurrentTrack = SoundCloundDataMng.getInstance().getCurrentTrackObject();
        if (this.mCurrentTrack == null) {
            this.mState = State.PAUSED;
            SoundCloundDataMng.getInstance().onDestroy();
            processStopRequest(true);
            onDestroyBitmap();
            updateWidget(false);
            return;
        }
        tryToGetAudioFocus();
        if (this.mState == State.STOPPED || this.mState == State.PLAYING || this.isNewSong==true) {
            playNextSong();
            broadcastAction(getPackageName() + IMusicConstant.ACTION_NEXT);
        } else if (this.mState == State.PAUSED) {
            this.mState = State.PLAYING;
            configAndStartMediaPlayer();
            updateStatusPlayPause();
        }
        if (this.mRemoteControlClientCompat != null) {
            this.mRemoteControlClientCompat.setPlaybackState(3);
        }
    }

    private void startGetListData(final IDBCallback mCallback) { // Quan trong de fix 14/9/2018
        final Context mContext = getApplicationContext();
        final SoundCloundAPI mSoundClound = new SoundCloundAPI(SOUNDCLOUND_CLIENT_ID, SOUNDCLOUND_CLIENT_SECRET);
        new DBTask(new IDBTaskListener() {
            private ArrayList<TrackObject> mListNewTrackObjects;

            public void onPreExcute() {
                MusicService.this.updateWidget(true);
            }

            public void onDoInBackground() {
                if (ApplicationUtils.isOnline(mContext)) {
                    this.mListNewTrackObjects = TotalDataManager.getInstance().getListCurrrentTrackObjects();
                    if (this.mListNewTrackObjects == null || this.mListNewTrackObjects.size() == 0) {
                        if (SettingManager.getSearchType(mContext) == 2) {
                            this.mListNewTrackObjects = mSoundClound.getListTrackObjectsByGenre(SettingManager.getLastKeyword(mContext), 0, 10);
                        } else {
                            this.mListNewTrackObjects = mSoundClound.getListTrackObjectsByQuery(SettingManager.getLastKeyword(mContext), 0, 10);
                        }
                        if (this.mListNewTrackObjects != null && this.mListNewTrackObjects.size() > 0) {
                            TotalDataManager.getInstance().setListCurrrentTrackObjects(this.mListNewTrackObjects);
                            SoundCloundDataMng.getInstance().setListPlayingTrackObjects(this.mListNewTrackObjects);
                            SoundCloundDataMng.getInstance().setCurrentIndex(0);
                            return;
                        }
                        return;
                    }
                    SoundCloundDataMng.getInstance().setListPlayingTrackObjects(this.mListNewTrackObjects);
                    SoundCloundDataMng.getInstance().setCurrentIndex(0);
                    return;
                }
                TotalDataManager.getInstance().readLibraryTrack(mContext);
                this.mListNewTrackObjects = TotalDataManager.getInstance().getListLibraryTrackObjects();
                if (this.mListNewTrackObjects != null && this.mListNewTrackObjects.size() > 0) {
                    SoundCloundDataMng.getInstance().setListPlayingTrackObjects(this.mListNewTrackObjects);
                    SoundCloundDataMng.getInstance().setCurrentIndex(0);
                }
            }

            public void onPostExcute() {
                MusicService.this.updateWidget(true);
                if (mCallback != null) {
                    mCallback.onAction();
                }
            }
        }).execute(new Void[0]);
    }

    private void processPauseRequest() {
        if (this.mCurrentTrack == null) {
            this.mState = State.PAUSED;
            processStopRequest(true);
            return;
        }
        if (this.mState == State.PLAYING) {
            this.mState = State.PAUSED;
            this.mPlayer.pause();
            relaxResources(false);
            updateStatusPlayPause();
            SettingManager.setPlayingState(this, false);
            broadcastAction(getPackageName() + IMusicConstant.ACTION_PAUSE);
        }
        if (this.mRemoteControlClientCompat != null) {
            this.mRemoteControlClientCompat.setPlaybackState(2);
        }
    }

    private void processPreviousRequest() {
        if (this.mState == State.PLAYING || this.mState == State.PAUSED || this.mState == State.STOPPED) {
            this.mCurrentTrack = SoundCloundDataMng.getInstance().getPrevTrackObject(this);
            if (this.mCurrentTrack != null) {
                tryToGetAudioFocus();
                playNextSong();
                return;
            }
            this.mState = State.PAUSED;
            processStopRequest(true);
        }
    }

    private void onDestroyBitmap() {
        if (this.mBitmapTrack != null) {
            this.mBitmapTrack.recycle();
            this.mBitmapTrack = null;
        }
    }

    private void startDownloadBitmap() {
        if (this.mCurrentTrack != null) {
            String artwork = this.mCurrentTrack.getArtworkUrl();
            if (!StringUtils.isEmptyString(artwork)) {
                if (artwork.startsWith("http")) {
                    try {
                        InputStream mInputStream = DownloadUtils.download(artwork);
                        if (mInputStream != null) {
                            this.mBitmapTrack = ImageProcessingUtils.decodePortraitBitmap(mInputStream, 100, 100);
                            mInputStream.close();
                            return;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    File mFile = new File(artwork);
                    if (mFile.exists() && mFile.isFile()) {
                        Options mOptions = new Options();
                        mOptions.inJustDecodeBounds = true;
                        BitmapFactory.decodeFile(artwork, mOptions);
                        ImageProcessingUtils.calculateInSampleSize(mOptions, 100, 100);
                        mOptions.inJustDecodeBounds = false;
                        this.mBitmapTrack = BitmapFactory.decodeFile(artwork, mOptions);
                        return;
                    }
                }
            }
        }
        this.mBitmapTrack = BitmapFactory.decodeResource(getResources(), R.drawable.ic_music_default);
    }

    private void processNextRequest() {
        if (this.mState == State.PLAYING || this.mState == State.PAUSED || this.mState == State.STOPPED) {
            this.mCurrentTrack = SoundCloundDataMng.getInstance().getNextTrackObject(this);
            //DBLog.m25d(TAG, "==========>mCurrentTrack=" + this.mCurrentTrack);
            if (this.mCurrentTrack != null) {
                tryToGetAudioFocus();
                playNextSong();
                return;
            }
            this.mState = State.PAUSED;
            processStopRequest(true);
        }
    }

    private void processStopRequest() {
        processStopRequest(false);
    }

    private void processStopRequest(boolean force) {
        if (this.mState == State.PLAYING || this.mState == State.PAUSED || force) {
            SettingManager.setPlayingState(this, false);
            this.mHandler.removeCallbacksAndMessages(null);
            this.mState = State.STOPPED;
            relaxResources(true);
            giveUpAudioFocus();
            if (this.mRemoteControlClientCompat != null) {
                this.mRemoteControlClientCompat.setPlaybackState(1);
            }
            broadcastAction(getPackageName() + IMusicConstant.ACTION_STOP);
            stopSelf();
        }
        updateWidget(false);
    }

    private void relaxResources(boolean releaseMediaPlayer) {
        if (releaseMediaPlayer && this.mPlayer != null) {
            stopForeground(true);
            this.mPlayer.reset();
            this.mPlayer.release();
            this.mPlayer = null;
            if (this.mEqualizer != null) {
                this.mEqualizer.release();
                this.mEqualizer = null;
            }
            SoundCloundDataMng.getInstance().setEqualizer(null);
            SoundCloundDataMng.getInstance().setPlayer(null);
        }
        if (this.mWifiLock.isHeld()) {
            this.mWifiLock.release();
        }
    }

    private void giveUpAudioFocus() {
        if (this.mAudioFocus != null && this.mAudioFocus == AudioFocus.FOCUSED && this.mAudioFocusHelper != null && this.mAudioFocusHelper.abandonFocus()) {
            this.mAudioFocus = AudioFocus.NO_FOCUS_NO_DUCK;
        }
    }

    private void configAndStartMediaPlayer() {
        if (this.mPlayer == null) {
            return;
        }
        if (this.mAudioFocus != AudioFocus.NO_FOCUS_NO_DUCK) {
            if (this.mAudioFocus == AudioFocus.NO_FOCUS_CAN_DUCK) {
                this.mPlayer.setVolume(IMusicConstant.DUCK_VOLUME, IMusicConstant.DUCK_VOLUME);
            } else {
                this.mPlayer.setVolume(TextTrackStyle.DEFAULT_FONT_SCALE, TextTrackStyle.DEFAULT_FONT_SCALE);
            }
            if (!this.mPlayer.isPlaying()) {
                this.mPlayer.start();
                SettingManager.setPlayingState(this, true);
                startUpdatePosition();
                broadcastAction(getPackageName() + IMusicConstant.ACTION_PLAY);
            }
        } else if (this.mPlayer.isPlaying()) {
            this.mPlayer.pause();
            SettingManager.setPlayingState(this, false);
            this.mHandler.removeCallbacksAndMessages(null);
            broadcastAction(getPackageName() + IMusicConstant.ACTION_PAUSE);
        }
    }

    private void broadcastAction(String action) {
        Intent mIntent = new Intent(getPackageName() + IMusicConstant.ACTION_BROADCAST_PLAYER);
        mIntent.putExtra(IMusicConstant.KEY_ACTION, action);
        sendBroadcast(mIntent);
    }

    private void tryToGetAudioFocus() {
        if (this.mAudioFocus != null && this.mAudioFocus != AudioFocus.FOCUSED && this.mAudioFocusHelper != null && this.mAudioFocusHelper.requestFocus()) {
            this.mAudioFocus = AudioFocus.FOCUSED;
        }
    }

    private void playNextSong() {
        this.mState = State.STOPPED;
        relaxResources(false);
        this.mHandler.removeCallbacksAndMessages(null);
        if (this.mCurrentTrack == null) {
            this.mState = State.PAUSED;
            processStopRequest(true);
            updateWidget(true);
            return;
        }
        sendBroadcast(new Intent(getPackageName() + IMusicConstant.ACTION_BROADCAST_COUNT_PLAY));
        startGetLinkStream();
    }

    private void startGetLinkStream() {
        if (this.mCurrentTrack != null) {
            final String packageName = getPackageName();
            onDestroyBitmap();
            if (StringUtils.isEmptyString(this.mCurrentTrack.getPath())) {
                String linkStream = this.mCurrentTrack.getLinkStream();
                if (StringUtils.isEmptyString(linkStream)) {
                    new DBTask(new IDBTaskListener() {
                        private String finalUrl;

                        public void onPreExcute() {
                            MusicService.this.broadcastAction(packageName + IMusicConstant.ACTION_LOADING);
                            MusicService.this.updateWidget(true);
                        }

                        public void onDoInBackground() {
                            if (MusicService.this.mCurrentTrack.isStreamable()) {
                                this.finalUrl = YPYNetUtils.getLinkStreamFromSoundClound(MusicService.this.mCurrentTrack.getId());
                            }
                            if (StringUtils.isEmptyString(this.finalUrl)) {
                                this.finalUrl = String.format(ISoundCloundConstants.FORMAT_URL_SONG, new Object[]{Long.valueOf(MusicService.this.mCurrentTrack.getId()), MusicService.SOUNDCLOUND_CLIENT_ID});
                            }
                            MusicService.this.startDownloadBitmap();
                        }

                        public void onPostExcute() {
                            //DBLog.m25d(MusicService.TAG, "========>final Url=" + this.finalUrl);
                            if (StringUtils.isEmptyString(this.finalUrl)) {
                                MusicService.this.broadcastAction(packageName + IMusicConstant.ACTION_DIMISS_LOADING);
                                return;
                            }
                            MusicService.this.startStreamWithUrl(this.finalUrl);
                            MusicService.this.mCurrentTrack.setLinkStream(this.finalUrl);
                        }
                    }).execute(new Void[0]);
                    return;
                }
                broadcastAction(new StringBuilder(String.valueOf(packageName)).append(IMusicConstant.ACTION_LOADING).toString());
                updateWidget(true);
                startDownloadBitmap();
                startStreamWithUrl(linkStream);
                return;
            }
            broadcastAction(new StringBuilder(String.valueOf(packageName)).append(IMusicConstant.ACTION_LOADING).toString());
            updateWidget(true);
            startStreamWithUrl(null);
        }
    }

    private void startStreamWithUrl(String manualUrl) {
        boolean isCanPlay = false;
        boolean isNeedAllowPreSyn = true;
        try {
            this.mSongTitle = this.mCurrentTrack.getTitle();
            if (!StringUtils.isEmptyString(this.mCurrentTrack.getPath())) {
                createMediaPlayerIfNeeded();
                this.mPlayer.setDataSource(this, this.mCurrentTrack.getURI());
                this.mPlayer.prepare();
                isCanPlay = true;
                isNeedAllowPreSyn = false;
            } else if (!StringUtils.isEmptyString(manualUrl)) {
                createMediaPlayerIfNeeded();
                this.mPlayer.setAudioStreamType(3);
                this.mPlayer.setDataSource(manualUrl);
                isCanPlay = true;
            }
            if (isCanPlay) {
                this.mState = State.PREPARING;
                setUpAsForeground();
                MediaButtonHelper.registerMediaButtonEventReceiverCompat(this.mAudioManager, this.mMediaButtonReceiverComponent);
                if (this.mRemoteControlClientCompat == null) {
                    Intent intent = new Intent("android.intent.action.MEDIA_BUTTON");
                    intent.setComponent(this.mMediaButtonReceiverComponent);
                    this.mRemoteControlClientCompat = new RemoteControlClientCompat(PendingIntent.getBroadcast(this, 0, intent, 0));
                    RemoteControlHelper.registerRemoteControlClient(this.mAudioManager, this.mRemoteControlClientCompat);
                }
                this.mRemoteControlClientCompat.setPlaybackState(3);
                this.mRemoteControlClientCompat.setTransportControlFlags(181);
                this.mRemoteControlClientCompat.editMetadata(true).putString(2, this.mCurrentTrack.getUsername()).putString(3, this.mCurrentTrack.getUsername()).putString(7, this.mSongTitle).putLong(9, this.mCurrentTrack.getDuration()).apply();
                if (isNeedAllowPreSyn) {
                    this.mPlayer.prepareAsync();
                }
                this.mWifiLock.acquire();
            }
        } catch (IOException ex) {
            //DBLog.m25d("MusicService", "IOException playing next song: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    public void onCompletion(MediaPlayer player) {
        this.mState = State.STOPPED;
        SettingManager.setPlayingState(this, false);
        if (SettingManager.getRepeat(this)) {
            playNextSong();
        } else {
            processNextRequest();
        }
        broadcastAction(getPackageName() + IMusicConstant.ACTION_NEXT);
    }

    public void onPrepared(MediaPlayer player) {
        broadcastAction(getPackageName() + IMusicConstant.ACTION_DIMISS_LOADING);
        this.mState = State.PLAYING;
        configAndStartMediaPlayer();
        updateNotification(this.mSongTitle);
    }

    private void updateNotification(String text) {
        this.mNotificationManager.notify(1000, this.mNotification);
        updateWidget(false);
    }

    private void updateStatusPlayPause() {
        if (this.mPlayer != null && this.notificationView != null) {
            if (this.mBitmapTrack != null) {
                this.notificationView.setImageViewBitmap(R.id.img_play, this.mBitmapTrack);
            } else {
                this.notificationView.setImageViewResource(R.id.img_play, R.drawable.ic_music_default);
            }
            this.notificationView.setImageViewResource(R.id.btn_play, this.mPlayer.isPlaying() ? R.drawable.ic_pause_white_36dp : R.drawable.ic_play_arrow_white_36dp);
            this.mNotificationManager.notify(1000, this.mNotification);
            updateWidget(false);
        }
    }

    private void updateWidget(boolean isLoading) {
        boolean z = false;
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] allWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(getApplicationContext(), MusicWidgetProvider.class));
        if (this.mBitmapTrack != null) {
            if (this.mPlayer != null) {
                z = this.mPlayer.isPlaying();
            }
            MusicWidgetProvider.updateWidget((Context) this, appWidgetManager, allWidgetIds, z, isLoading, this.mBitmapTrack);
            return;
        }
        if (this.mPlayer != null) {
            z = this.mPlayer.isPlaying();
        }
        MusicWidgetProvider.updateWidget((Context) this, appWidgetManager, allWidgetIds, z, isLoading, (int) R.drawable.ic_music_default);
    }

    private void setUpAsForeground() {
        if (this.mCurrentTrack != null) {
            Intent mIntent = new Intent(getApplicationContext(), MainActivity.class);
            mIntent.putExtra(ICloudMusicPlayerConstants.KEY_SONG_ID, this.mCurrentTrack.getId());
            mIntent.addFlags(DriveFile.MODE_READ_ONLY);
            PendingIntent pi = PendingIntent.getActivity(getApplicationContext(), 1000, mIntent, DriveFile.MODE_READ_ONLY);
            this.mNotification = new Notification(R.mipmap.ic_launcher_2, this.mCurrentTrack.getTitle(), System.currentTimeMillis());
            this.notificationView = new RemoteViews(getPackageName(), R.layout.item_small_notification_music);
            this.notificationView.setTextViewText(R.id.tv_song, this.mCurrentTrack.getTitle());
            this.notificationView.setTextViewText(R.id.tv_singer, StringUtils.isEmptyString(this.mCurrentTrack.getUsername()) ? getString(R.string.title_unknown) : this.mCurrentTrack.getUsername());
            this.notificationView.setImageViewResource(R.id.btn_play, R.drawable.ic_pause_white_36dp);
            if (this.mBitmapTrack != null) {
                this.notificationView.setImageViewBitmap(R.id.img_play, this.mBitmapTrack);
            } else {
                this.notificationView.setImageViewResource(R.id.img_play, R.drawable.ic_music_default);
            }
            String packageName = getPackageName();
            this.notificationView.setOnClickPendingIntent(R.id.btn_play, PendingIntent.getBroadcast(this, 100, new Intent(new StringBuilder(String.valueOf(packageName)).append(IMusicConstant.ACTION_TOGGLE_PLAYBACK).toString()), 0));
            this.notificationView.setOnClickPendingIntent(R.id.btn_next, PendingIntent.getBroadcast(this, 100, new Intent(new StringBuilder(String.valueOf(packageName)).append(IMusicConstant.ACTION_NEXT).toString()), 0));
            this.notificationView.setOnClickPendingIntent(R.id.btn_prev, PendingIntent.getBroadcast(this, 100, new Intent(new StringBuilder(String.valueOf(packageName)).append(IMusicConstant.ACTION_PREVIOUS).toString()), 0));
            this.notificationView.setOnClickPendingIntent(R.id.btn_close, PendingIntent.getBroadcast(this, 100, new Intent(new StringBuilder(String.valueOf(packageName)).append(IMusicConstant.ACTION_STOP).toString()), 0));
            this.mNotification.contentView = this.notificationView;
            this.mNotification.contentIntent = pi;
            Notification notification = this.mNotification;
            notification.flags |= 32;
            startForeground(1000, this.mNotification);
            updateWidget(true);
        }
    }

    public boolean onError(MediaPlayer mp, int what, int extra) {
        //DBLog.m26e(TAG, "Error: what=" + String.valueOf(what) + ", extra=" + String.valueOf(extra));
        broadcastAction(getPackageName() + IMusicConstant.ACTION_DIMISS_LOADING);
        this.mState = State.PAUSED;
        processStopRequest(true);
        return true;
    }

    public void onGainedAudioFocus() {
        this.mAudioFocus = AudioFocus.FOCUSED;
        if (this.mState == State.PLAYING) {
            configAndStartMediaPlayer();
        }
    }

    public void onLostAudioFocus(boolean canDuck) {
        this.mAudioFocus = canDuck ? AudioFocus.NO_FOCUS_CAN_DUCK : AudioFocus.NO_FOCUS_NO_DUCK;
        if (this.mPlayer != null && this.mPlayer.isPlaying()) {
            configAndStartMediaPlayer();
        }
    }

    private void startUpdatePosition() {
        this.mHandler.postDelayed(new C05414(), 1000);
    }

    public void onDestroy() {
        onDestroyBitmap();
        this.mHandler.removeCallbacksAndMessages(null);
        this.mState = State.STOPPED;
        try {
            relaxResources(true);
            giveUpAudioFocus();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public IBinder onBind(Intent arg0) {
        return null;
    }
}
