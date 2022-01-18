package com.example.proje_1_elka;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.media.MediaDrm;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ChatActivity extends AppCompatActivity {

RecyclerView recGelenMesaj,recGidenMesaj;
EditText editTextMesaj;
Button mesajGonder;
TextView kisiEmail;
    String profileR;
    ImageView profilPhoto;

    mesajGidenAdapter mesajGidenAdapter;
    mesajGelenAdapter mesajGelenAdapter;



    private ArrayList<String> mesajFromFb;
    private ArrayList<String> mesajGidenFromFb;
    private ArrayList<String> mesajAlanFromFb;
    private ArrayList<String> mesajGonderenFromFb;
    private ArrayList<String> mesajTarihFromFb;
    private ArrayList<String>  urlImageProfil;
    public FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;

    public Intent i;
    public String a;

    private ArrayList<String> GidenmesajFromFb;
    private ArrayList<String> GidenmesajTarihFromFb;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore= FirebaseFirestore.getInstance();

        editTextMesaj=findViewById(R.id.editTextMesaj);
        mesajGonder=findViewById(R.id.mesajGonder);
        kisiEmail=findViewById(R.id.kisiEmail);
        profilPhoto=findViewById(R.id.profilPhoto);


        gidenMesajlar();
        gelenMesajlar();
        //Recyler view
        recGelenMesaj=findViewById(R.id.recGelenMesaj);

        mesajFromFb=new ArrayList<>();
        mesajAlanFromFb=new ArrayList<>();
        mesajGonderenFromFb=new ArrayList<>();
        mesajTarihFromFb=new ArrayList<>();
        mesajGidenFromFb=new ArrayList<>();

        urlImageProfil=new ArrayList<>();
        i=getIntent();
        kisiEmail.setText(i.getStringExtra("eMailTut").toString());
        a=i.getStringExtra("eMailTut").toString();


        //Rec
        RecyclerView recGelenMesaj=findViewById(R.id.recGelenMesaj);
        recGelenMesaj.setLayoutManager(new GridLayoutManager(ChatActivity.this,1));
        mesajGidenAdapter= new mesajGidenAdapter(mesajFromFb,mesajTarihFromFb);
        recGelenMesaj.setAdapter(mesajGidenAdapter);

        //Rec Gelen Mesaj
        RecyclerView recGidenMesaj=findViewById(R.id.recGidenMesaj);
        recGidenMesaj.setLayoutManager(new GridLayoutManager(ChatActivity.this,1));
        mesajGelenAdapter= new mesajGelenAdapter(mesajGidenFromFb);
        recGidenMesaj.setAdapter(mesajGelenAdapter);





        profilbilgisicek();
        i=getIntent();
        kisiEmail.setText(i.getStringExtra("eMailTut").toString());
        a=i.getStringExtra("eMailTut").toString();
        FirebaseUser firebaseUser=firebaseAuth.getCurrentUser();
        if(firebaseUser.getEmail().toString().equals(a.toString()))
        {
            Intent geri=new Intent(ChatActivity.this,deneme.class);
            startActivity(geri);
            finish();
            Toast.makeText(ChatActivity.this ,"Kendinize Mesaj Atamazsınız",Toast.LENGTH_LONG).show();
        }



    }

    public void profilbilgisicek()
    {
        CollectionReference collectionReference = firebaseFirestore.collection("users");
        collectionReference.whereEqualTo("email",kisiEmail.getText().toString()).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {


                if(e!=null)
                {

                    //.makeText(ChatActivity.this, e.getLocalizedMessage().toString(),Toast.LENGTH_LONG).show();
                }
                else
                {
                    for(DocumentSnapshot snapshots:queryDocumentSnapshots.getDocuments())
                    {
                        Map<String,Object> data =snapshots.getData();

                        profileR= (String) data.get("Profil Fotografi");
                        String isim=(String) data.get("name");
                        String soyisim=(String)data.get("surname");

                        urlImageProfil.add(profileR);

                        Picasso.get().load(urlImageProfil.get(0)).into(profilPhoto);



                    }

                }

            }
        });



    }
    public void mesajGonderClick(View view)
    {FirebaseUser firebaseUser=firebaseAuth.getCurrentUser();
        { UUID uuid1= UUID.randomUUID();
            String mesajNo=uuid1.toString();
            if(kisiEmail.getText().toString().trim().equals(""))
            {
                Toast.makeText(ChatActivity.this ,"Mesaj Alanını Boş Bırakmayınız",Toast.LENGTH_LONG).show();
            }


            else
            {


                HashMap<String,Object> mesajData =new HashMap<>();
                mesajData.put("mesajGonderen",firebaseUser.getEmail().toString() );
                mesajData.put("mesajAlan",kisiEmail.getText().toString());
                mesajData.put("mesaj",editTextMesaj.getText().toString());
                mesajData.put("mesajTarih", FieldValue.serverTimestamp());

                firebaseFirestore.collection("mesajlar").document(mesajNo).set(mesajData).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        i=getIntent();
                        kisiEmail.setText(i.getStringExtra("eMailTut").toString());
                        a=i.getStringExtra("eMailTut").toString();
                        Toast.makeText(ChatActivity.this, "Mesaj Verildi", Toast.LENGTH_LONG).show();

                        i=getIntent();
                        kisiEmail.setText(i.getStringExtra("eMailTut").toString());
                        a=i.getStringExtra("eMailTut").toString();

                        HashMap<String,Object> mesajData1 =new HashMap<>();
                        mesajData1.put("mesajGonderen",firebaseUser.getEmail().toString() );
                        mesajData1.put("mesajAlan",a.toString() );

                        firebaseFirestore.collection("users").document(firebaseUser.getEmail()).collection("dahaOnceMesaj").document(firebaseUser.getEmail()).set(mesajData1);

                      // CollectionReference collectionReference1=firebaseFirestore.collection("users").document(firebaseUser.getEmail()).collection("dahaoncemsaj");
                            Intent intent = getIntent();
                            finish();
                            startActivity(intent);


                        editTextMesaj.setText("");
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                       // Toast.makeText(ChatActivity.this,e.getLocalizedMessage().toString(),Toast.LENGTH_LONG).show();

                    }
                });
            }

        }

    }

    public void gidenMesajlar()
    {
        i=getIntent();
        kisiEmail.setText(i.getStringExtra("eMailTut").toString());
        a=i.getStringExtra("eMailTut").toString();
        FirebaseUser firebaseUser=firebaseAuth.getCurrentUser();
        CollectionReference collectionReference = firebaseFirestore.collection("mesajlar");
        collectionReference.orderBy("mesajTarih", Query.Direction.ASCENDING).whereEqualTo("mesajGonderen",firebaseUser.getEmail()).whereEqualTo("mesajAlan",a.toString()).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {


                if(e!=null)
                {

                    //Toast.makeText(ChatActivity.this, e.getLocalizedMessage().toString(),Toast.LENGTH_LONG).show();
                }
                else
                {
                    for(DocumentSnapshot snapshots:queryDocumentSnapshots.getDocuments())
                    {
                        Map<String,Object> data =snapshots.getData();
                        String mesaj=(String) data.get("mesaj");
                        String mesajAlan=(String) data.get("mesajAlan");
                        String mesajGonderen=(String)data.get("mesajGonderen");
                        Timestamp mesajTarih=(Timestamp)data.get("mesajTarih");


                        mesajFromFb.add(mesaj);
                       // mesajTarihFromFb.add(mesajTarih.toString());




                       mesajGidenAdapter.notifyDataSetChanged();




                    }

                }

            }
        });
    }
    public void gelenMesajlar()
    {


        FirebaseUser firebaseUser=firebaseAuth.getCurrentUser();
        CollectionReference collectionReference = firebaseFirestore.collection("mesajlar");
        collectionReference.whereEqualTo("mesajAlan",firebaseUser.getEmail()).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {


                if(e!=null)
                {

                   //Toast.makeText(ChatActivity.this, e.getLocalizedMessage().toString(),Toast.LENGTH_LONG).show();
                }
                else
                {
                    for(DocumentSnapshot snapshots:queryDocumentSnapshots.getDocuments())
                    {
                        Map<String,Object> data =snapshots.getData();
                        String mesaj=(String) data.get("mesaj");
                        String mesajAlan=(String) data.get("mesajAlan");
                        String mesajGonderen=(String)data.get("mesajGonderen");
                        Timestamp mesajTarih=(Timestamp)data.get("mesajTarih");


                        mesajGidenFromFb.add(mesaj);




                       mesajGelenAdapter.notifyDataSetChanged();




                    }

                }

            }
        });
    }
}