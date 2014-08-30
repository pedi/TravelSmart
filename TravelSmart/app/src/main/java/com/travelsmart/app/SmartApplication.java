package com.travelsmart.app;

import android.app.Application;
import android.content.Intent;
import android.widget.Toast;

/**
 * Created by qiaoliang89 on 30/8/14.
 */
public class SmartApplication extends Application implements Foreground.Listener {

    @Override
    public void onCreate() {
        super.onCreate();
        Foreground.get(this).addListener(this);
    }

    // Foreground Callback
    @Override
    public void onBecameForeground() {
        Toast.makeText(SmartApplication.this, "Startting service", Toast.LENGTH_LONG).show();
        this.startService(new Intent(this, MyLocationService.class));
    }

    // Foreground Callback
    @Override
    public void onBecameBackground() {
        this.stopService(new Intent(this, MyLocationService.class));
    }
}
