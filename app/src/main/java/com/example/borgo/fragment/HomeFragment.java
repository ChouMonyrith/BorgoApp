package com.example.borgo.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.borgo.R;
import com.example.borgo.activity.FoodDetailActivity;
import com.example.borgo.adapter.CategoryAdapter;
import com.example.borgo.adapter.FoodAdapter;
import com.example.borgo.data.model.Category;
import com.example.borgo.data.model.MenuItem;
import com.example.borgo.manager.TokenManager;
import com.example.borgo.ui.viewmodel.CategoryViewModel;
import com.example.borgo.ui.viewmodel.MenuItemViewModel;

import java.util.ArrayList;

public class HomeFragment extends Fragment {
    private static final String TAG = "HomeFragment";

    private RecyclerView categoryRecycler, menuRecycler;
    private EditText searchBar;
    private ProgressBar progressBar;
    private CategoryAdapter categoryAdapter;
    private FoodAdapter foodAdapter;
    private CategoryViewModel categoryVM;
    private MenuItemViewModel menuVM;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Initialize TokenManager
        TokenManager.init(requireContext());

        searchBar = view.findViewById(R.id.search_bar);
        categoryRecycler = view.findViewById(R.id.recycler_categories);
        menuRecycler = view.findViewById(R.id.recycler_menu_items);
        progressBar = view.findViewById(R.id.progress_bar);

        // Initialize ViewModels first
        categoryVM = new ViewModelProvider(this).get(CategoryViewModel.class);
        menuVM = new ViewModelProvider(this).get(MenuItemViewModel.class);

        // Category click -> filter menu
        categoryAdapter = new CategoryAdapter(category -> {
            if (category != null) {
                menuVM.filterByCategory(category.getCategoryId()); // Filter by category ID
            } else {
                menuVM.filterByCategory(null); // Load all items
            }
        });

        // Initialize foodAdapter with an empty list initially
        foodAdapter = new FoodAdapter(getContext(), new ArrayList<>(), position -> {
            // Handle item click here
            if (menuVM.getMenuItems().getValue() != null && position < menuVM.getMenuItems().getValue().size()) {
                MenuItem selectedItem = menuVM.getMenuItems().getValue().get(position);
                // Navigate to FoodDetailActivity with the selected item
                Intent intent = new Intent(getContext(), FoodDetailActivity.class);
                intent.putExtra("food", selectedItem);
                startActivity(intent);
            }
        });

        categoryRecycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        categoryRecycler.setAdapter(categoryAdapter);

        menuRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        menuRecycler.setAdapter(foodAdapter);

        // Set up search functionality
        setupSearch();

        observeViewModels();
        loadData();

        return view;
    }

    private void setupSearch() {
        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Not needed
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Not needed
            }

            @Override
            public void afterTextChanged(Editable s) {
                String query = s.toString().trim();
                menuVM.searchMenuItems(query);
            }
        });
    }

    private void observeViewModels() {
        categoryVM.getCategories().observe(getViewLifecycleOwner(), categories -> {
            categoryAdapter.submitList(categories);
        });

        menuVM.getMenuItems().observe(getViewLifecycleOwner(), menuItems -> {
            foodAdapter.updateList(menuItems);
        });

        categoryVM.getLoading().observe(getViewLifecycleOwner(), this::toggleLoading);
        menuVM.getLoading().observe(getViewLifecycleOwner(), this::toggleLoading);

        categoryVM.getError().observe(getViewLifecycleOwner(), this::showError);
        menuVM.getError().observe(getViewLifecycleOwner(), this::showError);
    }

    private void loadData() {
        categoryVM.fetchCategories();
        menuVM.fetchMenuItems();
    }

    private void toggleLoading(boolean isLoading) {
        progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
    }

    private void showError(String msg) {
        Log.e(TAG, "Error: " + msg);
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }
}