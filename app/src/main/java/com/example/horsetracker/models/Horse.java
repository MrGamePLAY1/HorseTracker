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


    public Horse(String userID, String name, int height, String ownerName, int ownerNumber) {
        this.name = name;
        this.height = height;
        this.ownerName = ownerName;
        this.ownerNumber = ownerNumber;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("uid", uid);
        result.put("name", name); // String
        result.put("height", height); // Int
        result.put("wormed", wormed); // Boolean
        result.put("field", fieldPosition); // Int
        result.put("owner name", ownerName); // String
        result.put("owner number", ownerNumber); // String

        return result;
    }
}
