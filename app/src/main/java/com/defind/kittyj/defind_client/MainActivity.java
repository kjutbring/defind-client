package com.defind.kittyj.defind_client;

import android.Manifest;
import android.app.job.*;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Base64;
import android.util.Log;

import com.defind.kittyj.defind_client.services.MyjobService;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class MainActivity extends AppCompatActivity implements LocationListener {

    LocationManager locationManager;
    String provider;
    String SECRETKEY = "kittys_super_cool_secret";


    // job service things
    private JobScheduler mJobScheduler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        int hasFineLocationPermission = checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION);
        int hasCoarseLocationPermission = checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION);
        int hasPhoneStatePermission = checkSelfPermission(Manifest.permission.READ_PHONE_STATE);
        List<String> permissions = new ArrayList<String>();

        if (hasFineLocationPermission != PackageManager.PERMISSION_GRANTED &&
                hasCoarseLocationPermission != PackageManager.PERMISSION_GRANTED
                ) {
            permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
            permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        }

        if (hasPhoneStatePermission != PackageManager.PERMISSION_GRANTED) {
            permissions.add(Manifest.permission.READ_PHONE_STATE);
        }

        if (!permissions.isEmpty()) {
            requestPermissions(permissions.toArray(new String[permissions.size()]), 1);
        }

        constructJob();

    }

    public void constructJob() {
        mJobScheduler = (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);
        JobInfo.Builder builder = new JobInfo.Builder(1,
                new ComponentName(getPackageName(), MyjobService.class.getName()));
        builder.setPeriodic(1000);

        mJobScheduler.schedule(builder.build());
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onLocationChanged(Location location) {
        Double lat = location.getLatitude();
        Double lng = location.getLongitude();

        Log.i("Latitude", lat.toString());
        Log.i("Longitude", lng.toString());
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
