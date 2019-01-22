package com.example.lubna.cloverweb.Adapter;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.bignerdranch.expandablerecyclerview.Adapter.ExpandableRecyclerAdapter;
import com.bignerdranch.expandablerecyclerview.Model.ParentObject;
import com.example.lubna.cloverweb.R;
import com.example.lubna.cloverweb.ViewHolder.TestChildViewHolder;
import com.example.lubna.cloverweb.ViewHolder.TestParentViewholder;
import com.example.lubna.cloverweb.models.Testchild;
import com.example.lubna.cloverweb.models.Testparent;
import java.util.List;
public class Multiadapter extends ExpandableRecyclerAdapter<TestParentViewholder,TestChildViewHolder>
{
    LayoutInflater inflater;
    public Multiadapter(Context context, List<ParentObject> parentItemList)
    {
        super(context, parentItemList);
       inflater = LayoutInflater.from(context);
    }
    @Override
    public TestParentViewholder onCreateParentViewHolder(ViewGroup viewGroup)
    {
        View view = inflater.inflate(R.layout.list_parent,viewGroup,false);
        return new TestParentViewholder(view);
    }
    @Override
    public TestChildViewHolder onCreateChildViewHolder(ViewGroup viewGroup)
    {
        View view = inflater.inflate(R.layout.list_child,viewGroup,false);
        return new TestChildViewHolder(view);
    }
    @Override
    public void onBindParentViewHolder(TestParentViewholder testParentViewholder, int i, Object o)
    {
        Testparent title = (Testparent)o;
        testParentViewholder._textview.setText(title.getTitle());
    }
    @Override
    public void onBindChildViewHolder(TestChildViewHolder testChildViewHolder, int i, Object o)
    {
        Testchild title = (Testchild)o;
        testChildViewHolder.option1.setText(title.getOption1());
        testChildViewHolder.option2.setText(title.getOption2());
    }
}
