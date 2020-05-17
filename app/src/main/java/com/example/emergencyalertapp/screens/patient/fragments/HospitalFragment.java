package com.example.emergencyalertapp.screens.patient.fragments;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.emergencyalertapp.R;
import com.example.emergencyalertapp.models.Hospital;
import com.example.emergencyalertapp.screens.patient.PatientActivities;
import com.example.emergencyalertapp.screens.patient.adapters.CustomInfoWindowAdapter;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.GeoPoint;

import java.util.ArrayList;

import static com.example.emergencyalertapp.screens.patient.Constants.DEFAULT_ZOOM;
import static com.example.emergencyalertapp.screens.patient.Constants.MAPVIEW_BUNDLE_KEY;

public class HospitalFragment extends Fragment implements OnMapReadyCallback {

    private MapView mMapView;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private GoogleMap map;
    private LatLngBounds mMapBoundary;
    private ArrayList<LatLng> latLngs;
    private ArrayList<MarkerOptions> markerOptions;
    private ArrayList<String> titles;
    private ArrayList<String> locations;



    public HospitalFragment() {}

    public static HospitalFragment newInstance() {
        return new HospitalFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.hospital_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mMapView = view.findViewById(R.id.google_map);
        initGoogleMap(savedInstanceState);
    }

    private void initGoogleMap(Bundle savedInstanceState){
        // *** IMPORTANT ***
        // MapView requires that the Bundle you pass contain _ONLY_ MapView SDK
        // objects or sub-Bundles.
        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY);
        }
        mMapView.onCreate(mapViewBundle);
        mMapView.getMapAsync(this);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Bundle mapViewBundle = outState.getBundle(MAPVIEW_BUNDLE_KEY);
        if (mapViewBundle == null) {
            mapViewBundle = new Bundle();
            outState.putBundle(MAPVIEW_BUNDLE_KEY, mapViewBundle);
        }
        mMapView.onSaveInstanceState(mapViewBundle);
    }

    @Override
    public void onMapReady(GoogleMap map) {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        this.map = map;
        map.setMyLocationEnabled(true);
        map.getUiSettings().setMyLocationButtonEnabled(true);
        getDeviceLocation();
        map.getUiSettings().setZoomGesturesEnabled(true);
        map.getUiSettings().setZoomControlsEnabled(true);
        map.getUiSettings().setRotateGesturesEnabled(true);
    }

    private void getDeviceLocation(){
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());
                final Task location = mFusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        Location currentLocation = (Location) task.getResult();
                        LatLng latLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
                        String markerTitle = "";
                        String markerSnippet = "";
//                        MarkerOptions myCurrentLocation = new MarkerOptions()
//                                .position(latLng)
//                                .title(null)
//                                .snippet(null);
//                        map.addMarker(myCurrentLocation);
                        moveCamera(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()), DEFAULT_ZOOM);
                        getHospitalLocation();
                    }
                });
    }

    private void moveCamera(LatLng latLng, float zoom){
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
    }

    private void getHospitalLocation(){
        map.setInfoWindowAdapter(new CustomInfoWindowAdapter(getActivity()));
        latLngs = new ArrayList<>();
        titles = new ArrayList<>();
        locations = new ArrayList<>();
        markerOptions = new ArrayList<>();
        for(Hospital hospital : ((PatientActivities)getActivity()).hospitals){
            latLngs.add(new LatLng(hospital.getGeoLocation().getLatitude(), hospital.getGeoLocation().getLongitude()));
            titles.add(hospital.getHospitalName());
            locations.add(hospital.getRegion());
        }
        for(int i = 0; i < latLngs.size(); i++){
            markerOptions.add(new MarkerOptions().position(latLngs.get(i)).title(titles.get(i)).snippet(locations.get(i)));
            map.addMarker(markerOptions.get(i));
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();
        mMapView.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        mMapView.onStop();
    }

    @Override
    public void onPause() {
        mMapView.onPause();
        super.onPause();
    }

    @Override
    public void onDestroy() {
        mMapView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }
}















