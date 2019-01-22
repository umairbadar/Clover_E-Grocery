package com.example.lubna.cloverweb;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
public class Carddetails extends AppCompatActivity
{
    EditText et1,et2,et3;
    Button btnplaceorder;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carddetails);
        et1 = findViewById(R.id.card2);
        et2 = findViewById(R.id.card4);
        et3 = findViewById(R.id.card6);
        btnplaceorder = findViewById(R.id.btn12);
        btnplaceorder.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                checkValidation();
            }
        });
    }
    @Override
    public void onBackPressed()
    {
        Intent i = new Intent(getApplicationContext(),PlaceOrder.class);
        startActivity(i);
        finish();
    }
    public boolean checkValidation()
    {
        boolean valid = true;
        if (et1.getText().toString().equals(""))
        {
            et1.setError("Can't be Empty");
            valid = false;
        }
        else if (et2.getText().toString().equals(""))
        {
            et2.setError("Can't be Empty");
            valid = false;
        }
        else if(et3.getText().toString().equals(""))
        {
            et3.setError("Can't be Empty");
            valid = false;
        }
        else
        {
            Toast.makeText(getApplicationContext(),"Data sent",Toast.LENGTH_LONG).show();
        }
        return valid;
    }
}
