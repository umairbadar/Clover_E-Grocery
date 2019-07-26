package com.example.lubna.cloverweb;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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

public class FragmentPaymentMethod extends Fragment {

    LinearLayout loyalty_layout,layout3;

    private Button btn_submit_payment, confirm;
    private RadioGroup radioGroup;
    private EditText utiliseloyaltypoints;
    String gettotal1, getdat, points;
    TextView loyaltypoints, totalamount, finalamount;
    String user_id, total,sub_total,gst;
    float f_price;
    private RadioButton cash, ebatwa;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final ViewGroup rootview = (ViewGroup) inflater.inflate(R.layout.fragment_payment_method, container, false);
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("Pre", Context.MODE_PRIVATE);
        user_id = sharedPreferences.getString("UserID", "");
        gettotal1 = sharedPreferences.getString("value1", "");
        return rootview;
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getArguments() != null) {

            total = getArguments().getString("TotalAmount");
            sub_total = getArguments().getString("SubTotal");
            gst = getArguments().getString("GST");
        }

        loyalty_layout = view.findViewById(R.id.loyalty_layout);
        layout3 = view.findViewById(R.id.layout3);

        utiliseloyaltypoints = view.findViewById(R.id.cashedit);
        btn_submit_payment = view.findViewById(R.id.btn_submit_payment);
        radioGroup = view.findViewById(R.id.radioGroup1);
        cash = view.findViewById(R.id.radio0);
        ebatwa = view.findViewById(R.id.radio1);
        loyaltypoints = view.findViewById(R.id.tvpheading);
        totalamount = view.findViewById(R.id.tvpheading0);
        totalamount.setText(total);
        totalamount.setGravity(Gravity.LEFT);
        finalamount = view.findViewById(R.id.finalamount);
        finalamount.setText(total);
        f_price = Float.parseFloat(total);
        confirm = view.findViewById(R.id.btnconfirm);

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (utiliseloyaltypoints.getText().toString().length() < 1) {
                    utiliseloyaltypoints.setError("Please enter loyalty points");
                    utiliseloyaltypoints.requestFocus();
                }
                else
                {
                    getdat = utiliseloyaltypoints.getText().toString();
                    int lp = Integer.parseInt(getdat);
                    float totalfromapi = Float.parseFloat( total);
                    f_price = totalfromapi - lp;
                    int point = Integer.parseInt(points);

                    if (lp <= point) {
                        finalamount.setText(String.valueOf(f_price));
                    } else {
                        utiliseloyaltypoints.setError("Kindly Enter points less than or equals to Loyalty Points");
                        finalamount.setText(null);
                    }
                }
            }
        });
        fetchdata();
        btn_submit_payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cash.isChecked()) {
                    Fragment fragment = new FragmentCash();
                    Bundle args = new Bundle();
                    args.putString("TotalAmount", String.valueOf(f_price));
                    if (utiliseloyaltypoints.getText().toString().length() > 0)
                        args.putString("points", String.valueOf(utiliseloyaltypoints.getText().toString()));
                    else
                        args.putString("points", "null");
                    args.putString("head", "Checkout | Via C.O.D.");
                    args.putString("method", "cash-on-delivery");
                    args.putString("account_id", "null");
                    args.putString("SubTotal", sub_total);
                    args.putString("GST", gst);
                    fragment.setArguments(args);
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.content_egrocery, fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                } else if (ebatwa.isChecked()) {
                    Fragment fragment1 = new FragmentEbatwa();
                    Bundle args = new Bundle();
                    args.putString("TotalAmount", String.valueOf(f_price));
                    if (utiliseloyaltypoints.getText().toString().length() > 0)
                        args.putString("points", String.valueOf(utiliseloyaltypoints.getText().toString()));
                    else
                        args.putString("points", "null");
                    args.putString("SubTotal", sub_total);
                    args.putString("GST", gst);
                    fragment1.setArguments(args);
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.content_egrocery, fragment1);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                } else if (!ebatwa.isChecked() || !cash.isChecked()) {
                    Toast.makeText(getContext(), "Kinldy select the Payment Method", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void fetchdata() {
        final String URL = "http://172.16.10.203/api/customer-complete/" + user_id;
        StringRequest req = new StringRequest(Request.Method.GET, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                if (!jsonObject.isNull("find_points")) {
                                    JSONObject obj = jsonObject.getJSONObject("find_points");
                                    points = obj.getString("points");
                                    //Toast.makeText(getContext(), "Hello",
                                    // Toast.LENGTH_LONG).show();
                                    loyaltypoints.setText(points);
                                    loyaltypoints.setGravity(Gravity.LEFT);
                                }
                                else
                                {
                                    loyalty_layout.setVisibility(View.GONE);
                                    layout3.setVisibility(View.GONE);
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(req);
    }
}

