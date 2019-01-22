package com.example.lubna.cloverweb;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import java.util.ArrayList;
import java.util.List;
public class Viewpageadapter extends FragmentPagerAdapter
{
    private final List<Fragment> fragmentList = new ArrayList<>();
    private final List<String> FragmentListTitles = new ArrayList<>();
    public Viewpageadapter(FragmentManager fm)
    {
       super(fm);
    }
    @Override
    public Fragment getItem(int position)
    {
        return fragmentList.get(position);
    }
    @Override
    public int getCount()
    {
        return FragmentListTitles.size();
    }
    @Override
    public long getItemId(int position)
    {
        return position;
    }
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Nullable
    @Override
    public CharSequence getPageTitle(int position)
    {
        return FragmentListTitles.get(position);
    }
    public void AddFragment(Fragment fragment,String Title)
    {
        fragmentList.add(fragment);
        FragmentListTitles.add(Title);
    }


}
