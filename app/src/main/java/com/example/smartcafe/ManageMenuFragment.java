package com.example.smartcafe;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ManageMenuFragment extends Fragment implements AdminMenuAdapter.OnDishActionListener {

    private AdminMenuAdapter adapter;
    private List<Dish> menuItems;
    private FirebaseFirestore db;
    private FirebaseStorage storage;
    private ActivityResultLauncher<Intent> imagePickerLauncher;
    private Uri selectedImageUri;
    private ImageView dialogImagePreview;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        imagePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        selectedImageUri = result.getData().getData();
                        if (dialogImagePreview != null) {
                            dialogImagePreview.setImageURI(selectedImageUri);
                        }
                    }
                });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_manage_menu, container, false);

        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        RecyclerView recyclerView = view.findViewById(R.id.menu_items_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        menuItems = new ArrayList<>();
        adapter = new AdminMenuAdapter(getContext(), menuItems, this);
        recyclerView.setAdapter(adapter);

        loadMenuItems();

        FloatingActionButton fab = view.findViewById(R.id.fab_add_menu_item);
        fab.setOnClickListener(v -> showAddOrUpdateDishDialog(null));

        return view;
    }

    private void loadMenuItems() {
        db.collection("menu").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                menuItems.clear();
                for (QueryDocumentSnapshot document : task.getResult()) {
                    Dish dish = document.toObject(Dish.class);
                    dish.setDocumentId(document.getId());
                    menuItems.add(dish);
                }
                adapter.notifyDataSetChanged();
            } else {
                Toast.makeText(getContext(), "Error loading menu items.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showAddOrUpdateDishDialog(@Nullable final Dish dish) {
        selectedImageUri = null;

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(dish == null ? "Add New Dish" : "Update Dish");

        View dialogView = getLayoutInflater().inflate(R.layout.dialog_add_dish, null);
        builder.setView(dialogView);

        dialogImagePreview = dialogView.findViewById(R.id.dish_image_preview);
        Button selectImageButton = dialogView.findViewById(R.id.select_image_button);
        TextInputEditText dishNameEditText = dialogView.findViewById(R.id.dish_name_edit_text);
        TextInputEditText dishCategoryEditText = dialogView.findViewById(R.id.dish_category_edit_text);
        TextInputEditText dishDescriptionEditText = dialogView.findViewById(R.id.dish_description_edit_text);
        TextInputEditText dishPriceEditText = dialogView.findViewById(R.id.dish_price_edit_text);

        if (dish != null) {
            dishNameEditText.setText(dish.getName());
            dishCategoryEditText.setText(dish.getCategory());
            dishDescriptionEditText.setText(dish.getDescription());
            dishPriceEditText.setText(dish.getPrice().replace("$", ""));
            if (dish.getImageUrl() != null && !dish.getImageUrl().isEmpty()) {
                Glide.with(getContext()).load(dish.getImageUrl()).into(dialogImagePreview);
            }
        }

        selectImageButton.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            imagePickerLauncher.launch(intent);
        });

        builder.setPositiveButton(dish == null ? "Add" : "Update", (dialog, which) -> {
            String name = dishNameEditText.getText().toString().trim();
            String category = dishCategoryEditText.getText().toString().trim();
            String description = dishDescriptionEditText.getText().toString().trim();
            String price = dishPriceEditText.getText().toString().trim();

            if (!name.isEmpty() && !price.isEmpty() && !category.isEmpty() && !description.isEmpty()) {
                if (selectedImageUri != null) {
                    uploadImageAndSaveDish(dish, name, category, description, price);
                } else if (dish != null) {
                    updateDishInFirestore(dish, name, category, description, price, dish.getImageUrl());
                } else {
                    Toast.makeText(getContext(), "Please select an image for the new dish", Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        builder.create().show();
    }

    private void uploadImageAndSaveDish(@Nullable Dish dish, String name, String category, String description, String price) {
        StorageReference storageRef = storage.getReference().child("dish_images/" + UUID.randomUUID().toString());
        storageRef.putFile(selectedImageUri)
                .addOnSuccessListener(taskSnapshot -> storageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                    String imageUrl = uri.toString();
                    if (dish == null) {
                        addDishToFirestore(name, category, description, price, imageUrl);
                    } else {
                        updateDishInFirestore(dish, name, category, description, price, imageUrl);
                    }
                }))
                .addOnFailureListener(e -> Toast.makeText(getContext(), "Image upload failed", Toast.LENGTH_SHORT).show());
    }

    private void addDishToFirestore(String name, String category, String description, String price, String imageUrl) {
        Dish newDish = new Dish(name, "$" + price, category, description, imageUrl);
        db.collection("menu").add(newDish).addOnSuccessListener(documentReference -> {
            Toast.makeText(getContext(), "Dish added successfully", Toast.LENGTH_SHORT).show();
            loadMenuItems();
        }).addOnFailureListener(e -> {
            Toast.makeText(getContext(), "Error adding dish", Toast.LENGTH_SHORT).show();
        });
    }

    private void updateDishInFirestore(Dish dish, String newName, String newCategory, String newDescription, String newPrice, String newImageUrl) {
        Map<String, Object> updatedDish = new HashMap<>();
        updatedDish.put("name", newName);
        updatedDish.put("category", newCategory);
        updatedDish.put("description", newDescription);
        updatedDish.put("price", "$" + newPrice);
        updatedDish.put("imageUrl", newImageUrl);

        db.collection("menu").document(dish.getDocumentId()).update(updatedDish)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(getContext(), "Dish updated successfully", Toast.LENGTH_SHORT).show();
                    loadMenuItems();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "Error updating dish", Toast.LENGTH_SHORT).show();
                });
    }

    @Override
    public void onEditDish(Dish dish) {
        showAddOrUpdateDishDialog(dish);
    }

    @Override
    public void onDeleteDish(Dish dish, int position) {
        db.collection("menu").document(dish.getDocumentId()).delete()
                .addOnSuccessListener(aVoid -> {
                    menuItems.remove(position);
                    adapter.notifyItemRemoved(position);
                    adapter.notifyItemRangeChanged(position, menuItems.size());
                    Toast.makeText(getContext(), "Deleted: " + dish.getName(), Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "Error deleting dish", Toast.LENGTH_SHORT).show();
                });
    }
}
