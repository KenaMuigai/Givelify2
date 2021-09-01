package com.example.givelify;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.givelify.Adapters.ChooseAdapter;
import com.example.givelify.Models.DonationCentre;
import com.example.givelify.databinding.ActivityPickup2Binding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class PickupActivity2 extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityPickup2Binding binding;
    String county,area,address;
    private ArrayList<DonationCentre> dcArrayList;
    private ArrayList<String> dcUIDList;
    private ChooseAdapter dcAdapter;


    private final FirebaseDatabase db=FirebaseDatabase.getInstance();
    private final FirebaseAuth mAuth=FirebaseAuth.getInstance();
    FirebaseUser currentUser= mAuth.getCurrentUser();
    private final DatabaseReference dcPickupRef= db.getReference().child("Dc_PickupDetails");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityPickup2Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Intent intent= getIntent();
        county= intent.getStringExtra("COUNTY");
        area= intent.getStringExtra("AREA");
        address= intent.getStringExtra("ADDRESS");

        Toast.makeText(this, "The is the area selected is: "+area, Toast.LENGTH_SHORT).show();
       loadDonationCenters();

    }

    private void loadDonationCenters() {
        dcArrayList= new ArrayList<>();
        dcUIDList= new ArrayList<>();
        //get the donation centers with area
        if(area!=null) {
            dcPickupRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                        //get each dcenter as a datasnapshot. check if the area the user entered is accepted under the datasnapshot
                        Boolean areaAccepts = (Boolean) dataSnapshot.child("Areas").child(area).getValue();
                        if (areaAccepts) {
                            String dcUID = dataSnapshot.getKey();
                            if (dcUID != null) {
                                Log.d("PickupAct2", "The dcUID is " + dcUID);
                                dcUIDList.add(dcUID);

                            }
                        }
                    }
                    setRvDonationCentres(dcUIDList);
                }

                @Override
                public void onCancelled(@NonNull @NotNull DatabaseError error) {

                }
            });
        }
    }

    private void setRvDonationCentres(ArrayList<String> dcUIDList) {
        RecyclerView rvDonationCentres = binding.rvDcChoose;

        //the recyclerview is set to horizontal so that the list goes horizontally to fit the layout
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(PickupActivity2.this,RecyclerView.HORIZONTAL,false);
        rvDonationCentres.setLayoutManager(layoutManager);

        dcAdapter= new ChooseAdapter(dcUIDList,PickupActivity2.this,county,area,address);

        rvDonationCentres.setAdapter(dcAdapter);
    }
}