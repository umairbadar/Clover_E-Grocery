package com.example.lubna.cloverweb;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.example.lubna.cloverweb.Database.ModelDB.Cart;
import com.example.lubna.cloverweb.Utils.Common;
import com.google.gson.Gson;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Adapter_ProductDetails extends BaseAdapter {

    private ImageLoader imageLoader;
    private Context context;
    ArrayList<Model_ProductDetails> products;
    ArrayList<String> ID;

    public Adapter_ProductDetails(Context context, ArrayList<Model_ProductDetails> products) {
        this.context = context;
        this.products = products;
    }

    @Override
    public int getCount() {
        return products.size();
    }

    @Override
    public Object getItem(int position) {
        return products.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {

        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_product_details, viewGroup, false);
        }

        final Model_ProductDetails items = (Model_ProductDetails) this.getItem(position);

        ImageView img = view.findViewById(R.id.imgProductImage);
        TextView txtName = view.findViewById(R.id.txtProductName);
        TextView txtPrice = view.findViewById(R.id.txtProductPrice);
        final Button Addtocart = view.findViewById(R.id.btnAddtoCart);
        final ProgressBar progressBar = view.findViewById(R.id.homeprogress);

        txtName.setText(items.getName());
        txtPrice.setText("Rs. " + items.getPrice());

        Picasso.with(context)
                .load(items.getImage())
                .into(img, new Callback() {
                    @Override
                    public void onSuccess() {
                        progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError() {

                    }
                });

        Addtocart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                ID = new ArrayList<>();
                ID.add(items.getId());

                int count = Common.cartRepository.countItem(items.getId());
                if (count >= 1) {
                    TextView text = (TextView) egrocery.layout.findViewById(R.id.text);
                    text.setText("Item already exist in Cart");

                    Toast toast = new Toast(context);
                    //toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                    toast.setDuration(Toast.LENGTH_LONG);
                    toast.setView(egrocery.layout);
                    toast.show();
                } else {

                    try {
                        Cart cartItem = new Cart();
                        cartItem.pid = items.getId();
                        cartItem.name = items.getName();
                        cartItem.image = items.getImage();
                        cartItem.quantity = 1;
                        cartItem.unitPrice = Integer.parseInt(items.getPrice());
                        cartItem.total = cartItem.quantity * cartItem.unitPrice;

                        //Add to DB
                        Common.cartRepository.insertToCart(cartItem);

                        //Icon Count
                        egrocery.badge.setText(String.valueOf(Common.cartRepository.countCartItems()));

                        Log.e("Clover_Debug", new Gson().toJson(cartItem));

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

        return view;
    }
}