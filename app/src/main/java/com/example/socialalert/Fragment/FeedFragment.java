package com.example.socialalert.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.socialalert.Adapter.*;
import com.example.socialalert.AlertData;
import com.example.socialalert.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FeedFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{

    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private CurrentAdapter mAdapter;
    private DatabaseReference mDatabase;
    final List<AlertData> dataArray = new ArrayList<>();

    public FeedFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_feed, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        recyclerView = (RecyclerView)getActivity().findViewById(R.id.recyclerViewContainer);
        swipeRefreshLayout=(SwipeRefreshLayout)getActivity().findViewById(R.id.swipe2);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setRefreshing(true);
        mDatabase = FirebaseDatabase.getInstance().getReference("Record");
        mAdapter = new CurrentAdapter(dataArray);
        recyclerView.setAdapter(mAdapter);
        onRefresh();
    }

    @Override
    public void onRefresh() {
        dataArray.clear();
        getData();
    }

    private void getData() {

        mDatabase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                fetchData(dataSnapshot);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    public void fetchData(DataSnapshot dataSnapshot){

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
        mAdapter = new CurrentAdapter(dataArray);
        recyclerView.setAdapter(mAdapter);
        swipeRefreshLayout.setRefreshing(false);
    }



    @Override
    public void onResume() {
        super.onResume();
        onRefresh();
    }
}
