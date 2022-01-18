package com.example.proje_1_elka;

import androidx.annotation.NonNull;
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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class girisyap extends AppCompatActivity {
    private Button giris ,btnkayitol;
    private EditText emailgir,sifregir;
    private FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_girisyap);

        firebaseAuth=FirebaseAuth.getInstance();

        FirebaseUser firebaseUser= firebaseAuth.getCurrentUser();


       giris=(Button)findViewById(R.id.giris);
       btnkayitol=(Button)findViewById(R.id.btnkayitol);
       emailgir=(EditText)findViewById(R.id.emailgir);
       sifregir=(EditText)findViewById(R.id.sifregir);


       giris.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {

               String email=emailgir.getText().toString();
               String sifre=sifregir.getText().toString();


                if(emailgir.getText().toString().trim().equals("") || sifregir.getText().toString().trim().equals(""))
                {
                    Toast.makeText(girisyap.this ,"Lütfen Boş Alan Bırakmayınız ",Toast.LENGTH_LONG).show();

                }

                else if(emailgir.getText().toString().trim().equals("admin") &&sifregir.getText().toString().trim().equals("admin") )
                {
                    Intent goadmin=new Intent(girisyap.this, AdminPanelKullaniciSil.class);
                    goadmin.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(goadmin);
                    finish();
                }
                else
                {
                    firebaseAuth.signInWithEmailAndPassword(email,sifre).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            Intent gomain=new Intent(girisyap.this, deneme.class);
                            gomain.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(gomain);
                            finish();

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(girisyap.this,e.getLocalizedMessage().toString(),Toast.LENGTH_LONG).show();

                        }
                    });
                }




           }
       });

       btnkayitol.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent kayitol= new Intent(getApplicationContext(),yenikayit.class);
               startActivity(kayitol);
           }
       });



    }
}