package com.travelsmart.app;

import android.app.AlertDialog;
import android.app.ExpandableListActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.Toast;

import org.json.JSONException;

import java.util.ArrayList;


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
        public boolean onQueryTextSubmit(final String query) {
            Log.d(TAG, "Submitted = " + query);
            if (MainActivity.this.finder == null) {
                Log.d(TAG, "finder is null");
            }
            if (MainActivity.this.finder != null) {
                mHandler.post( new Runnable() {
                    @Override
                    public void run() {
                        try {
                            ArrayList<String> stops = MainActivity.this.finder.getPossibleBusStops(query);
                            if (stops != null && stops.size() > 0) {
                                ArrayList<String> lines = MainActivity.this.finder.getPossibleBusLines(stops.get(0));
                                if (lines != null && lines.size() > 0){

                                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                                    builder.setTitle("Pick a Bus");
                                    builder.setItems((CharSequence[])lines.toArray(), new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            // the user clicked on colors[which]
                                        }
                                    });
                                    builder.show();

                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
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
                protected Void doInBackground(double[]... pairOfPoints) {
                    try {
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(MainActivity.this, "Location Update", Toast.LENGTH_LONG).show();
                            }
                        });
                        MainActivity.this.finder = new BusRouteFinder(pairOfPoints[0]);
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
