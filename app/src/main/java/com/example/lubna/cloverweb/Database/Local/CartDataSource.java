package com.example.lubna.cloverweb.Database.Local;

import com.example.lubna.cloverweb.Database.DataSource.ICartDataSource;
import com.example.lubna.cloverweb.Database.ModelDB.Cart;

import java.util.List;

import io.reactivex.Flowable;

public class CartDataSource implements ICartDataSource {

    private CartDAO cartDAO;
    private static CartDataSource instance;

    public CartDataSource(CartDAO cartDAO) {
        this.cartDAO = cartDAO;
    }

    public static CartDataSource getInstance(CartDAO cartDAO)
    {
        if (instance == null)
            instance = new CartDataSource(cartDAO);
        return instance;
    }

    @Override
    public Flowable<List<Cart>> getCartItems() {
        return cartDAO.getCartItems();
    }

    @Override
    public Flowable<List<Cart>> getCartItemById(int cartItemId) {
        return cartDAO.getCartItemById(cartItemId);
    }

    @Override
    public int countCartItems() {
        return cartDAO.countCartItems();
    }

    @Override
    public int sumCartItems() {
        return cartDAO.sumCartItems();
    }

    @Override
    public void emptyCart() {
        cartDAO.emptyCart();
    }

    @Override
    public void insertToCart(Cart... carts) {
        cartDAO.insertToCart(carts);
    }

    @Override
    public void updateCart(Cart... carts) {
        cartDAO.updateCart(carts);
    }

    @Override
    public void deleteCartItem(Cart cart) {
        cartDAO.deleteCartItem(cart);
    }

    @Override
    public void updateCartItem(Cart cart) {
        cartDAO.updateCartItem(cart);
    }

    @Override
    public int countItem(String d) {
        return cartDAO.countItem(d);
    }

    @Override
    public String[] getID() {
        return cartDAO.getID();
    }

    @Override
    public int[] getQuantity() {
        return cartDAO.getQuantity();
    }

    @Override
    public void updateQ(int q, String d) {
        cartDAO.updateQ(q,d);
    }

    @Override
    public int getQ(String d) {
        return cartDAO.getQ(d);
    }

}
