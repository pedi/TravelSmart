package com.travelsmart.app;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.TextView;

/**
 * Created by qiaoliang89 on 30/8/14.
 */
public class ExpandableSectionAdapter extends BaseExpandableListAdapter {
    private LayoutInflater mInflater;
    private ExpandableListView mContext;
    private RecordModel[] dummy;

    public ExpandableSectionAdapter(ExpandableListView context) {
        this.mContext = context;
        initDummyData();
    }

    private void initDummyData() {
        this.dummy = new RecordModel[3];

        this.dummy[0] = new RecordModel();
        this.dummy[0].buses = new BusStopModel[1];
        this.dummy[0].labelName = "Work";
        this.dummy[0].searchString = "Kent Ridge";

        this.dummy[0].buses[0] = new BusStopModel();

        this.dummy[0].buses[0].min = 1;
        this.dummy[0].buses[0].busNumber = "96";

        this.dummy[1] = new RecordModel();
        this.dummy[1].buses = new BusStopModel[3];
        this.dummy[1].labelName = "Home";
        this.dummy[1].searchString = "Bona Vista";

        this.dummy[1].buses[0] = new BusStopModel();
        this.dummy[1].buses[1] = new BusStopModel();
        this.dummy[1].buses[2] = new BusStopModel();

        this.dummy[1].buses[0].min = 1;
        this.dummy[1].buses[0].busNumber = "232";
        this.dummy[1].buses[1].min = 2;
        this.dummy[1].buses[1].busNumber = "154";
        this.dummy[1].buses[2].min = 3;
        this.dummy[1].buses[2].busNumber = "166";

        this.dummy[2] = new RecordModel();
        this.dummy[2].buses = new BusStopModel[8];
        this.dummy[2].labelName = "GF's home";
        this.dummy[2].searchString = "Clementi";

        this.dummy[2].buses[0] = new BusStopModel();
        this.dummy[2].buses[1] = new BusStopModel();
        this.dummy[2].buses[2] = new BusStopModel();
        this.dummy[2].buses[3] = new BusStopModel();
        this.dummy[2].buses[4] = new BusStopModel();
        this.dummy[2].buses[5] = new BusStopModel();
        this.dummy[2].buses[6] = new BusStopModel();
        this.dummy[2].buses[7] = new BusStopModel();

        this.dummy[2].buses[0].min = 1;
        this.dummy[2].buses[0].busNumber = "196";
        this.dummy[2].buses[1].min = 2;
        this.dummy[2].buses[1].busNumber = "154";
        this.dummy[2].buses[2].min = 3;
        this.dummy[2].buses[2].busNumber = "36";
        this.dummy[2].buses[3].min = 4;
        this.dummy[2].buses[3].busNumber = "196";
        this.dummy[2].buses[4].min = 5;
        this.dummy[2].buses[4].busNumber = "199";
        this.dummy[2].buses[5].min = 6;
        this.dummy[2].buses[5].busNumber = "36";
        this.dummy[2].buses[6].min = 7;
        this.dummy[2].buses[6].busNumber = "36";
        this.dummy[2].buses[7].min = 8;
        this.dummy[2].buses[7].busNumber = "36";

    }

    public void setInflater(LayoutInflater inflater, Activity activity) {
        this.mInflater = inflater;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition, boolean isLastChild, View convertView,
                             ViewGroup parent) {
        if (convertView == null) {
            if (childPosition == 0) {
                convertView = mInflater.inflate(R.layout.children_list_header, null);
            } else {
                convertView = mInflater.inflate(R.layout.children_list_item, null);
            }
        }
        if (childPosition == 0) {
            final TextView searchStringText = (TextView) convertView.findViewById(R.id.searchStringTextView);
            final ImageButton bookmarkButton = (ImageButton) convertView.findViewById(R.id.bookmark);
            final Button bookingButton = (Button) convertView.findViewById(R.id.booking);
            searchStringText.setText(this.dummy[groupPosition].searchString);
        } else {
            final TextView[] busInfo = new TextView[6];
            busInfo[0] = (TextView) convertView.findViewById(R.id.bus1);
            busInfo[1] = (TextView) convertView.findViewById(R.id.sub1);
            busInfo[2] = (TextView) convertView.findViewById(R.id.bus2);
            busInfo[3] = (TextView) convertView.findViewById(R.id.sub2);
            busInfo[4] = (TextView) convertView.findViewById(R.id.bus3);
            busInfo[5] = (TextView) convertView.findViewById(R.id.sub3);

            for (int i = 0; i < 3; i++) {
                int childPos = childPosition - 1;
                if (childPos * 3 + i <= this.dummy[groupPosition].buses.length - 1) {
                    busInfo[i * 2].setVisibility(View.VISIBLE);
                    busInfo[i * 2].setText(this.dummy[groupPosition].buses[childPos * 3 + i].busNumber);
                    busInfo[i * 2 + 1].setVisibility(View.VISIBLE);
                    busInfo[i * 2 + 1].setText(this.dummy[groupPosition].buses[childPos * 3 + i].min + " m");
                } else {
                    busInfo[i * 2].setVisibility(View.GONE);
                    busInfo[i * 2 + 1].setVisibility(View.GONE);
                }
            }
        }

        return convertView;
    }

    @Override
    public View getGroupView(final int groupPosition, final boolean isExpanded, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.parent_list_item, null);
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final int pos = ((Integer)v.getTag()).intValue();
                    if (ExpandableSectionAdapter.this.mContext.isGroupExpanded(pos)) {
                        ExpandableSectionAdapter.this.mContext.collapseGroup(pos);
                    } else {
                        ExpandableSectionAdapter.this.mContext.expandGroup(pos);
                    }
                }
            });
        }
        convertView.setTag(groupPosition);
        final TextView sectionLabelTextView = (TextView) convertView.findViewById(R.id.itemName);
        sectionLabelTextView.setText(this.dummy[groupPosition].labelName);

        return convertView;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return null;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return (int) Math.ceil(this.dummy[groupPosition].buses.length / 3.0) + 1;
    }

    @Override
    public int getChildTypeCount() {
        return 2;
    }

    @Override
    public int getChildType(int groupPosition, int childPosition) {

        if (childPosition == 0) {
            return 0;
        } else {
            return 1;
        }
    }


    @Override
    public Object getGroup(int groupPosition) {
        return null;
    }

    @Override
    public int getGroupCount() {
        return this.dummy.length;
    }

    @Override
    public void onGroupCollapsed(int groupPosition) {
        super.onGroupCollapsed(groupPosition);
    }

    @Override
    public void onGroupExpanded(int groupPosition) {
        super.onGroupExpanded(groupPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
}
