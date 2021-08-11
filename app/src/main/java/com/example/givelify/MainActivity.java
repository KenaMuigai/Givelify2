package com.example.givelify;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.givelify.Adapters.DcAdapter;
import com.example.givelify.Models.DonationCentre;
import com.example.givelify.Models.DonationItem;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    RecyclerView rvDonationCentres;
    DcAdapter dcAdapter;
    ArrayList<DonationCentre> dcArrayList;
    ArrayList<DonationItem> itemArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (user == null) {
            // No user is signed in
            Intent i =new Intent(MainActivity.this, LoginActivity.class);
            startActivity(i);
            finish();
        }

        initializeData();
        //get the toolbar
        Toolbar toolbar= findViewById(R.id.toolbar);
        toolbar.setOnMenuItemClickListener(item -> {
            int id = item.getItemId();

            if (id == R.id.view_profile) {
                Intent i =new Intent(MainActivity.this, DcProfileActivity.class);
                startActivity(i);
                return true;
            }
            else if (id == R.id.logout) {
                Intent i =new Intent(MainActivity.this, LogoutActivity.class);
                startActivity(i);
                return true;
            }
            return false;
        });

    }

    private void initializeData() {
        //create a list of the donation centres
        dcArrayList= new ArrayList<>();

        //item array list for the donation items of each donation centre. It is empty for each at the moment
        itemArrayList= new ArrayList<>();

        DonationCentre dcentre1= new DonationCentre("DonationCentre1","This is DonationCentre1",itemArrayList,"Category1","Nairobi",R.drawable.donations);
        DonationCentre dcentre2= new DonationCentre("DonationCentre2","This is DonationCentre2",itemArrayList,"Category2","Nairobi",R.drawable.donations);
        DonationCentre dcentre3= new DonationCentre("DonationCentre3","This is DonationCentre3",itemArrayList,"Category3","Nairobi",R.drawable.donations);

        dcArrayList.add(dcentre1);
        dcArrayList.add(dcentre2);
        dcArrayList.add(dcentre3);

        //add the list to its respective recyclerview in the main activity
        setRvDonationCentres(dcArrayList);

    }
    private void setRvDonationCentres(ArrayList<DonationCentre> dcArrayList){
        rvDonationCentres = findViewById(R.id.rv_donation_centres);

        //the recyclerview is set to horizontal so that the list goes horizontally to fit the layout
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this,RecyclerView.HORIZONTAL,false);
        rvDonationCentres.setLayoutManager(layoutManager);

        dcAdapter= new DcAdapter(dcArrayList, this);

        rvDonationCentres.setAdapter(dcAdapter);
    }
}