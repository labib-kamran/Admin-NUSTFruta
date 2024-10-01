package com.labibkamran.adminnustfruta.Adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.labibkamran.adminnustfruta.Models.Allmenu;
import com.labibkamran.adminnustfruta.databinding.ItemAllitemBinding;

import java.util.ArrayList;
import java.util.Arrays;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.AllItemViewHolder> {

    private Context context;
    private ArrayList<Allmenu> menuList;
    private int[] itemQuantities;
    private OnDeleteClickListener onDeleteClickListener;

    public MenuAdapter(Context context, ArrayList<Allmenu> menuList, OnDeleteClickListener onDeleteClickListener) {
        this.menuList = menuList;
        this.context = context;
        this.onDeleteClickListener = onDeleteClickListener;
        this.itemQuantities = new int[menuList.size()];
        Arrays.fill(itemQuantities, 1);
    }

    @NonNull
    @Override
    public AllItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemAllitemBinding binding = ItemAllitemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new AllItemViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull AllItemViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return menuList.size();
    }

    public interface OnDeleteClickListener {
        void onDeleteClick(int position);
    }

    public class AllItemViewHolder extends RecyclerView.ViewHolder {
        private ItemAllitemBinding binding;

        public AllItemViewHolder(ItemAllitemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(int position) {
            int quantity = itemQuantities[position];
            Allmenu menuItem = menuList.get(position);
            String uriString = menuItem.getFoodImage();
            Uri uri = Uri.parse(uriString);

            binding.nameItems.setText(menuItem.getFoodname());
            binding.ItemPrice.setText(menuItem.getFoodprice());
            Glide.with(context).load(uri).into(binding.itemImage);
            binding.ItemQuantity.setText(String.valueOf(quantity));

            binding.minusButton.setOnClickListener(v -> decreaseQuantity(position));
            binding.acceptButton.setOnClickListener(v -> increaseQuantity(position));
            binding.deleteButton.setOnClickListener(v -> onDeleteClickListener.onDeleteClick(position));
        }

        public void decreaseQuantity(int position) {
            if (itemQuantities[position] > 0) {
                itemQuantities[position]--;
                binding.ItemQuantity.setText(String.valueOf(itemQuantities[position]));
            }
        }

        public void increaseQuantity(int position) {
            itemQuantities[position]++;
            binding.ItemQuantity.setText(String.valueOf(itemQuantities[position]));
        }
    }
}
