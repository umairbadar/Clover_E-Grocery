package com.example.lubna.cloverweb;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
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
import android.widget.EditText;
import android.widget.RelativeLayout;
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
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class FragmentPhoneVerification extends Fragment {

    FirebaseAuth firebaseAuth;

    ArrayList<String> Arr_area_name;
    ArrayList<String> Arr_area_id;

    View view;

    Geocoder geocoder;
    PlaceAutocompleteFragment places;

    ProgressDialog progressDialog;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    EditText et_phone, et_phone_code, et_username, et_house_no, et_street_name;
    Button btn_send_code, btn_next, btn_done, btn_address_submit, btn_cntn_shopping;
    FirebaseAuth mAuth;
    TextView tv_state, tv_city, tv_country;
    Spinner area_spinner;

    String codeSent;

    RelativeLayout relativeLayout01, relativeLayout02, relativeLayout03, relativeLayout04;

    String email,password,UserEmail, UserPassword, UserPhone, UserLat, UserLng, UserAreaID, UserCity, UserAddress, UserHouseNo, UserStreet;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_phone_verification, container, false);

        email = getArguments().getString("UserEmail");
        password = getArguments().getString("UserPassword");

        Arr_area_id = new ArrayList<>();
        Arr_area_name = new ArrayList<>();

        return view;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        firebaseAuth = FirebaseAuth.getInstance();

        //first layout
        relativeLayout01 = view.findViewById(R.id.relativeLayout01);
        relativeLayout01.setVisibility(View.VISIBLE);

        //second layout
        relativeLayout02 = view.findViewById(R.id.relativeLayout02);

        //third layout
        relativeLayout03 = view.findViewById(R.id.relativeLayout03);

        //fourth layout
        relativeLayout04 = view.findViewById(R.id.relativeLayout04);

        et_house_no = view.findViewById(R.id.et_house_no);
        et_street_name = view.findViewById(R.id.et_street_name);

        area_spinner = view.findViewById(R.id.area_spinner);

        tv_state = view.findViewById(R.id.tv_state);
        tv_city = view.findViewById(R.id.tv_city);
        tv_country = view.findViewById(R.id.tv_country);

        btn_address_submit = view.findViewById(R.id.btn_next01);


        //Place Auto Complete
        geocoder = new Geocoder(getContext());


        places = (PlaceAutocompleteFragment) getActivity().
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment01);

        places.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {

                String placename = (String) place.getAddress();

                try {
                    List<Address> addresses = geocoder.getFromLocation(place.getLatLng().latitude, place.getLatLng().longitude, 1);
                    String country = addresses.get(0).getCountryName();
                    String city = addresses.get(0).getLocality();
                    String state = addresses.get(0).getAdminArea();
                    tv_state.setText(state);
                    tv_city.setText(city);
                    tv_country.setText(country);
                    //Storing Data in SharedPreferences
                    editor.putString("UserCity", city);
                    editor.putString("UserLat", String.valueOf(place.getLatLng().latitude));
                    editor.putString("UserLng", String.valueOf(place.getLatLng().longitude));
                    editor.putString("UserAddress", placename);
                    editor.apply();


                    final String URL = "http://172.16.10.203/api/get_areas/" + city;
                    StringRequest req = new StringRequest(Request.Method.GET, URL,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {

                                    Arr_area_name.add("Select Area");
                                    try {
                                        btn_address_submit.setBackgroundColor(Color.parseColor("#1A9A55"));
                                        btn_address_submit.setEnabled(true);
                                        JSONArray jsonArray = new JSONArray(response);
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                                            String area_id = jsonObject.getString("area_id");
                                            String area_name = jsonObject.getString("area_name");
                                            Arr_area_id.add(area_id);
                                            Arr_area_name.add(area_name);
                                        }
                                        area_spinner.setAdapter(new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, Arr_area_name));
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

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setTitle("Please wait");
        progressDialog.setMessage("Loading...");

        //Edittext Username
        et_username = view.findViewById(R.id.et_username);

        //Button Done
        btn_done = view.findViewById(R.id.btn_done);

        sharedPreferences = getActivity().getSharedPreferences("Pre", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        mAuth = FirebaseAuth.getInstance();

        et_phone = view.findViewById(R.id.et_phone);
        et_phone_code = view.findViewById(R.id.et_phone_code);

        btn_send_code = view.findViewById(R.id.btn_send_code);
        btn_next = view.findViewById(R.id.btn_next);

        //Button Continue Shopping
        btn_cntn_shopping = view.findViewById(R.id.btn_cntn_shopping);

        btn_send_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendVerificationCode();
            }
        });

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressDialog.setCancelable(false);
                progressDialog.show();
                verifySignInCode();
                registerUserEmail(email,password);
            }
        });

        btn_address_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String houseno = et_house_no.getText().toString().trim();
                String streetname = et_street_name.getText().toString().trim();

                if (houseno.isEmpty()) {
                    et_house_no.setError("Please enter House No");
                    et_house_no.requestFocus();
                } else {
                    editor.putString("UserHouseNo", houseno);
                    editor.putString("UserStreet", streetname);
                    editor.apply();
                    if (streetname.isEmpty()) {
                        et_street_name.setError("Please enter Street Name");
                        et_street_name.requestFocus();
                    } else {
                        if (area_spinner.getSelectedItemPosition() == 0) {
                            Toast.makeText(getContext(), "Please Select Area!", Toast.LENGTH_LONG).show();
                        } else {

                            //Getting Data from SharedPreferences
                            SharedPreferences sp = getActivity().getSharedPreferences("Pre", Context.MODE_PRIVATE);
                            UserEmail = sp.getString("UserEmail", "");
                            UserPassword = sp.getString("UserPassword", "");
                            UserPhone = sp.getString("UserPhone", "");
                            UserLat = sp.getString("UserLat", "");
                            UserLng = sp.getString("UserLng", "");
                            UserAreaID = sp.getString("UserAreaID", "");
                            UserCity = sp.getString("UserCity", "");
                            UserAddress = sp.getString("UserAddress", "");
                            UserHouseNo = sp.getString("UserHouseNo", "");
                            UserStreet = sp.getString("UserStreet", "");

                            relativeLayout01.setVisibility(View.GONE);
                            relativeLayout02.setVisibility(View.GONE);
                            relativeLayout03.setVisibility(View.VISIBLE);
                        }
                    }
                }
            }
        });

        btn_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences sp = getActivity().getSharedPreferences("Pre", Context.MODE_PRIVATE);
                UserEmail = sp.getString("UserEmail", "");
                UserPassword = sp.getString("UserPassword", "");

                final String username = et_username.getText().toString().trim();

                if (username.isEmpty()) {
                    et_username.setError("Please enter Username");
                    et_username.requestFocus();
                } else {

                    firebaseAuth.signInWithEmailAndPassword(UserEmail, UserPassword)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {

                                    if (firebaseAuth.getCurrentUser().isEmailVerified()) {
                                        editor.putString("UserFname", username);
                                        editor.apply();

                                        final String URL_SIGNUP = "http://172.16.10.203/api/sign-up-customer";
                                        StringRequest req = new StringRequest(Request.Method.POST, URL_SIGNUP,
                                                new Response.Listener<String>() {
                                                    @Override
                                                    public void onResponse(String response) {

                                                        try {
                                                            JSONObject jsonObject = new JSONObject(response);
                                                            String msg = jsonObject.getString("msg");
                                                            if (msg.equals("success")) {
                                                                //Toast.makeText(getContext(),"Account Created!",Toast.LENGTH_LONG).show();
                                                                Fragment fragment = new loginfragment();
                                                                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                                                                ft.replace(R.id.content_egrocery,fragment);
                                                                ft.commit();
                                                                /*relativeLayout01.setVisibility(View.GONE);
                                                                relativeLayout02.setVisibility(View.GONE);
                                                                relativeLayout03.setVisibility(View.GONE);
                                                                relativeLayout04.setVisibility(View.VISIBLE);*/
                                                            } else {
                                                                Toast.makeText(getContext(), "Error", Toast.LENGTH_LONG).show();
                                                            }

                                                        } catch (JSONException e) {
                                                            e.printStackTrace();
                                                        }

                                                    }
                                                },
                                                new Response.ErrorListener() {
                                                    @Override
                                                    public void onErrorResponse(VolleyError error) {
                                                        Toast.makeText(getContext(), error.toString(), Toast.LENGTH_LONG).show();
                                                    }
                                                }) {
                                            @Override
                                            protected Map<String, String> getParams() throws AuthFailureError {
                                                Map<String, String> map = new HashMap<String, String>();
                                                map.put("firstname", username);
                                                map.put("customer_email", UserEmail);
                                                map.put("password", UserPassword);
                                                map.put("cell", UserPhone);
                                                map.put("longitude", UserLng);
                                                map.put("latitude", UserLat);
                                                map.put("area", UserAreaID);
                                                map.put("city", UserCity);
                                                map.put("address", UserAddress);
                                                map.put("house_no", UserHouseNo);
                                                map.put("block_street", UserStreet);
                                                return map;
                                            }
                                        };

                                        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
                                        requestQueue.add(req);
                                    }

                                    else
                                    {
                                        Toast.makeText(getContext(), "Verify your Email to continue",
                                                Toast.LENGTH_LONG).show();
                                    }

                                }
                            });

                }
            }
        });

        area_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String a_id = Arr_area_id.get(position);
                editor.putString("UserAreaID", a_id);
                editor.apply();

                //Toast.makeText(getContext(),a_id,Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btn_cntn_shopping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Fragment fragment = new Home();
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.content_egrocery, fragment);
                ft.commit();
            }
        });
    }

    private void verifySignInCode() {
        String code = et_phone_code.getText().toString();

        if (code.isEmpty()) {
            progressDialog.dismiss();
            et_phone_code.setError("Please enter code");
            et_phone_code.requestFocus();
            return;
        }
        if (code.length() < 6) {
            progressDialog.dismiss();
            et_phone_code.setError("Please enter valid code");
            et_phone_code.requestFocus();
            return;
        }

        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(codeSent, code);
        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            String phone = "+92" + et_phone.getText().toString().trim();
                            //btn_next.setText("Verified");
                            editor.putString("UserPhone", phone);
                            editor.apply();
                            progressDialog.dismiss();
                            relativeLayout01.setVisibility(View.GONE);
                            relativeLayout02.setVisibility(View.VISIBLE);
                            relativeLayout03.setVisibility(View.GONE);

                        } else {
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {

                                //btn_next.setText("Incorrect");
                                Toast.makeText(getContext(), "Incorrect Code", Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                });
    }

    private void sendVerificationCode() {
        String phone = "+92" + et_phone.getText().toString();

        if (phone.isEmpty()) {
            et_phone.setError("Phone number is required");
            et_phone.requestFocus();
            return;
        }

        if (phone.length() < 10) {
            et_phone.setError("Please enter a valid phone");
            et_phone.requestFocus();
            return;
        }


        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phone,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                getActivity(),               // Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallbacks

        new CountDownTimer(60000, 1000) {
            public void onTick(long millisUntilFinished) {
                btn_send_code.setEnabled(false);
                btn_send_code.setBackgroundColor(Color.LTGRAY);
                btn_send_code.setText("00:" + millisUntilFinished / 1000);
            }

            public void onFinish() {
                btn_send_code.setEnabled(true);
                btn_send_code.setBackgroundColor(Color.parseColor("#1A9A55"));
                btn_send_code.setText("Resend");
            }
        }.start();
    }

    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

        }

        @Override
        public void onVerificationFailed(FirebaseException e) {

        }

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);

            codeSent = s;
        }
    };

    public void registerUserEmail(String email, String password)
    {
        firebaseAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful())
                        {
                            sendEmailVerification();
                        }
                        else
                        {
                            if(task.getException()instanceof FirebaseAuthUserCollisionException)
                            {
                                progressDialog.dismiss();
                                Toast.makeText(getContext(), "You are already Registered ", Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                progressDialog.dismiss();
                                Toast.makeText(getContext(),task.getException().getMessage(),Toast.LENGTH_LONG).show();
                            }
                        }

                    }
                });
    }

    private void sendEmailVerification() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user!=null){
            user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        //progressDialog.dismiss();
                        /*Fragment fragment = new FragmentPhoneVerification();
                        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                        ft.replace(R.id.content_egrocery,fragment);
                        ft.commit();*/
                        Toast.makeText(getContext(),"Registered Successfully! Check your email for verification",Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

    }


}