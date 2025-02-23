package com.example.firstpage;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class InProgressFragment extends Fragment {

    private Button btnStartChallenge3;
    private TextView txtUserPoints;

    private FirebaseFirestore db;
    private FirebaseAuth auth;
    private String username;
    private DocumentReference userRef;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout
        View view = inflater.inflate(R.layout.fragment_in_progress, container, false);

        btnStartChallenge3 = view.findViewById(R.id.btnStartChallenge3);
        txtUserPoints = view.findViewById(R.id.txtPoints); // Ensure you have a TextView in your XML

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        fetchUserInfo(); // Fetch user info when fragment loads

        btnStartChallenge3.setOnClickListener(v -> {
            Intent intent = new Intent(requireActivity(), Chall3.class);
            startActivity(intent);
        });

        return view;
    }

    private void fetchUserInfo() {
        FirebaseUser user = auth.getCurrentUser();
        if (user != null) {
            String userEmail = user.getEmail();

            db.collection("users")
                    .whereEqualTo("email", userEmail)
                    .get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            username = document.getString("username");
                            if (username != null) {
                                userRef = db.collection("users").document(username);
                                getUserPoints();
                                checkChallengeStatus();
                            } else {
                                Toast.makeText(getContext(), "Username not found!", Toast.LENGTH_SHORT).show();
                            }
                            break; // Exit loop after finding the first match
                        }
                    })
                    .addOnFailureListener(e -> Log.e("Firestore", "Error fetching username", e));
        } else {
            Toast.makeText(getContext(), "User not logged in!", Toast.LENGTH_SHORT).show();
        }
    }

    private void getUserPoints() {
        if (userRef == null) return;

        userRef.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists() && documentSnapshot.contains("points")) {
                long points = documentSnapshot.getLong("points");
                txtUserPoints.setText("Points: " + points);
            } else {
                txtUserPoints.setText("Points: 0");
            }
        }).addOnFailureListener(e -> Log.e("Firestore", "Error fetching points", e));
    }

    private void checkChallengeStatus() {
        if (userRef == null) return;

        userRef.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists() && documentSnapshot.contains("chall3_completed")) {
                boolean isCompleted = documentSnapshot.getBoolean("chall3_completed");
                if (isCompleted) {
                    btnStartChallenge3.setText("Done");
                    btnStartChallenge3.setEnabled(false);
                }
            }
        }).addOnFailureListener(e -> Log.e("Firestore", "Error checking challenge status", e));
    }
}