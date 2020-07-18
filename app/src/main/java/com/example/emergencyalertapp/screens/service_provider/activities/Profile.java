package com.example.emergencyalertapp.screens.service_provider.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.emergencyalertapp.R;
import com.example.emergencyalertapp.models.service_providers.ServiceProvider;
import com.example.emergencyalertapp.utils.Essentials;
import com.example.emergencyalertapp.utils.UserClient;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class Profile extends AppCompatActivity {

    //widgets
    private ImageView back, ok;
    private LinearLayout rootLayout;
    private TextInputEditText userName, fullName, userEmail, placeOfWork, phoneNum, residentialAddress, doc_or_nurser, speciality;

    //fields
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private FirebaseFirestore db;
    private DocumentReference getServiceProvider;
    private DocumentReference usersDoc;
    private String usersDocumentPath;
    private String serviceProvider;
    private ServiceProvider provider;
    private Essentials essentials;
    private boolean txtchanged = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_sp);

        setup();
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    private void setup(){
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();
        getServiceProvider = db.collection("Service Providers")
                .document(((UserClient)this.getApplicationContext()).getUser().getUserID());
        usersDocumentPath = "Users/".concat(firebaseAuth.getUid());
        essentials = new Essentials();
        rootLayout = findViewById(R.id.rootLayout);
        ok = findViewById(R.id.ok);
        ok.setOnClickListener(v -> {
            updateUser();
        });
        back = findViewById(R.id.back);
        back.setOnClickListener(v -> {
            onBackPressed();
        });

        userName = findViewById(R.id.userName);
        fullName = findViewById(R.id.fullName);
        userEmail = findViewById(R.id.userEmail);
        userEmail.setEnabled(false);
        placeOfWork = findViewById(R.id.placeOfWork);
        phoneNum = findViewById(R.id.phoneNum);
        residentialAddress = findViewById(R.id.residentialAddress);
        doc_or_nurser = findViewById(R.id.doc_or_nurser);
        speciality = findViewById(R.id.speciality);

        userName.setOnFocusChangeListener((v, hasFocus) -> {
            if(hasFocus){
                if(ok.getVisibility() == View.GONE){
                    ok.setVisibility(View.VISIBLE);
                }
            }
        });
        fullName.setOnFocusChangeListener((v, hasFocus) -> {
            if(hasFocus){
                if(ok.getVisibility() == View.GONE){
                    ok.setVisibility(View.VISIBLE);
                }
            }
        });
        placeOfWork.setOnFocusChangeListener((v, hasFocus) -> {
            if(hasFocus){
                if(ok.getVisibility() == View.GONE){
                    ok.setVisibility(View.VISIBLE);
                }
            }
        });
        phoneNum.setOnFocusChangeListener((v, hasFocus) -> {
            if(hasFocus){
                if(ok.getVisibility() == View.GONE){
                    ok.setVisibility(View.VISIBLE);
                }
            }
        });
        residentialAddress.setOnFocusChangeListener((v, hasFocus) -> {
            if(hasFocus){
                if(ok.getVisibility() == View.GONE){
                    ok.setVisibility(View.VISIBLE);
                }
            }
        });
        doc_or_nurser.setOnFocusChangeListener((v, hasFocus) -> {
            if(hasFocus){
                if(ok.getVisibility() == View.GONE){
                    ok.setVisibility(View.VISIBLE);
                }
            }
        });
        speciality.setOnFocusChangeListener((v, hasFocus) -> {
            if(hasFocus){
                if(ok.getVisibility() == View.GONE){
                    ok.setVisibility(View.VISIBLE);
                }
            }
        });

        getServiceProviderDetails();
    }

    private void getServiceProviderDetails(){
        getServiceProvider.addSnapshotListener(this, (documentSnapshot, e) -> {
            if(e != null){
                return;
            }
            provider = documentSnapshot.toObject(ServiceProvider.class);
            new Handler().postDelayed(() -> {
                updateFields(provider);
                },100);
        });
    }

    private void updateFields(ServiceProvider serviceProvider){
        userName.setText(serviceProvider.getUsername());
        fullName.setText(serviceProvider.getFullName());
        userEmail.setText(serviceProvider.getEmail());
        placeOfWork.setText(serviceProvider.getHospital());
        phoneNum.setText(serviceProvider.getPhoneNum());
        residentialAddress.setText(serviceProvider.getResidentialAddress());
        doc_or_nurser.setText(serviceProvider.getServiceType());
        speciality.setText(serviceProvider.getSpeciality());
    }

    private void updateUser(){
        usersDoc = db.document(usersDocumentPath);
        usersDoc.update("username", userName.getText().toString().trim()).addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                ok.setVisibility(View.GONE);
                getServiceProvider.update("username", userName.getText().toString().trim());
                usersDoc.update("fullName", fullName.getText().toString().trim());
                getServiceProvider.update("fullName", fullName.getText().toString().trim());
                getServiceProvider.update("hospital", placeOfWork.getText().toString().trim());
                getServiceProvider.update("phoneNum", phoneNum.getText().toString().trim());
                getServiceProvider.update("residentialAddress", residentialAddress.getText().toString().trim());
                getServiceProvider.update("serviceType", doc_or_nurser.getText().toString().trim());
                getServiceProvider.update("speciality", speciality.getText().toString().trim());
                ((UserClient)this.getApplicationContext()).getUser().setUsername(userName.getText().toString().trim());
                rootLayout.requestFocus();
                essentials.hideSoftKeyboard(this, ok);
                Toast.makeText(this, "Update successful", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(this, "Profile updating failed", Toast.LENGTH_SHORT).show();
            }
        });
    }
}












