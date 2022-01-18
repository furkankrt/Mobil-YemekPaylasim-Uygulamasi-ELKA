package com.example.proje_1_elka;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Map;

public class mesajGelenAdapter extends RecyclerView.Adapter<mesajGelenAdapter.mesajHolder> {
    private ArrayList<String> mesajGelenFromFb;



    public mesajGelenAdapter(ArrayList<String> mesajGelenFromFb) {
        this.mesajGelenFromFb = mesajGelenFromFb;


    }
    @NonNull
    @Override
    public mesajGelenAdapter.mesajHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater =LayoutInflater.from(parent.getContext());
        View view=layoutInflater.inflate(R.layout.recgelenmesaj,parent,false);
        return new mesajHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull mesajGelenAdapter.mesajHolder holder, int position) {
        {

            holder.TVmesaj.setText(mesajGelenFromFb.get(position));

        }


    }

    @Override
    public int getItemCount() {
        return mesajGelenFromFb.size();
    }

    class mesajHolder extends RecyclerView.ViewHolder{

        TextView TVmesaj;




        public mesajHolder(@NonNull  View itemView) {
            super(itemView);

            TVmesaj= itemView.findViewById(R.id.TVmesaj);


        }
    }

}
