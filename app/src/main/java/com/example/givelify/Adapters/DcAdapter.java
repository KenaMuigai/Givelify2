package com.example.givelify.Adapters;

import android.content.Context;
import android.content.Intent;
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

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class DcAdapter extends RecyclerView.Adapter<DcAdapter.ViewHolder> {
    private ArrayList<DonationCentre> donationCentreArrayList;
    private Context context;

    public DcAdapter(ArrayList<DonationCentre> donationCentreArrayList, Context context){
        this.donationCentreArrayList= donationCentreArrayList;
        this.context=context;
    }
    @NonNull
    @NotNull
    @Override
    public DcAdapter.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.dcentre_row_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull DcAdapter.ViewHolder holder, int position) {
        DonationCentre donationCentre = donationCentreArrayList.get(position);
        holder.bindTo(donationCentre);
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

            //go to the dcentres details activity when you click the View button
            btnView.setOnClickListener(v -> {
                Intent i = new Intent(context, DonationCentreActivity.class);
                context.startActivity(i);
            });
        }

        //bind the values to the placeholders in the layout
        public void bindTo(DonationCentre donationCentre) {
            dcName.setText(donationCentre.getName());
            dcImage.setImageResource(donationCentre.getImageUrl());
        }
    }

}
