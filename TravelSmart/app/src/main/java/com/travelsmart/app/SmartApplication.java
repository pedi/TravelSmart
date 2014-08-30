package com.travelsmart.app;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.support.v4.content.LocalBroadcastManager;
import android.os.Handler;
import android.widget.Toast;

/**
 * Created by qiaoliang89 on 30/8/14.
 */
public class SmartApplication extends Application implements Foreground.Listener {
    public Location currentLocation;
    final Handler mHandler = new Handler();

    private BroadcastReceiver mLocationUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final Location location = intent.getParcelableExtra(MyLocationService.NOTIF_LOCATION_KEY);
            SmartApplication.this.currentLocation = location;
            SmartApplication.this.mHandler.post( new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(SmartApplication.this, "Location Available !!!", Toast.LENGTH_LONG).show();
                }
            });
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        LocalBroadcastManager.getInstance(this).registerReceiver(mLocationUpdateReceiver,
                new IntentFilter(MyLocationService.NOTIF_LOCATION_UPDATED));
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
