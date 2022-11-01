package com.example.horsetracker.models;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
public class Note
{

    public String uid;
    public String title;
    public String description;
    public int starCount = 0;
    public Map<String, Boolean> stars = new HashMap<>();
    public int author;

    public Note()
    {
        //empty constructor
    }

    public Note(String uid, String title, String description) {

        this.uid = uid;
        this.title = title;
        this.description = description;

    }

    public Note(String userId, String username, String title, String description) {
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("uid", uid);
        result.put("title", title);
        result.put("description", description);
        result.put("starCount", starCount);
        result.put("stars", stars);

        return result;
    }
}
