package com.example.givelify;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.givelify.databinding.ActivityConfirmDetailsBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class ConfirmDetailsActivity extends AppCompatActivity {
    private ActivityConfirmDetailsBinding binding;
    private final FirebaseDatabase db=FirebaseDatabase.getInstance();
    private final FirebaseAuth mAuth=FirebaseAuth.getInstance();
    FirebaseUser currentUser= mAuth.getCurrentUser();
    private final DatabaseReference dcPickupRef= db.getReference().child("Dc_PickupDetails");
    private final DatabaseReference dcRequestsRef= db.getReference().child("Dc_PickupRequests");
    private final DatabaseReference requestsRef= db.getReference().child("PickupRequests");
    private final DatabaseReference userRequestsRef= db.getReference().child("Users_PickupRequests");
    private final DatabaseReference userRef= db.getReference().child("Users");
    private final DatabaseReference dcRef= db.getReference().child("DCenters");



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityConfirmDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        updateUserDetails();
        updateRequestDetails();
        updateDCenterDetails();

        binding.btnConfirmRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveRequest();
            }
        });
        binding.btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCancelDialog();
            }
        });

    }

    private void showCancelDialog() {
        new AlertDialog.Builder(ConfirmDetailsActivity.this)
                .setTitle("Cancel Pickup Request")
                .setMessage("Are you sure you want to cancel your pickup request?")
                .setIcon(android.R.drawable.ic_delete)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        Toast.makeText(ConfirmDetailsActivity.this, "Pickup Request Cancelled", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(ConfirmDetailsActivity.this,MainActivity.class);
                        startActivity(intent);
                    }})
                .setNegativeButton(R.string.cancel, null).show();
    }

    private void updateDCenterDetails() {
        Intent i = getIntent();
        String dcenterUID =i.getStringExtra("dcenterUID");
        dcRef.child(dcenterUID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                String dcName= (String) snapshot.child("name").getValue();
                String dcPhone =(String) snapshot.child("phone").getValue();

                binding.tvDcName.setText(dcName);
                binding.tvDcPhone.setText(dcPhone);
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }

    private void updateRequestDetails() {
        Intent i = getIntent();
        String date=i.getStringExtra("date");
        String county=i.getStringExtra("county");
        String area=i.getStringExtra("area");
        String address=i.getStringExtra("address");
        String item=i.getStringExtra("item");

        binding.tvItemName.setText(item);
        binding.tvPickupDate.setText(date);
        binding.tvCounty.setText(county);
        binding.tvLocation.setText(area);
        binding.tvUserAddress.setText(address);
    }

    private void saveRequest() {
    Intent i = getIntent();
        String uid=i.getStringExtra("UID");
        String dcenterUID =i.getStringExtra("dcenterUID");
        String date=i.getStringExtra("date");
        String county=i.getStringExtra("county");
        String area=i.getStringExtra("area");
        String address=i.getStringExtra("address");
        String item=i.getStringExtra("item");
        String currentDate;

        //get current date
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
        currentDate = df.format(c);

        userRef.child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                //get the phone number of the user to add to the pickup request
                String phone = (String)snapshot.child("phone").getValue();
                String email= (String)snapshot.child("email").getValue();

                //add the pickup to the requests ref under dcenter id .put the request under pending
                HashMap<String,String> hashMap = new HashMap<>();
                hashMap.put("uid",currentUser.getUid());
                hashMap.put("dcenterUID",dcenterUID);
                hashMap.put("pickupDate",date);
                hashMap.put("county",county);
                hashMap.put("area",area);
                hashMap.put("address",address);
                hashMap.put("item",item);
                hashMap.put("phone",phone);
                hashMap.put("email",email);
                hashMap.put("createdAt",currentDate);
                hashMap.put("lastModified",currentDate);

                //get the requests unique key
                String requestID=dcRequestsRef.child("Pending Requests").push().getKey();
                assert requestID != null;
                requestsRef.child("Pending Requests").child(requestID).setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<Void> task) {
                        //put the key into the users and dcenters ref
                        userRequestsRef.child(currentUser.getUid()).child("Pending Requests").child(requestID).setValue(hashMap);
                        dcRequestsRef.child(dcenterUID).child("Pending Requests").child(requestID).setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull @NotNull Task<Void> task) {
                                Intent intent= new Intent(ConfirmDetailsActivity.this,RequestSuccessActivity.class);
                                startActivity(intent);
                            }
                        });
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });


    }
    private void updateUserDetails() {
        //get extra user info
        String userId= currentUser.getUid();
        String userEmail= currentUser.getEmail();
        String userName= currentUser.getDisplayName();

        userRef.child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                String phone=(String)snapshot.child("phone").getValue();
                binding.tvUserName.setText(userName);
                binding.tvUserEmail.setText(userEmail);
                binding.tvUserPhone.setText(phone);

            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }
}