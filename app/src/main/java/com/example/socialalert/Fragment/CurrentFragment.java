package com.example.socialalert.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.socialalert.AlertData;
import com.example.socialalert.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import es.dmoral.toasty.Toasty;

public class CurrentFragment extends Fragment implements OnMapReadyCallback {

    public CurrentFragment() {
        // Required empty public constructor
    }

    //    private SwipeRefreshLayout swipeRefreshLayout;
    MapView mapView;
    final List<AlertData> dataArray = new ArrayList<>();
    private List<Marker> markers = new ArrayList<>();
    GoogleMap map;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_current, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//        swipeRefreshLayout=(SwipeRefreshLayout)getActivity().findViewById(R.id.swipe);
//        swipeRefreshLayout.setOnRefreshListener(this);
//        swipeRefreshLayout.setRefreshing(true);
        mapView = (MapView) getActivity().findViewById(R.id.mapCurrent);
        mapView.onCreate(savedInstanceState);
        mapView.onResume();
        try {
            MapsInitializer.initialize(getContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
        mapView.getMapAsync(this);

    }

    public void onRefresh() {
        getData();
    }

    private void getData() {

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("Current");

        mDatabase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                fetchData(dataSnapshot);

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
//                onRefresh();
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                removeData(dataSnapshot);
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
//                onRefresh();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toasty.error(getContext(), databaseError.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void fetchData(DataSnapshot dataSnapshot) {
        if (dataSnapshot.getValue() != null) {
            Log.d("Datalog", "onChildAdded:" + dataSnapshot.getKey());
            AlertData info = new AlertData();
            Map<String, String> map = (Map) dataSnapshot.getValue();
            info.username = map.get("username");
            info.time = map.get("time");
            info.date = map.get("date");
            info.message = map.get("message");
            info.latitude = map.get("latitude");
            info.longitude = map.get("longitude");
            dataArray.add(info);
            addMarker(info);

        }
    }

    public void removeData(DataSnapshot dataSnapshot){
        if(dataSnapshot.getValue()!=null) {
            Log.d("Datalog", "onChildAdded:" + dataSnapshot.getKey());
            AlertData info = new AlertData();
            Map<String, String> map = (Map) dataSnapshot.getValue();
//            info.username = map.get("username");
//            info.time = map.get("time");
//            info.date = map.get("date");
//            info.message = map.get("message");
            info.latitude = map.get("latitude");
            info.longitude = map.get("longitude");
            for(AlertData x: dataArray){
                if(x.latitude.equals(info.latitude)&& x.longitude.equals(info.longitude)){
                    onDataRemoved(x);
                }
            }
//            swipeRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        onRefresh();
//        Toasty.info(getContext(), "Map Ready").show();
        map = googleMap;
        LatLng india = new LatLng(28.661898, 77.227396);
        map.moveCamera(CameraUpdateFactory.newLatLng(india));


    }

    public void onDataRemoved(AlertData x){
        LatLng alert = new LatLng(Double.parseDouble(x.latitude),Double.parseDouble(x.longitude));
        for (Marker m : markers){
            if(m.getPosition().equals(alert)){
                m.remove();
                markers.remove(m);
            }
        }

    }


    public void addMarker(AlertData data) {
        double lat = Double.parseDouble(data.latitude);
        double lng = Double.parseDouble(data.longitude);
        LatLng alert = new LatLng(lat, lng);
        Marker markerName = map.addMarker(new MarkerOptions().position(alert)
                .title("Alert"));
        markers.add(markerName);
        map.setMyLocationEnabled(true);
        map.getUiSettings().setMapToolbarEnabled(true);
//        swipeRefreshLayout.setRefreshing(false);
    }

}
