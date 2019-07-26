package com.example.lubna.cloverweb;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class FragmentMyOrderDetails extends Fragment {

    View view;
    String user_id,order_id;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private RecyclerView recyclerVieworderdetails;
    private RecyclerView.Adapter adapter;
    private List<Model_OrderDetails> m_orders_details;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_myorderdetails, container, false);
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("Pre", Context.MODE_PRIVATE);
        user_id = sharedPreferences.getString("UserID", "");
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getArguments() != null)
        {
            order_id = getArguments().getString("OrderID");
            //Toast.makeText(getContext(),order_id,Toast.LENGTH_LONG).show();
        }

        recyclerVieworderdetails = view.findViewById(R.id.listvieworderdetails);
        recyclerVieworderdetails.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        m_orders_details = new ArrayList<>();
        getorderdetails();
    }

    public void getorderdetails() {
        final String URL = "http://172.16.10.203/api/get-customer-orders/" + user_id;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    String prod = "Product Name:   ";
                    String quan = "Quantity:   ";
                    String uni = "Unit Price:   ";
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String o_id = jsonObject.getString("order_id");
                        if (o_id.equals(order_id)) {
                            JSONArray array = jsonObject.getJSONArray("findorderdetails");
                            for (int k = 0; k < array.length(); k++) {
                                JSONObject innerObj = array.getJSONObject(k);
                                JSONObject Obj = innerObj.getJSONObject("findproduct");
                                String p_name =prod+ Obj.getString("product_name");
                                String p_quantity = quan+innerObj.getString("quantity");
                                String p_price = uni+innerObj.getString("unitprice");

                                Model_OrderDetails items = new Model_OrderDetails(
                                        p_name,
                                        p_quantity,
                                        p_price
                                );
                                m_orders_details.add(items);
                            }
                        }
                        adapter = new Adapter_MyOrderDetails(m_orders_details, getContext());
                        recyclerVieworderdetails.setAdapter(adapter);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(), error.getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }
}
