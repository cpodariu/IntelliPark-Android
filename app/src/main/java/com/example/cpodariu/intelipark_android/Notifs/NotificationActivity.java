package com.example.cpodariu.intelipark_android.Notifs;

import android.os.Bundle;

import com.example.cpodariu.intelipark_android.BaseMainActivity;
import com.example.cpodariu.intelipark_android.ParkingSpotFragment;

/**
 * Created by cpodariu on 05.11.2017.
 */

public class NotificationActivity extends BaseMainActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.contentFragment = new NotificationsFragment();
        super.onCreate(savedInstanceState);
    }
}
