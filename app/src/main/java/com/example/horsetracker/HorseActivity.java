package com.example.horsetracker;

import static com.google.api.FieldBehavior.REQUIRED;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.horsetracker.models.Horse;
import com.example.horsetracker.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.example.horsetracker.databinding.ActivityHorseBinding;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class HorseActivity extends AppCompatActivity {

    // Log
    private static final String TAG = "HorseActivity";
    private static final String REQUIRED = "Required";

    // Log Clients UID
    public String getUID()
    {
        return Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
    }

    // Variables
    private Button addHorse, removeHorse;
    private EditText horseName, horseHeight;
    private RadioButton f1, f2, f3;

    // DB
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference myDatabase;

    // New Horse Binding
    private ActivityHorseBinding bind;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_horse);

        // Firebasae
        mAuth = FirebaseAuth.getInstance();

        // Horse bind
        bind = ActivityHorseBinding.inflate(getLayoutInflater());
        setContentView(bind.getRoot());

        // Firebase listener
        mAuthListener = firebaseAuth -> {
            // Firebase user logged
            FirebaseUser user = firebaseAuth.getCurrentUser();

            // Check if user is signed in or not
            if (user != null)
            {
                // Logged in
                Log.d(TAG, "User is now logged in as: " + user.getUid());
                // Toast.makeText(HorseActivity.this, "User is now logged in as " + user.getEmail(), Toast.LENGTH_SHORT).show();
            }

            else
            {
                // Logged in
                Log.d(TAG, "User is now logged out ");
                // Toast.makeText(HorseActivity.this, "Signed Out", Toast.LENGTH_SHORT).show();
            }

            // Hide Action bar
            Objects.requireNonNull(getSupportActionBar()).hide();

            // Finding the IDs
            addHorse = (Button) findViewById(R.id.addHorse);
            removeHorse = (Button) findViewById(R.id.deleteHorse);
            horseName = (EditText) findViewById(R.id.horseName);
            horseHeight = (EditText) findViewById(R.id.horseHeight);
            f1 = (RadioButton) findViewById(R.id.field1);
            f2 = (RadioButton) findViewById(R.id.field2);
            f3 = (RadioButton) findViewById(R.id.field3);

            // Making the Database
            // Getting the UID
            final String uid = getUID();
            Log.d(TAG, "Clients UID: " + getUID());

            // Database work
            myDatabase = FirebaseDatabase.getInstance().getReference();

            // Listeners
            bind.addHorse.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    submitHorse();
                }
            });

        };
    }

    public void submitHorse()
    {
        // Add Shit From Form Code
        final String name = bind.horseName.getText().toString();
        final String height = bind.horseHeight.getText().toString();

        // required information
        if (TextUtils.isEmpty(name))
        {
            bind.horseName.setError(REQUIRED);
            return;
        }

        if (TextUtils.isEmpty(height))
        {
            bind.horseHeight.setError(REQUIRED);
            return;
        }

        // Submitting message
        Toast.makeText(this, "Uploading", Toast.LENGTH_SHORT).show();

        // Database Work
        final String userID = getUID();
        myDatabase.child("horses").child(userID).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        // Get Users Values
                        User user = snapshot.getValue(User.class);

                        if (user == null)
                        {
                            // Error is user is null
                            Log.d(TAG, "USER " + userID + " should not be null");
                            Toast.makeText(HorseActivity.this, "Error: Could not find horse", Toast.LENGTH_SHORT).show();
                        }

                        else
                        {
                            // Make new Horse
                            addToDatabase(userID, name, height);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                }
        );
    }

    private void addToDatabase(String userID, String name, String height) {
        // code to add to database
        String key = myDatabase.child("horses").push().getKey();
        Horse horse = new Horse(userID, name, height);

        // Map Entity
        Map<String, Object> horseValues = horse.toMap();

        // Map Child
        Map<String, Object> childUpdate = new HashMap<>();
        childUpdate.put(userID + "/horse/" + "/" + key, horseValues);

        // Update
        myDatabase.updateChildren(childUpdate);
    }

    public void removeHorseFromDatabase()
    {
        // To Do Code
    }

    public void addToDatabase(String userID, String hName, double height,  Boolean isWormed, int fieldPos, String ownerName, int ownNum)
    {
        // code to add to database
        String key = myDatabase.child("horses").push().getKey();
        Horse horse = new Horse(userID, hName, height, isWormed, fieldPos, ownerName, ownNum);

        // Map Entity
        Map<String, Object> horseValues = horse.toMap();

        // Map Child
        Map<String, Object> childUpdate = new HashMap<>();
        childUpdate.put(userID + "/horse/" + "/" + key, horseValues);

        // Update
        myDatabase.updateChildren(childUpdate);

    }
}