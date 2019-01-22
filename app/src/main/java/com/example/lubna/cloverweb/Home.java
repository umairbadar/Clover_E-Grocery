package com.example.lubna.cloverweb;
import android.app.ProgressDialog;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.textclassifier.TextLinks;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

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
import java.util.HashMap;
import java.util.List;
public class Home extends Fragment
{

    //URL to get All Data
    private static final String URL = "http://172.16.10.203/api/getAllProducts";
    private ExpandableListView dableListView;
    private ExpandableListViewAdapter listadapter;
    private List<String> listDataHeader;
    private HashMap<String,List<String>> listHash;
    TextView tv1,tv2,tv3;
    ViewFlipper viewflip;
    View view;
    CardView cardView;

    //Non Clover Products
    private RecyclerView myrecyclerview2;
    private RecyclerView.Adapter adapter;
    private List<Model_NonCloverProducts> products;

    //Clover Products
    private RecyclerView myrecyclerview3;
    private RecyclerView.Adapter CloverAdapter;
    private List<Model_CloverProducts> CloverProducts;

    //Latest Products
    private RecyclerView myrecyclerview4;
    private RecyclerView.Adapter LatestAdapter;
    private List<Model_LatestProducts> LatestProducts;


    private List<Product_Model> lstproduct;
    private List<Product_Model> lstlatest;
    private List<Product_Model> lstclover;
    private Button left,right;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable final Bundle savedInstanceState)
    {
        view=inflater.inflate(R.layout.home,container,false);

        //Non Clover Products
        myrecyclerview2 = view.findViewById(R.id.contact_recyclerview2);
        myrecyclerview2.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.HORIZONTAL,false));
        products = new ArrayList<>();
        FetchNonCloverProducts();

        //Clover Products
        myrecyclerview3 = view.findViewById(R.id.contact_recyclerview3);
        myrecyclerview3.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.HORIZONTAL,false));
        CloverProducts = new ArrayList<>();
        FetchCloverProducts();

        //Latest Products
        myrecyclerview4 = view.findViewById(R.id.contact_recyclerview4);
        myrecyclerview4.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.HORIZONTAL,false));
        LatestProducts = new ArrayList<>();
        FetchLatestProducts();

        dableListView =  view.findViewById(R.id.listmainexpand);
        initData();
        listadapter =  new ExpandableListViewAdapter(getContext(),listDataHeader,listHash);
        dableListView.setAdapter(listadapter);

        dableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener()
        {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id)
            {

                Fragment fragment = new Fragmentproductdetail();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.content_egrocery, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                return true;
            }
        });
        left= view.findViewById(R.id.left);
        right = view.findViewById(R.id.right);
        tv1 = view.findViewById(R.id.tv1);
        tv2 = view.findViewById(R.id.tv2);
        tv3 = view.findViewById(R.id.tv3);
        tv1.setPaintFlags(tv1.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        tv1.setText("Non Clover Products");
        tv2.setPaintFlags(tv2.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        tv2.setText("Clover Products");
        tv3.setPaintFlags(tv3.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        tv3.setText("Latest Products");
        right.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                LinearLayoutManager layoutManager = (LinearLayoutManager) myrecyclerview2.getLayoutManager();
                //layoutManager.scrollToPositionWithOffset(6 ,1);
                layoutManager.scrollToPositionWithOffset(1, 6);
            }
        });
        return view;
        }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewflip = view.findViewById(R.id.viewflip);
        int images[] ={R.drawable.spicetwo,R.drawable.pulsestwo,R.drawable.oiltwo,R.drawable.oilthree,R.drawable.spiceone,R.drawable.pulses1,R.drawable.spice3};
        for(int image:images)
        {
            flipimage(image);
        }
    }
    public void flipimage(int image)
    {
        ImageView imageView = new ImageView(getContext());
        imageView.setBackgroundResource(image);
        viewflip.addView(imageView);
        viewflip.setFlipInterval(4000);
        viewflip.setAutoStart(true);
        viewflip.setInAnimation(getContext(),android.R.anim.slide_in_left);
        viewflip.setOutAnimation(getContext(),android.R.anim.slide_out_right);
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        lstproduct = new ArrayList<>();
        lstclover = new ArrayList<>();
        lstlatest = new ArrayList<>();
        lstproduct.add(new Product_Model("Rice", "All kind of Rice", R.drawable.clover_logo_final));
        lstproduct.add(new Product_Model("Flours", "All kind of flours", R.drawable.clover_logo_final));
        lstproduct.add(new Product_Model("Staples","Different variety of staples",R.drawable.clover_logo_final));
        lstproduct.add(new Product_Model("Oils","All kind of Oils",R.drawable.clover_logo_final));
        lstproduct.add(new Product_Model("Spices","All variety of Spices",R.drawable.clover_logo_final));
        lstproduct.add(new Product_Model("Grains","All variety of Grains",R.drawable.clover_logo_final));
        lstproduct.add(new Product_Model("Banaspati Ghee","All variety of banaspati ghee",R.drawable.clover_logo_final));
        lstclover.add(new Product_Model("Rice", "All kind of Rice", R.drawable.clover_logo_final));
        lstclover.add(new Product_Model("Flours", "All kind of flours", R.drawable.clover_logo_final));
        lstclover.add(new Product_Model("Staples","Different variety of staples",R.drawable.clover_logo_final));
        lstclover.add(new Product_Model("Oils","All kind of Oils",R.drawable.clover_logo_final));
        lstclover.add(new Product_Model("Spices","All variety of Spices",R.drawable.clover_logo_final));
        lstclover.add(new Product_Model("Grains","All variety of Grains",R.drawable.clover_logo_final));
        lstclover.add(new Product_Model("Banaspati Ghee","All variety of banaspati ghee",R.drawable.clover_logo_final));
        lstlatest.add(new Product_Model("Rice", "All kind of Rice", R.drawable.clover_logo_final));
        lstlatest.add(new Product_Model("Flours", "All kind of flours", R.drawable.clover_logo_final));
        lstlatest.add(new Product_Model("Staples","Different variety of staples",R.drawable.clover_logo_final));
        lstlatest.add(new Product_Model("Oils","All kind of Oils",R.drawable.clover_logo_final));
        lstlatest.add(new Product_Model("Spices","All variety of Spices",R.drawable.clover_logo_final));
        lstlatest.add(new Product_Model("Grains","All variety of Grains",R.drawable.clover_logo_final));
        lstlatest.add(new Product_Model("Banaspati Ghee","All variety of banaspati ghee",R.drawable.clover_logo_final));
    }
    private void initData()
    {
        listDataHeader = new ArrayList<>();
        listHash = new HashMap<>();
        listDataHeader.add("Food");
        listDataHeader.add("Vegetables & Fruits");
        listDataHeader.add("Breakfast & Dairy");
        listDataHeader.add("Beverages");
        listDataHeader.add("Personal care");
        listDataHeader.add("Baby care");
        listDataHeader.add("Instand Food");
        listDataHeader.add("House Hold");
        listDataHeader.add("Meat & Sea Food");
        listDataHeader.add("Lubricants");
        List<String> first = new ArrayList<>();
        first.add("Rice");
        first.add("Flours");
        first.add("Staples");
        first.add("Oils");
        first.add("Spices");
        first.add("Grains");
        first.add("BanaspatiGhee");
        List<String> androidstudio = new ArrayList<>();
        androidstudio.add("Vegetable");
        androidstudio.add("Fruits");
        androidstudio.add("Packed Vegetables and Fruits");
        List<String> xamrin = new ArrayList<>();
        xamrin.add("Breakfast Products");
        xamrin.add("Dairy Products");
        xamrin.add("Milk");
        List<String> ump = new ArrayList<>();
        ump.add("Hot Beverages");
        ump.add("Cold Beverages");
        List<String> pc = new ArrayList<>();
        pc.add("Shampos");
        pc.add("Face Wash");
        List<String > babyc = new ArrayList<>();
        babyc.add("Baby Food");
        List<String> Instant = new ArrayList<>();
        Instant.add("Biscuit");
        Instant.add("Noddle");
        Instant.add("Ice cream");
        List<String> house = new ArrayList<>();
        house.add("Detergent");
        house.add("Furninshing and home need");
        house.add("Kitchen holds");
        List<String> Meat = new ArrayList<>();
        Meat.add("Meat");
        Meat.add("Poultry");
        Meat.add("Fish and Sea Food");
        List<String> Lubricants = new ArrayList<>();
        Lubricants.add("lubricants");
        listHash.put(listDataHeader.get(0),first);
        listHash.put(listDataHeader.get(1),androidstudio);
        listHash.put(listDataHeader.get(2),xamrin);
        listHash.put(listDataHeader.get(3),ump);
        listHash.put(listDataHeader.get(4),pc);
        listHash.put(listDataHeader.get(5),babyc);
        listHash.put(listDataHeader.get(6),Instant);
        listHash.put(listDataHeader.get(7),house);
        listHash.put(listDataHeader.get(8),Meat);
        listHash.put(listDataHeader.get(9),Lubricants);
    }

    public void FetchNonCloverProducts()
    {
        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Please Wait...");
        progressDialog.show();

        StringRequest req = new StringRequest(Request.Method.GET, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("non_clover_products");
                            for (int i = 0; i < jsonArray.length(); i++)
                            {
                                JSONObject object = jsonArray.getJSONObject(i);
                                String name = object.getString("product_name");
                                String price = object.getString("normalcustomer_price");
                                JSONObject innerObj = object.getJSONObject("findimage");
                                String image = "http://172.16.10.203/images/" + innerObj.getString("image");

                                Model_NonCloverProducts items = new Model_NonCloverProducts(
                                        name,
                                        price,
                                        image
                                );
                                products.add(items);
                            }

                            adapter = new Adapter_NonCloverProducts(products,getContext());
                            myrecyclerview2.setAdapter(adapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(getContext(),error.getMessage(),Toast.LENGTH_LONG).show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(req);
    }

    public void FetchCloverProducts()
    {
        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Please Wait...");
        progressDialog.show();

        StringRequest req = new StringRequest(Request.Method.GET, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("clover_products");
                            for (int i = 0; i < jsonArray.length(); i++)
                            {
                                JSONObject object = jsonArray.getJSONObject(i);
                                String name = object.getString("product_name");
                                String price = object.getString("normalcustomer_price");
                                JSONObject innerObj = object.getJSONObject("findimage");
                                String image = "http://172.16.10.203/images/" + innerObj.getString("image");

                                Model_CloverProducts items = new Model_CloverProducts(
                                        name,
                                        price,
                                        image
                                );
                                CloverProducts.add(items);
                            }

                            CloverAdapter = new Adapter_CloverProducts(CloverProducts,getContext());
                            myrecyclerview3.setAdapter(CloverAdapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(getContext(),error.getMessage(),Toast.LENGTH_LONG).show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(req);
    }

    public void FetchLatestProducts()
    {
        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Please Wait...");
        progressDialog.show();

        StringRequest req = new StringRequest(Request.Method.GET, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("latest_products");
                            for (int i = 0; i < jsonArray.length(); i++)
                            {
                                JSONObject object = jsonArray.getJSONObject(i);
                                String name = object.getString("product_name");
                                String price = object.getString("normalcustomer_price");
                                JSONObject innerObj = object.getJSONObject("findimage");
                                String image = "http://172.16.10.203/images/" + innerObj.getString("image");

                                Model_LatestProducts items = new Model_LatestProducts(
                                        name,
                                        price,
                                        image
                                );
                                LatestProducts.add(items);
                            }

                            LatestAdapter = new Adapter_LatestProducts(LatestProducts,getContext());
                            myrecyclerview4.setAdapter(LatestAdapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
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
