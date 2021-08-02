package com.example.givelify;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

public class DonationCentreActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dcenter_details);

        TabLayout tabLayout = findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("About Us"));
        tabLayout.addTab(tabLayout.newTab().setText("Donation Items List"));
        tabLayout.addTab(tabLayout.newTab().setText("Gallery"));

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        //using the pager adapter to manage the screens
        //create an instance of the view pager
        final ViewPager viewPager = findViewById(R.id.dc_view_pager);
        //create an instance of page adapter
        final PagerAdapter pagerAdapter = new com.example.givelify.Adapters.PagerAdapter(getSupportFragmentManager(),tabLayout.getTabCount());
        //set the adapter to the view pager
        viewPager.setAdapter(pagerAdapter);
        //set listener for clicks
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }
}
