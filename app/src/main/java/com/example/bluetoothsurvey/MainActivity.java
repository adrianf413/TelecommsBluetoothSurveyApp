package com.example.bluetoothsurvey;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Add Two buttons in here
        //One button to open mna activity where detected devices were displayed on a map
        Button locationsButton = (Button) findViewById(R.id.LocationsButton);

        locationsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, LocationHistory.class));
            }
        });

        //Button for opening up list of devices found
        Button devicesButton = (Button) findViewById(R.id.Devicesbutton);

        devicesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, DeviceList.class));
            }
        });
    }
}
