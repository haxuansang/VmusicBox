package com.mozia.VmusicBox;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.audiofx.Equalizer;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import com.mozia.VmusicBox.setting.SettingManager;
import com.mozia.VmusicBox.soundclound.SoundCloundDataMng;
import com.ypyproductions.utils.StringUtils;

import java.util.ArrayList;

/**
 * Created by thiennhan on 8/13/17.
 */

public class EqualizerActivityNew  extends DBFragmentActivity {
    public static final String TAG = EqualizerActivity.class.getSimpleName();
    private TextView minEQLevelText,tvNamePresetEq;
    private boolean isCreateLocal;
    private ArrayList<SeekBar> listSeekBars = new ArrayList();
    private ArrayList<TextView> listTextViewFreg=new ArrayList<>();
    private Equalizer mEqualizer;
    private String[] mEqualizerParams;
    private LinearLayout mLayoutBands;
    private String[] mListsNamePresset;
    private MediaPlayer mMediaPlayer;
    private Spinner mSpinnerPresents;
    private Switch mSwitchBtn;
    private short maxEQLevel;
    private short minEQLevel;
    private short bands;

    public /* bridge */ /* synthetic */ View onCreateView(View view, String str, Context context, AttributeSet attributeSet) {
        return super.onCreateView(view, str, context, attributeSet);
    }

    public /* bridge */ /* synthetic */ View onCreateView(String str, Context context, AttributeSet attributeSet) {
        return super.onCreateView(str, context, attributeSet);
    }


