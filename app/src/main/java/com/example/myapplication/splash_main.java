package com.example.myapplication;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;
import com.example.myapplication.sign.login;

public class splash_main extends AppCompatActivity {

    LottieAnimationView lottie;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        lottie = findViewById(R.id.lottie);

        lottie.animate().translationX(2000).setDuration(5000).setStartDelay(2500);

        // Start checking for internet connectivity
        checkInternetPeriodically();
    }

    private void checkInternetPeriodically() {
        final Handler handler = new Handler();
        final int delay = 2000; // 5 seconds delay between checks

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isConnectedToInternet()) {
                    // Internet is available, proceed to the login screen
                    startActivity(new Intent(getApplicationContext(), login.class));
                    finish();
                } else {
                    Toast.makeText(splash_main.this, "Connect to Internet", Toast.LENGTH_SHORT).show();
                    handler.postDelayed(this, delay);
                }
            }
        }, delay);
    }

    private boolean isConnectedToInternet() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnectedOrConnecting();
    }
}