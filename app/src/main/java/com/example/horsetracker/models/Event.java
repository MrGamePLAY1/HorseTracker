package com.example.horsetracker.models;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
public class Event
{

    public String uid;
    public String title;
    public String location;
    public String date;
    public String time;
    public int starCount = 0;
    public Map<String, Boolean> stars = new HashMap<>();
    public int author;

    public Event()
    {
        //empty constructor
    }

    public Event(String uid, String title, String location, String date, String time) {

        this.uid = uid;
        this.title = title;
        this.location = location;
        this.date = date;
        this.time = time;
    }

    public Event(String userId, String username, String title, String location, String date, String time) {
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("uid", uid);
        result.put("title", title);
        result.put("location", location);
        result.put("date", date);
        result.put("time", time);
        result.put("starCount", starCount);
        result.put("stars", stars);

        return result;
    }
}
