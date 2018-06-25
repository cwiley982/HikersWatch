package com.caitlynwiley.hikerswatch;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    LocationManager locationManager;
    LocationListener locationListener;
    Geocoder geocoder;

    TextView latitudeText;
    TextView longitudeText;
    TextView accuracyText;
    TextView altitudeText;
    TextView addressText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        latitudeText = findViewById(R.id.latitudeText);
        longitudeText = findViewById(R.id.longitudeText);
        accuracyText = findViewById(R.id.accuracyText);
        altitudeText = findViewById(R.id.altitudeText);
        addressText = findViewById(R.id.addressText);

        geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                latitudeText.setText(getString(R.string.latitude) + " " + ((Double) location.getLatitude()).toString());
                longitudeText.setText(getString(R.string.longitude) + " " + ((Double) location.getLongitude()).toString());
                accuracyText.setText(getString(R.string.accuracy) + " " + ((Float) location.getAccuracy()).toString());
                altitudeText.setText(getString(R.string.altitude) + " " + ((Double) location.getAltitude()).toString());

                try {
                    List<Address> addressList = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                    Address address = addressList.get(0);

                    String addressString = getString(R.string.address) + "\n";

                    if (address.getFeatureName() != null) {
                        addressString += address.getFeatureName() + "\n";
                    }

                    if (address.getThoroughfare() != null) {
                        addressString += address.getThoroughfare() + "\n";
                    }

                    if (address.getLocality() != null) {
                        addressString += address.getLocality() + ", ";
                    }

                    if (address.getAdminArea() != null) {
                        addressString += address.getAdminArea() + "\n";
                    }

                    if (address.getCountryName() != null) {
                        addressString += address.getCountryName();
                    }

                    addressText.setText(addressString);
                } catch (IOException e) {
                    e.printStackTrace();
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
        };

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // need to ask for permission
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 2);
        } else {
            // already have permission
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
    }
}
