package com.example.proje_1_elka;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;

public class AdminPanelYemekSil extends AppCompatActivity {
    DrawerLayout drawerLayout;
    private FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_panel_yemek_sil);
        drawerLayout = findViewById(R.id.drawer_layout);
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
        recreate();

    }
    public void ClickSiparisSil(View view)
    {

        AdminPanelKullaniciSil.redirectActivity(this,AdminPanelSiparisSil.class);
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
}