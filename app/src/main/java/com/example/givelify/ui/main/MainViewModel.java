package com.example.givelify.ui.main;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.givelify.Models.DonationCentre;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class MainViewModel extends ViewModel {

    private MutableLiveData<String> username,userCity;
    private final FirebaseDatabase db=FirebaseDatabase.getInstance();
    private final FirebaseAuth mAuth=FirebaseAuth.getInstance();
    FirebaseUser currentUser= mAuth.getCurrentUser();
    private final DatabaseReference donationCenterRef= db.getReference().child("DCenters");
    private final DatabaseReference userRef= db.getReference().child("Users");



    private MutableLiveData<ArrayList<DonationCentre>> dcMutableArrayList;
    private MutableLiveData<ArrayList<String>> dcMutableUserIDList;
    private ArrayList<DonationCentre> dcArrayList;
    private ArrayList<String> dcUserIDList;
    String city;


    public MainViewModel() {
        if(currentUser!=null){
            String displayName =currentUser.getDisplayName();
            username = new MutableLiveData<>();
            userCity= new MutableLiveData<>();
            username.setValue(displayName);
        }
    }

    public LiveData<String> getUsername() {
        return username;
    }
    public LiveData<ArrayList<DonationCentre>> getDonationCentersList(){
        if (dcMutableArrayList==null){
            dcMutableArrayList = new MutableLiveData<>();
            loadDonationCenters();
        }
        return dcMutableArrayList;
    }

    private void loadDonationCenters() {
        //get the donation centers
        donationCenterRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dcArrayList= new ArrayList<>();
                for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                    DonationCentre donationCentre = dataSnapshot.getValue(DonationCentre.class);
                    assert donationCentre != null;
                    if (donationCentre.getCity().equals(city)) {
                        dcArrayList.add(donationCentre);
                    }
                }
                dcMutableArrayList.postValue(dcArrayList);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public LiveData<ArrayList<String>> getUserIDList(){
        if (dcMutableUserIDList==null){
            dcMutableUserIDList= new MutableLiveData<>();
            loadUserIDs();
        }
        return dcMutableUserIDList;
    }

    private void loadUserIDs() {
        //get the donation centers
        donationCenterRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dcUserIDList= new ArrayList<>();
                for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                    DonationCentre donationCentre = dataSnapshot.getValue(DonationCentre.class);
                    String donationCenterUID=dataSnapshot.getKey();

                    if (donationCentre.getCity().equals(city)) {
                        dcUserIDList.add(donationCenterUID);
                    }
                }
                dcMutableUserIDList.postValue(dcUserIDList);

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public LiveData<String> getUserCity(){
        donationCenterRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if(snapshot.hasChild(currentUser.getUid())){
                    city=snapshot.child(currentUser.getUid()).child("city").getValue().toString();
                    userCity.setValue(city);
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if(snapshot.hasChild(currentUser.getUid())){
                    city=snapshot.child(currentUser.getUid()).child("city").getValue().toString();
                    userCity.setValue(city);
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

        return userCity;
    }

}