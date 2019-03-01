package com.example.lubna.cloverweb;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lubna.cloverweb.Utils.Common;

import org.w3c.dom.Text;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class Cart extends Fragment {

    RecyclerView recyclerView_cart;
    Button btn_place_order;
    SharedPreferences pref;
    public static TextView txt_total_price, txt_item_count;

    public static RelativeLayout layout01,layout02;

    CompositeDisposable compositeDisposable;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Boolean UserLogin;

    Button btn_add_items_to_cart;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final ViewGroup rootview = (ViewGroup) inflater.inflate(R.layout.cart, container, false);
        return rootview;
    }

    @SuppressLint({"LongLogTag", "SetTextI18n"})
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btn_add_items_to_cart = view.findViewById(R.id.btn_add_items_to_cart);
        btn_add_items_to_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Fragment fragment = new Home();
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.content_egrocery,fragment);
                ft.addToBackStack(null);
                ft.commit();
            }
        });

        layout01 = view.findViewById(R.id.Cart_layout01);
        layout02 = view.findViewById(R.id.Cart_layout02);

        if (Common.cartRepository.countCartItems() == 0)
        {
            layout01.setVisibility(View.GONE);
            layout02.setVisibility(View.VISIBLE);
        }
        else
        {
            layout01.setVisibility(View.VISIBLE);
            layout02.setVisibility(View.GONE);
        }

        txt_total_price = view.findViewById(R.id.txt_total_price);
        txt_item_count = view.findViewById(R.id.txt_item_count);

        txt_item_count.setText(String.valueOf("Items: " + Common.cartRepository.countCartItems()));
        txt_total_price.setText(String.valueOf(Common.cartRepository.sumCartItems()));

        sharedPreferences = getActivity().getSharedPreferences("Pre", Context.MODE_PRIVATE);
        UserLogin = sharedPreferences.getBoolean("UserLogin", false);

        /*//TextView Total Price of Cart
        txt_total_price = view.findViewById(R.id.txt_total_price);
        String t_price = new DecimalFormat(".00").format(Common.cartRepository.sumCartItems());
        txt_total_price.setText("Total: Rs." + t_price);*/

        /*//Calculating GST
        double cal_gst = Common.cartRepository.sumCartItems() * 0.17;
        txt_gst.setText("GST Rs." + new DecimalFormat("##.##").format(cal_gst));*/

        /*//Calculate Final Price
        double final_price = Common.cartRepository.sumCartItems() + cal_gst;
        txt_final_price.setText("Final Price Rs." + String.valueOf(final_price));*/

        compositeDisposable = new CompositeDisposable();

        recyclerView_cart = view.findViewById(R.id.recycler_cart);
        recyclerView_cart.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView_cart.setHasFixedSize(true);

        loadCartItems();

        btn_place_order = view.findViewById(R.id.btn_place_order);

        //txt_total_price.setText(String.valueOf(Adapter_Cart.price));

        btn_place_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(Common.cartRepository.countCartItems() == 0)
                {
                    Toast.makeText(getContext(),"Cart is empty!!" + "\n" + "Add items first",
                            Toast.LENGTH_LONG).show();
                }
                else
                {
                    Fragment fragment = new FragmentSelectAddress();
                    FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.content_egrocery,fragment);
                    ft.addToBackStack(null);
                    ft.commit();
                }
            }
        });

    }

    private void loadCartItems() {

        compositeDisposable.add(
                Common.cartRepository.getCartItems()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe(new Consumer<List<com.example.lubna.cloverweb.Database.ModelDB.Cart>>() {
                            @Override
                            public void accept(List<com.example.lubna.cloverweb.Database.ModelDB.Cart> carts) throws Exception {
                                displayCartItems(carts);
                            }
                        })
        );
    }

    private void displayCartItems(List<com.example.lubna.cloverweb.Database.ModelDB.Cart> carts) {

        Adapter_Cart adapter = new Adapter_Cart(getContext(), carts);
        recyclerView_cart.setAdapter(adapter);
    }

}
