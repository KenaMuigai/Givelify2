package com.example.givelify.ui.donationitems;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.givelify.Adapters.DonationItemsAdapter;
import com.example.givelify.databinding.FragmentProhibitedItemsBinding;

import java.util.ArrayList;

public class ProhibitedFragment extends Fragment {

    private ProhibitedViewModel mViewModel;
    private FragmentProhibitedItemsBinding binding;
    private ArrayList<String> itemList;
    private RecyclerView rvProhibitedItems;
    private DonationItemsAdapter donationItemsAdapter;

    public static ProhibitedFragment newInstance() {
            ProhibitedFragment fragment = new ProhibitedFragment();
            Bundle bundle = new Bundle();
            fragment.setArguments(bundle);
            return fragment;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(ProhibitedViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentProhibitedItemsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        mViewModel.getItemList().observe(getViewLifecycleOwner(), new Observer<ArrayList<String>>() {
            @Override
            public void onChanged(ArrayList<String> strings) {
                itemList= new ArrayList<>();
                itemList=strings;
                setRvProhibitedItems(itemList);

            }
        });
        return root;
    }

    private void setRvProhibitedItems(ArrayList<String> itemList) {
        rvProhibitedItems= binding.rvProhibitedItems;

        //the recyclerview is set to horizontal so that the list goes horizontally to fit the layout
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this.requireContext(),RecyclerView.VERTICAL,false);
        rvProhibitedItems.setLayoutManager(layoutManager);

        donationItemsAdapter= new DonationItemsAdapter(itemList,getContext());

        rvProhibitedItems.setAdapter(donationItemsAdapter);
    }


}