package com.example.emergencyalertapp.screens.patient.fragments;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.example.emergencyalertapp.R;
import com.example.emergencyalertapp.adapters.HospitalAdapter;
import com.example.emergencyalertapp.models.Hospital;
import com.example.emergencyalertapp.screens.patient.PatientActivities;
import com.example.emergencyalertapp.screens.patient.adapters.CustomInfoWindowAdapter;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;

import static com.example.emergencyalertapp.screens.patient.Constants.DEFAULT_ZOOM;
import static com.example.emergencyalertapp.screens.patient.Constants.MAPVIEW_BUNDLE_KEY;

public class HospitalFragment extends Fragment implements OnMapReadyCallback {

    //widgets
    private TextView mapView, listView;
    private RelativeLayout map_container, list_container;
    private RecyclerView recycler_view;

    private MapView mMapView;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private GoogleMap map;
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
        recycler_view = view.findViewById(R.id.recycler_view);
        recycler_view.setLayoutManager(new LinearLayoutManager(getContext()));
        recycler_view.setHasFixedSize(true);
        mapView = view.findViewById(R.id.mapView);
        listView = view.findViewById(R.id.listView);
        map_container = view.findViewById(R.id.map_container);
        list_container = view.findViewById(R.id.list_container);
        list_container.setVisibility(View.GONE);

        listView.setOnClickListener(v -> {
            list_container.setVisibility(View.VISIBLE);
            YoYo.with(Techniques.SlideOutLeft)
                    .duration(250)
                    .delay(900)
                    .playOn(map_container);
            map_container.setVisibility(View.GONE);

            YoYo.with(Techniques.SlideInRight)
                    .duration(250)
                    .playOn(list_container);
            activeView(list_container, listView, mapView);

            listView.setClickable(false);
            mapView.setClickable(true);
        });

        mapView.setOnClickListener(v -> {
            map_container.setVisibility(View.VISIBLE);
            YoYo.with(Techniques.SlideInLeft)
                    .duration(250)
                    .playOn(map_container);

            YoYo.with(Techniques.SlideOutRight)
                    .duration(250)
                    .delay(900)
                    .playOn(list_container);
            activeView(map_container, mapView, listView);
            listView.setClickable(true);
            mapView.setClickable(false);
        });

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
        map.getUiSettings().setRotateGesturesEnabled(false);
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

        HospitalAdapter hospitalAdapter = new HospitalAdapter(getContext(), ((PatientActivities)getActivity()).hospitals);
        recycler_view.setAdapter(hospitalAdapter);
    }

    private void activeView(RelativeLayout relativeLayout ,TextView textView1, TextView textView2){
        if(relativeLayout.getVisibility() == View.VISIBLE){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                textView1.setTextColor(getActivity().getColor(R.color.whiteColor));
                textView1.setBackgroundColor(getActivity().getColor(R.color.primaryButtonColorActive));

                textView2.setTextColor(getActivity().getColor(R.color.primaryButtonColorActive));
                textView2.setBackgroundColor(getActivity().getColor(R.color.whiteColor));
            }
            else {
                textView1.setTextColor(getResources().getColor(R.color.whiteColor));
                textView1.setBackgroundColor(getResources().getColor(R.color.primaryButtonColorActive));

                textView2.setTextColor(getResources().getColor(R.color.primaryButtonColorActive));
                textView2.setBackgroundColor(getResources().getColor(R.color.whiteColor));
            }
        }
        else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                textView1.setTextColor(getActivity().getColor(R.color.primaryButtonColorActive));
                textView1.setBackgroundColor(getActivity().getColor(R.color.whiteColor));

                textView2.setTextColor(getActivity().getColor(R.color.whiteColor));
                textView2.setBackgroundColor(getActivity().getColor(R.color.primaryButtonColorActive));
            }
            else {
                textView1.setTextColor(getResources().getColor(R.color.primaryButtonColorActive));
                textView1.setBackgroundColor(getResources().getColor(R.color.whiteColor));

                textView2.setTextColor(getResources().getColor(R.color.whiteColor));
                textView2.setBackgroundColor(getResources().getColor(R.color.primaryButtonColorActive));
            }
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















