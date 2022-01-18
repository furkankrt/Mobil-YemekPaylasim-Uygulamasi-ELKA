package com.example.proje_1_elka;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Map;

public class kullaniciFazlaYemek extends AppCompatActivity {
    public String tutEmail;
    public FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    public ImageButton detailedYemek1, detailedYemek2, detailedYemek3, detailedYemek4, detailedYemek5;
    public TextView detailedYemek1TV, detailedYemek2TV, detailedYemek3TV, detailedYemek4TV, detailedYemek5TV;
    public TextView textisim;
    ImageView profilPhoto;

    public ArrayList<String> urlImageYemek;
    public ArrayList<String> adiYemek;
    public ArrayList<String> paylasimNo;

    String profileR;

    ArrayList<String> urlImageProfil;
    ArrayList<String> userProfilisim;
    ArrayList<String> userProfilsoyisim;

    public int tur=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kullanici_fazla_yemek);



        textisim=(TextView) findViewById(R.id.textisim);
        profilPhoto=findViewById(R.id.profilPhoto);
        tutEmail = getIntent().getStringExtra("tutEmail");
        textisim.setText(tutEmail);

        detailedYemek1 = findViewById(R.id.detailedYemek1);
        detailedYemek2 = findViewById(R.id.detailedYemek2);
        detailedYemek3 = findViewById(R.id.detailedYemek3);
        detailedYemek4 = findViewById(R.id.detailedYemek4);
        detailedYemek5 = findViewById(R.id.detailedYemek5);

        detailedYemek1TV = findViewById(R.id.detailedYemek1TV);
        detailedYemek2TV = findViewById(R.id.detailedYemek2TV);
        detailedYemek3TV = findViewById(R.id.detailedYemek3TV);
        detailedYemek4TV = findViewById(R.id.detailedYemek4TV);
        detailedYemek5TV = findViewById(R.id.detailedYemek5TV);

        urlImageYemek = new ArrayList<>();
        adiYemek = new ArrayList<>();
        paylasimNo = new ArrayList<>();

        urlImageProfil = new ArrayList<>();
        userProfilisim = new ArrayList<>();
        userProfilsoyisim = new ArrayList<>();

        firebaseFirestore = FirebaseFirestore.getInstance();
        yemekDetayCek();
        profilbilgisicek();

    }
    public void yemekDetayCek(){

        CollectionReference collectionReference = firebaseFirestore.collection("yemekler");
        collectionReference.whereEqualTo("userEmail",textisim.getText())
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                        if(error !=null)
                        {
                            Toast.makeText(kullaniciFazlaYemek.this,error.getLocalizedMessage().toString(),Toast.LENGTH_LONG);
                        }
                        if(value !=null)
                        {
                            for(DocumentSnapshot snapshot : value.getDocuments())
                            {
                                Map<String, Object> data = snapshot.getData();
                                String paylasimNo=(String) data.get("paylasimNo");
                                String tutUrl = (String) data.get("tutUrl");
                                String yemekAdi=(String) data.get("yemekadi");

                                urlImageYemek.add(tutUrl);
                                adiYemek.add(yemekAdi);


                            }

                            for (tur=0;tur<adiYemek.size();tur++)
                            {

                                if(tur==0)
                                {
                                    Picasso.get().load(urlImageYemek.get(0)).into(detailedYemek1);
                                    detailedYemek1TV.setText(adiYemek.get(0).toString());

                                }
                                if(tur==1)
                                {Picasso.get().load(urlImageYemek.get(1)).into(detailedYemek2);
                                    detailedYemek2TV.setText(adiYemek.get(1).toString());

                                }
                                if(tur==2)
                                {Picasso.get().load(urlImageYemek.get(2)).into(detailedYemek3);
                                    detailedYemek3TV.setText(adiYemek.get(2).toString());

                                }
                                if(tur==3)
                                {Picasso.get().load(urlImageYemek.get(3)).into(detailedYemek4);
                                    detailedYemek4TV.setText(adiYemek.get(3).toString());

                                }
                                if(tur==4)
                                {Picasso.get().load(urlImageYemek.get(4)).into(detailedYemek5);
                                    detailedYemek5TV.setText(adiYemek.get(4).toString());

                                }


                            }


                        }


                    }
                });
    }
    public void profilbilgisicek()
    {
        CollectionReference collectionReference = firebaseFirestore.collection("users");
        collectionReference.whereEqualTo("email",textisim.getText().toString()).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {


                if(e!=null)
                {

                    Toast.makeText(kullaniciFazlaYemek.this, e.getLocalizedMessage().toString(),Toast.LENGTH_LONG).show();
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
                        userProfilisim.add(isim);
                        userProfilsoyisim.add(soyisim);

                        Picasso.get().load(profileR).into(profilPhoto);


                    }

                }

            }
        });



    }
}