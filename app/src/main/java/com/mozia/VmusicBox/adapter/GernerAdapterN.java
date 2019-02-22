package com.mozia.VmusicBox.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mozia.VmusicBox.R;
import com.mozia.VmusicBox.object.GenreObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mozia on 10/16/17.
 */

public class GernerAdapterN extends RecyclerView.Adapter<GernerAdapterN.CustomViewHolder> {
    Context mContext;
    List<GenreObject> mList;
    private OnItemClickListener listener;

    public GernerAdapterN(Context context, List<GenreObject> list) {
        this.mList = list;
        this.mContext = context;
    }

    // Define the method that allows the parent activity or fragment to define the listener
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }


    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_genre_new, null);
        CustomViewHolder viewHolder = new CustomViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(CustomViewHolder customViewHolder, int i) {
        GenreObject object = mList.get(i);


        //Setting text view title
        customViewHolder.textView.setText(object.getName());
        customViewHolder.imageView.setImageResource(getImages().get(i));
    }


    @Override
    public int getItemCount() {
        return (null != mList ? mList.size() : 0);
    }

    // Define the listener interface
    public interface OnItemClickListener {
        void onItemClick(View itemView, int position);
    }


    class CustomViewHolder extends RecyclerView.ViewHolder {
        protected ImageView imageView;
        protected TextView textView;

        public CustomViewHolder(View view) {
            super(view);
            this.imageView = (ImageView) view.findViewById(R.id.ivItemGenner);
            this.textView = (TextView) view.findViewById(R.id.tvItemGener);
            // Setup the click listener
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Triggers click upwards to the adapter on click
                    if (listener != null) {
                        // gets item position
                        int position = getAdapterPosition();
                        // Check if an item was deleted, but the user clicked it before the UI removed it
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(itemView, position);
                        }
                    }
                }
            });

        }
    }







    public static ArrayList<Integer> getImages(){
        ArrayList<Integer> arrs=new ArrayList<>();
        arrs.add(R.drawable.g_popular);
        arrs.add(R.drawable.g_alternativerock);
        arrs.add(R.drawable.g_ambient);
        arrs.add(R.drawable.g_classical);
        arrs.add(R.drawable.g_country);
        arrs.add(R.drawable.g_dance);
        arrs.add(R.drawable.g_deephouse);
        arrs.add(R.drawable.g_disco);
        arrs.add(R.drawable.g_db);
        arrs.add(R.drawable.g_dubstep);
        arrs.add(R.drawable.g_electro);
        arrs.add(R.drawable.g_electronic);
        arrs.add(R.drawable.g_folk);
        arrs.add(R.drawable.g_hardcore);
        arrs.add(R.drawable.g_hiphop);
        arrs.add(R.drawable.g_house);
        arrs.add(R.drawable.g_indierock);
        arrs.add(R.drawable.g_jazz);
        arrs.add(R.drawable.g_latin);
        arrs.add(R.drawable.g_metal);
        arrs.add(R.drawable.g_minimaltechno);
        arrs.add(R.drawable.g_piano);
        arrs.add(R.drawable.g_pop);
        arrs.add(R.drawable.g_progressive);
        arrs.add(R.drawable.g_punk);
        arrs.add(R.drawable.g_rb);
        arrs.add(R.drawable.g_rap);
        arrs.add(R.drawable.g_reggae);
        arrs.add(R.drawable.g_rock);
        arrs.add(R.drawable.g_soul);
        arrs.add(R.drawable.g_techhouse);
        arrs.add(R.drawable.g_techno);
        arrs.add(R.drawable.g_tran);
        arrs.add(R.drawable.g_trap);
        arrs.add(R.drawable.g_triphop);
        arrs.add(R.drawable.g_world);
        return arrs;
    }

}
