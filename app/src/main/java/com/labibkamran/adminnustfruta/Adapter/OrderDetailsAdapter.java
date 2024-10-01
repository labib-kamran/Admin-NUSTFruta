package com.labibkamran.adminnustfruta.Adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.labibkamran.adminnustfruta.orderDetailsActivity;
import com.labibkamran.adminnustfruta.databinding.OrderDetailsItemsBinding;

import java.util.ArrayList;

public class OrderDetailsAdapter extends RecyclerView.Adapter<OrderDetailsAdapter.OrderViewHolder> {

     private Context context;
    private ArrayList<String> foodNames = new ArrayList<>();
    private ArrayList<String> foodImages = new ArrayList<>();
    private ArrayList<Integer> foodQuantity = new ArrayList<Integer>();
    private ArrayList<String> foodPrices = new ArrayList<>();

    OrderDetailsItemsBinding binding;

    public OrderDetailsAdapter(ArrayList<String> foodNames, ArrayList<String> foodPrices, ArrayList<String> foodImages, ArrayList<Integer> foodQuantity, orderDetailsActivity context) {

        this.foodNames = foodNames;
        this.foodPrices = foodPrices;
        this.foodImages = foodImages;
        this.foodQuantity = foodQuantity;
        this.context = context;
    }

    @NonNull
    @Override
    public OrderDetailsAdapter.OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = OrderDetailsItemsBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new OrderViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderDetailsAdapter.OrderViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return foodNames.size();
    }

    public class OrderViewHolder extends RecyclerView.ViewHolder {
        OrderDetailsItemsBinding binding;
        public OrderViewHolder(@NonNull OrderDetailsItemsBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(int position) {
            binding.foodname.setText(foodNames.get(position));
            binding.foodprice.setText(foodPrices.get(position));
            binding.foodQuantity.setText(Integer.toString(foodQuantity.get(position)));
            Uri uri = Uri.parse(foodImages.get(position));
            Glide.with(context).load(uri).into(binding.orderitemImage);
        }
    }
}
