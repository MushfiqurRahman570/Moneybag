package com.example.myapplication;

import static com.example.myapplication.amount.generateUid;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class RAmmount extends AppCompatActivity {

    private Button longPressButton;
    private EditText Eamount,password;
    String rNumber="";
    String rOperator="";
    ProgressBar progressBar;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    private View customAnimationView;
    private CountDownTimer countDownTimer;
    private Animation scaleAnimation;
    private boolean buttonHeld = false;
    String uuid = generateUid(10);

    TextView number,operator;
    Date currentDate = new Date();
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd h:mm a", Locale.getDefault());
    String dateTimeString = dateFormat.format(currentDate);



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rammount);

        number = findViewById(R.id.number);
        operator = findViewById(R.id.operator);
        Eamount = findViewById(R.id.amount);
        password = findViewById(R.id.password);
        progressBar = findViewById(R.id.progress);


        rOperator = getIntent().getStringExtra("selectedItem");
        rNumber = getIntent().getStringExtra("RNumber");


        number.setText(rNumber);
        operator.setText(rOperator);

        longPressButton = findViewById(R.id.longPressButton);
        customAnimationView = findViewById(R.id.customAnimationView);

        // Load the custom scale animation
        scaleAnimation = AnimationUtils.loadAnimation(this, R.anim.scale_animation);

        // Set a touch listener to detect button presses and releases
        longPressButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        // Start the CountDownTimer when the button is touched (pressed)
                        buttonHeld = true;
                        customAnimationView.setVisibility(View.VISIBLE);
                        customAnimationView.startAnimation(scaleAnimation);
                        startCountdown();
                        break;
                    case MotionEvent.ACTION_UP:
                        // Stop the CountDownTimer when the button is released
                        buttonHeld = false;
                        customAnimationView.setVisibility(View.INVISIBLE);
                        stopCountdown();
                        break;
                }
                return false;
            }
        });

        // Initialize the CountDownTimer
        countDownTimer = new CountDownTimer(3000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                // Not used in this example, but you can update UI for the remaining time
            }

            @Override
            public void onFinish() {
                if (buttonHeld) {
                    // Long press detected (4 seconds), trigger the action
                    performAction();
                }
            }
        };
    }

    private void startCountdown() {
        if (buttonHeld) {
            countDownTimer.start();
        }
    }

    private void stopCountdown() {
        countDownTimer.cancel();
    }

    private void performAction() {


        String RMoney = Eamount.getText().toString();
        String Rnumber = rNumber.toString();
        String reOperator= rOperator.toString();
        final String userEnteredPassword = password.getText().toString().trim();


        try {
            Bundle phoneNo = getIntent().getExtras();
            String phone = phoneNo.getString("phoneNo");



            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
            Query checkUser = reference.orderByChild("phoneNo").equalTo(phone);

            checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        String passwordFromDB = dataSnapshot.child(phone).child("password").getValue(String.class);


                        if (passwordFromDB.equals(userEnteredPassword)) {

                            progressBar.setVisibility(View.VISIBLE);


                            // Create a new user with a first and last name
                            Map<String, Object> userModel = new HashMap<>();
                            userModel.put("uId", uuid.toString());
                            userModel.put("rechargeAmount", RMoney);
                            userModel.put("rechargeNumber", Rnumber);
                            userModel.put("rechargeOperator", reOperator);
                            userModel.put("timeStamp", dateTimeString);
                            //userModel.put("type", type);


                            // creating a collection reference
                            // for our Firebase Firestore database.
                            db.collection("userList").document(phone).collection("recharge").document(uuid.toString()).set(userModel)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Intent intent = new Intent(getApplicationContext(), RechargeNum.class);
                                            startActivity(intent);

                                            Toast.makeText(RAmmount.this, " Recharge Done", Toast.LENGTH_SHORT).show();

//                                    ____________________________________________________________

                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(RAmmount.this, "Error", Toast.LENGTH_SHORT).show();
                                        }
                                    });


                        } else {
                            Toast.makeText(RAmmount.this, "wrong Password", Toast.LENGTH_SHORT).show();


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
        } catch (Exception e) {
                Toast.makeText(RAmmount.this, "Error DB", Toast.LENGTH_SHORT).show();

            }}}
