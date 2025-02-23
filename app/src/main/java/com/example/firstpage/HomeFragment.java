package com.example.firstpage;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager2.widget.ViewPager2;

import com.example.firstpage.HomeRecomFood.BFastFragment;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private LinearLayout linearLayoutBf, linearLayoutS, linearLayoutI, linearLayoutW, linearLayoutE,
            linearLayoutJ, linearLayoutB, linearLayoutEnvi;

    private TabLayout foodhome;
    private ViewPager2 homefood;
    private HomeAdapter tabAdapter;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.activity_homepage, container, false);

        foodhome = view.findViewById(R.id.foodhome);
        homefood = view.findViewById(R.id.homefood);

        // Set up adapter with fragments
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new BFastFragment());
        fragments.add(new LunchFragment());
        fragments.add(new DinnerFragment());

        homefood.getChildAt(0).setOnTouchListener((v, event) -> {
            v.getParent().requestDisallowInterceptTouchEvent(true);
            return false;
        });


        tabAdapter = new HomeAdapter(requireActivity(), fragments);
        homefood.setAdapter(tabAdapter);

        // Link TabLayout with ViewPager2
        new TabLayoutMediator(foodhome, homefood,
                (tab, position) -> {
                    switch (position) {
                        case 0:
                            tab.setText("Breakfast");
                            break;
                        case 1:
                            tab.setText("Lunch");
                            break;
                        case 2:
                            tab.setText("Dinner");
                            break;
                    }
                }).attach();


        // Initialize the LinearLayouts for food items


        linearLayoutS = view.findViewById(R.id.linearLayoutSphere);
        linearLayoutI = view.findViewById(R.id.linearLayoutIce);
        linearLayoutW = view.findViewById(R.id.linearLayoutWeather);
        linearLayoutE = view.findViewById(R.id.linearLayoutExp);
        linearLayoutJ = view.findViewById(R.id.linearLayoutJar);
        linearLayoutB = view.findViewById(R.id.linearLayoutBarrier);
        linearLayoutEnvi = view.findViewById(R.id.linearLayoutEnvironment);





        linearLayoutS.setOnClickListener(v -> startNewActivity(Sphere.class));
        linearLayoutI.setOnClickListener(v -> startNewActivity(Ice.class));
        linearLayoutW.setOnClickListener(v -> startNewActivity(Weather.class));
        linearLayoutE.setOnClickListener(v -> startNewActivity(Exp.class));
        linearLayoutJ.setOnClickListener(v -> startNewActivity(Jar.class));
        linearLayoutB.setOnClickListener(v -> startNewActivity(Barrier.class));
        linearLayoutEnvi.setOnClickListener(v -> startNewActivity(Environment.class));

        return view;
    }

    // Method to replace fragments
    private void replaceFragment(Fragment fragment) {
        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment); // Ensure you have a container in your activity XML
        transaction.addToBackStack(null);
        transaction.commit();
    }

    // Helper method to start an activity from the fragment
    private void startNewActivity(Class<?> activityClass) {
        Intent intent = new Intent(requireActivity(), activityClass); // Use requireActivity to ensure valid context
        startActivity(intent);
    }
}