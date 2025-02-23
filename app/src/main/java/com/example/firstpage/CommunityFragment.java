package com.example.firstpage;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;

public class CommunityFragment extends Fragment {


    private TabLayout tabLayout;
    private ViewPager2 viewPager2;
    private ChallAdapter adapter;

    public CommunityFragment() {
        super(R.layout.fragment_community); // Ensure this matches your XML layout filename
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tabLayout = view.findViewById(R.id.inprog);
        viewPager2 = view.findViewById(R.id.challenge);

        // Set up ViewPager2 Adapter
        adapter = new ChallAdapter(requireActivity());
        viewPager2.setAdapter(adapter);

        // Set up TabLayout and ViewPager2 interaction
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager2.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                TabLayout.Tab tab = tabLayout.getTabAt(position);
                if (tab != null) {
                    tab.select();
                }
            }
        });


    }
}