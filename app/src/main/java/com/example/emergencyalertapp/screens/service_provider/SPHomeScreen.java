package com.example.emergencyalertapp.screens.service_provider;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.emergencyalertapp.R;
import com.example.emergencyalertapp.adapters.PatientDetailsAdapter;
import com.example.emergencyalertapp.adapters.ViewPagerAdapter;
import com.example.emergencyalertapp.models.User;
import com.example.emergencyalertapp.models.service_providers.PatientDetails;
import com.example.emergencyalertapp.screens.service_provider.fragments.FullScreenDia;
import com.example.emergencyalertapp.screens.service_provider.fragments.HomeFragment;
import com.example.emergencyalertapp.screens.service_provider.fragments.ProfileFragment;
import com.example.emergencyalertapp.utils.CheckNetworkConnectivity;
import com.example.emergencyalertapp.utils.UserClient;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

import onboarding.screens.Login;

public class SPHomeScreen extends AppCompatActivity {

    private HomeFragment homeFragment;
    private ProfileFragment profileFragment;

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore db;
    private CollectionReference getPatientsCollection;
    private CollectionReference usersCollection;
    private User user;
    public ArrayList<PatientDetails> patientDetails;
    private FullScreenDia fullScreenDia;

    //widgets
    private Toolbar toolbar;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private ViewPagerAdapter viewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.s_p_home_screen);

        setup();
        getAuthenticatedUser();
    }

    private void setup(){
        firebaseAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        String path = "Service Providers/" + firebaseAuth.getUid() + "/" + "patients";
        getPatientsCollection = db.collection(path);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Emergency Alert");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        toolbar.setBackgroundColor(ContextCompat.getColor(SPHomeScreen.this, R.color.whiteColor));
        toolbar.setTitleTextColor(ContextCompat.getColor(SPHomeScreen.this, R.color.forgotPassword));

        tabLayout = findViewById(R.id.tab_layout);
        viewPager = findViewById(R.id.view_pager);
        tabLayout.setupWithViewPager(viewPager);

        homeFragment = new HomeFragment();
        profileFragment = new ProfileFragment();

        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), 0);
        viewPagerAdapter.addFragment(homeFragment);
        viewPagerAdapter.addFragment(profileFragment);
        viewPager.setAdapter(viewPagerAdapter);

        tabLayout.getTabAt(0).setIcon(R.drawable.ic_home);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_account);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0) {
                    toolbar.setVisibility(View.VISIBLE);
                }
                else {
                    toolbar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });

        getPatients();
    }

    @Override
    public void onBackPressed() {
        if(viewPager.getCurrentItem() == 0){
            super.finish();
        }
        else {
            viewPager.setCurrentItem(0);
        }
    }

    private void getAuthenticatedUser(){
        if (CheckNetworkConnectivity.getInstance(this).isOnline()) {
            usersCollection = db.collection("Users");
            FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
            if (firebaseUser == null) {
                startActivity(new Intent(this, Login.class));
                finish();
            } else {
                usersCollection.whereEqualTo("userID", firebaseAuth.getUid())
                        .get()
                        .addOnSuccessListener(queryDocumentSnapshots -> {
                            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                user = documentSnapshot.toObject(User.class);
                            }
                            ((UserClient)this.getApplicationContext()).setUser(user);

                        });
            }

        } else {
            Toast.makeText(this, "No internet connection", Toast.LENGTH_SHORT).show();
        }
    }

    public void getPatients(){
        patientDetails = new ArrayList<>();
        getPatientsCollection.addSnapshotListener(this, (queryDocumentSnapshots, e) -> {
            if(e != null){
                return;
            }
            patientDetails.clear();
            for(QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                patientDetails.add(documentSnapshot.toObject(PatientDetails.class));
            }
            new Handler().postDelayed(() -> System.out.println(">>>>>>>>>>>>>>> Patients are ready... " + patientDetails.toString()),90);
        });
    }
}












