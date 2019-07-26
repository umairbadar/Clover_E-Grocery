package com.example.lubna.cloverweb;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lubna.cloverweb.Database.ModelDB.Cart;
import com.example.lubna.cloverweb.Utils.Common;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.List;

public class Adapter_Cart extends RecyclerView.Adapter<Adapter_Cart.CartViewHolder> {

    Context context;
    List<Cart> cartList;
    Cart cart;
    int sub_total;
    com.example.lubna.cloverweb.Cart c = new com.example.lubna.cloverweb.Cart();
    private static DecimalFormat df2 = new DecimalFormat(".##");


    public Adapter_Cart(Context context, List<Cart> cartList) {
        this.context = context;
        this.cartList = cartList;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {

        View itemView = LayoutInflater.from(context).inflate(R.layout.item_cart, parent, false);
        return new CartViewHolder(itemView);
    }

    @Override
    public int getItemCount() {
        return cartList.size();
    }

    @Override
    public void onBindViewHolder(@NonNull final CartViewHolder holder, final int position) {

        String quantity = String.valueOf(Common.cartRepository.getQ(cartList.get(position).pid));
        holder.et_quantity.setText(quantity);

        Picasso.with(context)
                .load(cartList.get(position).image)
                .into(holder.img_product);

        holder.txt_product_name.setText(cartList.get(position).name);
        //holder.txt_amount.setNumber(String.valueOf(cartList.get(position).quantity));
        holder.txt_product_price.setText(new StringBuilder("Rs.").append(cartList.get(position).unitPrice));

        sub_total = cartList.get(position).unitPrice * cartList.get(position).quantity;
        holder.txt_total_price.setText("Sub-Total: " + String.valueOf(sub_total));


        holder.item_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cart cart = cartList.get(position);
                Common.cartRepository.deleteCartItem(cart);

                //Icon Count
                egrocery.badge.setText(String.valueOf(Common.cartRepository.countCartItems()));


                com.example.lubna.cloverweb.Cart.txt_total_price.setText(
                        String.valueOf(Common.cartRepository.sumCartItems()));


                if (Common.cartRepository.countCartItems() == 0) {
                    com.example.lubna.cloverweb.Cart.layout01.setVisibility(View.GONE);
                    com.example.lubna.cloverweb.Cart.layout02.setVisibility(View.VISIBLE);
                } else {
                    com.example.lubna.cloverweb.Cart.layout01.setVisibility(View.VISIBLE);
                    com.example.lubna.cloverweb.Cart.layout02.setVisibility(View.GONE);
                }
                c.getGst(context);
                //c.Cal_Final_price();
            }
        });

        holder.btn_increase_quantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int q = Integer.parseInt(holder.et_quantity.getText().toString());
                q = q + 1;
                Common.cartRepository.updateQ(q, cartList.get(position).pid);
                String quantity = String.valueOf(Common.cartRepository.getQ(cartList.get(position).pid));
                holder.et_quantity.setText(quantity);
                int total = Integer.parseInt(
                        com.example.lubna.cloverweb.Cart.txt_total_price.getText().toString());
                int unit_price = cartList.get(position).unitPrice;
                int final_price = total + unit_price;
                double gst = final_price * 0.17;
                double new_price = final_price + gst;
                com.example.lubna.cloverweb.Cart.txt_total_price.setText(String.valueOf(final_price));
                com.example.lubna.cloverweb.Cart.txt_final_price.setText(String.valueOf(new_price));
                com.example.lubna.cloverweb.Cart.txt_gst.setText(String.valueOf(df2.format(gst)));
            }
        });

        holder.btn_decrease_quantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int q = Integer.parseInt(holder.et_quantity.getText().toString());
                if (q > 1) {
                    q = q - 1;
                    Common.cartRepository.updateQ(q, cartList.get(position).pid);
                    String quantity = String.valueOf(Common.cartRepository.getQ(cartList.get(position).pid));
                    holder.et_quantity.setText(quantity);
                    int total = Integer.parseInt(
                            com.example.lubna.cloverweb.Cart.txt_total_price.getText().toString());
                    int unit_price = cartList.get(position).unitPrice;
                    int final_price = total - unit_price;
                    double gst = final_price * 0.17;
                    double new_price = final_price + gst;
                    com.example.lubna.cloverweb.Cart.txt_total_price.setText(String.valueOf(final_price));
                    com.example.lubna.cloverweb.Cart.txt_final_price.setText(String.valueOf(new_price));
                    com.example.lubna.cloverweb.Cart.txt_gst.setText(String.valueOf(df2.format(gst)));
                }
            }
        });

    }

    class CartViewHolder extends RecyclerView.ViewHolder {
        ImageView img_product;
        TextView txt_product_name, txt_product_price, txt_total_price;
        Button item_delete, btn_increase_quantity, btn_decrease_quantity;
        EditText et_quantity;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);

            img_product = itemView.findViewById(R.id.img_product);
            txt_product_name = itemView.findViewById(R.id.txt_product_name);
            txt_product_price = itemView.findViewById(R.id.txt_product_price);
            //txt_amount = itemView.findViewById(R.id.txt_amount);
            txt_total_price = itemView.findViewById(R.id.txt_total_price);
            item_delete = itemView.findViewById(R.id.item_delete);

            //quantity
            btn_decrease_quantity = itemView.findViewById(R.id.btn_decrease_quantity);
            btn_increase_quantity = itemView.findViewById(R.id.btn_increase_quantity);
            et_quantity = itemView.findViewById(R.id.et_quantity);
        }
    }
}
