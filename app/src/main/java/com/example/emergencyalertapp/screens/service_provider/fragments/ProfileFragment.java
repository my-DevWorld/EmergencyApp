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
import com.example.emergencyalertapp.utils.CheckNetworkConnectivity;
import com.example.emergencyalertapp.utils.UserClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import onboarding.screens.Login;

public class ProfileFragment extends Fragment {

    //widget
    private RelativeLayout logout;
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

        getAuthenticatedUser();

        logout = view.findViewById(R.id.logout);
        logout.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(getContext(), Login.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            getActivity().overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        });

        new Handler().postDelayed(() -> {
            user_name.setText(((UserClient)getActivity().getApplicationContext()).getUser().getUsername());
            userEmailAddress.setText(((UserClient)getActivity().getApplicationContext()).getUser().getEmail());
            System.out.println(">>>>>>>>>>>>> User fullname: " +
                    ((UserClient)getActivity().getApplicationContext()).getUser().getEmail());
        }, 700);

    }

    private void getAuthenticatedUser(){
        if (CheckNetworkConnectivity.getInstance(getActivity()).isOnline()) {
            firebaseAuth = FirebaseAuth.getInstance();
            usersCollection = db.collection("Users");
            FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
            if (firebaseUser == null) {
                startActivity(new Intent(getContext(), Login.class));
                getActivity().finish();
            } else {
                usersCollection.whereEqualTo("userID", firebaseAuth.getUid())
                        .get()
                        .addOnSuccessListener(queryDocumentSnapshots -> {
                            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                user = documentSnapshot.toObject(User.class);
                            }
                            ((UserClient)getActivity().getApplicationContext()).setUser(user);

                        });
            }

        } else {
            Toast.makeText(getActivity(), "No internet connection", Toast.LENGTH_SHORT).show();
        }
    }
}

















