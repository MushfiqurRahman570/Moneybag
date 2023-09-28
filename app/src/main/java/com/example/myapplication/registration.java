package com.example.myapplication;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.sign.login;
import com.example.myapplication.sign.otp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageReference;

public class registration extends AppCompatActivity {


    public Button upBtn;
    private static final int PICK_IMAGE_REQUEST = 1;
    private ImageView profilePictureImageView;
    private Uri selectedImageUri;
    public EditText regName, reUsername, regEmail, regPhone, regPassword;
    public TextView logBtn;
    FirebaseDatabase rootNode;
    DatabaseReference reference;
    private StorageReference storageReference;

    private boolean passwordShowing = false;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://money-bag-1857e-default-rtdb.firebaseio.com/");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        upBtn = findViewById(R.id.signupBtn);
        logBtn = findViewById(R.id.signInBtn);
        regName = findViewById(R.id.Name);
        reUsername = findViewById(R.id.userName);
        regEmail = findViewById(R.id.Email);
        regPhone = findViewById(R.id.Number);
        regPassword = findViewById(R.id.passwordET);
        profilePictureImageView = findViewById(R.id.profilePictureImageView);
        final ImageView PasswordIcon = findViewById(R.id.passwordIcon);

        rootNode = FirebaseDatabase.getInstance();
        reference = rootNode.getReference("users");


        PasswordIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (passwordShowing) {
                    passwordShowing = false;

                    regPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    PasswordIcon.setImageResource(R.drawable.view);
                } else {
                    passwordShowing = true;
                    regPassword.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    PasswordIcon.setImageResource(R.drawable.hide);
                }
                regPassword.setSelection(regPassword.length());
            }
        });

        logBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(registration.this, login.class));
            }
        });
    }

    private boolean validateName() {
        String val = regName.getText().toString();

        if (val.isEmpty()) {
            regName.setError("Field cannot be empty");
            return false;
        } else {
            regName.setError(null);
            return true;
        }
    }

    private boolean validateUserName() {
        String val = reUsername.getText().toString();
        String noWhiteSpace = "\\A\\w{4,20}\\z";

        if (val.isEmpty()) {
            reUsername.setError("Field cannot be empty");
            return false;
        } else if (val.length() >= 15) {
            reUsername.setError("Username too long");
            return true;
        } else if (!val.matches(noWhiteSpace)) {
            reUsername.setError("White Space are not Allowed");
            return false;
        } else {
            reUsername.setError(null);
            return false;
        }
    }

    private boolean validateEmail() {
        String val = regEmail.getText().toString();
        String emailPattern = "[a-zA-Z0-9_-]+@[a-z]+\\.+[a-z]+";

        if (val.isEmpty()) {
            regEmail.setError("Field cannot be empty");
            return false;
        } else if (!val.matches(emailPattern)) {
            regEmail.setError("Invalid Email");
            return false;
        } else {
            regEmail.setError(null);
            return false;
        }
    }

    private boolean validatePhone() {
        String val = regPhone.getText().toString();

        if (val.isEmpty()) {
            regPhone.setError("Field cannot be empty");
            return false;
        } else {
            regPhone.setError(null);
            return true;
        }
    }

    private boolean validatePassword() {
        String val = regPassword.getText().toString();
        String passwordVal = "0-9";

        if (val.isEmpty()) {
            regPassword.setError("Field cannot be empty");
            return false;
        } else if (val.matches(passwordVal)) {
            regPassword.setError("Password should Number");
            return false;
        } else {
            regPassword.setError(null);
            return true;
        }
    }

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            selectedImageUri = data.getData();
            profilePictureImageView.setImageURI(selectedImageUri);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void registerUser(View view) {


        String name = regName.getText().toString();
        String userName = reUsername.getText().toString();
        String email = regEmail.getText().toString();
        String regPhoneNo = regPhone.getText().toString();
        String password = regPassword.getText().toString();

        if (name.isEmpty() || userName.isEmpty() || email.isEmpty() || regPhoneNo.isEmpty() || password.isEmpty()) {
            Toast.makeText(registration.this, "Fill all field", Toast.LENGTH_SHORT).show();
        } else {

            Intent intent = new Intent(getApplicationContext(), otp.class);

            intent.putExtra("phoneNo", regPhoneNo);
            intent.putExtra("name", name);
            intent.putExtra("userName", userName);
            intent.putExtra("password", password);
            intent.putExtra("email", email);


            startActivity(intent);
        }

    }
}
