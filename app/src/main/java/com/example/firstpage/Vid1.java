package com.example.firstpage;

import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.VideoView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.firstpage.R;

public class Vid1 extends AppCompatActivity {

    private VideoView videoView;
    private Button btnPlay, btnPause, btnRestart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vid1);

        videoView = findViewById(R.id.videoView);
        btnPlay = findViewById(R.id.btnPlay);
        btnPause = findViewById(R.id.btnPause);
        btnRestart = findViewById(R.id.btnRestart);

        // Set video source (Use either local file from res/raw or an online URL)
        Uri videoUri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.intro);
        // Uri videoUri = Uri.parse("https://www.example.com/sample.mp4"); // Use this for online video

        videoView.setVideoURI(videoUri);

        // Add media controls (Play/Pause/Seek)
        MediaController mediaController = new MediaController(this);
        mediaController.setAnchorView(videoView);
        videoView.setMediaController(mediaController);

        // Play button
        btnPlay.setOnClickListener(v -> videoView.start());

        // Pause button
        btnPause.setOnClickListener(v -> videoView.pause());

        // Restart button
        btnRestart.setOnClickListener(v -> {
            videoView.seekTo(0);
            videoView.start();
        });
    }
}
