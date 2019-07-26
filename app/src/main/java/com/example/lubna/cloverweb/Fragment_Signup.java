package com.example.lubna.cloverweb;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.PriorityQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Fragment_Signup extends Fragment {

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    View view;
    private TextView tv_already_member;
    private EditText et_email,et_password;
    private Button btn_next;
    ProgressDialog progressDialog;

    FirebaseAuth firebaseAuth;
    String codeSent;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_signup,container,false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        sharedPreferences = getActivity().getSharedPreferences("Pre",Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setTitle("Please wait");
        progressDialog.setMessage("Loading...");

        firebaseAuth = FirebaseAuth.getInstance();


        //edittext email address
        et_email = view.findViewById(R.id.et_email);

        //edittext password and confirm password
        et_password = view.findViewById(R.id.et_password);

        btn_next = view.findViewById(R.id.btn_next);
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressDialog.setCancelable(false);
                progressDialog.show();
                String email = et_email.getText().toString().trim();
                String password = et_password.getText().toString().trim();

                if (email.isEmpty())
                {
                    progressDialog.dismiss();
                    et_email.setError("Please enter email address");
                    et_email.requestFocus();
                }
                else {
                    if (isEmailValid(email)) {
                        if (password.isEmpty()) {
                            progressDialog.dismiss();
                            et_password.setError("Please enter password");
                            et_password.requestFocus();
                        } else {
                            if (password.length() < 6)
                            {
                                progressDialog.dismiss();
                                et_password.setError("Password must be 6 characters long");
                                et_password.requestFocus();
                            }
                            else {
                                progressDialog.dismiss();
                                editor.putString("UserEmail", email);
                                editor.putString("UserPassword", password);
                                editor.apply();
                                Fragment fragment = new FragmentPhoneVerification();
                                Bundle args = new Bundle();
                                args.putString("UserEmail", email);
                                args.putString("UserPassword", password);
                                fragment.setArguments(args);
                                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                                ft.replace(R.id.content_egrocery, fragment);
                                ft.commit();
                                //registerUserEmail(email,password);
                            }
                        }
                    }
                    else
                    {
                        progressDialog.dismiss();
                        et_email.setError("Please enter valid email");
                        et_email.requestFocus();
                    }
                }
            }
        });

        //textview to open login activity
        tv_already_member = view.findViewById(R.id.tv_already_member);
        tv_already_member.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new loginfragment();
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.content_egrocery,fragment);
                ft.addToBackStack(null);
                ft.commit();
            }
        });
    }

    public void registerUserEmail(String email, String password)
    {
        firebaseAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful())
                        {
                            sendEmailVerification();
                        }
                        else
                        {
                            if(task.getException()instanceof FirebaseAuthUserCollisionException)
                            {
                                progressDialog.dismiss();
                                Toast.makeText(getContext(), "You are already Registered ", Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                progressDialog.dismiss();
                                Toast.makeText(getContext(),task.getException().getMessage(),Toast.LENGTH_LONG).show();
                            }
                        }

                    }
                });
    }

    private void sendEmailVerification() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user!=null){
            user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        //progressDialog.dismiss();
                        /*Fragment fragment = new FragmentPhoneVerification();
                        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                        ft.replace(R.id.content_egrocery,fragment);
                        ft.commit();*/
                        Toast.makeText(getContext(),"Registered Successfully! Check your email for verification",Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

    }

    public static boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}
