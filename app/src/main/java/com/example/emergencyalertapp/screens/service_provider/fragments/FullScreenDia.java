package com.example.emergencyalertapp.screens.service_provider.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.emergencyalertapp.R;
import com.example.emergencyalertapp.models.service_providers.PatientDetails;
import com.example.emergencyalertapp.screens.patient.fragments.SendAlertBottomSheetDialog;

public class FullScreenDia extends DialogFragment {

    private PatientDetails patientDetails;
    private Callback callback;

    static FullScreenDia newInstance() {
        return new FullScreenDia();
    }

    public FullScreenDia() {
    }

    public FullScreenDia(PatientDetails patientDetails, Callback callback) {
        this.patientDetails = patientDetails;
        this.callback = callback;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullscreenDialogTheme);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.patient_details_full_screen_dialog, container, false);
        ImageView back = view.findViewById(R.id.back);
        back.setOnClickListener(v -> {
            dismiss();
        });
        TextView username = view.findViewById(R.id.username);
        username.setText(patientDetails.getUserName());
        TextView fullName = view.findViewById(R.id.fullName);
        fullName.setText(patientDetails.getFullName());
        TextView phoneNum = view.findViewById(R.id.phoneNum);
        phoneNum.setText(patientDetails.getPatientPhoneNum());
        TextView residentialAddress = view.findViewById(R.id.residentialAddress);
        residentialAddress.setText(patientDetails.getPatientResAddress());
        TextView bodyWeight = view.findViewById(R.id.bodyWeight);
        bodyWeight.setText(patientDetails.getWeight());
        TextView bodyHeight = view.findViewById(R.id.bodyHeight);
        bodyHeight.setText(patientDetails.getHeight());
        TextView bloodGroup = view.findViewById(R.id.bloodGroup);
        bloodGroup.setText(patientDetails.getBloodType());
        TextView onMedication = view.findViewById(R.id.onMedication);
        TextView treatment = view.findViewById(R.id.treatment);
        TextView allergies = view.findViewById(R.id.allergies);
        TextView updateMedRec = view.findViewById(R.id.updateMedRec);

        if(patientDetails.isOnMedication()){
            onMedication.setText(R.string.yes);
        }
        else {
            onMedication.setText(R.string.no);
        }

        if(patientDetails.getTreatment() == null){
            treatment.setText(R.string.no_illness);
        }
        else {
            treatment.setText(patientDetails.getTreatment());
        }

        if(patientDetails.getAllergies() == null){
            allergies.setText(R.string.no_allergies);
        }
        else {
            allergies.setText(patientDetails.getAllergies());
        }

        updateMedRec.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Update the record", Toast.LENGTH_SHORT).show();
        });
        return view;
    }

    public interface Callback {
        void onActionClick(String name);
    }
}
