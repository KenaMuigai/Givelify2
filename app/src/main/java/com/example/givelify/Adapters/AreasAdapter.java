package com.example.givelify.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.givelify.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

public class AreasAdapter extends RecyclerView.Adapter<AreasAdapter.Viewholder> {
    private String[] areasList;
    private Context context;
    private final FirebaseAuth mAuth=FirebaseAuth.getInstance();
    FirebaseUser currentUser= mAuth.getCurrentUser();
    private final DatabaseReference dbroot = FirebaseDatabase.getInstance().getReference();
    private final DatabaseReference dcPickupRef= dbroot.child("Dc_PickupDetails");
    private final DatabaseReference dcAreaRef= dbroot.child("Dc_Areas");
    Boolean isSelected;




    public AreasAdapter(String[] areasList, Context context) {
        this.areasList = areasList;
        this.context = context;
    }

    @NonNull
    @NotNull
    @Override
    public AreasAdapter.Viewholder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        return new AreasAdapter.Viewholder(LayoutInflater.from(context).inflate(R.layout.edit_areas_list_items,parent,false));

    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull AreasAdapter.Viewholder holder, int position) {
        String areas= areasList[position];
        Log.w("Areas Adapter","itemcount is"+areasList.length);
        holder.bindTo(areas);
    }

    @Override
    public int getItemCount() {
        return areasList.length;
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        private CheckBox cbArea;

        public Viewholder(@NonNull @NotNull View areasView) {
            super(areasView);
            cbArea=areasView.findViewById(R.id.cb_area);

        }
        public void bindTo(String areasTitle){
            cbArea.setText(areasTitle);
            dcPickupRef.child(currentUser.getUid()).child("Areas").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                    isSelected=(Boolean)snapshot.child(areasTitle).getValue();
                    if (isSelected){
                        cbArea.setChecked(true);
                    }
                }

                @Override
                public void onCancelled(@NonNull @NotNull DatabaseError error) {

                }
            });
            cbArea.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked){
                        dcPickupRef.child(currentUser.getUid()).child("Areas").child(areasTitle).setValue(true);
                        dcAreaRef.child("Areas").child(areasTitle).child(currentUser.getUid()).setValue(true);
                    }
                    else{
                        dcPickupRef.child(currentUser.getUid()).child("Areas").child(areasTitle).setValue(false);
                        dcAreaRef.child("Areas").child(areasTitle).child(currentUser.getUid()).setValue(false);
                    }
                }
            });
        }
    }
}
