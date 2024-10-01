package com.labibkamran.adminnustfruta;

import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.labibkamran.adminnustfruta.Adapter.OrderDetailsAdapter;
import com.labibkamran.adminnustfruta.Models.OrderDetails;
import com.labibkamran.adminnustfruta.databinding.ActivityOrderDetailsBinding;

import java.util.ArrayList;

public class orderDetailsActivity extends AppCompatActivity {
    ActivityOrderDetailsBinding binding;
    private String userName = null;
    private String address = null;
    private String phoneNumber = null;
    private String TotalPrice = null;
    private ArrayList<String> foodNames = new ArrayList<>();
    private ArrayList<String> foodImages = new ArrayList<>();
    private ArrayList<Integer> foodQuantity = new ArrayList<>();
    private ArrayList<String> foodPrices = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityOrderDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        getDataFromIntent();
    }

    private void getDataFromIntent() {
        OrderDetails receivedOrderDetails = getIntent().getParcelableExtra("userOrderDetails");
        if(receivedOrderDetails != null){
            userName = receivedOrderDetails.getUserName();
            foodNames = receivedOrderDetails.getFoodNames();
            foodPrices = receivedOrderDetails.getFoodPrices();
            foodQuantity = receivedOrderDetails.getFoodQuantities();
            foodImages = receivedOrderDetails.getFoodImages();
            address = receivedOrderDetails.getAddress();
            phoneNumber = receivedOrderDetails.getPhoneNumber();
            TotalPrice = receivedOrderDetails.getTotalPrice();
            setUserDetail();
            setAdapter();
        }
    }

    private void setAdapter() {
        binding.orderDetailsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        OrderDetailsAdapter adapter = new OrderDetailsAdapter(foodNames, foodPrices, foodImages, foodQuantity, this);
        binding.orderDetailsRecyclerView.setAdapter(adapter);
    }

    private void setUserDetail() {
        binding.name.setText(userName);
        binding.address.setText(address);
        binding.totalAmount.setText("Rs " + TotalPrice);
        binding.phone.setText(phoneNumber);
    }
}
