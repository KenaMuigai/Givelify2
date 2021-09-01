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
import com.example.givelify.R;
import com.example.givelify.RequestActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class ChooseAdapter extends RecyclerView.Adapter<ChooseAdapter.ViewHolder> {
    private ArrayList<String> dcUserIDList;
    private String county,area,address;
    private Context context;
    private StorageReference reference = FirebaseStorage.getInstance().getReference().child("Profile Pictures");
    private StorageReference imageRef;
    private final FirebaseDatabase db=FirebaseDatabase.getInstance();
    private final DatabaseReference donationCenterRef= db.getReference().child("DCenters");


    public ChooseAdapter(ArrayList<String> dcUserIDList,Context context,String county, String area, String address){
        this.dcUserIDList= dcUserIDList;
        this.context=context;
        this.county=county;
        this.area=area;
        this.address=address;
    }
    @NonNull
    @NotNull
    @Override
    public ChooseAdapter.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.dc_choose_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ChooseAdapter.ViewHolder holder, int position) {
        String dcUserID= dcUserIDList.get(position);
        holder.bindTo(dcUserID,this.county,this.area,this.address);
    }

    @Override
    public int getItemCount() {
        return dcUserIDList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView dcImage;
        private TextView dcName;
        private Button btnView,btnChoose;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            dcImage= itemView.findViewById(R.id.dc_image);
            dcName=itemView.findViewById(R.id.dc_name);
            btnView=itemView.findViewById(R.id.btn_view);
            btnChoose=itemView.findViewById(R.id.btn_choose);
        }

        //bind the values to the placeholders in the layout
        public void bindTo(String dcUserID,String county,String area,String address) {
            //dcName.setText(donationCentre.getName());
            donationCenterRef.child(dcUserID).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                    dcName.setText(snapshot.child("name").getValue().toString());
                }

                @Override
                public void onCancelled(@NonNull @NotNull DatabaseError error) {

                }
            });

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
            btnChoose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(context, RequestActivity.class);
                    i.putExtra("DcUID",dcUserID);
                    i.putExtra("COUNTY",county);
                    i.putExtra("AREA",area);
                    i.putExtra("ADDRESS",address);
                    context.startActivity(i);
                }
            });
        }
    }

}
