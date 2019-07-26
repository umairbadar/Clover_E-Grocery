package com.example.lubna.cloverweb;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.DuplicateFormatFlagsException;

public class Fragment_Member_Account extends Fragment {

    View view;
    LinearLayout linearLayout_member_account;
    TextView ebatwa_total,ebatwa_balance,ebatwa_deduction,ebatwa_error;

    String total,a_name,a_no,a_balance,a_id,points,sub_total,gst;

    Double t,b;

    Button btn_confirm_order;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        view = inflater.inflate(R.layout.fragment_member_account,container,false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btn_confirm_order = view.findViewById(R.id.btn_confirm_order);

        if (getArguments() != null)
        {
            total = getArguments().getString("total");
            a_balance = getArguments().getString("a_balance");
            a_id = getArguments().getString("account_id");
            points = getArguments().getString("points");
            sub_total = getArguments().getString("SubTotal");
            gst = getArguments().getString("GST");
        }

        //Toast.makeText(getContext(), total + " " + a_balance, Toast.LENGTH_LONG).show();

        t = Double.parseDouble(total);
        b = Double.parseDouble(a_balance);

        ebatwa_total = view.findViewById(R.id.tv_ebatwa_total);
        ebatwa_balance = view.findViewById(R.id.tv_ebatwa_remaining_balance);
        ebatwa_deduction = view.findViewById(R.id.tv_ebatwa_deduction_amount);
        ebatwa_error = view.findViewById(R.id.tv_error);
        linearLayout_member_account = view.findViewById(R.id.layout_member_account);

        if (t > b)
        {
            linearLayout_member_account.setVisibility(View.GONE);
            ebatwa_error.setVisibility(View.VISIBLE);
        }
        else
        {
            ebatwa_total.setText("You have Rs." + a_balance + " in your e-batwa.");
            Double c = b - t;
            ebatwa_deduction.setText("After deduction of Rs." + total);
            ebatwa_balance.setText("Your remaining balance is Rs." + String.valueOf(c));

        }

        btn_confirm_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Fragment fragment = new FragmentCash();
                Bundle args = new Bundle();
                args.putString("TotalAmount",total);
                args.putString("head", "Checkout | Via E-Batwa");
                args.putString("account_id", a_id);
                args.putString("method", "e-batwa");
                args.putString("points", points);
                args.putString("SubTotal", sub_total);
                args.putString("GST", gst);
                fragment.setArguments(args);
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.content_egrocery,fragment);
                ft.addToBackStack(null);
                ft.commit();
            }
        });
    }
}
