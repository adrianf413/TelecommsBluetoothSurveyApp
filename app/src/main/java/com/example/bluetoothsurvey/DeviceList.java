package com.example.bluetoothsurvey;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/*
This is the class to display the devices on the screen
*/

public class DeviceList extends AppCompatActivity {

    ArrayList<String> device_names = new ArrayList<>();
    ArrayList<BluetoothDeviceObject> devices = new ArrayList<>();
    ArrayList<String> list = new ArrayList<String>();

    ArrayAdapter adapter;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference reference = database.getReference(this.getDeviceName());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_list);





        String[] values = new String[] { "Android", "iPhone", "WindowsMobile",
                "Blackberry", "WebOS", "Ubuntu", "Windows7", "Max OS X",
                "Linux", "OS/2", "Ubuntu", "Windows7", "Max OS X", "Linux",
                "OS/2", "Ubuntu", "Windows7", "Max OS X", "Linux", "OS/2",
                "Android", "iPhone", "WindowsMobile" };

        /*
        for (int i = 0; i < values.length; ++i) {
            list.add(values[i]);
        }*/

        list.add("");
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list);

        ListView listview;
        listview = (ListView) findViewById(R.id.list);
        listview.setAdapter(adapter);

        reference.child("Locations").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                List<String> keys = new ArrayList<>();

                for (DataSnapshot ds: dataSnapshot.getChildren()) {
                    keys.add(ds.getKey());

                    if(dataSnapshot.exists()){
                        LocationData location_data = (ds.getValue(LocationData.class));

                        devices = location_data.getDevicesList();
                        if(devices != null){
                            //System.out.println(devices.get(0).name);
                            for(BluetoothDeviceObject device : devices) {
                                if (device.name != null) {
                                    if(!list.contains(device.name + " : " +device.address)) {
                                        adapter.add(device.name + " : " + device.address);
                                    }
                                    //adapter.add(device.name + ":" +device.address);
                                } else {

                                    if(!list.contains("Unnamed Device" +" : " +  device.address)) {
                                        //list.add("Unnamed Device" + ":" + device.address);
                                        adapter.add("Unnamed Device" +" : " +  device.address);
                                    }
                                    //list.add("Unnamed Device" + ":" + device.address);
                                    //adapter.add("Unnamed Device" +":" +  device.address);
                                }
                                //add_devices(device.name);
                            }
                        }

                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    public void add_devices(String name){
        //list.add(name);
        //adapter.notifyDataSetChanged();
        adapter.add(name);
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
