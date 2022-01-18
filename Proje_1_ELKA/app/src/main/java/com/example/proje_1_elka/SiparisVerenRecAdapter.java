package com.example.proje_1_elka;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Map;

public class SiparisVerenRecAdapter extends RecyclerView.Adapter<SiparisVerenRecAdapter.SiparisVerenHolder> {

    private ArrayList<String> siparisNoFromFbVerenRec;
    private ArrayList<String> siparisYemekAdiFromFbVerenRec;
    private ArrayList<String> siparisYemekFiyatiFromFbVerenRec;




    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;

    TextView recSiparisVeren;
    TextView recYemekAdiVeren;
    TextView recYemekFiyatiVeren;



    public SiparisVerenRecAdapter(ArrayList<String> siparisNoFromFbVeren, ArrayList<String> siparisYemekAdiFromFbVeren, ArrayList<String> siparisYemekFiyatiFromFbVeren) {
        this.siparisNoFromFbVerenRec= siparisNoFromFbVeren;
        this.siparisYemekAdiFromFbVerenRec = siparisYemekAdiFromFbVeren;
        this.siparisYemekFiyatiFromFbVerenRec = siparisYemekFiyatiFromFbVeren;
    }

    @NonNull

    @Override
    public SiparisVerenHolder onCreateViewHolder(@NonNull  ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater =LayoutInflater.from(parent.getContext());
        View view=layoutInflater.inflate(R.layout.recycler_siparisveren,parent,false);
        return new SiparisVerenRecAdapter.SiparisVerenHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull SiparisVerenRecAdapter.SiparisVerenHolder holder, int position) {

        holder.recSiparisVeren.setText(siparisNoFromFbVerenRec.get(position));
        holder.recYemekAdiVeren.setText(siparisYemekAdiFromFbVerenRec.get(position));
        holder.recYemekFiyatiVeren.setText(siparisYemekFiyatiFromFbVerenRec.get(position));
        firebaseFirestore = FirebaseFirestore.getInstance();
        holder.recYemekAdiVeren.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CollectionReference collectionReference = firebaseFirestore.collection("siparisler");
                collectionReference.whereEqualTo("siparisNo",siparisNoFromFbVerenRec.get(position)).addSnapshotListener(new EventListener<QuerySnapshot>() {
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
                                builder.setTitle("Siparişiniz");
                                builder.setMessage("Siparişi Getiren Kişi = "+SiparisiAlanEmail+"\n" +
                                        "Siparişi Alacak Kişi = "+SiparisiVerenEmail+"\n" +
                                        "Adres = "+Adres+"\n" +
                                        "Sipariş Adı = "+siparisYemekAdi+"\n" +
                                        "Fiyatı = "+siparisYemekFiyati+"\n"
                                        );
                                builder.setNegativeButton("", null);
                                builder.setPositiveButton("Tamam", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                });builder.show();



                            }

                        }

                    }
                });






            }
        });
    }

    @Override
    public int getItemCount() {
        return siparisNoFromFbVerenRec.size();
    }

    class SiparisVerenHolder extends RecyclerView.ViewHolder{
        TextView recSiparisVeren;
        TextView recYemekAdiVeren;
        TextView recYemekFiyatiVeren;

        public SiparisVerenHolder(@NonNull View itemView) {
            super(itemView);
            recSiparisVeren= itemView.findViewById(R.id.recSiparisVeren);
            recYemekAdiVeren= itemView.findViewById(R.id.recKisiAdi);
            recYemekFiyatiVeren= itemView.findViewById(R.id.recChatKisiAdi);


        }
    }
    public void siparisVerenBilgiCek() {





    }

}
