package com.example.lubna.cloverweb;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.List;
public class FeaturedAdapter extends RecyclerView.Adapter<FeaturedAdapter.MyViewHolder>{
    Context mContext;
    List<Product_Model> mData;
    public FeaturedAdapter(Context mContext,List<Product_Model> mData)
    {
        this.mContext = mContext;
        this.mData = mData;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i)
    {
        View v;
        v= LayoutInflater.from(mContext).inflate(R.layout.item_product,parent,false);
        final MyViewHolder viewHolder = new MyViewHolder(v);
        return viewHolder;
    }
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int position)
    {
        myViewHolder.food1.setText(mData.get(position).getName());
        myViewHolder.foodname1.setText(mData.get(position).getPhone());
        myViewHolder.foodimage1.setImageResource(mData.get(position).getPhoto());
        myViewHolder.but1.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                AlertDialog alertDialog = new AlertDialog.Builder(mContext).create();
               alertDialog.setTitle("Select Quantity");
               alertDialog.setMessage("2");
               alertDialog.setButton("Continue..", new DialogInterface.OnClickListener()
               {
                    public void onClick(DialogInterface dialog, int which)
                    {
                    }
                });
                alertDialog.show();
//                Toast.makeText(mContext,"hi",Toast.LENGTH_LONG).show();
            }
        });
        }
        @Override
    public int getItemCount()
        {
        return mData.size();
         }
    public static class MyViewHolder extends RecyclerView.ViewHolder
    {
        private TextView food1,foodname1;
        private ImageView foodimage1;
        public ImageView but1;
        private LinearLayout item_product;
        public MyViewHolder(@NonNull View itemView)
        {
            super(itemView);
            item_product = itemView.findViewById(R.id.item_product);
            food1 = itemView.findViewById(R.id.name_food);
            foodname1= itemView.findViewById(R.id.food_detail);
            foodimage1 = itemView.findViewById(R.id.image_food);
            but1 = itemView.findViewById(R.id.trolleyimage);
        }
    }
}
