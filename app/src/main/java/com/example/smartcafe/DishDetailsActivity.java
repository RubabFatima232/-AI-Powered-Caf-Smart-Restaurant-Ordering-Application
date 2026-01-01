package com.example.smartcafe;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.FirebaseFirestore;

public class DishDetailsActivity extends AppCompatActivity {

    public static final String EXTRA_DISH_ID = "extra_dish_id";
    private static final String TAG = "DishDetailsActivity";

    private FirebaseFirestore db;
    private Dish currentDish;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dish_details);

        db = FirebaseFirestore.getInstance();

        ImageView dishImageView = findViewById(R.id.dish_image);
        TextView dishNameTextView = findViewById(R.id.dish_name);
        TextView dishDescriptionTextView = findViewById(R.id.dish_description);
        RatingBar userRatingBar = findViewById(R.id.user_rating_bar);
        EditText commentEditText = findViewById(R.id.comment_edit_text);
        Button submitFeedbackButton = findViewById(R.id.submit_feedback_button);
        Button addToCartButton = findViewById(R.id.add_to_cart_button);

        String dishId = getIntent().getStringExtra(EXTRA_DISH_ID);

        if (dishId == null) {
            Toast.makeText(this, "Error: Dish ID is missing", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        loadDishDetails(dishId, dishImageView, dishNameTextView, dishDescriptionTextView);

        addToCartButton.setOnClickListener(v -> {
            if (currentDish != null) {
                CartManager.getInstance().addToCart(currentDish);
                Toast.makeText(this, currentDish.getName() + " added to cart", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Dish details not loaded yet.", Toast.LENGTH_SHORT).show();
            }
        });

        submitFeedbackButton.setOnClickListener(v -> {
            float rating = userRatingBar.getRating();
            String comment = commentEditText.getText().toString().trim();
            String userId = "guest_user"; // Placeholder for guest feedback

            if (rating > 0) {
                Feedback feedback = new Feedback(userId, dishId, rating, comment);
                db.collection("feedback").add(feedback)
                        .addOnSuccessListener(documentReference -> {
                            Toast.makeText(DishDetailsActivity.this, "Feedback submitted successfully!", Toast.LENGTH_SHORT).show();
                            finish();
                        })
                        .addOnFailureListener(e -> Toast.makeText(DishDetailsActivity.this, "Error submitting feedback", Toast.LENGTH_SHORT).show());
            } else {
                Toast.makeText(this, "Please provide a rating", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadDishDetails(String dishId, ImageView imageView, TextView nameView, TextView descriptionView) {
        db.collection("menu").document(dishId).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        currentDish = documentSnapshot.toObject(Dish.class);
                        if (currentDish != null) {
                            currentDish.setDocumentId(documentSnapshot.getId());
                            nameView.setText(currentDish.getName());
                            descriptionView.setText(currentDish.getDescription());
                            if (currentDish.getImageUrl() != null && !currentDish.getImageUrl().isEmpty()) {
                                Glide.with(this).load(currentDish.getImageUrl()).into(imageView);
                            }
                        }
                    } else {
                        Toast.makeText(this, "Dish not found", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error loading dish details", e);
                    Toast.makeText(this, "Error loading dish details.", Toast.LENGTH_SHORT).show();
                });
    }
}
