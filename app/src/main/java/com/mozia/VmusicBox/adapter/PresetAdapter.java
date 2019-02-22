package com.mozia.VmusicBox.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.mozia.VmusicBox.R;

public class PresetAdapter extends ArrayAdapter<String> {
    private Context mContext;
    private String[] mListString;
    private Typeface mTypeFace;

    private static class ViewHolder {
        public TextView mTvName;

        private ViewHolder() {
        }
    }

    public PresetAdapter(Context context, int resource, String[] objects, Typeface mTypeFace) {
        super(context, resource, objects);
        this.mContext = context;
        this.mListString = objects;
        this.mTypeFace = mTypeFace;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder mHolder;
        if (convertView == null) {
            mHolder = new ViewHolder();
            convertView = ((LayoutInflater) this.mContext.getSystemService("layout_inflater")).inflate(R.layout.item_preset_name, null);
            convertView.setTag(mHolder);
        } else {
            mHolder = (ViewHolder) convertView.getTag();
        }
        mHolder.mTvName = (TextView) convertView.findViewById(R.id.tv_name);
        mHolder.mTvName.setText(this.mListString[position]);
        mHolder.mTvName.setTypeface(this.mTypeFace);
        return convertView;
    }
}
