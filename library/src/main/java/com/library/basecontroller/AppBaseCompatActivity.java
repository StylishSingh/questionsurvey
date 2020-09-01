package com.library.basecontroller;

import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by root on 23/11/17.
 */

abstract public class AppBaseCompatActivity extends AppCompatActivity {

    protected boolean isBLEDevice() {
        return getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE);
    }


}
