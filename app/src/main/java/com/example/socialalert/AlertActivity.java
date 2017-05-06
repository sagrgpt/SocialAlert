package com.example.socialalert;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import es.dmoral.toasty.Toasty;

import static java.text.DateFormat.getDateInstance;
import static java.text.DateFormat.getTimeInstance;

public class AlertActivity extends AppCompatActivity {


    TextView settings;
    Button alert_btn;
    SharedPreferences sharedPreferences;
    Boolean alertACTIVE = false;
    ViewGroup alertLayout;

    LocationManager manager;
    LocationListener locationListener;

    DatabaseReference databaseReference;
    String key;
    AlertData changeData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alert);

        settings = (TextView) findViewById(R.id.settings);
        alert_btn = (Button) findViewById(R.id.alert_btn);
        alertLayout = (ViewGroup) findViewById(R.id.alertLayout);


        //Call settings on preference not set
        sharedPreferences = getSharedPreferences("settings", Context.MODE_PRIVATE);
        if (!sharedPreferences.getBoolean("IsSet", false)) {


            settings.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(AlertActivity.this, SettingActivity.class));

                }
            });
        }
        //if settings done make text click gone
        else settings.setVisibility(View.GONE);

    }

    public void onNetworkClicked(View view) {
        startActivity(new Intent(AlertActivity.this,NetworkActivity.class));
        finish();
    }

    public void onAlert(View view) {
        if (!sharedPreferences.getBoolean("IsSet", false)) {
            return;
        }
        if(!alertACTIVE) {
            Toasty.info(getApplicationContext(),"Getting location...",Toast.LENGTH_SHORT).show();
            alertACTIVE = true;
            alert_btn.setText(R.string.btn_text_cancel);
            getLocation();
        }
        else{
            copyData();
        }
    }

    private void getLocation() {
        manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();
        }
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location loc) {
                Log.d("Location",String.valueOf(loc.getLongitude()));
                Log.d("Location",String.valueOf(loc.getLatitude()));
                String lat = String.valueOf(loc.getLatitude());
                String lng = String.valueOf(loc.getLongitude());
                Toasty.custom(getApplicationContext(),"Location Received!",R.drawable.ic_location, Color.TRANSPARENT,Toast.LENGTH_SHORT,true,true).show();
                createNews(lat,lng);
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
        manager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);

    }

    private void createNews(String lat, String lng) {
        manager.removeUpdates(locationListener);

        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = (SimpleDateFormat) getTimeInstance();
        String time = sdf.format(c.getTime());
        sdf = (SimpleDateFormat) getDateInstance();
        String date = sdf.format(new Date());

        databaseReference = FirebaseDatabase.getInstance().getReference("Current");
        AlertData data = new AlertData(sharedPreferences.getString("Name",null),
                sharedPreferences.getString("Message",null),
                lat,
                lng,
                time,
                date);
        key = databaseReference.push().getKey();
        databaseReference.child(key).setValue(data);
        Toasty.success(getApplicationContext(),"People have been informed! Stay Strong.",Toast.LENGTH_LONG).show();
    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    private void copyData() {
        databaseReference = FirebaseDatabase.getInstance().getReference("Current").child(key);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue()!=null) {
                    changeData = new AlertData();
                    Map<String, String> map = (Map) dataSnapshot.getValue();
                    changeData.username = map.get("username");
                    changeData.time = map.get("time");
                    changeData.date = map.get("date");
                    changeData.message = map.get("message");
                    changeData.latitude = map.get("latitude");
                    changeData.longitude = map.get("longitude");
                    moveData(changeData);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void moveData(AlertData data) {
        databaseReference = FirebaseDatabase.getInstance().getReference("Current").child(key);
        DatabaseReference dr2 = FirebaseDatabase.getInstance().getReference("Record").child(key);
        dr2.setValue(data);
        databaseReference.removeValue();
        alertACTIVE = false;
        Toasty.info(getApplicationContext(),"Alert removed. Hope you are Safe!",Toast.LENGTH_SHORT).show();
        alert_btn.setText(R.string.btn_text_alert);
    }

    @Override
    protected void onStop() {
        super.onStop();
//        if(alertACTIVE) {
//            copyData();
//        }
    }

}
