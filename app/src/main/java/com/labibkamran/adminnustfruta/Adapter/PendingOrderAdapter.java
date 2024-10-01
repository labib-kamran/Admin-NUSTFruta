package com.labibkamran.adminnustfruta.Adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.labibkamran.adminnustfruta.databinding.PendingOrderItemBinding;

import java.util.ArrayList;

public class PendingOrderAdapter extends RecyclerView.Adapter<PendingOrderAdapter.PendingOrderViewHolder> {

    private PendingOrderItemBinding binding;
    private ArrayList<String> customerNames;
    private ArrayList<String> quantity;
    private ArrayList<String> foodImages;
    Context context;
    OnItemClicked itemClicked;

    public PendingOrderAdapter(Context context, ArrayList<String> customerName, ArrayList<String> quantity, ArrayList<String> itemImages) {
        this.customerNames = customerName;
        this.quantity = quantity;
        this.foodImages = itemImages;
        this.context = context;
    }
    public void setOnItemClickListener(OnItemClicked listener) {
        this.itemClicked = listener;
    }


    public interface  OnItemClicked {
        void onItemClickListener(int positon);
        void onItemAcceptClickListener(int positon);
        void onItemDispatchClickListener(int positon);
        void onItemRejectClickListener(int position);
    }

    @NonNull
    @Override
    public PendingOrderAdapter.PendingOrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = PendingOrderItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new PendingOrderViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull PendingOrderAdapter.PendingOrderViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return customerNames.size();
    }

    public class PendingOrderViewHolder extends RecyclerView.ViewHolder {

        public PendingOrderViewHolder(@NonNull PendingOrderItemBinding binding) {
            super(binding.getRoot());
        }

        public void bind(int position) {

            AppCompatButton appCompatButton = binding.acceptButton;

            if (!appCompatButton.getText().equals("Accept"))
                appCompatButton.setText("Accept");
            binding.customername.setText(customerNames.get(position));
            binding.ItemPrice.setText("Rs " + quantity.get(position));
            String uriString = foodImages.get(position);

            Uri uri = Uri.parse(uriString);
            Glide.with(context).load(uri).into(binding.orderitemImage);

            binding.acceptButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (appCompatButton.getText().equals("Accept")) {
                        appCompatButton.setText("Dispatch");
                        itemClicked.onItemAcceptClickListener(position);
                    } else {
                        customerNames.remove(getAdapterPosition());
                        notifyItemRemoved(getAdapterPosition());
                        itemClicked.onItemDispatchClickListener(position);
                    }
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemClicked.onItemClickListener(position);
                }
            });

            binding.rejectButton.setOnClickListener(v -> {
                int adapterPosition = getAdapterPosition();
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    customerNames.remove(adapterPosition);
                    notifyItemRemoved(adapterPosition);
                    itemClicked.onItemRejectClickListener(adapterPosition);
                }
            });
        }

    }
}
