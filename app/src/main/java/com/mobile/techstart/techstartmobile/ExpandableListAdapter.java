package com.mobile.techstart.techstartmobile;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Lucas on 3/6/2018.
 */

public class ExpandableListAdapter extends BaseExpandableListAdapter {
    private Context context;
    private List<String> listDataHeader;
    private List<String> listDataBody;

    public ExpandableListAdapter(Context context, List<String> listDataHeader, List<String> listHashMap) {
        this.context = context;
        this.listDataHeader = listDataHeader;
        this.listDataBody = listHashMap;
    }

    @Override
    public int getGroupCount() {
        return listDataHeader.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 1;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return listDataHeader.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        //Log.e("ExpandableListAdapter",childPosition + "   " + listDataBody.get(childPosition));
        return listDataBody.get(groupPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String headerTitle = (String)getGroup(groupPosition);
        String[] splitHeader = headerTitle.split(";");
        if(convertView == null) {
            LayoutInflater inf = (LayoutInflater)this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inf.inflate(R.layout.list_head,null);
        }
        TextView lblListHeader = convertView.findViewById(R.id.listHead);
        TextView lblListDate = convertView.findViewById(R.id.listHead2);
        lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListDate.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(splitHeader[0]);
        lblListDate.setText(splitHeader[1]);
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final String childText = (String)getChild(groupPosition,childPosition);

        if(convertView == null) {
            LayoutInflater inf = (LayoutInflater)this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inf.inflate(R.layout.list_body,null);
        }


        TextView textListChild = convertView.findViewById(R.id.listBody);
        textListChild.setText(childText);
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
