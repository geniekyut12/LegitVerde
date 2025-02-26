package com.example.firstpage;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import java.util.HashMap;
import java.util.Map;

public class CommunityFragment extends Fragment {

    private static final int REQUEST_CODE_CHALL3 = 1001;
    private Button btnStartChallenge3;
    private SharedPreferences sharedPreferences;
    private static final String PREFS_NAME = "MyPrefs";
    private static final String KEY_CHALL3_DONE = "challenge3Done";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_community, container, false);
        btnStartChallenge3 = view.findViewById(R.id.btnStartChallenge3);
        sharedPreferences = getActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        // Check if the challenge has already been completed
        boolean challenge3Done = sharedPreferences.getBoolean(KEY_CHALL3_DONE, false);
        if (challenge3Done) {
            btnStartChallenge3.setText("Done");
            btnStartChallenge3.setEnabled(false);
        } else {
            btnStartChallenge3.setOnClickListener(v -> {
                // Launch the Chall3 activity and wait for its result
                Intent intent = new Intent(getActivity(), Chall3.class);
                startActivityForResult(intent, REQUEST_CODE_CHALL3);
            });
        }
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CHALL3 && resultCode == Activity.RESULT_OK) {
            // Challenge completed successfully: update points and disable the button
            updatePoints(5);
            btnStartChallenge3.setText("Done");
            btnStartChallenge3.setEnabled(false);
            // Persist the challenge completion so it remains disabled in the future
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean(KEY_CHALL3_DONE, true);
            editor.apply();
        }
    }

    // Method to update the points in Firestore using the username as the document identifier.
    private void updatePoints(int pointsToAdd) {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String username = currentUser.getDisplayName();
            if (username == null || username.isEmpty()) {
                Log.w("CommunityFragment", "Username not found; cannot update points.");
                return;
            }
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            DocumentReference userRef = db.collection("UserPoints").document(username);
            Map<String, Object> data = new HashMap<>();
            data.put("points", FieldValue.increment(pointsToAdd));
            userRef.set(data, SetOptions.merge())
                    .addOnSuccessListener(aVoid -> Log.d("CommunityFragment", "Points updated successfully for user: " + username))
                    .addOnFailureListener(e -> Log.w("CommunityFragment", "Error updating points for user: " + username, e));
        } else {
            Log.w("CommunityFragment", "User not signed in; cannot update points.");
        }
    }
}