package com.example.givelify;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

public class SplashScreenActivity extends AppCompatActivity {
    private static final long SPLASH_SCREEN_TIME_OUT = 2000;
    private final FirebaseDatabase db = FirebaseDatabase.getInstance();
    private final DatabaseReference dCentersRef = db.getReference().child("DCenters");
    private FirebaseAuth mAuth=FirebaseAuth.getInstance();
    FirebaseUser currentUser= mAuth.getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        if (currentUser==null){
            new Handler().postDelayed(() -> {
                Intent i =new Intent(SplashScreenActivity.this,
                        LoginActivity.class);
                startActivity(i);
                //the current activity will get finished.
            }, SPLASH_SCREEN_TIME_OUT);
        }
        else {
            dCentersRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                    if (snapshot.hasChild(currentUser.getUid())){
                        //the current user is a donation center
                        new Handler().postDelayed(() -> {
                            Intent i =new Intent(SplashScreenActivity.this,
                                    HomeActivity.class);
                            startActivity(i);
                            //the current activity will get finished.
                        }, SPLASH_SCREEN_TIME_OUT);
                    }else{
                        //the current user is not a donation center
                        new Handler().postDelayed(() -> {
                            Intent i =new Intent(SplashScreenActivity.this,
                                    MainActivity.class);
                            startActivity(i);
                            //the current activity will get finished.
                        }, SPLASH_SCREEN_TIME_OUT);
                    }
                }

                @Override
                public void onCancelled(@NonNull @NotNull DatabaseError error) {

                }
            });

        }

    }
}