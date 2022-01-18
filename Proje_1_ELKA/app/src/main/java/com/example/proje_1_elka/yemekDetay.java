package com.example.proje_1_elka;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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

public class yemekDetay extends AppCompatActivity {

    public FirebaseAuth firebaseAuth;
    public FirebaseFirestore firebaseFirestore;

    public ArrayList<String> userEmailFromFB;
    public ArrayList<String> yemekadiFromFB;
    public ArrayList<String> yemekfiyatiFromFB;
    public ArrayList<String> urlImageFromFB;
    public ArrayList<String> TarihFromFB;
    public String tutucuId;
    public String useremailTutucu,yemekAdi,yemekFiyati;

    TextView yemekİsmidetay, emaildetay, yemekfiyatidetay, tutId;
    ImageView imageView2;
    Button ara,buttonSiparis,buttonMesaj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yemek_detay);


        yemekİsmidetay = findViewById(R.id.yemekİsmidetay);
        emaildetay = findViewById(R.id.emaildetay);
        yemekfiyatidetay = findViewById(R.id.fiyatidetay);
        ara=findViewById(R.id.buttonAra);
        imageView2 = findViewById(R.id.imageView2);
        buttonSiparis=findViewById(R.id.buttonSiparis);
        buttonMesaj=findViewById(R.id.buttonMesaj);

        tutId = findViewById(R.id.tutId);
        tutucuId = getIntent().getStringExtra("paylasimNo");
        tutId.setText(tutucuId);


        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        userEmailFromFB = new ArrayList<>();
        yemekadiFromFB = new ArrayList<>();
        yemekfiyatiFromFB = new ArrayList<>();
        urlImageFromFB = new ArrayList<>();
        TarihFromFB = new ArrayList<>();

        ara.setBackgroundResource(R.drawable.effect1);


        yemekbilgisicek();
        ara.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("156");
                System.out.println(useremailTutucu);
                CollectionReference collectionReference = firebaseFirestore.collection("users");
                collectionReference.whereEqualTo("email",useremailTutucu).addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            System.out.println("256");

                            Toast.makeText(yemekDetay.this, e.getLocalizedMessage().toString(), Toast.LENGTH_LONG).show();
                        }
                        else
                            {
                            for (DocumentSnapshot snapshots : queryDocumentSnapshots.getDocuments())
                            {
                                System.out.println("456");
                                Map<String, Object> data = snapshots.getData();

                                String phone=(String) data.get("Phone");
                                System.out.println(phone);
                                Intent ara =new Intent(Intent.ACTION_DIAL);
                                ara.setData(Uri.parse("tel:"+phone));
                                startActivity(ara);

                            }

                        }

                    }
                });


            }//ARAMAM BÖLÜMÜ

        });

        emaildetay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String tutEmail =emaildetay.getText().toString();
                Intent i= new Intent(yemekDetay.this,kullaniciFazlaYemek.class);
                i.putExtra("tutEmail",tutEmail);
                AlertDialog.Builder builder = new AlertDialog.Builder(yemekDetay.this);
                builder.setTitle("ELKA");
                builder.setMessage("Kullanıcının diğer yemeklerini görüntülemek ister misiniz?");
                builder.setNegativeButton("Hayır", null);
                builder.setPositiveButton("Evet", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(i);
                    }
                });builder.show();
            {

            }


            }
        });

        buttonSiparis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String tutSiparisAlanEmail =emaildetay.getText().toString();
                Intent i= new Intent(yemekDetay.this,SiparisVer.class);
                i.putExtra("tutSiparisAlanEmail",tutSiparisAlanEmail);
                i.putExtra("tutucuId",tutucuId.toString());
                i.putExtra("yemekAdi",yemekİsmidetay.getText());
                i.putExtra("yemekFiyati",yemekfiyatidetay.getText());

                startActivity(i);


            }
        });

        buttonMesaj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goMesaj=new Intent(yemekDetay.this,ChatActivity.class);
                goMesaj.putExtra("eMailTut",emaildetay.getText().toString());

                startActivity(goMesaj);
            }
        });






    }

    public void yemekbilgisicek() {
        CollectionReference collectionReference = firebaseFirestore.collection("yemekler");
        collectionReference.whereEqualTo("paylasimNo", tutucuId).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {


                if (e != null) {

                    Toast.makeText(yemekDetay.this, e.getLocalizedMessage().toString(), Toast.LENGTH_LONG).show();
                } else {
                    for (DocumentSnapshot snapshots : queryDocumentSnapshots.getDocuments()) {
                        Map<String, Object> data = snapshots.getData();

                        String useremail = (String) data.get("userEmail");
                        String yemekadigetir = (String) data.get("yemekadi");//çevirdik objeyi string olarak yaptık
                        String yemekfiyatigetir = (String) data.get("yemekfiyati");
                        String Urlgetir = (String) data.get("tutUrl");

                        userEmailFromFB.add(useremail);
                        yemekadiFromFB.add(yemekadigetir);
                        yemekfiyatiFromFB.add(yemekfiyatigetir);


                        Picasso.get().load(Urlgetir).into(imageView2);
                        yemekİsmidetay.setText(yemekadigetir);
                        System.out.println(useremail+"1");
                        yemekfiyatidetay.setText(yemekfiyatigetir);
                        emaildetay.setText(useremail);
                        useremailTutucu=useremail;
                        System.out.println(useremailTutucu);
                    }
                }
            }

        });


    }


    }
