package com.mozia.VmusicBox;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.audiofx.Equalizer;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import com.google.android.gms.cast.TextTrackStyle;
import com.mozia.VmusicBox.adapter.PresetAdapter;
import com.mozia.VmusicBox.setting.SettingManager;
import com.mozia.VmusicBox.soundclound.SoundCloundDataMng;
import com.ypyproductions.utils.DirectionUtils;
import com.ypyproductions.utils.StringUtils;

import java.util.ArrayList;

public class EqualizerActivity extends DBFragmentActivity {
    public static final String TAG = EqualizerActivity.class.getSimpleName();
    private short bands;
    private boolean isCreateLocal;
    private ArrayList<SeekBar> listSeekBars = new ArrayList();
    private Equalizer mEqualizer;
    private String[] mEqualizerParams;
    private LinearLayout mLayoutBands;
    private String[] mLists;
    private MediaPlayer mMediaPlayer;
    private Spinner mSpinnerPresents;
    private Switch mSwitchBtn;
    private short maxEQLevel;
    private short minEQLevel;

    class C05031 implements OnClickListener {
        C05031() {
        }

        public void onClick(View v) {
            Log.d("C05031" ,"SettingManager.setEqualizer");
            SettingManager.setEqualizer(EqualizerActivity.this, EqualizerActivity.this.mSwitchBtn.isChecked());
            EqualizerActivity.this.startCheckEqualizer();
        }
    }

    class C05053 implements OnItemSelectedListener {
        C05053() {
        }

        public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
            SettingManager.setEqualizerPreset(EqualizerActivity.this, String.valueOf(position));
            if (position < EqualizerActivity.this.mLists.length - 1) {
                Log.d("C05053" ,"OnItemSelectedListener");
                EqualizerActivity.this.mEqualizer.usePreset((short) position);
            } else {
                Log.d("C05053" ,"OnItemSelectedListener111111");
                EqualizerActivity.this.setUpEqualizerCustom();
            }
            saveEqualizerParams();
            for (short i = (short) 0; i < EqualizerActivity.this.bands; i = (short) (i + 1)) {
                Log.d("C05053" ,"OnItemSelectedListener222222222"+(EqualizerActivity.this.mEqualizer.getBandLevel(i) - EqualizerActivity.this.minEQLevel)+"");
                ((SeekBar) EqualizerActivity.this.listSeekBars.get(i)).setProgress(EqualizerActivity.this.mEqualizer.getBandLevel(i) - EqualizerActivity.this.minEQLevel);
            }
        }

