package com.example.proje_1_elka;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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

public class SiparisAlanRecAdapter extends RecyclerView.Adapter<SiparisAlanRecAdapter.SiparisAlanHolder> {

    private FirebaseFirestore firebaseFirestore;

    private ArrayList<String> siparisNoFromFbRec;
    private ArrayList<String> siparisYemekAdiFromFbRec;
    private ArrayList<String> siparisYemekFiyatiFromFbRec;

    public SiparisAlanRecAdapter(ArrayList<String> siparisNoFromFbRec, ArrayList<String> siparisYemekAdiFromFbRec, ArrayList<String> siparisYemekFiyatiFromFbRec) {
        this.siparisNoFromFbRec = siparisNoFromFbRec;
        this.siparisYemekAdiFromFbRec = siparisYemekAdiFromFbRec;
        this.siparisYemekFiyatiFromFbRec = siparisYemekFiyatiFromFbRec;
    }

    public SiparisAlanRecAdapter(ArrayList<String> gidenmesajGonderenFromFb, ArrayList<String> gidenmesajTarihFromFb) {
    }


    @Override
    public SiparisAlanHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater =LayoutInflater.from(parent.getContext());
        View view=layoutInflater.inflate(R.layout.recycler_siparisalan,parent,false);
        return new SiparisAlanHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SiparisAlanRecAdapter.SiparisAlanHolder holder, int position) {

        holder.recSiparis.setText(siparisNoFromFbRec.get(position));
        holder.recYemekAdi.setText(siparisYemekAdiFromFbRec.get(position));
        holder.recYemekFiyati.setText(siparisYemekFiyatiFromFbRec.get(position));
        firebaseFirestore = FirebaseFirestore.getInstance();
        holder.recYemekAdi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CollectionReference collectionReference = firebaseFirestore.collection("siparisler");
                collectionReference.whereEqualTo("siparisNo",siparisNoFromFbRec.get(position)).addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                        if(e!=null)
                        {

                            //Toast.makeText(Siparisler.this, e.getLocalizedMessage().toString(),Toast.LENGTH_LONG).show();
                        }
                        else
                        {
                            for(DocumentSnapshot snapshots:queryDocumentSnapshots.getDocuments())
                            {
                                Map<String,Object> data =snapshots.getData();
                                String siparisNo=(String) data.get("siparisNo");
                                String SiparisiAlanEmail=(String) data.get("SiparisiAlanEmail");
                                String SiparisiVerenEmail=(String)data.get("SiparisiVerenEmail");
                                String Adres=(String)data.get("Adres");
                                String siparisYemekAdi=(String)data.get("siparisYemekAdi");
                                String siparisYemekFiyati=(String)data.get("siparisYemekFiyati");
                                Timestamp siparisTarih=(Timestamp)data.get("siparisTarih");






                                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                                builder.setTitle("Siparişleriniz");
                                builder.setMessage("Siparişi Getiren Kişi = "+SiparisiAlanEmail+"\n" +
                                        "Siparişi Alacak Kişi = "+SiparisiVerenEmail+"\n" +
                                        "Adres = "+Adres+"\n" +
                                        "Sipariş Adı = "+siparisYemekAdi+"\n" +
                                        "Fiyatı = "+siparisYemekFiyati+"\n"
                                );
                                builder.setNegativeButton("Tamam", null);
                                builder.setPositiveButton("Teslim Edildi", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        firebaseFirestore.collection("siparisler").document(siparisNoFromFbRec.get(position)).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                Toast.makeText(v.getContext(), "Sipariş Başarıyla Teslim Edildi",Toast.LENGTH_LONG).show();

                                            }
                                        });
                                    }
                                });
                                builder.show();



                            }

                        }

                    }
                });






            }
        });
    }

    @Override
    public int getItemCount() {
        return siparisNoFromFbRec.size();
    }

    class SiparisAlanHolder extends RecyclerView.ViewHolder{

        TextView recSiparis;
        TextView recYemekAdi;
        TextView recYemekFiyati;


        public SiparisAlanHolder(@NonNull  View itemView) {
            super(itemView);

            recSiparis= itemView.findViewById(R.id.recSiparis);
            recYemekAdi= itemView.findViewById(R.id.recYemekAdi);
            recYemekFiyati= itemView.findViewById(R.id.recYemekFiyati);
        }
    }
}
