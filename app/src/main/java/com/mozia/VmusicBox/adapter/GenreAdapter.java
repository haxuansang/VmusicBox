package com.mozia.VmusicBox.adapter;

import android.app.Activity;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mozia.VmusicBox.R;
import com.mozia.VmusicBox.constants.ICloudMusicPlayerConstants;
import com.mozia.VmusicBox.object.GenreObject;
import com.ypyproductions.abtractclass.DBBaseAdapter;

import java.util.ArrayList;

public class GenreAdapter extends DBBaseAdapter implements ICloudMusicPlayerConstants {
    public static final String TAG = GenreAdapter.class.getSimpleName();
    private Typeface mTypeface;
    protected ArrayList<GenreObject> resultList;

    private static class ViewHolder {
        public TextView mTvGenreName;
        public RelativeLayout mRelativeMain;
        public ImageView mImageViewChoseanMusic;
        private ViewHolder() {
        }
    }

    public GenreAdapter(Activity mContext, ArrayList<GenreObject> listDrawerObjects, Typeface mTypeface) {
        super(mContext, listDrawerObjects);
        this.mTypeface = mTypeface;
    }

    public View getAnimatedView(int position, View convertView, ViewGroup parent) {
        return null;
    }

    public View getNormalView(int position, View convertView, ViewGroup parent) {
        ViewHolder mHolder;
        if (convertView == null) {
            mHolder = new ViewHolder();
            convertView = ((LayoutInflater) this.mContext.getSystemService("layout_inflater")).inflate(R.layout.item_genre, null);
            convertView.setTag(mHolder);
        } else {
            mHolder = (ViewHolder) convertView.getTag();
        }
        mHolder.mRelativeMain = (RelativeLayout)  convertView.findViewById(R.id.main_item_genre);
        mHolder.mTvGenreName = (TextView) convertView.findViewById(R.id.tv_genre_name);
        mHolder.mTvGenreName.setText(((GenreObject) this.mListObjects.get(position)).getName());
        mHolder.mTvGenreName.setTypeface(this.mTypeface);
        mHolder.mImageViewChoseanMusic = (ImageView) convertView.findViewById(R.id.img_avatar_new);

        int position1 = Integer.parseInt(((GenreObject) this.mListObjects.get(position)).getId().trim());


        if ( position == 0)
        {
            mHolder.mRelativeMain.setBackgroundResource(R.drawable.bg_blue_1_list_selector);
            mHolder.mImageViewChoseanMusic.setImageResource(R.mipmap.ic_popular);
        }
        else if ( position == 1)
        {
            mHolder.mRelativeMain.setBackgroundResource(R.drawable.bg_blue_2_list_selector);
            mHolder.mImageViewChoseanMusic.setImageResource(R.mipmap.ic_alternativerock);
        }
        else if ( position == 2)
        {
            //todo image not size memory
            mHolder.mRelativeMain.setBackgroundResource(R.drawable.bg_blue_3_list_selector);

            mHolder.mImageViewChoseanMusic.setImageResource(R.mipmap.ic_ambient_1);
        }
        else if (position == 3)
        {
            mHolder.mRelativeMain.setBackgroundResource(R.drawable.bg_blue_4_list_selector);
            mHolder.mImageViewChoseanMusic.setImageResource(R.mipmap.ic_classical);
        }
        else if (position == 4)
        {
            mHolder.mRelativeMain.setBackgroundResource(R.drawable.bg_blue_5_list_selector);

            mHolder.mImageViewChoseanMusic.setImageResource(R.mipmap.ic_country);
        }

        else if (position == 5)
        {
            mHolder.mRelativeMain.setBackgroundResource(R.drawable.bg_blue_6_list_selector);

            mHolder.mImageViewChoseanMusic.setImageResource(R.mipmap.ic_dance);
        }

        else if (position == 6)
        {
            mHolder.mRelativeMain.setBackgroundResource(R.drawable.bg_blue_7_list_selector);

            mHolder.mImageViewChoseanMusic.setImageResource(R.mipmap.ic_deephouse);
        }
        else if (position == 7)
        {
            mHolder.mRelativeMain.setBackgroundResource(R.drawable.bg_blue_8_list_selector);

            mHolder.mImageViewChoseanMusic.setImageResource(R.mipmap.ic_disco);
        }

        else if (position == 8)
        {
            mHolder.mRelativeMain.setBackgroundResource(R.drawable.bg_blue_9_list_selector);

            mHolder.mImageViewChoseanMusic.setImageResource(R.mipmap.ic_db);
        }

        else if (position == 9)
        {
            mHolder.mRelativeMain.setBackgroundResource(R.drawable.bg_blue_10_list_selector);

            mHolder.mImageViewChoseanMusic.setImageResource(R.mipmap.ic_dubstep);
        }
        else if (position == 10)
        {
            mHolder.mRelativeMain.setBackgroundResource(R.drawable.bg_blue_11_list_selector);

            mHolder.mImageViewChoseanMusic.setImageResource(R.mipmap.ic_electro);
        }
        else if (position == 11)
        {
            mHolder.mRelativeMain.setBackgroundResource(R.drawable.bg_blue_12_list_selector);

            mHolder.mImageViewChoseanMusic.setImageResource(R.mipmap.ic_electronic);
        }
        else if (position == 12)
        {
            mHolder.mRelativeMain.setBackgroundResource(R.drawable.bg_blue_13_list_selector);

            mHolder.mImageViewChoseanMusic.setImageResource(R.mipmap.ic_folk);
        }
        else if (position == 13)
        {
            mHolder.mRelativeMain.setBackgroundResource(R.drawable.bg_blue_14_list_selector);

            mHolder.mImageViewChoseanMusic.setImageResource(R.mipmap.ic_hardcore);
        }
        else if (position == 14)
        {
            mHolder.mRelativeMain.setBackgroundResource(R.drawable.bg_blue_15_list_selector);

            mHolder.mImageViewChoseanMusic.setImageResource(R.mipmap.ic_hiphop);
        }
        else if (position == 15)
        {
            mHolder.mRelativeMain.setBackgroundResource(R.drawable.bg_blue_16_list_selector);

            mHolder.mImageViewChoseanMusic.setImageResource(R.mipmap.ic_house);
        }
        else if (position == 16)
        {
            mHolder.mRelativeMain.setBackgroundResource(R.drawable.bg_blue_17_list_selector);

            mHolder.mImageViewChoseanMusic.setImageResource(R.mipmap.ic_indierock);
        }
        else if (position == 17)
        {
            mHolder.mRelativeMain.setBackgroundResource(R.drawable.bg_blue_18_list_selector);

            mHolder.mImageViewChoseanMusic.setImageResource(R.mipmap.ic_jazz);
        }
        else if (position == 18)
        {
            mHolder.mRelativeMain.setBackgroundResource(R.drawable.bg_blue_19_list_selector);

            mHolder.mImageViewChoseanMusic.setImageResource(R.mipmap.ic_latin);
        }
        else if (position == 19)
        {
            mHolder.mRelativeMain.setBackgroundResource(R.drawable.bg_blue_20_list_selector);

            mHolder.mImageViewChoseanMusic.setImageResource(R.mipmap.ic_metal);
        }
        else if (position == 20)
        {
            mHolder.mRelativeMain.setBackgroundResource(R.drawable.bg_blue_21_list_selector);

            mHolder.mImageViewChoseanMusic.setImageResource(R.mipmap.ic_minimaltechno);
        }

        else if (position == 21)
        {
            mHolder.mRelativeMain.setBackgroundResource(R.drawable.bg_blue_22_list_selector);

            mHolder.mImageViewChoseanMusic.setImageResource(R.mipmap.ic_piano);
        }
        else if (position == 22)
        {
            mHolder.mRelativeMain.setBackgroundResource(R.drawable.bg_blue_23_list_selector);

            mHolder.mImageViewChoseanMusic.setImageResource(R.mipmap.ic_pop);
        }
        else if (position == 23)
        {
            mHolder.mRelativeMain.setBackgroundResource(R.drawable.bg_blue_24_list_selector);

            mHolder.mImageViewChoseanMusic.setImageResource(R.mipmap.ic_progressive);
        }
        else if (position == 24)
        {
            mHolder.mRelativeMain.setBackgroundResource(R.drawable.bg_blue_25_list_selector);

            mHolder.mImageViewChoseanMusic.setImageResource(R.mipmap.ic_punk);
        }
        else if (position == 25)
        {
            mHolder.mRelativeMain.setBackgroundResource(R.drawable.bg_blue_26_list_selector);

            mHolder.mImageViewChoseanMusic.setImageResource(R.mipmap.ic_rb);
        }
        else if (position == 26)
        {
            mHolder.mRelativeMain.setBackgroundResource(R.drawable.bg_blue_27_list_selector);

            mHolder.mImageViewChoseanMusic.setImageResource(R.mipmap.ic_rap);
        }
        else if (position == 27)
        {
            mHolder.mRelativeMain.setBackgroundResource(R.drawable.bg_blue_28_list_selector);

            mHolder.mImageViewChoseanMusic.setImageResource(R.mipmap.ic_reggae);
        }
        else if (position == 28)
        {
            mHolder.mRelativeMain.setBackgroundResource(R.drawable.bg_blue_29_list_selector);

            mHolder.mImageViewChoseanMusic.setImageResource(R.mipmap.ic_rock);
        }
        else if (position == 29)
        {
            mHolder.mRelativeMain.setBackgroundResource(R.drawable.bg_blue_30_list_selector);

            mHolder.mImageViewChoseanMusic.setImageResource(R.mipmap.ic_soul);
        }
        else if (position == 30)
        {
            mHolder.mRelativeMain.setBackgroundResource(R.drawable.bg_blue_31_list_selector);

            mHolder.mImageViewChoseanMusic.setImageResource(R.mipmap.ic_techhouse);
        }
        else if (position == 31)
        {
            mHolder.mRelativeMain.setBackgroundResource(R.drawable.bg_blue_32_list_selector);

            mHolder.mImageViewChoseanMusic.setImageResource(R.mipmap.ic_techno);
        }
        else if (position == 32)
        {
            mHolder.mRelativeMain.setBackgroundResource(R.drawable.bg_blue_33_list_selector);

            mHolder.mImageViewChoseanMusic.setImageResource(R.mipmap.ic_tran);
        }
        else if (position == 33)
        {
            mHolder.mRelativeMain.setBackgroundResource(R.drawable.bg_blue_34_list_selector);

            // TODO: 8/3/17  fix for trap
            mHolder.mImageViewChoseanMusic.setImageResource(R.mipmap.ic_trap);
        }
        else if (position == 34)
        {
            mHolder.mRelativeMain.setBackgroundResource(R.drawable.bg_blue_35_list_selector);

            mHolder.mImageViewChoseanMusic.setImageResource(R.mipmap.ic_triphop);
        }
        else if (position == 35)
        {
            mHolder.mRelativeMain.setBackgroundResource(R.drawable.bg_blue_36_list_selector);

            mHolder.mImageViewChoseanMusic.setImageResource(R.mipmap.ic_world);
        }
//        if(position1 % 5 == 1)
//            mHolder.mRelativeMain.setBackgroundResource(R.drawable.bg_blue_1_list_selector);
//        else if(position1 % 5 == 2)
//            mHolder.mRelativeMain.setBackgroundResource(R.drawable.bg_blue_2_list_selector);
//        else if(position1 % 5 == 3)
//            mHolder.mRelativeMain.setBackgroundResource(R.drawable.bg_blue_3_list_selector);
//        else if(position1 % 5 == 4)
//            mHolder.mRelativeMain.setBackgroundResource(R.drawable.bg_blue_4_list_selector);
//        else if(position1 % 5 == 0)
//            mHolder.mRelativeMain.setBackgroundResource(R.drawable.bg_blue_5_list_selector);
        return convertView;
    }
}
