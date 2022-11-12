package com.example.horsetracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.NoCopySpan;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class MenuActivity extends AppCompatActivity {


    private static final String TAG = "MenuActivity";

    //Menu Items
    private Button btnSignOut, viewHorse, viewEvents, viewNote, calendar;

    // Login cred
    public String email;

    // Database stuff
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;




    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        // Firebase Initzialisation
        mAuth = FirebaseAuth.getInstance();

        // Firebase listener
        mAuthListener = firebaseAuth -> {
            // Firebase user logged
            FirebaseUser user = firebaseAuth.getCurrentUser();

            // Check if user is signed in or not
            if (user != null)
            {
                // Logged in
                Log.d(TAG, "User is now logged in as: " + user.getUid());
                Toast.makeText(MenuActivity.this, "User is now logged in as " + user.getEmail(), Toast.LENGTH_SHORT).show();
            }

            else
            {
                // Logged in
                Log.d(TAG, "User is now logged out ");
                Toast.makeText(MenuActivity.this, "Signed Out", Toast.LENGTH_SHORT).show();
            }
        };

        // Hide Action bar
        Objects.requireNonNull(getSupportActionBar()).hide();

        // Finding IDs
        btnSignOut = (Button) findViewById(R.id.signOutButton);
        viewHorse = (Button) findViewById(R.id.horses);



        // Menu Button Onclicks()
        btnSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                signOut();
            }
        });

        // View Horses OnClick()
        viewHorse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Check if Signed in
                viewHorses();
            }
        });


    }

    // Sign out function for SIGN OUT BUTTON
    public void signOut(){
        Intent intent = new Intent(MenuActivity.this, MainActivity.class);
        startActivity(intent);
    }

    public void viewHorses(){
        Intent intent = new Intent(MenuActivity.this, HorseActivity.class);
        startActivity(intent);
    }
}
