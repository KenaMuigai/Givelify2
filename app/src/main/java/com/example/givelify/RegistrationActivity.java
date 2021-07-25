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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegistrationActivity extends AppCompatActivity {
    private EditText mEmail, mPass;
    private TextView mTextView;
    private Button mLoginBtn;
    private ImageView LoginImage;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mEmail = findViewById(R.id.email_reg);
        mPass = findViewById(R.id.reg_password);
        mLoginBtn = findViewById(R.id.sign_up_btn);

        mAuth = FirebaseAuth.getInstance();

//        mTextView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(LoginActivity.this, RegistrationActivity.class));
////            Sign in Activity should change from registration
//            }
//        });

        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createUser();
            }

            private void createUser() {
                String email = mEmail.getText().toString();
                String pass = mPass.getText().toString();

                if (!email.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    if (!pass.isEmpty()) {
                        mAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(task -> {
                            Toast.makeText(RegistrationActivity.this, "Registered Successfully!", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(RegistrationActivity.this, LoginActivity.class));
                            finish();
                            //Change to the activity that comes after you register successfully
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
        });
    }
}
