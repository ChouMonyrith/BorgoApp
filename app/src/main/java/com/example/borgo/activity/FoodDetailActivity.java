package com.example.borgo.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.borgo.R;
import com.example.borgo.data.model.MenuItem;
import com.example.borgo.manager.CartManager;

import java.text.NumberFormat;
import java.util.Locale;

public class FoodDetailActivity extends AppCompatActivity {

    private static final String TAG = "FoodDetailActivity";
    
    private ImageView foodImage;
    private TextView nameTextView, descriptionTextView, priceValueTextView;
    private TextView quantityValueTextView, subtotalValueTextView;
    private ImageButton increaseButton, decreaseButton;
    private View addToCartButton;

    private int quantity = 1;
    private MenuItem food;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_details); // Use your actual layout filename

        foodImage = findViewById(R.id.food_description_image);
        nameTextView = findViewById(R.id.name_textview);
        descriptionTextView = findViewById(R.id.description_textview);
        priceValueTextView = findViewById(R.id.price_value_textview);
        quantityValueTextView = findViewById(R.id.quantity_value_textview);
        subtotalValueTextView = findViewById(R.id.subtotal_value_textview);
        increaseButton = findViewById(R.id.increase_quantity_button);
        decreaseButton = findViewById(R.id.decrease_quantity_button);
        addToCartButton = findViewById(R.id.check_out_button);

        // Load Food from Intent
        food = getIntent().getParcelableExtra("food");

        if (food != null) {
            // Set food image with proper null checking
            if (food.getImage() != null && !food.getImage().isEmpty()) {
                try {
                    Glide.with(this)
                            .load(food.getImage()) // works now since model has imageUrl mapped
                            .placeholder(R.drawable.burger_image)
                            .error(R.drawable.burger_1)
                            .into(foodImage);
                } catch (NumberFormatException e) {
                    Log.e(TAG, "Invalid image resource ID: " + food.getImage(), e);
                    // Set a default image when parsing fails
                    foodImage.setImageResource(R.drawable.burger_image);
                }
            } else {
                // Set a default image when image is null or empty
                foodImage.setImageResource(R.drawable.burger_image);
            }
            
            nameTextView.setText(food.getName());
            descriptionTextView.setText(food.getDescription());
            priceValueTextView.setText(formatCurrency(food.getPrice()));
            updateSubtotal();
        }

        increaseButton.setOnClickListener(v -> {
            quantity++;
            updateSubtotal();
        });

        decreaseButton.setOnClickListener(v -> {
            if (quantity > 1) {
                quantity--;
                updateSubtotal();
            }
        });

        addToCartButton.setOnClickListener(v -> {
            if (food != null) {
                CartManager.getInstance().addItemToCart(food, quantity);
                Toast.makeText(this, "Added to cart", Toast.LENGTH_SHORT).show();
                finish(); // return to previous screen
            } else {
                Toast.makeText(this, "Unable to add item to cart", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateSubtotal() {
        quantityValueTextView.setText(String.valueOf(quantity));
        if (food != null) {
            double subtotal = food.getPrice() * quantity;
            subtotalValueTextView.setText(formatCurrency(subtotal));
        }
    }

    private String formatCurrency(double value) {
        return NumberFormat.getCurrencyInstance(Locale.US).format(value);
    }
}