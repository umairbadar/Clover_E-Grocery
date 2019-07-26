package com.example.lubna.cloverweb;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class Adapter_Myorder extends RecyclerView.Adapter<Adapter_Myorder.ViewHolder> {
    List<Model_Order> m_orders;
    Context context;
    public Adapter_Myorder(List<Model_Order> m_orders, Context context) {
        this.m_orders = m_orders;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View v =LayoutInflater.from(parent.getContext()).inflate(R.layout.item_myorders,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {
        final Model_Order items = m_orders.get(i);
        viewHolder.tv_delivery_date.setText(items.getDate());
        viewHolder.tv_total.setText(items.getTotal());
        if (FragmentMyOrder.status.equals("Status: pending")) {
            viewHolder.tv_status.setTextColor(Color.RED);
            viewHolder.tv_status.setText(items.getStatus());
        }
        else if(FragmentMyOrder.status.equals("Status: complete"))
        {
            viewHolder.tv_status.setTextColor(Color.GREEN);
            viewHolder.tv_status.setText(items.getStatus());
        }
        viewHolder.tv_order_id.setText(items.getOrder_id());
        viewHolder.tv_shipingadd.setText(items.getAddress());
        viewHolder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppCompatActivity activity = (AppCompatActivity) v.getContext();
                Fragment fragment = new FragmentMyOrderDetails();
                Bundle args = new Bundle();
                args.putString("OrderID",viewHolder.tv_order_id.getText().toString());
                fragment.setArguments(args);
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.content_egrocery, fragment)
                        .addToBackStack(null).commit();

            }
        });
    }

    @Override
    public int getItemCount() {
        return m_orders.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_delivery_date,tv_total,tv_status,tv_shipingadd,tv_order_id;
        LinearLayout mainLayout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_delivery_date = itemView.findViewById(R.id.tv_delivery_date);
            tv_total = itemView.findViewById(R.id.tv_total);
            tv_status = itemView.findViewById(R.id.tv_status);
            tv_shipingadd = itemView.findViewById(R.id.tv_shipping_address);
            tv_order_id = itemView.findViewById(R.id.tv_order_id);
            mainLayout = itemView.findViewById(R.id.mainLayout);
        }
    }
}