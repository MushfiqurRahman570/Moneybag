package com.example.myapplication;


import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class profile extends AppCompatActivity {

    TextView fullNameField, usernameField, fullName, email, phoneNo, userName, balance;
    ImageView pro_back, imageView;
    private StorageReference storageReference;
    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri imageUri;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firestore;
    private FirebaseStorage storage;
    private String userId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Initialize Firebase
        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();


        fullName = findViewById(R.id.pro_fullName);
        email = findViewById(R.id.pr_email);
        phoneNo = findViewById(R.id.pro_number);
        userName = findViewById(R.id.pro_username);
        pro_back = findViewById(R.id.pro_back);
        imageView = findViewById(R.id.pro_img);
        fullNameField = findViewById(R.id.fullNameField);
        usernameField = findViewById(R.id.userNameField);
        balance = findViewById(R.id.balance);


        balance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onStart();
            }
        });


        pro_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(profile.this, MainActivity.class));
            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openImagePicker();
            }
        });

        // Load and display the user's profile image from Firestore (if available)
        displayProfileImage();
    }

    private void openImagePicker() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            Uri selectedImageUri = data.getData();

            // Update the profile image with the selected image
            imageView.setImageURI(selectedImageUri);

            // Upload the selected image to Firebase Storage
            uploadImageToFirebase(selectedImageUri);
        }
    }

    private void uploadImageToFirebase(Uri imageUri) {
        // Generate a unique image filename (e.g., user's UID)
        String imageFileName = userId + ".jpg";
        StorageReference imageRef = storageReference.child("profile_images/" + imageFileName);

        imageRef.putFile(imageUri)
                .addOnSuccessListener(taskSnapshot -> {
                    // Image uploaded successfully
                    imageRef.getDownloadUrl()
                            .addOnSuccessListener(downloadUri -> {
                                // Save the download URL in Firestore
                                firestore.collection("usersList").document(userId)
                                        .update("profileImage", downloadUri.toString())
                                        .addOnSuccessListener(aVoid -> {
                                            // Download URL saved successfully in Firestore
                                        })
                                        .addOnFailureListener(e -> {
                                            // Handle Firestore update failure
                                        });
                            })
                            .addOnFailureListener(e -> {
                                // Handle getting download URL failure
                            });
                })
                .addOnFailureListener(e -> {
                    // Handle image upload failure
                });
    }

    private void displayProfileImage() {
        firestore.collection("usersList").document(userId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String profileImageURL = documentSnapshot.getString("profileImage");
                        if (profileImageURL != null) {
                            // Use Glide to load and display the profile image
                            RequestOptions requestOptions = new RequestOptions()
                                    .diskCacheStrategy(DiskCacheStrategy.ALL); // Cache options

                            Glide.with(this)
                                    .load(profileImageURL)
                                    .apply(requestOptions)
                                    .placeholder(R.drawable.pro_img)
                                    .error(R.drawable.pro_img)
                                    .into(imageView);
                        } else {
                            // Use a default profile image if profileImageURL is null
                            imageView.setImageResource(R.drawable.pro_img);
                        }
                    }
                });

        getData();

    }
     
    private void getData() {
        FirebaseDatabase myDataBase = FirebaseDatabase.getInstance();
        DatabaseReference myDB = myDataBase.getReference("users");
        Bundle phone = getIntent().getExtras();
        if (phone != null) {
            String userId = phone.getString("phoneNo");

            myDB.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    UserhelperClass userhelperClass = snapshot.getValue(UserhelperClass.class);

                    phoneNo.setText(userhelperClass.phoneNo.toString());
                    userName.setText(userhelperClass.username.toString());
                    email.setText(userhelperClass.email.toString());
                    fullName.setText(userhelperClass.name.toString());
                    fullNameField.setText(userhelperClass.name.toString());
                    usernameField.setText(userhelperClass.username.toString());

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

    }


}

