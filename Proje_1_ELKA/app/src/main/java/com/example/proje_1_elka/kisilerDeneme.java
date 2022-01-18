package com.example.proje_1_elka;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.proje_1_elka.R;
import com.example.proje_1_elka.chatKisilerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Map;

public class kisilerDeneme extends AppCompatActivity {


    private ArrayList<String> kisiEmailFromFbRec;
    private ArrayList<String> kisiIsimFromFbRec;
    private ArrayList<String> kisiFotografFromFbRec;
    public FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;

    chatKisilerAdapter chatKisilerAdapter;
    RecyclerView kisiler_listesi;
    private chatKisilerAdapter.RecyclerViewClickListener listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kisiler_deneme);



        kisiEmailFromFbRec=new ArrayList<>();
        kisiIsimFromFbRec=new ArrayList<>();
        kisiFotografFromFbRec=new ArrayList<>();

        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();



        kisileriGetir();
        setOnClickListener();


        kisiler_listesi=findViewById(R.id.kisiler_listesi);
        kisiler_listesi.setLayoutManager(new LinearLayoutManager(kisilerDeneme.this));
        chatKisilerAdapter=new chatKisilerAdapter(kisiEmailFromFbRec,kisiIsimFromFbRec,kisiFotografFromFbRec,listener);
        kisiler_listesi.setAdapter(chatKisilerAdapter);
    }

    public void kisileriGetir ()
    {
        
        CollectionReference collectionReference= firebaseFirestore.collection("users");
        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if(e!=null)
                {
                    //Toast.makeText(KisilerFragment.this,e.getLocalizedMessage().toString(),Toast.LENGTH_LONG).show();
                }
                if(queryDocumentSnapshots!=null)
                {
                    for(DocumentSnapshot snapshots:queryDocumentSnapshots.getDocuments())
                    {

                        Map<String,Object> data =snapshots.getData();
                        String Email=(String) data.get("email");
                        String Isim=(String) data.get("name")+" "+data.get("surname");
                        String fotourlgetir=(String)data.get("Profil Fotografi");
                        int i =0;


                        kisiEmailFromFbRec.add(Email);
                        kisiIsimFromFbRec.add(Isim);
                        kisiFotografFromFbRec.add(fotourlgetir);
                        System.out.println(Email);

                        chatKisilerAdapter.notifyDataSetChanged();



                    }
                }




            }
        });






    }

    private void setOnClickListener()
    {
        listener = new chatKisilerAdapter.RecyclerViewClickListener() {
            @Override
            public void onClick(View v, int position) {
                Intent goMesaj=new Intent(kisilerDeneme.this,ChatActivity.class);
                goMesaj.putExtra("eMailTut",kisiEmailFromFbRec.get(position));

                startActivity(goMesaj);

            }
        };
    }
}