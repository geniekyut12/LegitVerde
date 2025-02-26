package com.example.firstpage;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.label.ImageLabel;
import com.google.mlkit.vision.label.ImageLabeling;
import com.google.mlkit.vision.label.defaults.ImageLabelerOptions;

public class Chall3 extends AppCompatActivity {

    private static final int CAMERA_REQUEST_CODE = 100;
    private ImageView cameraIcon, checkMark;
    private Button captureButton;
    private Bitmap capturedImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chall3);

        cameraIcon = findViewById(R.id.cameraIcon);
        checkMark = findViewById(R.id.checkMark);
        captureButton = findViewById(R.id.captureButton);

        captureButton.setOnClickListener(v -> openCamera());
    }

    private void openCamera() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (cameraIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CAMERA_REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {
            capturedImage = (Bitmap) data.getExtras().get("data");

            if (capturedImage != null) {
                processImage();
            }
        }
    }

    private void processImage() {
        InputImage image = InputImage.fromBitmap(capturedImage, 0);
        ImageLabeling.getClient(new ImageLabelerOptions.Builder()
                        .setConfidenceThreshold(0.7f)
                        .build())
                .process(image)
                .addOnSuccessListener(labels -> {
                    // Check each detected label for "food"
                    for (ImageLabel label : labels) {
                        if (label.getText().toLowerCase().contains("food")) {
                            checkMark.setVisibility(View.VISIBLE);
                            Toast.makeText(this, "Food detected! Challenge completed!", Toast.LENGTH_SHORT).show();
                            // Set result and finish to return to the previous screen (CommunityFragment)
                            setResult(Activity.RESULT_OK);
                            finish();
                            return;
                        }
                    }
                    checkMark.setVisibility(View.GONE);
                    Toast.makeText(this, "No food detected.", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                );
    }
}
