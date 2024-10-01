package com.labibkamran.adminnustfruta;

import static android.app.PendingIntent.getActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.labibkamran.adminnustfruta.Models.OrderDetails;
import com.labibkamran.adminnustfruta.databinding.ActivityMainBinding;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    private FirebaseDatabase database;
    private FirebaseAuth auth;
    private DatabaseReference completedOrderRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initialize Firebase Auth and Database
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        binding.addMenu.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), AddItemActivity.class);
            startActivity(intent);
        });

        binding.allItemmenu.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), AllItemActivity.class);
            startActivity(intent);
        });

        binding.ourfordelivery.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), OutForDeliveryActivity.class);
            startActivity(intent);
        });

        binding.profile.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), AdminProfile.class);
            startActivity(intent);
        });

        binding.createnewuser.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), CreateUser.class);
            startActivity(intent);
        });
        binding.signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth.signOut();
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });

        binding.pendingOrdertextView.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), PendingOrders.class);
            startActivity(intent);
        });

        pendingOrders();
        completedOrders();
       // wholeTiemEarning();
    }

    private void wholeTiemEarning() {
        final int[] totalEarnings = {0};
        completedOrderRef = database.getReference().child("CompletedOrders");
        completedOrderRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot orderSnapshot : dataSnapshot.getChildren()) {
                    OrderDetails completeOrder = orderSnapshot.getValue(OrderDetails.class);
                    if (completeOrder != null) {
                        String price = completeOrder.getTotalPrice().replace("$", "");
                        try {
                            int priceValue = Integer.parseInt(price);
                            totalEarnings[0] += priceValue;
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                        }
                    }
                }
                binding.wholeTimeEarning.setText("Rs " + totalEarnings[0]);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle possible errors.
            }
        });
    }

    private void completedOrders() {
        final int[] completedOrdersCount = {0};
        completedOrderRef = database.getReference().child("CompletedOrders");
        completedOrderRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                completedOrdersCount[0] = (int) snapshot.getChildrenCount();
                binding.completedOrders.setText(String.valueOf(completedOrdersCount[0]));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle possible errors.
            }
        });
    }

    private void pendingOrders() {
        DatabaseReference pendingOrderRef = database.getReference().child("OrderDetails");
        final int[] pendingOrdersCount = {0};
        pendingOrderRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                pendingOrdersCount[0] = (int) snapshot.getChildrenCount();
                binding.pendingOrders.setText(String.valueOf(pendingOrdersCount[0]));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle possible errors.
            }
        });
    }
}
