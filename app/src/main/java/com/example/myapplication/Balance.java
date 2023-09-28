package com.example.myapplication;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class Balance extends AppCompatActivity {
    public TextView FinalAmount;
    public TextView FinalAmount2;
//    Button Balance;
    Double totalAdd=0.0;
    private Double addSum,bal;
    BalanceHelper c = new BalanceHelper();

    Double totalSend = 0.0;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    HistoryAdapter myAdapter;
    ArrayList<userModel> mArrayList1 = new ArrayList<>();
    ArrayList<userModel> mArrayList = new ArrayList<>();
//    public double balance;

    private FrameLayout frameLayout;
    private Button button;
    private ImageView backBtn;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_balance);

//        FinalAmount = findViewById(R.id.TSend);
//        FinalAmount2= findViewById(R.id.TAdd);
//        Balance = findViewById(R.id.balance);
        db = FirebaseFirestore.getInstance();

        frameLayout = findViewById(R.id.frameLayout);
        button = findViewById(R.id.button);
        textView = findViewById(R.id.textView);
        backBtn =findViewById(R.id.backBtn);


        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Balance.this,MainActivity.class);
                intent.putExtra("bal",textView.getText());
                startActivity(intent);
            }
        });


//    }
//    public void CheckBalance(View view) {
//    Bundle phoneNo = getIntent().getExtras();
//    String phone = phoneNo.getString("phoneNo");
//
//    //send
//
//        db.collection("userList").document(phone).collection("sendMoney").get()
//                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
//                    @Override
//                    public void onSuccess(QuerySnapshot documentSnapshots) {
//                        if (documentSnapshots.isEmpty()) {
//                            Log.d(TAG, "onSuccess: LIST EMPTY");
//                            return;
//                        } else {
//
//                            List<userModel> acc = documentSnapshots.toObjects(userModel.class);
//                            mArrayList.addAll(acc);
//                            acc.clear();
//
//                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//
//                                mArrayList.forEach((n) ->
//
//                                        {
//                                            Double value = Double.parseDouble(n.sendMoney);
//                                            totalSend += value;
//
//                                        }
//                                );
//
//                                c.setBal(totalSend);
//                                double sendSum = c.getBal();
//                                Log.d(TAG, "total Send: " + sendSum);
//                                FinalAmount.setText(totalSend.toString());
//
//
//                            }
//                        }
//                    }
//                });
//
//        //add
//
//        db.collection("userList").document(phone).collection("addMoney").get()
//                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
//                    @Override
//                    public void onSuccess(QuerySnapshot documentSnapshots) {
//                        if (documentSnapshots.isEmpty()) {
//                            Log.d(TAG, "onSuccess: LIST EMPTY");
//                            return;
//                        } else {
//
//                            List<userModel> acc = documentSnapshots.toObjects(userModel.class);
//                            mArrayList1.addAll(acc);
//                            acc.clear();
//
//
//                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//
//                                mArrayList1.forEach((n) ->
//
//                                        {
//
//                                            Double value = Double.parseDouble(n.addMoney);
//                                            totalAdd += value;
//
//
//                                        }
//                                );
//
//
//                                c.setBal(totalAdd);
//                                addSum = c.getBal();
//                                Log.d(TAG, "total AddMoney: " + addSum.toString());
//                                FinalAmount2.setText(totalAdd.toString());
//                                FinalAmount.setText(totalSend.toString());
//
//                                double difference = totalAdd - totalSend;
//                                sum.setText(String.valueOf(difference));
//                                Balance.setVisibility(View.GONE);
//
//                            }
//                        }
//                    }
//                });


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show the TextView when the button is clicked
                textView.setVisibility(View.VISIBLE);
                button.setVisibility(View.GONE);

                Bundle phoneNo = getIntent().getExtras();
                String phone = phoneNo.getString("phoneNo");

                //send

                db.collection("userList").document(phone).collection("sendMoney").get()
                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot documentSnapshots) {
                                if (documentSnapshots.isEmpty()) {
                                    Log.d(TAG, "onSuccess: LIST EMPTY");
                                    return;
                                } else {

                                    List<userModel> acc = documentSnapshots.toObjects(userModel.class);
                                    mArrayList.addAll(acc);
                                    acc.clear();

                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {

                                        mArrayList.forEach((n) ->

                                                {
                                                    Double value = Double.parseDouble(n.sendMoney);
                                                    totalSend += value;

                                                }
                                        );

                                        c.setBal(totalSend);
                                        double sendSum = c.getBal();
                                        Log.d(TAG, "total Send: " + sendSum);
                                        //FinalAmount.setText(totalSend.toString());


                                    }
                                }
                            }
                        });

                //add

                db.collection("userList").document(phone).collection("addMoney").get()
                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot documentSnapshots) {
                                if (documentSnapshots.isEmpty()) {
                                    Log.d(TAG, "onSuccess: LIST EMPTY");
                                    return;
                                } else {

                                    List<userModel> acc = documentSnapshots.toObjects(userModel.class);
                                    mArrayList1.addAll(acc);
                                    acc.clear();


                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {

                                        mArrayList1.forEach((n) ->

                                                {

                                                    Double value = Double.parseDouble(n.addMoney);
                                                    totalAdd += value;


                                                }
                                        );


                                        c.setBal(totalAdd);
                                        addSum = c.getBal();
                                        Log.d(TAG, "total AddMoney: " + addSum.toString());
                                        //FinalAmount2.setText(totalAdd.toString());
                                        //FinalAmount.setText(totalSend.toString());



                                    }
                                }
                                double difference = (totalAdd - totalSend);
                                textView.setText(String.valueOf(difference));
                                mArrayList1.clear();
                                mArrayList.clear();
                                totalAdd=0.0;
                                totalSend=0.0;
                            }
                        });

                // Post a delayed runnable to hide the TextView after 5 seconds
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // Hide the TextView after 5 seconds
                        textView.setVisibility(View.GONE);
                        button.setVisibility(View.VISIBLE);
                    }
                }, 3000); // 5000 milliseconds = 5 seconds
            }
        });

    }

}