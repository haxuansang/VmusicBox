package com.mozia.VmusicBox.fragment;


import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnLastItemVisibleListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.mozia.VmusicBox.APISuggestion.ResponseSuggestion;
import com.mozia.VmusicBox.APISuggestion.RetrofitSuggestion.APIUltilsSuggestion;
import com.mozia.VmusicBox.APISuggestion.RetrofitSuggestion.SOServiceSuggestion;
import com.mozia.VmusicBox.APISuggestion.Track;
import com.mozia.VmusicBox.MainActivity;
import com.mozia.VmusicBox.R;
import com.mozia.VmusicBox.adapter.NewTrackAdapter;
import com.mozia.VmusicBox.adapter.NewTrackAdapter.INewTrackAdapterListener;
import com.mozia.VmusicBox.adapter.SuggestAdapter;
import com.mozia.VmusicBox.constants.ICloudMusicPlayerConstants;
import com.mozia.VmusicBox.dataMng.TotalDataManager;
import com.mozia.VmusicBox.listener.AddToPlaying;
import com.mozia.VmusicBox.object.TopMusicObject;
import com.mozia.VmusicBox.setting.ISettingConstants;
import com.mozia.VmusicBox.setting.SettingManager;
import com.mozia.VmusicBox.soundclound.ISoundCloundConstants;
import com.mozia.VmusicBox.soundclound.SoundCloundDataMng;
import com.mozia.VmusicBox.soundclound.object.TrackObject;
import com.ypyproductions.abtractclass.fragment.DBFragment;
import com.ypyproductions.task.DBTask;
import com.ypyproductions.task.IDBTaskListener;
import com.ypyproductions.utils.ApplicationUtils;
import com.ypyproductions.utils.StringUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import pl.droidsonroids.gif.GifImageButton;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;
import static com.facebook.FacebookSdk.getApplicationContext;

public class FragmentSearch extends DBFragment implements ICloudMusicPlayerConstants, ISoundCloundConstants, ISettingConstants, OnLastItemVisibleListener {
    public static final String TAG = FragmentSearch.class.getSimpleName();
    private int currentPage = 0;
    private boolean isAllowAddPage = false;
    private boolean isStartAddingPage = false;
    private MainActivity mContext;
    private DBTask mDBTask;
    private RelativeLayout mFooterView;
    private ArrayList<TrackObject> mListTrackObjects;
    private List<Track> mListSuggestion=new ArrayList<>();
    private ArrayList<TrackObject> mListMainSuggestion= new ArrayList<>();
    private RecyclerView recyclerSuggestion;
    private PullToRefreshListView mListView;
    private NewTrackAdapter mTrackAdapter;
    private TextView mTvNoResult;
    protected ProgressDialog progressDialog;
    private GifImageButton boxGiftView;
    private RelativeLayout rl_giftbox,rl_aigiftbox_emotion;
    private Button btnOpenGiftBox,btnCloseGiftBox2,btnSendResult;
    private ImageButton btnCloseGiftBox1,btnVoiceSpeech;
    private EditText edt_answer;
    private CardView cardViewGiftBox;
    private LinearLayout linear_main_suggestion,linear_load_more;
    private SuggestAdapter suggestAdapter;
    private SOServiceSuggestion mSOS;
    private static int countSongSuggestion=0;
    public final static int REQ_CODE_SPEECH_INPUT_SUGGESTION=2807;
    private LinearLayoutManager linearLayoutManager;
    private boolean isScrolling=false;
    private int currentItems,totalItems,scrolledItems;
    private ProgressBar progressBarLoadData;
    private int countVisibleItem=0;


    class C08261 implements OnRefreshListener<ListView> {
        C08261() {
        }

        public void onRefresh(PullToRefreshBase<ListView> pullToRefreshBase) {
            FragmentSearch.this.startGetData(true, SettingManager.getLastKeyword(FragmentSearch.this.mContext), true);
        }
    }

    class C08272 implements INewTrackAdapterListener {
        C08272() {
        }

        public void onDownload(TrackObject mTrackObject) {
        }

        public void onListenDemo(TrackObject mTrackObject) {  // chuyen sang activity nghe nhac
            if (ApplicationUtils.isOnline(FragmentSearch.this.mContext)) {
                SoundCloundDataMng.getInstance().setListPlayingTrackObjects(FragmentSearch.this.mListTrackObjects);
                FragmentSearch.this.mContext.setInfoForPlayingTrack(mTrackObject, true, true);
                Log.d(TAG, "onListenDemo: "+mTrackObject.getLinkStream()+"\t"+mTrackObject.getPermalinkUrl());
                return;
            }
            FragmentSearch.this.mContext.showToast((int) R.string.info_server_error);
        }

