package com.example.givelify;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.givelify.databinding.ActivityDcDonationItemsBinding;
import com.example.givelify.ui.donationitems.SectionsPagerAdapter;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DcDonationItemsActivity extends AppCompatActivity {

    private ActivityDcDonationItemsBinding binding;
    String type="";
    private final DatabaseReference dbroot = FirebaseDatabase.getInstance().getReference();
    private final FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser currentUser = mAuth.getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityDcDonationItemsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = binding.viewPager;
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = binding.tabs;
        tabs.setupWithViewPager(viewPager);
        FloatingActionButton fab = binding.fabNewItem;


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //open a dialog to create a new item
                showBottomSheetDialog();
            }
        });
    }

    private void showBottomSheetDialog() {
        //declare the bottomsheetdialog
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(DcDonationItemsActivity.this);
        bottomSheetDialog.setContentView(R.layout.new_item_bottom_dialog);
        final Button addButton = bottomSheetDialog.findViewById(R.id.btn_add_item);
        final EditText etItemName = bottomSheetDialog.findViewById(R.id.et_item_name);
        final RadioButton radioAccepted = bottomSheetDialog.findViewById(R.id.radio_accepted);
        final RadioButton radioProhibited = bottomSheetDialog.findViewById(R.id.radio_prohibited);

        radioAccepted.setOnClickListener(v -> {
            onRadioButtonClicked(v);
        });
        radioProhibited.setOnClickListener(v -> {
            onRadioButtonClicked(v);
        });
        addButton.setOnClickListener(v -> {
            String itemName = etItemName.getText().toString().trim();
            addItem(itemName, type);
            bottomSheetDialog.dismiss();
        });
        bottomSheetDialog.show();
    }

    private void addItem(String itemName, String type) {
        //ADD ITEM TO DB USING TYPE AS REF
        if (type.equals("")||itemName.equals("")){
            Toast.makeText(this, "Please complete the fields", Toast.LENGTH_SHORT).show();

        }else {
            dbroot.child(type).child(itemName).child(currentUser.getUid()).setValue(true).addOnCompleteListener(task -> {
                Toast.makeText(DcDonationItemsActivity.this, "Item Added Successfully", Toast.LENGTH_SHORT).show();
            });
        }
    }


    private void onRadioButtonClicked(View v) {
        // Is the button now checked?
        boolean checked = ((RadioButton)v).isChecked();

        // Check which radio button was clicked
        switch(v.getId()) {
            case R.id.radio_accepted:
                if (checked)
                    type="Dc_AcceptedItems";
                break;
            case R.id.radio_prohibited:
                if (checked)
                    type="Dc_ProhibitedItems";
                break;
            default:
                break;
        }
    }
}