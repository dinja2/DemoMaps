package com.example.denis.demomaps;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements LocationListener {

    GPSTracker gps;
    private GoogleMap mMap;
    Intent intentThatCalled;
    public double latitude;
    public double longitude;
    public LocationManager locationManager;
    private List<LatLng> list; //added
    Polyline polyline; //added

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        setUpMapIfNeeded();
    }



    private void setUpMapIfNeeded() {
        if (mMap == null) {
            mMap = ((SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map)).getMap();
            if (mMap != null) {
                getLocation();
            }
        }
    }


    private void getLocation() {
              //  if(gps.canGetLocation()){
                    gps = new GPSTracker(MapsActivity.this);
                    latitude = gps.getLatitude();
                    longitude = gps.getLongitude();
                    mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                    LatLng latLng = new LatLng(latitude, longitude);
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                   // mMap.animateCamera(CameraUpdateFactory.zoomTo(20));
                   // mMap.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)).title("You are here"));
                    mMap.getUiSettings().setZoomControlsEnabled(true);
                    list = new ArrayList<LatLng>();
              //  } else {
                //    gps.showSettingsAlert();
              // }
            }


    private void redrawLine() {
        mMap.clear();  //clears all Markers and Polylines
        PolylineOptions options;
        if(polyline == null){
            options = new PolylineOptions();
            for(int i = 0, tam = list.size(); i < tam ; i++){
                options.add(list.get(i));
            }
            options.color(Color.BLUE);
            polyline = mMap.addPolyline(options);
        }
        else{
            polyline.setPoints(list);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            locationManager.removeUpdates(this);
        }

    }


    @Override
    public void onLocationChanged(Location location) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            locationManager.removeUpdates(this);
            LatLng latLng = new LatLng(location.getLatitude(), location.getLatitude());
            list.add(latLng);
            redrawLine();
        }
    }


    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}




