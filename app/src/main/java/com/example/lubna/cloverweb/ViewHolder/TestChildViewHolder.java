package com.example.lubna.cloverweb.ViewHolder;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.bignerdranch.expandablerecyclerview.ViewHolder.ChildViewHolder;
import com.example.lubna.cloverweb.R;
public class TestChildViewHolder extends ChildViewHolder
{
    public TextView option1,option2;
    ImageView option3;
    public TestChildViewHolder(View itemView)
    {
        super(itemView);
        option1= itemView.findViewById(R.id.name_food4);
        option2 = itemView.findViewById(R.id.food_detail5);
        option3 = itemView.findViewById(R.id.image_food7);
    }
}
