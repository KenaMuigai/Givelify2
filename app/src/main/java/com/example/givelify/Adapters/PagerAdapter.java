package com.example.givelify.Adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.givelify.Fragments.AboutUsFragment;
import com.example.givelify.Fragments.DonationItemsListFragment;
import com.example.givelify.Fragments.GalleryFragment;

import org.jetbrains.annotations.NotNull;

public class PagerAdapter extends FragmentStatePagerAdapter {
    int num_of_tabs;

    public PagerAdapter(@NonNull @NotNull FragmentManager fm, int num_of_tabs) {
        super(fm,num_of_tabs);
        this.num_of_tabs=num_of_tabs;
    }

    @NonNull
    @NotNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0: return new AboutUsFragment();
            case 1: return new DonationItemsListFragment();
            case 2: return new GalleryFragment();
            default:return null;
        }
    }

    @Override
    public int getCount() {
        return num_of_tabs;
    }
}
