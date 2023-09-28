package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class AddMoney extends AppCompatActivity {

    Button bkash,rocket;
    ImageView Back;
    CardView bkashBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_money);

        Back = findViewById(R.id.back);
        bkash = findViewById(R.id.bkash);
        bkashBtn = findViewById(R.id.bkashBtn);
        rocket = findViewById(R.id.rocket);

        Bundle phoneNo = getIntent().getExtras();
        String Uid = phoneNo.getString("phoneNo");
        String BTxt =bkash.getText().toString();
        String RTxt = rocket.getText().toString();




        Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(AddMoney.this, MainActivity.class);
                startActivity(i);
            }
        });

        bkash.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent i = new Intent(AddMoney.this, amount.class);
                i.putExtra("phoneNo", Uid);
                i.putExtra("type",BTxt);

                startActivity(i);
            }
        });

        rocket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(AddMoney.this, amount.class);
                i.putExtra("phoneNo", Uid);
                i.putExtra("type",RTxt);

                startActivity(i);
            }
        });
    }
}