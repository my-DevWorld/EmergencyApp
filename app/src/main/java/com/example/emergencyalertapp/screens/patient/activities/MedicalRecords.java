package com.example.emergencyalertapp.screens.patient.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;

import com.example.emergencyalertapp.R;

public class MedicalRecords extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.medical_records);

        setup();
    }

    private void setup(){
        ImageView back = findViewById(R.id.back);
        back.setOnClickListener(v -> {
            onBackPressed();
        });
    }
}
