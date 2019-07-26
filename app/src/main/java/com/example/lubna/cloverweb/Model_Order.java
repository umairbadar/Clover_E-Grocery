package com.example.lubna.cloverweb;

public class Model_Order {
    private String date;
    private String total;
    private String status;
    private String address;
    private String order_id;

    public String getDate() {
        return date;
    }

    public String getTotal() {
        return total;
    }

    public String getStatus() {
        return status;
    }

    public String getAddress() {
        return address;
    }
    public String getOrder_id()
    {
        return order_id;
    }

    public Model_Order(String date, String total, String status, String address,String order_id) {
        this.date = date;
        this.total = total;
        this.status = status;
        this.address = address;
        this.order_id=order_id;
    }
}
