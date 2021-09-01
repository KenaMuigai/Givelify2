package com.example.givelify.ui.profile;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.givelify.DcDonationItemsActivity;
import com.example.givelify.DcPickupActivity;
import com.example.givelify.DcProfileActivity;
import com.example.givelify.LoginActivity;
import com.example.givelify.LogoutActivity;
import com.example.givelify.R;
import com.example.givelify.databinding.FragmentProfileBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class ProfileFragment extends Fragment {

    private ProfileViewModel profileViewModel;
    private FragmentProfileBinding binding;
    private final FirebaseDatabase db=FirebaseDatabase.getInstance();
    private final FirebaseAuth mAuth=FirebaseAuth.getInstance();
    FirebaseUser currentUser= mAuth.getCurrentUser();
    private StorageReference reference = FirebaseStorage.getInstance().getReference().child("Profile Pictures");

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        profileViewModel =
                new ViewModelProvider(this).get(ProfileViewModel.class);

        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        binding.toolbar.setOnMenuItemClickListener(item -> {
            int id = item.getItemId();
            if (id == R.id.logout) {
                Intent i =new Intent(getActivity(), LogoutActivity.class);
                startActivity(i);
                return true;
            }else if(id==R.id.delete_account){
                new AlertDialog.Builder(getActivity())
                        .setTitle("Delete Your Account?")
                        .setMessage("Are you sure you want to delete your account?")
                        .setIcon(android.R.drawable.ic_delete)
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {
                                deleteUser();
                            }})
                        .setNegativeButton(android.R.string.cancel, null).show();
            }
            return false;
        });

        updateUI();

        binding.cvEditProfile.setOnClickListener(v -> {
            Intent intent= new Intent(getActivity(), DcProfileActivity.class);
            startActivity(intent);
        });
        binding.cvItemsList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), DcDonationItemsActivity.class);
                startActivity(intent);
            }
        });
        binding.cvPickup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), DcPickupActivity.class);
                startActivity(intent);
            }
        });
        return root;
    }

    private void updateUI() {

        profileViewModel.getUsername().observe(getViewLifecycleOwner(), s -> binding.dcProfileName.setText(s));
        profileViewModel.getEmail().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                binding.dcProfileEmail.setText(s);
            }
        });
        profileViewModel.getCategory().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                binding.dcProfileCategory.setText(s);
            }
        });
        profileViewModel.getCity().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                binding.dcProfileCity.setText(s);
            }
        });
        profileViewModel.getDescription().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                binding.dcProfileDesc.setText(s);
            }
        });
        profileViewModel.getWeb().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                binding.dcProfileWeb.setText(s);
            }
        });
        profileViewModel.getPhone().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                binding.dcProfilePhone.setText(s);
            }
        });
        profileViewModel.getProfileImage().observe(getViewLifecycleOwner(), new Observer<Uri>() {
            @Override
            public void onChanged(Uri uri) {
                //default image
                Picasso.get().load(uri).into(binding.dcProfileImage);
            }
        });

    }
    private void deleteUser() {
        currentUser.delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getActivity(), "User Deleted Successfully", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getActivity(), LoginActivity.class);
                        }
                    }
                });
        db.getReference().child("Dcenters").child(currentUser.getUid()).removeValue();
        //remove the user in all the other default additions.
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}