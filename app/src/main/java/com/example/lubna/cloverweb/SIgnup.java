package com.example.lubna.cloverweb;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
public class SIgnup extends AppCompatActivity implements View.OnClickListener
{
EditText username,email,password,conf,phone;
Button signup;
TextView login;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        username = findViewById(R.id.username);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        conf = findViewById(R.id.confirmpassword);
        phone = findViewById(R.id.phonenumber);
        signup = findViewById(R.id.signup);
        login = findViewById(R.id.login);
        SpannableString content = new SpannableString("Already have an account?           Login");
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        login.setText(content);
        signup.setOnClickListener(this);
        login.setOnClickListener(new View.OnClickListener()
        {
    @Override
    public void onClick(View v)
    {
        Intent i = new Intent(getApplicationContext(),Loginpage.class);
        startActivity(i);
        finish();
    }
    });
        }
    public void checkvalidation()
    {
        if(username.getText().toString().equals(""))
        {
            username.setError("cant be empty");

        }
        if(email.getText().toString().equals(""))
        {
            email.setError("cant be empty");
        }
        if(password.getText().toString().equals(""))
        {
            password.setError("cant be empty");
        }
        if(conf.getText().toString().equals(""))
        {
            conf.setError("cant be empty");
        }
        if(phone.getText().toString().equals(""))
        {
            phone.setError("cant be empty");
        }
        else
        {
          login();
        }
    }
public void login()
{
    Intent i = new Intent(getApplicationContext(),Loginpage.class);
    startActivity(i);
    finish();
}
    @Override
    public void onClick(View v)
    {
        checkvalidation();
    }
    @Override
    public void onBackPressed()
    {
        Intent i = new Intent(getApplicationContext(),Loginpage.class);
        startActivity(i);
        finish();
    }
}
