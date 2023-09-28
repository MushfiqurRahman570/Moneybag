package com.example.myapplication;

import static com.example.myapplication.amount.generateUid;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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

public class SendMoney extends AppCompatActivity {

    TextInputLayout SAmount;
    TextInputLayout SNumber;
    Button send;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    boolean isSend = false;
    String uuid = generateUid(10);
    String sendMoney ="";
    String sendNumber ="";
    Date currentDate = new Date();
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd h:mm a", Locale.getDefault());
    String dateTimeString = dateFormat.format(currentDate);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_money);

        SAmount = findViewById(R.id.sAmount);
        SNumber = findViewById(R.id.sNumber);
        send = findViewById(R.id.send);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 sendMoney = SAmount.getEditText().getText().toString();
                 sendNumber = SNumber.getEditText().getText().toString();

                sendDataToFirestore();
            }

            private void sendDataToFirestore() {

                try {
                    Bundle phoneNo = getIntent().getExtras();
                    String phone = phoneNo.getString("phoneNo");


                    if(sendNumber.equals(phone)){
                        Toast.makeText(SendMoney.this, "Try another Number", Toast.LENGTH_SHORT).show();

                    }else {

                    // Create a new user with a first and last name
                    Map<String, Object> userModel = new HashMap<>();
                    userModel.put("uId", uuid.toString());
                    userModel.put("sendMoney", sendMoney);
                    userModel.put("sendNumber", sendNumber);
                    userModel.put("timeStamp", dateTimeString);
                    //userModel.put("type", type);


                    // creating a collection reference
                    // for our Firebase Firestore database.
                    db.collection("userList").document(phone).collection("sendMoney").document(uuid.toString()).set(userModel)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(SendMoney.this, " send Money Done", Toast.LENGTH_SHORT).show();
//                                    _________________________________________________________________

                                    Map<String, Object> sendData = new HashMap<>();
                                    sendData.put("uId", uuid.toString());
                                    sendData.put("addMoney", sendMoney);
                                    sendData.put("receiveNumber", sendNumber);
                                    sendData.put("timeStamp", dateTimeString);


                                    db.collection("userList").document(sendNumber).collection("addMoney").document(uuid.toString()).set(sendData)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Toast.makeText(SendMoney.this, "add Money Done", Toast.LENGTH_SHORT).show();

                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(SendMoney.this, "Error", Toast.LENGTH_SHORT).show();
                                                }
                                            });
//                                    ____________________________________________________________

                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(SendMoney.this, "Error", Toast.LENGTH_SHORT).show();
                                }
                            });}


                } catch (Exception e) {
                    Toast.makeText(SendMoney.this, "Error DB", Toast.LENGTH_SHORT).show();
                }

            }
        });






    }
    public void addData(){


    }

}