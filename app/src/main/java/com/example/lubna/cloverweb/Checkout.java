package com.example.lubna.cloverweb;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
public class Checkout extends AppCompatActivity
{
Button payment;
TextView tvdelivery;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);
        payment = findViewById(R.id.buttonprocedecheck2);
        tvdelivery = findViewById(R.id.adresshome1);
        payment.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent i = new Intent(getApplicationContext(),PlaceOrder.class);
                startActivity(i);
                finish();
            }
        });
        tvdelivery.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent i = new Intent(getApplicationContext(),Editaddress.class);
                startActivity(i);
                finish();
            }
        });
    }
}
