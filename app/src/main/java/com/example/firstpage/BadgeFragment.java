package com.example.firstpage;

import androidx.fragment.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class BadgeFragment extends Fragment {

    public BadgeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the fragment layout
        View rootView = inflater.inflate(R.layout.activity_badge_fragment, container, false);

        // (Optional) If you have a back button in the layout:
        ImageView backButton = rootView.findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> {
            // If you want to close the hosting Activity:
            requireActivity().finish();
            // Or go back (pop the back stack):
            // requireActivity().onBackPressed();
        });

        return rootView;
    }

    // This method is called from XML if you have, for example,
    // android:onClick="onRectangleClick" on some view in your layout.
    public void onRectangleClick(View view) {
        // Since this is a Fragment, call requireActivity().finish() or onBackPressed()
        requireActivity().finish();
        // Or you could do:
        // requireActivity().onBackPressed();
    }
}
