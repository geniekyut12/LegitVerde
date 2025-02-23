package com.example.firstpage;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

public class FragmentRewardM extends AppCompatActivity {

    private final int userPoints = 350;
    private Button redeemButton1;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_reward_m);

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences("RewardPrefs", MODE_PRIVATE);

        // Initialize buttons
        redeemButton1 = findViewById(R.id.claim_masug5per);
        Button redeemButton2 = findViewById(R.id.claim_masug10per);
        Button redeemButton3 = findViewById(R.id.claim_wani5per);
        Button redeemButton4 = findViewById(R.id.claim_playm5per);

        // Check if the reward has already been claimed
        if (sharedPreferences.getBoolean("MASUG5PER_CLAIMED", false)) {

        }

        // Set click listener for redeemButton1
        redeemButton1.setOnClickListener(v -> showRedeemDialog());

        // Open RewMasug5per Activity when redeemButton2 is clicked
        redeemButton2.setOnClickListener(v -> {
            Intent intent = new Intent(FragmentRewardM.this, RewMasug5per.class);
            startActivity(intent);
        });

        // Open RewWani5per Activity when redeemButton3 is clicked
        redeemButton3.setOnClickListener(v -> {
            Intent intent = new Intent(FragmentRewardM.this, RewWani5per.class);
            startActivity(intent);
        });

        // Open RewPlaym5per Activity when redeemButton4 is clicked
        redeemButton4.setOnClickListener(v -> {
            Intent intent = new Intent(FragmentRewardM.this, RewPlaym5per.class);
            startActivity(intent);
        });
    }

    // Method to show the RedeemDialog fragment
    private void showRedeemDialog() {
        FragmentManager fm = getSupportFragmentManager();
        RedeemDialog redeemDialog = new RedeemDialog();
        redeemDialog.show(fm, "redeem_dialog");

        // Mark the reward as claimed
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("MASUG5PER_CLAIMED", true);
        editor.apply();



    }

    // Method to disable the claim button

}