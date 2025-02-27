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

public class DinnerFragment extends Fragment {

    private LinearLayout Dinner1, Dinner2, Dinner3;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dinner, container, false); // Inflate the layout

        LinearLayout Dinner1 = view.findViewById(R.id.Dinner1);
        Dinner1.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), D1.class);
            startActivity(intent);
        });

        LinearLayout Dinner2 = view.findViewById(R.id.Dinner2);
        Dinner2.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), D2.class);
            startActivity(intent);
        });

        LinearLayout Dinner3 = view.findViewById(R.id.Dinner3);
        Dinner3.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), D3.class);
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
