package com.example.lubna.cloverweb;

import android.app.ProgressDialog;
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
import android.widget.LinearLayout;
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

public class FragmentEbatwa extends Fragment {

    LinearLayout layout_error;

    public static String total,points,sub_total,gst;
    String user_id;
    View view;
    TextView ebatwa2;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private List<Model_Ebatwa> m_accounts;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_ebatwa, container, false);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("Pre", Context.MODE_PRIVATE);
        user_id = sharedPreferences.getString("UserID", "");

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getArguments() != null) {
            total = getArguments().getString("TotalAmount");
            points = getArguments().getString("points");
            sub_total = getArguments().getString("SubTotal");
            gst = getArguments().getString("GST");
        }
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false));
        m_accounts = new ArrayList<>();
        FetchMembershipAccunts();

        layout_error = view.findViewById(R.id.layout_error);

    }

    private void FetchMembershipAccunts() {

        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Please wait");
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        final String URL = "http://172.16.10.203/api/customer-complete/" + user_id;

        StringRequest req = new StringRequest(Request.Method.GET, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                if (!jsonObject.isNull("0")) {
                                    progressDialog.dismiss();
                                    JSONObject innerObj = jsonObject.getJSONObject("0");
                                    JSONObject Obj = innerObj.getJSONObject("memberships");
                                    JSONArray innerArray = Obj.getJSONArray("original");
                                    for (int j = 0; j < innerArray.length(); j++) {
                                        JSONObject Object = innerArray.getJSONObject(j);
                                        String issue_date = Object.getString("issue_date");
                                        String[] i_d = issue_date.split("\\s+");
                                        String expiry_date = Object.getString("expiry_date");
                                        String[] e_d = expiry_date.split("\\s+");
                                        JSONObject find_member = Object.getJSONObject("find_member_account");
                                        String branch_name = find_member.getString("branch_name");
                                        String account_id = find_member.getString("account_id");
                                        String account_no = find_member.getString("account_no");
                                        String account_title = find_member.getString("account_title");
                                        String account_balance = find_member.getString("account_balance");

                                        Model_Ebatwa items = new Model_Ebatwa(
                                                account_id,
                                                i_d[0],
                                                e_d[0],
                                                account_title,
                                                branch_name,
                                                account_no,
                                                account_balance
                                        );

                                        m_accounts.add(items);
                                    }
                                } else {
                                    progressDialog.dismiss();
                                    recyclerView.setVisibility(View.GONE);
                                    layout_error.setVisibility(View.VISIBLE);
                                }
                            }

                            adapter = new Adapter_Ebatwa(m_accounts, getContext());
                            recyclerView.setAdapter(adapter);

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
                });

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(req);
    }
}

