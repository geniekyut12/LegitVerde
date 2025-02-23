package com.example.firstpage;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class eggtoast extends AppCompatActivity {

    private Button eggtstStart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_eggtoast);

        // Initialize and set up "Get Started" button
        eggtstStart = findViewById(R.id.eggtstStart);
        eggtstStart.setOnClickListener(v -> {
            Intent intent = new Intent(eggtoast.this, Bfast1Fragment.class);
            startActivity(intent);
        });
    }
}