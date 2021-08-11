package com.example.givelify;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    private EditText mEmail,mPass;
    private Button mLoginBtn;
    private ImageView RegisterImage;
    private TextView mSignUp;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mEmail = findViewById(R.id.email_login);
        mPass = findViewById(R.id.password_login);
        mLoginBtn = findViewById(R.id.button_login);
        mSignUp=findViewById(R.id.sign_up);

        mAuth = FirebaseAuth.getInstance();

//        mTextView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(RegistrationActivity.this, SplashScreenActivity.class));
////            Sign in Activity should change from registration
//            }
//        });

        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginUser();
            }

            private void loginUser() {
                String email = mEmail.getText().toString();
                String pass = mPass.getText().toString();

                if (!email.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    if (!pass.isEmpty()) {
                        mAuth.signInWithEmailAndPassword(email, pass ).addOnSuccessListener(authResult -> {
                            Toast.makeText(LoginActivity.this, "Login Successful!!", Toast.LENGTH_SHORT).show();
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

        mSignUp.setOnClickListener(v -> launchSignUp(v));
    }

    public void launchSignUp(View view) {
        Intent myIntent = new Intent(LoginActivity.this, RegistrationActivity.class);
        startActivity(myIntent);
    }
}