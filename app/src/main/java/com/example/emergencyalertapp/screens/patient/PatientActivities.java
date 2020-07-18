package com.example.emergencyalertapp.screens.patient;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import com.example.emergencyalertapp.R;
import com.example.emergencyalertapp.adapters.ViewPagerAdapter;
import com.example.emergencyalertapp.models.Hospital;
import com.example.emergencyalertapp.models.User;
import com.example.emergencyalertapp.models.service_providers.ServiceProvider;
import com.example.emergencyalertapp.screens.patient.fragments.AccountFragment;
import com.example.emergencyalertapp.screens.patient.fragments.DocNurseDetailsBottomSheetDialog;
import com.example.emergencyalertapp.screens.patient.fragments.SendAlertBottomSheetDialog;
import com.example.emergencyalertapp.screens.patient.fragments.DoctorsAndNursesFragment;
import com.example.emergencyalertapp.screens.patient.fragments.HomeFragment;
import com.example.emergencyalertapp.screens.patient.fragments.HospitalFragment;
import com.example.emergencyalertapp.utils.CheckNetworkConnectivity;
import com.example.emergencyalertapp.utils.UserClient;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

import onboarding.screens.Login;

import static com.example.emergencyalertapp.Constants.ERROR_DIALOG_REQUEST;
import static com.example.emergencyalertapp.Constants.PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION;
import static com.example.emergencyalertapp.Constants.PERMISSIONS_REQUEST_ENABLE_GPS;
import static com.example.emergencyalertapp.Constants.MAKE_PHONE_CALL_REQUEST_CODE;
import static com.example.emergencyalertapp.Constants.SEND_SMS_REQUEST_CODE;

public class PatientActivities extends AppCompatActivity implements SendAlertBottomSheetDialog.BottomSheetListener {

    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private FirebaseFirestore db;
    private CollectionReference usersCollection;
    private CollectionReference hospitalCollection;
    private CollectionReference serviceProviderCollection;

    private HomeFragment homeFragment;
    private HospitalFragment hospitalFragment;
    private AccountFragment accountFragment;
    private DoctorsAndNursesFragment doctorsAndNursesFragment;

    public String userEmail;
    private SendAlertBottomSheetDialog sendAlertBottomSheetDialog;
//    private DocNurseDetailsBottomSheetDialog docNurseDetailsBottomSheetDialog;
    private boolean mLocationPermissionGranted = false;
    public FusedLocationProviderClient mFusedLocationClient;
    private User user;
    public Location location;
    public ArrayList<Hospital> hospitals;
    public ArrayList<ServiceProvider> serviceProviders;

    //widgets
    private Toolbar toolbar;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private ViewPagerAdapter viewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.patient_activities);

        setup();
        getAuthenticatedUser();
    }

    private void setup(){
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();
        hospitalCollection = db.collection("Hospitals and Clinics");
        serviceProviderCollection = db.collection("Service Providers");
        userEmail = firebaseUser.getEmail();

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
        doctorsAndNursesFragment = new DoctorsAndNursesFragment();
        hospitalFragment = new HospitalFragment();
        accountFragment = new AccountFragment();

        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), 0);
        viewPagerAdapter.addFragment(homeFragment);
        viewPagerAdapter.addFragment(doctorsAndNursesFragment);
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
        sendAlertBottomSheetDialog = new SendAlertBottomSheetDialog();
//        docNurseDetailsBottomSheetDialog = new DocNurseDetailsBottomSheetDialog();
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
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

    public void sendAlertShowBottomSheet(){
        sendAlertBottomSheetDialog.show(getSupportFragmentManager(), "BottomSheet");
    }

    public void showDocNurseDetailsBottomSheet() {
//        docNurseDetailsBottomSheetDialog.show(getSupportFragmentManager(), "Doc_Nurse BottomSheetDialog");
    }

    @Override
    public void onSendAlertButtonClicked(String text) {
        if (text.equals("ambulance")) {
            makePhoneCall();
        }
        else {
            sendSMS();
        }
    }


