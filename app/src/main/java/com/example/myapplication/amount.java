package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

public class amount extends AppCompatActivity {

    ImageView BackA;
    Button Continue;
    TextInputLayout Add_amount;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ammount);

        BackA = findViewById(R.id.back);
        Continue = findViewById(R.id.Continue);
        Add_amount = findViewById(R.id.addAmount);
        db = FirebaseFirestore.getInstance();

        BackA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(amount.this, AddMoney.class);
                startActivity(i);
            }
        });

        Continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String addMoney = Add_amount.getEditText().getText().toString();

                addDataToFirestore(addMoney);
            }
        });
    }

    private void addDataToFirestore(String addMoney) {
        try {


            Bundle phoneNo = getIntent().getExtras();
            String phone = phoneNo.getString("phoneNo");
            String type = phoneNo.getString("type");
//            UUID uuid = UUID. randomUUID();
            String uuid = generateUid(10); // generates a UID of length 10


            Date currentDate = new Date();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd h:mm a", Locale.getDefault());
            String dateTimeString = dateFormat.format(currentDate);


            // Create a new user with a first and last name
            Map<String, Object> userModel = new HashMap<>();
            userModel.put("uId", uuid.toString());
            userModel.put("addMoney", addMoney);
            userModel.put("type", type);
            userModel.put("timeStamp", dateTimeString);

            // creating a collection reference
            // for our Firebase Firestore database.
            db.collection("userList").document(phone).collection("addMoney").document(uuid.toString()).set(userModel)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(amount.this, "Done", Toast.LENGTH_SHORT).show();
//                       Intent i = new Intent(amount.this, MainActivity.class);
//                       startActivity(i);
                            Continue.setVisibility(View.GONE);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(amount.this, "Error", Toast.LENGTH_SHORT).show();
                        }
                    });
        } catch (Exception e) {
            Toast.makeText(amount.this, "Error DB", Toast.LENGTH_SHORT).show();
        }
    }

    public static String generateUid(int length) {
        String allowedChars = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        Random random = new Random();
        StringBuilder uidBuilder = new StringBuilder();
        for (int i = 0; i < length; i++) {
            uidBuilder.append(allowedChars.charAt(random.nextInt(allowedChars.length())));
        }
        return uidBuilder.toString();
    }
}