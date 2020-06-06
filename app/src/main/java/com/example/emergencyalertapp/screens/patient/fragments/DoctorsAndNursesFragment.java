package com.example.emergencyalertapp.screens.patient.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.emergencyalertapp.R;
import com.example.emergencyalertapp.adapters.ServiceProviderAdapter;
import com.example.emergencyalertapp.models.patient.MedicalRecord;
import com.example.emergencyalertapp.models.patient.PatientProfile;
import com.example.emergencyalertapp.models.service_providers.PatientDetails;
import com.example.emergencyalertapp.models.service_providers.ServiceProvider;
import com.example.emergencyalertapp.screens.patient.PatientActivities;
import com.example.emergencyalertapp.utils.Essentials;
import com.example.emergencyalertapp.utils.SharedPreference;
import com.example.emergencyalertapp.utils.UserClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;


public class DoctorsAndNursesFragment extends Fragment implements ServiceProviderAdapter.GetServiceProviderDetails, DocNurseDetailsBottomSheetDialog.BottomSheetListener {

    private RecyclerView recycler_view;
    private DocNurseDetailsBottomSheetDialog docNurseDetailsBottomSheetDialog;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore db;
    private DocumentReference medicalRecords;
    private DocumentReference profile;
    private DocumentReference addServiceProvider;
    private DocumentReference addEmergencyContact;
    private PatientProfile patientProfile;
    private MedicalRecord medicalRecord;
    private int numberOfContact;
    private Essentials essentials = new Essentials();

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

        firebaseAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        recycler_view = view.findViewById(R.id.recycler_view);
        recycler_view.setLayoutManager(new LinearLayoutManager(getContext()));
        recycler_view.setHasFixedSize(true);

        new Handler().postDelayed(() -> {
            if(((PatientActivities)getActivity()).serviceProviders != null) {
                ServiceProviderAdapter serviceProviderAdapter = new ServiceProviderAdapter(getContext(),
                        ((PatientActivities) getActivity()).serviceProviders, this);
                recycler_view.setAdapter(serviceProviderAdapter);
            }
        }, 900);
    }

    @Override
    public void onServiceProviderClicked(ServiceProvider serviceProvider) {
        docNurseDetailsBottomSheetDialog = new DocNurseDetailsBottomSheetDialog(serviceProvider, this);
        docNurseDetailsBottomSheetDialog.show(getFragmentManager(), "Doc_Nurse BottomSheetDialog");
    }

    @Override
    public void viewServiceProviderDetailsClicked(ServiceProvider serviceProvider) {
        numberOfContact = Integer.parseInt(SharedPreference.getInstance(getContext()).getNumberOnContact());
        if(numberOfContact <= 3){
            profile = db.collection("Patients").document(firebaseAuth.getUid()).collection("Profile").document("data");
            profile.get().addOnSuccessListener(documentSnapshot -> {
                patientProfile = documentSnapshot.toObject(PatientProfile.class);
                System.out.println(">>>>>>>>> Patient Profile >>>>>>>>>>>>>>>>>>>: " + patientProfile.toString());
            });
            medicalRecords = db.collection("Patients").document(firebaseAuth.getUid()).collection("MedicalRecord").document("data");
            medicalRecords.get().addOnSuccessListener(documentSnapshot -> {
                medicalRecord = documentSnapshot.toObject(MedicalRecord.class);
                System.out.println(">>>>>>>>> Patient MedRecord >>>>>>>>>>>>>>>>>>>: " + medicalRecord.toString());
            });
            essentials.startProgressLoader(getContext(), "Adding to contact...");

            new Handler().postDelayed(() -> {
                PatientDetails patientDetails = new PatientDetails();
                patientDetails.setPatientID(firebaseAuth.getUid());
                patientDetails.setUserName(((UserClient)getActivity().getApplicationContext()).getUser().getUsername());
                patientDetails.setFullName(((UserClient)getActivity().getApplicationContext()).getUser().getFullName());
                String medRecord = medicalRecord.getAllergies();
                if(medRecord == null){
                    patientDetails.setAllergies(null);
                }
                else {
                    patientDetails.setAllergies(medRecord);
                }
                patientDetails.setBloodType(medicalRecord.getBloodGroup());
                patientDetails.setHeight(medicalRecord.getHeight());
                patientDetails.setWeight(medicalRecord.getWeight());
                patientDetails.setOnMedication(false);
                patientDetails.setTreatment(null);
                patientDetails.setPatientPhoneNum(patientProfile.getPhoneNum());
                patientDetails.setPatientResAddress(patientProfile.getResidentialAddress());

                addServiceProvider(serviceProvider.getUserID(), serviceProvider.getFullName(),
                        ((UserClient)getActivity().getApplicationContext()).getUser().getFullName(), patientDetails, serviceProvider);

            }, 3500);
        }
        else {
            Toast.makeText(getContext(), "Emergency contact limit reached.", Toast.LENGTH_SHORT).show();

        }
    }

    private void addServiceProvider(String spID, String spName, String patientName, PatientDetails patientDetails, ServiceProvider serviceProvider){
        addServiceProvider = db.collection("Service Providers").document(spID)
                .collection("patients").document(patientName);
        addServiceProvider.set(patientDetails).addOnSuccessListener(aVoid -> {
            updateEmergencyContactList(serviceProvider, spName);
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                essentials.dismissProgressBar();
                Toast.makeText(getContext(), "Something went wrong, please try again later", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateEmergencyContactList(ServiceProvider serviceProvider, String spName){
        addEmergencyContact = db.collection("Patients").document(firebaseAuth.getUid())
                .collection("EmergencyContact").document(spName);
        addEmergencyContact.set(serviceProvider).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    SharedPreference.getInstance(getContext()).setNumberOnContact("2");
                    essentials.dismissProgressBar();
                    Toast.makeText(getContext(), "Added to contact list", Toast.LENGTH_SHORT).show();
                }
                else {
                    essentials.dismissProgressBar();
                    Toast.makeText(getContext(), "Could not add to contact list", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
























