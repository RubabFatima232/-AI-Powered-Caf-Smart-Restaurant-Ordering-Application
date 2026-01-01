package com.example.smartcafe;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.HashMap;

public class ProfileFragment extends Fragment {

    private SessionManager sessionManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        sessionManager = SessionManager.getInstance(requireContext());

        TextView profileName = view.findViewById(R.id.profile_name);
        TextView profileEmail = view.findViewById(R.id.profile_email);
        TextView profileRole = view.findViewById(R.id.profile_role);
        Button logoutButton = view.findViewById(R.id.logout_button);

        HashMap<String, String> userDetails = sessionManager.getUserDetails();
        profileName.setText(userDetails.get(SessionManager.KEY_NAME));
        profileEmail.setText(userDetails.get(SessionManager.KEY_EMAIL));
        profileRole.setText(userDetails.get(SessionManager.KEY_ROLE));

        logoutButton.setOnClickListener(v -> {
            sessionManager.logoutUser();
            requireActivity().finish();
        });

        return view;
    }
}
