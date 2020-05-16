package com.example.emergencyalertapp.screens.patient;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.emergencyalertapp.R;
import com.example.emergencyalertapp.adapters.ViewPagerAdapter;
import com.example.emergencyalertapp.screens.patient.fragments.AccountFragment;
import com.example.emergencyalertapp.screens.patient.fragments.BottomSheetDialog;
import com.example.emergencyalertapp.screens.patient.fragments.DoctorsAndNurses;
import com.example.emergencyalertapp.screens.patient.fragments.HomeFragment;
import com.example.emergencyalertapp.screens.patient.fragments.HospitalFragment;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import onboarding.screens.Login;

import static com.example.emergencyalertapp.screens.patient.Constants.ERROR_DIALOG_REQUEST;
import static com.example.emergencyalertapp.screens.patient.Constants.PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION;
import static com.example.emergencyalertapp.screens.patient.Constants.PERMISSIONS_REQUEST_ENABLE_GPS;

public class PatientActivities extends AppCompatActivity implements BottomSheetDialog.BottomSheetListener{

    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private HomeFragment homeFragment;
    private HospitalFragment hospitalFragment;
    private AccountFragment accountFragment;
    private DoctorsAndNurses doctorsAndNurses;
    public String userEmail;
    private BottomSheetDialog bottomSheetDialog;
    private boolean mLocationPermissionGranted = false;

    private Toolbar toolbar;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private ViewPagerAdapter viewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.patient_activities);

        setup();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(checkMapServices()){
            if(mLocationPermissionGranted){
                getLocationPermission();
            }
            else{
//                getChatrooms();
            }
        }
    }

    private void setup(){
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        userEmail = firebaseUser.getEmail();
        System.out.println("User email: " + firebaseUser.getEmail());

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Emergency Alert");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        toolbar.setBackgroundColor(ContextCompat.getColor(PatientActivities.this, R.color.whiteColor));
        toolbar.setTitleTextColor(ContextCompat.getColor(PatientActivities.this, R.color.colorPrimary));

        tabLayout = findViewById(R.id.tab_layout);
        viewPager = findViewById(R.id.view_pager);
        tabLayout.setupWithViewPager(viewPager);

        homeFragment = new HomeFragment();
        doctorsAndNurses = new DoctorsAndNurses();
        hospitalFragment = new HospitalFragment();
        accountFragment = new AccountFragment();

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
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });

//        BadgeDrawable badgeDrawable = tabLayout.getTabAt(0).getOrCreateBadge();
//        badgeDrawable.setVisible(true);
//        badgeDrawable.setNumber(12);
        bottomSheetDialog = new BottomSheetDialog();
    }

    @Override
    public void onBackPressed() {
        if(viewPager.getCurrentItem() == 0){
            super.finish();
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

    public void showBottomSheet(){
        bottomSheetDialog.show(getSupportFragmentManager(), "BottomSheet");
    }

    @Override
    public void onButtonClicked(String text) {
        if(text.equals("ambulance")){
            Snackbar.make(findViewById(R.id.rootLayout), text, Snackbar.LENGTH_SHORT).show();
        }
        else if(text.equals("message")){
            Snackbar.make(findViewById(R.id.rootLayout), text, Snackbar.LENGTH_SHORT).show();
        }
        else {
            bottomSheetDialog.dismiss();
        }
    }

    public void logoutUser(){
        firebaseAuth.signOut();
        Intent intent = new Intent(this, Login.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    private boolean checkMapServices(){
        if(isServicesOK()){
            if(isMapsEnabled()){
                return true;
            }
        }
        return false;
    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("This application requires GPS to work properly, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        Intent enableGpsIntent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivityForResult(enableGpsIntent, PERMISSIONS_REQUEST_ENABLE_GPS);
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    public boolean isMapsEnabled(){
        final LocationManager manager = (LocationManager) getSystemService( Context.LOCATION_SERVICE );

        if ( !manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
            buildAlertMessageNoGps();
            return false;
        }
        return true;
    }

    private void getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
//            getChatrooms();
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    public boolean isServicesOK(){

        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(PatientActivities.this);

        if(available == ConnectionResult.SUCCESS){
            //everything is fine and the user can make map requests
//            Log.d(TAG, "isServicesOK: Google Play Services is working");
            return true;
        }
        else if(GoogleApiAvailability.getInstance().isUserResolvableError(available)){
            //an error occured but we can resolve it
//            Log.d(TAG, "isServicesOK: an error occured but we can fix it");
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(PatientActivities.this, available, ERROR_DIALOG_REQUEST);
            dialog.show();
        }else{
            Toast.makeText(this, "You can't make map requests", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        Log.d(TAG, "onActivityResult: called.");
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ENABLE_GPS: {
                if(!mLocationPermissionGranted){
                    getLocationPermission();
                }
                else{
//                    getChatrooms();
                }
            }
        }

    }

}






















