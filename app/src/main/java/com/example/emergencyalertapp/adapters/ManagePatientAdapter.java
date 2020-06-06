package com.example.emergencyalertapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.emergencyalertapp.R;
import com.example.emergencyalertapp.models.service_providers.PatientDetails;
import com.example.emergencyalertapp.screens.service_provider.activities.ManagePatient;

import java.util.ArrayList;

public class ManagePatientAdapter extends RecyclerView.Adapter<ManagePatientAdapter.ManagePatientAdapterViewHolder> {

    private Context context;
    private ArrayList<PatientDetails> patientDetails;
    private OnPatientDetailsListener onPatientDetailsListener;

    public ManagePatientAdapter() {
    }

    public ManagePatientAdapter(Context context, ArrayList<PatientDetails> patientDetails, OnPatientDetailsListener onPatientDetailsListener) {
        this.context = context;
        this.patientDetails = patientDetails;
        this.onPatientDetailsListener = onPatientDetailsListener;
    }

    @NonNull
    @Override
    public ManagePatientAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.manage_patient_item, parent, false);
        return new ManagePatientAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ManagePatientAdapterViewHolder holder, int position) {
        PatientDetails patientDetail = this.patientDetails.get(position);
        holder.provider_name.setText(patientDetail.getFullName());

        holder.delete_Patient.setOnClickListener(v -> {
            onPatientDetailsListener.getPatientDetailsClicked(patientDetail, position);
//            patientDetails.remove(position);
//            notifyDataSetChanged();
        });
    }

    @Override
    public int getItemCount() {
        return patientDetails.size();
    }

    public void removePatient(){
//        patientDetails.remove(position);
        notifyDataSetChanged();
    }

    public class ManagePatientAdapterViewHolder extends RecyclerView.ViewHolder {
        private TextView provider_name;
        private ImageView delete_Patient;
        public ManagePatientAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            delete_Patient = itemView.findViewById(R.id.delete_Patient);
            provider_name = itemView.findViewById(R.id.provider_name);
        }
    }

    public interface OnPatientDetailsListener{
        void getPatientDetailsClicked(PatientDetails patientDetails, int index);
    }
}





