package com.example.socialalert;

/**
 * Created by sagar on 30/4/17.
 */

public class AlertData {

    public String username, message, latitude, longitude, time, date;

    public AlertData(String username, String message, String latitude, String longitude, String time, String date) {
        this.username = username;
        this.message = message;
        this.latitude = latitude;
        this.longitude = longitude;
        this.time = time;
        this.date = date;
    }

    public AlertData() {
    }
}
