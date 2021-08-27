package com.example.givelify.ui.donationitems;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class ProhibitedViewModel extends ViewModel {
    private MutableLiveData<ArrayList<String>> itemList = new MutableLiveData<>();
    private final FirebaseDatabase db=FirebaseDatabase.getInstance();
    private final FirebaseAuth mAuth=FirebaseAuth.getInstance();
    FirebaseUser currentUser= mAuth.getCurrentUser();
    private final DatabaseReference prohibitedRef= db.getReference().child("Dc_ProhibitedItems");

    private ArrayList<String> itemArrayList;

    public MutableLiveData<ArrayList<String>> getItemList() {
        prohibitedRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                itemArrayList= new ArrayList<>();

                for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                    if (dataSnapshot.hasChild(currentUser.getUid())){
                        itemArrayList.add(dataSnapshot.getKey());
                    }
                }
                itemList.postValue(itemArrayList);
            }
            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

        return itemList;
    }
}