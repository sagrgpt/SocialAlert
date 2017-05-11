package com.example.socialalert;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import es.dmoral.toasty.Toasty;

public class SplashScreenActivity extends AppCompatActivity {

    private static final int LOCATION_ACCESS_PERMISSION = 1;
    private static final int SEND_SMS_PERMISSION = 2;
    private static final int SPLASH_DISPLAY_LENGTH = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                getPermissions();
            Log.d("Working", "This function is activated");
            }
        }, SPLASH_DISPLAY_LENGTH);

    }

    private void getPermissions() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.d("location", "Getting user permission for fine location");
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_ACCESS_PERMISSION);
        }
        else if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED){
            Log.d("location", "Getting user permission to send message");
            ActivityCompat.requestPermissions(this,new String[] {Manifest.permission.SEND_SMS},SEND_SMS_PERMISSION);
        }
        else{
            startActivity(new Intent(SplashScreenActivity.this,AlertActivity.class));
            finish();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case LOCATION_ACCESS_PERMISSION: {
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    getPermissions();
                }
                else{
                    Toasty.error(getApplicationContext(),"Permission not granted!").show();
                }
                break;
            }
            case SEND_SMS_PERMISSION: {
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    getPermissions();
                }
                else{
                    Toasty.error(getApplicationContext(),"Permission not granted!").show();
                }
                break;
            }
        }
    }
}
