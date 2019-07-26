package com.example.lubna.cloverweb.Database.DataSource;

import com.example.lubna.cloverweb.Database.ModelDB.Cart;

import java.util.List;

import io.reactivex.Flowable;

public class CartRepository implements ICartDataSource {

    private ICartDataSource iCartDataSource;

    public CartRepository(ICartDataSource iCartDataSource) {
        this.iCartDataSource = iCartDataSource;
    }

    private static CartRepository instance;

    public static CartRepository getInstance(ICartDataSource iCartDataSource)
    {
        if (instance == null)
            instance = new CartRepository(iCartDataSource);
        return instance;
    }

    @Override
    public Flowable<List<Cart>> getCartItems() {
        return iCartDataSource.getCartItems();
    }

    @Override
    public Flowable<List<Cart>> getCartItemById(int cartItemId) {
        return iCartDataSource.getCartItemById(cartItemId);
    }

    @Override
    public int countCartItems() {
        return iCartDataSource.countCartItems();
    }

    @Override
    public int sumCartItems() {
        return iCartDataSource.sumCartItems();
    }

    @Override
    public void emptyCart() {
        iCartDataSource.emptyCart();
    }

    @Override
    public void insertToCart(Cart... carts) {
        iCartDataSource.insertToCart(carts);
    }

    @Override
    public void updateCart(Cart... carts) {
        iCartDataSource.updateCart(carts);
    }

    @Override
    public void deleteCartItem(Cart cart) {
        iCartDataSource.deleteCartItem(cart);
    }

    @Override
    public void updateCartItem(Cart cart) {
        iCartDataSource.updateCartItem(cart);
    }

    @Override
    public int countItem(String d) {
        return iCartDataSource.countItem(d);
    }

    @Override
    public String[] getID() {
        return iCartDataSource.getID();
    }

    @Override
    public int[] getQuantity() {
        return iCartDataSource.getQuantity();
    }

    @Override
    public void updateQ(int q, String d) {
        iCartDataSource.updateQ(q,d);
    }

    @Override
    public int getQ(String d) {
        return iCartDataSource.getQ(d);
    }
}
