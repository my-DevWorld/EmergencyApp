package com.example.emergencyalertapp.screens.patient.fragments;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.emergencyalertapp.R;
import com.example.emergencyalertapp.models.service_providers.ServiceProvider;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class DocNurseDetailsBottomSheetDialog extends BottomSheetDialogFragment {

    private ServiceProvider serviceProvider;
    private BottomSheetListener serviceProviderBottomSheetListener;

    public DocNurseDetailsBottomSheetDialog(ServiceProvider serviceProvider, BottomSheetListener serviceProviderBottomSheetListener) {
        this.serviceProvider = serviceProvider;
        this.serviceProviderBottomSheetListener = serviceProviderBottomSheetListener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.doc_and_nurse_details_bottom_sheet_dialog, container, false);
        ServiceProvider sProvider = serviceProvider;
        ImageView cancel = view.findViewById(R.id.cancel);
        TextView doc_nurse_serviceType = view.findViewById(R.id.doc_nurse_serviceType);
        TextView doc_nurse_name = view.findViewById(R.id.doc_nurse_name);
        TextView doc_nurse_email = view.findViewById(R.id.doc_nurse_email);
        TextView doc_nurse_phoneNum = view.findViewById(R.id.doc_nurse_phoneNum);
        TextView doc_nurse_resAddress = view.findViewById(R.id.doc_nurse_resAddress);
        TextView doc_nurse_serviceAt = view.findViewById(R.id.doc_nurse_serviceAt);
        TextView doc_nurse_speciality = view.findViewById(R.id.doc_nurse_speciality);
        TextView add_service_provider = view.findViewById(R.id.add_service_provider);

        doc_nurse_serviceType.setText(sProvider.getServiceType());
        doc_nurse_name.setText(sProvider.getFullName());
        doc_nurse_email.setText(sProvider.getEmail());
        doc_nurse_phoneNum.setText(sProvider.getPhoneNum());
        doc_nurse_resAddress.setText("Res. Address: " + sProvider.getResidentialAddress());
        doc_nurse_serviceAt.setText(String.format("%s At: %s", sProvider.getServiceType(), sProvider.getHospital()));
        doc_nurse_speciality.setText(sProvider.getSpeciality());

        add_service_provider.setOnClickListener(v -> {
            serviceProviderBottomSheetListener.viewServiceProviderDetailsClicked(sProvider);
            dismiss();
        });

        cancel.setOnClickListener(v -> {
            dismiss();
        });

        return view;
    }

    public interface BottomSheetListener {
        void viewServiceProviderDetailsClicked(ServiceProvider serviceProvider);
    }
}






