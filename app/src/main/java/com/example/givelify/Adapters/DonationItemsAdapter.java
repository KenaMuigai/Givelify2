package com.example.givelify.Adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.givelify.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class DonationItemsAdapter extends RecyclerView.Adapter<DonationItemsAdapter.Viewholder> {
    private ArrayList<String> itemList;
    private Context context;
    private final DatabaseReference dbroot = FirebaseDatabase.getInstance().getReference();


    public DonationItemsAdapter(ArrayList<String> itemList, Context context) {
        this.itemList = itemList;
        this.context = context;
    }

    @NonNull
    @NotNull
    @Override
    public DonationItemsAdapter.Viewholder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
       return new DonationItemsAdapter.Viewholder(LayoutInflater.from(context).inflate(R.layout.donation_item,parent,false));

    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull DonationItemsAdapter.Viewholder holder, int position) {
        String item= itemList.get(position);
        holder.bindTo(item);
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        private TextView tvTitle,tvRemove;

        public Viewholder(@NonNull @NotNull View itemView) {
            super(itemView);
            tvTitle=itemView.findViewById(R.id.tv_item_title);
            tvRemove=itemView.findViewById(R.id.tv_remove);

        }
        public void bindTo(String itemTitle){
            tvTitle.setText(itemTitle);
            tvRemove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //implement a way to delete the item from the list
                    new AlertDialog.Builder(context)
                            .setTitle("Delete")
                            .setMessage("Are you sure you want to delete "+itemTitle+" from your list?")
                            .setIcon(android.R.drawable.ic_delete)
                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {

                                public void onClick(DialogInterface dialog, int whichButton) {
                                    //TODO Delete the item from list
                                    Toast.makeText(context, "Item Deleted", Toast.LENGTH_SHORT).show();
                                }})
                            .setNegativeButton(R.string.cancel, null).show();
                }
            });
        }
    }
}
