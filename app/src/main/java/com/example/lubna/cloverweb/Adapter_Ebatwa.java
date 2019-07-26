package com.example.lubna.cloverweb;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.RowId;
import java.util.List;

public class Adapter_Ebatwa extends RecyclerView.Adapter<Adapter_Ebatwa.ViewHolder> {

    List<Model_Ebatwa> m_accounts;
    Context context;

    public Adapter_Ebatwa(List<Model_Ebatwa> m_accounts, Context context) {
        this.m_accounts = m_accounts;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_ebatwa,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        final Model_Ebatwa items = m_accounts.get(i);

        viewHolder.tv_member_name.setText(items.getAccount_title());
        viewHolder.tv_member_issue_date.setText(items.getIssue_date());
        viewHolder.tv_member_expiry_date.setText(items.getExpiry_date());

        viewHolder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String a_name = items.getAccount_title();
                String a_no = items.getAccount_number();
                String a_balance = items.getAccount_balance();
                String total = FragmentEbatwa.total;
                String account_id = items.getAccount_id();
                String points = FragmentEbatwa.points;
                String sub_total = FragmentEbatwa.sub_total;
                String gst = FragmentEbatwa.gst;

                AppCompatActivity activity = (AppCompatActivity) v.getContext();
                Fragment fragment = new Fragment_Member_Account();
                Bundle args = new Bundle();
                args.putString("a_name",a_name);
                args.putString("a_no",a_no);
                args.putString("a_balance",a_balance);
                args.putString("total",total);
                args.putString("account_id",account_id);
                args.putString("points",points);
                args.putString("SubTotal",sub_total);
                args.putString("GST",gst);
                fragment.setArguments(args);
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.content_egrocery, fragment)
                        .addToBackStack(null).commit();

                //Toast.makeText(context, a_name + " " + a_no + " " + a_balance + " " + total,Toast.LENGTH_LONG).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return m_accounts.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        TextView tv_member_name,tv_member_type,tv_member_issue_date,tv_member_expiry_date;
        RelativeLayout mainLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_member_name = itemView.findViewById(R.id.tv_member_name);
            tv_member_type = itemView.findViewById(R.id.tv_member_type);
            tv_member_issue_date = itemView.findViewById(R.id.tv_member_issue_date);
            tv_member_expiry_date = itemView.findViewById(R.id.tv_member_expiry_date);
            mainLayout = itemView.findViewById(R.id.mainLayout);
        }
    }

}
