package com.example.proje_1_elka;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class intro extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_intro);
        firebaseAuth=FirebaseAuth.getInstance();


        FirebaseUser firebaseUser=firebaseAuth.getCurrentUser();

        Thread logoAnimation =new Thread(){
            @Override
            public void run(){
                ImageView iv_logo=findViewById(R.id.profilPhoto);
                Animation animation = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.intro_giris_logo);
                iv_logo.startAnimation(animation);
            }
        };
        logoAnimation.start();

        Thread titleAnimation =new Thread(){
            @Override
            public void run(){
                TextView labelTitle=findViewById(R.id.labelTitle);
                Animation animation = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.intro_giris_logo);
                labelTitle.startAnimation(animation);
            }
        };
        titleAnimation.start();



        Thread authorAnimation =new Thread(){
            @Override
            public void run(){
                TextView author= findViewById(R.id.author);
                Animation animation = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.intro_girisi);
                author.startAnimation(animation);
            }
        };
        authorAnimation.start();

        if(firebaseUser!=null)
        {
            Thread mainegit =new Thread(){
                @Override
                public void run(){
                    try {
                        sleep(4000);
                        Intent i=new Intent(getApplicationContext(),deneme.class);
                        startActivity(i);
                        finish();
                        super.run();

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            };
            mainegit.start();

        }
        else
        {
            Thread girisegit =new Thread(){
                @Override
                public void run(){
                    try {
                        sleep(4000);
                        Intent i=new Intent(getApplicationContext(),girisyap.class);
                        startActivity(i);
                        finish();
                        super.run();

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            };
            girisegit.start();

        }








    }

}