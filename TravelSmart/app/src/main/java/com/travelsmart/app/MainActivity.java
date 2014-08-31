package com.travelsmart.app;

import android.app.ExpandableListActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.SearchView;

import org.json.JSONException;


public class MainActivity extends ExpandableListActivity {

    private SearchView mSearchView;
    private ExpandableListView mExpandableList;
    private ExpandableSectionAdapter mExpandableListAdapter;
    public Location currentLocation;
    public BusRouteFinder finder;
    final Handler mHandler = new Handler();

    private final String TAG = "DEBUG";
    final SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener() {
        @Override
        public boolean onQueryTextChange(String query) {
            Log.d(TAG, "Inputing = " + query);
            return false;

        }

        @Override
        public boolean onQueryTextSubmit(String query) {
            Log.d(TAG, "Submitted = " + query);
            return false;
        }
    };

    private BroadcastReceiver mLocationUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final Location location = intent.getParcelableExtra(MyLocationService.NOTIF_LOCATION_KEY);
            MainActivity.this.currentLocation = location;
            double[] pair = {location.getLatitude(), location.getLongitude()};

            new AsyncTask<double[], Void, Void>() {
                @Override
                protected Void doInBackground(double[]... pair) {
                    try {
                        MainActivity.this.finder = new BusRouteFinder(pair[0]);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    return null;
                }
            }.execute(pair);

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setUpdateExpandableView();
        LocalBroadcastManager.getInstance(this).registerReceiver(mLocationUpdateReceiver,
                new IntentFilter(MyLocationService.NOTIF_LOCATION_UPDATED));

        for (int i = 0; i < 3; i ++){
            mExpandableList.expandGroup(i, true);
        }
    }

    protected void setUpdateExpandableView() {
        mExpandableList = getExpandableListView();
        mExpandableList.setDividerHeight(2);
        mExpandableList.setGroupIndicator(null);
        mExpandableList.setClickable(true);
        mExpandableList.setOnGroupClickListener(new OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                return true;
            }
        });

        mExpandableListAdapter = new ExpandableSectionAdapter(mExpandableList);
        mExpandableListAdapter.setInflater((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE), this);
        mExpandableList.setAdapter(mExpandableListAdapter);
        mExpandableList.setOnChildClickListener(this);
        mExpandableList.setDividerHeight(2);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.search_bar, menu);
        MenuItem searchItem = menu.findItem(R.id.search);
        mSearchView = (SearchView) searchItem.getActionView();
        setupSearchView(searchItem);

        return true;
    }


    private void setupSearchView(MenuItem searchItem) {
        mSearchView.setOnQueryTextListener(queryTextListener);
    }

    protected boolean isAlwaysExpanded() {
        return false;
    }
}
