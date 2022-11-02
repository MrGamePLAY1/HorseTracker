package com.example.horsetracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.horsetracker.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {
    //Message Tag
    private static final String TAG = "RegisterActivity";

    //buttons, textfields, passwords
    Button regButton, regLogin;
    EditText regEmail, regPass;

    //Database
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;

    private Boolean validEmail()
    {
        String email = regEmail.getText().toString();
        String emailPrefix = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if (!email.matches(emailPrefix))
        {
            regEmail.setError("Invalid Email!");
            return false;
        }

        else
        {
            regEmail.setError(null);
            return false;
        }
    }

    private Boolean seriousPassValid()
    {
        String pass = regPass.getText().toString();
        String passwordValid = "^" +
                //"(?=.*[0-9])" +               //at least 1 digit
                //"(?=.*[a-z])" +               //at least 1 lower case char
                //"(?=.*[A-Z])" +               //at least 1 upper case char
                "(?=.*[a-zA-Z])" +              //any letter
                "(?=.*[@#$%^&!.+=])" +            //at least 1 special char
                "(?=\\S+$)" +                   //no spaces
                ".{4,}" +                       //at least 4 char
                "$";

        if (!pass.matches(passwordValid))
        {
            regPass.setError("Password is weak!");
            toastMessage("Please use: \n " +
                    "1 special character \n" +
                    "no spaces \n" +
                    "at least 4 characters");
            return false;
        }

        else
        {
            regPass.setError(null);
            return  true;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        //gets rid of the action bar at the top
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            Window w = getWindow();
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }

        setContentView(R.layout.activity_register);

        getSupportActionBar().hide();

        //Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        //If logged in send to main
        if (mAuth.getCurrentUser() != null)
        {
            openMenuActivity();
        }

        //setting views
        regEmail = findViewById(R.id.regEmail);
        regPass = findViewById(R.id.regPass);
        regButton = findViewById(R.id.regButton);
        regLogin = findViewById(R.id.loginButton);


        //On click for register button
        regButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = regEmail.getText().toString().trim();
                String pass = regPass.getText().toString().trim();

                validEmail();
                seriousPassValid();


                if (email.isEmpty())
                {
                    toastMessage("Please place your email!");
                }

                if (pass.isEmpty())
                {
                    toastMessage("Please type a password!");
                }

                //Sending off to the database
                mAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful())
                        {
                            onAuthSuccess(task.getResult().getUser());
                            toastMessage("Registration Completed!");
                            openMainActivity();
                        }

                        else
                        {
                            Log.d(TAG, "BIG ERROR : " + task);
                        }
                    }
                });
            }
        });



        //Sending new Activity to Login
        regLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               openMainActivity();
            }
        });
    }

    private void onAuthSuccess(FirebaseUser user) {
        String username = usernameFromEmail(user.getEmail());
        writeNewUser(user.getUid(), username, user.getEmail());

    }


    private String usernameFromEmail(String email) {
        if (email.contains("@")) {
            return email.split("@")[0];
        } else {
            return email;
        }
    }

    private void writeNewUser(String userId, String name, String email) {
        User user = new User(name, email);

        mDatabase.child("users").child(userId).setValue(user);
    }

    private void toastMessage(String message){
        Toast.makeText(RegisterActivity.this,message,Toast.LENGTH_SHORT).show();
    }

    public void openMenuActivity(){
        //FIX
        Intent intent = new Intent(RegisterActivity.this, MenuActivity.class);
        startActivity(intent);
    }

    public void openMainActivity(){
        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
