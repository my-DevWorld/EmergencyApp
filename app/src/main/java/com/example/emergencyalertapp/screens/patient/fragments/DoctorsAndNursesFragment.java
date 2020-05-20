package com.example.emergencyalertapp.screens.patient.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.emergencyalertapp.R;
import com.example.emergencyalertapp.adapters.ServiceProviderAdapter;
import com.example.emergencyalertapp.screens.patient.PatientActivities;


public class DoctorsAndNursesFragment extends Fragment {

    private RecyclerView recycler_view;

    public DoctorsAndNursesFragment() {}

    public static DoctorsAndNursesFragment newInstance() {
        return new DoctorsAndNursesFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.doctors_and_nurses_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recycler_view = view.findViewById(R.id.recycler_view);
        recycler_view.setLayoutManager(new LinearLayoutManager(getContext()));
        recycler_view.setHasFixedSize(true);

        new Handler().postDelayed(() -> {
            if(((PatientActivities)getActivity()).serviceProviders != null) {
                ServiceProviderAdapter serviceProviderAdapter = new ServiceProviderAdapter(getContext(),
                        ((PatientActivities) getActivity()).serviceProviders);
                recycler_view.setAdapter(serviceProviderAdapter);
            }
        }, 1000);
//        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>: " + ((PatientActivities)getActivity()).serviceProviders.toString())
    }
}
























