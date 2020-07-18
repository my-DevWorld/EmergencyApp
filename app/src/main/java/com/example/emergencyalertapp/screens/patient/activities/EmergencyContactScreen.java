package com.example.emergencyalertapp.screens.patient.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.emergencyalertapp.R;
import com.example.emergencyalertapp.models.patient.EmergencyContact;
import com.example.emergencyalertapp.models.service_providers.ServiceProvider;
import com.example.emergencyalertapp.utils.UserClient;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class EmergencyContactScreen extends AppCompatActivity {

    //widgets
    private TextView name, relation, phoneNum, resAddss;
    private CardView otherContacts;

    private FirebaseFirestore db;
    private DocumentReference primaryContact;
    private CollectionReference eContacts;
    private ArrayList<ServiceProvider> serviceProviders;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.emergency_contact);

        setup();
    }

    private void setup() {
        ImageView back = findViewById(R.id.back);
        back.setOnClickListener(v -> {
            onBackPressed();
        });

        name = findViewById(R.id.name);
        relation = findViewById(R.id.relation);
        phoneNum = findViewById(R.id.phoneNum);
        resAddss = findViewById(R.id.resAddress);
        otherContacts = findViewById(R.id.otherContacts);

        db = FirebaseFirestore.getInstance();
        primaryContact = db.document("Patients/".concat(((UserClient) this.getApplication()).getUser().getUserID())
                .concat("/EmergencyContact/Primary Contact"));
        eContacts = db.collection("Patients/".concat(((UserClient) this.getApplication()).getUser().getUserID())
                .concat("/EmergencyContact"));
        getPrimaryContact();
    }

    private void getPrimaryContact() {
        primaryContact.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                EmergencyContact emergencyContact = task.getResult().toObject(EmergencyContact.class);
                System.out.println(">>>>>>>>>>>>>>>>>>>>>>>: " + emergencyContact.toString());
                name.setText(emergencyContact.getContactName());
                relation.setText(emergencyContact.getContactRelationship());
                phoneNum.setText(emergencyContact.getContactPhoneNumb());
                resAddss.setText(emergencyContact.getContactResidentialAddress());
            }
        });

        serviceProviders = new ArrayList<>();
        eContacts.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                    serviceProviders.add(documentSnapshot.toObject(ServiceProvider.class));
                }
                for(int i = 1; i < serviceProviders.size(); i++){
                    System.out.println(">>>>>>>>>>>>>>>>>>>>>>>: " + serviceProviders.get(i).getFullName());
                    View view = getLayoutInflater().inflate(R.layout.contact_item, null);
                    TextView contact_item_name = view.findViewById(R.id.contact_item_name);
                    TextView contact_item_relation = view.findViewById(R.id.contact_item_relation);
                    TextView contact_item_phoneNum = view.findViewById(R.id.contact_item_phoneNum);
                    TextView contact_item_resAddss = view.findViewById(R.id.contact_item_res_address);
                    contact_item_name.setText(serviceProviders.get(i).getFullName());
                    contact_item_relation.setText(serviceProviders.get(i ).getServiceType());
                    contact_item_phoneNum.setText(serviceProviders.get(i).getPhoneNum());
                    contact_item_resAddss.setText(serviceProviders.get(i).getResidentialAddress());
                    otherContacts.addView(view);
                }
            }
        });
    }
}




















