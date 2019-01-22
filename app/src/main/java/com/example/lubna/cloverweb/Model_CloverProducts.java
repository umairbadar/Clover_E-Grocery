package com.example.lubna.cloverweb;

public class Model_CloverProducts {

    private String product_name;
    private String product_price;
    private String product_image;

    public String getProduct_name() {
        return product_name;
    }

    public String getProduct_price() {
        return product_price;
    }

    public String getProduct_image() {
        return product_image;
    }

    public Model_CloverProducts(String product_name, String product_price, String product_image) {
        this.product_name = product_name;
        this.product_price = product_price;
        this.product_image = product_image;
    }
}
