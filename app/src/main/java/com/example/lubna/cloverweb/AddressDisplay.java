package com.example.lubna.cloverweb;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
public class AddressDisplay extends Fragment
{
 View view;
 Button submit;
 RelativeLayout lay;
 Button submit3;
 Button submit4;
    @Nullable
    @Override
    public View onCreateView (@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        view = inflater.inflate(R.layout.addressdisplay, container, false);
        return view;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

//        lay= view.findViewById(R.id.linearlayout);
//        addaddress = new TextView(getContext());
//        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//        addaddress.setLayoutParams(lp);
//        addaddress.setGravity(Gravity.CENTER);
//        addaddress.setVisibility(addaddress.VISIBLE);
//        lay.addView(addaddress);
          submit = view.findViewById(R.id.addadd);
//        String value = getArguments().getString("address");
//        addaddress.setText(value);
//        submit3 = view.findViewById(R.id.addadd);
          submit.setOnClickListener(new View.OnClickListener()
          {
            @Override
            public void onClick(View v)
            {
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                Addaddress NAME = new Addaddress();
                fragmentTransaction.replace(R.id.drawer_layout, NAME);
                fragmentTransaction.commit();
            }
        });
    }
}
