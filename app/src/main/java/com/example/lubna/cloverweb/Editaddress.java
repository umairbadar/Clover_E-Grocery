package com.example.lubna.cloverweb;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
public class Editaddress extends AppCompatActivity
{
    EditText edit1,edit2,edit3,edit4;
    String getaddress,getcity,getprovince,getcountry;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editaddress);
        edit1 = findViewById(R.id.address1);
        edit2 = findViewById(R.id.city1);
        edit3 = findViewById(R.id.province1);
        edit4 = findViewById(R.id.country1);
        Intent intent=getIntent();
        getaddress=intent.getStringExtra("addaddress");
        getcity=intent.getStringExtra("city");
        getprovince = intent.getStringExtra("province");
        getcountry = intent.getStringExtra("country");
        edit1.setText(getaddress);
        edit2.setText(getcity);
        edit3.setText(getprovince);
        edit4.setText(getcountry);
    }
}
