package com.labibkamran.adminnustfruta;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.labibkamran.adminnustfruta.Models.AdminModel;
import com.labibkamran.adminnustfruta.databinding.ActivityAdminProfileBinding;

public class AdminProfile extends AppCompatActivity {

    private ActivityAdminProfileBinding binding;
    private FirebaseAuth auth;
    private FirebaseDatabase database;
    private DatabaseReference adminRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAdminProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        adminRef = database.getReference().child("admin").child(auth.getCurrentUser().getUid());

        binding.backButton.setOnClickListener(v -> finish());
        binding.name.setEnabled(false);
        binding.address.setEnabled(false);
        binding.email.setEnabled(false);
        binding.phone.setEnabled(false);
        binding.designation.setEnabled(false);
        binding.password.setEnabled(false);
        binding.saveInfoButton.setEnabled(false);

        final Boolean[] isEditable = {false};
        binding.editbutton.setOnClickListener(v -> {
            isEditable[0] = !isEditable[0];
            setEditingEnabled(isEditable[0]);
        });

        binding.saveInfoButton.setOnClickListener(v -> saveUserData());

        retrieveUserData();
    }


    private void setEditingEnabled(boolean enabled) {
        binding.name.setEnabled(enabled);
        binding.address.setEnabled(enabled);
        binding.phone.setEnabled(enabled);
        binding.saveInfoButton.setEnabled(enabled);
        if (enabled) {
            binding.name.requestFocus();
        }
    }

    private void retrieveUserData() {
        adminRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    AdminModel adminProfile = snapshot.getValue(AdminModel.class);
                    if (adminProfile != null) {
                        binding.name.setText(adminProfile.getName());
                        binding.address.setText(adminProfile.getAddress());
                        binding.email.setText(adminProfile.getEmail());
                        binding.phone.setText(adminProfile.getPhoneNumber());
                        binding.designation.setText(adminProfile.getDesignation());
                    }
                } else {

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(AdminProfile.this, "Failed to retrieve data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveUserData() {
        String name = binding.name.getText().toString().trim();
        String address = binding.address.getText().toString().trim();
        String email = binding.email.getText().toString().trim();
        String phone = binding.phone.getText().toString().trim();
        String password = binding.password.getText().toString().trim();

        adminRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    AdminModel adminProfile = snapshot.getValue(AdminModel.class);
                    if (adminProfile != null) {
                        adminProfile.setName(name);
                        adminProfile.setAddress(address);
                        adminProfile.setEmail(email);
                        adminProfile.setPhoneNumber(phone);
                        adminRef.setValue(adminProfile).addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Toast.makeText(AdminProfile.this, "Data saved successfully", Toast.LENGTH_SHORT).show();
                                setEditingEnabled(false);
                            } else {
                                Toast.makeText(AdminProfile.this, "Failed to save data", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(AdminProfile.this, "Failed to retrieve data", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
