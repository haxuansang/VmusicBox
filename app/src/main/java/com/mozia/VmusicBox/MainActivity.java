package com.mozia.VmusicBox;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.speech.RecognizerIntent;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.SearchView.OnQueryTextListener;
import android.support.v7.widget.SearchView.OnSuggestionListener;
import android.support.v7.widget.Toolbar;
import android.test.mock.MockPackageManager;
import android.text.Html;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.actions.SearchIntents;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.mozia.VmusicBox.APIWeather.Retrofit.APIUtils;
import com.mozia.VmusicBox.APIWeather.Retrofit.SOService;
import com.mozia.VmusicBox.APIWeather.SOAnswersResponse;
import com.mozia.VmusicBox.adapter.DBSlidingTripAdapter;
import com.mozia.VmusicBox.adapter.SuggestionAdapter;
import com.mozia.VmusicBox.adapter.TrackAdapter;
import com.mozia.VmusicBox.app.Navigator;
import com.mozia.VmusicBox.constants.ICloudMusicPlayerConstants;
import com.mozia.VmusicBox.dataMng.TotalDataManager;
import com.mozia.VmusicBox.dataMng.XMLParsingData;
import com.mozia.VmusicBox.fragment.DiscoverFragment;
import com.mozia.VmusicBox.fragment.FragmentLibrary;
import com.mozia.VmusicBox.fragment.FragmentMoreInformation;
import com.mozia.VmusicBox.fragment.FragmentMusicGenre;
import com.mozia.VmusicBox.fragment.FragmentPlaylist;
import com.mozia.VmusicBox.fragment.FragmentSearch;
import com.mozia.VmusicBox.fragment.FragmentTopMusic;
import com.mozia.VmusicBox.fragment.NewPlaylistFragment;
import com.mozia.VmusicBox.object.DBImageLoader;
import com.mozia.VmusicBox.object.PlaylistObject;
import com.mozia.VmusicBox.object.TopMusicObject;
import com.mozia.VmusicBox.playerservice.IMusicConstant;
import com.mozia.VmusicBox.setting.ISettingConstants;
import com.mozia.VmusicBox.setting.SettingManager;
import com.mozia.VmusicBox.soundclound.ISoundCloundConstants;
import com.mozia.VmusicBox.soundclound.SoundCloundAPI;
import com.mozia.VmusicBox.soundclound.SoundCloundDataMng;
import com.mozia.VmusicBox.soundclound.object.TrackObject;
import com.mozia.VmusicBox.utils.ShareUtils;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration.Builder;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.tweetcomposer.ComposerActivity;
import com.ypyproductions.abtractclass.fragment.IDBFragmentConstants;
import com.ypyproductions.task.IDBCallback;
import com.ypyproductions.utils.ApplicationUtils;
import com.ypyproductions.utils.DBListExcuteAction;
import com.ypyproductions.utils.DirectionUtils;
import com.ypyproductions.utils.ResolutionUtils;
import com.ypyproductions.utils.ShareActionUtils;
import com.ypyproductions.utils.StringUtils;
import com.ypyproductions.webservice.DownloadUtils;

import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.READ_CONTACTS;
import static android.Manifest.permission.READ_SMS;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

@SuppressLint({"NewApi"})
public class MainActivity extends DBFragmentActivity implements LocationListener, IDBFragmentConstants, ISoundCloundConstants, ISettingConstants, IMusicConstant, BottomNavigationView.OnNavigationItemSelectedListener {
    private static final String APP_PNAME = "musictune.com";
    public static final String TAG = MainActivity.class.getSimpleName();
    private ActionBar toolBar;
    private AdView adView;
    public boolean isFirstTime;
    private RelativeLayout layoutAd;
    public DisplayImageOptions mAvatarOptions;
    private LayoutParams mBottomLayoutParams;
    private LayoutParams mBottomSmallLayoutParams;
    private Button mBtnNext;
    private Button mBtnPlay;
    private Button mBtnPrev;
    private Button mBtnSmallNext;
    private Button mBtnSmallPlay;
    private CheckBox mCbRepeat;
    private CheckBox mCbShuffe;
    private String[] mColumns;
    private TrackObject mCurrentTrack;
    private MatrixCursor mCursor;
    private Date mDate;
    private ImageView mImgAvatar;
    private ImageView mImgSmallSong;
    public de.hdodenhof.circleimageview.CircleImageView mImgTrack;
    public ImageView mImgTrack1;
    public DisplayImageOptions mImgTrackOptions;
    private RelativeLayout mLayoutControl;
    private RelativeLayout mLayoutPlayMusic;

    private RelativeLayout mMainLayoutPlayMusic;


    private RelativeLayout mLayoutSmallMusic;
    private ArrayList<Fragment> mListFragments = new ArrayList();
    private String[] mListStr;
    private ArrayList<String> mListSuggestionStr;
    private ArrayList<String> mListTitle = new ArrayList();
    private Menu mMenu;

    private MusicPlayerBroadcast mPlayerBroadcast;
    private SeekBar mSeekbar;
    public SoundCloundAPI mSoundClound = new SoundCloundAPI(SOUNDCLOUND_CLIENT_ID, SOUNDCLOUND_CLIENT_SECRET);
    private SuggestionAdapter mSuggestAdapter;
    private DBSlidingTripAdapter mTabAdapters;
    private Object[] mTempData;
    private LayoutParams mTopLayoutParams;
    private LayoutParams mTopSmallLayoutParams;
    private TextView mTvCurrentTime;
    private TextView mTvDuration;
    public TextView mTvLink;
    private TextView mTvPlayCount;
    private TextView mTvSmallSong;
    private TextView mTvTime;
    private TextView mTvTitleSongs;
    private TextView mTvUserName;
    private ViewPager mViewPager;
    protected ProgressDialog progressDialog;
    private SearchView searchView;
    private Intent mIntent;
    private LocationManager locationManager;
    private SOService mService;

    // public  static  String urlImage;
    private final int REQ_CODE_SPEECH_INPUT = ;// code for result of activity speech voice
    private String urlImage;
    private static final int REQUEST_CODE_PERMISSION = 2;
    private Location location;
    private BottomNavigationView bottomNavigation;


