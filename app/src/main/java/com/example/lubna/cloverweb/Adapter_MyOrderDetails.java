package com.example.lubna.cloverweb;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;



import java.util.List;

public class Adapter_MyOrderDetails extends RecyclerView.Adapter<Adapter_MyOrderDetails.ViewHolder> {
    List<Model_OrderDetails> m_orders_details;
    Context context;
    public Adapter_MyOrderDetails(List<Model_OrderDetails> m_orders_details, Context context)
    {
        this.m_orders_details = m_orders_details;
        this.context = context;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_orderdetails,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        final Model_OrderDetails items = m_orders_details.get(i);
        viewHolder.product_name.setText(items.getProduct_name());
        viewHolder.quantity.setText(items.getQuantity());
        viewHolder.unitprice.setText(items.getUnitprice());
    }

    @Override
    public int getItemCount() {
        return m_orders_details.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView product_name,quantity,unitprice;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            product_name = itemView.findViewById(R.id.tv_product_name);
            quantity = itemView.findViewById(R.id.tv_product_quantity);
            unitprice = itemView.findViewById(R.id.tv_unitprice);
        }
    }
}
