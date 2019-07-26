package com.example.lubna.cloverweb;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.daimajia.androidanimations.library.specials.out.TakingOffAnimator;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FragmentAddAddress extends Fragment {

    ArrayList<String> Arr_area_name;
    ArrayList<String> Arr_area_id;

    Geocoder geocoder;
    PlaceAutocompleteFragment places;

    TextView tv_state, tv_city, tv_country;
    EditText et_house_no, et_street_name;
    Spinner area_spinner;
    Button btn_add;

    String placename, country, city, state, lat, lng, user_id, area_id;

    private static View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //final ViewGroup rootview = (ViewGroup) inflater.inflate(R.layout.fragment_add_address,
        // container, false);

        if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null)
                parent.removeView(view);
        }
        try {
            view = inflater.inflate(R.layout.fragment_add_address, container, false);
        } catch (InflateException e) {
            /* map is already there, just return view as it is */
        }

        Arr_area_id = new ArrayList<>();
        Arr_area_name = new ArrayList<>();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SharedPreferences sp = getActivity().getSharedPreferences("Pre", Context.MODE_PRIVATE);
        user_id = sp.getString("UserID", "");
        //Toast.makeText(getContext(),user_id,Toast.LENGTH_LONG).show();

        btn_add = view.findViewById(R.id.btn_add);

        tv_state = view.findViewById(R.id.tv_state);
        tv_city = view.findViewById(R.id.tv_city);
        tv_country = view.findViewById(R.id.tv_country);

        et_house_no = view.findViewById(R.id.et_house_no);
        et_street_name = view.findViewById(R.id.et_street_name);

        area_spinner = view.findViewById(R.id.area_spinner);

        area_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                area_id = Arr_area_id.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        geocoder = new Geocoder(getContext());

        places = (PlaceAutocompleteFragment) getActivity().getFragmentManager().
                findFragmentById(R.id.place_autocomplete_fragment02);


        places.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {

                placename = (String) place.getAddress();

                try {

                    List<Address> addresses = geocoder.
                            getFromLocation(place.getLatLng().latitude, place.getLatLng().longitude, 1);
                    lat = String.valueOf(place.getLatLng().latitude);
                    lng = String.valueOf(place.getLatLng().longitude);
                    country = addresses.get(0).getCountryName();
                    city = addresses.get(0).getLocality();
                    state = addresses.get(0).getAdminArea();
                    tv_state.setText(state);
                    tv_city.setText(city);
                    tv_country.setText(country);


                    final String URL = "http://172.16.10.203/api/get_areas/" + city;
                    StringRequest req = new StringRequest(Request.Method.GET, URL,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {

                                    Arr_area_name.add("Select Area");
                                    try {
                                        JSONArray jsonArray = new JSONArray(response);
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                                            String area_id = jsonObject.getString("area_id");
                                            String area_name = jsonObject.getString("area_name");
                                            Arr_area_id.add(area_id);
                                            Arr_area_name.add(area_name);
                                        }
                                        area_spinner.setAdapter(new ArrayAdapter<>(getContext(),
                                                android.R.layout.simple_spinner_dropdown_item, Arr_area_name));
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

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }


            @Override
            public void onError(Status status) {
                Toast.makeText(getContext(), status.toString(), Toast.LENGTH_SHORT).show();
            }


        });

        AutocompleteFilter autocompleteFilter = new AutocompleteFilter.Builder().setCountry("PK").build();
        places.setFilter(autocompleteFilter);
        places.getView().setBackgroundColor(Color.parseColor("#DDDDDD"));


        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String houseno = et_house_no.getText().toString().trim();
                final String streetname = et_street_name.getText().toString().trim();

                if (houseno.isEmpty()) {
                    et_house_no.setError("Please enter House No");
                    et_house_no.requestFocus();
                } else {
                    if (streetname.isEmpty()) {
                        et_street_name.setError("Please enter Street Name");
                        et_street_name.requestFocus();
                    } else {
                        if (area_spinner.getSelectedItemPosition() == 0) {
                            Toast.makeText(getContext(), "Please Select Area!", Toast.LENGTH_LONG).show();
                        } else {
                            final String URL_ADD_ADDRESS = "http://172.16.10.203/api/addAddress";

                            StringRequest req = new StringRequest(Request.Method.POST, URL_ADD_ADDRESS,
                                    new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {

                                            try {
                                                JSONObject jsonObject = new JSONObject(response);
                                                String msg = jsonObject.getString("msg");
                                                if (msg.equals("success")) {
                                                    Fragment fragment = new FragmentSelectAddress();
                                                    FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                                                    ft.replace(R.id.content_egrocery, fragment);
                                                    ft.addToBackStack(null);
                                                    ft.commit();
                                                } else {
                                                    Toast.makeText(getContext(), "Error!",
                                                            Toast.LENGTH_LONG).show();
                                                }
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }

                                        }
                                    },
                                    new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {

                                        }
                                    }) {
                                @Override
                                protected Map<String, String> getParams() throws AuthFailureError {
                                    Map<String, String> map = new HashMap<String, String>();
                                    map.put("area_id", area_id);
                                    map.put("address", placename);
                                    map.put("longitude", lng);
                                    map.put("latitude", lat);
                                    map.put("city_name", city);
                                    map.put("house_no", houseno);
                                    map.put("block_street", streetname);
                                    map.put("user_id", user_id);
                                    return map;
                                }
                            };

                            RequestQueue requestQueue = Volley.newRequestQueue(getContext());
                            requestQueue.add(req);
                        }
                    }
                }
            }
        });
    }
}
