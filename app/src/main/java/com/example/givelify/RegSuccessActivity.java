package com.example.givelify;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class RegSuccessActivity extends AppCompatActivity {
    private FloatingActionButton fab;
    private TextView sentEmail;
    private final FirebaseAuth mAuth = FirebaseAuth.getInstance();
    final FirebaseUser user = mAuth.getCurrentUser();

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg_success);

        fab=findViewById(R.id.fab_next);
        sentEmail= findViewById(R.id.sent_email);
        sentEmail.setText(user.getEmail());

        fab.setOnClickListener(v -> {
            startActivity(new Intent(RegSuccessActivity.this,MainActivity.class));
            finish();
        });
    }

}
