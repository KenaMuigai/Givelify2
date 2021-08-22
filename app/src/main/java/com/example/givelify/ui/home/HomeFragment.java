package com.example.givelify.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.givelify.LogoutActivity;
import com.example.givelify.R;
import com.example.givelify.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        //this is to hide the actionbar. I don't want a title on the homepage
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();

        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        //get the toolbar
        binding.toolbar.setOnMenuItemClickListener(item -> {
            int id = item.getItemId();
             if (id == R.id.logout) {
                Intent i =new Intent(getActivity(), LogoutActivity.class);
                startActivity(i);
                return true;
            }
            return false;
        });
        homeViewModel.getUsername().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                binding.user.setText(s+" !");
            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}