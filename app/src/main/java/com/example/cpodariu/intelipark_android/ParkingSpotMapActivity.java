package com.example.cpodariu.intelipark_android;

import android.os.Bundle;

/**
 * Created by cpodariu on 05.11.2017.
 */

public class ParkingSpotMapActivity extends BaseMainActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.contentFragment = new ParkingSpotMapFragment();
        super.onCreate(savedInstanceState);
    }
}