    @Override
    public void onLocationChanged(Location location) {
        getCurrentCity(location);
        locationManager.removeUpdates(this);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.menu_item_YourBox:
                mViewPager.setCurrentItem(0,true);
                break;
            case R.id.menu_item_Discover:
                mViewPager.setCurrentItem(1,true);
                break;
            case R.id.menu_item_Playlists:
                mViewPager.setCurrentItem(2,true);
                break;

            case R.id.menu_item_more:
                mViewPager.setCurrentItem(3,true);
                break;
        }
        return true;
    }

    class C10852 implements ShareUtils.OnCallbackShareListener {
        C10852() {

        }

        public void success() {

        }

        public void failed(String message) {

        }

        public void cancel() {

        }
    }

    static class C05061 implements OnClickListener {
        private final /* synthetic */ Dialog val$dialog;
        private final /* synthetic */ Context val$mContext;

        C05061(Context context, Dialog dialog) {
            this.val$mContext = context;
            this.val$dialog = dialog;
        }

        public void onClick(View v) {
            this.val$mContext.startActivity(new Intent("android.intent.action.VIEW", Uri.parse("market://details?id=musictune.com")));
            this.val$dialog.dismiss();
        }
    }

    static class C05082 implements OnClickListener {
        private final /* synthetic */ Dialog val$dialog;

        C05082(Dialog dialog) {
            this.val$dialog = dialog;
        }

        public void onClick(View v) {
            this.val$dialog.dismiss();

        }
    }

    class C05093 implements OnClickListener {
        C05093() {
        }

        public void onClick(View v) {
            MainActivity.this.onHiddenPlay(true);

        }
    }

    class C05104 implements OnClickListener {
        C05104() {
        }

        public void onClick(View v) {
            if (MainActivity.this.mLayoutPlayMusic.getVisibility() != View.VISIBLE) {
                //MainActivity.this.mLayoutPlayMusic.setVisibility(0);A
                animateViewVisibility(mLayoutPlayMusic, 0);

            }
        }
    }

    class C05115 implements OnClickListener {
        C05115() {
        }

        public void onClick(View v) {
            MainActivity.this.onProcessPausePlayAction();

        }
    }

    class C05126 implements OnClickListener {
        C05126() {
        }

        public void onClick(View v) {
            MainActivity.this.nextTrack();

        }
    }

    class C05137 implements OnSeekBarChangeListener {
        C05137() {
        }

        public void onStopTrackingTouch(SeekBar seekBar) {
        }

        public void onStartTrackingTouch(SeekBar seekBar) {
        }

        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if (fromUser && MainActivity.this.mCurrentTrack != null) {
                int currentPos = (int) (((float) (((long) progress) * MainActivity.this.mCurrentTrack.getDuration())) / 100.0f);
                //DBLog.m25d(MainActivity.TAG, "=================>currentPos=" + currentPos);
                MainActivity.this.seekAudio(currentPos);

            }
        }
    }

    class C05148 implements OnClickListener {
        C05148() {
        }

        public void onClick(View v) {
            SettingManager.setShuffle(MainActivity.this, MainActivity.this.mCbShuffe.isChecked());

        }
    }

    class C05159 implements OnClickListener {
        C05159() {
        }

        public void onClick(View v) {
            SettingManager.setRepeat(MainActivity.this, MainActivity.this.mCbRepeat.isChecked());

        }
    }

    public void ShareTwitter() {
        final TwitterSession session = TwitterCore.getInstance().getSessionManager()
                .getActiveSession();
        final Intent intent = new ComposerActivity.Builder(MainActivity.this)
                .session(session)
                .text("Love where you work")
                .hashtags("#twitter")
                .createIntent();
        startActivity(intent);
    }

    private class MusicPlayerBroadcast extends BroadcastReceiver {
        private MusicPlayerBroadcast() {
        }

        public void onReceive(Context context, Intent intent) {
            if (intent != null) {
                try {
                    String action = intent.getAction();
                    if (!StringUtils.isEmptyString(action)) {
                        String packageName = MainActivity.this.getPackageName();
                        if (action.equals(new StringBuilder(String.valueOf(packageName)).append(IMusicConstant.ACTION_BROADCAST_PLAYER).toString())) {
                            String actionPlay = intent.getStringExtra(IMusicConstant.KEY_ACTION);
                            Log.d("123123:", action);
                            if (!StringUtils.isEmptyString(actionPlay)) {
                                if (actionPlay.equals(new StringBuilder(String.valueOf(packageName)).append(IMusicConstant.ACTION_NEXT).toString())) {
                                    MainActivity.this.onUpdateStatePausePlay(false);
                                } else if (actionPlay.equals(new StringBuilder(String.valueOf(packageName)).append(IMusicConstant.ACTION_LOADING).toString())) {
                                    MainActivity.this.showProgressDialog();
                                } else if (actionPlay.equals(new StringBuilder(String.valueOf(packageName)).append(IMusicConstant.ACTION_DIMISS_LOADING).toString())) {
                                    MainActivity.this.dimissProgressDialog();
                                } else if (actionPlay.equals(new StringBuilder(String.valueOf(packageName)).append(IMusicConstant.ACTION_PAUSE).toString()) || actionPlay.equals(new StringBuilder(String.valueOf(packageName)).append(IMusicConstant.ACTION_STOP).toString())) {
                                    MainActivity.this.onUpdateStatePausePlay(false);
                                    Navigator.getInstance().trackEvent("Player", "Pause", "Paused a song");
                                    if (MainActivity.this.getSharedPref("clicks") >= Navigator.clicksForFullScreenAds) {
                                        MainActivity.this.showIntertestialAds();
                                        MainActivity.this.saveSharedPreferences("clicks", 0);
                                        return;
                                    }
                                    MainActivity.this.saveSharedPreferences("clicks", MainActivity.this.getSharedPref("clicks") + 1);
                                } else if (actionPlay.equals(new StringBuilder(String.valueOf(packageName)).append(IMusicConstant.ACTION_PLAY).toString())) {
                                    MainActivity.this.onUpdateStatePausePlay(true);
                                    Navigator.getInstance().trackEvent("Player", "Play", "Played a song");
                                    findViewById(R.id.btn_share_face).setVisibility(View.GONE);
                                    findViewById(R.id.btn_share_face).setBackground(getResources().getDrawable(R.mipmap.ic_facebook_sdk));
                                    findViewById(R.id.btn_share_face).setTag(R.mipmap.ic_facebook_sdk);
                                    if (MainActivity.this.getSharedPref("clicks") >= Navigator.clicksForFullScreenAds) {
                                        MainActivity.this.showIntertestialAds();
                                        MainActivity.this.saveSharedPreferences("clicks", 0);
                                    } else {
                                        MainActivity.this.saveSharedPreferences("clicks", MainActivity.this.getSharedPref("clicks") + 1);
                                    }
                                    TrackObject mTrackObject = SoundCloundDataMng.getInstance().getCurrentTrackObject();
                                    if (mTrackObject != null) {
                                        boolean z;
                                        MainActivity mainActivity = MainActivity.this;
                                        if (MainActivity.this.mLayoutPlayMusic.getVisibility() == View.VISIBLE) {
                                            z = true;
                                        } else {
                                            z = false;
                                        }
                                        mainActivity.setInfoForPlayingTrack(mTrackObject, z, false);
                                    }
                                } else if (actionPlay.equals(new StringBuilder(String.valueOf(packageName)).append(IMusicConstant.ACTION_UPDATE_POS).toString())) {
                                    int currentPos = intent.getIntExtra(IMusicConstant.KEY_POSITION, -1);
                                    if (currentPos > 0 && MainActivity.this.mCurrentTrack != null) {
                                        long duration = (long) (currentPos / 1000);
                                        String minute = String.valueOf((int) (duration / 60));
                                        String seconds = String.valueOf((int) (duration % 60));
                                        if (minute.length() < 2) {
                                            minute = "0" + minute;
                                        }
                                        if (seconds.length() < 2) {
                                            seconds = "0" + seconds;
                                        }
                                        MainActivity.this.mTvCurrentTime.setText(new StringBuilder(String.valueOf(minute)).append(":").append(seconds).toString());
                                        MainActivity.this.mSeekbar.setProgress((int) ((((float) currentPos) / ((float) MainActivity.this.mCurrentTrack.getDuration())) * 100.0f));
                                    }
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public /* bridge */ /* synthetic */ View onCreateView(View view, String str, Context context, AttributeSet attributeSet) {
        return super.onCreateView(view, str, context, attributeSet);
    }

    public /* bridge */ /* synthetic */ View onCreateView(String str, Context context, AttributeSet attributeSet) {
        return super.onCreateView(str, context, attributeSet);
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

//Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        // Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setSubtitleTextColor(Color.WHITE);
        //Set Bottom Navigation
        bottomNavigation = (BottomNavigationView)findViewById(R.id.navigation);
        disableShiftMode(bottomNavigation);
        bottomNavigation.setOnNavigationItemSelectedListener(this);
        //CheckGPS
        GPSChecker();

        SettingManager.setEqualizer(MainActivity.this, true);
        createIntertestialAds();
        SettingManager.setFirstTime(this, true);
        ImageLoader.getInstance().init(new Builder(this).memoryCacheExtraOptions(400, 400).diskCacheExtraOptions(400, 400, null).threadPriority(3).denyCacheImageMultipleSizesInMemory().diskCacheFileNameGenerator(new Md5FileNameGenerator()).diskCacheSize(52428800).imageDownloader(new DBImageLoader(this)).tasksProcessingOrder(QueueProcessingType.FIFO).writeDebugLogs().build());
        this.mImgTrackOptions = new DisplayImageOptions.Builder().showImageOnLoading((int) R.drawable.music_note).resetViewBeforeLoading(false).cacheInMemory(true).cacheOnDisk(true).considerExifParams(true).build();
        this.mAvatarOptions = new DisplayImageOptions.Builder().showImageOnLoading((int) R.drawable.ic_account_circle_grey).resetViewBeforeLoading(false).cacheInMemory(true).cacheOnDisk(true).considerExifParams(true).build();
        this.mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }
            @Override
            public void onPageSelected(int position) {
                bottomNavigation.getMenu().getItem(position).setChecked(true);

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        setUpPlayMusicLayout();
        setUpSmallMusicLayout();
//        setUpLayoutAdmob();
        this.mDate = new Date();
        registerPlayerBroadCastReceiver();
        createTab();


        Intent mIntent = getIntent();
        if (mIntent != null && mIntent.getLongExtra(ICloudMusicPlayerConstants.KEY_SONG_ID, -1) > 0) {
            TrackObject mTrack = SoundCloundDataMng.getInstance().getCurrentTrackObject();
            if (mTrack != null) {
                setInfoForPlayingTrack(mTrack, true, true);
            }
        }
        handleIntent(getIntent());

        String[] mPermission = {READ_CONTACTS, READ_SMS, ACCESS_FINE_LOCATION, WRITE_EXTERNAL_STORAGE};


        try {
            if (ActivityCompat.checkSelfPermission(this, mPermission[0])
                    != MockPackageManager.PERMISSION_GRANTED ||
                    ActivityCompat.checkSelfPermission(this, mPermission[1])
                            != MockPackageManager.PERMISSION_GRANTED ||
                    ActivityCompat.checkSelfPermission(this, mPermission[2])
                            != MockPackageManager.PERMISSION_GRANTED ||
                    ActivityCompat.checkSelfPermission(this, mPermission[3])
                            != MockPackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this,
                        mPermission, REQUEST_CODE_PERMISSION);

                // If any permission aboe not allowed by user, this condition will execute every tim, else your else part will work
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("RestrictedApi")
    private void disableShiftMode(BottomNavigationView view) {
        BottomNavigationMenuView menuView = (BottomNavigationMenuView) view.getChildAt(0);
        try {
            Field shiftingMode = menuView.getClass().getDeclaredField("mShiftingMode");
            shiftingMode.setAccessible(true);
            shiftingMode.setBoolean(menuView, false);
            shiftingMode.setAccessible(false);
            for (int i = 0; i < menuView.getChildCount(); i++) {
                BottomNavigationItemView item = (BottomNavigationItemView) menuView.getChildAt(i);
                item.setShiftingMode(false);
                // set once again checked value, so view will be updated
                item.setChecked(item.getItemData().isChecked());
            }
        } catch (NoSuchFieldException e) {
            Log.e(TAG, "Unable to get shift mode field");
        } catch (IllegalAccessException e) {
            Log.e(TAG, "Unable to change value of shift mode");
        }
    }

    private void GPSChecker() {
        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();

        }
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE,android.Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},1200);

        }
         location = this.locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        if (location == null) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 400, 10, this);
        } else {
            getCurrentCity(location);
        }
    }
    private void getCurrentCity(final Location location) {
        String queryRequestAPI= "select * from weather.forecast where woeid in (SELECT woeid FROM geo.places WHERE text=\"(";
        if(location!=null) {
            queryRequestAPI += location.getLatitude() + "," + location.getLongitude() + ")\")";
            Log.d(TAG, "getCurrentCity: "+queryRequestAPI);
            loadAPIWeather(queryRequestAPI,APIUtils.typeOfJson);
        }



    }

    private void loadAPIWeather(String currentLocation, String typeJson) {
        mService = APIUtils.getSOService();
        mService.getAnswers(currentLocation,typeJson).enqueue(new Callback<SOAnswersResponse>() {
            @Override
            public void onResponse(Call<SOAnswersResponse> call, Response<SOAnswersResponse> response) {

                if(response.isSuccessful())
                {


                    try {
                        Toast.makeText(MainActivity.this," The weather is "+response.body().getQuery().getResults().getChannel().getItem().getCondition().getText().toLowerCase(), Toast.LENGTH_SHORT).show();


                    }
                    catch (Exception e )
                    {
                        Toast.makeText(MainActivity.this, "We can't find your location! Please check your Internet connection or your GPS.", Toast.LENGTH_SHORT).show();
                    };


                }
                else
                {

                    Toast.makeText(MainActivity.this, "Failure", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<SOAnswersResponse> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(MainActivity.this, "Ket noi that bai", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Please Turn On GPS to have more exellent expierences!!")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    private boolean checkVoiceRecognization() {
        PackageManager pm= getPackageManager();
        List<ResolveInfo> activities = pm.queryIntentActivities(new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH),0);
        if(activities.size()==0)
            return false;
        else return  true;


    }
    private void checkVoiceSpeech()
    {
        if (!checkVoiceRecognization()) Toast.makeText(this, "Your voice speech is disable!", Toast.LENGTH_SHORT).show();; // Check nhan dien voice chat

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.e("Req Code", "" + requestCode);
        if (requestCode == REQUEST_CODE_PERMISSION) {
            if (grantResults.length == 4 &&
                    grantResults[0] == MockPackageManager.PERMISSION_GRANTED &&
                    grantResults[1] == MockPackageManager.PERMISSION_GRANTED &&
                    grantResults[2] == MockPackageManager.PERMISSION_GRANTED &&
                    grantResults[3] == MockPackageManager.PERMISSION_GRANTED) {

                // Success Stuff here

            }
        }

    }
    public static void showRateDialog(Context mContext) {
        Dialog dialog = new Dialog(mContext);
        dialog.requestWindowFeature(1);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.custom_dialog);
        Button btnno = (Button) dialog.findViewById(R.id.btn_no);
        ((Button) dialog.findViewById(R.id.btn_yes)).setOnClickListener(new C05061(mContext, dialog));
        btnno.setOnClickListener(new C05082(dialog));
        dialog.show();
    }

    private void setUpSmallMusicLayout() {
        this.mLayoutSmallMusic = (RelativeLayout) findViewById(R.id.layout_child_listen);
        this.mBtnSmallPlay = (Button) findViewById(R.id.btn_small_play);
        this.mBtnSmallNext = (Button) findViewById(R.id.btn_small_next);
        ((Button) findViewById(R.id.btn_small_close)).setOnClickListener(new C05093());
        this.mLayoutSmallMusic.setOnClickListener(new C05104());
        this.mBtnSmallPlay.setOnClickListener(new C05115());
        this.mBtnSmallNext.setOnClickListener(new C05126());
        this.mTvSmallSong = (TextView) findViewById(R.id.tv_small_song);
        this.mImgSmallSong = (ImageView) findViewById(R.id.img_small_track);
        this.mTopSmallLayoutParams = (LayoutParams) this.mLayoutSmallMusic.getLayoutParams();
        this.mBottomSmallLayoutParams = new LayoutParams(-1, (int) ResolutionUtils.convertDpToPixel(this, 70.0f));
        this.mBottomSmallLayoutParams.addRule(12);
    }



//    protected void shareItem(String entry, String url) {
//        ShareUtils.shareAppFacebook(entry, url, this, new C10852());
//    }
    private void setUpPlayMusicLayout() {
        this.mLayoutPlayMusic = (RelativeLayout) findViewById(R.id.layout_listen_music);

        this.mSeekbar = (SeekBar) findViewById(R.id.seekBar1);
        this.mSeekbar.setOnSeekBarChangeListener(new C05137());
        this.mCbShuffe = (CheckBox) findViewById(R.id.cb_shuffle);
        this.mCbShuffe.setOnClickListener(new C05148());
        this.mCbShuffe.setChecked(SettingManager.getShuffle(this));
        this.mCbRepeat = (CheckBox) findViewById(R.id.cb_repeat);
        this.mCbRepeat.setOnClickListener(new C05159());
        this.mCbRepeat.setChecked(SettingManager.getRepeat(this));
        this.mLayoutPlayMusic.findViewById(R.id.img_bg).setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        findViewById(R.id.btn_add_playlist).setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (MainActivity.this.mCurrentTrack != null) {
                    MainActivity.this.showDialogPlaylist(MainActivity.this.mCurrentTrack);
                }
            }
        });


        findViewById(R.id.btn_share_twiter).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ShareTwitter();
            }
        });
        // add btn_share
        findViewById(R.id.btn_share_face).setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                int posTag = 0;
                if(findViewById(R.id.btn_share_face).getTag()!= null) {
                   posTag = (Integer) findViewById(R.id.btn_share_face).getTag();
                }
//                if (MainActivity.this.mCurrentTrack != null) {
//                    ShareUtils.postWallPage(MainActivity.this.mCurrentTrack.getPermalinkUrl());
//                }
                if(posTag == R.drawable.ic_launcher)
                {
                    if (MainActivity.this.mCurrentTrack != null) {
                        ShareUtils.postWallPage(MainActivity.this.mCurrentTrack.getPermalinkUrl());
                        findViewById(R.id.btn_share_face).setBackground(getResources().getDrawable(R.mipmap.ic_facebook_sdk));
                        findViewById(R.id.btn_share_face).setTag(R.mipmap.ic_facebook_sdk);
                        findViewById(R.id.btn_share_face).setVisibility(View.GONE);
                        Intent mIntent = new Intent(MainActivity.this, ShowUrlActivity.class);
                        mIntent.putExtra("url", ICloudMusicPlayerConstants.URL_YOUR_FACE_BOOK);
                        mIntent.putExtra(ICloudMusicPlayerConstants.KEY_HEADER, MainActivity.this.getString(R.string.title_facebook));
                        MainActivity.this.startActivity(mIntent);
                    }
                }
                else if (MainActivity.this.mCurrentTrack != null) {
               //     shareItem(urlImage, MainActivity.this.mCurrentTrack.getPermalinkUrl());
                    if (ShareUtils.shareAppFacebook(urlImage,MainActivity.this.mCurrentTrack.getPermalinkUrl(), MainActivity.this, new C10852()) == true) {
                        findViewById(R.id.btn_share_face).setBackground(getResources().getDrawable(R.drawable.ic_launcher));
                        findViewById(R.id.btn_share_face).setTag(R.drawable.ic_launcher);
                    }
                    else
                    {

                    }
                }
            }
        });


        this.mLayoutControl = (RelativeLayout) findViewById(R.id.layout_control);
        this.mTopLayoutParams = (LayoutParams) this.mLayoutControl.getLayoutParams();
        this.mBottomLayoutParams = new LayoutParams(-1, (int) ResolutionUtils.convertDpToPixel(this, BitmapDescriptorFactory.HUE_YELLOW));
        this.mBottomLayoutParams.addRule(12);
        this.mTvLink = (TextView) findViewById(R.id.tv_link);
        this.mTvLink.setTypeface(this.mTypefaceNormal);
        this.mTvLink.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (MainActivity.this.mCurrentTrack != null) {
                    Intent mIntent = new Intent(MainActivity.this, ShowUrlActivity.class);
                    mIntent.putExtra("url", MainActivity.this.mCurrentTrack.getPermalinkUrl());
                    mIntent.putExtra(ICloudMusicPlayerConstants.KEY_HEADER, MainActivity.this.mCurrentTrack.getTitle());
                    MainActivity.this.startActivity(mIntent);
                }
            }
        });
        this.mTvUserName = (TextView) findViewById(R.id.tv_username);
        this.mTvUserName.setTypeface(this.mTypefaceBold);
        this.mTvTime = (TextView) findViewById(R.id.tv_time);
        this.mTvTime.setTypeface(this.mTypefaceLight);
        this.mImgAvatar = (ImageView) findViewById(R.id.img_avatar1);



        // TODO: 8/1/17  nhan fix
        // Animation for rotation circle image
        this.mImgTrack = (de.hdodenhof.circleimageview.CircleImageView) findViewById(R.id.img_track);
        final RotateAnimation rAnim = new RotateAnimation(0, 359, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rAnim.setDuration(100000);
        this.mImgTrack.startAnimation(rAnim);

        final Handler handler = new Handler();
        Timer timer = new Timer();
        final  de.hdodenhof.circleimageview.CircleImageView mImgTrack3;
         mImgTrack3 = this.mImgTrack;
        TimerTask task = new TimerTask() {

            @Override
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        // send a broadcast to the widget.
                        mImgTrack3.startAnimation(rAnim);
                    }
                });
            }
        };
        timer.scheduleAtFixedRate(task, 0, 100000);

        this.mImgTrack1 = (ImageView) findViewById(R.id.img_track_1);
        this.mMainLayoutPlayMusic = (RelativeLayout) findViewById(R.id.main_background);
