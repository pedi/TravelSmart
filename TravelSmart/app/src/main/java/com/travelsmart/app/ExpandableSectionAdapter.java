package com.travelsmart.app;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;

/**
 * Created by qiaoliang89 on 30/8/14.
 */
public class ExpandableSectionAdapter extends BaseExpandableListAdapter {
    private LayoutInflater mInflater;
    private Context mContext;

    public ExpandableSectionAdapter(Context context) {
        this.mContext = context;
    }

    public void setInflater(LayoutInflater inflater, Activity activity) {
        this.mInflater = inflater;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition, boolean isLastChild, View convertView,
                             ViewGroup parent) {
        //TODO
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.children_list_header, null);
        }

        return convertView;
    }

    @Override
    public View getGroupView(final int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        //TODO
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.parent_list_item, null);
        }

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
        //TODO
        return 1;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return null;
    }

    @Override
    public int getGroupCount() {
        return 3; // Home + Work + Search Result
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
