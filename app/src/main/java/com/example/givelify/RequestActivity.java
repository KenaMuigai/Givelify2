package com.example.givelify;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.ui.AppBarConfiguration;

import com.example.givelify.databinding.ActivityRequestBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class RequestActivity extends AppCompatActivity {
    private AppBarConfiguration appBarConfiguration;
    private ActivityRequestBinding binding;

    String dcUID;
    private final FirebaseDatabase db=FirebaseDatabase.getInstance();
    private final FirebaseAuth mAuth=FirebaseAuth.getInstance();
    FirebaseUser currentUser= mAuth.getCurrentUser();
    private final DatabaseReference dcPickupRef= db.getReference().child("Dc_PickupDetails");
    private final DatabaseReference dcRequestsRef= db.getReference().child("Dc_PickupRequests");
    private final DatabaseReference requestsRef= db.getReference().child("PickupRequests");
    private final DatabaseReference userRequestsRef= db.getReference().child("User_PickupRequests");



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRequestBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Intent i = getIntent();
        String dcenterUID = i.getStringExtra("DcUID");
        String county = i.getStringExtra("COUNTY");
        String area = i.getStringExtra("AREA");
        String address = i.getStringExtra("ADDRESS");
        //Toast.makeText(this, "DCUID:"+dcenterUID+"\nCounty:"+county+"\nArea:"+area+"\nAddress"+address, Toast.LENGTH_SHORT).show();


        Calendar now = Calendar.getInstance();
        DatePicker calendarView= findViewById(R.id.calendarView);
        Long currentDate = Calendar.getInstance().getTimeInMillis();
        calendarView.setMinDate(currentDate);
        calendarView.setBackgroundColor(Color.WHITE);
        Button btnConfirm = findViewById(R.id.btn_confirm);
        EditText etItem= findViewById(R.id.et_donation_items);

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // get the values for day of month , month and year from a date picker
                String date = calendarView.getDayOfMonth()+"-"+(calendarView.getMonth()+1)+"-"+calendarView.getYear();
                Intent i = new Intent(RequestActivity.this,ConfirmDetailsActivity.class);
                i.putExtra("UID",currentUser.getUid());
                i.putExtra("dcenterUID",dcenterUID);
                i.putExtra("date",date);
                i.putExtra("county",county);
                i.putExtra("area",area);
                i.putExtra("address",address);
                i.putExtra("item",etItem.getText().toString().trim());
                startActivity(i);

            }
        });


    }

}