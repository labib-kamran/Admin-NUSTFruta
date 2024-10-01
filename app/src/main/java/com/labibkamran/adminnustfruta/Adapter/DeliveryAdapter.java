package com.labibkamran.adminnustfruta.Adapter;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.labibkamran.adminnustfruta.Models.OrderDetails;
import com.labibkamran.adminnustfruta.R;
import com.labibkamran.adminnustfruta.databinding.DeliveryItemBinding;

import java.util.ArrayList;

public class DeliveryAdapter extends RecyclerView.Adapter<DeliveryAdapter.DeliveryViewHolder> {
    private ArrayList<String> customerName;
    private ArrayList<Boolean> moneyStatus;
    private ArrayList<OrderDetails> listOfCompletedOrders;
    private Context context;
    private OnItemClicked onItemClicked;

    public DeliveryAdapter(ArrayList<String> customerName, ArrayList<Boolean> moneyStatus, ArrayList<OrderDetails> listOfCompletedOrders, Context context) {
        this.customerName = customerName != null ? customerName : new ArrayList<>();
        this.moneyStatus = moneyStatus != null ? moneyStatus : new ArrayList<>();
        this.listOfCompletedOrders = listOfCompletedOrders != null ? listOfCompletedOrders : new ArrayList<>();
        this.context = context;
    }

    public void setOnItemClickListener(OnItemClicked onItemClicked) {
        this.onItemClicked = onItemClicked;
    }

