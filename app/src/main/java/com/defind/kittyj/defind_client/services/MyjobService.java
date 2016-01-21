package com.defind.kittyj.defind_client.services;

import android.Manifest;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.util.Base64;
import android.util.Log;

import com.defind.kittyj.defind_client.DeviceLocation;
import com.defind.kittyj.defind_client.MainActivity;

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
import java.util.Date;
import java.util.LinkedList;
import java.util.concurrent.ExecutionException;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;


/**
 * Created by kittyj on 1/20/16.
 */
public class MyjobService extends JobService {

    LocationManager locationManager;
    String provider;
    String SECRETKEY = "kittys_super_cool_secret";

    @Override
    public boolean onStartJob(JobParameters params) {
        Log.i("START JOB: ", String.valueOf(params.getJobId()));
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        provider = locationManager.getBestProvider(new Criteria(), false);
        sendLocation();

        return false;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        Log.i("STOP JOB: ", String.valueOf(params.getJobId()));
        return false;
    }

    public DeviceLocation createDeviceLocation(Location location) {

        TelephonyManager telephonyManager = (TelephonyManager) getBaseContext().getSystemService(Context.TELEPHONY_SERVICE);

        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    public void requestPermissions(@NonNull String[] permissions, int requestCode)
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for Activity#requestPermissions for more details.
            return null;
        }

        Date dateTime = new Date();
        Double lat = location.getLatitude();
        Double lng = location.getLongitude();
        String deviceId = telephonyManager.getDeviceId();

        DeviceLocation deviceLocation = new DeviceLocation(deviceId, lat,
                lng, dateTime);

        return deviceLocation;
    }

    public String generateJWT() {

        //encode secret to base64
        byte[] secret = null;
        try {
            secret = SECRETKEY.getBytes("utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        String base64Secret = Base64.encodeToString(secret, Base64.DEFAULT);

        String jwt = Jwts.builder().setSubject("kittyj").signWith(SignatureAlgorithm.HS256, base64Secret).compact();

        return jwt;
    }

    public JSONObject constructLocationJsonObject(DeviceLocation deviceLocation) {

        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("device", deviceLocation.getDevice());
            jsonObject.put("lat", deviceLocation.getLat().toString());
            jsonObject.put("lng", deviceLocation.getLng().toString());
            jsonObject.put("time", deviceLocation.getTime().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    public String sendLocation() {
        String url = "http://185.86.151.212:3000/api/location";
        String result = "";
        ApiAction apiAction = new ApiAction();

        try {
            result = apiAction.execute(url).get();
            return result;
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return result;
    }

    public class ApiAction extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {

            URL url;
            HttpURLConnection httpURLConnection = null;
            OutputStreamWriter outputStreamWriter;
            InputStream input;

            // check location permissions
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    public void requestPermissions(@NonNull String[] permissions, int requestCode)
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for Activity#requestPermissions for more details.
                return null;
            }
            Location location = locationManager.getLastKnownLocation(provider);

            DeviceLocation deviceLocation = createDeviceLocation(location);
            String jsonLocation = constructLocationJsonObject(deviceLocation).toString();

            String jwt = generateJWT();

            try {
                url = new URL(urls[0]);
                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setRequestProperty("Content-Type", "application/json");
                httpURLConnection.setRequestProperty("Authorization", "Bearer " + jwt);
                httpURLConnection.connect();

                outputStreamWriter = new OutputStreamWriter(httpURLConnection.getOutputStream());
                outputStreamWriter.write(jsonLocation);
                outputStreamWriter.flush();
                outputStreamWriter.close();

                input = httpURLConnection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                StringBuilder result = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }
                Log.i("RESULT: ", result.toString());
                return result.toString();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                httpURLConnection.disconnect();
            }

            return null;
        }
    }

}
