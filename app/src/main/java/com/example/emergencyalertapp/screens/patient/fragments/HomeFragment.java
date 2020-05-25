package com.example.emergencyalertapp.screens.patient.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.emergencyalertapp.R;
import com.example.emergencyalertapp.screens.patient.PatientActivities;

public class HomeFragment extends Fragment {

    private LinearLayout sosBtn;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.home_fragment, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        sosBtn = view.findViewById(R.id.sosBtn);
        sosBtn.setOnClickListener(v -> {
            ((PatientActivities)getActivity()).sendAlertShowBottomSheet();
        });
    }

}
