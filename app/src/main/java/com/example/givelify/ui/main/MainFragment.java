package com.example.givelify.ui.main;

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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.givelify.Adapters.DcAdapter;
import com.example.givelify.LogoutActivity;
import com.example.givelify.Models.DonationCentre;
import com.example.givelify.R;
import com.example.givelify.databinding.FragmentHomeBinding;

import java.util.ArrayList;

public class MainFragment extends Fragment {

    private MainViewModel homeViewModel;
    private FragmentHomeBinding binding;
    private ArrayList<DonationCentre> dcArrayList;
    private ArrayList<String> dcUserIDList;
    private RecyclerView rvDonationCentres;
    private DcAdapter dcAdapter;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        //this is to hide the actionbar. I don't want a title on the homepage
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
        dcArrayList= new ArrayList<>();
        dcUserIDList= new ArrayList<>();


        homeViewModel =
                new ViewModelProvider(this).get(MainViewModel.class);

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
        homeViewModel.getUserCity().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                binding.city.setText(s);
            }
        });
        homeViewModel.getDonationCentersList().observe(getViewLifecycleOwner(), new Observer<ArrayList<DonationCentre>>() {
            @Override
            public void onChanged(ArrayList<DonationCentre> s) {
                dcArrayList=s;
                setRvDonationCentres(dcArrayList,dcUserIDList);

            }
        });
        homeViewModel.getUserIDList().observe(getViewLifecycleOwner(), new Observer<ArrayList<String>>() {
            @Override
            public void onChanged(ArrayList<String> s) {
                dcUserIDList=s;
                setRvDonationCentres(dcArrayList,dcUserIDList);
            }
        });

        return root;
    }

    private void setRvDonationCentres(ArrayList<DonationCentre> dcArrayList, ArrayList<String> dcUserIDList){
        rvDonationCentres = binding.rvFragDonationCentres;

        //the recyclerview is set to horizontal so that the list goes horizontally to fit the layout
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this.requireContext(),RecyclerView.HORIZONTAL,false);
        rvDonationCentres.setLayoutManager(layoutManager);

        dcAdapter= new DcAdapter(dcArrayList, dcUserIDList,this.requireContext());

        rvDonationCentres.setAdapter(dcAdapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}