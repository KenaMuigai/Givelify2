package com.example.givelify;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

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
    private final FirebaseAuth mAuth= FirebaseAuth.getInstance();

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

        mBtn.setOnClickListener(v -> {
            String name= mName.getText().toString();
            String email= mEmail.getText().toString();
            String pass= mPass.getText().toString();
            String confirmPass= mConfirmPass.getText().toString();
            String city= mCity.getText().toString();
            String desc= mDescription.getText().toString();

            if (Objects.equals(pass, confirmPass) && !name.isEmpty() && !email.isEmpty() && !pass.isEmpty() && !city.isEmpty() && !desc.isEmpty()) {
                HashMap<String, String> dcenterMap = new HashMap<>();
                dcenterMap.put("name", name);
                dcenterMap.put("email", email);
                dcenterMap.put("password", pass);
                dcenterMap.put("city", city);
                dcenterMap.put("description", desc);

                root.push().setValue(dcenterMap).addOnCompleteListener(task -> Toast.makeText(DcRegistrationActivity.this,"Data Saved Successfully",Toast.LENGTH_SHORT).show());

                mAuth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(task -> Toast.makeText(DcRegistrationActivity.this,"User Registered",Toast.LENGTH_SHORT).show());
                mAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(task -> {
                    FirebaseUser user = mAuth.getCurrentUser();

                    assert user != null;
                    user.sendEmailVerification()
                            .addOnCompleteListener(task1 -> {
                                if (task1.isSuccessful()) {
                                    startActivity(new Intent(DcRegistrationActivity.this,RegSuccessActivity.class));
                                    finish();
                                }
                            });
                });

            }
            else if(!Objects.equals(pass, confirmPass)){
                Toast.makeText(DcRegistrationActivity.this,"Passwords Do Not Match",Toast.LENGTH_SHORT).show();
            }
            else if(name.isEmpty() || email.isEmpty() || pass.isEmpty() || city.isEmpty()|| desc.isEmpty()){
                Toast.makeText(DcRegistrationActivity.this,"Empty Fields Are Not Allowed!",Toast.LENGTH_SHORT).show();

            }

        });
    }

}
