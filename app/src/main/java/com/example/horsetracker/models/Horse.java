package com.example.horsetracker.models;

import android.text.Editable;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class Horse {
    public String uid;
    public String name = null;
    public int height = 0;
    public Boolean wormed;
    public int fieldPosition = 0;
    public String ownerName = null;
    public int ownerNumber = 0;

    public Horse(){
        // Empty Constructor
    }


    public Horse(String userID, String name, int height) {
        this.name = name;
        this.height = height;

    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("uid", uid);
        result.put("name", name);
        result.put("height", height);
        result.put("wormed", wormed);
        result.put("field", fieldPosition);
        result.put("owner name", ownerName);
        result.put("owner number", ownerNumber);

        return result;
    }
}
