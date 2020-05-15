package com.example.emergencyalertapp.screens.patient.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.example.emergencyalertapp.R;
import com.example.emergencyalertapp.screens.patient.activities.EmergencyContact;
import com.example.emergencyalertapp.screens.patient.activities.MedicalRecords;
import com.example.emergencyalertapp.screens.patient.activities.Medication;
import com.example.emergencyalertapp.screens.patient.activities.UserProfile;
import com.google.firebase.auth.FirebaseAuth;

import onboarding.screens.Login;

public class AccountFragment extends Fragment {

    private RelativeLayout userProfileLabel, userMedicalRecordLabel, userEmergencyContactLabel, userMedicationLabel, logout;

    public static AccountFragment newInstance() {
        AccountFragment fragment = new AccountFragment();
        fragment.setRetainInstance(true);
        return fragment;
    }

    public AccountFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return  inflater.inflate(R.layout.account_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        userProfileLabel = view.findViewById(R.id.userProfileLabel);
        userMedicalRecordLabel = view.findViewById(R.id.userMedicalRecordLabel);
        userEmergencyContactLabel = view.findViewById(R.id.userEmergencyContactLabel);
        userMedicationLabel = view.findViewById(R.id.userMedicationLabel);
        logout = view.findViewById(R.id.logout);


        userProfileLabel.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), UserProfile.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });

        userMedicalRecordLabel.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), MedicalRecords.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });

        userEmergencyContactLabel.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), EmergencyContact.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });

        userMedicationLabel.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), Medication.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });

        logout.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(getActivity(), Login.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            getActivity().finish();
        });
    }

}
