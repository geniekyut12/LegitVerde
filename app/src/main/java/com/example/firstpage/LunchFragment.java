package com.example.firstpage;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import com.example.firstpage.R;
import com.example.firstpage.eggtoast;

public class LunchFragment extends Fragment {

    private LinearLayout Lunch1, Lunch2, Lunch3;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lunch, container, false); // Inflate the layout

        LinearLayout Lunch1 = view.findViewById(R.id.Lunch1);
        Lunch1.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), L1.class);
            startActivity(intent);
        });

        LinearLayout Lunch2 = view.findViewById(R.id.Lunch2);
        Lunch2.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), L2.class);
            startActivity(intent);
        });

        LinearLayout Lunch3 = view.findViewById(R.id.Lunch3);
        Lunch3.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), L3.class);
            startActivity(intent);
        });

        return view; // Return the view at the end
    }

    // Method to replace the fragment
    private void replaceFragment(Fragment fragment) {
        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment); // Make sure R.id.fragment_container exists in the parent activity
        transaction.addToBackStack(null); // Allows back navigation
        transaction.commit();
    }


}
