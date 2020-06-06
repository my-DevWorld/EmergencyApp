package com.example.emergencyalertapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.emergencyalertapp.R;
import com.example.emergencyalertapp.models.service_providers.PatientDetails;

import java.util.ArrayList;

public class PatientDetailsAdapter extends RecyclerView.Adapter<PatientDetailsAdapter.PatientDetailsViewHolder> {

    private Context context;
    private ArrayList<PatientDetails> patientDetails;
    private OnPatientDetailsListener onPatientDetailsListener;

    public PatientDetailsAdapter(Context context, ArrayList<PatientDetails> patientDetails, OnPatientDetailsListener onPatientDetailsListener) {
        this.context = context;
        this.patientDetails = patientDetails;
        this.onPatientDetailsListener = onPatientDetailsListener;
    }

    @NonNull
    @Override
    public PatientDetailsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.service_provider_item, parent, false);
        return new PatientDetailsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PatientDetailsViewHolder holder, int position) {
        PatientDetails patientDetails = this.patientDetails.get(position);
        holder.provider_name.setText(patientDetails.getFullName());
        holder.service_type.setVisibility(View.GONE);

        holder.itemView.setOnClickListener(v -> {
            onPatientDetailsListener.getPatientDetailsClicked(patientDetails);
        });
    }

    @Override
    public int getItemCount() {
        return patientDetails.size();
    }

    public class PatientDetailsViewHolder extends RecyclerView.ViewHolder {
        private TextView provider_name, service_type;

        public PatientDetailsViewHolder(@NonNull View itemView) {
            super(itemView);
            provider_name = itemView.findViewById(R.id.provider_name);
            service_type = itemView.findViewById(R.id.service_type);
        }
    }

    public interface OnPatientDetailsListener{
        void getPatientDetailsClicked(PatientDetails patientDetails);
    }
}
