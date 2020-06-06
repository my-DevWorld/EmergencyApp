package com.example.emergencyalertapp.screens.service_provider.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.emergencyalertapp.R;
import com.example.emergencyalertapp.adapters.PatientDetailsAdapter;
import com.example.emergencyalertapp.models.service_providers.PatientDetails;
import com.example.emergencyalertapp.screens.service_provider.SPHomeScreen;


public class HomeFragment extends Fragment implements PatientDetailsAdapter.OnPatientDetailsListener {

    private RecyclerView recycler_view;
    private SwipeRefreshLayout swipeRefreshLayout;

    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance(){
        return new HomeFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.home_fragment_sp, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        setup(view);

    }

    private void setup(View view){
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setEnabled(true);
        recycler_view = view.findViewById(R.id.recycler_view);
        recycler_view.setLayoutManager(new LinearLayoutManager(getContext()));
        recycler_view.setHasFixedSize(true);
        populateData();

        swipeRefreshLayout.setOnRefreshListener(() -> {
            new Handler().postDelayed(() -> {
                populateData();
            },500);
        });
    }

    private void populateData(){
        ((SPHomeScreen)getActivity()).getPatients();
        swipeRefreshLayout.setRefreshing(true);
        new Handler().postDelayed(() -> {
            PatientDetailsAdapter patientDetailsAdapter = new PatientDetailsAdapter(getContext(),
                    ((SPHomeScreen)getActivity()).patientDetails, this);
            recycler_view.setAdapter(patientDetailsAdapter);
            patientDetailsAdapter.notifyDataSetChanged();
            swipeRefreshLayout.setRefreshing(false);
        },2000);
    }


    @Override
    public void getPatientDetailsClicked(PatientDetails patientDetails) {
        Toast.makeText(getContext(), "Display patient details", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResume() {
        super.onResume();
        populateData();
    }
}





















