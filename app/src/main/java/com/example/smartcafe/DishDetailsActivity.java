package com.example.smartcafe;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class DishDetailsActivity extends AppCompatActivity {

    public static final String EXTRA_DISH_ID = "extra_dish_id";

    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dish_details);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        RatingBar userRatingBar = findViewById(R.id.user_rating_bar);
        EditText commentEditText = findViewById(R.id.comment_edit_text);
        Button submitFeedbackButton = findViewById(R.id.submit_feedback_button);

        String dishId = getIntent().getStringExtra(EXTRA_DISH_ID);

        submitFeedbackButton.setOnClickListener(v -> {
            FirebaseUser currentUser = mAuth.getCurrentUser();
            if (currentUser == null) {
                Toast.makeText(this, "You must be logged in to submit feedback", Toast.LENGTH_SHORT).show();
                return;
            }
            if (dishId == null) {
                Toast.makeText(this, "Error: Dish ID is missing", Toast.LENGTH_SHORT).show();
                return;
            }

            float rating = userRatingBar.getRating();
            String comment = commentEditText.getText().toString().trim();
            String userId = currentUser.getUid();

            if (rating > 0) {
                Feedback feedback = new Feedback(userId, dishId, rating, comment);
                db.collection("feedback").add(feedback)
                        .addOnSuccessListener(documentReference -> {
                            Toast.makeText(DishDetailsActivity.this, "Feedback submitted successfully!", Toast.LENGTH_SHORT).show();
                            finish(); // Close the activity after submission
                        })
                        .addOnFailureListener(e -> Toast.makeText(DishDetailsActivity.this, "Error submitting feedback", Toast.LENGTH_SHORT).show());
            } else {
                Toast.makeText(this, "Please provide a rating", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
