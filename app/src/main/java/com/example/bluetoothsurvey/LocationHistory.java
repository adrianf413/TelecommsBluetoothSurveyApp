package com.example.bluetoothsurvey;

import androidx.fragment.app.FragmentActivity;

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
    private boolean data_exists = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_history);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    public void getData(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        String devicename = getDeviceName();
        String reference_children = devicename + "/Location Data Points";
        DatabaseReference reference = database.getReference(this.getDeviceName());
        DatabaseReference childRef = reference.child("Location Data Points");

        //datapoints

        reference.child("Locations").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    data_exists = true;
                }else{
                    data_exists = true;
                }

                List<String> keys = new ArrayList<>();

                for (DataSnapshot ds: dataSnapshot.getChildren()) {
                    keys.add(ds.getKey());

                    if(dataSnapshot.exists()){
                        LocationData location_data = (ds.getValue(LocationData.class));
                        //int devices = location_data.num_devices;
                        datapoints.add(location_data);
                        //System.out.println("\n\n\n\n\n\n\n\n\n");
                        //System.out.println(devices);
                        //System.out.println(ds.getValue());
                        //System.out.println(location_data.num_devices);
                        LatLng latlng = new LatLng(location_data.getLatitude(), location_data.getLongitude());
                        addMarker(latlng, String.valueOf(location_data.getNum_devices()));

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
        LatLng sydney = new LatLng(53.283912, -9.063874);
        //mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Engineering Building"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 8));


        FirebaseDatabase database = FirebaseDatabase.getInstance();
        String devicename = getDeviceName();
        String reference_children = devicename + "/Location Data Points";
        DatabaseReference reference = database.getReference(this.getDeviceName());
        DatabaseReference childRef = reference.child("Location Data Points");

        //datapoints = new ArrayList<>();

        reference.child("Locations").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    data_exists = true;
                }else{
                    data_exists = true;
                }

                List<String> keys = new ArrayList<>();

                for (DataSnapshot ds: dataSnapshot.getChildren()) {
                    keys.add(ds.getKey());

                    if(dataSnapshot.exists()){
                        LocationData location_data = (ds.getValue(LocationData.class));
                        //int devices = location_data.num_devices;
                        datapoints.add(location_data);
                        //System.out.println("\n\n\n\n\n\n\n\n\n");
                        //System.out.println(devices);
                        //System.out.println(ds.getValue());
                        //System.out.println(location_data.num_devices);
                        LatLng latlng = new LatLng(location_data.getLatitude(), location_data.getLongitude());
                        String message = "Devices:" + String.valueOf(location_data.getNum_devices());
                        message = message + "\n" + location_data.datetime;
                        addMarker(latlng, message);

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
        }else{
            LatLng latLng = new LatLng(52.9455563, -8.9227235);
            addMarker(latLng, "No devices Found");
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

        /*
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(latLng.latitude, latLng.longitude))
                .zoom(10)
                .build();

        if (mMap != null)
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    */
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
