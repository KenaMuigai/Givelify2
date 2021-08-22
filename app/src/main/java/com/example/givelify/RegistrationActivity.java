package com.example.givelify;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

import static android.content.ContentValues.TAG;

public class RegistrationActivity extends AppCompatActivity {
    private EditText mEmail, mPass, mFullName, mPhoneNo,mCity;
    private Button mSignUpBtn;
    private TextView mDcReg;

    private ImageView LoginImage;

    private FirebaseAuth mAuth;
    private final FirebaseDatabase db = FirebaseDatabase.getInstance();
    private final DatabaseReference root = db.getReference().child("Users");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mAuth= FirebaseAuth.getInstance();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        mEmail = findViewById(R.id.email_sign_up);
        mPass = findViewById(R.id.password_sign_up);
        mSignUpBtn = findViewById(R.id.button_sign_up);
        mDcReg =findViewById(R.id.tv_dc_register);
        mFullName= findViewById(R.id.full_name);
        mPhoneNo= findViewById(R.id.phone_sign_up);
        mCity= findViewById(R.id.reg_city2);

        mSignUpBtn.setOnClickListener(view -> createUser());

        mDcReg.setOnClickListener(v -> {
            Intent myIntent = new Intent(RegistrationActivity.this, DcRegistrationActivity.class);
            startActivity(myIntent);
        });

    }
    private void createUser() {
        String email = mEmail.getText().toString();
        String pass = mPass.getText().toString();

        if (!email.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            if (!pass.isEmpty()) {
                mAuth.createUserWithEmailAndPassword(email,pass).addOnSuccessListener(task -> {
                    Toast.makeText(RegistrationActivity.this, "Registered Successfully!", Toast.LENGTH_SHORT).show();
                    saveUserData();
                }).addOnFailureListener(e -> Toast.makeText(RegistrationActivity.this, "Registration Error!!", Toast.LENGTH_SHORT).show());
            } else {
                mPass.setError("Empty Fields Are Not Allowed");
            }
        } else if (email.isEmpty()) {
            mEmail.setError("Empty Fields Are Not Allowed");
        } else {
            mEmail.setError("Please Enter Correct Email");
        }
    }

    private void saveUserData() {
        String user_id =mAuth.getCurrentUser().getUid();
        DatabaseReference current_user_db = root.child(user_id);

        String email= mEmail.getText().toString();
        String pass= mPass.getText().toString();
        String city= mCity.getText().toString();
        String name= mFullName.getText().toString();
        String phone= mPhoneNo.getText().toString();

        //add their info and data to realtime db
        HashMap<String, String> dcenterMap = new HashMap<>();
        dcenterMap.put("name", name);
        dcenterMap.put("email", email);
        dcenterMap.put("password", pass);
        dcenterMap.put("city", city);
        dcenterMap.put("phone", phone);

        current_user_db.setValue(dcenterMap).addOnCompleteListener(task -> Toast.makeText(RegistrationActivity.this, "Data Saved Successfully", Toast.LENGTH_SHORT).show());
        sendEmailVerification();
    }
    private void sendEmailVerification() {
        // Send verification email
        // [START send_email_verification]
        final FirebaseUser user = mAuth.getCurrentUser();
        assert user != null;
        user.sendEmailVerification()
                .addOnCompleteListener(this, task -> {
                    startActivity(new Intent(RegistrationActivity.this, RegSuccessActivity.class));
                    finish();
                });
        updateProfile();
    }
    private void updateProfile() {
        final FirebaseUser user = mAuth.getCurrentUser();
        assert user != null;
        String uname= mFullName.getText().toString();

        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(uname)
                .setPhotoUri(Uri.parse("android.resource://com.example.givelify/drawable/charity"))
                .build();

        user.updateProfile(profileUpdates)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "User profile updated.");
                    }
                });

    }
}
