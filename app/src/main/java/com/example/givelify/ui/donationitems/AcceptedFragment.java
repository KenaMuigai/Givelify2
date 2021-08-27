package com.example.givelify.ui.donationitems;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.givelify.Adapters.DonationItemsAdapter;
import com.example.givelify.databinding.FragmentAcceptedItemsBinding;

import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class AcceptedFragment extends Fragment {

    private AcceptedViewModel acceptedViewModel;
    private FragmentAcceptedItemsBinding binding;
    private ArrayList<String> itemList;
    private RecyclerView rvAcceptedItems;
    private DonationItemsAdapter donationItemsAdapter;

    public static AcceptedFragment newInstance() {
        AcceptedFragment fragment = new AcceptedFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        acceptedViewModel = new ViewModelProvider(this).get(AcceptedViewModel.class);

    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        binding = FragmentAcceptedItemsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        acceptedViewModel.getItemList().observe(getViewLifecycleOwner(), new Observer<ArrayList<String>>() {
            @Override
            public void onChanged(ArrayList<String> strings) {
                itemList= new ArrayList<>();
                itemList=strings;
                setRvAcceptedItems(itemList);

            }
        });
        return root;
    }

    private void setRvAcceptedItems(ArrayList<String> itemList) {
        rvAcceptedItems= binding.rvAcceptedItems;

        //the recyclerview is set to horizontal so that the list goes horizontally to fit the layout
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this.requireContext(),RecyclerView.VERTICAL,false);
        rvAcceptedItems.setLayoutManager(layoutManager);

        donationItemsAdapter= new DonationItemsAdapter(itemList,getContext());

        rvAcceptedItems.setAdapter(donationItemsAdapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}