        public void onAddToPlaylist(TrackObject mTrackObject) {
            FragmentSearch.this.mContext.showDialogPlaylist(mTrackObject);
        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.mRootView = inflater.inflate(R.layout.fragment_search, container, false);



        return this.mRootView;
    }

    public View onInflateLayout(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.mRootView = inflater.inflate(R.layout.fragment_search, container, false);
        return this.mRootView;
    }


    public void findView() {
        intializeData();
        startGetData(false, SettingManager.getLastKeyword(this.mContext), false);
        setUpGiftBoxView();
       /* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mListView.setNestedScrollingEnabled(true);
        }*/
    }

    private void setUpGiftBoxView() {
        this.btnOpenGiftBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    rl_giftbox.setVisibility(View.GONE);
                    btnCloseGiftBox1.setVisibility(View.GONE);
                    rl_aigiftbox_emotion.setVisibility(View.VISIBLE);
            }
        });
        this.btnCloseGiftBox1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cardViewGiftBox.setVisibility(View.GONE);
            }
        });
        this.btnCloseGiftBox2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cardViewGiftBox.setVisibility(View.GONE);
            }
        });
        this.btnSendResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rl_aigiftbox_emotion.setVisibility(View.GONE);
                progressBarLoadData.setVisibility(View.VISIBLE);
                btnCloseGiftBox1.setVisibility(View.VISIBLE);
                mSOS= APIUltilsSuggestion.getSOService();
                startGetSuggestion(String.valueOf(countSongSuggestion));
                recyclerSuggestion.addOnScrollListener(new RecyclerView.OnScrollListener() {
                    @Override
                    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                        super.onScrollStateChanged(recyclerView, newState);
                        if(newState== AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL)
                        {
                            isScrolling=true;
                        }
                    }

                    @Override
                    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                        super.onScrolled(recyclerView, dx, dy);
                        scrolledItems=linearLayoutManager.findFirstVisibleItemPosition();
                        currentItems = linearLayoutManager.getChildCount();
                        totalItems= linearLayoutManager.getItemCount();
                        if(isScrolling && (scrolledItems+currentItems==totalItems))
                        {   
                            
                            isScrolling=false;
                            startGetSuggestion(String.valueOf(countSongSuggestion+=10));
                            linear_load_more.setVisibility(View.VISIBLE);
                            if(linearLayoutManager.getItemCount()<countSongSuggestion)
                                linear_load_more.setVisibility(View.GONE);

                        }
                    }
                });

            }
        });
        btnVoiceSpeech.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                promptSpeechInput();

            }
        });



    }
    private void promptSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                getString(R.string.speech_prompt));
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT_SUGGESTION);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getApplicationContext(),
                    getString(R.string.speech_not_supported),
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode)
        {
            case REQ_CODE_SPEECH_INPUT_SUGGESTION:
            {
                if(resultCode==RESULT_OK && data!=null)
                {
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                     String mainResult = result.get(0);
                     edt_answer.setText(mainResult);

                }
            }
        }
    }

    private void  startGetSuggestion(String offset) {


        mSOS.getAnswers(idOfPlaylist,idOfClient,offset,maxItems).enqueue(new Callback<ResponseSuggestion>() {
            @Override
            public void onResponse(Call<ResponseSuggestion> call, Response<ResponseSuggestion> response) {
                if(response.isSuccessful())
                {
                    Log.d(TAG, "startGetSuggestion: lay listnhac");
                    List<Track> mListCurrent = new ArrayList<>();
                    mListCurrent =response.body().getTracks();
                    mListSuggestion.addAll(mListCurrent);
                    Log.d(TAG, "onResponse: "+mListSuggestion.size());
                    addToMainListSuggestion(mListCurrent);
                    if(suggestAdapter==null)
                    {

                        suggestAdapter = new SuggestAdapter(FragmentSearch.this.getContext(),FragmentSearch.this.mListSuggestion);
                        recyclerSuggestion.setAdapter(suggestAdapter);
                        FragmentSearch.this.linearLayoutManager=new LinearLayoutManager(FragmentSearch.this.getContext(),LinearLayoutManager.HORIZONTAL,false);
                        recyclerSuggestion.setLayoutManager(linearLayoutManager);
                        recyclerSuggestion.setHasFixedSize(true);
                        suggestAdapter.registerListener(new AddToPlaying() {
                            @Override
                            public void addToPlaying(TrackObject mTrackObject,int position) {
                                if (ApplicationUtils.isOnline(FragmentSearch.this.mContext)) {
                                    SoundCloundDataMng.getInstance().setListPlayingTrackObjects(FragmentSearch.this.mListMainSuggestion);
                                    FragmentSearch.this.mContext.setInfoForPlayingTrack(mTrackObject, true, true,position);

                                }
                            }
                        });

                        progressBarLoadData.setVisibility(View.GONE);
                        linear_main_suggestion.setVisibility(View.VISIBLE);



                    }

                    suggestAdapter.notifyDataSetChanged();
                    linear_load_more.setVisibility(View.GONE);

                }
            }

            @Override
            public void onFailure(Call<ResponseSuggestion> call, Throwable t) {
                Log.d(TAG, "startGetSuggestion: "+t.getMessage());
            }
        });

    }


    private void addToMainListSuggestion(List<Track> mListCurrent) {
        if(mListMainSuggestion==null)
            mListMainSuggestion= new ArrayList<>();
        for(int i=0;i<mListCurrent.size();i++) {
            mListMainSuggestion.add(new TrackObject(mListSuggestion.get(i).getId(), mListSuggestion.get(i).getDuration(), mListSuggestion.get(i).getTitle(), mListSuggestion.get(i).getDescription(),
                    mListSuggestion.get(i).getUser().getUsername(), mListSuggestion.get(i).getUser().getAvatarUrl(), mListSuggestion.get(i).getPermalinkUrl(),
                    mListSuggestion.get(i).getArtworkUrl(), mListSuggestion.get(i).getPlaybackCount()));

        }
    }

    private void intializeData() {
        setAllowFindViewContinous(true);
        this.mContext = (MainActivity) getActivity();
        this.mListView = (PullToRefreshListView) this.mRootView.findViewById(R.id.list_tracks);
        this.mTvNoResult = (TextView) this.mRootView.findViewById(R.id.tv_no_result);
        this.mTvNoResult.setTypeface(this.mContext.mTypefaceNormal);
        this.mFooterView = (RelativeLayout) this.mRootView.findViewById(R.id.layout_footer);
        ((TextView) this.mFooterView.findViewById(R.id.tv_message)).setTypeface(this.mContext.mTypefaceNormal);
        //GiftBoxTextView
        boxGiftView= (GifImageButton) this.mRootView.findViewById(R.id.gifView);
        rl_giftbox=(RelativeLayout)this.mRootView.findViewById(R.id.viewGiftBox);
        rl_aigiftbox_emotion=(RelativeLayout)this.mRootView.findViewById(R.id.GiftBoxAIMain);

        btnOpenGiftBox=(Button)this.mRootView.findViewById(R.id.opengift);
        btnCloseGiftBox1=(ImageButton) this.mRootView.findViewById(R.id.btn_close_giftbox);
        btnCloseGiftBox2=(Button)this.mRootView.findViewById(R.id.btn_close_giftbox2);
        btnSendResult=(Button)this.mRootView.findViewById(R.id.btn_send_answer);
        cardViewGiftBox=(CardView)this.mRootView.findViewById(R.id.cardViewAIEmotions);
        recyclerSuggestion = (RecyclerView)this.mRootView.findViewById(R.id.recyclerSuggestion);
        linear_main_suggestion=(LinearLayout)this.mRootView.findViewById(R.id.linear_main_suggestion);
        linear_load_more=(LinearLayout)this.mRootView.findViewById(R.id.linear_loadmore);
        btnVoiceSpeech=(ImageButton)this.mRootView.findViewById(R.id.speech_voice_giftbox);
        edt_answer=(EditText)this.mRootView.findViewById(R.id.edt_answerGiftBox);
        progressBarLoadData=(ProgressBar)this.mRootView.findViewById(R.id.load_data);

        this.mListView.setOnRefreshListener(new C08261());
    }

    private void showFooterView() {
        if (this.mFooterView.getVisibility() != View.VISIBLE) {
            this.mFooterView.setVisibility(View.VISIBLE);
        }
    }

    private void hideFooterView() {
        if (this.mFooterView.getVisibility() == View.VISIBLE) {
            this.mFooterView.setVisibility(View.GONE);
        }
    }

    private void setUpInfo(ArrayList<TrackObject> mListNewTrackObjects, boolean isRefresh) { // Tim bai hat
        if (this.mListView == null) {
            intializeData();
        }
        this.mListView.setAdapter(null);
        if (isRefresh && this.mListTrackObjects != null) {
            this.mListTrackObjects.clear();
            this.mListTrackObjects = null;
        }
        this.mListTrackObjects = mListNewTrackObjects;
        if (mListNewTrackObjects == null || mListNewTrackObjects.size() <= 0) { // fix 2 14/9/201
            this.mTvNoResult.setVisibility(View.VISIBLE);

        }
        else {
            OnLastItemVisibleListener onLastItemVisibleListener;
            this.mTvNoResult.setVisibility(View.GONE);
            this.mListView.setVisibility(View.VISIBLE);

            this.mTrackAdapter = new NewTrackAdapter(this.mContext, mListNewTrackObjects, this.mContext.mTypefaceBold, this.mContext.mTypefaceLight, this.mContext.mImgTrackOptions, this.mContext.mAvatarOptions);
            this.mListView.setAdapter(this.mTrackAdapter);

            this.mTrackAdapter.setNewTrackAdapterListener(new C08272());

            this.currentPage = this.mListTrackObjects.size() / 10;
            if (this.currentPage >= 14) {
                this.isAllowAddPage = false;
            } else {
                this.isAllowAddPage = true;
            }
            PullToRefreshListView pullToRefreshListView = this.mListView;
            if (this.isAllowAddPage) {
                onLastItemVisibleListener = this;
            } else {
                onLastItemVisibleListener = null;
            }
            pullToRefreshListView.setOnLastItemVisibleListener(onLastItemVisibleListener);
            MediaPlayer mPlayer = SoundCloundDataMng.getInstance().getPlayer();
            if (mPlayer != null) {
                try {
                    if (mPlayer.isPlaying()) {
                        ArrayList<TrackObject> mListCurrrentPlayer = SoundCloundDataMng.getInstance().getListPlayingTrackObjects();
                        if (mListCurrrentPlayer == null || !mListCurrrentPlayer.equals(TotalDataManager.getInstance().getListLibraryTrackObjects())) {
                            SoundCloundDataMng.getInstance().setListPlayingTrackObjects(this.mListTrackObjects);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void startGetData(final boolean isRefresh, final String keyword, boolean isForce) {

        // TODO: 8/10/17  nhan fix for no find data main
//        FragmentMusicGenre fragmentMusicGenre = new FragmentMusicGenre();
//        fragmentMusicGenre.checkHomeEnable();
        if (!(isRefresh || isForce)) {
            ArrayList<TrackObject> mListCurrentTrack = TotalDataManager.getInstance().getListCurrrentTrackObjects();
            if (mListCurrentTrack != null) {
                this.mListView.onRefreshComplete();
                setUpInfo(mListCurrentTrack, isRefresh);
                return;
            }
        }
        if (this.mContext == null) {
            intializeData();
        }
        if (!ApplicationUtils.isOnline(this.mContext)) {
            this.mListView.onRefreshComplete();
          //  this.mContext.showToast((int) R.string.info_lose_internet);
            if (this.mTrackAdapter == null) {
                //this.mTvNoResult.setVisibility(View.VISIBLE);
            }
        } else if (this.isStartAddingPage) {
            this.mListView.onRefreshComplete();
        } else {
            this.isStartAddingPage = false;
            this.isAllowAddPage = false;
            this.currentPage = 0;
            if (StringUtils.isEmptyString(keyword)) {
                        setUpInfo(new ArrayList(), true);
//                return;
            }
            this.mDBTask = new DBTask(new IDBTaskListener() {
                private ArrayList<TopMusicObject> mListNewTopObjects;
                private ArrayList<TrackObject> mListNewTrackObjects;

                class C05401 implements Runnable {
                    C05401() {
                    }

                    public void run() {
                        FragmentSearch.this.mContext.setUpInfoForTop(mListNewTopObjects);
                    }
                }

                public void onPreExcute() {
                    FragmentSearch.this.mTvNoResult.setVisibility(View.GONE);
                    if (!isRefresh) {
                        FragmentSearch.this.mContext.showProgressDialog();
                    }
                }

                public void onDoInBackground() {// Important part
                    Log.d(TAG, "onDoInBackground:load data");
                    if (SettingManager.getSearchType(FragmentSearch.this.mContext) == 2) {
                        this.mListNewTrackObjects = FragmentSearch.this.mContext.mSoundClound.getListTrackObjectsByGenre(keyword, 0, 10);
                    } else {
                        this.mListNewTrackObjects = FragmentSearch.this.mContext.mSoundClound.getListTrackObjectsByQuery(keyword, 0, 10);
                    }
                    if (this.mListNewTrackObjects != null && this.mListNewTrackObjects.size() > 0) {
                        TotalDataManager.getInstance().setListCurrrentTrackObjects(this.mListNewTrackObjects);

                        SettingManager.setLastKeyword(FragmentSearch.this.mContext, keyword);
                    } else if (SettingManager.getSearchType(FragmentSearch.this.mContext) == 2) {
                        SettingManager.setSearchType(FragmentSearch.this.mContext, 1);
                    }
                    if (!FragmentSearch.this.mContext.isFirstTime) {
                        FragmentSearch.this.mContext.isFirstTime = true;
                        if (TotalDataManager.getInstance().getListTopMusicObjects() == null || TotalDataManager.getInstance().getListTopMusicObjects().size() == 0) {
                            this.mListNewTopObjects = FragmentSearch.this.mContext.mSoundClound.getListTopMusic(SettingManager.getLanguage(FragmentSearch.this.mContext), 80);
                            if (this.mListNewTopObjects != null && this.mListNewTopObjects.size() > 0) {

                                TotalDataManager.getInstance().setListTopMusicObjects(this.mListNewTopObjects);
                            }
                            FragmentSearch.this.mContext.runOnUiThread(new C05401());
                        }
                    }
                }

                public void onPostExcute() {
                    FragmentSearch.this.mContext.dimissProgressDialog();
                    FragmentSearch.this.mListView.onRefreshComplete();
                    FragmentSearch.this.mContext.hiddenVirtualKeyBoard();
                    FragmentSearch.this.setUpInfo(this.mListNewTrackObjects, true);
                }
            });
            this.mDBTask.execute(new Void[0]);
        }
    }

    public void onLastItemVisible() {
        if (this.mTrackAdapter != null) {
           // //DBLog.m25d(TAG, "==============>isAllowAddPage=" + this.isAllowAddPage + "===>isRefreshing=" + this.mListView.isRefreshing());
            if (!this.isAllowAddPage || this.mListView.isRefreshing()) {
                hideFooterView();
            } else if (ApplicationUtils.isOnline(this.mContext)) {
                showFooterView();
                if (!this.isStartAddingPage) {
                    this.isStartAddingPage = true;
                    onLoadNextTrackObject();
                }
            }
        }
    }

    private void onLoadNextTrackObject() {
        if (ApplicationUtils.isOnline(this.mContext)) {
            final int offset = this.mListTrackObjects.size() + 1;
            this.mDBTask = new DBTask(new IDBTaskListener() {
                private ArrayList<TrackObject> mListNewTrackObjects;

                public void onPreExcute() {
                }

                public void onDoInBackground() {
                    if (SettingManager.getSearchType(FragmentSearch.this.mContext) == 2) {
                        this.mListNewTrackObjects = FragmentSearch.this.mContext.mSoundClound.getListTrackObjectsByGenre(SettingManager.getLastKeyword(FragmentSearch.this.mContext), offset, 10);
                    } else {
                        this.mListNewTrackObjects = FragmentSearch.this.mContext.mSoundClound.getListTrackObjectsByQuery(SettingManager.getLastKeyword(FragmentSearch.this.mContext), offset, 10);
                    }
                }

                public void onPostExcute() {
                    FragmentSearch.this.hideFooterView();
                    FragmentSearch.this.isStartAddingPage = false;
                    if (this.mListNewTrackObjects == null || this.mListNewTrackObjects.size() <= 0) {
                        FragmentSearch.this.isAllowAddPage = false;
                    } else {
                        Iterator it = this.mListNewTrackObjects.iterator();
                        while (it.hasNext()) {
                            FragmentSearch.this.mListTrackObjects.add((TrackObject) it.next());
                        }
                        if (FragmentSearch.this.mTrackAdapter != null) {
                            FragmentSearch.this.mTrackAdapter.notifyDataSetChanged();
                        }
                        FragmentSearch fragmentSearch = FragmentSearch.this;
                        fragmentSearch.currentPage = fragmentSearch.currentPage + 1;
                        //DBLog.m25d(FragmentSearch.TAG, "=========>currentPage=" + FragmentSearch.this.currentPage);
                        if (FragmentSearch.this.currentPage < 14) {
                            FragmentSearch.this.isAllowAddPage = true;
                        } else {
                            FragmentSearch.this.isAllowAddPage = false;
                        }
                    }
                    FragmentSearch.this.mListView.setOnLastItemVisibleListener(FragmentSearch.this.isAllowAddPage ? FragmentSearch.this : null);
                }
            });
            this.mDBTask.execute(new Void[0]);
            return;
        }
        hideFooterView();
        this.mContext.showToast((int) R.string.info_lose_internet);
    }
}
