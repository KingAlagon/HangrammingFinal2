package com.example.hangramming;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class CategoryActivity extends AppCompatActivity {

    private Button categoryButton1;
    private Button categoryButton2;
    private Button categoryButton3;
    // Add more category buttons as needed

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        // Initialize UI elements
        categoryButton1 = findViewById(R.id.java);
        categoryButton2 = findViewById(R.id.python);
        categoryButton3 = findViewById(R.id.webdev);
        // Initialize more category buttons as needed

        // Set click listeners for category buttons
        categoryButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startGameWithCategory("Java");
            }
        });

        categoryButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startGameWithCategory("Python");
            }
        });
        categoryButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startGameWithCategory("Web Development");
            }
        });

        // Set click listeners for more category buttons as needed
    }

    public void startGameWithCategory(String selectedCategory) {
        // Pass the selected category to the game activity (replace GameActivity.class with your actual game activity)
        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtra("selected_category", selectedCategory);
        startActivity(intent);
    }
}
