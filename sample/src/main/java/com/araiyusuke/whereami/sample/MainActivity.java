package com.araiyusuke.whereami.sample;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.araiyusuke.whereami.Failed;
import com.araiyusuke.whereami.OnLocationUpdatedListener;
import com.araiyusuke.whereami.ResponseResult;
import com.araiyusuke.whereami.Success;
import com.araiyusuke.whereami.WhereAmI;

public class MainActivity extends AppCompatActivity {

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case WhereAmI.REQUEST_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    WhereAmI.with(this).start(new OnLocationUpdatedListener() {
                        @Override
                        public void onResponse(ResponseResult response) {
                            switch (response.getType()) {
                                case failed:
                                    Failed failed = (Failed) response;
                                    break;
                                case success:
                                    Success result = (Success) response;
                                    showToast(result.location);
                                    break;
                                case unauthorized:
                                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{
                                            Manifest.permission.ACCESS_FINE_LOCATION
                                    }, WhereAmI.REQUEST_CODE);

                                    break;
                                default:
                            }
                        }
                    });
                } else {
                    // パーミッションが得られなかった時
                    // 処理を中断する・エラーメッセージを出す・アプリケーションを終了する等
                }
            }
        }
    }

    private void showToast(Location location) {
        String message = location.getLatitude() + "," + location.getLongitude();
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        WhereAmI.repeat = true;
        WhereAmI.with(this).start(new OnLocationUpdatedListener() {
            @Override
            public void onResponse(ResponseResult response) {
                switch (response.getType()) {
                    case failed:
                        Failed failed = (Failed) response;
                        break;
                    case success:
                        Success result = (Success) response;
                        showToast(result.location);
                        break;
                    case unauthorized:
                        WhereAmI.checkPermission(MainActivity.this);
                        break;
                    default:
                }
            }
        });
    }
}