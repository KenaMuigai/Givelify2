package com.example.givelify.Adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.givelify.DonationCentreActivity;
import com.example.givelify.Models.DonationCentre;
import com.example.givelify.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class HomeDcAdapter extends RecyclerView.Adapter<HomeDcAdapter.ViewHolder> {
    private ArrayList<DonationCentre> donationCentreArrayList;
    private ArrayList<String> dcUserIDList;
    private Context context;
    private StorageReference reference = FirebaseStorage.getInstance().getReference().child("Profile Pictures");
    private StorageReference imageRef;

    public HomeDcAdapter(ArrayList<DonationCentre> donationCentreArrayList, ArrayList<String> dcUserIDList,Context context){
        this.donationCentreArrayList= donationCentreArrayList;
        this.dcUserIDList= dcUserIDList;
        this.context=context;
    }
    @NonNull
    @NotNull
    @Override
    public HomeDcAdapter.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.home_dc_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull HomeDcAdapter.ViewHolder holder, int position) {
        DonationCentre donationCentre = donationCentreArrayList.get(position);
        String dcUserID= dcUserIDList.get(position);
        holder.bindTo(donationCentre,dcUserID);
    }

    @Override
    public int getItemCount() {
        return donationCentreArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView dcImage;
        private TextView dcName;
        private Button btnView;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            dcImage= itemView.findViewById(R.id.dc_image);
            dcName=itemView.findViewById(R.id.dc_name);
            btnView=itemView.findViewById(R.id.btn_view);
        }

        //bind the values to the placeholders in the layout
        public void bindTo(DonationCentre donationCentre,String dcUserID) {
            dcName.setText(donationCentre.getName());

            //get the profile image from storage
            imageRef= reference.child(dcUserID);
            imageRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull @NotNull Task<Uri> task) {
                    Uri profileImage=task.getResult();
                    Picasso.get().load(profileImage).into(dcImage);
                }
            });

            //go to the dcentres details activity when you click the View button
            btnView.setOnClickListener(v -> {
                Intent i = new Intent(context, DonationCentreActivity.class);
                i.putExtra("UID",dcUserID);
                context.startActivity(i);
            });
        }
    }

}
