package com.example.emergencyalertapp.screens.service_provider.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.emergencyalertapp.R;
import com.example.emergencyalertapp.adapters.ManagePatientAdapter;
import com.example.emergencyalertapp.models.service_providers.PatientDetails;
import com.example.emergencyalertapp.screens.patient.PatientActivities;
import com.example.emergencyalertapp.utils.UserClient;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

import static com.example.emergencyalertapp.Constants.PERMISSIONS_REQUEST_ENABLE_GPS;

public class ManagePatient extends AppCompatActivity implements ManagePatientAdapter.OnPatientDetailsListener {

    //widgets
    private ImageView back;
    private RecyclerView recycler_view;

    //fields
    private FirebaseFirestore db;
    private DocumentReference removePatient;
    private DocumentReference removeEmergencyContact;
    public ArrayList<PatientDetails> patientDetails;
    private ManagePatientAdapter managePatientAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manage_patient);

        if(getIntent().getParcelableArrayListExtra("Patient Details") != null){
            patientDetails = getIntent().getParcelableArrayListExtra("Patient Details");
            managePatientAdapter = new ManagePatientAdapter(this, patientDetails, this);
        }
        setup();
    }

    private void setup(){
        db = FirebaseFirestore.getInstance();
        back = findViewById(R.id.back);
        back.setOnClickListener(v -> {
            onBackPressed();
        });
        recycler_view = findViewById(R.id.recycler_view);
        recycler_view.setLayoutManager(new LinearLayoutManager(this));
        recycler_view.setHasFixedSize(true);

        populateData();
    }

    private void populateData(){
        recycler_view.setAdapter(managePatientAdapter);
    }

    @Override
    public void getPatientDetailsClicked(PatientDetails patientDetails, int index) {
        buildAlertMessageNoGps(patientDetails.getFullName(), patientDetails.getPatientID(), index);
    }

    private void deletePatient(String spID, String spName, String patientName, String patientID, int position){
        removePatient = db.collection("Service Providers").document(spID)
                .collection("patients").document(patientName);
        removeEmergencyContact = db.collection("Patients").document(patientID)
                .collection("EmergencyContact").document(spName);
        removePatient.delete().addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                removeEmergencyContact.delete().addOnSuccessListener(aVoid -> {
                    patientDetails.remove(position);
                    populateData();
                    managePatientAdapter.notifyItemRemoved(position);
                    Toast.makeText(this, "Patient deleted.", Toast.LENGTH_SHORT).show();
                });
            }
            else {
                Toast.makeText(this, "Something went wrong, please try again.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void buildAlertMessageNoGps(String patientName, String patientID, int position) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to delete this patient?")
                .setCancelable(false)
                .setNegativeButton("Cancel", (dialog, which) -> dialog.cancel())
                .setPositiveButton("Yes", (dialog, id) -> {
                    deletePatient(((UserClient)this.getApplicationContext()).getUser().getUserID(),
                            ((UserClient)this.getApplicationContext()).getUser().getFullName(),
                            patientName, patientID, position);
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }
}



















