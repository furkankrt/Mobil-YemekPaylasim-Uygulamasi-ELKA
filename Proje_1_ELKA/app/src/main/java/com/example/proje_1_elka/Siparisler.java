package com.example.proje_1_elka;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
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
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Map;

public class Siparisler extends AppCompatActivity {
    DrawerLayout drawerLayout;
    ImageView nav_drawer_logo,imageView3;

    public SiparisAlanRecAdapter siparisAlanRecAdapter;
    public SiparisVerenRecAdapter siparisVerenRecAdapter;

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    TextView navDraweremail, navDrawerisim;
    String profileR;
    ArrayList<String> urlImageProfil;
    ArrayList<String> userProfilisim;
    ArrayList<String> userProfilsoyisim;

     //ALAN
     ArrayList<String> SiparisiAlanEmailFromFb;
     ArrayList<String> SiparisiVerenEmailFromFb;
     ArrayList<String> AdresFromFb;
     ArrayList<String> siparisYemekAdiFromFb;
     ArrayList<String> siparisYemekFiyatiFromFb;
     ArrayList<String> siparisNoFromFb;

    //Veren
    ArrayList<String> SiparisiAlanEmailFromFbVeren;
    ArrayList<String> SiparisiVerenEmailFromFbVeren;
    ArrayList<String> AdresFromFbVeren;
    ArrayList<String> siparisYemekAdiFromFbVeren;
    ArrayList<String> siparisYemekFiyatiFromFbVeren;
    ArrayList<String> siparisNoFromFbVeren;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_siparisler);



        drawerLayout = findViewById(R.id.drawer_layout);
        urlImageProfil = new ArrayList<>();
        userProfilisim = new ArrayList<>();
        userProfilsoyisim = new ArrayList<>();


        siparisNoFromFb = new ArrayList<>();
        SiparisiAlanEmailFromFb = new ArrayList<>();
        SiparisiVerenEmailFromFb = new ArrayList<>();
        AdresFromFb = new ArrayList<>();
        siparisYemekAdiFromFb = new ArrayList<>();
        siparisYemekFiyatiFromFb = new ArrayList<>();
        //------------------------------------------------
        siparisNoFromFbVeren = new ArrayList<>();
        SiparisiAlanEmailFromFbVeren = new ArrayList<>();
        SiparisiVerenEmailFromFbVeren = new ArrayList<>();
        AdresFromFbVeren = new ArrayList<>();
        siparisYemekAdiFromFbVeren = new ArrayList<>();
        siparisYemekFiyatiFromFbVeren = new ArrayList<>();




        navDraweremail=(TextView) findViewById(R.id.navDraweremail);
        navDrawerisim=(TextView) findViewById(R.id.navDrawerisim);
        nav_drawer_logo=(ImageView)findViewById(R.id.profilPhoto);
        imageView3=(ImageView)findViewById(R.id.imageView3);
        nav_drawer_logo = (ImageView) findViewById(R.id.profilPhoto);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        String userEmail = firebaseUser.getEmail();
        navDraweremail.setText(userEmail.toString());
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        profilbilgisicek();
        siparisAlanBilgiCek();
        siparisVerenBilgiCek();

        //Rec
        RecyclerView recSiparisAlinan=findViewById(R.id.recSiparisAlinan);
        recSiparisAlinan.setLayoutManager(new LinearLayoutManager(this));
        siparisAlanRecAdapter= new SiparisAlanRecAdapter(siparisNoFromFb,siparisYemekAdiFromFb,siparisYemekFiyatiFromFb);
        recSiparisAlinan.setAdapter(siparisAlanRecAdapter);
        //---------------------
        RecyclerView recSiparisVerilen=findViewById(R.id.recSiparisVerilen);
        recSiparisVerilen.setLayoutManager(new LinearLayoutManager(this));
        siparisVerenRecAdapter= new SiparisVerenRecAdapter(siparisNoFromFbVeren,siparisYemekAdiFromFbVeren,siparisYemekFiyatiFromFbVeren);
        recSiparisVerilen.setAdapter(siparisVerenRecAdapter);



    }
    public void ClickMenu(View view){
        deneme.openDrawer(drawerLayout);
    }

    public void ClickLogo(View view){

        deneme.closeDrawer(drawerLayout);
    }

    public void ClickHome(View view)
    {
        deneme.redirectActivity(this,deneme.class);

    }
    public void ClickDashboard(View view){
        deneme.redirectActivity(this,ProfilBilgisi.class);

    }

    public void ClickMap(View view)
    {
        deneme.redirectActivity(this,MapsActivity.class);
    }

    public void ClickAboutUs(View view)
    {

        deneme.redirectActivity(this,AboutUs.class);
    }

    public void ClickYemekEkle(View view)
    {
        deneme.redirectActivity(this,yemekkaydet.class);

    }
    public void ClickSiparler(View view)
    {

        recreate();
    }
    public void ClickLogOut(View view)
    {
        // deneme.Logout(this);
    }
    public  void Logout(Activity activity)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("Logout");
        builder.setMessage("Çıkmak İsrediğinize Emin Misiniz ?");
        builder.setPositiveButton("Evet", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                firebaseAuth.signOut();
                activity.finishAffinity();
                System.exit(0);
            }
        });
        builder.setNegativeButton("Hayır", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.show();
    }
    @Override
    protected void onPause() {
        super.onPause();
        deneme.closeDrawer(drawerLayout);
    }

    public void profilbilgisicek() {
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        CollectionReference collectionReference = firebaseFirestore.collection("users");
        collectionReference.whereEqualTo("email", firebaseUser.getEmail()).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {


                if (e != null) {

                    Toast.makeText(Siparisler.this, e.getLocalizedMessage().toString(), Toast.LENGTH_LONG).show();
                } else {
                    for (DocumentSnapshot snapshots : queryDocumentSnapshots.getDocuments()) {
                        Map<String, Object> data = snapshots.getData();
                        profileR = (String) data.get("Profil Fotografi");
                        String isim = (String) data.get("name");
                        String soyisim = (String) data.get("surname");
                        urlImageProfil.add(profileR);
                        userProfilisim.add(isim);
                        userProfilsoyisim.add(soyisim);


                        Picasso.get().load(urlImageProfil.get(0)).into(nav_drawer_logo);
                        navDrawerisim.setText(isim + " " + soyisim);


                    }

                }

            }
        });


    }

    public void siparisAlanBilgiCek() {

        FirebaseUser firebaseUser=firebaseAuth.getCurrentUser();
        CollectionReference collectionReference = firebaseFirestore.collection("siparisler");
        collectionReference.whereEqualTo("SiparisiAlanEmail",firebaseUser.getEmail()).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {


                if(e!=null)
                {

                    Toast.makeText(Siparisler.this, e.getLocalizedMessage().toString(),Toast.LENGTH_LONG).show();
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

                        SiparisiAlanEmailFromFb.add(SiparisiAlanEmail);
                        SiparisiVerenEmailFromFb.add(SiparisiVerenEmail);
                        AdresFromFb.add(Adres);
                        siparisYemekAdiFromFb.add(siparisYemekAdi);
                        siparisYemekFiyatiFromFb.add(siparisYemekFiyati);
                        siparisNoFromFb.add(siparisNo);

                        System.out.println("siparisNo"+siparisNo);

                        siparisAlanRecAdapter.notifyDataSetChanged();




                    }

                }

            }
        });


    }

    public void siparisVerenBilgiCek() {

        FirebaseUser firebaseUser=firebaseAuth.getCurrentUser();
        CollectionReference collectionReference = firebaseFirestore.collection("siparisler");
        collectionReference.whereEqualTo("SiparisiVerenEmail",firebaseUser.getEmail()).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {


                if(e!=null)
                {

                    Toast.makeText(Siparisler.this, e.getLocalizedMessage().toString(),Toast.LENGTH_LONG).show();
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

                        SiparisiAlanEmailFromFbVeren.add(SiparisiAlanEmail);
                        SiparisiVerenEmailFromFbVeren.add(SiparisiVerenEmail);
                        AdresFromFbVeren.add(Adres);
                        siparisYemekAdiFromFbVeren.add(siparisYemekAdi);
                        siparisYemekFiyatiFromFbVeren.add(siparisYemekFiyati);
                        siparisNoFromFbVeren.add(siparisNo);

                        System.out.println("siparisNoVeren"+siparisNo);

                        siparisVerenRecAdapter.notifyDataSetChanged();




                    }

                }

            }
        });


    }
}