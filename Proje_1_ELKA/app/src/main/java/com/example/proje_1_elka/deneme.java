package com.example.proje_1_elka;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
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



public class deneme extends AppCompatActivity {
    DrawerLayout drawerLayout;
    TextView navDraweremail,navDrawerisim;
    ImageView nav_drawer_logo;
    public FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    ArrayList<String> userEmailFromFB;
    ArrayList<String> yemekadiFromFB;
    ArrayList<String> yemekfiyatiFromFB;
    ArrayList<String> urlImageFromFB;
    ArrayList<String>urlImageProfil;
    ArrayList<String>paylasimNo;

    mainRecyclerAdapter mainRecyclerAdapter;

    ArrayList<String>  userProfilPhoto;
    ArrayList<String>  userProfilisim;
    ArrayList<String>  userProfilsoyisim;

    String profileR;
    Bitmap profileRBitmap;
    String isim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deneme);
        drawerLayout = findViewById(R.id.drawer_layout);


        userEmailFromFB=new ArrayList<>();
        yemekadiFromFB=new ArrayList<>();
        yemekfiyatiFromFB=new ArrayList<>();
        urlImageFromFB=new ArrayList<>();
        userProfilPhoto=new ArrayList<>();
        paylasimNo=new ArrayList<>();

        urlImageProfil=new ArrayList<>();
        userProfilisim=new ArrayList<>();
        userProfilsoyisim=new ArrayList<>();

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore= FirebaseFirestore.getInstance();

        yemeklericek();
        profilbilgisicek();

       //RECYCLER VİEW BÖLÜMÜÜÜÜÜÜ
        RecyclerView recyclerView=findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(deneme.this,2));
        mainRecyclerAdapter=new mainRecyclerAdapter(userEmailFromFB,yemekadiFromFB,yemekfiyatiFromFB,urlImageFromFB);
        recyclerView.setAdapter(mainRecyclerAdapter);

        FirebaseUser firebaseUser=firebaseAuth.getCurrentUser();
        String userEmail=firebaseUser.getEmail();
        navDraweremail=(TextView) findViewById(R.id.navDraweremail);
        navDrawerisim=(TextView) findViewById(R.id.navDrawerisim);
        nav_drawer_logo=(ImageView)findViewById(R.id.profilPhoto) ;

        navDraweremail.setText(userEmail.toString());
        profilbilgisicek();






    }

    public void ClickMenu(View view)
    {
        openDrawer(drawerLayout);

    }
    public void ClickChat(View view)
    {
        redirectActivity(this,kisilerDeneme.class);
    }

    public static void openDrawer(DrawerLayout drawerLayout)
    {
        drawerLayout.openDrawer(GravityCompat.START);

    }

    public void ClickLogo(View view)
    {
        closeDrawer(drawerLayout);
    }

    public static void closeDrawer(DrawerLayout drawerLayout)
    {
        //check condition
        if(drawerLayout.isDrawerOpen(GravityCompat.START))
        {
            //when it's open close it
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }

    public void ClickHome(View view)
    {
        recreate();
    }

    public void ClickDashboard(View view)
    {
        redirectActivity(this,ProfilBilgisi.class);
    }

    public void ClickYemekEkle(View view)
    {
        redirectActivity(this,yemekkaydet.class);
    }

    public void ClickSiparler(View view)
    {
        redirectActivity(this,Siparisler.class);
    }



    public void ClickAboutUs(View view)
    {
        redirectActivity(this,AboutUs.class);
    }

    public void ClickMap(View view)
    {
        redirectActivity(this,MapsActivity.class);
    }


    public void ClickLogOut(View view)
    {
        Logout(this);
    }

    public void Logout(Activity activity)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("Logout");
        builder.setMessage("Çıkmak İsrediğinize Emin Misiniz ?");
        builder.setPositiveButton("Evet", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                firebaseAuth.signOut();
                Intent i=new Intent(getApplicationContext(),girisyap.class);
                startActivity(i);
                finish();
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
    public static void redirectActivity(Activity activity, Class aClass)
    {
        Intent intent = new Intent(activity,aClass);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
    }
    @Override
    protected void onPause() {
        super.onPause();
        closeDrawer(drawerLayout);
    }

    public  void yemeklericek()
    {
        CollectionReference collectionReference= firebaseFirestore.collection("yemekler");
        collectionReference.orderBy("Tarih", Query.Direction.DESCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if(e!=null)
                {
                    Toast.makeText(deneme.this,e.getLocalizedMessage().toString(),Toast.LENGTH_LONG).show();
                }
                if(queryDocumentSnapshots!=null)
                {
                    for(DocumentSnapshot snapshots:queryDocumentSnapshots.getDocuments())
                    {
                        Map<String,Object> data =snapshots.getData();
                        String paylasimNo=(String) data.get("paylasimNo");
                        String yemekadigetir=(String) data.get("yemekadi");//çevirdik objeyi string olarak yaptık
                        String useremail=(String) data.get("userEmail");
                        String yemekfiyatigetir=(String) data.get("yemekfiyati");
                        String fotourlgetir=(String)data.get("tutUrl");

                        userEmailFromFB.add(useremail);
                        yemekadiFromFB.add(yemekadigetir);
                        yemekfiyatiFromFB.add(yemekfiyatigetir);
                        urlImageFromFB.add(fotourlgetir);
                        mainRecyclerAdapter.notifyDataSetChanged();



                    }
                }




            }
        });

    }

    public void profilbilgisicek()
    {    FirebaseUser firebaseUser=firebaseAuth.getCurrentUser();
        CollectionReference collectionReference = firebaseFirestore.collection("users");
        collectionReference.whereEqualTo("email",firebaseUser.getEmail()).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {


                if(e!=null)
                {

                    Toast.makeText(deneme.this, e.getLocalizedMessage().toString(),Toast.LENGTH_LONG).show();
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


                    }

                }

            }
        });



    }
}
