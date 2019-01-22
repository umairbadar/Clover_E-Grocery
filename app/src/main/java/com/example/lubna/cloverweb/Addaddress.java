package com.example.lubna.cloverweb;
import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import java.io.IOException;
import java.util.List;
import java.util.Locale;
public class Addaddress extends Fragment implements OnMapReadyCallback, android.location.LocationListener
{
    GoogleMap gmap;
    MapView mapview;
    Geocoder geocoder;
    View view;
    Button addadd2,addadd3;
    String address,city,country,province;
    String address100,city100,country100,province100;
    double lat,lng;
    LinearLayout l2;
    TextView latitude1, longitude1,address2,city1,province1,country1;
    private LocationManager locationManager;
    List<Address>addresses;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        view = inflater.inflate(R.layout.addaddress, container, false);
        return view;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        l2 = view.findViewById(R.id.addaddressfragxml);
        latitude1 = view.findViewById(R.id.latitude);
        longitude1 = view.findViewById(R.id.longitude);
        address2= view.findViewById(R.id.address);
        city1= view.findViewById(R.id.city);
        province1= view.findViewById(R.id.province);
        country1 = view.findViewById(R.id.country);
        addadd2 = view.findViewById(R.id.addaddressbt);
        addadd3 = view.findViewById(R.id.addaddressbt2);
        locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            return;
        }
        Location location = locationManager.getLastKnownLocation(locationManager.NETWORK_PROVIDER);
        onLocationChanged(location);
        mapview = view.findViewById(R.id.maps);
        if(mapview !=null)
        {
            mapview.onCreate(null);
            mapview.onResume();
            mapview.getMapAsync(this);
        }
        addadd2.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
//                FragmentManager fragmentManager = getFragmentManager();
//                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                Bundle args = new Bundle();
//                AddressDisplay nay = new AddressDisplay();
//                args.putString("address",address);
//                args.putString("city",city);
//                args.putString("province",province);
//                args.putString("country",country);
//                nay.setArguments(args);
//                fragmentTransaction.replace(R.id.drawer_layout, nay);
//                fragmentTransaction.commit();
            }
        });
        addadd3.setOnClickListener(new View.OnClickListener()
        {
            @SuppressLint("ResourceType")
            @Override
            public void onClick(View v)
            {
                address100= address2.getText().toString();
                city100=city1.getText().toString();
                province100 = province1.getText().toString();
                country100 = country1.getText().toString();
                Intent i = new Intent(getActivity(),Editaddress.class);
                i.putExtra("addaddress",address100);
                i.putExtra("city",city100);
                i.putExtra("province",province100);
                i.putExtra("country",country100);
                startActivity(i);
            }
        });
    }
    @Override
    public void onLocationChanged(Location location)
    {
        latitude1.setText(String.valueOf(location.getLatitude()));
        longitude1.setText(String.valueOf(location.getLongitude()));
    }
    @Override
    public void onMapReady(GoogleMap googleMap)
    {
        if (latitude1.getText() != null && longitude1.getText() != null)
        {
            geocoder= new Geocoder(getContext(),Locale.getDefault());
            lat = Double.parseDouble(latitude1.getText().toString());
            lng = Double.parseDouble(longitude1.getText().toString());
            try
            {
                addresses = geocoder.getFromLocation(lat,lng,1);
                address = addresses.get(0).getAddressLine(0);
                city = addresses.get(0).getLocality();
                province = addresses.get(0).getAdminArea();
                country = addresses.get(0).getCountryName();
                address2.setText("    "+address);
                city1.setText("    "+city);
                province1.setText("    "+province);
                country1.setText("    "+country);
                }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            MapsInitializer.initialize(getContext());
            gmap=googleMap;
            googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            googleMap.addMarker(new MarkerOptions().position(new LatLng(Double.valueOf(latitude1.getText().toString()),Double.valueOf(longitude1.getText().toString()))).title("Hascol Petrol pump").snippet("My work place"));
            CameraPosition liberty = CameraPosition.builder().target(new LatLng(Double.valueOf(latitude1.getText().toString()),Double.valueOf(longitude1.getText().toString()))).zoom(16).bearing(0).tilt(45).build();
            googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(liberty));
        }
    }
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras)
    {
    }
    @Override
    public void onProviderEnabled(String provider)
    {
    }
    @Override
    public void onProviderDisabled(String provider)
    {
    }
}
