package com.example.givelify;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.givelify.databinding.ActivityRequestSuccessBinding;

public class RequestSuccessActivity extends AppCompatActivity {
    private ActivityRequestSuccessBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRequestSuccessBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnDone.setOnClickListener(v -> {
            Intent intent= new Intent(RequestSuccessActivity.this,MainActivity.class);
            startActivity(intent);
        });


    }
}