package com.travelsmart.app;

import android.app.ExpandableListActivity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ExpandableListView.OnGroupCollapseListener;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.SearchView;


public class MainActivity extends ExpandableListActivity {

    private SearchView mSearchView;
    private ExpandableListView mExpandableList;
    private ExpandableSectionAdapter mExpandableListAdapter;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setUpdateExpandableView();
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

        mExpandableListAdapter = new ExpandableSectionAdapter(this);

        mExpandableListAdapter.setInflater((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE), this);

        mExpandableList.setAdapter(mExpandableListAdapter);
        mExpandableList.setOnChildClickListener(this);
        mExpandableList.setDividerHeight(2);

        mExpandableList.setOnGroupExpandListener(new OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                ExpandableViewUtil.setExpandedListViewHeightBasedOnChildren(mExpandableList, groupPosition);
            }
        });
        mExpandableList.setOnGroupCollapseListener(new OnGroupCollapseListener() {
            @Override
            public void onGroupCollapse(int groupPosition) {
                ExpandableViewUtil.setCollapseListViewHeightBasedOnChildren(mExpandableList, groupPosition);
            }
        });
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
