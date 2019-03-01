package com.example.lubna.cloverweb;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class loginfragment extends Fragment {

    View view;
    private TextView tv_create_account;

    //Input Feilds
    private EditText et_username, et_password;

    //Login Button
    private Button btn_login;

    private FirebaseAuth firebaseAuth;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private ProgressDialog progressDialog;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_login, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        progressDialog = new ProgressDialog(getContext());

        sharedPreferences = getActivity().getSharedPreferences("Pre", Context.MODE_PRIVATE);


        firebaseAuth = FirebaseAuth.getInstance();

        et_username = view.findViewById(R.id.et_username);
        et_password = view.findViewById(R.id.et_password);

        //Login Button
        btn_login = view.findViewById(R.id.btn_login);

        tv_create_account = view.findViewById(R.id.tv_create_account);
        tv_create_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Fragment fragment = new Fragment_Signup();
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.content_egrocery, fragment);
                ft.addToBackStack(null);
                ft.commit();
            }
        });

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                UserLogin();
            }
        });
    }

    private void UserLogin() {
        progressDialog.setTitle("Please wait");
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        progressDialog.setCancelable(false);
        final String username = et_username.getText().toString().trim();
        final String password = et_password.getText().toString().trim();

        if (username.isEmpty()) {
            progressDialog.dismiss();
            et_username.setError("Please enter Username");
            et_username.requestFocus();
        } else {
            if (password.isEmpty()) {
                progressDialog.dismiss();
                et_password.setError("Please enter Password");
                et_password.requestFocus();
            } else {
                editor = sharedPreferences.edit();
                editor.putBoolean("UserLogin", true);
                editor.apply();

                final String URL_LOGIN = "http://172.16.10.203/api/signin";

                StringRequest req = new StringRequest(Request.Method.POST, URL_LOGIN,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    String msg = jsonObject.getString("msg");
                                    if (msg.equals("success")) {
                                        progressDialog.dismiss();

                                        JSONObject innerObj = jsonObject.getJSONObject("user");
                                        String user_id = innerObj.getString("id");
                                        editor.putString("UserID", user_id);
                                        editor.apply();

                                        Fragment fragment = new Home();
                                        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                                        ft.replace(R.id.content_egrocery,fragment);
                                        ft.commit();


                                    } else {
                                        progressDialog.dismiss();
                                        Toast.makeText(getContext(), "Error",
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

                                Toast.makeText(getContext(), error.getMessage(),
                                        Toast.LENGTH_LONG).show();
                            }
                        }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> map = new HashMap<String, String>();
                        map.put("email", username);
                        map.put("password", password);
                        return map;
                    }
                };

                RequestQueue requestQueue = Volley.newRequestQueue(getContext());
                requestQueue.add(req);
            }
        }
    }
}
