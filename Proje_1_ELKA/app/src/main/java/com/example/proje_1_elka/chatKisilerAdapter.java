package com.example.proje_1_elka;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.view.menu.MenuView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class chatKisilerAdapter extends RecyclerView.Adapter<chatKisilerAdapter.chatKisilerHolder> {

    private FirebaseFirestore firebaseFirestore;

    private ArrayList<String> kisiEmailFromFbRec;
    private ArrayList<String> kisiIsimFromFbRec;
    private ArrayList<String> kisiFotografFromFbRec;

    private chatKisilerAdapter.RecyclerViewClickListener listener;

    TextView recKisiEmail;
    TextView recChatKisiAdi;
    ImageView recKisiFotografi;




    public chatKisilerAdapter(ArrayList<String> kisiEmailFromFbRec, ArrayList<String> kisiIsimFromFbRec, ArrayList<String> kisiFotografFromFbRec,chatKisilerAdapter.RecyclerViewClickListener listener) {
        this.kisiEmailFromFbRec = kisiEmailFromFbRec;
        this.kisiIsimFromFbRec = kisiIsimFromFbRec;
        this.kisiFotografFromFbRec = kisiFotografFromFbRec;
        this.listener=listener;
    }
    @NonNull
    @Override
    public chatKisilerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater =LayoutInflater.from(parent.getContext());
        View view=layoutInflater.inflate(R.layout.recycler_chatkisiler,parent,false);

        return new chatKisilerHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull chatKisilerAdapter.chatKisilerHolder holder, int position) {
        holder.recKisiEmail.setText(kisiEmailFromFbRec.get(position));
        holder.recChatKisiAdi.setText(kisiIsimFromFbRec.get(position));
        Picasso.get().load(kisiFotografFromFbRec.get(position)).into(holder.recKisiFotografi);
        firebaseFirestore = FirebaseFirestore.getInstance();

    }



    @Override
    public int getItemCount() {
        return kisiEmailFromFbRec.size();
    }

    class chatKisilerHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView recKisiEmail;
        TextView recChatKisiAdi;
        ImageView recKisiFotografi;


        public chatKisilerHolder(@NonNull  View itemView) {
            super(itemView);

            recKisiEmail= itemView.findViewById(R.id.recKisiEmail);
            recChatKisiAdi= itemView.findViewById(R.id.recChatKisiAdi);
            recKisiFotografi= itemView.findViewById(R.id.recKisiFotografi);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            listener.onClick(v, getAdapterPosition());


        }
    }

    public interface RecyclerViewClickListener {void onClick(View v,int position);}

}
