package com.example.horsetracker.models;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

public class Horse {
    public String uid;
    public String name;
    public double height;
    public Boolean wormed;
    public int fieldPosition;
    public String ownerName;
    public int ownerNumber;

    public Horse(){
        // Empty Constructor
    }

    public Horse(String uid, String name, double height, Boolean wormed, int fieldPosition, String ownerName, int ownerNumber)
    {
        this.uid = uid;
        this.name = name;
        this.height = height;
        this.wormed = wormed;
        this.fieldPosition = fieldPosition;
        this.ownerName = ownerName;
        this.ownerNumber = ownerNumber;
    }

    public Horse(String userID, String horseName, Boolean isWormed, int fieldPos, String ownerID)
    {

    }

    public Horse(String userID, String name, String height) {

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