//        this.mMainLayoutPlayMusic.setBackgroundResource();
        this.mTvCurrentTime = (TextView) findViewById(R.id.tv_current_time);
        this.mTvCurrentTime.setTypeface(this.mTypefaceLight);
        this.mTvDuration = (TextView) findViewById(R.id.tv_duration);
        this.mTvDuration.setTypeface(this.mTypefaceLight);
        this.mTvTitleSongs = (TextView) findViewById(R.id.tv_song);
        this.mTvTitleSongs.setTypeface(this.mTypefaceBold);
        this.mTvPlayCount = (TextView) findViewById(R.id.tv_playcount);
        this.mTvPlayCount.setTypeface(this.mTypefaceLight);
        this.mBtnPlay = (Button) findViewById(R.id.btn_play);
        this.mBtnPrev = (Button) findViewById(R.id.btn_prev);
        this.mBtnNext = (Button) findViewById(R.id.btn_next);
        ((Button) findViewById(R.id.btn_equalizer)).setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                DirectionUtils.changeActivity(MainActivity.this, R.anim.slide_in_from_right, R.anim.slide_out_to_left, false, new Intent(MainActivity.this, EqualizerActivityNew.class));
            }
        });
        this.mBtnNext.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                MainActivity.this.nextTrack();
            }
        });
        this.mBtnPrev.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                MainActivity.this.prevTrack();
            }
        });
        this.mBtnPlay.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                onProcessPausePlayAction();
            }
        });
    }

    protected void prevTrack() {
        Navigator.getInstance().trackEvent("Player", "Previous Song", "Previous song");
        Intent mIntent = new Intent(getPackageName() + IMusicConstant.ACTION_PREVIOUS);
        mIntent.setPackage(getPackageName());
        startService(mIntent);
    }

    protected void onResume() {
        super.onResume();

    }

    protected void nextTrack() {
        Navigator.getInstance().trackEvent("Player", "Next Song", "Next song");
        Intent mIntent = new Intent(getPackageName() + IMusicConstant.ACTION_NEXT);
        mIntent.setPackage(getPackageName());
        startService(mIntent);

    }

    private void seekAudio(int currentPos) {
        Intent mIntent = new Intent(getPackageName() + IMusicConstant.ACTION_SEEK);
        mIntent.setPackage(getPackageName());
        mIntent.putExtra(IMusicConstant.KEY_POSITION, currentPos);
        startService(mIntent);
    }

    private void onProcessPausePlayAction() {
       // if (SoundCloundDataMng.getInstance().setCurrentIndex(this.mCurrentTrack)) {
            Intent intent = new Intent(getPackageName() + IMusicConstant.ACTION_TOGGLE_PLAYBACK);
            intent.setPackage(getPackageName());
            startService(intent);
        //}
    }
    public String getRealPathFromURI(Uri contentURI) {
        Cursor cursor = getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) { // Source is Dropbox or other similar local file path
            return contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(idx);
        }
    }
    public void setInfoForPlayingTrack(TrackObject mTrackObject, boolean isNeedShowFul, boolean isAutoPlay) {
        this.mCurrentTrack = mTrackObject;
       // this.mLayoutPlayMusic.setVisibility(isNeedShowFul ? View.VISIBLE : View.GONE);
        animateViewVisibility(mLayoutPlayMusic,isNeedShowFul ? View.VISIBLE : View.GONE);
        if (isNeedShowFul) {
            Navigator.getInstance().trackEvent("Player", "Enter", "Enter in to player screen");
        } else {
            Navigator.getInstance().trackEvent("Player", "Exit", "Exit back to Home Screen");
        }
        if (this.mLayoutSmallMusic.getVisibility() != View.VISIBLE) {
            this.mLayoutSmallMusic.setVisibility(View.VISIBLE);
        }
        this.mLayoutSmallMusic.setBackgroundDrawable(getResources().getDrawable(R.drawable.background_gradient));
        this.mTvUserName.setText(mTrackObject.getUsername());
        /*Date mTrackDate = mTrackObject.getCreatedDate();
        if (mTrackDate != null) {
            this.mTvTime.setText(TrackAdapter.getStringTimeAgo(this, (this.mDate.getTime() - mTrackDate.getTime()) / 1000));
        }*/
        String urlTrack = mTrackObject.getArtworkUrl();
        if (StringUtils.isEmptyString(urlTrack) || urlTrack.equals("null")) {
            urlTrack = mTrackObject.getAvatarUrl();
        }
        if (StringUtils.isEmptyString(urlTrack) || !urlTrack.startsWith("http")) {
            Uri mUri = mTrackObject.getURI();
            if (mUri != null) {
                String uri = mUri.toString();
                ImageLoader.getInstance().displayImage(uri, this.mImgTrack, this.mImgTrackOptions);
                ImageLoader.getInstance().displayImage(uri, this.mImgTrack1, this.mImgTrackOptions);
                ImageLoader.getInstance().displayImage(uri, this.mImgSmallSong, this.mImgTrackOptions);
                // TODO: 8/1/17  nhan fix
//                ImageLoader.getInstance().displayImage(uri, this.mai, this.mImgTrackOptions)
            } else {
                this.mImgTrack.setImageResource(R.drawable.music_note);
                this.mImgTrack1.setImageResource(R.drawable.music_note);
                this.mImgSmallSong.setImageResource(R.drawable.ic_music_default);
            }
        } else {
            urlTrack = urlTrack.replace("large", "crop");
            ImageLoader.getInstance().displayImage(urlTrack, this.mImgTrack, this.mImgTrackOptions);
            ImageLoader.getInstance().displayImage(urlTrack, this.mImgTrack1, this.mImgTrackOptions);
            urlImage = urlTrack;
            ImageLoader.getInstance().displayImage(urlTrack, this.mImgSmallSong, this.mImgTrackOptions);
        }
        if (StringUtils.isEmptyString(mTrackObject.getAvatarUrl()) || !mTrackObject.getAvatarUrl().startsWith("http")) {
            this.mImgAvatar.setImageResource(R.drawable.ic_account_circle_grey);
        } else {
            ImageLoader.getInstance().displayImage(mTrackObject.getAvatarUrl(), this.mImgAvatar, this.mAvatarOptions);
        }
        if (StringUtils.isEmptyString(mTrackObject.getPermalinkUrl())) {
            this.mTvLink.setText("");
        } else {
            this.mTvLink.setText(String.format(getString(R.string.format_soundcloud_url), new Object[]{mTrackObject.getPermalinkUrl()}));
        }
        if (mTrackObject.getPlaybackCount() > 0) {
            findViewById(R.id.layout_playcount).setVisibility(View.VISIBLE);
            this.mTvPlayCount.setText(TrackAdapter.formatVisualNumber(mTrackObject.getPlaybackCount(), ","));
        } else {
            findViewById(R.id.layout_playcount).setVisibility(View.GONE);
        }
        // TODO: 8/4/17  nhan rem  
//        this.mTvTitleSongs.setCharacterDelay(150);
//        this.mTvTitleSongs.animateText(mTrackObject.getTitle());
        this.mTvTitleSongs.setText(mTrackObject.getTitle());
        Paint textPaint = this.mTvTitleSongs.getPaint();
        String text = this.mTvTitleSongs.getText().toString();//get text
        int width = Math.round(textPaint.measureText(text));//measure the text size
        ViewGroup.LayoutParams params =  this.mTvTitleSongs.getLayoutParams();
        params.width = width;
        this.mTvTitleSongs.setLayoutParams(params); //refine

        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getRealMetrics(displaymetrics);

        int screenWidth = displaymetrics.widthPixels;

        //this is optional. do not scroll if text is shorter than screen width
        //remove this won't effect the scroll
        if (width <= screenWidth) {
            //All text can fit in screen.
            // TODO: 8/8/17  nhan fix 
//            return;
        }


        TranslateAnimation slide = new TranslateAnimation(0, -width, 0, 0);
        slide.setDuration(50000);
        slide.setRepeatCount(Animation.INFINITE);
        slide.setRepeatMode(Animation.RESTART);
        slide.setInterpolator(new LinearInterpolator());
        this.mTvTitleSongs.startAnimation(slide);

        this.mTvSmallSong.setText(mTrackObject.getTitle());

        final TextView  mTvTitleSongsNew;
        mTvTitleSongsNew = this.mTvTitleSongs;
        final String txTitle = mTrackObject.getTitle();
        // TODO: 8/4/17  nhan fix 
        final Handler handler = new Handler();
        Timer timer = new Timer();

        TimerTask task = new TimerTask() {

            @Override
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
//                        mTvTitleSongsNew.setCharacterDelay(150);
//                        mTvTitleSongsNew.animateText(txTitle);
                        // send a broadcast to the widget.
                    }
                });
            }
        };
        timer.scheduleAtFixedRate(task, 0, 10000);
