package com.example.smartcafe;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
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
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class ManageMenuFragment extends Fragment implements AdminMenuAdapter.OnDishActionListener {

    private static final String TAG = "ManageMenuFragment";
    private AdminMenuAdapter adapter;
    private List<Dish> menuItems;
    private FirebaseFirestore db;
    private FirebaseStorage storage;
    private ActivityResultLauncher<Intent> imagePickerLauncher;
    private Uri selectedImageUri;
    private ImageView dialogImagePreview;
    private ListenerRegistration menuListener;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        imagePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null && result.getData().getData() != null) {
                        selectedImageUri = result.getData().getData();
                        if (dialogImagePreview != null) {
                            Glide.with(requireContext()).load(selectedImageUri).into(dialogImagePreview);
                        }
                    } else {
                        Toast.makeText(getContext(), "No image selected.", Toast.LENGTH_SHORT).show();
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
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        menuItems = new ArrayList<>();
        adapter = new AdminMenuAdapter(requireContext(), menuItems, this);
        recyclerView.setAdapter(adapter);

        FloatingActionButton fab = view.findViewById(R.id.fab_add_menu_item);
        fab.setOnClickListener(v -> showAddOrUpdateDishDialog(null));

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        loadMenuItems();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (menuListener != null) {
            menuListener.remove();
        }
    }

    private void loadMenuItems() {
        if (menuListener != null) menuListener.remove();
        menuListener = db.collection("menu").addSnapshotListener((snapshots, e) -> {
            if (e != null) {
                Log.w(TAG, "Listen failed.", e);
                if (isAdded()) Toast.makeText(requireContext(), "Error loading menu items.", Toast.LENGTH_SHORT).show();
                return;
            }

            if (snapshots != null && isAdded()) {
                for (DocumentChange dc : snapshots.getDocumentChanges()) {
                    Dish dish = dc.getDocument().toObject(Dish.class);
                    dish.setDocumentId(dc.getDocument().getId());

                    switch (dc.getType()) {
                        case ADDED:
                            if (!menuItems.contains(dish)) {
                                menuItems.add(dc.getNewIndex(), dish);
                                adapter.notifyItemInserted(dc.getNewIndex());
                            }
                            break;
                        case MODIFIED:
                            if (dc.getOldIndex() == dc.getNewIndex()) {
                                menuItems.set(dc.getNewIndex(), dish);
                                adapter.notifyItemChanged(dc.getNewIndex());
                            } else {
                                menuItems.remove(dc.getOldIndex());
                                menuItems.add(dc.getNewIndex(), dish);
                                adapter.notifyItemMoved(dc.getOldIndex(), dc.getNewIndex());
                            }
                            break;
                        case REMOVED:
                            menuItems.remove(dc.getOldIndex());
                            adapter.notifyItemRemoved(dc.getOldIndex());
                            break;
                    }
                }
            }
        });
    }

    private void showAddOrUpdateDishDialog(@Nullable final Dish dish) {
        selectedImageUri = null;

        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
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
            dishPriceEditText.setText(dish.getPrice());
            if (dish.getImageUrl() != null && !dish.getImageUrl().isEmpty()) {
                Glide.with(requireContext()).load(dish.getImageUrl()).placeholder(R.drawable.ic_placeholder_dish).into(dialogImagePreview);
            }
        }

        selectImageButton.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            imagePickerLauncher.launch(intent);
        });

        builder.setPositiveButton(dish == null ? "Add" : "Update", (dialog, which) -> {
            // Implemented in OnShowListener to prevent dialog from closing on invalid input
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        AlertDialog dialog = builder.create();
        dialog.setOnShowListener(dialogInterface -> {
            Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            positiveButton.setOnClickListener(v -> {
                String name = Objects.requireNonNull(dishNameEditText.getText()).toString().trim();
                String category = Objects.requireNonNull(dishCategoryEditText.getText()).toString().trim();
                String description = Objects.requireNonNull(dishDescriptionEditText.getText()).toString().trim();
                String price = Objects.requireNonNull(dishPriceEditText.getText()).toString().trim();

                if (TextUtils.isEmpty(name) || TextUtils.isEmpty(category) || TextUtils.isEmpty(description) || TextUtils.isEmpty(price)) {
                    Toast.makeText(getContext(), "All fields are required.", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (dish == null && selectedImageUri == null) {
                    Toast.makeText(getContext(), "Please select an image for the new dish.", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (selectedImageUri != null) {
                    uploadImageAndSaveDish(dish, name, category, description, price, dialog);
                } else if (dish != null) {
                    updateDishInFirestore(dish.getDocumentId(), name, category, description, price, dish.getImageUrl(), dialog);
                }
            });
        });

        dialog.show();
    }

    private void uploadImageAndSaveDish(@Nullable Dish dish, String name, String category, String description, String price, AlertDialog dialog) {
        Toast.makeText(getContext(), "Uploading image...", Toast.LENGTH_SHORT).show();
        StorageReference storageRef = storage.getReference().child("dish_images/" + UUID.randomUUID().toString());
        storageRef.putFile(selectedImageUri)
                .addOnSuccessListener(taskSnapshot -> storageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                    String imageUrl = uri.toString();
                    String documentId = (dish != null) ? dish.getDocumentId() : null;
                    updateDishInFirestore(documentId, name, category, description, price, imageUrl, dialog);
                }))
                .addOnFailureListener(e -> {
                    Toast.makeText(requireContext(), "Image upload failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    Log.e(TAG, "Image upload failed", e);
                });
    }

    private void updateDishInFirestore(@Nullable String documentId, String name, String category, String description, String price, String imageUrl, AlertDialog dialog) {
        Map<String, Object> dishData = new HashMap<>();
        dishData.put("name", name);
        dishData.put("category", category);
        dishData.put("description", description);
        dishData.put("price", price);
        dishData.put("imageUrl", imageUrl);

        if (documentId != null) {
            db.collection("menu").document(documentId).update(dishData)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(requireContext(), "Dish updated successfully", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    })
                    .addOnFailureListener(e -> Toast.makeText(requireContext(), "Error updating dish", Toast.LENGTH_SHORT).show());
        } else {
            db.collection("menu").add(dishData)
                    .addOnSuccessListener(documentReference -> {
                        Toast.makeText(requireContext(), "Dish added successfully", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    })
                    .addOnFailureListener(e -> Toast.makeText(requireContext(), "Error adding dish", Toast.LENGTH_SHORT).show());
        }
    }


    @Override
    public void onEditDish(Dish dish) {
        showAddOrUpdateDishDialog(dish);
    }

    @Override
    public void onDeleteDish(Dish dish, int position) {
        new AlertDialog.Builder(requireContext())
                .setTitle("Delete Dish")
                .setMessage("Are you sure you want to delete '" + dish.getName() + "'?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    db.collection("menu").document(dish.getDocumentId()).delete()
                            .addOnSuccessListener(aVoid -> Toast.makeText(requireContext(), "Deleted: " + dish.getName(), Toast.LENGTH_SHORT).show())
                            .addOnFailureListener(e -> Toast.makeText(requireContext(), "Error deleting dish", Toast.LENGTH_SHORT).show());
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}
