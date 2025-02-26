package com.example.firstpage;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class QuizFrag extends Fragment {

    private Button btnstartqz1, btnstartqz2;
    private FirebaseFirestore db;
    private String userId;

    public QuizFrag() {
        // Required empty public constructor
    }

    public static QuizFrag newInstance(String param1, String param2) {
        QuizFrag fragment = new QuizFrag();
        Bundle args = new Bundle();
        args.putString("param1", param1);
        args.putString("param2", param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Initialize Firestore
        db = FirebaseFirestore.getInstance();
        // Get the current user's UID (ensure user is authenticated)
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_quiz, container, false);

        btnstartqz1 = view.findViewById(R.id.btnstartqz1);
        btnstartqz2 = view.findViewById(R.id.btnstartqz2);

        // Get today's date as a string in "yyyy-MM-dd" format
        String todayDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

        // References to the quiz completion documents in Firestore
        DocumentReference quiz1Ref = db.collection("users").document(userId)
                .collection("quizCompletions").document("quiz1");
        DocumentReference quiz2Ref = db.collection("users").document(userId)
                .collection("quizCompletions").document("quiz2");

        // Check if Quiz 1 was already completed today
        quiz1Ref.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                String storedDate = documentSnapshot.getString("date");
                if (todayDate.equals(storedDate)) {
                    btnstartqz1.setAlpha(0.3f);
                    btnstartqz1.setEnabled(false);
                }
            }
        });

        // Check if Quiz 2 was already completed today
        quiz2Ref.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                String storedDate = documentSnapshot.getString("date");
                if (todayDate.equals(storedDate)) {
                    btnstartqz2.setAlpha(0.3f);
                    btnstartqz2.setEnabled(false);
                }
            }
        });

        // Set click listeners for each button
        btnstartqz1.setOnClickListener(v -> {
            // Save today's date as the completion date for Quiz 1
            Map<String, Object> data = new HashMap<>();
            data.put("date", todayDate);
            quiz1Ref.set(data);
            navigateToQuiz1();
        });

        btnstartqz2.setOnClickListener(v -> {
            // Save today's date as the completion date for Quiz 2
            Map<String, Object> data = new HashMap<>();
            data.put("date", todayDate);
            quiz2Ref.set(data);
            navigateToQuiz2();
        });

        return view;
    }

    private void navigateToQuiz1() {
        Intent intent = new Intent(getActivity(), Quiz1of1.class);
        startActivity(intent);
    }

    private void navigateToQuiz2() {
        Intent intent = new Intent(getActivity(), Quiz1of2.class);
        startActivity(intent);
    }
}
