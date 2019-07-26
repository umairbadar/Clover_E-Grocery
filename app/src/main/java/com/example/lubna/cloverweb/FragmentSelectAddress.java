package com.example.lubna.cloverweb;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.lubna.cloverweb.Utils.Common;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

import static java.security.AccessController.getContext;

public class FragmentSelectAddress extends Fragment {

    String a_id = "";

    private SharedPreferences sp;
    private SharedPreferences.Editor editor;

    private Spinner spinner_address;
    private ArrayList<String> addresses;
    private ArrayList<String> address_id;

    //Getting User ID from sharedPreferences
    private String UserID, Selected_Address, total, sub_total, gst;

    private Button btn_add_address, btn_submit_address;

    private TextView tv_selecetedAddress;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final ViewGroup rootview = (ViewGroup) inflater.inflate(R.layout.fragment_select_address, container, false);
        return rootview;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getArguments() != null) {
            total = getArguments().getString("TotalAmount");
            gst = getArguments().getString("GST");
            sub_total = getArguments().getString("SubTotal");
        }

        tv_selecetedAddress = view.findViewById(R.id.tv_selectedAddress);

        btn_submit_address = view.findViewById(R.id.btn_submit_address);
        btn_add_address = view.findViewById(R.id.btn_add_address);

        sp = getActivity().getSharedPreferences("Pre", Context.MODE_PRIVATE);
        UserID = sp.getString("UserID", "");

        spinner_address = view.findViewById(R.id.spinner_address);

        addresses = new ArrayList<>();
        address_id = new ArrayList<>();

        getUserAddresses();

        btn_submit_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (spinner_address.getSelectedItemPosition() == 0) {
                    Toast.makeText(getContext(), "Please Select Address!",
                            Toast.LENGTH_LONG).show();
                } else {
                    Selected_Address = spinner_address.
                            getItemAtPosition(spinner_address.getSelectedItemPosition()).toString();

                    editor = sp.edit();
                    editor.putString("UserSelectedAddress", Selected_Address);
                    editor.apply();

                   /* Toast.makeText(getContext(), Selected_Address,
                            Toast.LENGTH_LONG).show();*/

                    Fragment fragment = new FragmentPaymentMethod();
                    Bundle args = new Bundle();
                    args.putString("TotalAmount", total);
                    args.putString("GST", gst);
                    args.putString("SubTotal", sub_total);
                    fragment.setArguments(args);
                    FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.content_egrocery, fragment);
                    ft.addToBackStack(null);
                    ft.commit();
                }
            }
        });

        btn_add_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Fragment fragment = new FragmentAddAddress();
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.content_egrocery, fragment);
                ft.addToBackStack(null);
                ft.commit();
            }
        });

        spinner_address.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                if (spinner_address.getSelectedItemPosition() == 0)
                    tv_selecetedAddress.setVisibility(View.GONE);
                else {
                    tv_selecetedAddress.setVisibility(View.VISIBLE);
                    a_id = address_id.get(position);
                    String address = spinner_address.getItemAtPosition(position).toString();
                    tv_selecetedAddress.setText("Selected Address: " + address);
                    editor = sp.edit();
                    editor.putString("address_id", a_id);
                    editor.apply();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    private void getUserAddresses() {

        final String URL_GetAddresses = "http://172.16.10.203/api/getCustomerAddresses/" + UserID;

        StringRequest req = new StringRequest(Request.Method.GET, URL_GetAddresses,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            addresses.add("Select Address");
                            address_id.add("0");
                            JSONArray jsonArray = new JSONArray(response);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                String id = jsonObject.getString("address_id");
                                String address = jsonObject.getString("address");
                                address_id.add(id);
                                addresses.add(address);
                            }
                           /* ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),
                                    R.layout.multiline_spinner_dropdown_item,addresses);
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spinner_address.setAdapter(adapter);*/

                            spinner_address.setAdapter(new ArrayAdapter<>(getContext(),
                                    android.R.layout.simple_spinner_dropdown_item, addresses));

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
