/*
 *   Created by Abhinav Pandey on 06/05/23, 1:54 AM
 */
package com.example.scrappy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.Toast;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class newsFeed extends AppCompatActivity implements LocationListener {

    Double userLat = 25.473034, userLng = 81.878357;
    adapter adapter;
    private final static int REQUEST_CODE = 100;
    private final static int REQUEST_CODE_LOCATION_PERMISSION = 200;
    private LocationManager locationManager;
    FusedLocationProviderClient fusedLocationProviderClient;
    List<model> postList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offers);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        getLastLocation();


        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Posts");
        GeoFire geoFire = new GeoFire(ref);

        Location userLocation = new Location("");
        userLocation.setLatitude(userLat);
        userLocation.setLongitude(userLng);

        double radius = 50.0; // kilometers

        GeoQuery geoQuery = geoFire.queryAtLocation(new GeoLocation(userLocation.getLatitude(), userLocation.getLongitude()), radius);

        geoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {
            @Override
            public void onKeyEntered(String key, GeoLocation location) {
                ref.child(key).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        model post = snapshot.getValue(model.class);
                        if (post != null) {
                            postList.add(post);
                            adapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // handle error
                    }
                });
            }

            @Override
            public void onKeyExited(String key) {
                // not needed
            }

            @Override
            public void onKeyMoved(String key, GeoLocation location) {
                // not needed
            }

            @Override
            public void onGeoQueryReady() {
                // not needed
            }

            @Override
            public void onGeoQueryError(DatabaseError error) {
                // handle error
            }
        });

        adapter = new adapter(postList);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(newsFeed.this, MainActivity.class));
    }

    private void loadPosts() {

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Posts");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                postList.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    model post = ds.getValue(model.class);
                    postList.add(post);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(newsFeed.this, "", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getLastLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            fusedLocationProviderClient.getLastLocation()
                    .addOnSuccessListener(new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if (location != null) {
                                Geocoder geocoder = new Geocoder(newsFeed.this, Locale.getDefault());
                                List<Address> addresses = null;
                                try {

                                    addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                                    userLng = addresses.get(0).getLongitude();
                                    userLat = addresses.get(0).getLatitude();

                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                            } else {
                                Toast.makeText(newsFeed.this, "Turn on your GPS, please", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        } else {

            askPermission();
        }
    }

    private void askPermission() {
        ActivityCompat.requestPermissions(newsFeed.this, new String[]
                {
                        Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation();
            } else {
                Toast.makeText(this, "Required Permission", Toast.LENGTH_SHORT).show();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {

    }
}