//
//    @Override
//    public void viewServiceProviderDetailsClicked(String text) {
//
//    }

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
                .setPositiveButton("Yes", (dialog, id) -> {
                    Intent enableGpsIntent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    PatientActivities.this.startActivityForResult(enableGpsIntent, PERMISSIONS_REQUEST_ENABLE_GPS);
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
            getLastKnownLocation();
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
            return true;
        }
        else if(GoogleApiAvailability.getInstance().isUserResolvableError(available)){
            //an error occured but we can resolve it
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(PatientActivities.this, available, ERROR_DIALOG_REQUEST);
            dialog.show();
        }
        else{
            Toast.makeText(this, "You can't make map requests", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                }
                break;
            }
            case MAKE_PHONE_CALL_REQUEST_CODE:
                makePhoneCall();
                break;

            case SEND_SMS_REQUEST_CODE:
                sendSMS();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ENABLE_GPS:
                if(mLocationPermissionGranted){
                    getLastKnownLocation();
                    getAuthenticatedUser();
                }
                else{
                    getLocationPermission();
                }
                break;
        }
    }

    private void getLastKnownLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mFusedLocationClient.getLastLocation().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                location = task.getResult();
                if(location != null){
                    GeoPoint geoPoint = new GeoPoint(location.getLatitude(), location.getLongitude());
                    System.out.println(">>>>>>>>>>>>>>>> " + geoPoint.getLatitude() + " / " + geoPoint.getLongitude());
                }
            }
        });

    }

    private void makePhoneCall() {
        if (ContextCompat.checkSelfPermission(PatientActivities.this,
                Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(PatientActivities.this,
                    new String[]{Manifest.permission.CALL_PHONE}, MAKE_PHONE_CALL_REQUEST_CODE);

        } else {
            String dial = "tel:" + getString(R.string.ambulance_service_number);
            startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
        }
    }

    private void sendSMS(){
        if(ContextCompat.checkSelfPermission(PatientActivities.this,
                Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(PatientActivities.this,
                    new String[]{Manifest.permission.SEND_SMS}, SEND_SMS_REQUEST_CODE);
        }
        else {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage("+233264449268", null, "the text goes here", null, null);
            Toast.makeText(this, "Alert message sent.", Toast.LENGTH_SHORT).show();
        }
    }

    private void getAuthenticatedUser(){
        if (CheckNetworkConnectivity.getInstance(this).isOnline()) {
            firebaseAuth = FirebaseAuth.getInstance();
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
                            if(user == null){
//                                Toast.makeText(this, "User is empty", Toast.LENGTH_SHORT).show();
                                System.out.println(">>>>>>>>>>>>>>>>>>>>>>>: " + user.toString());
                            }
                            ((UserClient)getApplicationContext()).setUser(user);
                            getHospitals();
                            getServiceProviders();
                        });
                if(user == null){
//                    Toast.makeText(this, "User is empty", Toast.LENGTH_SHORT).show();
                    //TODO:
                }
            }

        } else {
            Toast.makeText(this, "No internet connection", Toast.LENGTH_SHORT).show();
        }
    }

    private void getHospitals(){
        hospitals = new ArrayList<>();
        hospitalCollection.addSnapshotListener(this, (queryDocumentSnapshots, e) -> {
            if(e != null){
                return;
            }
            hospitals.clear();
            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                hospitals.add(documentSnapshot.toObject(Hospital.class));
            }
        });
    }

    private void getServiceProviders(){
        serviceProviders = new ArrayList<>();
        serviceProviderCollection.addSnapshotListener(this, (queryDocumentSnapshots, e) -> {
            if(e != null){
                return;
            }
            serviceProviders.clear();
            for(QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){

                serviceProviders.add(documentSnapshot.toObject(ServiceProvider.class));
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(checkMapServices()){
            if(mLocationPermissionGranted){
                getLastKnownLocation();
            }
            else{
                getLocationPermission();
            }
        }
    }
}






















