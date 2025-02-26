package com.example.firstpage;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Collections;

public class FragmentRewardM extends AppCompatActivity {

    private Button redeemButton1, redeemButton2, redeemButton3, redeemButton4;
    private TextView TotalPoints;
    private ImageView rankImageView, backButton;  // Rank badge ImageView
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    // Computed component points from each collection.
    private int gamePoints = 0;
    private int quizPoints = 0;
    private int quiz2Points = 0;
    private int userTotalPoints = 0; // Sum of all three

    private String currentUsername;

    // Map to hold reward claimed statuses from Firestore.
    private Map<String, Boolean> rewardStatusMap = new HashMap<>();

    // Required points to redeem each reward.
    private final int MASUG5PER_POINTS = 12352;
    private final int MASUG10PER_POINTS = 150;
    private final int WANI5PER_POINTS = 1200;
    private final int PLAYM5PER_POINTS = 1100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_reward_m);

        LinearLayout headerLayout = findViewById(R.id.btn_backReward);
        ImageView backButton = findViewById(R.id.backrew);

        // Set click listener for back navigation
        backButton.setOnClickListener(v -> finish()); // Closes the current activity

        // Set click listener for the whole layout (if needed)
        headerLayout.setOnClickListener(v -> {
            // Perform additional actions if required
        });

        // Initialize UI elements.
        redeemButton1 = findViewById(R.id.claim_masug5per);
        redeemButton2 = findViewById(R.id.claim_masug10per);
        redeemButton3 = findViewById(R.id.claim_wani5per);
        redeemButton4 = findViewById(R.id.claim_playm5per);
        TotalPoints = findViewById(R.id.pointsinR);
        rankImageView = findViewById(R.id.rewRank);

        // Initialize Firebase.
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Fetch points from the three collections and update reward status.
        fetchAndUpdatePoints();

        // Set click listeners for reward redemption.
        redeemButton1.setOnClickListener(v -> redeemReward("MASUG5PER_CLAIMED", MASUG5PER_POINTS, redeemButton1));
        redeemButton2.setOnClickListener(v -> redeemReward("MASUG10PER_CLAIMED", MASUG10PER_POINTS, redeemButton2));
        redeemButton3.setOnClickListener(v -> redeemReward("WANI5PER_CLAIMED", WANI5PER_POINTS, redeemButton3));
        redeemButton4.setOnClickListener(v -> redeemReward("PLAYM5PER_CLAIMED", PLAYM5PER_POINTS, redeemButton4));
    }

    private void fetchAndUpdatePoints() {
        if (mAuth.getCurrentUser() == null) {
            Toast.makeText(this, "Error: User not logged in!", Toast.LENGTH_LONG).show();
            return;
        }

        // Use displayName or UID as username.
        currentUsername = mAuth.getCurrentUser().getDisplayName();
        if (currentUsername == null || currentUsername.isEmpty()) {
            currentUsername = mAuth.getCurrentUser().getUid();
        }

        // Get document references for each collection.
        DocumentReference gameRef = db.collection("Games").document(currentUsername);
        DocumentReference quizRef = db.collection("Quiz").document(currentUsername);
        DocumentReference quizRef2 = db.collection("Quiz2").document(currentUsername);

        // Fetch points from the three sources sequentially.
        gameRef.get().addOnSuccessListener(gameSnapshot -> {
            gamePoints = (gameSnapshot.exists() && gameSnapshot.contains("highScore"))
                    ? gameSnapshot.getLong("highScore").intValue() : 0;
            quizRef.get().addOnSuccessListener(quizSnapshot -> {
                quizPoints = (quizSnapshot.exists() && quizSnapshot.contains("score"))
                        ? quizSnapshot.getLong("score").intValue() : 0;
                quizRef2.get().addOnSuccessListener(quiz2Snapshot -> {
                    quiz2Points = (quiz2Snapshot.exists() && quiz2Snapshot.contains("score"))
                            ? quiz2Snapshot.getLong("score").intValue() : 0;

                    // Compute total points.
                    userTotalPoints = gamePoints + quizPoints + quiz2Points;
                    TotalPoints.setText(String.format(Locale.getDefault(), "Total Points: %d", userTotalPoints));

                    // Update UI buttons and rank image.
                    updateButtonStatus();
                    updateRankImage();

                    // Fetch reward claimed status.
                    fetchRewardStatus();

                }).addOnFailureListener(e -> showError("Error fetching Quiz2 points", e));
            }).addOnFailureListener(e -> showError("Error fetching Quiz points", e));
        }).addOnFailureListener(e -> showError("Error fetching Game points", e));
    }

    private void fetchRewardStatus() {
        DocumentReference rewardsRef = db.collection("Rewards").document(currentUsername);
        rewardsRef.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists() && documentSnapshot.getData() != null) {
                Map<String, Object> data = documentSnapshot.getData();
                for (Map.Entry<String, Object> entry : data.entrySet()) {
                    if (entry.getValue() instanceof Boolean) {
                        rewardStatusMap.put(entry.getKey(), (Boolean) entry.getValue());
                    }
                }
            }
            updateButtonStatus(); // Refresh button text based on reward status.
        }).addOnFailureListener(e -> showError("Error fetching reward status", e));
    }

    // Update rank badge based on available total points.
    private void updateRankImage() {
        if (userTotalPoints >= 25) {
            rankImageView.setImageResource(R.drawable.badgey);
        } else if (userTotalPoints >= 23) {
            rankImageView.setImageResource(R.drawable.badgebb);
        } else if (userTotalPoints >= 21) {
            rankImageView.setImageResource(R.drawable.badgeg);
        } else if (userTotalPoints >= 20) {
            rankImageView.setImageResource(R.drawable.bagdeb);
        }
    }

    private void updateButtonStatus() {
        updateButtonText(redeemButton1, "MASUG5PER_CLAIMED", MASUG5PER_POINTS);
        updateButtonText(redeemButton2, "MASUG10PER_CLAIMED", MASUG10PER_POINTS);
        updateButtonText(redeemButton3, "WANI5PER_CLAIMED", WANI5PER_POINTS);
        updateButtonText(redeemButton4, "PLAYM5PER_CLAIMED", PLAYM5PER_POINTS);
    }

    private void updateButtonText(Button button, String rewardKey, int requiredPoints) {
        boolean claimed = rewardStatusMap.containsKey(rewardKey) ? rewardStatusMap.get(rewardKey) : false;
        if (claimed) {
            button.setText("Reward Claimed");
        } else if (userTotalPoints >= requiredPoints) {
            button.setText("Claim Reward");
        } else {
            button.setText("Insufficient Points");
        }
    }

    private void redeemReward(String rewardKey, int requiredPoints, Button button) {
        boolean claimed = rewardStatusMap.containsKey(rewardKey) ? rewardStatusMap.get(rewardKey) : false;
        if (claimed) {
            Toast.makeText(this, "Reward already claimed!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (userTotalPoints >= requiredPoints) {
            showRedeemDialog(rewardKey, requiredPoints, button);
        } else {
            Toast.makeText(this, "Insufficient Points to claim this reward!", Toast.LENGTH_SHORT).show();
        }
    }

    private void showRedeemDialog(String rewardKey, int requiredPoints, Button button) {
        FragmentManager fm = getSupportFragmentManager();
        RedeemDialog redeemDialog = new RedeemDialog();
        redeemDialog.show(fm, "redeem_dialog");

        // Mark the reward as claimed in Firestore.
        DocumentReference rewardRef = db.collection("Rewards").document(currentUsername);
        Map<String, Object> update = new HashMap<>();
        update.put(rewardKey, true);
        rewardRef.set(update, SetOptions.merge())
                .addOnSuccessListener(aVoid -> {
                    rewardStatusMap.put(rewardKey, true);
                    button.setText("Reward Claimed");

                    // Subtract the required points proportionally from the three collections.
                    int total = gamePoints + quizPoints + quiz2Points;
                    if(total <= 0) {
                        Toast.makeText(this, "Error: Total points is zero", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    float fractionGame = (float) gamePoints / total;
                    float fractionQuiz = (float) quizPoints / total;
                    float fractionQuiz2 = (float) quiz2Points / total;
                    int deductGame = Math.round(fractionGame * requiredPoints);
                    int deductQuiz = Math.round(fractionQuiz * requiredPoints);
                    int deductQuiz2 = requiredPoints - deductGame - deductQuiz; // ensures the sum equals requiredPoints

                    // Get document references for each collection.
                    DocumentReference gameRef = db.collection("Games").document(currentUsername);
                    DocumentReference quizRef = db.collection("Quiz").document(currentUsername);
                    DocumentReference quizRef2 = db.collection("Quiz2").document(currentUsername);

                    // Use set() with merge to update points even if the document doesn't exist.
                    Tasks.whenAll(
                            gameRef.set(Collections.singletonMap("highScore", FieldValue.increment(-deductGame)), SetOptions.merge()),
                            quizRef.set(Collections.singletonMap("score", FieldValue.increment(-deductQuiz)), SetOptions.merge()),
                            quizRef2.set(Collections.singletonMap("score", FieldValue.increment(-deductQuiz2)), SetOptions.merge())
                    ).addOnSuccessListener(unused -> {
                        // Update local values.
                        gamePoints -= deductGame;
                        quizPoints -= deductQuiz;
                        quiz2Points -= deductQuiz2;
                        userTotalPoints = gamePoints + quizPoints + quiz2Points;
                        TotalPoints.setText(String.format(Locale.getDefault(), "Total Points: %d", userTotalPoints));
                        updateButtonStatus();
                        updateRankImage();
                    }).addOnFailureListener(e -> showError("a", e));
                })
                .addOnFailureListener(e -> showError("Error updating reward status", e));
    }

    private void showError(String message, Exception e) {
        Log.e("FragmentRewardM", message, e);
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
