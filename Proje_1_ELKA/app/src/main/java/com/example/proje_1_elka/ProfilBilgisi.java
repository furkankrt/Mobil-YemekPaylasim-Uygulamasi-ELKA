package com.example.proje_1_elka;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ScrollView;
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


public class ProfilBilgisi extends AppCompatActivity {
    DrawerLayout drawerLayout;
    ImageView nav_drawer_logo,profilPhoto;
    ImageButton detailedYemek1, detailedYemek2, detailedYemek3, detailedYemek4, detailedYemek5;
    TextView detailedYemek1TV, detailedYemek2TV, detailedYemek3TV, detailedYemek4TV, detailedYemek5TV;
    public String userEmail;
    public FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    TextView navDraweremail, navDrawerisim,textisim;
    String profileR;
    ArrayList<String> urlImageProfil;
    ArrayList<String> userProfilisim;
    ArrayList<String> userProfilsoyisim;

    public ArrayList<String> urlImageYemek;
    public ArrayList<String> adiYemek;
    public ArrayList<String> paylasimNo;




    public int tur=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil_bilgisi);
        drawerLayout = findViewById(R.id.drawer_layout);


        urlImageProfil = new ArrayList<>();
        userProfilisim = new ArrayList<>();
        userProfilsoyisim = new ArrayList<>();

        urlImageYemek = new ArrayList<>();
        adiYemek = new ArrayList<>();
        paylasimNo = new ArrayList<>();

        navDraweremail=(TextView) findViewById(R.id.navDraweremail);
        navDrawerisim=(TextView) findViewById(R.id.navDrawerisim);
        textisim=(TextView) findViewById(R.id.textisim);



        nav_drawer_logo=(ImageView)findViewById(R.id.profilPhoto) ;
        profilPhoto=(ImageView)findViewById(R.id.profilPhoto) ;


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




        nav_drawer_logo = (ImageView) findViewById(R.id.profilPhoto);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        userEmail = firebaseUser.getEmail();

        navDraweremail.setText(userEmail);

        firebaseAuth = FirebaseAuth.getInstance();

        firebaseFirestore = FirebaseFirestore.getInstance();
        profilbilgisicek();
        yemekDetayCek();
    }

    public void ClickMenu(View view) {
        deneme.openDrawer(drawerLayout);
    }

    public void ClickLogo(View view) {

        deneme.closeDrawer(drawerLayout);
    }

    public void ClickHome(View view) {
        deneme.redirectActivity(this, deneme.class);

    }

    public void ClickDashboard(View view) {
        recreate();

    }

    public void ClickMap(View view) {
        deneme.redirectActivity(this, MapsActivity.class);
    }

    public void ClickAboutUs(View view) {
        deneme.redirectActivity(this, AboutUs.class);

    }

    public void ClickYemekEkle(View view) {
        deneme.redirectActivity(this, yemekkaydet.class);

    }


    public void ClickLogOut(View view) {
        //deneme.Logout(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        deneme.closeDrawer(drawerLayout);
    }


    public void profilbilgisicek()
    {    FirebaseUser firebaseUser=firebaseAuth.getCurrentUser();
        CollectionReference collectionReference = firebaseFirestore.collection("users");
        collectionReference.whereEqualTo("email",firebaseUser.getEmail()).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {


                if(e!=null)
                {

                    Toast.makeText(ProfilBilgisi.this, e.getLocalizedMessage().toString(),Toast.LENGTH_LONG).show();
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

                        Picasso.get().load(urlImageProfil.get(0)).into(nav_drawer_logo);
                        navDrawerisim.setText(isim+" "+soyisim);
                        textisim.setText(isim+" "+soyisim);


                    }

                }

            }
        });



    }

    public void yemekDetayCek(){
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        System.out.println(userEmail);
        CollectionReference collectionReference = firebaseFirestore.collection("yemekler");
        collectionReference.whereEqualTo("userEmail",firebaseUser.getEmail())
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                if(error !=null)
                {
                    Toast.makeText(ProfilBilgisi.this,error.getLocalizedMessage().toString(),Toast.LENGTH_LONG);
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

                        /*CollectionReference collectionReference1 = firebaseFirestore.collection("yemekler");
                        collectionReference1.whereEqualTo("userEmail",firebaseUser.getEmail()).addSnapshotListener(new EventListener<QuerySnapshot>() {
                            @Override
                            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                                for(DocumentSnapshot snapshot : value.getDocuments())
                                {
                                    Map<String, Object> data = snapshot.getData();
                                    String tutUrl = (String) data.get("tutUrl");
                                    Picasso.get().load(tutUrl).into(detailedYemek1);
                                    detailedYemek1TV.setText(data.get("yemekadi").toString());
                                }
                            }
                        });*/

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
}
