package com.example.lubna.cloverweb;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Fragmentproductdetail extends Fragment
{
    //Child Category ID
    private String Cat_ID;



    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private List<Model_SubCategoryList> products;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragmentproductdetailtab, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        if (getArguments() != null)
        {
            Cat_ID = getArguments().getString("id");
        }
        //Toast.makeText(getContext(),Cat_ID,Toast.LENGTH_LONG).show();
        recyclerView = view.findViewById(R.id.recyclerView_SubCat);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL,false));
        products = new ArrayList<>();
        FetchSubCatList();
    }
    public void FetchSubCatList()
    {
        String URL = "http://172.16.10.203/api/getChildCats/"+Cat_ID;
        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Please Wait...");
        progressDialog.show();
        StringRequest req = new StringRequest(Request.Method.GET, URL,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response)
                    {
                        progressDialog.dismiss();
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            for (int i = 0; i < jsonArray.length(); i++)
                            {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                String cat_id = jsonObject.getString("category_id");
                                String cat_title = jsonObject.getString("cat_title");
                                String description = jsonObject.getString("description");
                                String cat_image = jsonObject.getString("cat_image");

                                Model_SubCategoryList items = new Model_SubCategoryList(
                                        cat_id,
                                        cat_title,
                                        description,
                                        cat_image
                                );
                                products.add(items);
                            }
                            adapter = new Adapter_SubCategoryList(products,getContext());
                            recyclerView.setAdapter(adapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(getContext(),error.getMessage(),Toast.LENGTH_LONG).show();
                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(req);
    }
}