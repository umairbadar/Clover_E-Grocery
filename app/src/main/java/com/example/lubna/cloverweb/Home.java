package com.example.lubna.cloverweb;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import dmax.dialog.SpotsDialog;

public class Home extends Fragment {

    ArrayList<String> sellstore;
    private String child_cat_id;
    Dialog MyDialog;
    SharedPreferences sp;
    SharedPreferences.Editor edit;
    Boolean location = false;
    ArrayList<String> stores;
    ArrayList<String> storesid;
    Spinner spinner;
    Button submitstore;
    String val;
    String value;
    String packid = "";
    EditText addaddress;
    Button submitbtn;
    //Expandable ListView
    private static final String URL_getAllCategories = "http://172.16.10.203/api/getAllCategories";
    private ExpandableListView expListView;
    private ExpandableListAdapter explistadapter;
    private List<String> listDataHeader;
    private HashMap<String, List<String>> listDataChild;
    EditText input, editText;
    Button gelatln;
    Button addressButton;
    TextView addressTV;
    TextView latLongTV;
    TextView tv1, tv2, tv3;
    ViewFlipper viewflip;
    View view;
    CardView cardView;
    String lat, lng;
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
    private Button left, right;
    PlaceAutocompleteFragment places;
    private String StoreID;
    AlertDialog progressDialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable final Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.home, container, false);

        progressDialog = new SpotsDialog(getContext(), R.style.Custom);

        progressDialog.show();
        progressDialog.setCancelable(false);
        sp = getActivity().getSharedPreferences("MyPre", Context.MODE_PRIVATE);
        SharedPreferences pref = getActivity().getSharedPreferences("MyPre", Context.MODE_PRIVATE);
        location = pref.getBoolean("location", false);

        if (!location.equals(true))
        {
            MyCustomAlertDialog();
        }
        //Non Clover Products
        myrecyclerview2 = view.findViewById(R.id.contact_recyclerview2);
        myrecyclerview2.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.HORIZONTAL, false));
        products = new ArrayList<>();
        if (location.equals(true))
            FetchNonCloverProducts();

        //Clover Products
        myrecyclerview3 = view.findViewById(R.id.contact_recyclerview3);
        myrecyclerview3.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.HORIZONTAL, false));
        CloverProducts = new ArrayList<>();
        if (location.equals(true))
            FetchCloverProducts();

        //Latest Products
        myrecyclerview4 = view.findViewById(R.id.contact_recyclerview4);
        myrecyclerview4.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.HORIZONTAL, false));
        LatestProducts = new ArrayList<>();
        if (location.equals(true))
            FetchLatestProducts();

        //spinner.setVisibility(View.GONE);
        stores = new ArrayList<>();
        storesid = new ArrayList<>();

        /* map is already there, just return view as it is */

        tv1 = view.findViewById(R.id.tv1);
        tv2 = view.findViewById(R.id.tv2);
        tv3 = view.findViewById(R.id.tv3);


        SpannableString content = new SpannableString(getResources().getString(R.string.prod));
        content.setSpan(new UnderlineSpan(), 0, content.length() - 9, 0);
        tv1.setText(content);
        SpannableString content2 = new SpannableString(getResources().getString(R.string.prod1));
        content2.setSpan(new UnderlineSpan(), 0, content2.length() - 9, 0);
        tv2.setText(content2);
