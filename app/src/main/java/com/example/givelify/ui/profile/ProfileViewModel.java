package com.example.givelify.ui.profile;

import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.givelify.Models.DonationCentre;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.jetbrains.annotations.NotNull;

public class ProfileViewModel extends ViewModel {
    DatabaseReference root;
    DatabaseReference current_user_db;
    private final FirebaseDatabase db;
    private final FirebaseAuth mAuth=FirebaseAuth.getInstance();
    FirebaseUser currentUser= mAuth.getCurrentUser();
    private StorageReference reference = FirebaseStorage.getInstance().getReference().child("Profile Pictures");
    private StorageReference imageRef;


    private final MutableLiveData<String> userName,email,userID,category,city,website,description,phone;
    private final MutableLiveData<Uri> profileImage;

    public ProfileViewModel() {
        db = FirebaseDatabase.getInstance();
        root = db.getReference().child("DCenters");
        current_user_db = root.child(mAuth.getCurrentUser().getUid());
        imageRef=reference.child(mAuth.getCurrentUser().getUid());

        userName = new MutableLiveData<>();
        email = new MutableLiveData<>();
        userID = new MutableLiveData<>();
        category = new MutableLiveData<>();
        city = new MutableLiveData<>();
        website = new MutableLiveData<>();
        description = new MutableLiveData<>();
        phone = new MutableLiveData<>();
        profileImage= new MutableLiveData<>();

        userName.setValue(currentUser.getDisplayName());
        email.setValue(currentUser.getEmail());
        userID.setValue(currentUser.getUid());

        //profileImage.setValue(currentUser.getPhotoUrl());
        imageRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<Uri> task) {
                profileImage.setValue(task.getResult());
            }
        });

        ValueEventListener userListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Donation center object and use the values to update the UI
                DonationCentre donationCentre = dataSnapshot.getValue(DonationCentre.class);
                String mWeb = donationCentre.getWebsite();
                String mCity = donationCentre.getCity();
                String mDesc = donationCentre.getDescription();
                String mCategory = donationCentre.getCategory();
                String mPhone = donationCentre.getPhone();

                category.setValue(mCategory);
                city.setValue(mCity);
                website.setValue(mWeb);
                description.setValue(mDesc);
                phone.setValue(mPhone);

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting donation center failed, log a message
                Log.w("TAG", "loadUser:onCancelled", databaseError.toException());
            }
        };
        current_user_db.addValueEventListener(userListener);


    }

    public LiveData<String> getUsername() {
        return userName;
    }
    public LiveData<String> getEmail() {
        return email;
    }
    public LiveData<Uri> getProfileImage() {
        return profileImage;
    }
    public LiveData<String> getCategory() {
        return category;
    }
    public LiveData<String> getDescription() {
        return description;
    }
    public LiveData<String> getCity() {
        return city;
    }
    public LiveData<String> getWeb() {
        return website;
    }
    public LiveData<String> getPhone() {
        return phone;
    }
}
