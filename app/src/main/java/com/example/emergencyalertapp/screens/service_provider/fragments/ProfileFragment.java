package com.example.emergencyalertapp.screens.service_provider.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.emergencyalertapp.R;
import com.example.emergencyalertapp.models.User;
import com.example.emergencyalertapp.screens.SplashScreen;
import com.example.emergencyalertapp.screens.patient.PatientActivities;
import com.example.emergencyalertapp.screens.service_provider.SPHomeScreen;
import com.example.emergencyalertapp.screens.service_provider.activities.ManagePatient;
import com.example.emergencyalertapp.screens.service_provider.activities.Profile;
import com.example.emergencyalertapp.utils.CheckNetworkConnectivity;
import com.example.emergencyalertapp.utils.UserClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import onboarding.screens.Login;

public class ProfileFragment extends Fragment implements View.OnClickListener {

    //widget
    private RelativeLayout logout, userProfileLabel, managePatientsLabel;
    private TextView user_name, userEmailAddress;

    //fields
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore db;
    private CollectionReference usersCollection;
    private User user;

    public ProfileFragment() {
        // Required empty public constructor
    }

    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.profile_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        firebaseAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        user_name = view.findViewById(R.id.user_name);
        userEmailAddress = view.findViewById(R.id.userEmailAddress);
        userProfileLabel = view.findViewById(R.id.userProfileLabel);
        userProfileLabel.setOnClickListener(this);
        managePatientsLabel = view.findViewById(R.id.managePatientsLabel);
        managePatientsLabel.setOnClickListener(this);

//        getAuthenticatedUser();

        logout = view.findViewById(R.id.logout);
        logout.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(getContext(), Login.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            getActivity().overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        });
        setFields();
    }

//    private void getAuthenticatedUser(){
//        if (CheckNetworkConnectivity.getInstance(getActivity()).isOnline()) {
//            firebaseAuth = FirebaseAuth.getInstance();
//            usersCollection = db.collection("Users");
//            FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
//            if (firebaseUser == null) {
//                startActivity(new Intent(getContext(), Login.class));
//                getActivity().finish();
//            } else {
//                usersCollection.whereEqualTo("userID", firebaseAuth.getUid())
//                        .get()
//                        .addOnSuccessListener(queryDocumentSnapshots -> {
//                            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
//                                user = documentSnapshot.toObject(User.class);
//                            }
//                            ((UserClient)getActivity().getApplicationContext()).setUser(user);
//
//                        });
//            }
//
//        } else {
//            Toast.makeText(getActivity(), "No internet connection", Toast.LENGTH_SHORT).show();
//        }
//    }

    private void setFields(){
        new Handler().postDelayed(() -> {
            if(((UserClient)getActivity().getApplicationContext()).getUser() != null){
                user_name.setText(((UserClient)getActivity().getApplicationContext()).getUser().getUsername());
                userEmailAddress.setText(((UserClient)getActivity().getApplicationContext()).getUser().getEmail());
                System.out.println(">>>>>>>>>>>>> User fullname: " +
                        ((UserClient)getActivity().getApplicationContext()).getUser().getEmail());
            }
        }, 500);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.userProfileLabel:
                Intent intent = new Intent(getActivity(), Profile.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
            case R.id.managePatientsLabel:
                Intent intent1 = new Intent(getActivity(), ManagePatient.class);
                intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent1.putExtra("Patient Details", ((SPHomeScreen)getActivity()).patientDetails);
                startActivity(intent1);
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        setFields();
    }
}

















