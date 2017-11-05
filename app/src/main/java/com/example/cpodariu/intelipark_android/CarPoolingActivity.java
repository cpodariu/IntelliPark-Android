package com.example.cpodariu.intelipark_android;

import android.os.Bundle;

/**
 * Created by cpodariu on 04.11.2017.
 */

public class CarPoolingActivity extends BaseMainActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.contentFragment = new CarpoolingFragment();
        super.onCreate(savedInstanceState);
    }
}
