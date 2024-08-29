package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.myapplication.databinding.ActivityGoogleMapBinding;

import java.util.List;
import java.util.Locale;

public class GoogleMapActivity extends FragmentActivity implements OnMapReadyCallback {
    private String countryToFocus;
    private GoogleMap mMap;
    private ActivityGoogleMapBinding binding;
    private Geocoder geocoder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityGoogleMapBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        countryToFocus = getIntent().getExtras().getString("categoryLocation");
//        geocoder = new Geocoder(this, Locale.getDefault());
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title(countryToFocus));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(10));
        findCountryMoveCamera();
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(@NonNull LatLng latLng) {

                //setup your Geocoder
                Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                String country = "null" ;
                List<Address> addresses = null;

                //this commented code takes you to Malaysia when you click anywhere on the
                //map. Comment out lines 95 - 106, then uncomment 81 - 93
                try {

                    //use the geocoder and return max 1 result (address) from the location "Malaysia"
                    addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);


                } catch (Exception e) {
                    Log.d("No Location", "Couldn't find location");
                    
                        // Show Toast message if no addresses found

                }

                //if there is more than 0 addresses, then get the lat and long from the 0th
                //address, and move the camera there
                if(addresses.size() > 0){
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(addresses.get(0).getLatitude(), addresses.get(0).getLongitude())));
                }

//                try {
//
//                    //use the geocoder and return max 1 result (address) from the lat and long clicked
//                    addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
//                } catch (Exception e) {
//                    Log.d("No Location", "Couldn't find location");
//                }
//                  //if there is more than 0 addresses, then get the countryname
//                if(addresses.size() > 0){
//                    country = addresses.get(0).getCountryName();
//                }
//                Snackbar.make(mapFragment.getView(), "you clicked on " + country, Snackbar.LENGTH_SHORT).show();
            }
        });
    }
    private void findCountryMoveCamera() {
        // initialise Geocode to search location using String
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());

        // getFromLocationName method works for API 33 and above
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {

            /**
             * countryToFocus: String value, any string we want to search
             * maxResults: how many results to return if search was successful
             * successCallback method: if results are found, this method will be executed
             *                          runs in a background thread
             */
            geocoder.getFromLocationName(countryToFocus, 1, addresses -> {
                // if there are results, this condition would return true
                if (!addresses.isEmpty()) {
                    // run on UI thread as the user interface will update once set map location
                    runOnUiThread(() -> {
                        // define new LatLng variable using the first address from list of addresses
                        LatLng newAddressLocation = new LatLng(
                                addresses.get(0).getLatitude(),
                                addresses.get(0).getLongitude()
                        );

                        // repositions the camera according to newAddressLocation
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(newAddressLocation));

                        // just for reference add a new Marker
                        mMap.addMarker(
                                new MarkerOptions()
                                        .position(newAddressLocation)
                                        .title(countryToFocus)
                        );

                        // set zoom level to 8.5f or any number between range of 2.0 to 21.0
                        mMap.animateCamera(CameraUpdateFactory.zoomTo(8.5f));
                    });
                }else{ runOnUiThread(() -> {
                    Toast.makeText(GoogleMapActivity.this, "Category address not found", Toast.LENGTH_LONG).show();
                });}

            });
        }
    }
}