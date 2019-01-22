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
public class ExpandableListViewAdapter extends BaseExpandableListAdapter
{
    private Context context;
    private List<String> listdataheader;
    private HashMap<String,List<String>> listHashMap;
    public ExpandableListViewAdapter(Context context, List<String> listdataheader, HashMap<String, List<String>> listHashMap)
    {
        this.context = context;
        this.listdataheader = listdataheader;
        this.listHashMap = listHashMap;
    }
    @Override
    public int getGroupCount()
    {
        return listdataheader.size();
    }
    @Override
    public int getChildrenCount(int i)
    {
        return listHashMap.get(listdataheader.get(i)).size();
    }
    @Override
    public Object getGroup(int i)
    {
        return listdataheader.get(i);
    }
    @Override
    public Object getChild(int i, int i1)
    {
        return listHashMap.get(listdataheader.get(i)).get(i1);
    }
    @Override
    public long getGroupId(int i)
    {
        return i;
    }
    @Override
    public long getChildId(int i, int i1)
    {
        return i1;
    }
    @Override
    public boolean hasStableIds()
    {
        return false;
    }
    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewgroup)
    {
        String headertitle  =(String) getGroup(i);
        if(view == null)
        {
            LayoutInflater inflater = (LayoutInflater)this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.expandablelistviewheader,null);
        }
        TextView listheader =  view.findViewById(R.id.name_food);
        listheader.setTypeface(null,Typeface.BOLD);
        listheader.setText(headertitle);
        ImageView im = view.findViewById(R.id.image_food);
        return view;
    }
    @Override
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup)
    {
        final String childText = (String) getChild(i,i1);
        if(view==null)
        {
            LayoutInflater inflater = (LayoutInflater)this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.expandablelistviewitem,null);
        }
        TextView listchild = (TextView) view.findViewById(R.id.name_food2);
        listchild.setText(childText);
        listchild.isHorizontalScrollBarEnabled();
        ImageView im=  view.findViewById(R.id.name_food2);
        return view;
    }
    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }
}
