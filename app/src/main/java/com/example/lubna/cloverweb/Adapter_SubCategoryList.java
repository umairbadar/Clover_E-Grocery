package com.example.lubna.cloverweb;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class Adapter_SubCategoryList extends RecyclerView.Adapter<Adapter_SubCategoryList.ViewHolder> {

    List<Model_SubCategoryList> products;
    private Context context;
    Fragment fragment;
    FragmentManager fragmentManager;
    Bundle args;
    FragmentTransaction fragmentTransaction;

    public Adapter_SubCategoryList(List<Model_SubCategoryList> products, Context context) {
        this.products = products;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_sub_category_list,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        final Model_SubCategoryList items = products.get(position);

        holder.ProductName.setText(items.getTitle());
        //holder.ProductDescrip.setText(items.getDescription());
        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = items.getId();
                AppCompatActivity activity = (AppCompatActivity) v.getContext();
                fragment = new ProductDetailsFragment();
                fragmentManager = activity.getSupportFragmentManager();
                args = new Bundle();
                args.putString("id", id);
                fragment.setArguments(args);
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.content_egrocery, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

            }
        });
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        public TextView ProductName;
        public ImageView ProductImage;
        public RelativeLayout relativeLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            ProductImage = itemView.findViewById(R.id.imgProductImage);
            ProductName = itemView.findViewById(R.id.txtProductName);
            relativeLayout = itemView.findViewById(R.id.layout1);
            //ProductDescrip = itemView.findViewById(R.id.txtProductDesc);
        }
    }
}
