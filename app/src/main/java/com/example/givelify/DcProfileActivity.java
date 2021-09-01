package com.example.givelify;

import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.givelify.Models.DonationCentre;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
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
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import static android.content.ContentValues.TAG;

public class DcProfileActivity extends AppCompatActivity {

    private Button updateBtn;
    private ImageView imageView;
    private ImageView mImage;
    private ProgressBar progBar;
    private Uri imageUri;
    private TextView mName,mEmail,mWebsite,mCity,mPhone,mDescription,mCategory,mPass,changePass;
    private String pass,date;
    private ImageButton mEditImage;
    private boolean profileUpdated;
    //default image
    private Uri defaultImageUri= Uri.parse("android.resource://com.example.givelify/drawable/charity.png");

    private final FirebaseDatabase db = FirebaseDatabase.getInstance();
    private final DatabaseReference dCentersRef = db.getReference().child("DCenters");
    private DatabaseReference current_user_db;
    private StorageReference reference =FirebaseStorage.getInstance().getReference().child("Profile Pictures");
    private FirebaseAuth mAuth=FirebaseAuth.getInstance();
    private StorageReference userPicRef;

    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dc_profile);
        current_user_db = dCentersRef.child(mAuth.getCurrentUser().getUid());
        FirebaseUser currentUser= mAuth.getCurrentUser();

        updateBtn = findViewById(R.id.btn_update);
        mEditImage = findViewById(R.id.edit_pic);
        imageView=findViewById(R.id.image);
        changePass=findViewById(R.id.change_password);
        mName=findViewById(R.id.et_edit_name);
        mEmail=findViewById(R.id.et_edit_email);
        mWebsite=findViewById(R.id.et_edit_website);
        mCity=findViewById(R.id.et_edit_city);
        mPhone=findViewById(R.id.et_edit_phone);
        mDescription=findViewById(R.id.et_edit_desc);
        mCategory=findViewById(R.id.et_edit_category);

        mEditImage.setOnClickListener(l-> {
            Intent galleryIntent = new Intent();
            galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
            galleryIntent.setType("image/*");
            startActivityForResult(galleryIntent,2);
        });
        updateBtn.setOnClickListener(v -> {
            if(imageUri !=null){
                //uploadToFirebase(imageUri);
                updateProfile();
                updateProfilePic(imageUri);
                uploadToFirebase(imageUri);
            }
            else {
                //image is not changed
                updateProfile();
            }
        });
        //Send Password Reset Link to Email
        changePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email= currentUser.getEmail();
                new AlertDialog.Builder(DcProfileActivity.this)
                        .setTitle("Send Password Reset Link")
                        .setMessage("Send a password reset link to your email: "+email)
                        .setIcon(android.R.drawable.ic_dialog_email)
                        .setPositiveButton(R.string.send, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {
                                mAuth.sendPasswordResetEmail(email);
                                Toast.makeText(DcProfileActivity.this, "Password Reset Link Sent to Email", Toast.LENGTH_SHORT).show();
                            }})
                        .setNegativeButton(R.string.cancel, null).show();
            }
        });

        updateUI();

    }

    private void updateProfile() {
        String web=mWebsite.getText().toString().trim();
        String name= mName.getText().toString().trim();
        String email= mEmail.getText().toString().trim();
        String city=mCity.getText().toString().trim();
        String category=mCategory.getText().toString().trim();
        String phone=mPhone.getText().toString().trim();
        String desc=mDescription.getText().toString().trim();

        DonationCentre dCenter= new DonationCentre(name,email,pass,desc,category,web,city,phone,false,date);
        current_user_db.setValue(dCenter).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<Void> task) {
                Snackbar mySnackbar = Snackbar.make(findViewById(R.id.prof_constraint_layout), R.string.updated, 1000);
                mySnackbar.show();

            }
        });

    }

    private void updateUI() {
        //get the donation center from firebase
        ValueEventListener userListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Donation center object and use the values to update the UI
                DonationCentre donationCentre = dataSnapshot.getValue(DonationCentre.class);
                mWebsite.setText(donationCentre.getWebsite());
                mName.setText(donationCentre.getName());
                mEmail.setText(donationCentre.getEmail());
                mCity.setText(donationCentre.getCity());
                mPhone.setText(donationCentre.getPhone());
                mDescription.setText(donationCentre.getDescription());
                mCategory.setText(donationCentre.getCategory());
                pass=donationCentre.getPass();
                date= donationCentre.getCreatedAt();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting donation center failed, log a message
                Log.w("TAG", "loadUser:onCancelled", databaseError.toException());
            }
        };
        current_user_db.addValueEventListener(userListener);
        userPicRef=reference.child(mAuth.getCurrentUser().getUid());
        userPicRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<Uri> task) {
                Picasso.get().load(task.getResult()).into(imageView);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {
                e.printStackTrace();
            }
        });
    }

    private void uploadToFirebase(Uri uri) {
        //StorageReference fileref = reference.child(mAuth.getCurrentUser().getUid()+"."+getFileExtension(uri));

        StorageReference fileref = reference.child(mAuth.getCurrentUser().getUid());
        fileref.putFile(uri).addOnSuccessListener(taskSnapshot -> fileref.getDownloadUrl().addOnSuccessListener(uri1 -> {
            Toast.makeText(DcProfileActivity.this, "Upload Successful", Toast.LENGTH_SHORT).show();
        })).addOnFailureListener(e -> {
            Toast.makeText(DcProfileActivity.this, "Upload Failed", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        });
    }

    private void updateProfilePic(Uri uri) {
        final FirebaseUser user = mAuth.getCurrentUser();
        assert user != null;
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setPhotoUri(uri)
                .build();

        user.updateProfile(profileUpdates)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "Donation Center profile updated.");
                        Toast.makeText(this, "Profile Updated", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private String getFileExtension(Uri uri) {
        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(uri));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode ==2 && resultCode == RESULT_OK && data != null){
        imageUri=data.getData();
        imageView.setImageURI(imageUri);
        }

    }
}
