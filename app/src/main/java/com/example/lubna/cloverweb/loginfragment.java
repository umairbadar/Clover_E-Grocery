package com.example.lubna.cloverweb;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
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
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;
import static com.google.android.gms.internal.zzip.runOnUiThread;
public class loginfragment extends Fragment implements View.OnClickListener
{
    View view;
    public static final  String Pre = "MyPre";
    public static final String LOGIN_URL = "http://172.16.10.202/api/checkLogin";
    public static final String KEY_USERNAME = "email";
    public static final String KEY_PASSWORD = "password";
    private EditText editTextUsername;
    private EditText editTextPassword;
    private TextView newhere,signup;
    private Button buttonLogin;
    private String username;
    private String password;
    SharedPreferences sp;
    SharedPreferences.Editor edit;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        view= inflater.inflate(R.layout.fragment_login, container, false);
        return view;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        buttonLogin = view.findViewById(R.id.Elogin);
        editTextUsername = view.findViewById(R.id.email);
        editTextPassword = view.findViewById(R.id.password);
        newhere = view.findViewById(R.id.newhere);
        signup = view.findViewById(R.id.signup);
        sp = getContext().getSharedPreferences(Pre, Context.MODE_PRIVATE);
        buttonLogin.setOnClickListener(this);
        newhere.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent i = new Intent(getContext(),SIgnup.class);
                startActivity(i);
            }
        });
        signup.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent j = new Intent(getContext(),SIgnup.class);
                startActivity(j);
            }
        });
    }
    public boolean CheckFieldValidation()
    {
        boolean valid = true;
        if (editTextUsername.getText().toString().equals(""))
        {
            editTextUsername.setError("Can't be Empty");
            valid = false;
        }
        else if (editTextPassword.getText().toString().equals(""))
        {
            editTextPassword.setError("Can't be Empty");
            valid = false;
        }
        return valid;
    }
    private void userLogin()
    {
        username = editTextUsername.getText().toString().trim();
        password = editTextPassword.getText().toString().trim();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, LOGIN_URL,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response)
                    {
                        if(response != null)
                        {
                            try
                            {
                                JSONObject json = new JSONObject(response.substring(response.indexOf("{"), response.lastIndexOf("}") + 1));
                                String msg = json.getString("msg");
                                if(msg.equals("success"))
                                {
                                    edit = sp.edit();
                                    edit.putString("customerid",json.getString("customer_id"));
                                    edit.putString("customername",json.getString("customer_fname"));
                                    edit.putString("email",username);
                                    edit.apply();
                                    //edit.commit();
                                    openProfile();
                                }
                                else if(msg.equals("error"))
                                {
                                    Toast.makeText(getContext(), "Your Email Or Password is Incorrect", Toast.LENGTH_LONG).show();
                                }
                            }
                            catch (final JSONException e)
                            {
                                runOnUiThread(new Runnable()
                                {
                                    @Override
                                    public void run()
                                    {
                                        Toast.makeText(getContext(), "Json parsing error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                                    }
                                });
                            }
                        }
                        else
                        {
                            Toast.makeText(getContext(),
                                    "Couldn't get json from server. Check LogCat for possible errors!", Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        Toast.makeText(getContext(), error.toString(), Toast.LENGTH_LONG).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError
            {
                Map<String, String> map = new HashMap<String, String>();
                map.put(KEY_USERNAME, username);
                map.put(KEY_PASSWORD, password);
                return map;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }
    private void openProfile()
    {
        Intent intent = new Intent(getContext(), egrocery.class);
        intent.putExtra(KEY_USERNAME, username);
        startActivity(intent);
    }
    public void onClick(View v)
    {
        isNetworkAvailable();
        edit = sp.edit();
        edit.putBoolean("saveLogin",true);
        edit.apply();
        Intent intent = new Intent(getContext(), egrocery.class);
        //intent.putExtra(KEY_USERNAME, username);
        startActivity(intent);
    }
    public boolean isNetworkAvailable()
    {
        ConnectivityManager cm = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected())
        {
            CheckFieldValidation();
            userLogin();
            return true;
        }
        else if(networkInfo == null)
        {
            Toast.makeText(getContext(),"No internet Connection",Toast.LENGTH_LONG).show();
            return false;
        }
        return false;
    }
}
