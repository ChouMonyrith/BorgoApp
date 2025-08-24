package com.example.borgo.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.borgo.R;
import com.example.borgo.adapter.CartAdapter;
import com.example.borgo.data.model.CartItem;
import com.example.borgo.data.model.Order;
import com.example.borgo.data.model.OrderItem;
import com.example.borgo.data.model.OrderRequest;
import com.example.borgo.data.repository.OrderRepository;
import com.example.borgo.manager.CartManager;
import com.example.borgo.manager.OrderManager;
import com.google.android.material.button.MaterialButton;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CartFragment extends Fragment implements CartAdapter.OnCartChangeListener {
    private static final String TAG = "CartFragment";

    private RecyclerView recyclerView;
    private CartAdapter cartAdapter;

    // UI Elements
    private TextView emptyText;
    private TextView subtotalText, taxesText, deliveryFeeText, totalText;
    private MaterialButton checkoutButton;
    private RadioGroup paymentOptions;
    private NumberFormat currencyFormat;

    // Views to hide when cart is empty
    private View yourCartText, orderSubtotalLabel, taxesLabel, deliveryFeeLabel, totalLabel, paymentMethodLabel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart, container, false);

        // Initialize UI components
        initializeViews(view);

        // Setup RecyclerView
        setupRecyclerView();

        // Set listeners
        setupListeners();

        // Initial UI update
        updateCartUI();

        return view;
    }

    private void initializeViews(View view) {
        currencyFormat = NumberFormat.getCurrencyInstance(Locale.US);

        recyclerView = view.findViewById(R.id.recycle_view_cart);
        emptyText = view.findViewById(R.id.empty_cart_text_view);
        subtotalText = view.findViewById(R.id.order_total);
        taxesText = view.findViewById(R.id.taxes);
        deliveryFeeText = view.findViewById(R.id.delivery_fee);
        totalText = view.findViewById(R.id.subtotal);
        checkoutButton = view.findViewById(R.id.check_out_button);
        paymentOptions = view.findViewById(R.id.payment_options);

        // Group views that will be hidden/shown together
        yourCartText = view.findViewById(R.id.textView9);
        orderSubtotalLabel = view.findViewById(R.id.textView10);
        taxesLabel = view.findViewById(R.id.textView11);
        deliveryFeeLabel = view.findViewById(R.id.textView12);
        totalLabel = view.findViewById(R.id.textView16);
        paymentMethodLabel = view.findViewById(R.id.textView18);
    }

    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        cartAdapter = new CartAdapter(CartManager.getInstance().getCartItems(), requireContext(), this);
        recyclerView.setAdapter(cartAdapter);
    }

    private void setupListeners() {
        CartManager.getInstance().setCartChangeListener(this);
        checkoutButton.setOnClickListener(v -> handleCheckout());
    }

    private void handleCheckout() {
        // 1. Check if cart is empty
        if (CartManager.getInstance().getCartItems().isEmpty()) {
            Toast.makeText(getContext(), "Your cart is empty!", Toast.LENGTH_SHORT).show();
            return;
        }

        // 2. Check if a payment option is selected
        int selectedId = paymentOptions.getCheckedRadioButtonId();
        if (selectedId == -1) {
            Toast.makeText(getContext(), "Please select a payment method!", Toast.LENGTH_SHORT).show();
            return;
        }

        RadioButton selectedOption = paymentOptions.findViewById(selectedId);
        String paymentMethod = selectedOption.getText().toString();
        Log.d(TAG, "Selected payment method: " + paymentMethod);

        // 3. Proceed with placing the order
        placeOrder(paymentMethod);
    }

    private void placeOrder(String paymentMethod) {
        // Disable button to prevent multiple clicks
        checkoutButton.setEnabled(false);

        List<CartItem> cartItems = CartManager.getInstance().getCartItems();
        
        // Add validation to check if cart is actually empty
        if (cartItems == null || cartItems.isEmpty()) {
            Log.e(TAG, "Cart is empty, cannot place order");
            Toast.makeText(getContext(), "Your cart is empty!", Toast.LENGTH_SHORT).show();
            checkoutButton.setEnabled(true);
            return;
        }

        List<OrderItem> orderItems = new ArrayList<>();
        for (CartItem cartItem : cartItems) {
            // Add null check for cartItem and food
            if (cartItem != null && cartItem.getFood() != null) {
                orderItems.add(new OrderItem(cartItem.getFood().getId(), cartItem.getQuantity()));
            } else {
                Log.w(TAG, "Found null cart item or food item, skipping");
            }
        }

        // Check if we have any valid items to order
        if (orderItems.isEmpty()) {
            Log.e(TAG, "No valid items to order");
            Toast.makeText(getContext(), "No valid items in cart!", Toast.LENGTH_SHORT).show();
            checkoutButton.setEnabled(true);
            return;
        }

        double subtotal = CartManager.getInstance().getSubtotal();
        double tax = CartManager.getInstance().getTax(0.1);
        double total = CartManager.getInstance().getTotal(0.1);

        Log.d(TAG, "Placing order with " + orderItems.size() + " items, total: " + total);

        OrderRequest orderRequest = new OrderRequest(orderItems, total);
        OrderRepository repo = new OrderRepository();

        repo.placeOrder(orderRequest, new OrderRepository.OrderCallback() {
            @Override
            public void onSuccess(Order order) {
                Log.d(TAG, "Order placed successfully");
                Toast.makeText(getContext(), "Order placed successfully with " + paymentMethod, Toast.LENGTH_LONG).show();

                Order localOrder = new Order(new ArrayList<>(cartItems), subtotal, tax, total);
                OrderManager.getInstance().addOrder(localOrder);

                CartManager.getInstance().clearCart();
                // onCartChanged will be triggered, which calls updateCartUI()
                checkoutButton.setEnabled(true);
            }

            @Override
            public void onError(String error) {
                Log.e(TAG, "Error placing order: " + error);
                Toast.makeText(getContext(), "Error: " + error, Toast.LENGTH_SHORT).show();
                checkoutButton.setEnabled(true);
            }
        });
    }

    @Override
    public void onCartChanged(double subtotal, double tax, double total) {
        updateCartUI();
    }

    private void updateCartUI() {
        List<CartItem> items = CartManager.getInstance().getCartItems();
        boolean isCartEmpty = items.isEmpty();

        // Toggle visibility based on whether the cart is empty
        int cartViewsVisibility = isCartEmpty ? View.GONE : View.VISIBLE;
        int emptyViewVisibility = isCartEmpty ? View.VISIBLE : View.GONE;

        recyclerView.setVisibility(cartViewsVisibility);
        yourCartText.setVisibility(cartViewsVisibility);
        orderSubtotalLabel.setVisibility(cartViewsVisibility);
        taxesLabel.setVisibility(cartViewsVisibility);
        deliveryFeeLabel.setVisibility(cartViewsVisibility);
        totalLabel.setVisibility(cartViewsVisibility);
        paymentOptions.setVisibility(cartViewsVisibility);
        paymentMethodLabel.setVisibility(cartViewsVisibility);
        checkoutButton.setVisibility(cartViewsVisibility);
        subtotalText.setVisibility(cartViewsVisibility);
        taxesText.setVisibility(cartViewsVisibility);
        deliveryFeeText.setVisibility(cartViewsVisibility);
        totalText.setVisibility(cartViewsVisibility);

        emptyText.setVisibility(emptyViewVisibility);

        // Update data and values
        cartAdapter.updateData(items);

        double subtotal = CartManager.getInstance().getSubtotal();
        double tax = CartManager.getInstance().getTax(0.1);
        double deliveryFee = CartManager.getInstance().getDeliveryFee();
        double total = CartManager.getInstance().getTotal(0.1);

        subtotalText.setText(currencyFormat.format(subtotal));
        taxesText.setText(currencyFormat.format(tax));
        deliveryFeeText.setText(currencyFormat.format(isCartEmpty ? 0 : deliveryFee));
        totalText.setText(currencyFormat.format(total));
    }
}