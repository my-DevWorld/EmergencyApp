package com.example.emergencyalertapp.screens.patient.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.emergencyalertapp.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class BottomSheetDialog extends BottomSheetDialogFragment {
    private BottomSheetListener bottomSheetListener;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_sheet, container, false);
        LinearLayout getAmbulance = view.findViewById(R.id.getAmbulance);
        LinearLayout getDocNurse = view.findViewById(R.id.getDocNurse);
        LinearLayout cancel = view.findViewById(R.id.cancel);

        getAmbulance.setOnClickListener(v -> {
            bottomSheetListener.onButtonClicked("get an ambulance");
            dismiss();
        });

        getDocNurse.setOnClickListener(v -> {
            bottomSheetListener.onButtonClicked("get a doctor/nurse");
            dismiss();
        });

        cancel.setOnClickListener(v -> {
            bottomSheetListener.onButtonClicked("cancelled");
            dismiss();
        });

        return view;
    }

    public interface BottomSheetListener {
        void onButtonClicked(String text);
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            bottomSheetListener = (BottomSheetListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement BottomSheetListener");
        }
    }
}
