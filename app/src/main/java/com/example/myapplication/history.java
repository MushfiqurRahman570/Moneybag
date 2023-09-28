package com.example.myapplication;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class history extends AppCompatActivity {

    RecyclerView recyclerView;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    //FirebaseFirestore db;
    HistoryAdapter myAdapter;
    ArrayList<userModel> list;
    String uid;
    String addMoney;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        recyclerView = findViewById(R.id.recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        db = FirebaseFirestore.getInstance();

        list = new ArrayList<>();
        myAdapter = new HistoryAdapter(this, list);
        recyclerView.setAdapter(myAdapter);
        Bundle phoneNo = getIntent().getExtras();
        String phone = phoneNo.getString("phoneNo");

        db.collection("userList").document(phone).collection("addMoney").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<DocumentSnapshot> listData = queryDocumentSnapshots.getDocuments();
                        for (DocumentSnapshot documentSnapshot : listData) {
                            userModel obj = documentSnapshot.toObject(userModel.class);

                            list.add(obj);
                        }

                        myAdapter.notifyDataSetChanged();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // if we do not get any data or any error we are displaying
                        // a toast message that we do not get any data
                        Toast.makeText(history.this, "Fail to get the data.", Toast.LENGTH_SHORT).show();
                    }
                });

        db.collection("userList").document(phone).collection("sendMoney").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<DocumentSnapshot> listData = queryDocumentSnapshots.getDocuments();
                        for (DocumentSnapshot documentSnapshot : listData) {
                            userModel obj = documentSnapshot.toObject(userModel.class);

                            list.add(obj);
                        }

                        myAdapter.notifyDataSetChanged();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // if we do not get any data or any error we are displaying
                        // a toast message that we do not get any data
                        Toast.makeText(history.this, "Fail to get the data.", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}