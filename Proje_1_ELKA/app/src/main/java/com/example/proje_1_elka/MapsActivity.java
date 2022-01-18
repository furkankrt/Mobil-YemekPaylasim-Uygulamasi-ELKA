package com.example.proje_1_elka;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.provider.MediaStore;
import android.telecom.Call;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.widget.ZoomControls;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Map;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;
    public Double konumgetird,konum1getird;
    public String useremail,yemekfiyatigetir;
    public int Size=0;

    public GoogleMap mMap;

    private Button btn_Type;

    static final LatLng TURKIYE = new LatLng(41.3977006, 33.7530643);

    public ArrayList<String> userEmailFromFB;
    public ArrayList<String> yemekadiFromFB;
    public ArrayList<String> yemekfiyatiFromFB;
    public ArrayList<String> urlImageFromFB;
    public ArrayList<String>  TarihFromFB;

    public ArrayList<Double> LatitudeFromFB;
    public ArrayList<Double>  LongitudeFromFB;




    public int i=1;
    public  GoogleMap googleMap;


    SupportMapFragment mapFragment;
    FusedLocationProviderClient client;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        userEmailFromFB=new ArrayList<>();
        yemekadiFromFB=new ArrayList<>();
        yemekfiyatiFromFB=new ArrayList<>();
        urlImageFromFB=new ArrayList<>();
        TarihFromFB=new ArrayList<>();

        LongitudeFromFB=new ArrayList<>();
        LatitudeFromFB=new ArrayList<>();

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore= FirebaseFirestore.getInstance();
        yemeklericek();


        System.out.println("onCreate");
        System.out.println(LongitudeFromFB+","+LatitudeFromFB);


        btn_Type = (Button) findViewById(R.id.btn_Type);

        client = LocationServices.getFusedLocationProviderClient(this);

        if (ActivityCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
        {

        }
        else
        {
            ActivityCompat.requestPermissions(MapsActivity.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},44);
        }

    }



    private BitmapDescriptor BitMapFromVector(Context context, int vectorResId) {

        // below line is use to generate a drawable.
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);

        // below line is use to set bounds to our vector drawable.
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());

        // below line is use to create a bitmap for our
        // drawable which we have added.
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);

        // below line is use to add bitmap in our canvas.
        Canvas canvas = new Canvas(bitmap);

        // below line is use to draw our
        // vector drawable in canvas.
        vectorDrawable.draw(canvas);

        // after generating our bitmap we are returning our bitmap.
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }
    public void turDegis(View view){
        if(mMap.getMapType()==GoogleMap.MAP_TYPE_NORMAL)
        {
            mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);//UYDU GÖRÜNÜMÜ
            mMap.setTrafficEnabled(true);

        }
        else if (mMap.getMapType()==GoogleMap.MAP_TYPE_SATELLITE){
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        }


    }
    public  void yemeklericek()
    {
        CollectionReference collectionReference= firebaseFirestore.collection("yemekler");
        collectionReference.orderBy("Tarih", Query.Direction.DESCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if(e!=null)
                {
                    Toast.makeText(MapsActivity.this,e.getLocalizedMessage().toString(),Toast.LENGTH_LONG).show();
                }
                if(queryDocumentSnapshots!=null)
                {
                    for(DocumentSnapshot snapshots:queryDocumentSnapshots.getDocuments())
                    {
                        Map<String,Object> data =snapshots.getData();
                        String paylasimNo=(String) data.get("paylasimNo");
                        String useremail=(String) data.get("userEmail");
                        String yemekadigetir=(String) data.get("yemekadi");//çevirdik objeyi string olarak yaptık
                        //String yemekfiyatigetir=(String) data.get("yemekfiyati");
                        //String fotourlgetir=(String)data.get("tutUrl");
                        //String tarihgetir=(String)data.get("Tarih");
                        String konumgetir=(String)data.get("Latitude");
                        String konum1getir=(String)data.get("Longitude");

                        konumgetird=Double.parseDouble(konumgetir);
                        konum1getird=Double.parseDouble(konum1getir);


                        userEmailFromFB.add(useremail);
                        yemekadiFromFB.add(yemekadigetir);
                        yemekfiyatiFromFB.add(yemekfiyatigetir);
                        //urlImageFromFB.add(fotourlgetir);
                       // TarihFromFB.add(tarihgetir);

                        LongitudeFromFB.add(konumgetird);
                        LatitudeFromFB.add(konum1getird);
                        mMap.addMarker(new MarkerOptions().position(new LatLng(konumgetird,konum1getird))
                                .title(yemekadigetir)
                                .snippet(paylasimNo))
                                .setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED ));


                        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                            @Override
                            public boolean onMarkerClick(Marker marker) {

                                String tutDetayid=marker.getSnippet();
                                Intent i= new Intent(MapsActivity.this,yemekDetay.class);
                                i.putExtra("paylasimNo",tutDetayid);

                                AlertDialog.Builder builder = new AlertDialog.Builder(MapsActivity.this);
                                builder.setTitle("ELKA");
                                builder.setMessage("Kullanıcı yemeğini görüntülemek ister misiniz?");
                                builder.setNegativeButton("Hayır", null);
                                builder.setPositiveButton("Evet", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        startActivity(i);
                                    }
                                });builder.show();

                                return false;
                            }
                        });





                    }
                }




            }
        });

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {


        btn_Type = findViewById(R.id.btnkayitol);
        mMap = googleMap;
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(TURKIYE, 4));//kameranın başlangıçta ne kadar yukarı olacağı sayı küçüldükçe zoom daha uzağa girer









    }






}
