package com.example.lubna.cloverweb.ViewHolder;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.bignerdranch.expandablerecyclerview.ViewHolder.ParentViewHolder;
import com.example.lubna.cloverweb.R;
public class TestParentViewholder extends ParentViewHolder
{
    public TextView _textview;
    public ImageView _imageview;
    public TestParentViewholder(View itemView)
    {
        super(itemView);
        _textview = itemView.findViewById(R.id.name_food3);
        _imageview = itemView.findViewById(R.id.image_food3);
    }
}
