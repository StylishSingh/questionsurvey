package com.library.basecontroller;

import android.content.Context;
import android.content.ContextWrapper;
import android.os.StrictMode;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import com.library.utils.Prefs;


public class BaseApplication extends MultiDexApplication {

    private static Context context;

    public static synchronized Context getGlobalContext() {
        return BaseApplication.context;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();


        if (BaseApplication.context == null) {
            BaseApplication.context = getApplicationContext();
        }

        new Prefs.Builder()
                .setContext(this)
                .setMode(ContextWrapper.MODE_PRIVATE)
                .setPrefsName(getPackageName())
                .setUseDefaultSharedPreference(true)
                .build();
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
    }

}