package com.example.firstpage;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class DoneFragment extends Fragment {

    private ImageView checkMark;
    private TextView completionMessage;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_done, container, false);

        checkMark = view.findViewById(R.id.checkMark);
        completionMessage = view.findViewById(R.id.completionMessage);

        // Make the checkmark visible
        checkMark.setVisibility(View.VISIBLE);

        // Show completion message
        completionMessage.setText("Challenge completed successfully!");

        return view;
    }
}
