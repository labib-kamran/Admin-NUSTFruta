package com.labibkamran.adminnustfruta;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.OnFailureListener;
import androidx.annotation.NonNull;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.labibkamran.adminnustfruta.Models.Allmenu;
import com.labibkamran.adminnustfruta.databinding.ActivityAddItemBinding;

public class AddItemActivity extends AppCompatActivity {

    private String foodname;
    private String foodprice;
    private String fooddescription;
    private Uri foodImage = null;
    // Fire base
    FirebaseAuth auth;
    FirebaseDatabase database;
    private ActivityResultLauncher<String> mGetContent;

    ActivityAddItemBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityAddItemBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // initialize fire base
        auth = FirebaseAuth.getInstance();
        // initialize database
        database = FirebaseDatabase.getInstance();

        mGetContent = registerForActivityResult(new ActivityResultContracts.GetContent(),
                new ActivityResultCallback<Uri>() {
                    @Override
                    public void onActivityResult(Uri uri) {
                        if (uri != null) {
                            binding.selectedImage.setImageURI(uri);
                            foodImage = uri;
                        }
                    }
                });

        binding.selectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mGetContent.launch("image/*");
            }
        });

        binding.backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        binding.addItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // getting data from fields
                foodname = binding.FoodName.getText().toString();
                foodprice = binding.Price.getText().toString();
                fooddescription = binding.descrption.getText().toString();

                if (!(foodname.isEmpty() || foodprice.isEmpty() || fooddescription.isEmpty())) {
                    uploadData();
                    Toast.makeText(AddItemActivity.this, "Item added successfully", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(AddItemActivity.this, "Fill all fields", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void uploadData() {
        // get database reference
        DatabaseReference menuRef = database.getReference("menu");
        // generate a unique key
        String newItemKey = menuRef.push().getKey();

        if (foodImage != null) {
            StorageReference storage = FirebaseStorage.getInstance().getReference();
            StorageReference imgRef = storage.child("menu_images/" + newItemKey + ".jpg");
            UploadTask uploadTask = imgRef.putFile(foodImage);

            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    imgRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String downloadUrl = uri.toString();
                            String key = menuRef.push().getKey();
                            // Create a new menu item
                            Allmenu newItem = new Allmenu(foodname, foodprice, fooddescription, downloadUrl,key);
                            // You can now use 'newItem' as needed

                            if (key != null) {
                                menuRef.child(key).setValue(newItem).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(AddItemActivity.this, "Data uploaded successfully", Toast.LENGTH_SHORT).show();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(AddItemActivity.this, "Failed to upload data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(AddItemActivity.this, "Failed to upload image: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
        else {
            Toast.makeText(this, "Please select an image", Toast.LENGTH_SHORT).show();
        }
    }
}
