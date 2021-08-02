package com.example.givelify;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;


public class RegSuccessActivity extends AppCompatActivity {
    private FloatingActionButton fab;
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg_success);

        fab.setOnClickListener(v -> {
            startActivity(new Intent(RegSuccessActivity.this,MainActivity.class));
            finish();
        });
    }

}