        public void onNothingSelected(AdapterView<?> adapterView) {
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
        setContentView(R.layout.activity_equalizer);
        setTitle(R.string.title_equalizer);
        this.mLayoutBands = (LinearLayout) findViewById(R.id.layout_bands);
        this.mSpinnerPresents = (Spinner) findViewById(R.id.list_preset);
        this.mSwitchBtn = (Switch) findViewById(R.id.switch1);
        this.mSwitchBtn.setOnClickListener(new C05031());
        this.mMediaPlayer = SoundCloundDataMng.getInstance().getPlayer();
        if (this.mMediaPlayer == null || !this.mMediaPlayer.isPlaying()) {
            this.isCreateLocal = true;
            this.mMediaPlayer = new MediaPlayer();
        }
        setupEqualizerFxAndUI();
        setUpPresetName();
        startCheckEqualizer();
        setUpParams();
        ((Button) findViewById(R.id.demoNew)).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                DirectionUtils.changeActivity(EqualizerActivity.this, R.anim.slide_in_from_right, R.anim.slide_out_to_left, false, new Intent(getApplicationContext(), EqualizerActivityNew.class));
            }
        });
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
                    this.mSpinnerPresents.setSelection(preset);
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
                    this.mSpinnerPresents.setSelection(this.mLists.length - 1);
                    SettingManager.setEqualizerPreset(this, String.valueOf(this.mLists.length - 1));
                }
            }
        }
    }

    private void saveEqualizerParams() {
        if (this.mEqualizer != null && this.bands > (short) 0) {
            Log.d("saveEqualizerParams" ,"saveEqualizerParams1111111111");
            String data = "";
            for (short i = (short) 0; i < this.bands; i = (short) (i + 1)) {
                if (i < this.bands - 1) {
                    data = new StringBuilder(String.valueOf(data)).append(this.mEqualizer.getBandLevel(i)).append(":").toString();
                }
            }
            //DBLog.m25d(TAG, "================>dataSave=" + data);
            SettingManager.setEqualizerPreset(this, String.valueOf(this.mLists.length - 1));
            SettingManager.setEqualizerParams(this, data);
        }
    }

    private void startCheckEqualizer() {
        boolean b = SettingManager.getEqualizer(this);
        this.mSpinnerPresents.setEnabled(b);
        if (this.mEqualizer != null) {
            this.mEqualizer.setEnabled(b);
        }
        if (this.listSeekBars.size() > 0) {
            for (int i = 0; i < this.listSeekBars.size(); i++) {
                ((SeekBar) this.listSeekBars.get(i)).setEnabled(b);
            }
        }
        this.mSwitchBtn.setChecked(b);
    }

    private void setupEqualizerFxAndUI() {
        Log.d("setupEqualizerFxAndUI" ,"setupEqualizerFxAndUI1111111111");
        this.mEqualizer = SoundCloundDataMng.getInstance().getEqualizer();
        if (this.mEqualizer == null) {
            Log.d("setupEqualizerFxAndUI" ,"setupEqualizerFxAndUI2222222222");
            this.mEqualizer = new Equalizer(0, this.mMediaPlayer.getAudioSessionId());
            this.mEqualizer.setEnabled(SettingManager.getEqualizer(this));
        }
        this.bands = this.mEqualizer.getNumberOfBands();
        if (this.bands != (short) 0) {
            short[] bandRange = this.mEqualizer.getBandLevelRange();
            if (bandRange != null && bandRange.length >= 2) {
                this.minEQLevel = bandRange[0];
                this.maxEQLevel = bandRange[1];
                for (short i = (short) 0; i < this.bands; i = (short) (i + 1)) {
                    final short band = i;
                    TextView freqTextView = new TextView(this);
                    freqTextView.setLayoutParams(new LayoutParams(-1, -2));
                    freqTextView.setGravity(1);
                    freqTextView.setText(new StringBuilder(String.valueOf(this.mEqualizer.getCenterFreq(band) / 1000)).append(" Hz").toString());
                    // TODO: 8/13/17
                    Log.d("getCenterFreq",String.valueOf(this.mEqualizer.getCenterFreq(band) / 1000));


                    freqTextView.setTextColor(getResources().getColor(R.color.black));
                    freqTextView.setTextSize(2, 16.0f);
                    this.mLayoutBands.addView(freqTextView);
                    LinearLayout row = new LinearLayout(this);
                    row.setOrientation(0);
                    TextView minDbTextView = new TextView(this);
                    minDbTextView.setLayoutParams(new LayoutParams(-2, -2));
                    minDbTextView.setText(new StringBuilder(String.valueOf(this.minEQLevel / 100)).append(" dB").toString());
                    Log.d("minEQLevel",String.valueOf(this.minEQLevel / 100) +"");

                    minDbTextView.setTextColor(getResources().getColor(R.color.black));
                    minDbTextView.setTextSize(2, 16.0f);
                    TextView maxDbTextView = new TextView(this);
                    maxDbTextView.setLayoutParams(new LayoutParams(-2, -2));
                    maxDbTextView.setText(new StringBuilder(String.valueOf(this.maxEQLevel / 100)).append(" dB").toString());
                    Log.d("maxEQLevel",String.valueOf(this.maxEQLevel / 100) +"");

                    maxDbTextView.setTextColor(getResources().getColor(R.color.black));
                    maxDbTextView.setTextSize(2, 16.0f);
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-1, -2);
                    layoutParams.weight = TextTrackStyle.DEFAULT_FONT_SCALE;
                    SeekBar bar = new SeekBar(this);
                    bar.setLayoutParams(layoutParams);
                    bar.setThumb(getResources().getDrawable(R.drawable.blue_scrubber_control_selector_holo_light));
                    bar.setIndeterminateDrawable(getResources().getDrawable(R.drawable.blue_scrubber_progress_horizontal_holo_light));
                    bar.setProgressDrawable(getResources().getDrawable(R.drawable.blue_scrubber_progress_horizontal_holo_light));
                    bar.setMax(this.maxEQLevel - this.minEQLevel);
                    bar.setProgress(this.mEqualizer.getBandLevel(band) - this.minEQLevel);
                    bar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
                        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                            if (fromUser) {
                                EqualizerActivity.this.mEqualizer.setBandLevel(band, (short) (EqualizerActivity.this.minEQLevel + progress));
                                Log.d("setBandLevel",String.valueOf(EqualizerActivity.this.minEQLevel + progress) +"");
                                EqualizerActivity.this.saveEqualizerParams();
                                EqualizerActivity.this.mSpinnerPresents.setSelection(EqualizerActivity.this.mLists.length - 1);
                            }
                        }

                        public void onStartTrackingTouch(SeekBar seekBar) {
                        }

                        public void onStopTrackingTouch(SeekBar seekBar) {
                        }
                    });
                    this.listSeekBars.add(bar);
                    row.addView(minDbTextView);
                    row.addView(bar);
                    row.addView(maxDbTextView);
                    this.mLayoutBands.addView(row);
                }
            }
        }
    }

    private void setUpPresetName() {
        Log.d("setUpPresetName" ,"setUpPresetName1111111111");

        if (this.mEqualizer != null) {
            short numberPreset = this.mEqualizer.getNumberOfPresets();
            if (numberPreset > (short) 0) {
                this.mLists = new String[(numberPreset + 1)];
                for (short i = (short) 0; i < numberPreset; i = (short) (i + 1)) {
                    this.mLists[i] = this.mEqualizer.getPresetName(i);
                }
                this.mLists[numberPreset] = getString(R.string.title_custom);
                PresetAdapter dataAdapter = new PresetAdapter(this, R.layout.item_preset_name, this.mLists, this.mTypefaceNormal);
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                this.mSpinnerPresents.setAdapter(dataAdapter);
                this.mSpinnerPresents.setOnItemSelectedListener(new C05053());
                return;
            }
            this.mSpinnerPresents.setVisibility(4);
            return;
        }
        this.mSpinnerPresents.setVisibility(4);
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode != 4) {
            return super.onKeyDown(keyCode, event);
        }
        finish();
        return true;
    }

    protected void onDestroy() {
        super.onDestroy();
        if (this.listSeekBars != null) {
            this.listSeekBars.clear();
            this.listSeekBars = null;
        }
        if (this.isCreateLocal && this.mMediaPlayer != null) {
            this.mMediaPlayer.release();
            this.mMediaPlayer = null;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.showIntertestialAds();
    }

}
