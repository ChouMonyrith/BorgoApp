package com.example.borgo.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.borgo.R;
import com.example.borgo.activity.MainActivity;
import com.example.borgo.data.model.MenuItem;
import com.example.borgo.manager.CartManager;
import com.example.borgo.data.model.CartItem;
import com.example.borgo.data.model.Order;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {
    private static final String TAG = "OrderAdapter";

    private Context context;
    private List<Order> orders;
    private NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(Locale.US);

    public OrderAdapter(Context context) {
        this.context = context;
        this.orders = new ArrayList<>();
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.order_row, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Order order = orders.get(position);
        
        // Log order details for debugging
        Log.d(TAG, "Displaying order at position " + position);
        Log.d(TAG, "Order subtotal: " + order.getSubtotal());
        Log.d(TAG, "Order tax: " + order.getTax());
        Log.d(TAG, "Order total: " + order.getTotal());
        Log.d(TAG, "Order items count: " + order.getItems().size());

        StringBuilder details = new StringBuilder();
        // Check if order items is not null before iterating
        if (order.getItems() != null) {
            for (CartItem item : order.getItems()) {
                String itemName = item.getFood() != null ? item.getFood().getName() : "Unknown Item";
                int quantity = item.getQuantity();
                double itemPrice = item.getFood() != null ? item.getFood().getPrice() : 0.0;
                double itemTotal = itemPrice * quantity;
                
                Log.d(TAG, "Order item: " + itemName + ", Quantity: " + quantity + ", Price: " + itemPrice);
                
                details.append(String.format(Locale.US, "%s (x%d) - %s\n",
                        itemName,
                        quantity,
                        currencyFormat.format(itemTotal)));
            }
        }

        holder.orderDetails.setText(details.toString().trim());
        holder.subtotal.setText("Subtotal: " + currencyFormat.format(order.getSubtotal()));
        holder.tax.setText("Tax: " + currencyFormat.format(order.getTax()));
        holder.total.setText("Total: " + currencyFormat.format(order.getTotal()));

        holder.reorderButton.setOnClickListener(v -> {
            // Clear the cart first before reordering
            CartManager.getInstance().clearCart();
            
            // Check if order items is not null before iterating
            if (order.getItems() != null) {
                for (CartItem item : order.getItems()) {
                    MenuItem food = item.getFood();
                    if (food != null) {
                        CartManager.getInstance().addItemToCart(food, item.getQuantity());
                    }
                }
                Intent intent = new Intent(context, MainActivity.class);
                intent.putExtra("fragment_to_load", "CartFragment");
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return orders != null ? orders.size() : 0;
    }
    
    public void updateOrders(List<Order> newOrders) {
        // Handle null list by using an empty list instead
        if (newOrders != null) {
            this.orders = newOrders;
        } else {
            this.orders = new ArrayList<>();
        }
        Log.d(TAG, "Updated orders list with " + this.orders.size() + " orders");
        notifyDataSetChanged();
    }

    static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView orderDetails, subtotal, tax, total;
        Button reorderButton;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            orderDetails = itemView.findViewById(R.id.order_details);
            subtotal = itemView.findViewById(R.id.order_subtotal);
            tax = itemView.findViewById(R.id.order_tax);
            total = itemView.findViewById(R.id.order_total);
            reorderButton = itemView.findViewById(R.id.reorder_button);
        }
    }
}