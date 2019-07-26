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
import android.widget.GridView;
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

public class ProductDetailsFragment extends Fragment
{
    private String ID,StoreID;

    //Display Images
    private GridView gridView;
    private ArrayList<Model_ProductDetails> products;
    Adapter_ProductDetails gridViewAdapter;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.productdetailsfragment, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        if (getArguments() != null) {
            ID = getArguments().getString("id");
        }
        //Toast.makeText(getContext(),ID,Toast.LENGTH_LONG).show();
        //Display Images
        gridView = (GridView) view.findViewById(R.id.gridView);
        products = new ArrayList<>();
        FetchProductDetails();
        }
    public void FetchProductDetails()
    {   SharedPreferences pref = getActivity().getSharedPreferences("MyPre", Context.MODE_PRIVATE);
        StoreID = pref.getString("StoreID","");
        final String SHOW_IMAGE_URL = "http://172.16.10.203/api/getCategoryProducts/" + ID + "," + StoreID;
        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Please Wait");
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        progressDialog.setCancelable(false);
        StringRequest req = new StringRequest(Request.Method.GET, SHOW_IMAGE_URL,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response)
                    {
                        try
                        {
                            progressDialog.dismiss();
                            JSONArray jsonArray = new JSONArray(response);
                            for (int i = 0; i < jsonArray.length(); i++)
                            {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                String id = jsonObject.getString("product_id");
                                String name = jsonObject.getString("product_name");
                                String price = jsonObject.getString("normalcustomer_price");
                                JSONObject Obj = jsonObject.getJSONObject("findimage");
                                String image = "http://172.16.10.203/images/" + Obj.getString("image");
                                Model_ProductDetails items = new Model_ProductDetails(
                                        id,
                                        name,
                                        price,
                                        image
                                );
                                products.add(items);
                            }
                        }
                        catch (JSONException e)
                        {
                            e.printStackTrace();
                        }
                        gridViewAdapter = new Adapter_ProductDetails(getContext(),products);
                        gridView.setAdapter(gridViewAdapter);
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(req);
    }
}