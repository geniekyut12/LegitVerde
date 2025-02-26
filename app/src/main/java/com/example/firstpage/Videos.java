package com.example.firstpage;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Videos extends AppCompatActivity {

    private Button vid1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_videos);

        Button vid1Button = findViewById(R.id.vid1);

        vid1Button.setOnClickListener(v -> {
            Intent intent = new Intent(this, Vid1.class);
            startActivity(intent);
        });


    }
}