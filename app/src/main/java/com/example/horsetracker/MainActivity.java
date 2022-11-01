package com.example.horsetracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    //Logs
    private static final String TAG = "MainActivity";

    //Database shit
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseFirestore firestore;

    //Inputs
    private EditText mEmail,mPassword;
    private Button btnSignIn, btnReg;



    // Validation Steps
    private Boolean validationChecker()
    {
        //Getting email / pass to string
        String email = mEmail.getText().toString();
        String pass = mPassword.getText().toString();
        String emailPrefix = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        // Checking if Empty (email)
        if (email.isEmpty())
        {
            mEmail.setError("You need to enter a valid Email");
            return false;
        }

        // Checking if Empty (pass)
        if (pass.isEmpty())
        {
            mPassword.setError("You need to enter a valid Password");
            return false;
        }

        // Checking if valid (email)
        if (!email.matches(emailPrefix))
        {
            mEmail.setError("Invalid Email!");
            return false;
        }




        // Else Continue
        else
        {
            mEmail.setError(null);
            mPassword.setError(null);
            return true;
        }
    }

    @Override
    public void onStart(){
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }


    @Override
    public void onStop(){
        super.onStop();
        if(mAuthListener != null){
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Hide Action bar
        Objects.requireNonNull(getSupportActionBar()).hide();


        // Finding objects
        mEmail = (EditText) findViewById(R.id.email);
        mPassword = (EditText) findViewById(R.id.password);
        btnSignIn = (Button) findViewById(R.id.signInButton);
        btnReg = (Button) findViewById(R.id.buttonRegister);

        //Initialize Firebase Database
        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();

                if(user != null){
                    //user is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                    toastMessage("Successfully Signed In with: " + user.getEmail());
                }else{
                    //user is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                    toastMessage("Successfully Signed Out");
                }
            }
        };

        //If logged in send to main
        if (mAuth.getCurrentUser() != null)
        {
            openMenuActivity();
        }

        btnReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openRegisterActivity();
            }
        });

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = mEmail.getText().toString();
                String pass = mPassword.getText().toString();

                //All validation checks
                validationChecker();




                if (!email.equals("") && !pass.equals("")){
                    mAuth.signInWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if(task.isSuccessful()){
                                openMenuActivity();
                            }
                            else{
                                toastMessage("Login Failed");
                            }
                        }
                    });
                }else{
                    toastMessage("Required Fields not filled");
                }

            }
        });

    }






    //add a toast to show when successfully signed in
    /**
     * customizable toast
     * @param message
     */
    private void toastMessage(String message){
        Toast.makeText(MainActivity.this,message,Toast.LENGTH_SHORT).show();
    }


    public void openMenuActivity(){
        Intent intent = new Intent(MainActivity.this, MainActivity.class);
        startActivity(intent);
    }

    public void openRegisterActivity(){
        Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
        startActivity(intent);
    }

}