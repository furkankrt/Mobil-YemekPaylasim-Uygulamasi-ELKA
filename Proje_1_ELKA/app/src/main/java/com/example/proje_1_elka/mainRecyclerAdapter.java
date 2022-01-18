package com.example.proje_1_elka;

import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class mainRecyclerAdapter extends RecyclerView.Adapter<mainRecyclerAdapter.YemekGoster> {
    private ArrayList<String> userEmailList;
    private ArrayList<String> userYemekisimList;
    private ArrayList<String> userFiyatiList;
    private ArrayList<String> userImgList;


    public mainRecyclerAdapter(ArrayList<String> userEmailList, ArrayList<String> userYemekisimList, ArrayList<String> userFiyatiList, ArrayList<String> userImgList) {
        this.userEmailList = userEmailList;
        this.userYemekisimList = userYemekisimList;
        this.userFiyatiList = userFiyatiList;
        this.userImgList = userImgList;
    }

    @NonNull
    @Override
    public YemekGoster onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater =LayoutInflater.from(parent.getContext());
        View view=layoutInflater.inflate(R.layout.recycler_row,parent,false);
        return new YemekGoster(view);

    }

    @Override
    public void onBindViewHolder(@NonNull YemekGoster holder, int position) {
        holder.rv_tv_fiyati.setText(userFiyatiList.get(position));
        holder.rv_tv_kullanici.setText(userEmailList.get(position));
        holder.rv_tv_yemekismi.setText(userYemekisimList.get(position));
        Picasso.get().load(userImgList.get(position)).into(holder.rv_imageView);

    }

    @Override
    public int getItemCount() {
        return userEmailList.size();

    }

    class YemekGoster extends RecyclerView.ViewHolder{

        ImageView rv_imageView;
        TextView rv_tv_kullanici;
        TextView rv_tv_yemekismi;
        TextView rv_tv_fiyati;

        public YemekGoster(@NonNull View itemView) {
            super(itemView);
            rv_imageView=itemView.findViewById(R.id.rv_imageView);
            rv_tv_kullanici=itemView.findViewById(R.id.rv_tv_kullanici);
            rv_tv_yemekismi=itemView.findViewById(R.id.rv_tv_yemekismi);
            rv_tv_fiyati=itemView.findViewById(R.id.rv_tv_fiyati);

        }
    }
}
