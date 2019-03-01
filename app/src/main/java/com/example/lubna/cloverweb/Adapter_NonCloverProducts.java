package com.example.lubna.cloverweb;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lubna.cloverweb.Database.ModelDB.Cart;
import com.example.lubna.cloverweb.Utils.Common;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.List;

public class Adapter_NonCloverProducts extends RecyclerView.Adapter<Adapter_NonCloverProducts.ViewHolder> {

    int count;
    List<Model_NonCloverProducts> products;
    private Context context;

    public Adapter_NonCloverProducts(List<Model_NonCloverProducts> products, Context context) {
        this.products = products;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_noncloverproducts, parent, false);
        return new ViewHolder(v);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {

        final Model_NonCloverProducts items = products.get(position);
        holder.ProductName.setText(items.getProduct_name());
        holder.ProductPrice.setText("Rs. " + items.getProduct_price());
        holder.ProductName.setTextColor(Color.GRAY);
        holder.ProductPrice.setTextColor(Color.parseColor("#1a9a55"));
        holder.ProductPrice.setTypeface(Typeface.DEFAULT_BOLD);


        Picasso.with(context)
                .load(items.getProduct_image())
                .into(holder.ProductImage);

        holder.Add_to_Cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*SharedPreferences sp = context.getSharedPreferences("Pre",Context.MODE_PRIVATE);
                Boolean UserLogin = sp.getBoolean("UserLogin",false);*/

                count = Common.cartRepository.countItem(items.getProduct_id());
                if (count >= 1) {
                    TextView text = (TextView) egrocery.layout.findViewById(R.id.text);
                    text.setText("Item already exist in Cart");

                    Toast toast = new Toast(context);
                    toast.setDuration(Toast.LENGTH_LONG);
                    toast.setView(egrocery.layout);
                    toast.show();
                } else {
                    try {
                        Cart cartItem = new Cart();
                        cartItem.pid = items.getProduct_id();
                        cartItem.name = items.getProduct_name();
                        cartItem.image = items.getProduct_image();
                        cartItem.quantity = 1;
                        cartItem.unitPrice = Integer.parseInt(items.getProduct_price());
                        cartItem.total = cartItem.quantity * cartItem.unitPrice;

                        //Add to DB
                        Common.cartRepository.insertToCart(cartItem);

                        //Icon Count
                        egrocery.badge.setText(String.valueOf(Common.cartRepository.countCartItems()));

                        Log.e("Clover_Debug", new Gson().toJson(cartItem));

                        /*Toast toast = Toast.makeText(context, "Added to Cart", Toast.LENGTH_SHORT);
                        toast.show();*/

                        TextView text = (TextView) egrocery.layout.findViewById(R.id.text);
                        text.setText("Added to Cart");

                        Toast toast = new Toast(context);
                        //toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                        toast.setDuration(Toast.LENGTH_LONG);
                        toast.setView(egrocery.layout);
                        toast.show();


                    } catch (Exception ex) {
                        Toast.makeText(context, ex.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView ProductName, ProductPrice;
        ImageView ProductImage;
        Button Add_to_Cart;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            ProductName = itemView.findViewById(R.id.txtProductName);
            ProductPrice = itemView.findViewById(R.id.txtProductPrice);
            ProductImage = itemView.findViewById(R.id.imgProductImage);
            Add_to_Cart = itemView.findViewById(R.id.btnAddtoCart);
        }
    }
}
