package com.example.lubna.cloverweb;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.lubna.cloverweb.Utils.Common;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
public class Cart extends Fragment
{
    RecyclerView recyclerView_cart;
    Button btn_place_order;
    String gettotal;
    public static TextView txt_total_price,txt_gst,txt_final_price;
    public static RelativeLayout layout01,layout02;
    CompositeDisposable compositeDisposable;
    SharedPreferences sharedPreferences;
    Boolean UserLogin;
    Button btn_add_items_to_cart;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        final ViewGroup rootview = (ViewGroup) inflater.inflate(R.layout.cart, container, false);
        return rootview;
    }
    @SuppressLint({"LongLogTag", "SetTextI18n"})
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        txt_gst = view.findViewById(R.id.txt_gst);
        txt_final_price = view.findViewById(R.id.txt_final_price);

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
        //txt_item_count = view.findViewById(R.id.txt_item_count);
        //txt_item_count.setText(String.valueOf("Items: " + Common.cartRepository.countCartItems()));
        txt_total_price.setText(String.valueOf(Common.cartRepository.sumCartItems()));
        //gettotal = txt_total_price.getText().toString();
        sharedPreferences = getActivity().getSharedPreferences("Pre", Context.MODE_PRIVATE);
        /*SharedPreferences.Editor editor1 = sharedPreferences.edit();
        editor1.putString("value1",gettotal);
        editor1.apply();*/
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
        getGst(getContext());

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
                    Bundle args = new Bundle();
                    args.putString("TotalAmount", txt_final_price.getText().toString());
                    args.putString("SubTotal", txt_total_price.getText().toString());
                    args.putString("GST", txt_gst.getText().toString());
                    fragment.setArguments(args);
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

    public void getGst(final Context context)
    {
        String URL = "http://172.16.10.203/api/get-gst-tax";

        StringRequest req = new StringRequest(Request.Method.GET, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        if (response != null)
                        {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                String tax = jsonObject.getString("percentage");
                                float t_price = Float.parseFloat(txt_total_price.getText().toString());
                                float gst = (Float.parseFloat(tax)/100);
                                float cal_gst = t_price * gst;
                                txt_gst.setText(String.valueOf(cal_gst));
                                float f_price = t_price + cal_gst;
                                txt_final_price.setText(String.valueOf(f_price));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context,error.getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(req);
    }

}
