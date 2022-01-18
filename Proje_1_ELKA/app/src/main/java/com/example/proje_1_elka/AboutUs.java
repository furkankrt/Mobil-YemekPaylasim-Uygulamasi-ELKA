package com.example.proje_1_elka;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
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
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Map;

public class AboutUs extends AppCompatActivity {
    DrawerLayout drawerLayout;
    ImageView nav_drawer_logo,imageView3;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    TextView navDraweremail, navDrawerisim;
    String profileR;
    ArrayList<String> urlImageProfil;
    ArrayList<String> userProfilisim;
    ArrayList<String> userProfilsoyisim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        drawerLayout = findViewById(R.id.drawer_layout);
        urlImageProfil = new ArrayList<>();
        userProfilisim = new ArrayList<>();
        userProfilsoyisim = new ArrayList<>();

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


        imageView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("https://www.instagram.com/furkkankurt/"));
                startActivity(intent);
            }
        });
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

        recreate();
    }

    public void ClickYemekEkle(View view)
    {
        deneme.redirectActivity(this,yemekkaydet.class);

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
                System.out.println("3");

                if (e != null) {
                    System.out.println("11");
                    Toast.makeText(AboutUs.this, e.getLocalizedMessage().toString(), Toast.LENGTH_LONG).show();
                } else {
                    for (DocumentSnapshot snapshots : queryDocumentSnapshots.getDocuments()) {
                        Map<String, Object> data = snapshots.getData();
                        profileR = (String) data.get("Profil Fotografi");
                        String isim = (String) data.get("name");
                        String soyisim = (String) data.get("surname");
                        urlImageProfil.add(profileR);
                        userProfilisim.add(isim);
                        userProfilsoyisim.add(soyisim);
                        System.out.println(profileR);

                        Picasso.get().load(urlImageProfil.get(0)).into(nav_drawer_logo);
                        navDrawerisim.setText(isim + " " + soyisim);


                    }

                }

            }
        });


    }
}