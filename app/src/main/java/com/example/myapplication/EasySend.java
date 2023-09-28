package com.example.myapplication;

import static com.example.myapplication.amount.generateUid;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
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

public class EasySend extends AppCompatActivity {

    TextView SNumber;
    TextInputLayout SAmount;
    EditText password;
    ProgressBar progressBar;
    Button send;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    boolean isSend = false;
    String uuid = generateUid(10);
    String Eamoney ="";
    String sendNumber ="";
    Date currentDate = new Date();
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd h:mm a", Locale.getDefault());
    String dateTimeString = dateFormat.format(currentDate);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_easy_send);

        SNumber = findViewById(R.id.resultTextView);
        SAmount = findViewById(R.id.sAmount);
        send = findViewById(R.id.send);
        password = findViewById(R.id.password);
        progressBar = findViewById(R.id.progress);

        String copiedResult = getIntent().getStringExtra("copiedResult");

        SNumber.setText(copiedResult);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                sendDataToFirestore();
            }

            private void sendDataToFirestore() {
                Eamoney = SAmount.getEditText().getText().toString();
                sendNumber = copiedResult.toString();
                final String userEnteredPassword = password.getText().toString().trim();


                try {

                    Bundle phoneNo = getIntent().getExtras();
                    String phone = phoneNo.getString("phoneNo");

                    if (sendNumber.equals(phone)){
                        Toast.makeText(EasySend.this, "Try another Number", Toast.LENGTH_SHORT).show();


                    }else {

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
                                        userModel.put("sendMoney", Eamoney);
                                        userModel.put("sendNumber", sendNumber);
                                        userModel.put("timeStamp", dateTimeString);
                                        //userModel.put("type", type);


                                        // creating a collection reference
                                        // for our Firebase Firestore database.
                                        db.collection("userList").document(phone).collection("sendMoney").document(uuid.toString()).set(userModel)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        Toast.makeText(EasySend.this, " send Money Done", Toast.LENGTH_SHORT).show();
//                                    _________________________________________________________________

                                                        Map<String, Object> sendData = new HashMap<>();
                                                        sendData.put("uId", uuid.toString());
                                                        sendData.put("addMoney", Eamoney);
                                                        sendData.put("receiveNumber", sendNumber);
                                                        sendData.put("timeStamp", dateTimeString);


                                                        db.collection("userList").document(sendNumber).collection("addMoney").document(uuid.toString()).set(sendData)
                                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                    @Override
                                                                    public void onSuccess(Void aVoid) {

                                                                    }
                                                                })
                                                                .addOnFailureListener(new OnFailureListener() {
                                                                    @Override
                                                                    public void onFailure(@NonNull Exception e) {
                                                                        Toast.makeText(EasySend.this, "Error", Toast.LENGTH_SHORT).show();
                                                                    }
                                                                });
//                                    ____________________________________________________________
                                                        progressBar.setVisibility(View.INVISIBLE);

                                                    }

                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Toast.makeText(EasySend.this, "Error", Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                    } else {
                                        Toast.makeText(EasySend.this, "wrong Password", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                            }
                        });
                    }
                } catch (Exception e) {
                    Toast.makeText(EasySend.this, "Error DB", Toast.LENGTH_SHORT).show();

                }
            }
        });
    }
}
