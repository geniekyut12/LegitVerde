package com.example.firstpage;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.RadioGroup;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Quiz5of3 extends AppCompatActivity {

    private RadioGroup radioGroup;
    private int score = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_quiz5of3);
        // Find the RadioGroup
        RadioGroup radioGroup = findViewById(R.id.qz3q5chcs);

        // Set listener for RadioGroup selection
        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId != -1) { // Ensure a valid selection
                // Check if the correct answer is selected (btnq1A)
                if (checkedId == R.id.btnq1B) { // Replace btnq1A with the actual button ID
                    score++; // Increment the score for the correct answer
                }

                // Proceed to the next question or finish quiz
                Intent intent = new Intent(Quiz5of3.this, Quiz3After.class);
                // Pass the score to the next activity
                intent.putExtra("score", score);
                startActivity(intent);
            }
        });
    }
}