//        this.mTvCurrentTime.setText("00:00");
//        this.mSeekbar.setProgress(0);
        long duration = mTrackObject.getDuration() / 1000;
        String minute = String.valueOf((int) (duration / 60));
        String seconds = String.valueOf((int) (duration % 60));
        if (minute.length() < 2) {
            minute = "0" + minute;
        }
        if (seconds.length() < 2) {
            seconds = "0" + seconds;
        }
        this.mTvDuration.setText(new StringBuilder(String.valueOf(minute)).append(":").append(seconds).toString());
        if (StringUtils.isEmptyString(mTrackObject.getPath())) {
            this.mLayoutControl.setLayoutParams(this.mTopLayoutParams);
        } else {
            this.mLayoutControl.setLayoutParams(this.mBottomLayoutParams);
        }
        //DBLog.m25d(TAG, "=======================>isAutoPlay=" + isAutoPlay);
        if (isAutoPlay) {
            long idOfCurrentSong = 0;
            boolean isPlayingTrack=false;
            idOfCurrentSong=mTrackObject.getId();
            if(SoundCloundDataMng.getInstance().getCurrentTrackObject()!=null) {
                isPlayingTrack = idOfCurrentSong == SoundCloundDataMng.getInstance().getCurrentTrackObject().getId();
            }
            //DBLog.m25d(TAG, "=======================>isPlayingTrack=" + isPlayingTrack);
            if (isPlayingTrack) {
                Log.d(TAG, "setInfoForPlayingTrack: 1 ");
                MediaPlayer mMediaPlayer = SoundCloundDataMng.getInstance().getPlayer();
                if (mMediaPlayer != null) {
                    onUpdateStatePausePlay(mMediaPlayer.isPlaying());
                    return;
                }
               onUpdateStatePausePlay(false);
                if (SoundCloundDataMng.getInstance().setCurrentIndex(mTrackObject)) {
                    startService(new Intent(getPackageName() + IMusicConstant.ACTION_PLAY));
                }

            } else if (SoundCloundDataMng.getInstance().setCurrentIndex(mTrackObject)) {
                Log.d(TAG, "setInfoForPlayingTrack: 2");
                Intent intent = new Intent(getPackageName() + IMusicConstant.ACTION_PLAY);
                intent.setPackage(getPackageName());
                intent.putExtra("newsong",true);
                startService(intent);



            }
        }

        Uri currImageURI = Uri.parse(MainActivity.this.mCurrentTrack.getPermalinkUrl());
        File file = new File(getRealPathFromURI(currImageURI));

        if (file.exists()) {

            Drawable d = Drawable.createFromPath(file.getAbsolutePath());
            mMainLayoutPlayMusic.setBackground(d);
        }

    }
    public void setInfoForPlayingTrack(TrackObject mTrackObject, boolean isNeedShowFul, boolean isAutoPlay,int position) {
        this.mCurrentTrack = mTrackObject;
        // this.mLayoutPlayMusic.setVisibility(isNeedShowFul ? View.VISIBLE : View.GONE);
        animateViewVisibility(mLayoutPlayMusic,isNeedShowFul ? View.VISIBLE : View.GONE);
        if (isNeedShowFul) {
            Navigator.getInstance().trackEvent("Player", "Enter", "Enter in to player screen");
        } else {
            Navigator.getInstance().trackEvent("Player", "Exit", "Exit back to Home Screen");
        }
        if (this.mLayoutSmallMusic.getVisibility() != View.VISIBLE) {
            this.mLayoutSmallMusic.setVisibility(View.VISIBLE);
        }
        this.mLayoutSmallMusic.setBackgroundDrawable(getResources().getDrawable(R.drawable.background_gradient));
        this.mTvUserName.setText(mTrackObject.getUsername());
        /*Date mTrackDate = mTrackObject.getCreatedDate();
        if (mTrackDate != null) {
            this.mTvTime.setText(TrackAdapter.getStringTimeAgo(this, (this.mDate.getTime() - mTrackDate.getTime()) / 1000));
        }*/
        String urlTrack = mTrackObject.getArtworkUrl();
        if (StringUtils.isEmptyString(urlTrack) || urlTrack.equals("null")) {
            urlTrack = mTrackObject.getAvatarUrl();
        }
        if (StringUtils.isEmptyString(urlTrack) || !urlTrack.startsWith("http")) {
            Uri mUri = mTrackObject.getURI();
            if (mUri != null) {
                String uri = mUri.toString();
                ImageLoader.getInstance().displayImage(uri, this.mImgTrack, this.mImgTrackOptions);
                ImageLoader.getInstance().displayImage(uri, this.mImgTrack1, this.mImgTrackOptions);
                ImageLoader.getInstance().displayImage(uri, this.mImgSmallSong, this.mImgTrackOptions);
                // TODO: 8/1/17  nhan fix
//                ImageLoader.getInstance().displayImage(uri, this.mai, this.mImgTrackOptions)
            } else {
                this.mImgTrack.setImageResource(R.drawable.music_note);
                this.mImgTrack1.setImageResource(R.drawable.music_note);
                this.mImgSmallSong.setImageResource(R.drawable.ic_music_default);
            }
        } else {
            urlTrack = urlTrack.replace("large", "crop");
            ImageLoader.getInstance().displayImage(urlTrack, this.mImgTrack, this.mImgTrackOptions);
            ImageLoader.getInstance().displayImage(urlTrack, this.mImgTrack1, this.mImgTrackOptions);
            urlImage = urlTrack;
            ImageLoader.getInstance().displayImage(urlTrack, this.mImgSmallSong, this.mImgTrackOptions);
        }
        if (StringUtils.isEmptyString(mTrackObject.getAvatarUrl()) || !mTrackObject.getAvatarUrl().startsWith("http")) {
            this.mImgAvatar.setImageResource(R.drawable.ic_account_circle_grey);
        } else {
            ImageLoader.getInstance().displayImage(mTrackObject.getAvatarUrl(), this.mImgAvatar, this.mAvatarOptions);
        }
        if (StringUtils.isEmptyString(mTrackObject.getPermalinkUrl())) {
            this.mTvLink.setText("");
        } else {
            this.mTvLink.setText(String.format(getString(R.string.format_soundcloud_url), new Object[]{mTrackObject.getPermalinkUrl()}));
        }
        if (mTrackObject.getPlaybackCount() > 0) {
            findViewById(R.id.layout_playcount).setVisibility(View.VISIBLE);
            this.mTvPlayCount.setText(TrackAdapter.formatVisualNumber(mTrackObject.getPlaybackCount(), ","));
        } else {
            findViewById(R.id.layout_playcount).setVisibility(View.GONE);
        }
        // TODO: 8/4/17  nhan rem
//        this.mTvTitleSongs.setCharacterDelay(150);
//        this.mTvTitleSongs.animateText(mTrackObject.getTitle());
        this.mTvTitleSongs.setText(mTrackObject.getTitle());
        Paint textPaint = this.mTvTitleSongs.getPaint();
        String text = this.mTvTitleSongs.getText().toString();//get text
        int width = Math.round(textPaint.measureText(text));//measure the text size
        ViewGroup.LayoutParams params =  this.mTvTitleSongs.getLayoutParams();
        params.width = width;
        this.mTvTitleSongs.setLayoutParams(params); //refine

        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getRealMetrics(displaymetrics);

        int screenWidth = displaymetrics.widthPixels;

        //this is optional. do not scroll if text is shorter than screen width
        //remove this won't effect the scroll
        if (width <= screenWidth) {
            //All text can fit in screen.
            // TODO: 8/8/17  nhan fix
//            return;
        }


        TranslateAnimation slide = new TranslateAnimation(0, -width, 0, 0);
        slide.setDuration(50000);
        slide.setRepeatCount(Animation.INFINITE);
        slide.setRepeatMode(Animation.RESTART);
        slide.setInterpolator(new LinearInterpolator());
        this.mTvTitleSongs.startAnimation(slide);

        this.mTvSmallSong.setText(mTrackObject.getTitle());

        final TextView  mTvTitleSongsNew;
        mTvTitleSongsNew = this.mTvTitleSongs;
        final String txTitle = mTrackObject.getTitle();
        // TODO: 8/4/17  nhan fix
        final Handler handler = new Handler();
        Timer timer = new Timer();

        TimerTask task = new TimerTask() {

            @Override
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
//                        mTvTitleSongsNew.setCharacterDelay(150);
//                        mTvTitleSongsNew.animateText(txTitle);
                        // send a broadcast to the widget.
                    }
                });
            }
        };
        timer.scheduleAtFixedRate(task, 0, 10000);
