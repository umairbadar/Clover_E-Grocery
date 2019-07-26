package com.example.lubna.cloverweb;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.icu.text.UnicodeSetSpanner;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.lubna.cloverweb.Utils.Common;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class FragmentCash extends Fragment {

    Button paycash;
    TextView amount, tv_test;
    String total, head;
    View v;

    TextView tv_head;

    SharedPreferences sp, sp1;

    String store_id, points, method, address_id, user_id, account_id, gst, sub_total;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_cash, container, false);
        sp = getActivity().getSharedPreferences("MyPre", Context.MODE_PRIVATE);
        sp1 = getActivity().getSharedPreferences("Pre", Context.MODE_PRIVATE);
        store_id = sp.getString("StoreID", "");
        address_id = sp1.getString("address_id", "");
        user_id = sp1.getString("UserID", "");
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tv_test = view.findViewById(R.id.tv_test);

        if (getArguments() != null) {
            total = getArguments().getString("TotalAmount");
            head = getArguments().getString("head");
            points = getArguments().getString("points");
            method = getArguments().getString("method");
            account_id = getArguments().getString("account_id");
            sub_total = getArguments().getString("SubTotal");
            gst = getArguments().getString("GST");
        }

        tv_head = view.findViewById(R.id.tv_head);
        tv_head.setText(head);

        paycash = view.findViewById(R.id.buttonpaycash);
        amount = view.findViewById(R.id.tcash);
        amount.setText(String.valueOf("  This is your total amount  " + total));
        paycash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String product_ids = Arrays.toString(Common.cartRepository.getID());
                String quantity = Arrays.toString(Common.cartRepository.getQuantity());

                product_ids = product_ids.replace("[","")
                        .replace("]","");

                quantity = quantity.replace("[","")
                        .replace("]","");

                submitOrder(product_ids, quantity);

            }
        });
    }

    private void submitOrder(final String product_ids, final String quantity) {

        /*tv_test.setText("StoreID:" + store_id + "\n" + " Loyalty points:"
                + points + "\n" + " Method:" + method + "\n" + " AddressID:"
                + address_id + "\n" + " AccountID:" + account_id + "\n" + " SubTotal:" + sub_total
                + "\n" + " GST:" + gst + "\n" + "Product ID:" + product_ids + "\n" + "Quantity:" + quantity);*/

        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Please Wait");
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        String URL = "http://172.16.10.203/api/insert-order";

        StringRequest req = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            progressDialog.dismiss();
                            JSONObject jsonObject = new JSONObject(response);
                            Toast.makeText(getContext(), jsonObject.getString("mesg"),
                                    Toast.LENGTH_LONG).show();
                            Common.cartRepository.emptyCart();
                            Common.cartRepository.countCartItems();
                            Fragment fragment = new Home();
                            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                            ft.replace(R.id.content_egrocery,fragment);
                            ft.commit();
                            //startActivity(getActivity().getIntent());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(getContext(), error.getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("store_id", store_id);
                map.put("loyalty_points", points);
                map.put("payment_method", method);
                map.put("address_id", address_id);
                map.put("account_id", account_id);
                map.put("sub_total", sub_total);
                map.put("gst", gst);
                map.put("total", total);
                map.put("user_id", user_id);
                map.put("product_ids", product_ids);
                map.put("quantity", quantity);
                return map;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(req);
    }
}
