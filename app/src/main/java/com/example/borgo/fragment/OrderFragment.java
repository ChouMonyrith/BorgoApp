package com.example.borgo.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.borgo.R;
import com.example.borgo.adapter.OrderAdapter;
import com.example.borgo.ui.viewmodel.OrderViewModel;

public class OrderFragment extends Fragment {
    private static final String TAG = "OrderFragment";

    RecyclerView recyclerView;
    OrderAdapter orderAdapter;
    OrderViewModel orderViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order, container, false);

        recyclerView = view.findViewById(R.id.orders_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        orderViewModel = new ViewModelProvider(this).get(OrderViewModel.class);

        orderAdapter = new OrderAdapter(getContext());
        recyclerView.setAdapter(orderAdapter);

        observeViewModel();
        orderViewModel.fetchUserOrders();

        return view;
    }

    private void observeViewModel() {
        orderViewModel.getUserOrders().observe(getViewLifecycleOwner(), orders -> {
            if (orders != null) {
                Log.d(TAG, "Received " + orders.size() + " orders from ViewModel");
                orderAdapter.updateOrders(orders);
            } else {
                Log.d(TAG, "Received null orders from ViewModel");
            }
        });

        orderViewModel.getError().observe(getViewLifecycleOwner(), error -> {
            if (error != null) {
                Log.e(TAG, "Error: " + error);
                Toast.makeText(getContext(), "Error: " + error, Toast.LENGTH_SHORT).show();
            }
        });
    }
}