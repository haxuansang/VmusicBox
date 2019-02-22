package com.mozia.VmusicBox.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mozia.VmusicBox.R;
import com.mozia.VmusicBox.constants.ICloudMusicPlayerConstants;
import com.mozia.VmusicBox.object.ItemDrawerObject;

import java.util.ArrayList;
import java.util.Iterator;

public class DrawerAdapter extends BaseAdapter implements ICloudMusicPlayerConstants {
    public static final String TAG = DrawerAdapter.class.getSimpleName();
    private ArrayList<ItemDrawerObject> listDrawerObjects;
    private Context mContext;
    private Typeface mTypefaceBold;
    private Typeface mTypefaceLight;

    private static class ViewHolder {
        public ImageView mImgIcon;
        public RelativeLayout mLayoutRoot;
        public TextView mTvNameDrawer;

        private ViewHolder() {
        }
    }

    public DrawerAdapter(Context mContext, ArrayList<ItemDrawerObject> listDrawerObjects, Typeface mTypefaceBold, Typeface mTypefaceLight) {
        this.mContext = mContext;
        this.listDrawerObjects = listDrawerObjects;
        this.mTypefaceBold = mTypefaceBold;
        this.mTypefaceLight = mTypefaceLight;
    }

    public int getCount() {
        if (this.listDrawerObjects != null) {
            return this.listDrawerObjects.size();
        }
        return 0;
    }

    public Object getItem(int arg0) {
        if (this.listDrawerObjects != null) {
            return this.listDrawerObjects.get(arg0);
        }
        return null;
    }

    public long getItemId(int arg0) {
        return (long) arg0;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder mHolder;
        if (convertView == null) {
            mHolder = new ViewHolder();
            convertView = ((LayoutInflater) this.mContext.getSystemService("layout_inflater")).inflate(R.layout.item_drawer, null);
            convertView.setTag(mHolder);
            mHolder.mTvNameDrawer = (TextView) convertView.findViewById(R.id.tv_name_setting);
            mHolder.mLayoutRoot = (RelativeLayout) convertView.findViewById(R.id.layout_root);
            mHolder.mImgIcon = (ImageView) convertView.findViewById(R.id.img_icon);
        } else {
            mHolder = (ViewHolder) convertView.getTag();
        }
        ItemDrawerObject mDrawerObject = (ItemDrawerObject) this.listDrawerObjects.get(position);
        mHolder.mImgIcon.setImageResource(mDrawerObject.getIconRes());
        mHolder.mTvNameDrawer.setText(mDrawerObject.getName());
        if (mDrawerObject.isSelected()) {
            mHolder.mTvNameDrawer.setTypeface(this.mTypefaceBold);
            mHolder.mLayoutRoot.setBackgroundColor(this.mContext.getResources().getColor(R.color.background_color_highlight));
        } else {
            mHolder.mTvNameDrawer.setTypeface(this.mTypefaceLight);
            mHolder.mLayoutRoot.setBackgroundColor(0);
        }
        return convertView;
    }

    public void setSelectedDrawer(int pos) {
        if (pos >= 0 && pos < this.listDrawerObjects.size()) {
            Iterator it = this.listDrawerObjects.iterator();
            while (it.hasNext()) {
                ((ItemDrawerObject) it.next()).setSelected(false);
            }
            ((ItemDrawerObject) this.listDrawerObjects.get(pos)).setSelected(true);
            notifyDataSetChanged();
        }
    }

    public ArrayList<ItemDrawerObject> getListDrawerObjects() {
        return this.listDrawerObjects;
    }
}
