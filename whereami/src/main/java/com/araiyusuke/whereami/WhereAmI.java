package com.araiyusuke.whereami;

/**
 * Created by araiyuusuke on 2018/02/01.
 */

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.os.Looper;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

/**
 * Created by araiyuusuke on 2018/01/27.
 */

public class WhereAmI implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private GoogleApiClient client;
    public static final int REQUEST_CODE = 1234;
    public static Boolean repeat = false;

    public OnLocationUpdatedListener listener;
    public Context context;

    private WhereAmI(Context context) {
        this.context = context;
    }

    public static WhereAmI with(Context context) {
        return new Builder(context).build();
    }

    @Override
    public void onConnectionSuspended(int i) {}
    @Override
    public void onProviderDisabled(String provider) {}

    @Override
    public void onLocationChanged(Location location) {}

    @Override
    public void onProviderEnabled(String provider) {}

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {}

    public static void checkPermission(Activity activity) {
        ActivityCompat.requestPermissions(activity, new String[]{
                Manifest.permission.ACCESS_FINE_LOCATION
        }, WhereAmI.REQUEST_CODE);
    }

    public void start(OnLocationUpdatedListener listener) {
        this.listener = listener;
        if (ContextCompat.checkSelfPermission(this.context,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Unauthorized result = new Unauthorized();
            this.listener.onResponse(result);
            return;
        } else {
            this.client = new GoogleApiClient.Builder(context)
                    .addApi(LocationServices.API)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .build();

            client.connect();
        }
    }

    @Override
    public void onConnected(Bundle bundle) {

        FusedLocationProviderClient fusedLocationClient =
                LocationServices.getFusedLocationProviderClient(this.context);

        LocationRequest locationRequest = new LocationRequest();

        int priority = 1;

        if (priority == 0) {
            // 高い精度の位置情報を取得したい場合
            // インターバルを例えば5000msecに設定すれば
            // マップアプリのようなリアルタイム測位となる
            // 主に精度重視のためGPSが優先的に使われる
            locationRequest.setPriority(
                    LocationRequest.PRIORITY_HIGH_ACCURACY);

        } else if (priority == 1) {
            // バッテリー消費を抑えたい場合、精度は100mと悪くなる
            // 主にwifi,電話網での位置情報が主となる
            // この設定の例としては　setInterval(1時間)、setFastestInterval(1分)
            locationRequest.setPriority(
                    LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        } else if (priority == 2) {
            // バッテリー消費を抑えたい場合、精度は10kmと悪くなる
            locationRequest.setPriority(
                    LocationRequest.PRIORITY_LOW_POWER);

        } else {
            // 受け身的な位置情報取得でアプリが自ら測位せず、
            // 他のアプリで得られた位置情報は入手できる
            locationRequest.setPriority(
                    LocationRequest.PRIORITY_NO_POWER);
        }

        LocationCallback locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                Location location = locationResult.getLastLocation();
                Success result = new Success();
                result.location = location;
                listener.onResponse(result);
            }
        };

        fusedLocationClient.requestLocationUpdates(
                locationRequest, locationCallback, Looper.myLooper());

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d("デバッグ", "onConnectionFailed");
    }

    public static class Builder {
        private final Context context;

        public Builder(Context context) {
            this.context = context;
        }

        public WhereAmI build() {
            return new WhereAmI(context);
        }
    }
}
