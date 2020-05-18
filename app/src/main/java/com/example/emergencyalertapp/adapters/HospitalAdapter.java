package com.example.emergencyalertapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.emergencyalertapp.R;
import com.example.emergencyalertapp.models.Hospital;

import java.util.ArrayList;

public class HospitalAdapter extends RecyclerView.Adapter<HospitalAdapter.HospitalViewHolder> {
    private Context context;
    private ArrayList<Hospital> hospitals;

    public HospitalAdapter(Context context, ArrayList<Hospital> hospitals) {
        this.context = context;
        this.hospitals = hospitals;
    }

    @NonNull
    @Override
    public HospitalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.hospital_item, parent, false);
        HospitalViewHolder viewHolder = new HospitalViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull HospitalViewHolder holder, int position) {
        Hospital hospital = hospitals.get(position);
        holder.hospital_name.setText(hospital.getHospitalName());
        holder.hospital_specialty.setText(hospital.getSpecialty());
        holder.hospital_location.setText(hospital.getRegion());
    }

    @Override
    public int getItemCount() {
        return hospitals.size();
    }

    public class HospitalViewHolder extends RecyclerView.ViewHolder {
        private ImageView hospital_image;
        private TextView hospital_name, hospital_location, hospital_specialty;
        public HospitalViewHolder(@NonNull View itemView) {
            super(itemView);
            hospital_image = itemView.findViewById(R.id.hospital_image);
            hospital_name = itemView.findViewById(R.id.hospital_name);
            hospital_specialty = itemView.findViewById(R.id.hospital_specialty);
            hospital_location = itemView.findViewById(R.id.hospital_location);
        }
    }
}

















