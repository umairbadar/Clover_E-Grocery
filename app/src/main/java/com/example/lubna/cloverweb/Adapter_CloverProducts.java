package com.example.lubna.cloverweb;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class Adapter_CloverProducts extends RecyclerView.Adapter<Adapter_CloverProducts.ViewHolder> {

    List<Model_CloverProducts> products;
    private Context context;

    public Adapter_CloverProducts(List<Model_CloverProducts> products, Context context) {
        this.products = products;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_cloverproducts,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter_CloverProducts.ViewHolder holder, int position) {

        Model_CloverProducts items = products.get(position);

        holder.ProductName.setText(items.getProduct_name());
        holder.ProductPrice.setText(items.getProduct_price());

        Picasso.with(context)
                .load(items.getProduct_image())
                .into(holder.ProductImage);
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        public TextView ProductName,ProductPrice;
        public ImageView  ProductImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            ProductName = itemView.findViewById(R.id.txtProductName);
            ProductPrice = itemView.findViewById(R.id.txtProductPrice);
            ProductImage = itemView.findViewById(R.id.imgProductImage);
        }
    }
}
