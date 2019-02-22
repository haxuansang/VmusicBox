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

public class DiscoverVerRecyclerViewAdapter extends RecyclerView.Adapter<DiscoverVerRecyclerViewAdapter.DiscoverVerRecyclerViewHolder>{
    private List<GenreObject> listGenres;
    private Context context;
    private OnclickListener listener;
    public DiscoverVerRecyclerViewAdapter(Context context,List<GenreObject> listGenres){
        this.context=context;
        this.listGenres=listGenres;
    }
    public interface OnclickListener{
        void onClick(int position);
    }
    public void setOnGenresMusicListener(OnclickListener listener){
        this.listener=listener;
    }

    @Override
    public DiscoverVerRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View view=inflater.inflate(R.layout.item_recyclerview_genres,parent,false);
        return new DiscoverVerRecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DiscoverVerRecyclerViewHolder holder, int position) {
        holder.image.setImageResource(getImages().get(position));
        holder.tittle.setText(listGenres.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return  (listGenres == null) ? 0 : listGenres.size();
    }

    public class DiscoverVerRecyclerViewHolder extends RecyclerView.ViewHolder{
        private TextView tittle;
        private ImageView image;
        public DiscoverVerRecyclerViewHolder(View itemView)
        {
            super(itemView);
            tittle=(TextView) itemView.findViewById(R.id.text_view_discover_hiz);
            image=(ImageView) itemView.findViewById(R.id.image_view_discover_hiz);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(listener!=null){
                        listener.onClick(getLayoutPosition());
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

