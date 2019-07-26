package com.example.lubna.cloverweb.Database.Local;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.lubna.cloverweb.Database.ModelDB.Cart;
import java.util.List;

import io.reactivex.Flowable;

@Dao
public interface CartDAO {

    @Query("SELECT * FROM Cart")
    Flowable<List<Cart>> getCartItems();

    @Query("SELECT * FROM Cart WHERE id=:cartItemId")
    Flowable<List<Cart>> getCartItemById(int cartItemId);

    @Query("SELECT COUNT(*) FROM Cart")
    int countCartItems();

    @Query("SELECT SUM(quantity * unitPrice) as Total FROM Cart")
    int sumCartItems();

    @Query("DELETE FROM Cart")
    void emptyCart();

    @Insert
    void insertToCart(Cart...carts);

    @Update
    void updateCart(Cart...carts);

    @Delete
    void deleteCartItem(Cart cart);

    @Update
    void updateCartItem(Cart cart);

    @Query("SELECT COUNT(*) FROM Cart WHERE pid=:d")
    int countItem(String d);

    @Query("SELECT pid as P_ID FROM Cart")
    String[] getID();

    @Query("SELECT quantity as quan FROM Cart")
    int[] getQuantity();

    @Query("UPDATE Cart SET quantity = :q WHERE pid = :d")
    void updateQ(int q, String d);

    @Query("SELECT quantity as Q FROM Cart WHERE pid = :d")
    int getQ(String d);

}
