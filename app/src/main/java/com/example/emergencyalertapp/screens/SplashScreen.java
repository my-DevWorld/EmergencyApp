package com.example.emergencyalertapp.screens;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.emergencyalertapp.R;
import com.example.emergencyalertapp.models.User;
import com.example.emergencyalertapp.screens.patient.PatientActivities;
import com.example.emergencyalertapp.screens.patient.SendAlert;
import com.example.emergencyalertapp.screens.service_provider.SPHomeScreen;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import onboarding.screens.CreateProfile;
import onboarding.screens.Login;

public class SplashScreen extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore db;
    private CollectionReference usersCollection;
    private User user;
    private static final String CATEGORY = "Patient";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

        new Handler().postDelayed(this::startApp,1000);
    }

    private void startApp(){
        firebaseAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        usersCollection = db.collection("Users");

        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser == null){
            startActivity(new Intent(this, Login.class));
            finish();
        }
        else {
            usersCollection.whereEqualTo("userID", firebaseAuth.getUid())
                    .get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {
                        for(QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                            user = documentSnapshot.toObject(User.class);
                        }
                        if(user.getCategory().equals(CATEGORY)){
                            if(!user.isRecordsAvailable()) {
                                Intent intent = new Intent(SplashScreen.this, CreateProfile.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                finish();
                            }
                            else {
                                Intent intent = new Intent(SplashScreen.this, PatientActivities.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                finish();
                            }
                        }
                        else {
                            Intent intent = new Intent(SplashScreen.this, SPHomeScreen.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            finish();
                        }
                    });
        }
    }
}
