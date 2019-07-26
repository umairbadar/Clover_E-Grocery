package com.example.lubna.cloverweb;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

public class Adapter_Membership extends RecyclerView.Adapter<Adapter_Membership.ViewHolder> {

    List<Model_Membership> m_accounts;
    Context context;

    public Adapter_Membership(List<Model_Membership> m_accounts, Context context) {
        this.m_accounts = m_accounts;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_membership,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        final Model_Membership items = m_accounts.get(i);

        viewHolder.tv_member_name.setText(items.getAccount_title());
        viewHolder.tv_member_issue_date.setText(items.getIssue_date());
        viewHolder.tv_member_expiry_date.setText(items.getExpiry_date());
    }

    @Override
    public int getItemCount() {
        return m_accounts.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView tv_member_name,tv_member_type,tv_member_issue_date,tv_member_expiry_date;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_member_name = itemView.findViewById(R.id.tv_member_name);
            tv_member_type = itemView.findViewById(R.id.tv_member_type);
            tv_member_issue_date = itemView.findViewById(R.id.tv_member_issue_date);
            tv_member_expiry_date = itemView.findViewById(R.id.tv_member_expiry_date);
        }
    }
}
