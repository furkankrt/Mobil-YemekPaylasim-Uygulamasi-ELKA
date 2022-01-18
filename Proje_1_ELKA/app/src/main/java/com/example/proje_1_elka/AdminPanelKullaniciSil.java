package com.example.proje_1_elka;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Map;

public class AdminPanelKullaniciSil extends AppCompatActivity {
    DrawerLayout drawerLayout;
    private FirebaseAuth firebaseAuth;
    private ArrayList<String> kisiEmailFromFbRec;
    private ArrayList<String> kisiIsimFromFbRec;
    private ArrayList<String> kisiFotografFromFbRec;
    private FirebaseFirestore firebaseFirestore;

    chatKisilerAdapter chatKisilerAdapter;
    RecyclerView kisiler_listesi;
    private chatKisilerAdapter.RecyclerViewClickListener listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_panel_kullanici_sil);

        drawerLayout = findViewById(R.id.drawer_layout);



        kisiEmailFromFbRec=new ArrayList<>();
        kisiIsimFromFbRec=new ArrayList<>();
        kisiFotografFromFbRec=new ArrayList<>();

        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();


        kisileriGetir();
        setOnClickListener();


        kisiler_listesi=findViewById(R.id.kisiler_listesi);
        kisiler_listesi.setLayoutManager(new LinearLayoutManager(AdminPanelKullaniciSil.this));
        chatKisilerAdapter=new chatKisilerAdapter(kisiEmailFromFbRec,kisiIsimFromFbRec,kisiFotografFromFbRec,listener);
        kisiler_listesi.setAdapter(chatKisilerAdapter);
    }

    public void ClickMenu(View view)
    {
        openDrawer(drawerLayout);

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

    public void ClickKullaniciSil(View view)
    {
        recreate();
    }

    public void ClickYemekSil(View view)
    {
        redirectActivity(this,AdminPanelYemekSil.class);
    }

    public void ClickSiparisSil(View view)
    {
        redirectActivity(this,AdminPanelSiparisSil.class);
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
               //SİLME YERİNİ YAZ



            }
        };
    }
}