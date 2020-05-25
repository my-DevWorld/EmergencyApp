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

import com.example.emergencyalertapp.R;
import com.example.emergencyalertapp.adapters.ViewPagerAdapter;
import com.example.emergencyalertapp.models.service_providers.PatientDetails;
import com.example.emergencyalertapp.screens.service_provider.fragments.HomeFragment;
import com.example.emergencyalertapp.screens.service_provider.fragments.ProfileFragment;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
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
    public ArrayList<PatientDetails> patientDetails;

    //widgets
    private Toolbar toolbar;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private ViewPagerAdapter viewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.s_p_home_screen);

//        TextView textView = findViewById(R.id.id);
//        textView.setOnClickListener(v -> {
//            FirebaseAuth.getInstance().signOut();
//            Intent intent = new Intent(SPHomeScreen.this, Login.class);
//            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            startActivity(intent);
//            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
//        });
        setup();
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
//        tabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.forgotPassword));
//        tabLayout.setBackgroundColor(getResources().getColor(R.color.whiteColor));
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

    private void getPatients(){
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












