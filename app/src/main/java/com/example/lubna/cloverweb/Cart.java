package com.example.lubna.cloverweb;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
public class Cart extends Fragment
{
    ListView lview2;
    Button btn2;
    TextView cross;
    TextView text;
    String gettext2;
    Button backward,forward;
    private TextView tv;
    private String text1;
    private Button btnbc;
    private Button textviewamount2;
    int a;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        final ViewGroup rootview = (ViewGroup) inflater.inflate(R.layout.cart, container, false);
        textviewamount2 = rootview.findViewById(R.id.buttonprocedecheck);
        textviewamount2.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent i = new Intent(getContext(),Checkout.class);
                startActivity(i);
                }
            });
        return rootview;
        }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        backward = view.findViewById(R.id.backword);
        forward = view.findViewById(R.id.buttonforward);
        text = view.findViewById(R.id.textviewtext);
        lview2 = view.findViewById(R.id.listcart);
        String[] ds = {"Rice"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.cartlisting,R.id.name_food,ds);
        lview2.setAdapter(adapter);
        lview2.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                tv = ((TextView) view.findViewById(R.id.textviewtext));
                btnbc = ((Button) view.findViewById(R.id.backword));
                Button btnfr = ((Button) view.findViewById(R.id.buttonforward));
                btnbc.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        text1 = tv.getText().toString();
                        int ab = Integer.parseInt(text1);
                        ab = ab - 1;
                        if (ab < 1)
                        {
                            tv.setText(String.valueOf(1));
                        }
                        else
                        {
                            tv.setText(String.valueOf(ab));
                        }
                    }
                });
                btnfr.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v)
                    {
                        text1 = tv.getText().toString();
                        int ab = Integer.parseInt(text1);
                        ab = ab + 1;
                        tv.setText(String.valueOf(ab));
                    }
                });
            }
        });
    }

}
