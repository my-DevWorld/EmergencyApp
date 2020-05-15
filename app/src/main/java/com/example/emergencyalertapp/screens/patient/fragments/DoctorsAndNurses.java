package com.example.emergencyalertapp.screens.patient.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.emergencyalertapp.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class DoctorsAndNurses extends Fragment {

    public DoctorsAndNurses() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_doctors_and_nurses, container, false);
    }
}
