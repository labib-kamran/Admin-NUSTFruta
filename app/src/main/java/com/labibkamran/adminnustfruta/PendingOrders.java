package com.labibkamran.adminnustfruta;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.labibkamran.adminnustfruta.Adapter.PendingOrderAdapter;
import com.labibkamran.adminnustfruta.Models.OrderDetails;
import com.labibkamran.adminnustfruta.databinding.ActivityPendingOrdersBinding;

import java.util.ArrayList;

public class PendingOrders extends AppCompatActivity implements PendingOrderAdapter.OnItemClicked {

    private ActivityPendingOrdersBinding binding;
    private ArrayList<String> listOfName = new ArrayList<>();
    private ArrayList<String> listOfTotalPrice = new ArrayList<>();
    private ArrayList<String> listOfImageFirstFood = new ArrayList<>();
    private ArrayList<OrderDetails> listOfOrderItem = new ArrayList<>();
    private FirebaseDatabase database;
    private DatabaseReference orderDetailsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityPendingOrdersBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        database = FirebaseDatabase.getInstance();
        orderDetailsRef = database.getReference().child("OrderDetails");
        binding.backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        // getting order details
        getOrderDetails();

    }

    private void getOrderDetails() {
        // retrieving items from firebase
        orderDetailsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot orederSnapshot : snapshot.getChildren()) {
                    OrderDetails orderDetails = orederSnapshot.getValue(OrderDetails.class);
                    if (orderDetails != null)
                        listOfOrderItem.add(orderDetails);
                }
                addDataToListForRecyclerView();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void addDataToListForRecyclerView() {
        for (OrderDetails orderItem : listOfOrderItem) {
            // add data to respective list for populating the recyclerView
            if (orderItem.getUserName() != null) {
                listOfName.add(orderItem.getUserName());
            }
            if (orderItem.getTotalPrice() != null) {
                listOfTotalPrice.add(orderItem.getTotalPrice());
            }
            if (orderItem.getFoodImages() != null) {
                for (String image : orderItem.getFoodImages()) {
                    if (!image.isEmpty()) {
                        listOfImageFirstFood.add(image);
                    }
                }
            }
        }
        setAdapter();
    }

    private void setAdapter() {
        binding.pendingorderrecyclerview.setLayoutManager(new LinearLayoutManager(this));
        PendingOrderAdapter adapter = new PendingOrderAdapter(this, listOfName, listOfTotalPrice, listOfImageFirstFood);
        adapter.setOnItemClickListener(this);
        binding.pendingorderrecyclerview.setAdapter(adapter);
    }

    @Override
    public void onItemClickListener(int positon) {
        Intent intent = new Intent(this, orderDetailsActivity.class);
        OrderDetails userOrderDetails = listOfOrderItem.get(positon);
        intent.putExtra("userOrderDetails", userOrderDetails);
        startActivity(intent);

    }

    @Override
    public void onItemAcceptClickListener(int position) {
        // handle item acceptance and update database
        String childItemPushKey = listOfOrderItem.get(position).getItemPushKey();
        if (childItemPushKey != null) {
            DatabaseReference clickItemOrderReference = database.getReference().child("OrderDetails").child(childItemPushKey);
            clickItemOrderReference.child("orderAccepted").setValue(true);
            updateOrderAcceptStatus(position);
        }

    }

    @Override
    public void onItemDispatchClickListener(int position) {
        // handke item dispatch
        String dispatchItemPushkey = listOfOrderItem.get(position).getItemPushKey();
        DatabaseReference dispatchItemOrderRef = database.getReference().child("CompletedOrders").child(dispatchItemPushkey);
        dispatchItemOrderRef.setValue(listOfOrderItem.get(position)).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                deleteTheItemFromOrderDetails(dispatchItemPushkey);
            }
        });


    }

    private void deleteTheItemFromOrderDetails(String dispatchItemPushkey) {
        DatabaseReference orderDetailsItemsRef = database.getReference().child("OrderDetails").child(dispatchItemPushkey);
        orderDetailsItemsRef.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(PendingOrders.this, "Order Successfully removed from pending order", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(PendingOrders.this, "Order removal unsuccessful", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateOrderAcceptStatus(int position) {
        // update order history and order detail
        String userIdOfClickedItem = listOfOrderItem.get(position).getUserUid();
        String pushkeyofclickeditem = listOfOrderItem.get(position).getItemPushKey();
        DatabaseReference historyRef = database.getReference().child("user").child(userIdOfClickedItem).child("BuyHistory").child(pushkeyofclickeditem);
        historyRef.child("orderAccepted").setValue(true);
        orderDetailsRef.child(pushkeyofclickeditem).child("orderAccepted").setValue(true);
    }
    @Override
    public void onItemRejectClickListener(int position) {
        if (position >= 0 && position < listOfOrderItem.size()) {
            OrderDetails orderDetails = listOfOrderItem.get(position);
            String rejectedItemPushkey = orderDetails.getItemPushKey();
            String userIdOfClickedItem = orderDetails.getUserUid();

            DatabaseReference rejectedItemOrderRef = database.getReference().child("RejectedOrders").child(rejectedItemPushkey);
            rejectedItemOrderRef.setValue(orderDetails).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    updateUserBuyHistory(userIdOfClickedItem, rejectedItemPushkey);
                    deleteTheItemFromOrderDetails(rejectedItemPushkey);
                }
            });
        }
    }

    private void updateUserBuyHistory(String userId, String pushKey) {
        DatabaseReference userBuyHistoryRef = database.getReference().child("user").child(userId).child("BuyHistory").child(pushKey);
        userBuyHistoryRef.child("orderRejected").setValue(true);
    }

}