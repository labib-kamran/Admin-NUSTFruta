package com.labibkamran.adminnustfruta;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.labibkamran.adminnustfruta.Adapter.DeliveryAdapter;
import com.labibkamran.adminnustfruta.Models.OrderDetails;
import com.labibkamran.adminnustfruta.databinding.ActivityOutForDeliveryBinding;

import java.util.ArrayList;

public class OutForDeliveryActivity extends AppCompatActivity implements DeliveryAdapter.OnItemClicked {

    private ActivityOutForDeliveryBinding binding;
    private ArrayList<String> listOfName = new ArrayList<>();
    private ArrayList<Boolean> listOfMoneyStatus = new ArrayList<>();
    private ArrayList<OrderDetails> listOfOrderItem = new ArrayList<>();
    private DatabaseReference completedOrdersRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityOutForDeliveryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initialize Firebase reference
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        completedOrdersRef = database.getReference().child("CompletedOrders");

        // Set up the back button click listener
        //binding.backButton.setOnClickListener(v -> finish());
        binding.backButton.setVisibility(View.INVISIBLE);


        // Fetch order details
        getOrderDetails();
    }

    private void getOrderDetails() {
        completedOrdersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listOfOrderItem.clear();  // Clear previous data to avoid duplication
                for (DataSnapshot orderSnapshot : snapshot.getChildren()) {
                    OrderDetails orderDetails = orderSnapshot.getValue(OrderDetails.class);
                    if (orderDetails != null) {
                        listOfOrderItem.add(orderDetails);
                    }
                }
                addDataToListForRecyclerView();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(OutForDeliveryActivity.this, "Failed to load data.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addDataToListForRecyclerView() {
        listOfName.clear();
        listOfMoneyStatus.clear();

        for (OrderDetails orderItem : listOfOrderItem) {
            if (orderItem.getUserName() != null) {
                listOfName.add(orderItem.getUserName());
            }
            listOfMoneyStatus.add(orderItem.isPaymentReceived());  // Directly add the boolean value
        }

        // Set the adapter to the RecyclerView
        setAdapter();
    }

    private void setAdapter() {
        binding.outfordeliveryrecyclerview.setLayoutManager(new LinearLayoutManager(this));
        DeliveryAdapter adapter = new DeliveryAdapter(listOfName, listOfMoneyStatus, listOfOrderItem, this);
        adapter.setOnItemClickListener(this);
        binding.outfordeliveryrecyclerview.setAdapter(adapter);
    }

    @Override
    public void onItemClickListener(int position) {
        Intent intent = new Intent(this, orderDetailsActivity.class);
        OrderDetails userOrderDetails = listOfOrderItem.get(position);
        intent.putExtra("userOrderDetails", userOrderDetails);
        startActivity(intent);
    }
}
