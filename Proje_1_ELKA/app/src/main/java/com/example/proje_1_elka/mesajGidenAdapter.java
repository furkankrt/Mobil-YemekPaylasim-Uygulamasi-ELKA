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

public class mesajGidenAdapter extends RecyclerView.Adapter<mesajGidenAdapter.mesajHolder> {
    private ArrayList<String> mesajFromFb;
    private ArrayList<String> mesajTarihFromFb;


    public mesajGidenAdapter(ArrayList<String> mesajFromFb, ArrayList<String> mesajTarihFromFb) {
        this.mesajFromFb = mesajFromFb;
        this.mesajTarihFromFb = mesajTarihFromFb;

    }
    @NonNull
    @Override
    public mesajGidenAdapter.mesajHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater =LayoutInflater.from(parent.getContext());
        View view=layoutInflater.inflate(R.layout.recgidenmsaj,parent,false);
        return new mesajHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull mesajGidenAdapter.mesajHolder holder, int position) {
        {
            holder.TVmesaj.setText(mesajFromFb.get(position));
            //holder.TVMesajTarih.setText(mesajTarihFromFb.get(position));
        }


    }

    @Override
    public int getItemCount() {
        return mesajFromFb.size();
    }

    class mesajHolder extends RecyclerView.ViewHolder{

          TextView TVmesaj;
          //TextView TVMesajTarih;



        public mesajHolder(@NonNull  View itemView) {
            super(itemView);

              TVmesaj= itemView.findViewById(R.id.TVmesaj);
          // TVMesajTarih= itemView.findViewById(R.id.recYemekAdi);

        }
    }

}
