package com.example.myapplication;

import static android.content.ContentValues.TAG;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager2.widget.ViewPager2;

import com.example.myapplication.sign.login;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;
    private ViewPager2 viewPager;
    private int[] imageIds = {R.drawable.alu, R.drawable.kola, R.drawable.begun};

    NavigationView navigationView;
    ActionBarDrawerToggle toggle;
    ImageView imageMenu, notification_menu;
    RelativeLayout scan, addMoney, myQr, sendMoney,recharge,payment;
    Button sign_off,BalanceBtn;
    TextView sum;
    private FrameLayout frameLayout;
    private Button button;
    private TextView textView;
    FirebaseAuth mfirebaseAuth;
    String uid;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    ArrayList<userModel> mArrayList1 = new ArrayList<>();
    ArrayList<userModel> mArrayList2 = new ArrayList<>();
    ArrayList<userModel>mArrayList3=new ArrayList<>();
    ArrayList<userModel>mArrayList4=new ArrayList<>();

    ArrayList<userModel> mArrayList = new ArrayList<>();
    ProgressBar progressBar;

    BalanceHelper c = new BalanceHelper();

    private Double totalAdd;
    private Double totalSend;
    private Double totalRecharge;
    private Double addSum;
    private Double Eamoney;
    public Double Minus;
    Double AddMoney;
    Double SendMoney;
    Boolean isAdd = true;
    Boolean isSend = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_View);
        imageMenu = findViewById(R.id.imageMenu);
        scan = findViewById(R.id.scanQr);
        myQr = findViewById(R.id.myQr);
        addMoney = findViewById(R.id.addMoney);
        sendMoney = findViewById(R.id.send);
        db = FirebaseFirestore.getInstance();
        frameLayout = findViewById(R.id.frameLayout);
        button = findViewById(R.id.button);
        textView = findViewById(R.id.textView);
        mfirebaseAuth = FirebaseAuth.getInstance();
        recharge = findViewById(R.id.rechargeId);
        payment=findViewById(R.id.payment);

        notification_menu = findViewById(R.id.notification_menu);

        viewPager = findViewById(R.id.viewPager);
        SlideshowPagerAdapter adapter = new SlideshowPagerAdapter(imageIds);
        viewPager.setAdapter(adapter);


        addMoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle phone = getIntent().getExtras();
                String userId = phone.getString("phoneNo");
                uid = userId;
                mArrayList1.clear();
                totalAdd = 0.0;
                Intent i = new Intent(MainActivity.this, AddMoney.class);
                i.putExtra("phoneNo", userId);
                startActivity(i);
            }
        });


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mArrayList2.clear();
                totalSend = 0.0;
                totalAdd = 0.0;
                Minus=0.0;
                Eamoney=0.0;
                totalRecharge=0.0;
                mArrayList1.clear();
                mArrayList3.clear();
                mArrayList4.clear();

                button.setVisibility(View.GONE);
                textView.setVisibility(View.VISIBLE);


                Bundle phoneNo = getIntent().getExtras();
                String phone = phoneNo.getString("phoneNo");

                //recharge

                db.collection("userList").document(phone).collection("recharge").get()
                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot documentSnapshots) {
                                if (documentSnapshots.isEmpty()) {
                                    Log.d(TAG, "onSuccess: LIST EMPTY");
                                    return;
                                } else {

                                    List<userModel> acc = documentSnapshots.toObjects(userModel.class);
                                    mArrayList3.addAll(acc);
                                    acc.clear();

                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {

                                        mArrayList3.forEach((n) ->

                                                {
                                                    Double value = Double.parseDouble(n.rechargeAmount);
                                                    totalRecharge += value;

                                                }
                                        );


                                        c.setBal(totalRecharge);
                                        double totalRecharge = c.getBal();
                                        Log.d(TAG, "total Send: " + totalRecharge);
                                        //FinalAmount.setText(totalSend.toString());


                                    }
                                }
                            }
                        });

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
                                double Minus = (totalSend+totalRecharge);
                                double difference = ( totalAdd - Minus );

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        textView.setText(String.valueOf(difference));

                                    }
                                });
                                mArrayList1.clear();
                                mArrayList2.clear();
                                mArrayList3.clear();
                                mArrayList.clear();
                                totalAdd = 0.0;
                                totalSend = 0.0;
                                totalRecharge=0.0;
                                Eamoney=0.0;


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

        sendMoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle phone = getIntent().getExtras();
                String userId = phone.getString("phoneNo");
                uid = userId;
                mArrayList2.clear();
                totalSend = 0.0;
                Intent i = new Intent(MainActivity.this, SendMoney.class);
                i.putExtra("phoneNo", userId);
                startActivity(i);
            }
        });

        recharge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle phone = getIntent().getExtras();
                String userId = phone.getString("phoneNo");
                uid = userId;
                mArrayList3.clear();
                totalRecharge = 0.0;
                Intent ii = new Intent(MainActivity.this, RechargeNum.class);
                ii.putExtra("phoneNo", userId);
                startActivity(ii);
            }
        });

        payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle phone = getIntent().getExtras();
                String userId = phone.getString("phoneNo");
                uid = userId;
                Intent iii = new Intent(MainActivity.this, Payment.class);
                iii.putExtra("phoneNo", userId);
                startActivity(iii);
            }
        });



        myQr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle phone = getIntent().getExtras();
                String userId = phone.getString("phoneNo");

                Intent i = new Intent(MainActivity.this, MyQr.class);
                i.putExtra("phoneNo", userId);
                startActivity(i);
            }
        });

        notification_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle phone = getIntent().getExtras();
                String userId = phone.getString("phoneNo");

                Intent i = new Intent(MainActivity.this, Notification.class);
                i.putExtra("phoneNo", userId);
                startActivity(i);
            }
        });


        toggle = new ActionBarDrawerToggle(MainActivity.this, drawerLayout, R.string.nav_open, R.string.nav_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.profile:

                        Bundle phone = getIntent().getExtras();
                        String uid = phone.getString("phoneNo");
                        Intent i = new Intent(MainActivity.this, profile.class);

                        i.putExtra("phoneNo", uid);
                        startActivity(i);

//                        Toast.makeText(MainActivity.this, "My Qr", Toast.LENGTH_SHORT).show();
                        drawerLayout.closeDrawers();
                        break;

                    case R.id.sign_off:
                        mfirebaseAuth.signOut();
                        Intent intent = new Intent(MainActivity.this, login.class);
                        startActivity(intent);
                        drawerLayout.closeDrawers();
                        break;
                    case R.id.MyQr:
                        Bundle phoneNo = getIntent().getExtras();
                        String Uid = phoneNo.getString("phoneNo");
                        Intent in = new Intent(MainActivity.this, MyQr.class);
                        in.putExtra("phoneNo", Uid);
                        startActivity(in);
                        drawerLayout.closeDrawers();
                        break;

                    case R.id.amount:
                        Bundle phoneNumber = getIntent().getExtras();
                        String uuid = phoneNumber.getString("phoneNo");
                        Intent intentI = new Intent(MainActivity.this, Balance.class);
                        intentI.putExtra("phoneNo", uuid);
                        startActivity(intentI);
                        drawerLayout.closeDrawers();
                        break;

                    case R.id.transaction:
                        Bundle phoneNum = getIntent().getExtras();
                        String Uuid = phoneNum.getString("phoneNo");
                        Intent intentII = new Intent(MainActivity.this, history.class);
                        intentII.putExtra("phoneNo", Uuid);
                        startActivity(intentII);
                        drawerLayout.closeDrawers();
                        break;


                }

                return false;
            }
        });
        imageMenu = findViewById(R.id.imageMenu);

        imageMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Code Here
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentIntegrator intentIntegrator = new IntentIntegrator(MainActivity.this);
                intentIntegrator.setDesiredBarcodeFormats(intentIntegrator.QR_CODE);
                intentIntegrator.setCameraId(0);
                intentIntegrator.setOrientationLocked(false);
                intentIntegrator.setPrompt("scanning");
                intentIntegrator.setBeepEnabled(true);
                intentIntegrator.setBarcodeImageEnabled(true);
                intentIntegrator.initiateScan();

            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        String scannedContent = result.getContents();

        if (isValid11DigitNumber(scannedContent)) {

        if (result != null && result.getContents() != null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("Scan Result");
            builder.setMessage(result.getContents());

            // Add a "Copy" button to the AlertDialog
            builder.setPositiveButton("Copy", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    // Copy the result to the clipboard
                    ClipboardManager manager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                    ClipData data = ClipData.newPlainText("result", result.getContents());
                    manager.setPrimaryClip(data);
                    // Dismiss the AlertDialog
                    dialogInterface.dismiss();
                }
            });

            // Add a "Redirect" button to the AlertDialog
            builder.setNegativeButton("Payment", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    // Start a new activity to display the copied result
                    Bundle phoneNum = getIntent().getExtras();
                    Eamoney=0.0;
                    mArrayList4.clear();
                    String Uuid = phoneNum.getString("phoneNo");
                    Intent intent = new Intent(MainActivity.this, EasySend.class);
                    intent.putExtra("copiedResult", result.getContents());
                    intent.putExtra("phoneNo", Uuid);
                    startActivity(intent);
                    // Dismiss the AlertDialog
                    dialogInterface.dismiss();
                }
            });

            // Create and show the AlertDialog
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        } else {
            // Show a message indicating that the scanned content is not valid
            new AlertDialog.Builder(MainActivity.this)
                    .setTitle("Invalid QR Code")
                    .setMessage("The scanned QR code does not contain a valid 11-digit number.")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    }).create().show();
        }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    // Helper method to check if a string is a valid 11-digit number
    private boolean isValid11DigitNumber(String content) {
        // Check if the content is numeric and has exactly 11 digits
        return content.matches("[0-9]+") && content.length() == 11;
    }

    }

