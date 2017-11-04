package com.example.cpodariu.intelipark_android;

import android.os.Bundle;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 * Created by cpodariu on 04.11.2017.
 */

public class ParkingSpotActivity extends BaseMainActivity {

    private TextView spotAssignedTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.contentViewId = R.layout.parking_spot_activity_content;
        super.onCreate(savedInstanceState);
        spotAssignedTv = (TextView) findViewById(R.id.parkingSpotTv);
    }
}
