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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Map;

public class AdminPanelSiparisSil extends AppCompatActivity {
    DrawerLayout drawerLayout;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    RecyclerView recSiparisSil;

    adminPanelSiparisSilAdapter adminPanelSiparisSilAdapter;

    private ArrayList<String> siparisNoFromFbRec;
    private ArrayList<String> SiparisiAlanEmailFromFbRec;
    private ArrayList<String> SiparisiVerenEmailFromFbRec;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_panel_siparis_sil);
        drawerLayout = findViewById(R.id.drawer_layout);
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        siparisNoFromFbRec=new ArrayList<>();
        SiparisiAlanEmailFromFbRec=new ArrayList<>();
        SiparisiVerenEmailFromFbRec=new ArrayList<>();

        sparisleriGetir();



        recSiparisSil=findViewById(R.id.recSiparisSil);
        recSiparisSil.setLayoutManager(new LinearLayoutManager(AdminPanelSiparisSil.this));
        adminPanelSiparisSilAdapter=new adminPanelSiparisSilAdapter(siparisNoFromFbRec,SiparisiAlanEmailFromFbRec,SiparisiVerenEmailFromFbRec);
        recSiparisSil.setAdapter(adminPanelSiparisSilAdapter);




    }
    public void ClickMenu(View view){
        AdminPanelKullaniciSil.openDrawer(drawerLayout);
    }
    public void ClickLogo(View view){

        AdminPanelKullaniciSil.closeDrawer(drawerLayout);
    }
    public void ClickKullaniciSil(View view)
    {
        AdminPanelKullaniciSil.redirectActivity(this,AdminPanelKullaniciSil.class);

    }
    public void ClickYemekSil(View view){
        AdminPanelKullaniciSil.redirectActivity(this,AdminPanelYemekSil.class);

    }
    public void ClickSiparisSil(View view)
    {
        recreate();
    }

    public void ClickAboutUs(View view)
    {

        recreate();
    }

    public void ClickYemekEkle(View view)
    {
        AdminPanelKullaniciSil.redirectActivity(this,yemekkaydet.class);

    }
    public void ClickLogOut(View view)
    {
        // AdminPanelKullaniciSil.Logout(this);
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
        AdminPanelKullaniciSil.closeDrawer(drawerLayout);
    }
    public void sparisleriGetir ()
    {
        CollectionReference collectionReference= firebaseFirestore.collection("siparisler");
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
                        String siparisNo=(String) data.get("siparisNo");
                        String  siparisAlan=(String) data.get("SiparisiAlanEmail");
                        String  siparisVeren=(String)data.get("SiparisiVerenEmail");



                        siparisNoFromFbRec.add(siparisNo);
                        SiparisiAlanEmailFromFbRec.add(siparisAlan);
                        SiparisiVerenEmailFromFbRec.add(siparisVeren);


                       adminPanelSiparisSilAdapter.notifyDataSetChanged();



                    }
                }




            }
        });






    }
}