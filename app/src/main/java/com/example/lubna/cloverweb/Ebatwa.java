package com.example.lubna.cloverweb;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
public class Ebatwa extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ebatwa);
    }
    @Override
    public void onBackPressed()
    {
        Intent i = new Intent(getApplicationContext(),PlaceOrder.class);
        startActivity(i);
        finish();
    }
}
