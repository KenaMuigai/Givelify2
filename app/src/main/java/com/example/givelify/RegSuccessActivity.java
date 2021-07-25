package com.example.givelify;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;


public class RegSuccessActivity extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg_success);
    }

    public void launchHome(View view) {
        startActivity(new Intent(RegSuccessActivity.this, MainActivity.class));
        finish();
    }
}
