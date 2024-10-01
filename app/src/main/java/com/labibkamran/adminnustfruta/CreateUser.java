package com.labibkamran.adminnustfruta;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.labibkamran.adminnustfruta.Models.AdminModel;
import com.labibkamran.adminnustfruta.databinding.ActivityCreateUserBinding;

import java.util.ArrayList;
import java.util.Arrays;

public class CreateUser extends AppCompatActivity {

    private ActivityCreateUserBinding binding;
    private FirebaseAuth mAuth;
    private DatabaseReference adminRef;
    private ArrayList<String> designations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityCreateUserBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();
        adminRef = FirebaseDatabase.getInstance().getReference().child("admin");

        binding.backButton.setOnClickListener(v -> finish());
        designations = new ArrayList<>(Arrays.asList("Manager", "Delivery Boy"));
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, designations);
        binding.position.setAdapter(adapter);

        binding.btn.setOnClickListener(v -> {
            checkCredentials();
        });
    }

    private void createNewUser(String email, String designation, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // User creation successful
                            String uid = task.getResult().getUser().getUid();
                            saveUserDetails(uid, email, designation);
                        } else {
                            // If user creation fails, display a message to the user
                            Toast.makeText(CreateUser.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void saveUserDetails(String uid, String email, String designation) {
        AdminModel adminModel = new AdminModel("", "", email, "", designation);
        adminRef.child(uid).setValue(adminModel).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(CreateUser.this, "User created successfully", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(CreateUser.this, "Failed to save user data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void checkCredentials() {
        String email = binding.mail.getText().toString().trim();
        String designation = binding.position.getText().toString().trim();
        String password = binding.Password.getText().toString().trim();
        if (email.isEmpty())
            showError(binding.mail, "please fill this field");
        else if (designation.isEmpty())
            showError(binding.position, "please fill this field");
        else if (!(designations.contains(designation)))
            showError(binding.position, "please select address from list");
        else if (password.isEmpty())
            showError(binding.Password, "please fill this field");
        else {
            createNewUser(email, designation, password);
        }
    }

    private void showError(EditText input, String error) {
        input.setError(error);
        input.requestFocus();
    }
}
