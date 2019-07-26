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
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class FragmentMyOrder extends Fragment {

    public static String order_id,status;
    View view;
    String user_id;
    private RecyclerView recyclerVieworder;
    private RecyclerView.Adapter adapter;
    private List<Model_Order> m_orders;
    TextView status1;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_myorders, container, false);
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("Pre", Context.MODE_PRIVATE);
        user_id = sharedPreferences.getString("UserID", "");
        //Toast.makeText(getContext(),user_id,Toast.LENGTH_LONG).show();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerVieworder = view.findViewById(R.id.recyclerVieworders);
        status1 = view.findViewById(R.id.tv_status);
        recyclerVieworder.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false));
        m_orders = new ArrayList<>();
        getorders();

    }

    private void getorders() {
        final String URL = "http://172.16.10.203/api/get-customer-orders/" + user_id;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    String dat = "Delivery Date: ";
                    String Tot = "Total Amount: ";
                    String Status = "Status: ";
                    String add = "Shipping Address: ";
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        order_id = jsonObject.getString("order_id");
                        String deldate = dat + jsonObject.getString("deliverydate");
                        String total_amount = Tot + jsonObject.getString("total_amount");
                        status = Status + jsonObject.getString("status");
                        JSONObject jsonObject1 = jsonObject.getJSONObject("findaddress");
                        String address = add+jsonObject1.getString("address");
                        Model_Order items = new Model_Order(
                                deldate,
                                total_amount,
                                status,
                                address,
                                order_id
                        );
                        m_orders.add(items);
                    }
                    adapter = new Adapter_Myorder(m_orders, getContext());
                    recyclerVieworder.setAdapter(adapter);
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