package com.example.givelify;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.givelify.Adapters.AreasAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class DcAreaActivity extends AppCompatActivity {
    private final FirebaseDatabase db=FirebaseDatabase.getInstance();
    private final FirebaseAuth mAuth=FirebaseAuth.getInstance();
    FirebaseUser currentUser= mAuth.getCurrentUser();
    private final DatabaseReference donationCenterRef= db.getReference().child("DCenters");
    private final DatabaseReference dcPickupRef= db.getReference().child("Dc_PickupDetails");
    private final DatabaseReference dcAreaRef= db.getReference().child("Dc_Area");

    private String[] myareasList;
    private ArrayList<Integer> selectedAreasList;
    private String city;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_areas_list);
        setRvAreas();

    }
    public void setRvAreas() {
        RecyclerView rvAreasList=findViewById(R.id.rv_areas);
        donationCenterRef.child(currentUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                city= snapshot.child("city").getValue().toString();
                Toast.makeText(DcAreaActivity.this, "City is "+city, Toast.LENGTH_SHORT).show();
                switch (city){
                    case "Nairobi":
                        myareasList=DcAreaActivity.this.getResources().getStringArray(R.array.nairobi_spinner);
                        break;
                    case "Nakuru":
                        myareasList=DcAreaActivity.this.getResources().getStringArray(R.array.nakuru_spinner);
                        break;
                    case "Mombasa":
                        myareasList=DcAreaActivity.this.getResources().getStringArray(R.array.mombasa_spinner);
                        break;
                    case "Kisumu":
                        myareasList=DcAreaActivity.this.getResources().getStringArray(R.array.kisumu_spinner);
                        break;
                    case "Kiambu":
                        myareasList=DcAreaActivity.this.getResources().getStringArray(R.array.kiambu_spinner);
                        break;
                    default:
                        break;
                }
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(DcAreaActivity.this,RecyclerView.VERTICAL,false);
                rvAreasList.setLayoutManager(layoutManager);
                AreasAdapter mAdapter= new AreasAdapter(myareasList,DcAreaActivity.this);
                rvAreasList.setAdapter(mAdapter);
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }

}
