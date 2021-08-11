package com.example.givelify;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;

import static android.content.ContentValues.TAG;

public class DcRegistrationActivity extends AppCompatActivity {
    private EditText mName, mEmail, mPass,mConfirmPass,mCity,mDescription;
    private ProgressBar progBar;
    private Uri imageUri= Uri.parse("android.resource://com.example.givelify/drawable/charity.png");

    private final FirebaseDatabase db = FirebaseDatabase.getInstance();
    private final DatabaseReference root = db.getReference().child("DCenters");
    private StorageReference reference = FirebaseStorage.getInstance().getReference();
    private final FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseAuth.AuthStateListener mAuthListener;
    private String user_id =mAuth.getCurrentUser().getUid();
    private final DatabaseReference current_user_db = root.child(user_id);


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
        mAuthListener = firebaseAuth -> {
            FirebaseUser user = firebaseAuth.getCurrentUser();
            if (user!=null){
                System.out.println("User logged in");
            }
            else{
                System.out.println("User not logged in");
            }
        };
    }
    private void createUser() {
        String email= mEmail.getText().toString();
        String pass= mPass.getText().toString();
        String confirmPass= mConfirmPass.toString();

        if (!pass.isEmpty() && !confirmPass.isEmpty()&& !email.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            mAuth.createUserWithEmailAndPassword(email,pass).addOnSuccessListener(task -> {
                Toast.makeText(DcRegistrationActivity.this, "Registered Successfully!", Toast.LENGTH_SHORT).show();
                saveUserData();
                }).addOnFailureListener(e -> {
                Toast.makeText(DcRegistrationActivity.this, "Registration Error!!", Toast.LENGTH_SHORT).show();
                Log.w(TAG, "createUserWithEmail:failure", e);
            });

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

        current_user_db.push().setValue(dcenterMap).addOnCompleteListener(task -> Toast.makeText(DcRegistrationActivity.this, "Data Saved Successfully", Toast.LENGTH_SHORT).show());
        sendEmailVerification(); // send the user a verification email after registering

        }
    private void sendEmailVerification() {
        // Send verification email
        // [START send_email_verification]
        final FirebaseUser user = mAuth.getCurrentUser();
        assert user != null;
        user.sendEmailVerification()
                .addOnCompleteListener(this, task -> {
                    startActivity(new Intent(DcRegistrationActivity.this, RegSuccessActivity.class));
                    finish();
                });
    }
    public void onStart(){
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }
    public void onStop(){
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);

        }
    }

}
