package com.example.firstpage;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;

public class Quiz1After extends AppCompatActivity {

    private Button btnq1done;
    private TextView scoreTextView; // Declare TextView for displaying score
    private FirebaseFirestore db; // Firestore instance
    private FirebaseAuth auth; // Firebase Authentication instance

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz1_after); // Set your XML file here

        // Initialize Firebase Authentication and Firestore
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Initialize button and TextView
        btnq1done = findViewById(R.id.btnq1done);
        scoreTextView = findViewById(R.id.q1score); // Initialize the TextView for the score

        // Retrieve the score passed from the previous activity
        int score = getIntent().getIntExtra("score", 0); // Default score is 0 if not passed

        // Display the score in the TextView
        scoreTextView.setText("Your final score: " + score);

        // Save the score to Firestore
        saveScoreToFirestore(score);

        // Set click listener for the button
        btnq1done.setOnClickListener(v -> {
            if (validateBeforeRedirect()) {
                navigateToGameFragment(score); // Pass score to the next fragment
            } else {
                Toast.makeText(Quiz1After.this, "Validation failed!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Save score to Firestore
    private void saveScoreToFirestore(int score) {
        // Get the current user from Firebase Authentication
        String username = auth.getCurrentUser() != null ? auth.getCurrentUser().getDisplayName() : null;

        if (username != null) {
            // Create a map of the data you want to save
            Map<String, Object> scoreData = new HashMap<>();
            scoreData.put("score", score);
            scoreData.put("timestamp", System.currentTimeMillis()); // Optional: Add a timestamp
            scoreData.put("username", username); // Add the username to the data

            // Save the score to the "Quiz" collection in Firestore
            db.collection("Quiz")  // "Quiz" is the Firestore collection name
                    .document(username)  // Use username as the document ID
                    .set(scoreData)
                    .addOnSuccessListener(aVoid -> {
                        // Success logic (if needed)
                        Toast.makeText(Quiz1After.this, "Score saved successfully", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> {
                        // Failure logic (if needed)
                        Toast.makeText(Quiz1After.this, "Error saving score", Toast.LENGTH_SHORT).show();
                    });
        } else {
            // Handle case where user is not logged in or displayName is null
            Toast.makeText(this, "Username not found", Toast.LENGTH_SHORT).show();
        }
    }

    // Validation logic before navigating
    private boolean validateBeforeRedirect() {
        // Example validation: Check if a certain condition is met
        // Replace this with your actual validation logic
        return true; // Return false to prevent navigation if validation fails
    }

    // Replace current fragment with QuizFrag and pass score and completion flag
    private void navigateToGameFragment(int score) {
        GameFragment gameFragment = new GameFragment(); // Create an instance of GameFragment
        Bundle bundle = new Bundle();
        bundle.putBoolean("isQuizDone", true); // Pass flag indicating the quiz is done
        bundle.putInt("score", score); // Optionally pass score for other uses
        gameFragment.setArguments(bundle); // Set arguments for the fragment

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(android.R.id.content, gameFragment); // Replace the current fragment with GameFragment
        transaction.addToBackStack(null); // Allow back navigation
        transaction.commit();
    }

}