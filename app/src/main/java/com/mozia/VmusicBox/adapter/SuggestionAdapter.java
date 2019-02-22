package com.mozia.VmusicBox.adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.CursorAdapter;
import android.widget.TextView;

import com.mozia.VmusicBox.R;
import com.mozia.VmusicBox.constants.ICloudMusicPlayerConstants;

import java.util.ArrayList;

public class SuggestionAdapter extends CursorAdapter implements ICloudMusicPlayerConstants {
    public static final String TAG = SuggestionAdapter.class.getSimpleName();
    private ArrayList<String> mListItems;

    public SuggestionAdapter(Context context, Cursor c, ArrayList<String> items) {
        super(context, c, false);
        this.mListItems = items;
    }

    public void bindView(View view, Context context, Cursor cursor) {
        ((TextView) view.findViewById(R.id.tv_name_options)).setText((CharSequence) this.mListItems.get(cursor.getPosition()));
    }

    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        if (cursor.getPosition() < 0 || cursor.getPosition() >= this.mListItems.size()) {
            return null;
        }
        View view = ((LayoutInflater) context.getSystemService("layout_inflater")).inflate(R.layout.item_suggestion, parent, false);
        ((TextView) view.findViewById(R.id.tv_name_options)).setText((CharSequence) this.mListItems.get(cursor.getPosition()));
        return view;
    }
}
