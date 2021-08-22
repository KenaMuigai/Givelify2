package com.example.givelify;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.givelify.Adapters.DcAdapter;
import com.example.givelify.Models.DonationCentre;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    FirebaseUser user;
    private final FirebaseDatabase db = FirebaseDatabase.getInstance();
    private final DatabaseReference donationCenterRef= db.getReference().child("DCenters");
    private final DatabaseReference commonDonationItemsRef= db.getReference().child("Common Donation Items");
    private StorageReference reference = FirebaseStorage.getInstance().getReference();
    private final FirebaseAuth mAuth = FirebaseAuth.getInstance();

    RecyclerView rvDonationCentres;
    DcAdapter dcAdapter;
    ArrayList<DonationCentre> dcArrayList;
    ArrayList<String> itemArrayList;
    TextView tvUsername;
    String userName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        user= FirebaseAuth.getInstance().getCurrentUser();

        if (user == null) {
            // No user is signed in
            Intent i =new Intent(MainActivity.this, LoginActivity.class);
            startActivity(i);
            finish();
        }
        checkUserExistence();
        //create a list of the donation centres
        dcArrayList= new ArrayList<>();

        //item array list for the donation items of each donation centre. It is empty for each at the moment
        itemArrayList= new ArrayList<>();
        //get the toolbar
        Toolbar toolbar= findViewById(R.id.toolbar);
        toolbar.setOnMenuItemClickListener(item -> {
            int id = item.getItemId();

             if (id == R.id.logout) {
                Intent i =new Intent(MainActivity.this, LogoutActivity.class);
                startActivity(i);
                return true;
            }
            return false;
        });

        initializeData();
    }

    private void checkUserExistence() {
        //check for presence in realtime
        //implement the verification by admin
    }

    private void initializeData() {
        //get the username
        userName= user.getDisplayName()+" !";
        tvUsername=findViewById(R.id.user);
        tvUsername.setText(userName);
        // Check if user's email is verified
        boolean emailVerified = user.isEmailVerified();


        DonationCentre dcentre1= new DonationCentre("DonationCentre1","This is DonationCentre1",itemArrayList,"Category1","Nairobi",R.drawable.donations);
        DonationCentre dcentre2= new DonationCentre("DonationCentre2","This is DonationCentre2",itemArrayList,"Category2","Nairobi",R.drawable.donations);
        DonationCentre dcentre3= new DonationCentre("DonationCentre3","This is DonationCentre3",itemArrayList,"Category3","Nairobi",R.drawable.donations);

        dcArrayList.add(dcentre1);
        dcArrayList.add(dcentre2);
        dcArrayList.add(dcentre3);
        //add the list to its respective recyclerview in the main activity
        setRvDonationCentres(dcArrayList);


    //Get data from Firebase
//        donationCenterRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
//                for (DataSnapshot dataSnapshot: snapshot.getChildren()){
//                    DonationCentre donationCentre = dataSnapshot.getValue(DonationCentre.class);
//                    dcArrayList.add(donationCentre);
//                }
//                setRvDonationCentres(dcArrayList);
//            }
//            @Override
//            public void onCancelled(@NonNull @NotNull DatabaseError error) {
//
//            }
//        });

    }

    private void setRvDonationCentres(ArrayList<DonationCentre> dcArrayList){
        rvDonationCentres = findViewById(R.id.rv_donation_centres);

        //the recyclerview is set to horizontal so that the list goes horizontally to fit the layout
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this,RecyclerView.HORIZONTAL,false);
        rvDonationCentres.setLayoutManager(layoutManager);

        dcAdapter= new DcAdapter(dcArrayList, this);

        rvDonationCentres.setAdapter(dcAdapter);
    }
    ///not functioning
    private void getDcenters(){
        final DatabaseReference dcRef = db.getReference().child("DCenters");

        dcRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    // DonationCentre donationCentre = snapshot.getValue(DonationCentre.class);
                    String name= snapshot.child("name").getValue().toString();
                    String city= snapshot.child("city").getValue().toString();
                    String description= snapshot.child("description").getValue().toString();
                    DonationCentre donCenter= new DonationCentre(name,description,itemArrayList,"Category3",city,R.drawable.donations);
                    dcArrayList.add(donCenter);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

    }

}