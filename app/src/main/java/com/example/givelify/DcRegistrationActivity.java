package com.example.givelify;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.givelify.Models.DonationCentre;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class DcRegistrationActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener{
    private EditText mName, mEmail, mPass,mConfirmPass,mDescription,mPhone,mWebsite;
    private Uri imageUri= Uri.parse("android.resource://com.example.givelify/"+R.mipmap.default_profile_image);
    private Spinner citySpinner, categorySpinner;
    private String mCategorySpinnerLabel,mCitySpinnerLabel;
    private ArrayList<String> donationItemsList;

    private final FirebaseDatabase db = FirebaseDatabase.getInstance();
    private final DatabaseReference root = db.getReference().child("DCenters");
    private final DatabaseReference dbroot = db.getReference();
    private final DatabaseReference cityRef = db.getReference().child("Dc_Cities");
    private final DatabaseReference categoryRef = db.getReference().child("Dc_Categories");
    private StorageReference reference = FirebaseStorage.getInstance().getReference();
    private final FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseAuth.AuthStateListener mAuthListener;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dc_registration);

        mName = findViewById(R.id.reg_name);
        mEmail= findViewById(R.id.email_reg);
        mPass= findViewById(R.id.reg_password);
        mConfirmPass= findViewById(R.id.reg_confirm_password);
        mDescription= findViewById(R.id.reg_description);
        Button mBtn = findViewById(R.id.sign_up_btn);
        categorySpinner=findViewById(R.id.spinner_categories);
        citySpinner=findViewById(R.id.spinner_cities);
        mPhone = (EditText) findViewById(R.id.phone_reg);
        mWebsite = (EditText) findViewById(R.id.website_reg);

        mBtn.setOnClickListener(v -> {
            createUser();
        });
        mAuthListener = firebaseAuth -> {
            FirebaseUser user = firebaseAuth.getCurrentUser();
            if (user!=null){
                System.out.println("User logged in");
            }
            else{
                System.out.println("User not logged in");
            }
        };
        categorySpinner = (Spinner) findViewById(R.id.spinner_categories);
        ArrayAdapter<CharSequence> adapter= ArrayAdapter.createFromResource(this,R.array.categories_spinner, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(adapter);
        categorySpinner.setOnItemSelectedListener(this);

        citySpinner = (Spinner) findViewById(R.id.spinner_cities);
        ArrayAdapter<CharSequence> cityAdapter= ArrayAdapter.createFromResource(this,R.array.cities_spinner, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        citySpinner.setAdapter(cityAdapter);
        citySpinner.setOnItemSelectedListener(this);
    }
    private void createUser() {
        String email= mEmail.getText().toString();
        String pass= mPass.getText().toString();
        String confirmPass= mConfirmPass.toString();

        if (!pass.isEmpty() && !confirmPass.isEmpty()&& !email.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            mAuth.createUserWithEmailAndPassword(email,pass).addOnSuccessListener(task -> {
                Toast.makeText(DcRegistrationActivity.this, "Registered Successfully!", Toast.LENGTH_SHORT).show();
                saveUserData();
                }).addOnFailureListener(e -> {
                Toast.makeText(DcRegistrationActivity.this, "Registration Error!!", Toast.LENGTH_SHORT).show();
                Log.w(TAG, "createUserWithEmail:failure", e);
            });

        } else if (email.isEmpty()) {
            mEmail.setError("Empty Fields Are Not Allowed");
        }else if (pass.isEmpty()) {
            mPass.setError("Empty Fields Are Not Allowed");
        } else if (confirmPass.isEmpty()) {
            mConfirmPass.setError("Empty Fields Are Not Allowed");
        }else {
            mEmail.setError("Please Enter Correct Email");
        }

    }
    private void saveUserData(){
        FirebaseUser user = mAuth.getCurrentUser();
        assert user != null;
        String user_id = mAuth.getCurrentUser().getUid();
        DatabaseReference current_user_db = root.child(user_id);

        String email= mEmail.getText().toString().trim();
        String pass= mPass.getText().toString().trim();
        String desc= mDescription.getText().toString().trim();
        String name= mName.getText().toString().trim();
        String phone= mPhone.getText().toString().trim();
        String web= mWebsite.getText().toString().trim();

        if (web.equals("")){
            web="www.defaultwebsite.com";
        }
        //add the user's info to the realtime database
        DonationCentre dCenter= new DonationCentre(name,email,pass,desc,mCategorySpinnerLabel,web,mCitySpinnerLabel,phone,false);
        current_user_db.setValue(dCenter).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<Void> task) {
                Toast.makeText(DcRegistrationActivity.this, "User Saved Successfully", Toast.LENGTH_SHORT).show();

                //update the user's profile with their display name and default image
                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                        .setDisplayName(name)
                        .setPhotoUri(imageUri)
                        .build();

                user.updateProfile(profileUpdates)
                        .addOnCompleteListener(task1 -> {
                            if (task1.isSuccessful()) {
                                Log.d(TAG, "Donation Center profile updated.");
                                Toast.makeText(DcRegistrationActivity.this, "Profile Updated Successfully", Toast.LENGTH_SHORT).show();

                                //send the user an email verification
                                sendEmailVerification();
                            }

                        });
                //add default accepted donation items
                addDefaultDonationItems();

                //add uderid to the cityref and categoryref under what they selected
                cityRef.child(mCitySpinnerLabel).child(user_id).setValue(true);
                categoryRef.child(mCategorySpinnerLabel).child(user_id).setValue(true);
            }
        });

    }
    private void sendEmailVerification() {
        // Send verification email
        final FirebaseUser user = mAuth.getCurrentUser();
        assert user != null;
        user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<Void> task) {
                Toast.makeText(DcRegistrationActivity.this, "Emailed Successfully", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void addDefaultDonationItems() {
        String user_id =mAuth.getCurrentUser().getUid();
        DatabaseReference donationItemsRef = db.getReference().child("CommonDonationItems");
        DatabaseReference prohibitedItemsRef = db.getReference().child("Prohibited Items");

        //iterate through all the children of the donationItemsRef and add the userid of the donation center and set it to true
        donationItemsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                    String key= dataSnapshot.getKey();
                    String item = dataSnapshot.getValue(String.class);
                    Log.d("TAG", key + item);
                    //add the users id under the item in question.So when a user removes it,their uid is removed from the ref
                    dbroot.child("Dc_AcceptedItems").child(item).child(user_id).setValue(true).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull @NotNull Task<Void> task) {

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull @NotNull Exception e) {
                            e.printStackTrace();
                        }
                    });
                }
            }
            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
                Log.d("TAG","DB ERROR!!!!!!!!!!!!!!!!");
            }
        });
        prohibitedItemsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                    String key= dataSnapshot.getKey();
                    String item = dataSnapshot.getValue(String.class);
                    Log.d("TAG", key + item);
                    //add the users id under the item in question.So when a user removes it,their uid is removed from the ref
                    dbroot.child("Dc_ProhibitedItems").child(item).child(user_id).setValue(true).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull @NotNull Task<Void> task) {
                            //go to the next activity
                            Intent regSuccessIntent= new Intent(DcRegistrationActivity.this,RegSuccessActivity.class);
                            startActivity(regSuccessIntent);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull @NotNull Exception e) {
                            e.printStackTrace();
                        }
                    });
                }
            }
            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
                Log.d("TAG","DB ERROR!!!!!!!!!!!!!!!!");
            }
        });


    }

    public void onStart(){
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);

    }
    public void onStop(){
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);

        }
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch(parent.getId()) {
            case R.id.spinner_categories:
                mCategorySpinnerLabel = parent.getItemAtPosition(position).toString();
                break;
            case R.id.spinner_cities:
                mCitySpinnerLabel = parent.getItemAtPosition(position).toString();
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
