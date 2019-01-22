package com.example.lubna.cloverweb;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.miguelcatalan.materialsearchview.MaterialSearchView;
public class egrocery extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener
{
    MaterialSearchView searchView;
    private SharedPreferences sharedp;
    private Boolean saveLogin;
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_egrocery);
        TextView text1 = findViewById(R.id.txtsearch);
        Toolbar toolbar =  findViewById(R.id.toolbar);
        sharedp = getSharedPreferences("MyPre", Context.MODE_PRIVATE);
        saveLogin = sharedp.getBoolean("saveLogin",false);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);
        serachviewcode();
//        getSupportActionBar().setDisplayUseLogoEnabled(true);
        DrawerLayout drawer =  findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        displaySelectedScreen(R.id.home);
        text1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               serachviewcode();
            }
        });
    }
    @Override
    public void onBackPressed()
    {
        if (searchView.isSearchOpen())
        {
            searchView.closeSearch();
        }
        else
        {
            super.onBackPressed();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.egrocery, menu);
        MenuItem item = menu.findItem(R.id.serach_view);
        searchView.setMenuItem(item);
        return super.onCreateOptionsMenu(menu);
    }
private void displaySelectedScreen(int id)
{
    if (saveLogin.equals(true))
    {
        NavigationView navigationView = findViewById(R.id.nav_view);
        Menu menu = navigationView.getMenu();
        menu.findItem(R.id.loginfrag).setEnabled(false);
    }
    Fragment fragment=null;
    switch(id)
    {
        case R.id.loginfrag:
            fragment = new loginfragment();
            break;
        case R.id.home:
            fragment = new Home();
            break;
        case R.id.cart:
            fragment = new Cart();
            break;
        case R.id.address:
            fragment = new AddressDisplay();
            break;
        case R.id.order:
            fragment = new Order();
            break;
        case R.id.terms:
            fragment = new FragmentTerms();
            break;
        case R.id.privacy:
            fragment = new FragmentPolicy();
            break;
        case R.id.contact:
            fragment = new FragmentContact();
            break;
        case R.id.about:
            fragment = new FragmentAbout();
            break;
        case R.id.Membership:
            fragment = new Membership();
            break;
        case R.id.logout:
            SharedPreferences pref =  getSharedPreferences("MyPre", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = pref.edit();
            editor.clear();
            editor.apply();
            startActivity(getIntent());
            break;
    }
    if(fragment!=null)
    {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content_egrocery,fragment);
        ft.commit();
    }
    DrawerLayout drawer =  findViewById(R.id.drawer_layout);
    drawer.closeDrawer(GravityCompat.START);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.serach_view:
                return true;
                default:
                return super.onOptionsItemSelected(item);
        }
    }
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
//        if(id == R.id.logout)
//    {
//        finish();
//    }
        displaySelectedScreen(id);
        return true;
    }
    private void serachviewcode()
    {
        searchView = findViewById(R.id.serach_view);
        searchView.setSuggestions(getResources().getStringArray(R.array.query_suggestions));
        searchView.setEllipsize(true);
        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener()
        {
            @Override
            public boolean onQueryTextSubmit(String query)
            {
                Toast.makeText(getApplicationContext(),query,Toast.LENGTH_LONG).show();
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText)
            {
                return false;
            }
        });
        searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener()
        {
            @Override
            public void onSearchViewShown()
            {
                }
            @Override
            public void onSearchViewClosed()
            {
                }
        });
    }
}