//        SpannableString content3 = new SpannableString(getResources().getString(R.string.prod2));
//        content3.setSpan(new UnderlineSpan(), 0, content3.length() - 9, 0);
//        tv3.setText(content3);

        //Expandable ListView
        expListView = view.findViewById(R.id.listmainexpand);
        new DownloadJason().execute();
       /* expListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                if (groupPosition == 9) {
                    return true;
                } else {
                    return false;
                }
            }
        });*/
        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                Fragment fragment;
                FragmentManager fragmentManager;
                Bundle args;
                FragmentTransaction fragmentTransaction;
                if (groupPosition == 0) {
                    switch (childPosition) {
                        case 0:
                            child_cat_id = "2";
                            fragment = new Fragmentproductdetail();
                            fragmentManager = getActivity().getSupportFragmentManager();
                            args = new Bundle();
                            args.putString("id", child_cat_id);
                            fragment.setArguments(args);
                            fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.content_egrocery, fragment);
                            fragmentTransaction.addToBackStack(null);
                            fragmentTransaction.commit();
                            break;
                        case 1:
                            child_cat_id = "3";
                            fragment = new Fragmentproductdetail();
                            fragmentManager = getActivity().getSupportFragmentManager();
                            args = new Bundle();
                            args.putString("id", child_cat_id);
                            fragment.setArguments(args);
                            fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.content_egrocery, fragment);
                            fragmentTransaction.addToBackStack(null);
                            fragmentTransaction.commit();
                            break;
                        case 2:
                            child_cat_id = "4";
                            fragment = new Fragmentproductdetail();
                            fragmentManager = getActivity().getSupportFragmentManager();
                            args = new Bundle();
                            args.putString("id", child_cat_id);
                            fragment.setArguments(args);
                            fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.content_egrocery, fragment);
                            fragmentTransaction.addToBackStack(null);
                            fragmentTransaction.commit();
                            break;
                        case 3:
                            child_cat_id = "5";
                            fragment = new Fragmentproductdetail();
                            fragmentManager = getActivity().getSupportFragmentManager();
                            args = new Bundle();
                            args.putString("id", child_cat_id);
                            fragment.setArguments(args);
                            fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.content_egrocery, fragment);
                            fragmentTransaction.addToBackStack(null);
                            fragmentTransaction.commit();
                            break;
                        case 4:
                            child_cat_id = "6";
                            fragment = new Fragmentproductdetail();
                            fragmentManager = getActivity().getSupportFragmentManager();
                            args = new Bundle();
                            args.putString("id", child_cat_id);
                            fragment.setArguments(args);
                            fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.content_egrocery, fragment);
                            fragmentTransaction.addToBackStack(null);
                            fragmentTransaction.commit();
                            break;
                        case 5:
                            child_cat_id = "50";
                            fragment = new Fragmentproductdetail();
                            fragmentManager = getActivity().getSupportFragmentManager();
                            args = new Bundle();
                            args.putString("id", child_cat_id);
                            fragment.setArguments(args);
                            fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.content_egrocery, fragment);
                            fragmentTransaction.addToBackStack(null);
                            fragmentTransaction.commit();
                            break;
                        case 6:
                            child_cat_id = "63";
                            fragment = new Fragmentproductdetail();
                            fragmentManager = getActivity().getSupportFragmentManager();
                            args = new Bundle();
                            args.putString("id", child_cat_id);
                            fragment.setArguments(args);
                            fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.content_egrocery, fragment);
                            fragmentTransaction.addToBackStack(null);
                            fragmentTransaction.commit();
                            break;
                    }
                } else if (groupPosition == 1) {
                    switch (childPosition) {
                        case 0:
                            child_cat_id = "8";
                            fragment = new Fragmentproductdetail();
                            fragmentManager = getActivity().getSupportFragmentManager();
                            args = new Bundle();
                            args.putString("id", child_cat_id);
                            fragment.setArguments(args);
                            fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.content_egrocery, fragment);
                            fragmentTransaction.addToBackStack(null);
                            fragmentTransaction.commit();
                            break;
                        case 1:
                            child_cat_id = "9";
                            fragment = new Fragmentproductdetail();
                            fragmentManager = getActivity().getSupportFragmentManager();
                            args = new Bundle();
                            args.putString("id", child_cat_id);
                            fragment.setArguments(args);
                            fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.content_egrocery, fragment);
                            fragmentTransaction.addToBackStack(null);
                            fragmentTransaction.commit();
                            break;
                        case 2:
                            child_cat_id = "80";
                            fragment = new Fragmentproductdetail();
                            fragmentManager = getActivity().getSupportFragmentManager();
                            args = new Bundle();
                            args.putString("id", child_cat_id);
                            fragment.setArguments(args);
                            fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.content_egrocery, fragment);
                            fragmentTransaction.addToBackStack(null);
                            fragmentTransaction.commit();
                            break;
                    }
                } else if (groupPosition == 2) {
                    switch (childPosition) {
                        case 0:
                            child_cat_id = "11";
                            fragment = new Fragmentproductdetail();
                            fragmentManager = getActivity().getSupportFragmentManager();
                            args = new Bundle();
                            args.putString("id", child_cat_id);
                            fragment.setArguments(args);
                            fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.content_egrocery, fragment);
                            fragmentTransaction.addToBackStack(null);
                            fragmentTransaction.commit();
                            break;
                        case 1:
                            child_cat_id = "12";
                            fragment = new Fragmentproductdetail();
                            fragmentManager = getActivity().getSupportFragmentManager();
                            args = new Bundle();
                            args.putString("id", child_cat_id);
                            fragment.setArguments(args);
                            fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.content_egrocery, fragment);
                            fragmentTransaction.addToBackStack(null);
                            fragmentTransaction.commit();
                            break;
                        case 2:
                            child_cat_id = "69";
                            fragment = new Fragmentproductdetail();
                            fragmentManager = getActivity().getSupportFragmentManager();
                            args = new Bundle();
                            args.putString("id", child_cat_id);
                            fragment.setArguments(args);
                            fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.content_egrocery, fragment);
                            fragmentTransaction.addToBackStack(null);
                            fragmentTransaction.commit();
                            break;
                    }
                } else if (groupPosition == 3) {
                    switch (childPosition) {
                        case 0:
                            child_cat_id = "14";
                            fragment = new Fragmentproductdetail();
                            fragmentManager = getActivity().getSupportFragmentManager();
                            args = new Bundle();
                            args.putString("id", child_cat_id);
                            fragment.setArguments(args);
                            fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.content_egrocery, fragment);
                            fragmentTransaction.addToBackStack(null);
                            fragmentTransaction.commit();
                            break;
                        case 1:
                            child_cat_id = "15";
                            fragment = new Fragmentproductdetail();
                            fragmentManager = getActivity().getSupportFragmentManager();
                            args = new Bundle();
                            args.putString("id", child_cat_id);
                            fragment.setArguments(args);
                            fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.content_egrocery, fragment);
                            fragmentTransaction.addToBackStack(null);
                            fragmentTransaction.commit();
                            break;
                    }
                } else if (groupPosition == 4) {
                    switch (childPosition) {
                        case 0:
                            child_cat_id = "17";
                            fragment = new Fragmentproductdetail();
                            fragmentManager = getActivity().getSupportFragmentManager();
                            args = new Bundle();
                            args.putString("id", child_cat_id);
                            fragment.setArguments(args);
                            fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.content_egrocery, fragment);
                            fragmentTransaction.addToBackStack(null);
                            fragmentTransaction.commit();
                            break;
                        case 1:
                            child_cat_id = "18";
                            fragment = new Fragmentproductdetail();
                            fragmentManager = getActivity().getSupportFragmentManager();
                            args = new Bundle();
                            args.putString("id", child_cat_id);
                            fragment.setArguments(args);
                            fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.content_egrocery, fragment);
                            fragmentTransaction.addToBackStack(null);
                            fragmentTransaction.commit();
                            break;
                    }
                } else if (groupPosition == 5) {
                    switch (childPosition) {
                        case 0:
                            child_cat_id = "20";
                            fragment = new Fragmentproductdetail();
                            fragmentManager = getActivity().getSupportFragmentManager();
                            args = new Bundle();
                            args.putString("id", child_cat_id);
                            fragment.setArguments(args);
                            fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.content_egrocery, fragment);
                            fragmentTransaction.addToBackStack(null);
                            fragmentTransaction.commit();
                            break;
                    }
                } else if (groupPosition == 6) {
                    switch (childPosition) {
                        case 0:
                            child_cat_id = "22";
                            fragment = new Fragmentproductdetail();
                            fragmentManager = getActivity().getSupportFragmentManager();
                            args = new Bundle();
                            args.putString("id", child_cat_id);
                            fragment.setArguments(args);
                            fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.content_egrocery, fragment);
                            fragmentTransaction.addToBackStack(null);
                            fragmentTransaction.commit();
                            break;
                        case 1:
                            child_cat_id = "23";
                            fragment = new Fragmentproductdetail();
                            fragmentManager = getActivity().getSupportFragmentManager();
                            args = new Bundle();
                            args.putString("id", child_cat_id);
                            fragment.setArguments(args);
                            fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.content_egrocery, fragment);
                            fragmentTransaction.addToBackStack(null);
                            fragmentTransaction.commit();
                            break;
                        case 2:
                            child_cat_id = "24";
                            fragment = new Fragmentproductdetail();
                            fragmentManager = getActivity().getSupportFragmentManager();
                            args = new Bundle();
                            args.putString("id", child_cat_id);
                            fragment.setArguments(args);
                            fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.content_egrocery, fragment);
                            fragmentTransaction.addToBackStack(null);
                            fragmentTransaction.commit();
                            break;
                        case 3:
                            child_cat_id = "25";
                            fragment = new Fragmentproductdetail();
                            fragmentManager = getActivity().getSupportFragmentManager();
                            args = new Bundle();
                            args.putString("id", child_cat_id);
                            fragment.setArguments(args);
                            fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.content_egrocery, fragment);
                            fragmentTransaction.addToBackStack(null);
                            fragmentTransaction.commit();
                            break;
                        case 4:
                            child_cat_id = "26";
                            fragment = new Fragmentproductdetail();
                            fragmentManager = getActivity().getSupportFragmentManager();
                            args = new Bundle();
                            args.putString("id", child_cat_id);
                            fragment.setArguments(args);
                            fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.content_egrocery, fragment);
                            fragmentTransaction.addToBackStack(null);
                            fragmentTransaction.commit();
                            break;
                        case 5:
                            child_cat_id = "56";
                            fragment = new Fragmentproductdetail();
                            fragmentManager = getActivity().getSupportFragmentManager();
                            args = new Bundle();
                            args.putString("id", child_cat_id);
                            fragment.setArguments(args);
                            fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.content_egrocery, fragment);
                            fragmentTransaction.addToBackStack(null);
                            fragmentTransaction.commit();
                            break;
                        case 6:
                            child_cat_id = "71";
                            fragment = new Fragmentproductdetail();
                            fragmentManager = getActivity().getSupportFragmentManager();
                            args = new Bundle();
                            args.putString("id", child_cat_id);
                            fragment.setArguments(args);
                            fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.content_egrocery, fragment);
                            fragmentTransaction.addToBackStack(null);
                            fragmentTransaction.commit();
                            break;
                        case 7:
                            child_cat_id = "74";
                            fragment = new Fragmentproductdetail();
                            fragmentManager = getActivity().getSupportFragmentManager();
                            args = new Bundle();
                            args.putString("id", child_cat_id);
                            fragment.setArguments(args);
                            fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.content_egrocery, fragment);
                            fragmentTransaction.addToBackStack(null);
                            fragmentTransaction.commit();
                            break;
                        case 8:
                            child_cat_id = "76";
                            fragment = new Fragmentproductdetail();
                            fragmentManager = getActivity().getSupportFragmentManager();
                            args = new Bundle();
                            args.putString("id", child_cat_id);
                            fragment.setArguments(args);
                            fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.content_egrocery, fragment);
                            fragmentTransaction.addToBackStack(null);
                            fragmentTransaction.commit();
                            break;
                        case 9:
                            child_cat_id = "82";
                            fragment = new Fragmentproductdetail();
                            fragmentManager = getActivity().getSupportFragmentManager();
                            args = new Bundle();
                            args.putString("id", child_cat_id);
                            fragment.setArguments(args);
                            fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.content_egrocery, fragment);
                            fragmentTransaction.addToBackStack(null);
                            fragmentTransaction.commit();
                            break;
                        case 10:
                            child_cat_id = "86";
                            fragment = new Fragmentproductdetail();
                            fragmentManager = getActivity().getSupportFragmentManager();
                            args = new Bundle();
                            args.putString("id", child_cat_id);
                            fragment.setArguments(args);
                            fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.content_egrocery, fragment);
                            fragmentTransaction.addToBackStack(null);
                            fragmentTransaction.commit();
                            break;
                        case 11:
                            child_cat_id = "89";
                            fragment = new Fragmentproductdetail();
                            fragmentManager = getActivity().getSupportFragmentManager();
                            args = new Bundle();
                            args.putString("id", child_cat_id);
                            fragment.setArguments(args);
                            fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.content_egrocery, fragment);
                            fragmentTransaction.addToBackStack(null);
                            fragmentTransaction.commit();
                            break;
                    }
                } else if (groupPosition == 7) {
                    switch (childPosition) {
                        case 0:
                            child_cat_id = "28";
                            fragment = new Fragmentproductdetail();
                            fragmentManager = getActivity().getSupportFragmentManager();
                            args = new Bundle();
                            args.putString("id", child_cat_id);
                            fragment.setArguments(args);
                            fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.content_egrocery, fragment);
                            fragmentTransaction.addToBackStack(null);
                            fragmentTransaction.commit();
                            break;
                        case 1:
                            child_cat_id = "29";
                            fragment = new Fragmentproductdetail();
                            fragmentManager = getActivity().getSupportFragmentManager();
                            args = new Bundle();
                            args.putString("id", child_cat_id);
                            fragment.setArguments(args);
                            fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.content_egrocery, fragment);
                            fragmentTransaction.addToBackStack(null);
                            fragmentTransaction.commit();
                            break;
                        case 2:
                            child_cat_id = "30";
                            fragment = new Fragmentproductdetail();
                            fragmentManager = getActivity().getSupportFragmentManager();
                            args = new Bundle();
                            args.putString("id", child_cat_id);
                            fragment.setArguments(args);
                            fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.content_egrocery, fragment);
                            fragmentTransaction.addToBackStack(null);
                            fragmentTransaction.commit();
                            break;
                    }
                } else if (groupPosition == 8) {
                    switch (childPosition) {
                        case 0:
                            child_cat_id = "32";
                            fragment = new Fragmentproductdetail();
                            fragmentManager = getActivity().getSupportFragmentManager();
                            args = new Bundle();
                            args.putString("id", child_cat_id);
                            fragment.setArguments(args);
                            fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.content_egrocery, fragment);
                            fragmentTransaction.addToBackStack(null);
                            fragmentTransaction.commit();
                            break;
                        case 1:
                            child_cat_id = "33";
                            fragment = new Fragmentproductdetail();
                            fragmentManager = getActivity().getSupportFragmentManager();
                            args = new Bundle();
                            args.putString("id", child_cat_id);
                            fragment.setArguments(args);
                            fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.content_egrocery, fragment);
                            fragmentTransaction.addToBackStack(null);
                            fragmentTransaction.commit();
                            break;
                        case 2:
                            child_cat_id = "34";
                            fragment = new Fragmentproductdetail();
                            fragmentManager = getActivity().getSupportFragmentManager();
                            args = new Bundle();
                            args.putString("id", child_cat_id);
                            fragment.setArguments(args);
                            fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.content_egrocery, fragment);
                            fragmentTransaction.addToBackStack(null);
                            fragmentTransaction.commit();
                            break;
                    }
                } else if (groupPosition == 9) {
                    switch (childPosition) {
                        case 0:
                            child_cat_id = "102";
                            fragment = new Fragmentproductdetail();
                            fragmentManager = getActivity().getSupportFragmentManager();
                            args = new Bundle();
                            args.putString("id", child_cat_id);
                            fragment.setArguments(args);
                            fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.content_egrocery, fragment);
                            fragmentTransaction.addToBackStack(null);
                            fragmentTransaction.commit();
                            break;
                        case 1:
                            child_cat_id = "104";
                            fragment = new Fragmentproductdetail();
                            fragmentManager = getActivity().getSupportFragmentManager();
                            args = new Bundle();
                            args.putString("id", child_cat_id);
                            fragment.setArguments(args);
                            fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.content_egrocery, fragment);
                            fragmentTransaction.addToBackStack(null);
                            fragmentTransaction.commit();
                            break;
                        case 2:
                            child_cat_id = "105";
                            fragment = new Fragmentproductdetail();
                            fragmentManager = getActivity().getSupportFragmentManager();
                            args = new Bundle();
                            args.putString("id", child_cat_id);
                            fragment.setArguments(args);
                            fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.content_egrocery, fragment);
                            fragmentTransaction.addToBackStack(null);
                            fragmentTransaction.commit();
                            break;
                        case 3:
                            child_cat_id = "107";
                            fragment = new Fragmentproductdetail();
                            fragmentManager = getActivity().getSupportFragmentManager();
                            args = new Bundle();
                            args.putString("id", child_cat_id);
                            fragment.setArguments(args);
                            fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.content_egrocery, fragment);
                            fragmentTransaction.addToBackStack(null);
                            fragmentTransaction.commit();
                            break;
                        case 4:
                            child_cat_id = "109";
                            fragment = new Fragmentproductdetail();
                            fragmentManager = getActivity().getSupportFragmentManager();
                            args = new Bundle();
                            args.putString("id", child_cat_id);
                            fragment.setArguments(args);
                            fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.content_egrocery, fragment);
                            fragmentTransaction.addToBackStack(null);
                            fragmentTransaction.commit();
                            break;
                    }

                } else if (groupPosition == 10) {
                    switch (childPosition) {
                        case 0:
                            child_cat_id = "95";
                            fragment = new Fragmentproductdetail();
                            fragmentManager = getActivity().getSupportFragmentManager();
                            args = new Bundle();
                            args.putString("id", child_cat_id);
                            fragment.setArguments(args);
                            fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.content_egrocery, fragment);
                            fragmentTransaction.addToBackStack(null);
                            fragmentTransaction.commit();
                            break;
                    }

                }
                return false;

            }
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewflip = view.findViewById(R.id.viewflip);
        int images[] = {R.drawable.cloverpic1, R.drawable.cloverpic2, R.drawable.cloverpic3,
                R.drawable.cloverpic4, R.drawable.cloverpic5, R.drawable.cloverpic6,
                R.drawable.cloverpic7, R.drawable.cloverpic8, R.drawable.cloverpic9,
                R.drawable.cloverpic10};
        for (int image : images) {
            flipimage(image);
        }
    }

    public void flipimage(int image) {
        ImageView imageView = new ImageView(getContext());
        imageView.setBackgroundResource(image);
        viewflip.addView(imageView);
        viewflip.setFlipInterval(4000);
        viewflip.setAutoStart(true);
        viewflip.setInAnimation(getContext(), android.R.anim.slide_in_left);
        viewflip.setOutAnimation(getContext(), android.R.anim.slide_out_right);
    }

    public void FetchNonCloverProducts() {
        final String URL;
        SharedPreferences pref = getActivity().getSharedPreferences("MyPre", Context.MODE_PRIVATE);
        StoreID = pref.getString("StoreID", "");

        if (StoreID.length() == 0) {
            URL = "http://172.16.10.203/api/getAllProducts/" + packid;
        } else {
            URL = "http://172.16.10.203/api/getAllProducts/" + StoreID;
        }

        StringRequest req = new StringRequest(Request.Method.GET, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //progressDialog.dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("non_clover_products");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject object = jsonArray.getJSONObject(i);
                                String id = object.getString("product_id");
                                String name = object.getString("product_name");
                                String price = object.getString("normalcustomer_price");
                                JSONObject innerObj = object.getJSONObject("findimage");
                                String image = "http://172.16.10.203/images/" + innerObj.getString("image");

                                Model_NonCloverProducts items = new Model_NonCloverProducts(
                                        id,
                                        name,
                                        price,
                                        image
                                );
                                products.add(items);
                            }

                            adapter = new Adapter_NonCloverProducts(products, getContext());
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
                        Toast.makeText(getContext(), "Non Clover Error: " + error.getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(req);
    }

    public void FetchCloverProducts() {
        final String URL;
        SharedPreferences pref = getActivity().getSharedPreferences("MyPre", Context.MODE_PRIVATE);
        StoreID = pref.getString("StoreID", "");

        if (StoreID.length() == 0) {
            URL = "http://172.16.10.203/api/getAllProducts/" + packid;
        } else {
            URL = "http://172.16.10.203/api/getAllProducts/" + StoreID;
        }

        StringRequest req = new StringRequest(Request.Method.GET, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //progressDialog.dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("clover_products");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject object = jsonArray.getJSONObject(i);
                                String id = object.getString("product_id");
                                String name = object.getString("product_name");
                                String price = object.getString("normalcustomer_price");
                                JSONObject innerObj = object.getJSONObject("findimage");
                                String image = "http://172.16.10.203/images/" + innerObj.getString("image");
                                Model_CloverProducts items = new Model_CloverProducts(
                                        id,
                                        name,
                                        price,
                                        image
                                );
                                CloverProducts.add(items);
                            }
                            CloverAdapter = new Adapter_CloverProducts(CloverProducts, getContext());
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
                        Toast.makeText(getContext(), "Clover Error: " + error.getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(req);
    }

    public void FetchLatestProducts() {
        final String URL;
        SharedPreferences pref = getActivity().getSharedPreferences("MyPre", Context.MODE_PRIVATE);
        StoreID = pref.getString("StoreID", "");

        if (StoreID.length() == 0) {
            URL = "http://172.16.10.203/api/getAllProducts/" + packid;
        } else {
            URL = "http://172.16.10.203/api/getAllProducts/" + StoreID;
        }
        StringRequest req = new StringRequest(Request.Method.GET, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Runnable progressRunnable = new Runnable() {

                            @Override
                            public void run() {
                                progressDialog.dismiss();
                            }
                        };

                        Handler pdCanceller = new Handler();
                        pdCanceller.postDelayed(progressRunnable, 4000);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("latest_products");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject object = jsonArray.getJSONObject(i);
                                String id = object.getString("product_id");
                                String name = object.getString("product_name");
                                String price = object.getString("normalcustomer_price");
                                JSONObject innerObj = object.getJSONObject("findimage");
                                String image = "http://172.16.10.203/images/" + innerObj.getString("image");

                                Model_LatestProducts items = new Model_LatestProducts(
                                        id,
                                        name,
                                        price,
                                        image
                                );
                                LatestProducts.add(items);
                            }

                            LatestAdapter = new Adapter_LatestProducts(LatestProducts, getContext());
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
                        Toast.makeText(getContext(), "Latest Error: " + error.getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(req);
    }

    private class DownloadJason extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {

            JSONParser jp = new JSONParser();
            String jsonstr = jp.makeServiceCall(URL_getAllCategories);
            Log.d("Response = ", jsonstr);

            if (jsonstr != null) {
                listDataHeader = new ArrayList<String>();
                listDataChild = new HashMap<String, List<String>>();

                try {
                    JSONArray jsonArray = new JSONArray(jsonstr);
                    for (int j = 0; j < jsonArray.length(); j++) {
                        JSONArray array = jsonArray.getJSONArray(j);
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject jsonObject = array.getJSONObject(i);
                            String cat_title = jsonObject.getString("cat_title");
                            listDataHeader.add(cat_title);
                            List<String> lease_offer = new ArrayList<String>();
                            JSONObject object = jsonObject.getJSONObject("0");
                            JSONArray innerArray = object.getJSONArray("sub_cat");
                            for (int a = 0; a < innerArray.length(); a++) {
                                JSONObject innerObj = innerArray.getJSONObject(a);
                                String sub_cat_title = innerObj.getString("cat_title");
                                lease_offer.add(sub_cat_title);
                                listDataChild.put(listDataHeader.get(j), lease_offer);
                            }
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(getContext(),
                        "Please Check internet Connection", Toast.LENGTH_SHORT)
                        .show();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            //mprocessingdialog.dismiss();

            //call constructor
            explistadapter = new ExpListAdapter(getContext(), listDataHeader, listDataChild);

            // setting list adapter
            expListView.setAdapter(explistadapter);
        }
    }

    class GeocoderHandler extends Handler {
        @Override
        public void handleMessage(Message message) {
            switch (message.what) {
                case 1:
                    Bundle bundle = message.getData();
                    lat = bundle.getString("lat");
                    lng = bundle.getString("lng");
                    break;
                default:
                    lat = null;
                    lng = null;
            }
            //Toast.makeText(getContext(),lat + " " + lng,Toast.LENGTH_LONG).show();
            final String URL_order = "http://172.16.10.203/api/getStores/" + lat + "," + lng;
            StringRequest req = new StringRequest(Request.Method.GET, URL_order,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                /*String selstore = "Select Store";
                                stores.add(selstore);*/
                                stores.add("Select Store");
                                JSONArray jsonArray = new JSONArray(response);
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject object = jsonArray.getJSONObject(i);
                                    String id = object.getString("id");
                                    String address = object.getString("name");
                                    String distance = object.getString("distance_in_km");
                                    String dt = new DecimalFormat(".00").format(Double.parseDouble(distance));
                                    String Total = address + "\n" + dt + " KM";
                                    stores.add(Total);
                                    storesid.add(id);
                                }

                                spinner.setAdapter(new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, stores));
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
            submitstore.setEnabled(true);
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                    val = spinner.getItemAtPosition(spinner.getSelectedItemPosition()).toString();
                    packid = storesid.get(position);
                    edit = sp.edit();
                    edit.putString("StoreID", packid);
                    edit.commit();
                    edit.apply();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            RequestQueue requestQueue = Volley.newRequestQueue(getContext());
            requestQueue.add(req);
        }
    }

    public void MyCustomAlertDialog() {
        MyDialog = new Dialog(getContext());
        MyDialog.setContentView(R.layout.customdialogbox);
        MyDialog.show();
        MyDialog.setCancelable(false);
        spinner = MyDialog.findViewById(R.id.orderspinner);
        submitstore = MyDialog.findViewById(R.id.buttonaddress);
        submitstore.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (spinner.getSelectedItemPosition() == 0)
                {
                    Toast.makeText(getContext(), "Please Select Store!", Toast.LENGTH_LONG).show();
                }
                else
                    {
                        FetchNonCloverProducts();
                        FetchLatestProducts();
                        FetchCloverProducts();
                        MyDialog.dismiss();
                    } }
        });
        places = (PlaceAutocompleteFragment) getActivity().getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);
        places.setOnPlaceSelectedListener(new PlaceSelectionListener()
        {
            @Override
            public void onPlaceSelected(Place place)
            {
                String placeName = (String) place.getAddress();
                //Toast.makeText(getContext(),placeName,Toast.LENGTH_LONG).show();
                GeocodingLocation locationAddress = new GeocodingLocation();
                locationAddress.getAddressFromLocation(placeName,
                        getContext(), new Home.GeocoderHandler());
                spinner.setVisibility(View.VISIBLE);
                edit = sp.edit();
                edit.putBoolean("location", true);
                edit.commit();
                edit.apply();
                //location = true;
                stores.clear();
            }
            @Override
            public void onError(Status status)
            {
                Toast.makeText(getContext(), status.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        AutocompleteFilter typeFilter = new AutocompleteFilter.Builder()
                .setCountry("PK")
                .build();
        places.setFilter(typeFilter);
        places.getView().setBackgroundColor(Color.WHITE);
    }
}
