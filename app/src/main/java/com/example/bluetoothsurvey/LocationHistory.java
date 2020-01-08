package com.example.bluetoothsurvey;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class LocationHistory extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Marker markerLocation;
    public ArrayList<LocationData> datapoints = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_history);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // Make sure that the application has permission to get the users location
        checkLocationPermission();
    }

    //Method given in the android workshop
    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(this)
                        .setTitle("Permission to access GPS")
                        .setMessage("Please allow the app to access you location.")
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(LocationHistory.this,
                                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                                        99);
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        99);
            }
            return false;
        } else {
            return true;
        }
    }


    public void getData(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference(this.getDeviceName());

        reference.child("Locations").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                List<String> keys = new ArrayList<>();

                for (DataSnapshot ds: dataSnapshot.getChildren()) {
                    keys.add(ds.getKey());

                    if(dataSnapshot.exists()){
                        LocationData location_data = (ds.getValue(LocationData.class));
                        datapoints.add(location_data);

                        LatLng latlng = new LatLng(location_data.getLatitude(), location_data.getLongitude());
                        addMarker(latlng, String.valueOf(location_data.getNum_devices()));
                        //adding data object read in from database to the list of objects
                        datapoints.add(location_data);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(52.852412, -8.961374);
        //mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Engineering Building"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 11));


        FirebaseDatabase database = FirebaseDatabase.getInstance();
        String devicename = getDeviceName();
        String reference_children = devicename + "/Location Data Points";
        DatabaseReference reference = database.getReference(this.getDeviceName());
        DatabaseReference childRef = reference.child("Location Data Points");

        reference.child("Locations").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                List<String> keys = new ArrayList<>();

                for (DataSnapshot ds: dataSnapshot.getChildren()) {
                    keys.add(ds.getKey());

                    if(dataSnapshot.exists()){
                        LocationData location_data = (ds.getValue(LocationData.class));
                        datapoints.add(location_data);

                        LatLng latlng = new LatLng(location_data.getLatitude(), location_data.getLongitude());
                        String message = "Devices:" + String.valueOf(location_data.getNum_devices());
                        //adding marker for this object to the map
                        addMarker(latlng, message);
                        //adding to list of objects
                        datapoints.add(location_data);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        if (!datapoints.isEmpty()) {
            for (LocationData datapoint : datapoints) {
                LatLng latLng = new LatLng(datapoint.getLatitude(), datapoint.getLongitude());
                addMarker(latLng, String.valueOf(datapoint.getNum_devices()));
            }
        }

        final Button backButton = (Button) findViewById(R.id.BackButton);
        backButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                });

    }


    private void addMarker(LatLng latLng, String message) {
        if (latLng == null) {
            return;
        }
        if (markerLocation != null) {
            //markerLocation.remove();
        }
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title(message);
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));
        if (mMap != null)
            markerLocation = mMap.addMarker(markerOptions);
        
    }


    public String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return capitalize(model);
        } else {
            return capitalize(manufacturer) + " " + model;
        }
    }


    private String capitalize(String s) {
        if (s == null || s.length() == 0) {
            return "";
        }
        char first = s.charAt(0);
        if (Character.isUpperCase(first)) {
            return s;
        } else {
            return Character.toUpperCase(first) + s.substring(1);
        }
    }
}
