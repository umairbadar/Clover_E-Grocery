package com.example.lubna.cloverweb;

public class Model_OrderDetails {

    private String product_name;
    private String quantity;
    private String unitprice;



    public String getProduct_name() {
        return product_name;
    }

    public String getQuantity() {
        return quantity;
    }

    public String getUnitprice() {
        return unitprice;
    }

    public Model_OrderDetails( String product_name, String quantity, String unitprice) {

        this.product_name = product_name;
        this.quantity = quantity;
        this.unitprice = unitprice;
    }
}
