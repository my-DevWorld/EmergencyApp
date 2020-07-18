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
import android.widget.TextView;

import com.example.emergencyalertapp.R;
import com.example.emergencyalertapp.screens.patient.PatientActivities;
import com.example.emergencyalertapp.screens.patient.activities.EmergencyContactScreen;
import com.example.emergencyalertapp.screens.patient.activities.MedicalRecords;
import com.example.emergencyalertapp.screens.patient.activities.Medication;
import com.example.emergencyalertapp.screens.patient.activities.UserAccountSetting;
import com.example.emergencyalertapp.screens.patient.activities.UserProfile;
import com.example.emergencyalertapp.utils.UserClient;

public class AccountFragment extends Fragment {

    private RelativeLayout userProfileLabel, userMedicalRecordLabel, userEmergencyContactLabel, userMedicationLabel, settings, logout;
    private TextView userEmailAddress, user_name;

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

        userEmailAddress = view.findViewById(R.id.userEmailAddress);
        user_name = view.findViewById(R.id.user_name);
        userProfileLabel = view.findViewById(R.id.userProfileLabel);
        userMedicalRecordLabel = view.findViewById(R.id.userMedicalRecordLabel);
        userEmergencyContactLabel = view.findViewById(R.id.userEmergencyContactLabel);
        userMedicationLabel = view.findViewById(R.id.userMedicationLabel);
        settings = view.findViewById(R.id.settings);
        logout = view.findViewById(R.id.logout);

        userEmailAddress.setText(((PatientActivities)getActivity()).userEmail);
        if(((UserClient)getActivity().getApplicationContext()).getUser() != null){
            user_name.setText(((UserClient)getActivity().getApplicationContext()).getUser().getUsername());
        }


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
            Intent intent = new Intent(getActivity(), EmergencyContactScreen.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });

        userMedicationLabel.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), Medication.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });

        settings.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), UserAccountSetting.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });

        logout.setOnClickListener(v -> {
            ((PatientActivities)getActivity()).logoutUser();
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if(((UserClient)getActivity().getApplicationContext()).getUser() != null){
            user_name.setText(((UserClient)getActivity().getApplicationContext()).getUser().getUsername());
        }
    }
}










