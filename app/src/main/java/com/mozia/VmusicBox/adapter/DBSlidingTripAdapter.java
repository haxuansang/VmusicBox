package com.mozia.VmusicBox.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.ypyproductions.abtractclass.fragment.DBFragmentAdapter;

import java.util.ArrayList;

public class DBSlidingTripAdapter extends DBFragmentAdapter {
    private ArrayList<String> mListTitles;

    public DBSlidingTripAdapter(FragmentManager fm, ArrayList<Fragment> listFragments, ArrayList<String> mListTitles) {
        super(fm, listFragments);
        this.mListTitles = mListTitles;
    }

    public CharSequence getPageTitle(int position) {
        if (this.mListTitles == null || this.mListTitles.size() <= 0 || position >= this.mListTitles.size() || position < 0) {
            return super.getPageTitle(position);
        }
        return (CharSequence) this.mListTitles.get(position);
    }
}
