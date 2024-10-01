package com.labibkamran.adminnustfruta;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.labibkamran.adminnustfruta.Adapter.MenuAdapter;
import com.labibkamran.adminnustfruta.Models.Allmenu;
import com.labibkamran.adminnustfruta.databinding.ActivityAllItemBinding;

import java.util.ArrayList;

public class AllItemActivity extends AppCompatActivity {

    DatabaseReference databaseReference;
    ArrayList<Allmenu> menuItems = new ArrayList<>();
    ActivityAllItemBinding binding;
    private MenuAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAllItemBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        databaseReference = FirebaseDatabase.getInstance().getReference().child("menu");
        retrieveMenuItems();

        binding.backButton.setOnClickListener(v -> finish());
    }

    private void retrieveMenuItems() {
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                menuItems.clear();
                for (DataSnapshot foodSnapshot : snapshot.getChildren()) {
                    Allmenu menuItem = foodSnapshot.getValue(Allmenu.class);
                    if (menuItem != null) {
                        menuItem.setKey(foodSnapshot.getKey());
                        menuItems.add(menuItem);
                    }
                }
                setAdapter();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(AllItemActivity.this, "Error fetching data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setAdapter() {
        adapter = new MenuAdapter(this, menuItems, position -> deleteMenuItem(position));
        binding.menuRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        binding.menuRecyclerView.setAdapter(adapter);
    }

    private void deleteMenuItem(int position) {
        Allmenu menuItemToDelete = menuItems.get(position);
        String menuItemKey = menuItemToDelete.getKey();
        DatabaseReference foodMenuReference = databaseReference.child(menuItemKey);

        foodMenuReference.removeValue().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                menuItems.remove(position);
                adapter.notifyItemRemoved(position);
                Toast.makeText(this, "Item Deleted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Item not Deleted", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
