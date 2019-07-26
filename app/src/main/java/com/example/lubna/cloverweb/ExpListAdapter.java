package com.example.lubna.cloverweb;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

public class ExpListAdapter extends BaseExpandableListAdapter {

    private Context _context;
    private List<String> _listDataHeader; // header titles
    // child data in format of header title, child title
    private HashMap<String, List<String>> _listDataChild;

    public ExpListAdapter(Context _context, List<String> _listDataHeader, HashMap<String, List<String>> _listDataChild) {
        this._context = _context;
        this._listDataHeader = _listDataHeader;
        this._listDataChild = _listDataChild;
    }
    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .get(childPosititon);
    }
    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }
    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        final String childText = (String) getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.subcat_explist, null);
        }

        TextView txtListChild = (TextView) convertView
                .findViewById(R.id.lblListItem);

        txtListChild.setText(childText);
        return convertView;
    }
    @Override
    public int getChildrenCount(int groupPosition) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .size();
    }
    @Override
    public Object getGroup(int groupPosition) {
        return this._listDataHeader.get(groupPosition);
    }
    @Override
    public int getGroupCount() {
        return this._listDataHeader.size();
    }
    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        String headerTitle = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.cat_explist, null);
        }

        TextView lblListHeader = (TextView) convertView
                .findViewById(R.id.lblListHeader);
        lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(headerTitle);
        ImageView imgHeader = convertView.findViewById(R.id.headerImage);

        if (headerTitle.equals("Food"))
        {
            imgHeader.setImageResource(R.drawable.food);
        }
        else if (headerTitle.equals("Vegetables & Fruits"))
        {
            imgHeader.setImageResource(R.drawable.veg_fruits);
        }
        else if (headerTitle.equals("Breakfast & Dairy"))
        {
            imgHeader.setImageResource(R.drawable.breakfast);
        }
        else if (headerTitle.equals("Beverages"))
        {
            imgHeader.setImageResource(R.drawable.beverages);
        }
        else if (headerTitle.equals("Baby Care"))
        {
            imgHeader.setImageResource(R.drawable.baby_and_kids);
        }
        else if (headerTitle.equals("Instant Food"))
        {
            imgHeader.setImageResource(R.drawable.noodles_sauces);
        }
        else if (headerTitle.equals("House Hold"))
        {
            imgHeader.setImageResource(R.drawable.household_needs);
        }
        else if (headerTitle.equals("Meat & Sea Food"))
        {
            imgHeader.setImageResource(R.drawable.meat_seafood_new);
        }
        else if (headerTitle.equals("Lubricants"))
        {
            imgHeader.setImageResource(R.drawable.lubricants);
        }
        else if (headerTitle.equals("Personal care"))
        {
            imgHeader.setImageResource(R.drawable.personal_care);
        }
        else if (headerTitle.equals("Filters"))
        {
            imgHeader.setImageResource(R.drawable.filters);
        }
        return convertView;
    }
    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}