package com.example.socialalert.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.socialalert.AlertData;
import com.example.socialalert.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Collections;
import java.util.List;


public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.MyViewHolderClass>{

    List<AlertData> dataArray = Collections.emptyList();
    Context context;

    public FeedAdapter(List<AlertData> dataArray) {
        this.dataArray = dataArray;
    }

    public static class MyViewHolderClass extends RecyclerView.ViewHolder{

        private MapView mapView;
        private TextView message, time;

        public MyViewHolderClass(View view) {
            super(view);
            mapView = (MapView) view.findViewById(R.id.map);
            message = (TextView) view.findViewById(R.id.userMessage);
            time = (TextView) view.findViewById(R.id.alertTime);
        }
    }




    @Override
    public FeedAdapter.MyViewHolderClass onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.current_row_layout,parent,false);
        FeedAdapter.MyViewHolderClass holder = new FeedAdapter.MyViewHolderClass(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolderClass holder, int position) {
        AlertData data = dataArray.get(position);
        final double lat = Double.parseDouble(data.latitude);
        final double lng = Double.parseDouble(data.longitude);
        holder.message.setText(data.message);
        holder.time.setText(data.time);
        //Initializing maps
        holder.mapView.onCreate(Bundle.EMPTY);
        holder.mapView.onResume();
        try {
            MapsInitializer.initialize(context);
        } catch (Exception e) {
            e.printStackTrace();
        }
        holder.mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                LatLng location = new LatLng(lat, lng);
                googleMap.addMarker(new MarkerOptions().position(location)
                        .title("Alert Location"));
                googleMap.moveCamera(CameraUpdateFactory.newLatLng(location));
            }
        });


    }


    @Override
    public int getItemCount() {
        return dataArray.size();
    }


}