    GridView gridEqual;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_equalizer_new_main);
        SettingManager.setEqualizer(EqualizerActivityNew.this,true );
        setTitle(R.string.title_equalizer);
        gridEqual=(GridView) findViewById(R.id.gridViewEqualze);

        tvNamePresetEq=(TextView) findViewById(R.id.tvNamePresetEq);
        setTitle(R.string.title_equalizer);
        this.mLayoutBands = (LinearLayout) findViewById(R.id.layout_band);
        this.mMediaPlayer = SoundCloundDataMng.getInstance().getPlayer();
        if (this.mMediaPlayer == null || !this.mMediaPlayer.isPlaying()) {
            this.isCreateLocal = true;
            this.mMediaPlayer = new MediaPlayer();
        }
        setupEqualizerFxAndUITest();
        setUpPresetName();
        setUpParams();
    }

    private void setUpParams() {
        if (this.mEqualizer != null) {
            Log.d("setUpParams" ,"setUpParams11111111111");
            String presetStr = SettingManager.getEqualizerPreset(this);
            if (!StringUtils.isEmptyString(presetStr) && StringUtils.isNumber(presetStr)) {
                short preset = Short.parseShort(presetStr);
                short numberPreset = this.mEqualizer.getNumberOfPresets();
                if (numberPreset > (short) 0 && preset < numberPreset - 1 && preset >= (short) 0) {
                    this.mEqualizer.usePreset(preset);
                    tvNamePresetEq.setText(mListsNamePresset[preset]);
                    adapter.select(preset);
                    adapter.notifyDataSetChanged();
                    return;
                }
            }
            setUpEqualizerCustom();
        }
    }

    private void setUpEqualizerCustom() {
        if (this.mEqualizer != null) {
            Log.d("setUpEqualizerCustom" ,"setUpEqualizerCustom1111111111");
            String params = SettingManager.getEqualizerParams(this);
            if (!StringUtils.isEmptyString(params)) {
                this.mEqualizerParams = params.split(":");
                if (this.mEqualizerParams != null && this.mEqualizerParams.length > 0) {
                    int size = this.mEqualizerParams.length;
                    for (int i = 0; i < size; i++) {
                        this.mEqualizer.setBandLevel((short) i, Short.parseShort(this.mEqualizerParams[i]));
                        ((SeekBar) this.listSeekBars.get(i)).setProgress(Short.parseShort(this.mEqualizerParams[i]) - this.minEQLevel);
                    }
                    tvNamePresetEq.setText(mListsNamePresset[mListsNamePresset.length-1]);
                    adapter.select(mListsNamePresset.length-1);
                    adapter.notifyDataSetChanged();
                    SettingManager.setEqualizerPreset(this, String.valueOf(this.mListsNamePresset.length - 1));
                }
            }
        }
    }


    private void setupEqualizerFxAndUITest() {
        Log.d("setupEqualizerFxAndUI" ,"setupEqualizerFxAndUI1111111111");
        this.mEqualizer = SoundCloundDataMng.getInstance().getEqualizer();
        if (this.mEqualizer == null) {
            Log.d("setupEqualizerFxAndUI" ,"setupEqualizerFxAndUI2222222222");
            this.mEqualizer = new Equalizer(0, this.mMediaPlayer.getAudioSessionId());
            this.mEqualizer.setEnabled(SettingManager.getEqualizer(this));
        }
        this.mEqualizer.setEnabled(SettingManager.getEqualizer(this));
        this.bands = this.mEqualizer.getNumberOfBands();
        if (this.bands != (short) 0) {
            short[] bandRange = this.mEqualizer.getBandLevelRange();
            if (bandRange != null && bandRange.length >= 2) {
                this.minEQLevel = bandRange[0];
                this.maxEQLevel = bandRange[1];
                for (short i = (short) 0; i < this.bands; i = (short) (i + 1)) {
                    final short band = i;
                    TextView freqTextView = new TextView(this);
                    LinearLayout.LayoutParams layoutParamsfreqTextView = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, 0);
                    layoutParamsfreqTextView.weight = 1;
                    freqTextView.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
                    freqTextView.setText(new StringBuilder(String.valueOf((this.mEqualizer.getBandFreqRange(band)[0] / 1000))).append(" Hz").toString());
                    LinearLayout row = new LinearLayout(this);
                    row.setWeightSum(10);
                    LinearLayout.LayoutParams layoutParamsT = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
                    layoutParamsT.weight=1;
                    row.setOrientation(LinearLayout.VERTICAL);
                    row.setLayoutParams(layoutParamsT);
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, 0);
                    layoutParams.weight = 9;
                    VerticalSeekBar bar = new VerticalSeekBar(this);
                    bar.setLayoutParams(layoutParams);
                    bar.setThumb(getResources().getDrawable(R.drawable.blue_scrubber_control_selector_holo_light));
                    bar.setIndeterminateDrawable(getResources().getDrawable(R.drawable.blue_scrubber_progress_horizontal_holo_light));
                    bar.setProgressDrawable(getResources().getDrawable(R.drawable.blue_scrubber_progress_horizontal_holo_light));
                    bar.setMax(this.maxEQLevel - this.minEQLevel);
                    bar.setProgress(this.mEqualizer.getBandLevel(band) - this.minEQLevel);
                    bar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                            if (fromUser) {
                                mEqualizer.setBandLevel(band, (short) (minEQLevel + progress));
                                Log.d("setBandLevel",String.valueOf(minEQLevel + progress) +"");
                                saveEqualizerParams();
                                tvNamePresetEq.setText(mListsNamePresset[mListsNamePresset.length-1]);
                                adapter.select(mListsNamePresset.length-1);
                                adapter.notifyDataSetChanged();
                            }
                        }

                        public void onStartTrackingTouch(SeekBar seekBar) {
                        }

                        public void onStopTrackingTouch(SeekBar seekBar) {
                        }
                    });
                    this.listSeekBars.add(bar);
                    listTextViewFreg.add(freqTextView);
                    row.addView(bar);
                    row.addView(freqTextView);
                    this.mLayoutBands.addView(row);
                }
            }
        }
    }

    private void saveEqualizerParams() {
        if (this.mEqualizer != null && this.bands > (short) 0) {
            Log.d("saveEqualizerParams", "saveEqualizerParams1111111111");
            String data = "";
            for (short i = (short) 0; i < this.bands; i = (short) (i + 1)) {
                if (i < this.bands ) {
                    data = new StringBuilder(String.valueOf(data)).append(this.mEqualizer.getBandLevel(i)).append(":").toString();
                }
            }
            //DBLog.m25d(TAG, "================>dataSave=" + data);
            SettingManager.setEqualizerPreset(this, String.valueOf(this.mListsNamePresset.length - 1));
            SettingManager.setEqualizerParams(this, data);
        }
    }
    private void saveEqualizerParams(int pos)   {
        if (this.mEqualizer != null && this.bands > (short) 0) {
            Log.d("saveEqualizerParams", "saveEqualizerParams1111111111");
            String data = "";
            for (short i = (short) 0; i < this.bands; i = (short) (i + 1)) {
                if (i < this.bands - 1) {
                    data = new StringBuilder(String.valueOf(data)).append(this.mEqualizer.getBandLevel(i)).append(":").toString();
                }
            }
            //DBLog.m25d(TAG, "================>dataSave=" + data);
            SettingManager.setEqualizerPreset(this, String.valueOf(pos));
            SettingManager.setEqualizerParams(this, data);
        }
    }

    public void clickSelect(int position){
        SettingManager.setEqualizerPreset(getApplicationContext(), String.valueOf(position));
        if (position < this.mListsNamePresset.length - 1) {
            Log.d("C05053" ,"OnItemSelectedListener");
            this.mEqualizer.usePreset((short) position);
        } else {
            Log.d("C05053" ,"OnItemSelectedListener111111");
            this.setUpEqualizerCustom();
            return;
        }
//        EqualizerActivityNew.this.mEqualizer.usePreset((short) position);
//        saveEqualizerParams(position);
        // change ui click select preset
        tvNamePresetEq.setText(mListsNamePresset[position]);
        adapter.select(position);
        adapter.notifyDataSetChanged();
        for (short i = (short) 0; i < EqualizerActivityNew.this.bands; i = (short) (i + 1)) {

            ((SeekBar) EqualizerActivityNew.this.listSeekBars.get(i)).setProgress(EqualizerActivityNew.this.mEqualizer.getBandLevel(i) - EqualizerActivityNew.this.minEQLevel);

            ((TextView)listTextViewFreg.get(i)).setText(String.valueOf((this.mEqualizer.getCenterFreq(i)/ 1000) +" hz"));

            Log.d("C05053" ,"OnItemSelectedListener:"+i+":"+(EqualizerActivityNew.this.mEqualizer.getBandLevel(i) - EqualizerActivityNew.this.minEQLevel)+"");
            Log.d("C05053" ,"OnItemSelectedListener: "+i+":"+String.valueOf((this.mEqualizer.getCenterFreq(i) / 1000)));
        }
    }

    private void setUpPresetName() {
        Log.d("setUpPresetName" ,"setUpPresetName1111111111");
        if (mEqualizer != null) {
            short numberPreset = this.mEqualizer.getNumberOfPresets();
            if (numberPreset > (short) 0) {
                ArrayList<Item> items=new ArrayList<>();
                this.mListsNamePresset = new String[(numberPreset+1)];
                for (short i = (short) 0; i < numberPreset; i = (short) (i + 1)) {
                    this.mListsNamePresset[i] = this.mEqualizer.getPresetName(i);
                    items.add(new Item(i,mListsNamePresset[i]));
                }
                this.mListsNamePresset[numberPreset] = getString(R.string.title_custom);
                items.add(new Item(numberPreset,mListsNamePresset[numberPreset]));
                setGridViewEqualzie(items);
                return;
            }
            return;
        }
    }


    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode != 4) {
            return super.onKeyDown(keyCode, event);
        }
        finish();
        return true;
    }


    MyAdapter adapter;
    void setGridViewEqualzie(ArrayList<Item> list){
        adapter=new MyAdapter(getApplicationContext(),R.layout.item_equalizer,list);
        gridEqual.setAdapter(adapter);
        gridEqual.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                clickSelect(position);
            }
        });
    }

    public class Item {
        int id;
        String Name;
        int icon;
        int iconSelect;
        public Item(int id,String name)
        {
            this.id=id;
            this.Name=name;
            setIcon(id);
            setIconSelect(id);
        }
        void setIconSelect(int id){
            int[] icon=new  int [10];
            icon[0]=R.drawable.equal_normal_click;
            icon[1]=R.drawable.equal_classic_click;
            icon[2]=R.drawable.equal_dance_click;
            icon[3]=R.drawable.equal_flat_click;
            icon[4]=R.drawable.equal_folk_click;
            icon[5]=R.drawable.equal_heavy_metal_click;
            icon[6]=R.drawable.equal_hiphop_click;
            icon[7]=R.drawable.equal_jazz_click;
            icon[8]=R.drawable.equal_pop_click;
            icon[9]=R.drawable.equal_rock_click;
            if(id>9){
                this.iconSelect=R.drawable.equal_normal_click;
                return;
            }
            this.iconSelect=icon[id];
        }
        void setIcon(int id){
            int[] icon=new  int [10];
            icon[0]=R.drawable.equal_normal;
            icon[1]=R.drawable.equal_classic;
            icon[2]=R.drawable.equal_dance;
            icon[3]=R.drawable.equal_flat;
            icon[4]=R.drawable.equal_folk;
            icon[5]=R.drawable.equal_heavymetal;
            icon[6]=R.drawable.equa_hiphop;
            icon[7]=R.drawable.equal_jazz;
            icon[8]=R.drawable.equal_pop;
            icon[9]=R.drawable.equal_rock;
            if(id>9){
                this.icon=R.drawable.equal_normal;
                return;
            }
            this.icon=icon[id];

        }
    }

    public class MyAdapter extends ArrayAdapter {
        int select=-1;
        ArrayList<Item> items = new ArrayList<>();

        public MyAdapter(Context context, int textViewResourceId, ArrayList<Item> objects) {
            super(context, textViewResourceId, objects);
            items = objects;
        }

        @Override
        public int getCount() {
            return super.getCount();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = convertView;
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.item_equalizer, null);
            TextView textView = (TextView) v.findViewById(R.id.tvNameEqualze);
            ImageView imageView = (ImageView) v.findViewById(R.id.ivItemEqualze);
            textView.setText(items.get(position).Name);
            if(select==position) imageView.setImageResource(items.get(position).iconSelect);
            else imageView.setImageResource(items.get(position).icon);
            return v;

        }
        public void select(int c){
            select=c;
        }
    }

}
