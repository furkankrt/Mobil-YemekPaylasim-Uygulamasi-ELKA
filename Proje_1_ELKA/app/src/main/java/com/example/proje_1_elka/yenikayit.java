package com.example.proje_1_elka;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

public class yenikayit extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    private FirebaseFirestore firebaseFirestore;
    private EditText kayit_emailgir,kayit_sifregir,kayit_sifreonaylagir,kayit_telefongir,kayit_isimgir,kayit_soyisimgir;
    private Button btn_kayitol;
    Uri imageData;
    public Bitmap selectedImage;
    private ImageView imageView_profilfotosec;







    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yenikayit);


        HashMap<String,Object> userData =new HashMap<>();

        firebaseStorage=FirebaseStorage.getInstance();
        storageReference=firebaseStorage.getReference();
        firebaseFirestore=FirebaseFirestore.getInstance();
        firebaseAuth=FirebaseAuth.getInstance();



        imageView_profilfotosec=findViewById(R.id.imageView_profilfotosec) ;

        //EDİTT TEXT LERİN TANIMLANMASI
        kayit_isimgir=(EditText)findViewById(R.id.kayit_isimgir) ;
        kayit_soyisimgir=(EditText)findViewById(R.id.kayit_soyisimgir) ;
        kayit_emailgir=(EditText)findViewById(R.id.kayit_emailgir);
        kayit_sifregir=(EditText)findViewById(R.id.kayit_sifregir);
        kayit_sifreonaylagir=(EditText)findViewById(R.id.kayit_sifreonaylagir);
        kayit_telefongir=(EditText)findViewById(R.id.kayit_telefongir);

        //BUTTONLARIN TANIMLANMASI
        btn_kayitol=(Button)findViewById(R.id.btn_kayitol);

        btn_kayitol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                UUID uuid= UUID.randomUUID();
                String imageName="avatars/"+uuid+"jpg";

                String isim=kayit_isimgir.getText().toString();
                String soyisim=kayit_soyisimgir.getText().toString();
                String email=kayit_emailgir.getText().toString();
                String sifre=kayit_sifregir.getText().toString();
                String sifreonayla=kayit_sifreonaylagir.getText().toString();
                String telefon=kayit_telefongir.getText().toString();


                if(!sifreonayla.equals(sifre))
                {
                    Toast.makeText(yenikayit.this ,"Şifreler Uyuşmuyor ",Toast.LENGTH_LONG).show();

                }

                else if(kayit_telefongir.length()==11)
                {

                    Toast.makeText(yenikayit.this ,"Telefon Numarasını Kontrol Ediniz ",Toast.LENGTH_LONG).show();
                }

                else if(kayit_isimgir.getText().toString().trim().equals("") || kayit_soyisimgir.getText().toString().trim().equals("") || kayit_sifreonaylagir.getText().toString().trim().equals("") || kayit_emailgir.getText().toString().trim().equals("") || kayit_sifregir.getText().toString().trim().equals("") || kayit_telefongir.getText().toString().trim().equals("") ||imageData==null)
                {

                    Toast.makeText(yenikayit.this ,"Lütfen Boş Alan Bırakmayınız ",Toast.LENGTH_LONG).show();
                }

                    else {
                    if (imageData != null) {
                        storageReference.child(imageName).putFile(imageData).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                //URL İNDİİİİİİİR
                                StorageReference newReference = FirebaseStorage.getInstance().getReference(imageName);
                                newReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {

                                        String tutUrlprofil = uri.toString();

                                        userData.put("name", isim);
                                        userData.put("surname", soyisim);
                                        userData.put("email", email);
                                        userData.put("password", sifre);
                                        userData.put("Phone", telefon);
                                        userData.put("Profil Fotografi", tutUrlprofil);


                                        firebaseAuth.createUserWithEmailAndPassword(email, sifre).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                            @Override
                                            public void onSuccess(AuthResult authResult) {

                                                String mevcutKullanici=firebaseAuth.getUid();

                                                Toast.makeText(yenikayit.this, "Kullanıcı oluşturuldu", Toast.LENGTH_LONG).show();
                                                firebaseFirestore.collection("users").document(email).set(userData);
                                                Intent gomain = new Intent(yenikayit.this, deneme.class);
                                                gomain.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                startActivity(gomain);
                                                finish();

                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(yenikayit.this, e.getLocalizedMessage().toString(), Toast.LENGTH_LONG).show();

                                            }
                                        });


                                    }
                                });


                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(yenikayit.this, e.getLocalizedMessage().toString(), Toast.LENGTH_LONG).show();
                            }
                        });

                    }
                }
            }
        });

        }

    public void selectImage(View view)
    {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }

        else {
            Intent intenttoGallery=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intenttoGallery,2);

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,@NonNull int[] grantResults) {

        if(requestCode==1)
        {
            if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){

                Intent intentToGallery=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intentToGallery,2);
            }
        }
        super.onRequestPermissionsResult(requestCode,permissions,grantResults);

    }
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        //kullanıcı görseli seçti
        if(requestCode == 2 && resultCode == RESULT_OK && data != null)
        {
            //görselin bilgileri bitmap olarak geldi
             imageData = data.getData();

            try {

                if(Build.VERSION.SDK_INT >=28)
                {
                    //datadaki bitmap görseli normal hale çeviriyoruz (?)
                    ImageDecoder.Source source = ImageDecoder.createSource(this.getContentResolver(),imageData);
                    selectedImage = ImageDecoder.decodeBitmap(source);
                }
                else
                {
                    selectedImage = MediaStore.Images.Media.getBitmap(this.getContentResolver(),imageData);
                }
                imageView_profilfotosec.setImageBitmap(selectedImage);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }




    }


