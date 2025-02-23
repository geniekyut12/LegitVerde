package com.example.firstpage.HomeRecomFood;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import com.example.firstpage.Bfast1Fragment;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import com.example.firstpage.R;
import com.example.firstpage.eggtoast;

public class BFastFragment extends Fragment {

    private LinearLayout bfast1, bfast2;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_b_fast, container, false); // Inflate the layout

        LinearLayout bfast1 = view.findViewById(R.id.bfast1);
        bfast1.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), eggtoast.class);
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
