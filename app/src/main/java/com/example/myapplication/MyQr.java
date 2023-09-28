package com.example.myapplication;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.UUID;


public class  MyQr extends AppCompatActivity {

    public String QrData;
    public Button generate;
    public ImageView qr_code,backA;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_qr);

        generate = findViewById(R.id.btnGenerate);
        qr_code = findViewById(R.id.imageCode);
        backA =findViewById(R.id.back);

        backA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MyQr.this, MainActivity.class);
                startActivity(i);
            }
        });

        getData();
    }

    private void getData(){
        FirebaseDatabase myDataBase= FirebaseDatabase.getInstance();
        DatabaseReference myDB = myDataBase.getReference("users");
        Bundle phone = getIntent().getExtras();
        if (phone != null) {
            String userId = phone.getString("phoneNo");

            myDB.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    UserhelperClass userhelperClass = snapshot.getValue(UserhelperClass.class);

                    QrData = phone.getString("phoneNo");

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }

    public void QrGenerate (View view){



        if(QrData!=null && !QrData.isEmpty()){
            try {
                MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
                BitMatrix bitMatrix = multiFormatWriter.encode(QrData,BarcodeFormat.QR_CODE,800,800);
                BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                Bitmap bitmap= barcodeEncoder.createBitmap(bitMatrix);
                qr_code.setImageBitmap(bitmap);
            }catch (WriterException e){
                e.printStackTrace();
            }
        }
    }

    }