    @NonNull
    @Override
    public DeliveryAdapter.DeliveryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        DeliveryItemBinding binding = DeliveryItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new DeliveryViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull DeliveryAdapter.DeliveryViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return customerName.size();
    }

    public class DeliveryViewHolder extends RecyclerView.ViewHolder {
        private final DeliveryItemBinding binding;
        private final int redColor;
        private final int greenColor;

        DeliveryViewHolder(DeliveryItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            redColor = ContextCompat.getColor(context, R.color.red);
            greenColor = ContextCompat.getColor(context, R.color.start_color);

            itemView.setOnClickListener(v -> {
                if (onItemClicked != null) {
                    onItemClicked.onItemClickListener(getAdapterPosition());
                }
            });
        }

        public void bind(int position) {
            binding.customername.setText(customerName.get(position));
            updatePaymentStatus(position);
            setupPaymentStatusClickListener(position);
            setupDeliveryStatusClickListener(position);
            updateDeliveryStatus(position);
        }

        private void updatePaymentStatus(int position) {
            if (moneyStatus.get(position)) {
                binding.paymentstatus.setText("Received");
                binding.paymentstatus.setTextColor(Color.GREEN);
                binding.paymentstatus.setBackgroundTintList(ColorStateList.valueOf(Color.GREEN));
            } else {
                binding.paymentstatus.setText("Not Received");
                binding.paymentstatus.setTextColor(Color.RED);
                binding.paymentstatus.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
            }
        }

        private void setupPaymentStatusClickListener(int position) {
            binding.paymentstatus.setOnClickListener(v -> {
                boolean currentStatus = moneyStatus.get(position);
                boolean newStatus = !currentStatus;
                moneyStatus.set(position, newStatus);

                if (newStatus) {
                    binding.paymentstatus.setText("Received");
                    binding.paymentstatus.setTextColor(Color.GREEN);
                    binding.paymentstatus.setBackgroundTintList(ColorStateList.valueOf(Color.GREEN));
                } else {
                    binding.paymentstatus.setText("Not Received");
                    binding.paymentstatus.setTextColor(Color.RED);
                    binding.paymentstatus.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
                }

                updateDatabasePaymentStatus(position, newStatus);
                checkAndMoveOrder(position);
            });
        }

        private void updateDatabasePaymentStatus(int position, boolean status) {
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            OrderDetails orderDetails = listOfCompletedOrders.get(position);
            String userIdOfClickedItem = orderDetails.getUserUid();
            String pushkeyofclickeditem = orderDetails.getItemPushKey();

            if (userIdOfClickedItem == null || pushkeyofclickeditem == null) {
                Toast.makeText(context, "Error: Missing data for order.", Toast.LENGTH_SHORT).show();
                return;
            }

            DatabaseReference orderRef = database.getReference().child("CompletedOrders").child(pushkeyofclickeditem);
            DatabaseReference historyRef = database.getReference().child("user").child(userIdOfClickedItem).child("BuyHistory").child(pushkeyofclickeditem);

            orderRef.child("paymentReceived").setValue(status);
            historyRef.child("paymentReceived").setValue(status);
        }

        private void setupDeliveryStatusClickListener(int position) {
            binding.deliverystatuscolor.setOnClickListener(v -> {
                int currentColor = binding.deliverystatuscolor.getBackgroundTintList().getDefaultColor();
                boolean newStatus = currentColor == redColor;

                int colorToSet = newStatus ? greenColor : redColor;
                binding.deliverystatuscolor.setBackgroundTintList(ColorStateList.valueOf(colorToSet));

                updateDatabaseDeliveryStatus(position, newStatus);
                checkAndMoveOrder(position);
            });
        }

        private void updateDatabaseDeliveryStatus(int position, boolean status) {
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            OrderDetails orderDetails = listOfCompletedOrders.get(position);
            String userIdOfClickedItem = orderDetails.getUserUid();
            String pushkeyofclickeditem = orderDetails.getItemPushKey();

            if (userIdOfClickedItem == null || pushkeyofclickeditem == null) {
                Toast.makeText(context, "Error: Missing data for order.", Toast.LENGTH_SHORT).show();
                return;
            }

            DatabaseReference orderRef = database.getReference().child("CompletedOrders").child(pushkeyofclickeditem);
            DatabaseReference historyRef = database.getReference().child("user").child(userIdOfClickedItem).child("BuyHistory").child(pushkeyofclickeditem);

            orderRef.child("orderDelivered").setValue(status);
            historyRef.child("orderDelivered").setValue(status);
        }

        private void updateDeliveryStatus(int position) {
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            OrderDetails orderDetails = listOfCompletedOrders.get(position);
            String pushkeyofclickeditem = orderDetails.getItemPushKey();

            if (pushkeyofclickeditem == null) {
                Toast.makeText(context, "Error: Missing data for order.", Toast.LENGTH_SHORT).show();
                return;
            }

            DatabaseReference orderRef = database.getReference().child("CompletedOrders").child(pushkeyofclickeditem);
            orderRef.child("orderDelivered").get().addOnCompleteListener(task -> {
                if (task.isSuccessful() && task.getResult().exists()) {
                    Boolean delivered = task.getResult().getValue(Boolean.class);
                    int colorToSet = delivered != null && delivered ? greenColor : redColor;
                    binding.deliverystatuscolor.setBackgroundTintList(ColorStateList.valueOf(colorToSet));
                }
            });
        }

        private void checkAndMoveOrder(int position) {
            if (moneyStatus.get(position) && isOrderDelivered(position)) {
                moveOrderToDelivered(position);
            }
        }

        private boolean isOrderDelivered(int position) {
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            OrderDetails orderDetails = listOfCompletedOrders.get(position);
            String pushkeyofclickeditem = orderDetails.getItemPushKey();

            DatabaseReference orderRef = database.getReference().child("CompletedOrders").child(pushkeyofclickeditem);
            orderRef.child("orderDelivered").get().addOnCompleteListener(task -> {
                if (task.isSuccessful() && task.getResult().exists()) {
                    Boolean delivered = task.getResult().getValue(Boolean.class);
                    if (delivered != null && delivered) {
                        moveOrderToDelivered(position);
                    }
                }
            });
            return false;
        }

        private void moveOrderToDelivered(int position) {
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            OrderDetails orderDetails = listOfCompletedOrders.get(position);
            String userIdOfClickedItem = orderDetails.getUserUid();
            String pushkeyofclickeditem = orderDetails.getItemPushKey();

            if (userIdOfClickedItem == null || pushkeyofclickeditem == null) {
                Toast.makeText(context, "Error: Missing data for order.", Toast.LENGTH_SHORT).show();
                return;
            }

            DatabaseReference completedOrderRef = database.getReference().child("CompletedOrders").child(pushkeyofclickeditem);
            DatabaseReference deliveredOrderRef = database.getReference().child("DeliveredOrders").child(pushkeyofclickeditem);
            DatabaseReference userHistoryRef = database.getReference().child("user").child(userIdOfClickedItem).child("BuyHistory").child(pushkeyofclickeditem);

            completedOrderRef.get().addOnCompleteListener(task -> {
                if (task.isSuccessful() && task.getResult().exists()) {
                    deliveredOrderRef.setValue(task.getResult().getValue()).addOnCompleteListener(deliveryTask -> {
                        if (deliveryTask.isSuccessful()) {
                            completedOrderRef.removeValue();
                            userHistoryRef.child("status").setValue("Delivered");

                            // Remove from local list and notify adapter
                            customerName.remove(position);
                            moneyStatus.remove(position);
                            listOfCompletedOrders.remove(position);
                            notifyItemRemoved(position);
                            notifyItemRangeChanged(position, getItemCount());
                        }
                    });
                }
            });
        }
    }

    public interface OnItemClicked {
        void onItemClickListener(int position);
    }
}
