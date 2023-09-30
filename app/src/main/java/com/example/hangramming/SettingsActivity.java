package com.example.hangramming;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class SettingsActivity extends AppCompatActivity {

    private AudioManager audioManager;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference userRef;
    private TextView usernameTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // Initialize AudioManager
        audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);

        // Initialize Firebase Auth
        firebaseAuth = FirebaseAuth.getInstance();

        // Find the TextView to display the username
        usernameTextView = findViewById(R.id.usernameTextView);

        // Volume Up Button
        Button volumeUpButton = findViewById(R.id.volumeUpButton);
        volumeUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                audioManager.adjustVolume(AudioManager.ADJUST_RAISE, AudioManager.FLAG_SHOW_UI);
            }
        });

        // Volume Down Button
        Button volumeDownButton = findViewById(R.id.volumeDownButton);
        volumeDownButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                audioManager.adjustVolume(AudioManager.ADJUST_LOWER, AudioManager.FLAG_SHOW_UI);
            }
        });

        // Logout Button
        Button logoutButton = findViewById(R.id.logoutButton);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLogoutConfirmationDialog();
            }
        });

        // Fetch and display the username from Firebase Realtime Database
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if (currentUser != null) {
            String uid = ((FirebaseUser) currentUser).getUid();
            userRef = FirebaseDatabase.getInstance().getReference().child("users").child(uid);

            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        String username = dataSnapshot.child("username").getValue(String.class);
                        usernameTextView.setText("Username: " + username);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    // Handle the error, if any
                }
            });
        }
    }

    // ... the rest of your code ...

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Remove the ValueEventListener to prevent memory leaks
        if (userRef != null) {
            ValueEventListener valueEventListener = null;
            userRef.removeEventListener(valueEventListener);
        }
    }

    private void showLogoutConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirm Logout");
        builder.setMessage("Are you sure you want to logout?");
        builder.setPositiveButton("Logout", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Clear any session data or perform necessary cleanup
                clearSessionData();

                // Redirect to LoginActivity
                Intent intent = new Intent(SettingsActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });
        builder.setNegativeButton("Cancel", null);
        builder.show();
    }

    // Implement this method to clear session data or perform any necessary cleanup
    private void clearSessionData() {
        // Clear any data stored in shared preferences (if applicable)
        SharedPreferences preferences = getSharedPreferences("login-signup-firebase", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();

        // Clear any local variables or flags
        // Example:
        // loggedInUser = null;

        // Remove any Firebase Realtime Database listeners (if applicable)
        // Example:
        // if (databaseReference != null && valueEventListener != null) {
        //     databaseReference.removeEventListener(valueEventListener);
        // }
    }
}