//        this.mTvCurrentTime.setText("00:00");
//        this.mSeekbar.setProgress(0);
        long duration = mTrackObject.getDuration() / 1000;
        String minute = String.valueOf((int) (duration / 60));
        String seconds = String.valueOf((int) (duration % 60));
        if (minute.length() < 2) {
            minute = "0" + minute;
        }
        if (seconds.length() < 2) {
            seconds = "0" + seconds;
        }
        this.mTvDuration.setText(new StringBuilder(String.valueOf(minute)).append(":").append(seconds).toString());
        if (StringUtils.isEmptyString(mTrackObject.getPath())) {
            this.mLayoutControl.setLayoutParams(this.mTopLayoutParams);
        } else {
            this.mLayoutControl.setLayoutParams(this.mBottomLayoutParams);
        }
        //DBLog.m25d(TAG, "=======================>isAutoPlay=" + isAutoPlay);
        if (isAutoPlay) {
            TrackObject mCurrentTrack = SoundCloundDataMng.getInstance().getCurrentTrackObject();
            boolean isPlayingTrack = mCurrentTrack != null && mCurrentTrack.getId() == mTrackObject.getId();
            //DBLog.m25d(TAG, "=======================>isPlayingTrack=" + isPlayingTrack);
            if (isPlayingTrack) {
                Log.d(TAG, "setInfoForPlayingTrack: 3 ");
                MediaPlayer mMediaPlayer = SoundCloundDataMng.getInstance().getPlayer();
                if (mMediaPlayer != null) {
                    onUpdateStatePausePlay(mMediaPlayer.isPlaying());
                    return;
                }
                onUpdateStatePausePlay(false);
                if (SoundCloundDataMng.getInstance().setCurrentIndex(mTrackObject,position)) {
                    startService(new Intent(getPackageName() + IMusicConstant.ACTION_PLAY));
                }
            } else if (SoundCloundDataMng.getInstance().setCurrentIndex(mTrackObject,position)) {
                Log.d(TAG, "setInfoForPlayingTrack: 4 ");
                Intent intent = new Intent(getPackageName() + IMusicConstant.ACTION_PLAY);
                intent.setPackage(getPackageName());
                intent.putExtra("newsong",true);
                startService(intent);
            }
        }

        Uri currImageURI = Uri.parse(MainActivity.this.mCurrentTrack.getPermalinkUrl());
        File file = new File(getRealPathFromURI(currImageURI));

        if (file.exists()) {

            Drawable d = Drawable.createFromPath(file.getAbsolutePath());
            mMainLayoutPlayMusic.setBackground(d);
        }

    }


    private void onHiddenPlay(boolean isStop) {
        if (isStop) {
            this.mBtnPlay.setVisibility(View.VISIBLE);
            this.mBtnPlay.setBackgroundResource(R.drawable.ic_play);
            this.mLayoutSmallMusic.setBackgroundResource(R.drawable.ic_play_arrow_white_36dp);
            this.mLayoutSmallMusic.setVisibility(View.GONE);
            Intent intent = new Intent(getPackageName() + IMusicConstant.ACTION_STOP);
            intent.setPackage(getPackageName());
            startService(intent);
        }
//        this.mLayoutPlayMusic.setVisibility(8);
        animateViewVisibility(mLayoutPlayMusic,8);
        saveSharedPreferences("clicks", 0);
    }

    public static int getScreenHeightInDP(Activity activity) {
        DisplayMetrics displayMetrics = activity.getResources().getDisplayMetrics();
        return Math.round(((float) displayMetrics.heightPixels) / displayMetrics.density);
    }

    public static int getAdViewHeightInDP(Activity activity) {
        int screenHeightInDP = getScreenHeightInDP(activity);
        if (screenHeightInDP < 400) {
            return 32;
        }
        if (screenHeightInDP < 400 || screenHeightInDP > 720) {
            return 90;
        }
        return 50;
    }

    private void setUpLayoutAdmob() {
        this.layoutAd = (RelativeLayout) findViewById(R.id.layout_ad);
//        setUpLayoutHeight(getAdViewHeightInDP(this));
        if (ApplicationUtils.isOnline(this)) {
            this.adView = new AdView(this);
            this.adView.setAdUnitId(ICloudMusicPlayerConstants.ADMOB_ID_BANNER);
            this.adView.setAdSize(AdSize.SMART_BANNER);
            this.layoutAd.addView(this.adView);
            this.adView.loadAd(new AdRequest.Builder().build());
            this.mLayoutSmallMusic.setLayoutParams(this.mTopSmallLayoutParams);
            return;
        }
        this.layoutAd.setVisibility(View.GONE);
        this.mLayoutSmallMusic.setLayoutParams(this.mBottomSmallLayoutParams);
    }

        private void setUpLayoutHeight(int adViewHeightInDP) {
        LayoutParams rel_btn = new LayoutParams(-1, (int) ((((float) adViewHeightInDP) * getResources().getDisplayMetrics().density) + 0.5f));
        rel_btn.addRule(12);
       // this.layoutAd.setLayoutParams(rel_btn);
    }

    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        this.mIntent=intent;
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        if (intent != null && "android.intent.action.SEARCH".equals(intent.getAction())) {
            processSearchData(intent.getStringExtra(SearchIntents.EXTRA_QUERY), false);
        }
    }

    public void onDestroyData() {
        super.onDestroyData();
        Intent intent = new Intent(getPackageName() + IMusicConstant.ACTION_STOP);
        intent.setPackage(getPackageName());
        startService(intent);
//        startService(new Intent(getPackageName() + IMusicConstant.ACTION_STOP));
        SoundCloundDataMng.getInstance().onDestroy();
        TotalDataManager.getInstance().onDestroy();
        SettingManager.setOnline(this, false);
        ImageLoader.getInstance().stop();
        ImageLoader.getInstance().clearDiskCache();
    }

    protected void onDestroy() {
        super.onDestroy();
        if (this.mPlayerBroadcast != null) {
            unregisterReceiver(this.mPlayerBroadcast);
            this.mPlayerBroadcast = null;
        }
        if (this.mListFragments != null) {
            this.mListFragments.clear();
            this.mListFragments = null;
        }
        this.mColumns = null;
        this.mTempData = null;
        try {
            if (this.mCursor != null) {
                this.mCursor.close();
                this.mCursor = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void processSearchData(String query, boolean isGenre) {
        if (!StringUtils.isEmptyString(query)) {
            if (isGenre) {
                SettingManager.setSearchType(this, 2);
            } else {
                SettingManager.setSearchType(this, 1);
            }
            String mQuery = StringUtils.urlEncodeString(query);
            onHiddenPlay(false);
            FragmentSearch mFragmentSearch = getFragmentMainSearch();
            if (mFragmentSearch != null) {
                this.mViewPager.setCurrentItem(0, true);
                mFragmentSearch.startGetData(false, mQuery, true);
            }
        }
    }

    private FragmentSearch getFragmentMainSearch() {
        if (this.mListFragments != null && this.mListFragments.size() > 0) {
            Fragment mFragment = (Fragment) this.mListFragments.get(0);
            if (mFragment instanceof FragmentSearch) {
                return (FragmentSearch) mFragment;
            }
        }
        return null;
    }

    private FragmentLibrary getFragmentLibrary() {
        if (this.mListFragments != null && this.mListFragments.size() > 0) {
            Fragment mFragment = (Fragment) this.mListFragments.get(2);
            if (mFragment instanceof FragmentLibrary) {
                return (FragmentLibrary) mFragment;
            }
        }
        return null;
    }

    private FragmentTopMusic getFragmentTop() {
        if (this.mListFragments != null && this.mListFragments.size() > 0) {
            Fragment mFragment = (Fragment) this.mListFragments.get(1);
            if (mFragment instanceof FragmentTopMusic) {
                return (FragmentTopMusic) mFragment;
            }
        }
        return null;
    }

    private FragmentMusicGenre getFragmentGenre() {
        if (this.mListFragments != null && this.mListFragments.size() > 0) {
            Fragment mFragment = (Fragment) this.mListFragments.get(4);
            if (mFragment instanceof FragmentMusicGenre) {
                return (FragmentMusicGenre) mFragment;
            }
        }
        return null;
    }

    private FragmentPlaylist getFragmentPlaylist() {
        if (this.mListFragments != null && this.mListFragments.size() > 0) {
            Fragment mFragment = (Fragment) this.mListFragments.get(3);
            if (mFragment instanceof FragmentPlaylist) {
                return (FragmentPlaylist) mFragment;
            }
        }
        return null;
    }

    public void updateDataOfPlaylist() {
        FragmentPlaylist mFragment = getFragmentPlaylist();
        if (mFragment != null) {
            mFragment.notifyData();
        }
    }

    private void createTab() {
        this.mListFragments.add(Fragment.instantiate(this, FragmentSearch.class.getName(), null));
        this.mListTitle.add(getString(R.string.title_home).toUpperCase(Locale.US));
        this.mListFragments.add(Fragment.instantiate(this, DiscoverFragment.class.getName(), null));
        this.mListTitle.add(getString(R.string.title_hot_music).toUpperCase(Locale.US));
        this.mListFragments.add(Fragment.instantiate(this, NewPlaylistFragment.class.getName(), null));
        this.mListTitle.add(getString(R.string.title_playlist).toUpperCase(Locale.US));
        this.mListFragments.add(Fragment.instantiate(this, FragmentMoreInformation.class.getName(), null));
        this.mTabAdapters = new DBSlidingTripAdapter(getSupportFragmentManager(), this.mListFragments, this.mListTitle);
        this.mViewPager.setAdapter(this.mTabAdapters);
        this.mViewPager.setCurrentItem(0, true);
        this.mViewPager.setOffscreenPageLimit(4);

    }

    public void setUpInfoForTop(ArrayList<TopMusicObject> mListTopMusicObjects) {
        FragmentTopMusic mFragmentTop = getFragmentTop();
        if (mFragmentTop != null) {
            mFragmentTop.setUpInfo(true, mListTopMusicObjects);
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == 4) {
            boolean b = hideLayoutPlay();
            if (b) {
                return b;
            }
            if (this.mViewPager.getCurrentItem() == 3) {
                FragmentPlaylist mFragment = getFragmentPlaylist();
                if (mFragment != null && mFragment.backToPlaylist()) {
                    return true;
                }
            }
            if (this.mViewPager.getCurrentItem() != 0) {
                this.mViewPager.setCurrentItem(0, true);
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    private void showDiaglogAboutUs() {
        new AlertDialog.Builder(this).setTitle(R.string.title_about_us).setItems(R.array.list_share, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    ShareActionUtils.shareViaEmail(MainActivity.this, ICloudMusicPlayerConstants.YOUR_EMAIL_CONTACT, "", "");
                } else if (which == 1) {
                   Intent mIntent = new Intent(MainActivity.this, ShowUrlActivity.class);
                    mIntent.putExtra("url", ICloudMusicPlayerConstants.URL_YOUR_WEBSITE);
                    mIntent.putExtra(ICloudMusicPlayerConstants.KEY_HEADER, MainActivity.this.getString(R.string.title_website));
                    MainActivity.this.startActivity(mIntent);
                }
//                else if (which == 2) {
//                    Intent mIntent = new Intent(MainActivity.this, ShowUrlActivity.class);
//                    mIntent.putExtra("url", ICloudMusicPlayerConstants.URL_YOUR_FACE_BOOK);
//                    mIntent.putExtra(ICloudMusicPlayerConstants.KEY_HEADER, MainActivity.this.getString(R.string.title_facebook));
//                    MainActivity.this.startActivity(mIntent);
//                }
                else if (which == 2) {
                    ShareActionUtils.goToUrl(MainActivity.this, String.format(ICloudMusicPlayerConstants.URL_FORMAT_LINK_APP, new Object[]{getPackageName()}));
                } else if (which == 3) {
                    ShareActionUtils.goToUrl(MainActivity.this, ICloudMusicPlayerConstants.URL_MORE_APPS);
                }
            }
        }).setPositiveButton(getString(R.string.title_cancel), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            }
        }).create().show();
    }

    private void changeSearchViewTextColor(View view) {
        if (view == null) {
            return;
        }
        if (view instanceof TextView) {
            ((TextView) view).setTextColor(-1);
        } else if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view;
            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                changeSearchViewTextColor(viewGroup.getChildAt(i));
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        this.searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        changeSearchViewTextColor(this.searchView);


        this.searchView.setQueryHint(Html.fromHtml("<font color = #ffffff>" + getResources().getString(R.string.title_search) + "</font>"));
        this.searchView.setSearchableInfo(((SearchManager) getSystemService(Context.SEARCH_SERVICE)).getSearchableInfo(getComponentName()));
        this.searchView.setOnQueryTextListener(new OnQueryTextListener() {
            public boolean onQueryTextSubmit(String query) {
                Log.d("sangha","submitne");
                MainActivity.this.processSearchData(query, false);
                return false;
            }
            public boolean onQueryTextChange(String newText) {
                Log.d("sangha","cap nhat ne");
                MainActivity.this.startSuggestion(newText);
                return false;
            }
        });
        this.searchView.setOnQueryTextFocusChangeListener(new OnFocusChangeListener() {
            @TargetApi(14)
            public void onFocusChange(View view, boolean queryTextFocused) {
                if (!queryTextFocused) {
                    MenuItem mMenuSearchItem = MainActivity.this.mMenu.findItem(R.id.action_search);
                    if (mMenuSearchItem != null) {
                        mMenuSearchItem.collapseActionView();
                    }
                    MainActivity.this.searchView.setQuery("", false);
                }
            }
        });
        this.searchView.setOnSuggestionListener(new OnSuggestionListener() {
            public boolean onSuggestionSelect(int position) {
                return false;
            }

            public boolean onSuggestionClick(int position) {
                if (MainActivity.this.mListSuggestionStr != null && MainActivity.this.mListSuggestionStr.size() > 0) {
                    MainActivity.this.searchView.setQuery((CharSequence) MainActivity.this.mListSuggestionStr.get(position), false);
                    MainActivity.this.processSearchData((String) MainActivity.this.mListSuggestionStr.get(position), false);
                }
                return false;
            }
        });
        this.mMenu = menu;
        return super.onCreateOptionsMenu(menu);
    }

    public void provideNameOfSongToSearch(String s)
    {

//        this.searchView.setSubmitButtonEnabled(true);
        searchView.setIconified(false);




    }
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.action_voice:
                checkVoiceSpeech();
                promptSpeechInput();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void hiddenVirtualKeyBoard() {
        if (this.searchView != null) {
            this.searchView.clearFocus();
            ApplicationUtils.hiddenVirtualKeyboard(this, this.searchView);
        }
    }



    public boolean hideLayoutPlay() {
        if (this.mLayoutPlayMusic.getVisibility() != View.VISIBLE) {
            return false;
        }
        onHiddenPlay(false);
        return true;
    }

    private void onUpdateStatePausePlay(boolean isPlay) {
        int i;
        Button button = this.mBtnPlay;
        if (isPlay) {
            i = R.drawable.ic_pause;
        } else {
            i = R.drawable.ic_play;
        }
        button.setBackgroundResource(i);
        button = this.mBtnSmallPlay;
        if (isPlay) {
            i = R.drawable.ic_pause_white_36dp;
        } else {
            i = R.drawable.ic_play_arrow_white_36dp;
        }
        button.setBackgroundResource(i);
    }

    public void registerPlayerBroadCastReceiver() {
        if (this.mPlayerBroadcast == null) {
            this.mPlayerBroadcast = new MusicPlayerBroadcast();
            IntentFilter mIntentFilter = new IntentFilter();
            mIntentFilter.addAction(getPackageName() + IMusicConstant.ACTION_BROADCAST_PLAYER);
            registerReceiver(this.mPlayerBroadcast, mIntentFilter);
        }
    }

    public void showDialogPlaylist(final TrackObject mTrackObject) { /// them vao playlist
        final ArrayList<PlaylistObject> mListPlaylist = TotalDataManager.getInstance().getListPlaylistObjects();
        if (mListPlaylist == null || mListPlaylist.size() <= 0) {
            this.mListStr = getResources().getStringArray(R.array.list_create_playlist);
            new AlertDialog.Builder(this).setTitle(R.string.title_select_playlist).setItems(this.mListStr, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    MainActivity mainActivity = MainActivity.this;
                    final TrackObject trackObject = mTrackObject;
                    mainActivity.createDialogPlaylist(false, null, new IDBCallback() {

                        class C08071 implements IDBCallback {
                            C08071() {
                            }

                            public void onAction() {
                                MainActivity.this.updateDataOfPlaylist();
                            }
                        }

                        public void onAction() {
                            MainActivity.this.updateDataOfPlaylist();
                            TotalDataManager.getInstance().addTrackToPlaylist(MainActivity.this, trackObject, (PlaylistObject) TotalDataManager.getInstance().getListPlaylistObjects().get(0), true, new C08071());
                        }
                    });
                    MainActivity.this.mListStr = null;
                }
            }).setPositiveButton(R.string.title_cancel, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    MainActivity.this.mListStr = null;
                }
            }).create().show();
            return;
        }
        int size = mListPlaylist.size();
        this.mListStr = new String[size];
        for (int i = 0; i < size; i++) {
            this.mListStr[i] = ((PlaylistObject) mListPlaylist.get(i)).getName();
        }
        new AlertDialog.Builder(this).setTitle(R.string.title_select_playlist).setItems(this.mListStr, new DialogInterface.OnClickListener() {

            class C08061 implements IDBCallback {
                C08061() {
                }

                public void onAction() {
                    MainActivity.this.updateDataOfPlaylist();
                }
            }

            public void onClick(DialogInterface dialog, int which) {
                TotalDataManager.getInstance().addTrackToPlaylist(MainActivity.this, mTrackObject, (PlaylistObject) mListPlaylist.get(which), true, new C08061());
                MainActivity.this.mListStr = null;
            }
        }).setPositiveButton(R.string.title_cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                MainActivity.this.mListStr = null;
            }
        }).create().show();
    }

    public void createDialogPlaylist(boolean isEdit, PlaylistObject mPlaylistObject, IDBCallback mCallback) {
        final EditText mEdPlaylistName = new EditText(this);
        if (isEdit) {
            mEdPlaylistName.setText(mPlaylistObject.getName());
        }
        final boolean z = isEdit;
        final PlaylistObject playlistObject = mPlaylistObject;
        final IDBCallback iDBCallback = mCallback;
        AlertDialog.Builder builder = new AlertDialog.Builder(this).setTitle(getString(R.string.title_playlist_name)).setView(mEdPlaylistName).setPositiveButton(getString(R.string.title_save), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                String mPlaylistName = mEdPlaylistName.getText().toString();
                if (StringUtils.isEmptyString(mPlaylistName)) {
                    MainActivity.this.showToast((int) R.string.info_playlistname_error);
                    return;
                }
                if (z) {
                    TotalDataManager.getInstance().editPlaylistObject(MainActivity.this, playlistObject, mPlaylistName);
                } else {
                    PlaylistObject mPlaylistObject = new PlaylistObject(System.currentTimeMillis(), mPlaylistName);
                    mPlaylistObject.setListTrackIds(new ArrayList());
                    TotalDataManager.getInstance().addPlaylistObject(MainActivity.this, mPlaylistObject);
                }
                if (iDBCallback != null) {
                    iDBCallback.onAction();
                }
            }
        }).setNegativeButton(getString(R.string.title_cancel), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        builder.setOnKeyListener(new OnKeyListener() {
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == 4) {
                    return true;
                }
                return false;
            }
        });
        AlertDialog mDialogPlaylist = builder.create();
        mDialogPlaylist.show();
        mDialogPlaylist.setCanceledOnTouchOutside(true);
        mDialogPlaylist.setCancelable(true);
    }


    public void animateViewVisibility(final View view, final int visibility)
    {

        // cancel runnning animations and remove and listeners
        view.animate().cancel();
        view.animate().setListener(null);

        // animate making view visible
        if (visibility == View.VISIBLE)
        {
            this.bottomNavigation.setVisibility(View.INVISIBLE);
            view.animate().alpha(1f).start();
            view.setVisibility(View.VISIBLE);

        }
        // animate making view hidden (HIDDEN or INVISIBLE)
        else
        {
            this.bottomNavigation.setVisibility(View.VISIBLE);
            view.animate().setListener(new AnimatorListenerAdapter()
            {


                @Override
                public void onAnimationEnd(Animator animation)
                {

                    view.setVisibility(visibility);

                }
            }).alpha(0f).start();
        }
    }
    //voice chat
    private void promptSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                getString(R.string.speech_prompt));
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getApplicationContext(),
                    getString(R.string.speech_not_supported),
                    Toast.LENGTH_SHORT).show();
        }
    }
    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputManager = (InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                MenuItem searchMenuItem = this.mMenu.findItem(R.id.action_search);

                MenuItemCompat.expandActionView(searchMenuItem);

                if (resultCode == RESULT_OK && null != data) {
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    searchView.setQuery(result.get(0), false);

                    AutoCompleteTextView autoCompleteView = (AutoCompleteTextView) MainActivity.this.searchView.findViewById(R.id.search_src_text);
                    if (autoCompleteView != null) {
                        autoCompleteView.showDropDown();


                    }
                }
                break;
            }


        }
    }
    public void startSuggestion(final String search) {

        if (!StringUtils.isEmptyString(search)) {
            DBListExcuteAction.getInstance().queueAction(new IDBCallback() {
                public void onAction() {
                    String url = String.format(ICloudMusicPlayerConstants.URL_FORMAT_SUGESSTION, new Object[]{StringUtils.urlEncodeString(search)});
                    //DBLog.m25d(MainActivity.TAG, "===============>url suggest=" + url);
                    InputStream mInputStream = DownloadUtils.download(url);
                    if (mInputStream != null) {
                        final ArrayList<String> mListSuggestionStr = XMLParsingData.parsingSuggestion(mInputStream);
                        if (mListSuggestionStr != null) {
                            Log.d("sangha","11111111111111111111111");
                            MainActivity.this.runOnUiThread(new Runnable() {
                                public void run() {
                                    MainActivity.this.searchView.setSuggestionsAdapter(null);
                                    MainActivity.this.mSuggestAdapter = null;
                                    if (MainActivity.this.mListSuggestionStr != null) {
                                        MainActivity.this.mListSuggestionStr.clear();
                                        MainActivity.this.mListSuggestionStr = null;
                                    }
                                    MainActivity.this.mListSuggestionStr = mListSuggestionStr;
                                    MainActivity.this.mTempData = null;
                                    MainActivity.this.mColumns = null;
                                    if (MainActivity.this.mCursor != null) {
                                        MainActivity.this.mCursor.close();
                                        MainActivity.this.mCursor = null;
                                    }
                                    MainActivity.this.mColumns = new String[]{"_id", "text"};
                                    MainActivity.this.mTempData = new Object[]{Integer.valueOf(0), "default"};
                                    MainActivity.this.mCursor = new MatrixCursor(MainActivity.this.mColumns);
                                    int size = mListSuggestionStr.size();
                                    MainActivity.this.mCursor.close();
                                    for (int i = 0; i < size; i++) {
                                        MainActivity.this.mTempData[0] = Integer.valueOf(i);
                                        MainActivity.this.mTempData[1] = mListSuggestionStr.get(i);
                                        MainActivity.this.mCursor.addRow(MainActivity.this.mTempData);
                                    }
                                    Log.d("sangha",""+MainActivity.this.mCursor.getCount());
                                    MainActivity.this.mSuggestAdapter = new SuggestionAdapter(MainActivity.this, MainActivity.this.mCursor, mListSuggestionStr);
                                    MainActivity.this.searchView.setSuggestionsAdapter(MainActivity.this.mSuggestAdapter);

                                }
                            });
                        }
                    }
                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }
}
