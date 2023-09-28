package com.example.myapplication.sign;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.MainActivity;
import com.example.myapplication.R;
import com.example.myapplication.registration;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


public class login extends AppCompatActivity {

    Button signup;
    Button signIn;
    EditText number, password;
    ProgressBar progressBar;
    private boolean passwordShowing = false;
    private static final String SHARED_PREFS = "your_shared_prefs_name";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        signup = findViewById(R.id.signup);
        signIn = findViewById(R.id.signIn);
        number = findViewById(R.id.logNumber);
        password = findViewById(R.id.passwordET);
        final ImageView passwordIcon = findViewById(R.id.passwordIcon);
        progressBar = findViewById(R.id.progressBar);


        passwordIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (passwordShowing) {
                    passwordShowing = false;

                    password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    passwordIcon.setImageResource(R.drawable.view);
                } else {
                    passwordShowing = true;
                    password.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    passwordIcon.setImageResource(R.drawable.hide);
                }
                password.setSelection(password.length());
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(login.this, registration.class));
            }
        });
    }

    private boolean validatePhone() {
        String val = number.getText().toString();

        if (val.isEmpty()) {
            number.setError("Field cannot be empty");
            return false;
        } else {
            number.setError(null);
            return true;
        }
    }

    private boolean validatePassword() {
        String val = password.getText().toString();
        String passwordVal = "0-9";

        if (val.isEmpty()) {
            password.setError("Field cannot be empty");
            return false;
        } else if (val.matches(passwordVal)) {
            password.setError("Password should Number");
            return false;
        } else {
            password.setError(null);
            return true;
        }
    }

    public void loginUser(View view) {
        //Validate Login Info
        if (!validatePhone() | !validatePassword()) {
            return;
        } else {
            isUser();
        }
    }

    public void isUser() {
        progressBar.setVisibility(View.VISIBLE);

        final String userEnteredUsername = number.getText().toString().trim();
        final String userEnteredPassword = password.getText().toString().trim();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
        Query checkUser = reference.orderByChild("phoneNo").equalTo(userEnteredUsername);

        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    String passwordFromDB = dataSnapshot.child(userEnteredUsername).child("password").getValue(String.class);


                    if (passwordFromDB.equals(userEnteredPassword)) {
                        progressBar.setVisibility(View.VISIBLE);

                        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("phoneNo", userEnteredUsername); // Store the phone number
                        editor.apply();


                        String nameFromDB = dataSnapshot.child(userEnteredUsername).child("name").getValue(String.class);
                        String usernameFromDB = dataSnapshot.child(userEnteredUsername).child("username").getValue(String.class);
                        String phoneNoFromDB = dataSnapshot.child(userEnteredUsername).child("phoneNo").getValue(String.class);
                        String emailFromDB = dataSnapshot.child(userEnteredUsername).child("email").getValue(String.class);
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);

                        intent.putExtra("name", nameFromDB);
                        intent.putExtra("username", usernameFromDB);
                        intent.putExtra("email", emailFromDB);
                        intent.putExtra("phoneNo", phoneNoFromDB);

                        startActivity(intent);
                        finish();

                    } else {
                        password.setError("Wrong Password");
                        password.requestFocus();
                    }
                } else {
                    number.setError("No such User exist");
                    number.requestFocus();
                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}