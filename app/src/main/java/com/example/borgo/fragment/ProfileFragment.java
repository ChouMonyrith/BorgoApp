package com.example.borgo.fragment;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.borgo.R;
import com.example.borgo.data.model.SignUpResponse;
import com.example.borgo.data.model.UpdateProfileRequest;
import com.example.borgo.manager.TokenManager;
import com.example.borgo.ui.viewmodel.AuthViewModel;

import java.util.Calendar;

public class ProfileFragment extends Fragment {

    private TextView userName, phoneNumber, location, dateOfBirth, gender, email;
    private ImageView profilePicture;
    private Button editProfileButton;
    private AuthViewModel authViewModel;
    private int userId;
    
    // Store the current user data to preserve all fields during updates
    private SignUpResponse currentUserData;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);

        email = view.findViewById(R.id.profile_email);
        userName = view.findViewById(R.id.profile_username);
        phoneNumber = view.findViewById(R.id.profile_phone_number);
        location = view.findViewById(R.id.profile_location);
        dateOfBirth = view.findViewById(R.id.profile_birth_date);
        gender = view.findViewById(R.id.profile_gender);
        profilePicture = view.findViewById(R.id.profile_image);
        editProfileButton = view.findViewById(R.id.edit_profile_button);

        // Initialize TokenManager
        TokenManager.init(requireActivity().getApplicationContext());
        userId = TokenManager.getUserId();

        authViewModel.getProfileLiveData().observe(getViewLifecycleOwner(), user -> {
            if (user != null) {
                // Store the current user data to preserve all fields during updates
                currentUserData = user;
                
                userName.setText(user.getName() != null ? user.getName() : "");
                phoneNumber.setText(user.getPhone() != null ? user.getPhone() : "");
                email.setText(user.getEmail() != null ? user.getEmail() : "");
                location.setText(user.getAddress() != null ? user.getAddress() : "");
                dateOfBirth.setText(user.getDob() != null ? user.getDob() : "");
                gender.setText(user.getGender() != null ? user.getGender() : "");
            }
        });

        authViewModel.getErrorLiveData().observe(getViewLifecycleOwner(), error -> {
            if (error != null) {
                Toast.makeText(getContext(), "Error: " + error, Toast.LENGTH_SHORT).show();
            }
        });

        if (userId != -1) {
            authViewModel.getProfile(userId);
        }

        editProfileButton.setOnClickListener(v -> showEditDialog());

        return view;
    }

    private void showEditDialog() {
        if (currentUserData == null) {
            Toast.makeText(getContext(), "Profile data not loaded yet. Please try again.", Toast.LENGTH_SHORT).show();
            return;
        }
        
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_edit_profile, null);

        EditText nameInput = dialogView.findViewById(R.id.edit_name);
        EditText phoneInput = dialogView.findViewById(R.id.edit_phone);
        EditText locationInput = dialogView.findViewById(R.id.edit_location);
        EditText dobInput = dialogView.findViewById(R.id.edit_dob);
        Spinner genderSpinner = dialogView.findViewById(R.id.edit_gender_spinner);

        // Pre-fill with current data, handling null values
        nameInput.setText(currentUserData.getName() != null ? currentUserData.getName() : "");
        phoneInput.setText(currentUserData.getPhone() != null ? currentUserData.getPhone() : "");
        locationInput.setText(currentUserData.getAddress() != null ? currentUserData.getAddress() : "");
        dobInput.setText(currentUserData.getDob() != null ? currentUserData.getDob() : "");

        ArrayAdapter<String> genderAdapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_spinner_dropdown_item,
                new String[]{"Male", "Female", "Other"}
        );
        genderSpinner.setAdapter(genderAdapter);

        String currentGender = currentUserData.getGender();
        if (currentGender != null && !currentGender.isEmpty()) {
            int spinnerPosition = genderAdapter.getPosition(currentGender);
            if (spinnerPosition >= 0) {
                genderSpinner.setSelection(spinnerPosition);
            }
        }

        dobInput.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            DatePickerDialog datePicker = new DatePickerDialog(
                    requireContext(),
                    (view, year, month, dayOfMonth) -> {
                        String dob = dayOfMonth + "/" + (month + 1) + "/" + year;
                        dobInput.setText(dob);
                    },
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
            );
            datePicker.show();
        });

        AlertDialog dialog = new AlertDialog.Builder(getContext())
                .setTitle("Edit Profile")
                .setView(dialogView)
                .setPositiveButton("Save", null) // Set to null to handle validation manually
                .setNegativeButton("Cancel", null)
                .show();

        // Override the positive button to handle the save action
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
            String name = nameInput.getText().toString().trim();
            String phone = phoneInput.getText().toString().trim();
            String address = locationInput.getText().toString().trim();
            String dob = dobInput.getText().toString().trim();
            String genderStr = genderSpinner.getSelectedItem().toString();

            // Validate required fields
            if (name.isEmpty()) {
                nameInput.setError("Name is required");
                return;
            }

            // Create update request with ALL current user data AND the updated values
            // Use updated values where provided, otherwise preserve existing values
            UpdateProfileRequest updateProfileRequest = new UpdateProfileRequest(
                    currentUserData.getFirstName() != null ? currentUserData.getFirstName() : "",  // Preserve first name or use empty string
                    currentUserData.getLastName() != null ? currentUserData.getLastName() : "",   // Preserve last name or use empty string
                    name,  // Use the updated name from the form
                    currentUserData.getEmail(),  // Preserve email
                    phone,  // Use the updated phone from the form
                    address,  // Use the updated address from the form
                    dob,  // Use the updated dob from the form
                    genderStr  // Use the updated gender from the form
            );

            // Update profile
            authViewModel.updateProfile(userId, updateProfileRequest);
            
            // Close dialog after saving
            dialog.dismiss();
            
            // Show a toast to indicate the update process has started
            Toast.makeText(getContext(), "Updating profile...", Toast.LENGTH_SHORT).show();
        });
    }
}