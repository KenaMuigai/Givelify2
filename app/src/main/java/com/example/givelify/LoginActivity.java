package com.example.givelify;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends AppCompatActivity {
    private EditText mName, mEmail, mPhone, mPass;
    private TextView mTextView;
    private Button mRegisterBtn;
    private ImageView RegisterImage;

    private FirebaseAuth mAuth;

    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private DatabaseReference root = db.getReference().child("Users");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        mName = findViewById(R.id.full_name);
        mEmail = findViewById(R.id.email_sign_up);
        mPhone = findViewById(R.id.phone_sign_up);
        mPass = findViewById(R.id.password_sign_up);
        mRegisterBtn = findViewById(R.id.button_sign_up);

        mAuth = FirebaseAuth.getInstance();

//        mTextView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(RegistrationActivity.this, SplashScreenActivity.class));
////            Sign in Activity should change from registration
//            }
//        });

        mRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginUser();

                String name = mName.getText().toString();
                root.setValue(name);
            }

            private void loginUser() {
                String name = mName.getText().toString();
                String email = mEmail.getText().toString();
                String phone = mPhone.getText().toString();
                String pass = mPass.getText().toString();

                if (!email.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    if (!pass.isEmpty()) {
                        mAuth.signInWithEmailAndPassword(email, pass ).addOnSuccessListener(authResult -> {
                            Toast.makeText(LoginActivity.this, "Login Successfull!!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        finish();
                        }).addOnFailureListener(e -> Toast.makeText(LoginActivity.this, "Login Failed!!", Toast.LENGTH_SHORT).show());
                    } else {
                        mPass.setError("Empty Fields Are Not Allowed");
                    }
                } else if (email.isEmpty()) {
                    mEmail.setError("Empty Fields Are Not Allowed");
                } else {
                    mEmail.setError("Please Enter Correct Email");
                }
            }
        });
    }
}