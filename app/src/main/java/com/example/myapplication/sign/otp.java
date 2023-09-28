package com.example.myapplication.sign;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.TimeUnit;

public class otp extends AppCompatActivity {

    private FirebaseAuth mAuth;
    String verificationCodeBySystem,profileImageUrl;
    Button verify_btn;
    EditText phoneNoEnteredByTheUser;
    ProgressBar progressBar;
    TextView sendBtn;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://money-bag-1857e-default-rtdb.firebaseio.com/");
    public String phoneNo,Name,UserName,Email,Password;
    private int resendTime = 60;
    private int selectedPosition = 0;
    private boolean resendEnabled = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);

        mAuth = FirebaseAuth.getInstance();
        verify_btn = findViewById(R.id.verifyBtn);
        phoneNoEnteredByTheUser = findViewById(R.id.OtpNumber);
        progressBar = findViewById(R.id.progress_bar);
        sendBtn = findViewById(R.id.sendBtn);


        phoneNo = getIntent().getStringExtra("phoneNo");
        Name = getIntent().getStringExtra("name");
        UserName = getIntent().getStringExtra("userName");
        Email = getIntent().getStringExtra("email");
        Password = getIntent().getStringExtra("password");

        startCountDownTime();
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (resendEnabled) {


                    startCountDownTime();
                }
            }
        });
        sendVerificationToUser(phoneNo);

        verify_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String code = phoneNoEnteredByTheUser.getText().toString();
                if (code.isEmpty() || code.length() < 6) {
                    phoneNoEnteredByTheUser.setError("Wrong OTP...");
                    phoneNoEnteredByTheUser.requestFocus();
                    return;
                }
                progressBar.setVisibility(View.VISIBLE);
                verifyCode(code);
            }
        });
    }

    private void sendVerificationToUser(String phoneNo) {
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber("+880" + phoneNo)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
        // [END start_phone_auth]
    }

    private final PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks =
            new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                @Override
                public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                    super.onCodeSent(s, forceResendingToken);

                    verificationCodeBySystem = s;
                }

                @Override
                public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

                    String code = phoneAuthCredential.getSmsCode();
                    if (code != null) {
                        progressBar.setVisibility(View.VISIBLE);
                        verifyCode(code);
                    }
                }

                @Override
                public void onVerificationFailed(@NonNull FirebaseException e) {
                    Toast.makeText(otp.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }

            };

    private void verifyCode(String codeByUser) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationCodeBySystem, codeByUser);
        signInTheUserByCredentials(credential);
    }

    private void signInTheUserByCredentials(PhoneAuthCredential credential) {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(otp.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            //Perform Your required action here to either let the user sign In or do something required
                            Intent intent = new Intent(getApplicationContext(), login.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                            databaseReference.child("users").child(phoneNo).child("name").setValue(Name);
                            databaseReference.child("users").child(phoneNo).child("username").setValue(UserName);
                            databaseReference.child("users").child(phoneNo).child("email").setValue(Email);
                            databaseReference.child("users").child(phoneNo).child("phoneNo").setValue(phoneNo);
                            databaseReference.child("users").child(phoneNo).child("password").setValue(Password);
                            databaseReference.child("users").child(phoneNo).child("profileImageUrl").setValue(profileImageUrl);
                            startActivity(intent);
                        } else {
                            Toast.makeText(otp.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        //TextView ResendLabel = findViewById(R.id.sendBtn);

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PhoneAuthOptions options =
                        PhoneAuthOptions.newBuilder(mAuth)
                                .setPhoneNumber("+880" + phoneNo)       // Phone number to verify
                                .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                                .setActivity(otp.this)                 // Activity (for callback binding)
                                .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                                .build();
                PhoneAuthProvider.verifyPhoneNumber(options);
                // [END start_phone_auth]
            }

            private final PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks =
                    new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                        @Override
                        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                            super.onCodeSent(s, forceResendingToken);

                            verificationCodeBySystem = s;
                        }

                        @Override
                        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

                            String code = phoneAuthCredential.getSmsCode();
                            if (code != null) {
                                progressBar.setVisibility(View.VISIBLE);
                                verifyCode(code);
                            }
                        }

                        @Override
                        public void onVerificationFailed(@NonNull FirebaseException e) {
                            Toast.makeText(otp.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    };

        });
    }
    private void startCountDownTime() {
        resendEnabled = false;
        sendBtn.setTextColor(Color.parseColor("#99000000"));

        new CountDownTimer(resendTime * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                sendBtn.setText("Resend Code(" + (millisUntilFinished / 1000) + ")");
            }

            @Override
            public void onFinish() {
                resendEnabled = true;
                sendBtn.setText("Resend Code");
                sendBtn.setTextColor(getResources().getColor(R.color.black));
            }
        }.start();
    }

}


