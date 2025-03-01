package com.example.firstpage;


import static android.content.ContentValues.TAG;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;


import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;


import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;


import java.util.Properties;
import java.util.Stack;


import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;


public class navbar extends AppCompatActivity {


    private BottomNavigationView bottomNavigationView;
    private final Stack<Integer> fragmentStack = new Stack<>();
    private static final String PREFS_NAME = "loginPrefs";
    private static final String PREF_HAS_SEEN_POPUP = "hasSeenPopup"; // Track if popup was shown


    // Configure your sender email details here
    private static final String SENDER_EMAIL = "pagetest296@gmail.com"; // Replace with your sender email
    private static final String SENDER_PASSWORD = "fflnedycbucwenen";   // Replace with your app password


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navbar);


        bottomNavigationView = findViewById(R.id.bottom_navigation_view);


        if (savedInstanceState == null) {
            loadFragment(new HomeFragment(), R.id.nav_home);
        }


        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (!fragmentStack.isEmpty() && fragmentStack.peek() == itemId) {
                return true;
            }


            Fragment selectedFragment = null;
            if (itemId == R.id.nav_home) {
                selectedFragment = new HomeFragment();
            } else if (itemId == R.id.nav_footprint) {
                selectedFragment = new FootPrintFragment();
            } else if (itemId == R.id.nav_games) {
                selectedFragment = new GameFragment();
            } else if (itemId == R.id.nav_challenge) {
                selectedFragment = new CommunityFragment();
            } else if (itemId == R.id.nav_profile) {
                selectedFragment = new ProfileFragment();
            }


            if (selectedFragment != null) {
                loadFragment(selectedFragment, itemId);
            }


            return true;
        });


        // Check if the user qualifies for the discount popup
        checkForDiscountPopup();
    }


    private void loadFragment(Fragment fragment, int itemId) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();


        // Add the selected fragment to the stack
        if (!fragmentStack.isEmpty() && fragmentStack.peek() == itemId) {
            return; // Prevent duplicate entries
        }
        fragmentStack.push(itemId);
    }


    @Override
    public void onBackPressed() {
        if (fragmentStack.size() > 1) {
            fragmentStack.pop(); // Remove the current fragment
            int previousItemId = fragmentStack.peek();


            Fragment previousFragment = null;
            if (previousItemId == R.id.nav_home) {
                previousFragment = new HomeFragment();
            } else if (previousItemId == R.id.nav_footprint) {
                previousFragment = new FootPrintFragment();
            } else if (previousItemId == R.id.nav_games) {
                previousFragment = new GameFragment();
            } else if (previousItemId == R.id.nav_challenge) {
                previousFragment = new CommunityFragment();
            } else if (previousItemId == R.id.nav_profile) {
                previousFragment = new ProfileFragment();
            }


            if (previousFragment != null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, previousFragment)
                        .commit();
                bottomNavigationView.setSelectedItemId(previousItemId);
            }
        } else {
            super.onBackPressed();
        }
    }


    /**
     * Fetch total registered users and determine if the current user qualifies for the 10% discount.
     */
    private void checkForDiscountPopup() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


        if (user == null) return; // Ensure user is logged in


        String userEmail = user.getEmail(); // Get the user's email to find their username


        if (userEmail == null) return; // Avoid errors if email is null


        // Retrieve username from Firestore based on email
        db.collection("users").whereEqualTo("email", userEmail).get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        String username = queryDocumentSnapshots.getDocuments().get(0).getString("username");


                        if (username == null) return; // Avoid errors if username is missing


                        // Check if this username already received the bonus
                        db.collection("users").document(username).get()
                                .addOnSuccessListener(documentSnapshot -> {
                                    if (documentSnapshot.exists() && documentSnapshot.contains("bonusReceived")) {
                                        boolean bonusReceived = documentSnapshot.getBoolean("bonusReceived");
                                        if (bonusReceived)
                                            return; // Exit if the bonus is already claimed
                                    }


                                    // Count how many users have already received the bonus
                                    db.collection("users").whereEqualTo("bonusReceived", true).get()
                                            .addOnCompleteListener(task -> {
                                                if (task.isSuccessful()) {
                                                    int bonusClaimedCount = task.getResult().size();


                                                    if (bonusClaimedCount < 10) { // Only show popup if less than 10 claimed
                                                        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
                                                        boolean hasSeenPopup = sharedPreferences.getBoolean(PREF_HAS_SEEN_POPUP, false);


                                                        if (!hasSeenPopup) {
                                                            showDiscountPopup(username);


                                                            SharedPreferences.Editor editor = sharedPreferences.edit();
                                                            editor.putBoolean(PREF_HAS_SEEN_POPUP, true);
                                                            editor.apply();
                                                        }
                                                    }
                                                }
                                            });
                                });
                    }
                });
    }


    /**
     * Displays the 10% discount popup.
     */
    private void showDiscountPopup(String username) { // Accepts username now
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Congratulations!")
                .setMessage("You are among the first 10 users! Enjoy a 10% discount on your first purchase.")
                .setPositiveButton("Claim Now", (dialog, which) -> {
                    // Let the user choose an enterprise
                    chooseEnterprise(username);
                    dialog.dismiss();
                })
                .setNegativeButton("Close", (dialog, which) -> dialog.dismiss())
                .show();
    }


    /**
     * Shows a list of enterprises and handles the selection.
     */
    private void chooseEnterprise(String username) {
        String[] enterprises = {"Playmaker", "Wani", "Masugid"};


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select an Enterprise")
                .setItems(enterprises, (dialog, which) -> {
                    String selectedEnterprise = enterprises[which];
                    applyBonusToUser(username, selectedEnterprise);
                })
                .show();
    }


    /**
     * Applies the bonus to the user and sends the bonus email automatically.
     */
    private void applyBonusToUser(String username, String selectedEnterprise) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();


        // Reference the user's document using their username
        db.collection("users")
                .whereEqualTo("username", username) // Find user by username
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        String userId = queryDocumentSnapshots.getDocuments().get(0).getId(); // Get user ID
                        db.collection("users").document(userId)
                                // Update bonus details and mark bonus as received
                                .update("bonusEnterprise", selectedEnterprise,
                                        "bonusAmount", "10%",
                                        "bonusReceived", true)
                                .addOnSuccessListener(aVoid -> {
                                    Toast.makeText(this, "Bonus applied successfully!", Toast.LENGTH_SHORT).show();
                                    // After successfully applying the bonus, send the bonus details via email.
                                    sendBonusEmail(selectedEnterprise);
                                })
                                .addOnFailureListener(e ->
                                        Toast.makeText(this, "Failed to apply bonus.", Toast.LENGTH_SHORT).show()
                                );
                    }
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Error finding user.", Toast.LENGTH_SHORT).show()
                );
    }


    /**
     * Sends bonus emails automatically using JavaMail:
     * one email to the user and another to the sender (admin).
     */
    private void sendBonusEmail(String selectedEnterprise) {
        new Thread(() -> {
            try {
                // Set up mail properties
                Properties props = new Properties();
                props.put("mail.smtp.host", "smtp.gmail.com");
                props.put("mail.smtp.port", "587");
                props.put("mail.smtp.auth", "true");
                props.put("mail.smtp.starttls.enable", "true");


                // Create session with authenticator
                Session session = Session.getInstance(props, new Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(SENDER_EMAIL, SENDER_PASSWORD);
                    }
                });


                // Get current user email
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user == null || user.getEmail() == null) {
                    return;
                }
                String recipient = user.getEmail();


                // Prepare email for the user
                MimeMessage userMessage = new MimeMessage(session);
                userMessage.setFrom(new InternetAddress(SENDER_EMAIL));
                userMessage.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipient));
                userMessage.setSubject("Your Bonus Details");
                userMessage.setText("Congratulations!\n\nYour bonus of 10% discount for "
                        + selectedEnterprise + " has been applied.");


                // Prepare email for the sender (admin)
                MimeMessage senderMessage = new MimeMessage(session);
                senderMessage.setFrom(new InternetAddress(SENDER_EMAIL));
                senderMessage.setRecipients(Message.RecipientType.TO, InternetAddress.parse(SENDER_EMAIL));
                senderMessage.setSubject("New Bonus Claimed!");
                senderMessage.setText("User " + recipient + " has claimed a bonus for " + selectedEnterprise + ".");


                // Send both emails
                Transport.send(userMessage);
                Transport.send(senderMessage);


                runOnUiThread(() -> Toast.makeText(navbar.this, "Bonus email sent successfully.", Toast.LENGTH_SHORT).show());
            } catch (MessagingException e) {
                e.printStackTrace();
                runOnUiThread(() -> Toast.makeText(navbar.this, "Error sending bonus email.", Toast.LENGTH_SHORT).show());
            }
        }).start();
    }
}
