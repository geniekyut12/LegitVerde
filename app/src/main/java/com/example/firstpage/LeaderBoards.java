package com.example.firstpage;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;

public class LeaderBoards extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Ensure your XML layout file name is correct (e.g., activity_leaderboard.xml)
        setContentView(R.layout.activity_leader_boards);

        // Find the back button defined in your XML layout
        ImageView backButton = findViewById(R.id.backButton);

        // Set a click listener on the back button
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Finish the current activity and go back to the previous one
                finish();
            }
        });
    }
}
