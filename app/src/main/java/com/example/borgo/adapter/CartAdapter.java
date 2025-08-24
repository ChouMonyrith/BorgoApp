package com.example.borgo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.borgo.R;
import com.example.borgo.data.model.MenuItem;
import com.example.borgo.manager.CartManager;
import com.example.borgo.data.model.CartItem;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    public interface OnCartChangeListener {
        void onCartChanged(double subtotal, double tax, double total);
    }
    private List<CartItem> cartItems;
    private Context context;
    private NumberFormat currencyFormat;
    private OnCartChangeListener onCartChangeListener;
    private static final double TAX_RATE = 0.1;

    public CartAdapter(List<CartItem> cartItems, Context context, OnCartChangeListener onCartChangeListener) {
        this.cartItems = cartItems;
        this.context = context;
        this.currencyFormat = NumberFormat.getCurrencyInstance(Locale.US);
        this.onCartChangeListener = onCartChangeListener;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.cart_item_row, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        CartItem cartItem = cartItems.get(position);
        MenuItem food = cartItem.getFood();

        Glide.with(holder.foodImage.getContext())
                .load(food.getImage())
                .placeholder(R.drawable.burger_image)
                .error(R.drawable.burger_1)
                .into(holder.foodImage);


        holder.foodName.setText(cartItem.getFood().getName());
        holder.foodPrice.setText(currencyFormat.format(cartItem.getFood().getPrice()) + "/unit");
        holder.foodQuantity.setText("Qty: " + cartItem.getQuantity());
        holder.foodSubtotal.setText(currencyFormat.format(cartItem.getSubtotal()));

        holder.addButton.setOnClickListener(v -> {
            CartManager.getInstance().increaseQuantity(cartItem.getFood().getId(), cartItem.getFood().getName(), cartItem.getFood().getPrice());
            refreshRow(holder.getAdapterPosition());
        });

        holder.removeButton.setOnClickListener(v -> {
            CartManager.getInstance().decreaseQuantity(cartItem.getFood().getId(), cartItem.getFood().getName(), cartItem.getFood().getPrice());
            // After removing an item, we might need to update the whole list
            updateData(CartManager.getInstance().getCartItems());
        });
    }

    public void updateData(List<CartItem> items) {
        this.cartItems = items;
        notifyDataSetChanged();
    }



    private void refreshRow(int adapterPosition) {
        // Refresh the whole list to ensure consistency
        updateData(CartManager.getInstance().getCartItems());

        if (onCartChangeListener != null) {
            double subtotal = CartManager.getInstance().getSubtotal();
            double tax = CartManager.getInstance().getTax(TAX_RATE);
            double total = CartManager.getInstance().getTotal(TAX_RATE);
            onCartChangeListener.onCartChanged(subtotal, tax, total);
        }
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    public void updateCartItems(List<CartItem> items) {
        this.cartItems = items;
        notifyDataSetChanged();
    }

    public static class CartViewHolder extends RecyclerView.ViewHolder {
        ImageView foodImage;
        TextView foodName, foodPrice, foodQuantity, foodSubtotal;
        ImageButton addButton, removeButton;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);

            foodImage = itemView.findViewById(R.id.cart_item_image);
            foodName = itemView.findViewById(R.id.cart_item_name);
            foodPrice = itemView.findViewById(R.id.cart_item_price_per_unit);
            foodQuantity = itemView.findViewById(R.id.cart_item_quantity);
            foodSubtotal = itemView.findViewById(R.id.cart_item_subtotal);

            addButton = itemView.findViewById(R.id.cart_item_add_button);
            removeButton = itemView.findViewById(R.id.cart_item_remove_button);
        }
    }
}
