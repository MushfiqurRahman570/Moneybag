package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

public class RechargeNum extends AppCompatActivity {

    Button rContinue;
    EditText ReNumber;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recharge_num);

        rContinue=findViewById(R.id.rContinue);
        ReNumber =findViewById(R.id.ReNumber);

        Bundle phoneNo = getIntent().getExtras();
        String phone = phoneNo.getString("phoneNo");




        Spinner staticSpinner = (Spinner) findViewById(R.id.static_spinner);
        ArrayAdapter<CharSequence> staticAdapter = ArrayAdapter
                .createFromResource(this, R.array.brew_array,
                        android.R.layout.simple_spinner_item);

        // Specify the layout to use when the list of choices appears
        staticAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        staticSpinner.setAdapter(staticAdapter);

        rContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Rnumber = ReNumber.getText().toString();

                if (Rnumber.isEmpty())  {

                    ReNumber.setError("Recharge number is required.");}

                 else {
                    // Recharge number is valid; proceed with the intent
                    Intent intent = new Intent(RechargeNum.this, RAmmount.class);
                    String selectedItem = staticSpinner.getSelectedItem().toString();
                    intent.putExtra("selectedItem", selectedItem);
                    intent.putExtra("RNumber", Rnumber);
                    intent.putExtra("phoneNo", phone);
                    startActivity(intent);
                }
            }

        });

    }
}