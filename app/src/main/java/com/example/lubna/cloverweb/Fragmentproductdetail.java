package com.example.lubna.cloverweb;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
public class Fragmentproductdetail extends Fragment
{
    private TabLayout tabLayout;
    private AppBarLayout appBarLayout;
    private ViewPager viewPager;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragmentproductdetailtab, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        tabLayout = view.findViewById(R.id.tablelayout2);
        appBarLayout = view.findViewById(R.id.appbarid);
        viewPager = view.findViewById(R.id.viewpager_id);
        Viewpageadapter adapter = new Viewpageadapter(getChildFragmentManager());
        adapter.AddFragment(new FragmentQuiz(), "Product 1");
        adapter.AddFragment(new FragmentExplore(), "Product 2");
        adapter.AddFragment(new Fragmentstore(), "Product 3");
        adapter.AddFragment(new Tyresfragment(), "product 4 ");
        adapter.AddFragment(new Bottlefragment(),"Product 5");
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }
}
