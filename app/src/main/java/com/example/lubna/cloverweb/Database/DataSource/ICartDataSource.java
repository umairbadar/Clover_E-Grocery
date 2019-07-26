package com.example.lubna.cloverweb.Database.DataSource;

import com.example.lubna.cloverweb.Database.ModelDB.Cart;

import java.util.List;

import io.reactivex.Flowable;

public interface ICartDataSource {

    Flowable<List<Cart>> getCartItems();
    Flowable<List<Cart>> getCartItemById(int cartItemId);
    int countCartItems();
    int sumCartItems();
    void emptyCart();
    void insertToCart(Cart...carts);
    void updateCart(Cart...carts);
    void deleteCartItem(Cart cart);
    void updateCartItem(Cart cart);
    int countItem(String d);
    String[] getID();
    int[] getQuantity();
    void updateQ(int q, String d);
    int getQ(String d);
}
