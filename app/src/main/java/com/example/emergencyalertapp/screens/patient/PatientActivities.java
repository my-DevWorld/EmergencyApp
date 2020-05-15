package com.example.emergencyalertapp.screens.patient;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.emergencyalertapp.R;
import com.example.emergencyalertapp.adapters.ViewPagerAdapter;
import com.example.emergencyalertapp.screens.patient.fragments.AccountFragment;
import com.example.emergencyalertapp.screens.patient.fragments.DoctorsAndNurses;
import com.example.emergencyalertapp.screens.patient.fragments.HomeFragment;
import com.example.emergencyalertapp.screens.patient.fragments.HospitalFragment;
import com.google.android.material.tabs.TabLayout;

public class PatientActivities extends AppCompatActivity {

    private HomeFragment homeFragment;
    private HospitalFragment hospitalFragment;
    private AccountFragment accountFragment;
    private DoctorsAndNurses doctorsAndNurses;

    private Toolbar toolbar;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private ViewPagerAdapter viewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.patient_activities);

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Emergency Alert");
        setSupportActionBar(toolbar);
        toolbar.setBackgroundColor(ContextCompat.getColor(PatientActivities.this, R.color.whiteColor));
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        toolbar.setTitleTextColor(ContextCompat.getColor(PatientActivities.this, R.color.colorPrimary));


        tabLayout = findViewById(R.id.tab_layout);
        viewPager = findViewById(R.id.view_pager);

        homeFragment = new HomeFragment();
        doctorsAndNurses = new DoctorsAndNurses();
        hospitalFragment = new HospitalFragment();
        accountFragment = new AccountFragment();

        tabLayout.setupWithViewPager(viewPager);

        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), 0);
        viewPagerAdapter.addFragment(homeFragment);
        viewPagerAdapter.addFragment(doctorsAndNurses);
        viewPagerAdapter.addFragment(hospitalFragment);
        viewPagerAdapter.addFragment(accountFragment);
        viewPager.setAdapter(viewPagerAdapter);


        tabLayout.getTabAt(0).setIcon(R.drawable.ic_home);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_doc_nurs);
        tabLayout.getTabAt(2).setIcon(R.drawable.ic_hospital_building);
        tabLayout.getTabAt(3).setIcon(R.drawable.ic_account);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0) {
                    toolbar.setVisibility(View.VISIBLE);

                }
                else if (tab.getPosition() == 1) {
                    toolbar.setVisibility(View.GONE);
                }
                else if (tab.getPosition() == 2) {
                    toolbar.setVisibility(View.GONE);
                }
                else {
                    toolbar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

//        BadgeDrawable badgeDrawable = tabLayout.getTabAt(0).getOrCreateBadge();
//        badgeDrawable.setVisible(true);
//        badgeDrawable.setNumber(12);
    }

    @Override
    public void onBackPressed() {
        if(viewPager.getCurrentItem() == 0){
            super.onBackPressed();
        }
        else if(viewPager.getCurrentItem() == 1){
            viewPager.setCurrentItem(0);
        }
        else if(viewPager.getCurrentItem() == 2){
            viewPager.setCurrentItem(0);
        }
        else {
            viewPager.setCurrentItem(0);
        }
    }

    public void test(){

        Toast.makeText(this, "This is how it is done", Toast.LENGTH_SHORT).show();
    }

}






















