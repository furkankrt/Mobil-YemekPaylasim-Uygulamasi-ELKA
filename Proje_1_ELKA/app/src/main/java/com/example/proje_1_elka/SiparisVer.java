package com.example.proje_1_elka;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SiparisVer extends AppCompatActivity {

    public String tutSiparisAlanEmail,yemekAdi,yemekFiyati;
    TextView SiparisiAlanEmail,SiparisiVerenEmail,siparisYemekAdi,siparisYemekFiyati,SiparisiAlanIsim;
    EditText editTextTextAdres;
    Button SiparisVer;
    public FirebaseAuth firebaseAuth;
    public FirebaseFirestore firebaseFirestore;
    public FirebaseUser firebaseUser;
    public String tutucuId;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;

    public ArrayList<String> yemekadiFromFB;
    public ArrayList<String> yemekfiyatiFromFB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_siparis_ver);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String email = user.getEmail();

        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();
        firebaseFirestore = FirebaseFirestore.getInstance();


        yemekadiFromFB = new ArrayList<>();
        yemekfiyatiFromFB = new ArrayList<>();

        SiparisiAlanEmail=findViewById(R.id.SiparisiAlanEmail);
        SiparisiVerenEmail=findViewById(R.id.SiparisiVerenEmail);
        siparisYemekAdi=findViewById(R.id.siparisYemekAdi);
        siparisYemekFiyati=findViewById(R.id.siparisYemekFiyati);
        editTextTextAdres=findViewById(R.id.editTextTextAdres);
        SiparisiAlanIsim=findViewById(R.id.SiparisiAlanIsim);
        SiparisVer=findViewById(R.id.SiparisVer);

        tutSiparisAlanEmail = getIntent().getStringExtra("tutSiparisAlanEmail");
        SiparisiAlanEmail.setText("Siparisi Alan E-mail = "+tutSiparisAlanEmail);
        System.out.println("tutSiparisAlanEmail"+tutSiparisAlanEmail);

        yemekAdi = getIntent().getStringExtra("yemekAdi");
        yemekFiyati = getIntent().getStringExtra("yemekFiyati");

        siparisYemekAdi.setText("Yemek Adı = "+yemekAdi);
        siparisYemekFiyati.setText("Yemek Fiyatı = "+yemekFiyati);


        tutucuId = getIntent().getStringExtra("tutucuId");
        SiparisiVerenEmail.setText("Sipariş Veren E-Mail = "+user.getEmail());
        siparisIsimleriCek();

        SiparisVer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(editTextTextAdres.getText().toString().trim().equals(""))
                {
                    Toast.makeText(SiparisVer.this ,"Adres Alanını Boş Bırakmayınız",Toast.LENGTH_LONG).show();
                }
                else if (tutSiparisAlanEmail.equals(user.getEmail()))
                {
                    Toast.makeText(SiparisVer.this ,"Kendinize Sipariş Veremezsiniz ",Toast.LENGTH_LONG).show();
                }

                else
                    {
                        UUID uuid1= UUID.randomUUID();
                        String siparisNo=uuid1.toString();

                        HashMap<String,Object> siparisData =new HashMap<>();
                        siparisData.put("siparisNo",siparisNo);
                        siparisData.put("siparisYemekNo",tutucuId);
                        siparisData.put("SiparisiAlanEmail",SiparisiAlanEmail.getText().toString());
                        siparisData.put("SiparisiVerenEmail",SiparisiVerenEmail.getText().toString());
                        siparisData.put("siparisYemekAdi",siparisYemekAdi.getText().toString());
                        siparisData.put("siparisYemekFiyati",siparisYemekFiyati.getText().toString());
                        siparisData.put("Adres",editTextTextAdres.getText().toString());
                        siparisData.put("siparisTarih", FieldValue.serverTimestamp());

                        firebaseFirestore.collection("siparisler").document(siparisNo).set(siparisData).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(SiparisVer.this, "Sipariş Verildi", Toast.LENGTH_LONG).show();
                                Intent gosiparis=new Intent(SiparisVer.this,Siparisler.class);
                                startActivity(gosiparis);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(SiparisVer.this,e.getLocalizedMessage().toString(),Toast.LENGTH_LONG).show();

                            }
                        });
                    }

            }
        });


    }

    public void siparisIsimleriCek(){
        CollectionReference collectionReference = firebaseFirestore.collection("users");
        collectionReference.whereEqualTo("email",tutSiparisAlanEmail).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {


                if (e != null) {

                    Toast.makeText(SiparisVer.this, e.getLocalizedMessage().toString(), Toast.LENGTH_LONG).show();
                } else {
                    for (DocumentSnapshot snapshots : queryDocumentSnapshots.getDocuments()) {
                        Map<String, Object> data = snapshots.getData();

                        String isim = (String) data.get("name");
                        String soyisim = (String) data.get("surname");
                        System.out.println(isim+" "+soyisim);
                        SiparisiAlanIsim.setText(isim+" "+soyisim);




                    }
                }
            }

        });


    }

}