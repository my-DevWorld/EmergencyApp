package com.example.emergencyalertapp.screens.patient.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.emergencyalertapp.R;
import com.example.emergencyalertapp.models.patient.PatientProfile;
import com.example.emergencyalertapp.utils.Essentials;
import com.example.emergencyalertapp.utils.UserClient;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class UserProfile extends AppCompatActivity {

    //widget
    private TextView editLabel;
    private ImageView back, cancel, ok;
    private TextInputEditText userName, fullName, userEmail, dob, phoneNum, residentialAddress;
    private LinearLayout rootLayout;

    //fields
    private FirebaseFirestore db;
    private DocumentReference usersDoc;
    private DocumentReference profile;
    private String usersDocumentPath;
    private Essentials essentials;
    private PatientProfile patientProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_profile);

        setup();
    }

    private void setup(){
        db = FirebaseFirestore.getInstance();
        usersDocumentPath = "Users/".concat(((UserClient)this.getApplicationContext()).getUser().getUserID());
        profile = db.document("Patients/"
                .concat(((UserClient)this.getApplicationContext()).getUser().getUserID())
                + "/Profile/data");
        essentials = new Essentials();
        essentials.startProgressLoader(this, "Loading...");
        rootLayout = findViewById(R.id.rootLayout);
        back = findViewById(R.id.back);
        back.setOnClickListener(v -> {
            onBackPressed();
        });

        cancel = findViewById(R.id.cancel);
        cancel.setOnClickListener(v -> {
            hideAndShowViews(cancel);
            populateFields();
        });

        ok = findViewById(R.id.ok);
        ok.setOnClickListener(v -> {
            essentials.startProgressLoader(this, "Updating profile...");
            updateProfile();
        });

        editLabel = findViewById(R.id.editLabel);
        editLabel.setOnClickListener(v -> {
            hideAndShowViews(editLabel);
        });

        userName = findViewById(R.id.userName);
        fullName = findViewById(R.id.fullName);
        userEmail = findViewById(R.id.userEmail);
        dob = findViewById(R.id.dob);
        phoneNum = findViewById(R.id.phoneNum);
        residentialAddress = findViewById(R.id.residentialAddress);

        populateFields();
    }

    private void hideAndShowViews(View view){
        if(view.getId() == R.id.editLabel){
            editLabel.setVisibility(View.GONE);
            ok.setVisibility(View.VISIBLE);
            back.setVisibility(View.INVISIBLE);
            cancel.setVisibility(View.VISIBLE);

            userName.setEnabled(true);
            fullName.setEnabled(true);
            phoneNum.setEnabled(true);
            residentialAddress.setEnabled(true);
            return;
        }
        if(view.getId() == R.id.cancel){
            back.setVisibility(View.VISIBLE);
            cancel.setVisibility(View.GONE);
            editLabel.setVisibility(View.VISIBLE);
            ok.setVisibility(View.GONE);

            essentials.hideSoftKeyboard(this, cancel);
            rootLayout.requestFocus();
            userName.setEnabled(false);
            fullName.setEnabled(false);
            phoneNum.setEnabled(false);
            residentialAddress.setEnabled(false);
        }
    }

    private void populateFields(){
        profile.addSnapshotListener(this, (documentSnapshot, e) -> {
            if(e != null){
                return;
            }
            essentials.dismissProgressBar();
            patientProfile = documentSnapshot.toObject(PatientProfile.class);
            userName.setText(((UserClient)this.getApplicationContext()).getUser().getUsername());
            fullName.setText(patientProfile.getFullName());
            userEmail.setText(((UserClient)this.getApplicationContext()).getUser().getEmail());
            dob.setText(patientProfile.getDateOfBirth());
            phoneNum.setText(patientProfile.getPhoneNum());
            residentialAddress.setText(patientProfile.getResidentialAddress());
        });
    }

    private void updateProfile(){
        usersDoc = db.document(usersDocumentPath);
        usersDoc.update("fullName", fullName.getText().toString().trim()).addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                usersDoc.update("username", userName.getText().toString().trim());
                profile.update("fullName", fullName.getText().toString().trim());
                profile.update("phoneNum", phoneNum.getText().toString().trim());
                profile.update("residentialAddress", residentialAddress.getText().toString().trim());
                ((UserClient)this.getApplicationContext()).getUser().setFullName(fullName.getText().toString().trim());
                ((UserClient)this.getApplicationContext()).getUser().setUsername(userName.getText().toString().trim());

                new Handler().postDelayed(() -> {
                    populateFields();
                    Toast.makeText(this, "Profile updated", Toast.LENGTH_SHORT).show();
                }, 500);
            }
            else {
                essentials.dismissProgressBar();
                Toast.makeText(this, "Profile updating failed", Toast.LENGTH_SHORT).show();
                //TODO: something went wrong
            }
        });
        hideAndShowViews(cancel);
    }
}















