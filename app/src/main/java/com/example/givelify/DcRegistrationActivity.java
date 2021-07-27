package com.example.givelify;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import java.util.HashMap;
import java.util.Objects;

public class DcRegistrationActivity extends AppCompatActivity {
    private EditText mName, mEmail, mPass,mConfirmPass,mCity,mDescription;

    private final FirebaseDatabase db = FirebaseDatabase.getInstance();
    private final DatabaseReference root = db.getReference().child("DCenters");
    private final FirebaseAuth mAuth = FirebaseAuth.getInstance();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dc_registration);

        mName = findViewById(R.id.reg_name);
        mEmail= findViewById(R.id.email_reg);
        mPass= findViewById(R.id.reg_password);
        mConfirmPass= findViewById(R.id.reg_confirm_password);
        mCity= findViewById(R.id.reg_city);
        mDescription= findViewById(R.id.reg_description);
        Button mBtn = findViewById(R.id.sign_up_btn);

        mBtn.setOnClickListener(v -> createUser());
    }

    private void createUser() {
        String email= mEmail.getText().toString();
        String pass= mPass.getText().toString();
        String confirmPass= mConfirmPass.toString();

        if (!pass.isEmpty() && !confirmPass.isEmpty()&& !email.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            mAuth.createUserWithEmailAndPassword(email,pass).addOnSuccessListener(task -> {
                Toast.makeText(DcRegistrationActivity.this, "Registered Successfully!", Toast.LENGTH_SHORT).show();
                saveUserData();
                }).addOnFailureListener(e -> Toast.makeText(DcRegistrationActivity.this, "Registration Error!!", Toast.LENGTH_SHORT).show());

        } else if (email.isEmpty()) {
            mEmail.setError("Empty Fields Are Not Allowed");
        }else if (pass.isEmpty()) {
            mPass.setError("Empty Fields Are Not Allowed");
        } else if (confirmPass.isEmpty()) {
            mConfirmPass.setError("Empty Fields Are Not Allowed");
        }else {
            mEmail.setError("Please Enter Correct Email");
        }

    }
    private void saveUserData(){
        String email= mEmail.getText().toString();
        String pass= mPass.getText().toString();
        String city= mCity.getText().toString();
        String desc= mDescription.getText().toString();
        String name= mName.getText().toString();

            //add their info and data to realtime db
            HashMap<String, String> dcenterMap = new HashMap<>();
            dcenterMap.put("name", name);
            dcenterMap.put("email", email);
            dcenterMap.put("password", pass);
            dcenterMap.put("city", city);
            dcenterMap.put("description", desc);

            root.push().setValue(dcenterMap).addOnCompleteListener(task -> {
                Toast.makeText(DcRegistrationActivity.this, "Data Saved Successfully", Toast.LENGTH_SHORT).show();
            });
        startActivity(new Intent(DcRegistrationActivity.this, RegSuccessActivity.class));
        finish();
        //Change to the activity that comes after you register successfully

        }
}
