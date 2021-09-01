package com.example.givelify;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.ui.AppBarConfiguration;

import com.example.givelify.databinding.ActivityDcPickupBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class DcPickupActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityDcPickupBinding binding;
    private final FirebaseDatabase db=FirebaseDatabase.getInstance();
    private final FirebaseAuth mAuth=FirebaseAuth.getInstance();
    FirebaseUser currentUser= mAuth.getCurrentUser();
    private final DatabaseReference donationCenterRef= db.getReference().child("DCenters");
    private final DatabaseReference dcPickupRef= db.getReference().child("Dc_PickupDetails");
    private String[] myareasList;
    private ArrayList<Integer> selectedAreasList;
    private String city;
    Boolean sun,mon,tue,wed,thur,fri,sat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityDcPickupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.btnEditAreas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(DcPickupActivity.this, DcAreaActivity.class);
                startActivity(intent);
            }
        });
        initializeUI();
        setPickupDays();
        setGuidelines();

    }

    private void initializeUI() {
        donationCenterRef.child(currentUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                city=snapshot.child("city").getValue().toString();
                binding.dcPickupCity.setText(city);
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
        dcPickupRef.child(currentUser.getUid()).child("Days").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                sun= (Boolean) snapshot.child("Sunday").getValue();
                mon=(Boolean)snapshot.child("Monday").getValue();
                tue=(Boolean)snapshot.child("Tuesday").getValue();
                wed=(Boolean)snapshot.child("Wednesday").getValue();
                thur=(Boolean)snapshot.child("Thursday").getValue();
                fri=(Boolean)snapshot.child("Friday").getValue();
                sat=(Boolean)snapshot.child("Saturday").getValue();

                binding.cbSunday.setChecked(sun);
                binding.cbMonday.setChecked(mon);
                binding.cbTuesday.setChecked(tue);
                binding.cbWednesday.setChecked(wed);
                binding.cbThursday.setChecked(thur);
                binding.cbFriday.setChecked(fri);
                binding.cbSaturday.setChecked(sat);
            }


            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

    }


    private void setPickupDays() {
        binding.cbSunday.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    dcPickupRef.child(currentUser.getUid()).child("Days").child("Sunday").setValue(true);
                }
                else {
                    dcPickupRef.child(currentUser.getUid()).child("Days").child("Sunday").setValue(false);

                }
            }
        });
        binding.cbMonday.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    dcPickupRef.child(currentUser.getUid()).child("Days").child("Monday").setValue(true);
                }
                else {
                    dcPickupRef.child(currentUser.getUid()).child("Days").child("Monday").setValue(false);

                }
            }
        });
        binding.cbTuesday.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    dcPickupRef.child(currentUser.getUid()).child("Days").child("Tuesday").setValue(true);
                }
                else {
                    dcPickupRef.child(currentUser.getUid()).child("Days").child("Tuesday").setValue(false);

                }
            }
        });
        binding.cbWednesday.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked){
                dcPickupRef.child(currentUser.getUid()).child("Days").child("Wednesday").setValue(true);
            }
            else {
                dcPickupRef.child(currentUser.getUid()).child("Days").child("Wednesday").setValue(false);

            }
        });
        binding.cbThursday.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked){
                dcPickupRef.child(currentUser.getUid()).child("Days").child("Thursday").setValue(true);
            }
            else {
                dcPickupRef.child(currentUser.getUid()).child("Days").child("Thursday").setValue(false);

            }
        });
        binding.cbFriday.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked){
                dcPickupRef.child(currentUser.getUid()).child("Days").child("Friday").setValue(true);
            }
            else {
                dcPickupRef.child(currentUser.getUid()).child("Days").child("Friday").setValue(false);

            }
        });
        binding.cbSaturday.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked){
                dcPickupRef.child(currentUser.getUid()).child("Days").child("Saturday").setValue(true);
            }
            else {
                dcPickupRef.child(currentUser.getUid()).child("Days").child("Saturday").setValue(false);

            }
        });
    }
    private void setGuidelines() {
        dcPickupRef.child(currentUser.getUid()).child("Guidelines").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                String guidelines=snapshot.getValue().toString();
                if (!guidelines.equals("Default")){
                    binding.etGuidelines.setText(guidelines);
                }
            }
            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

        binding.btnSaveGuidelines.setOnClickListener(v -> {
           String guidelines= binding.etGuidelines.getText().toString().trim();
           if (guidelines.equals("")){
               Toast.makeText(DcPickupActivity.this, "Enter Guidelines", Toast.LENGTH_SHORT).show();
           }
           else {
               dcPickupRef.child(currentUser.getUid()).child("Guidelines").setValue(guidelines);
               Toast.makeText(DcPickupActivity.this, "Guidelines Saved", Toast.LENGTH_SHORT).show();
           }
       });
    }

}