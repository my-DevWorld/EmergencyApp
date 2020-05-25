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
import com.example.emergencyalertapp.models.service_providers.ServiceProvider;

import java.util.ArrayList;

public class ServiceProviderAdapter extends RecyclerView.Adapter<ServiceProviderAdapter.ServiceProviderAdapterViewHolder> {
    private Context context;
    private ArrayList<ServiceProvider> serviceProviders;
    private GetServiceProviderDetails getServiceProviderDetails;

    public ServiceProviderAdapter(Context context, ArrayList<ServiceProvider> serviceProviders, GetServiceProviderDetails getServiceProviderDetails) {
        this.context = context;
        this.serviceProviders = serviceProviders;
        this.getServiceProviderDetails = getServiceProviderDetails;
    }

    @NonNull
    @Override
    public ServiceProviderAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.service_provider_item, parent, false);
        ServiceProviderAdapterViewHolder serviceProviderAdapterViewHolder = new ServiceProviderAdapterViewHolder(view);
        return serviceProviderAdapterViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ServiceProviderAdapterViewHolder holder, int position) {
        ServiceProvider serviceProvider = serviceProviders.get(position);
        holder.provider_name.setText(serviceProvider.getFullName());
        holder.service_type.setText(serviceProvider.getServiceType());
        holder.itemView.setOnClickListener(v -> {
            getServiceProviderDetails.onServiceProviderClicked(serviceProvider);
        });
    }

    @Override
    public int getItemCount() {
        return serviceProviders.size();
    }

    public class ServiceProviderAdapterViewHolder extends RecyclerView.ViewHolder {
        private ImageView provider_image;
        private TextView provider_name, service_type;
        public ServiceProviderAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            provider_image = itemView.findViewById(R.id.provider_image);
            provider_name = itemView.findViewById(R.id.provider_name);
            service_type = itemView.findViewById(R.id.service_type);
        }
    }

    public interface GetServiceProviderDetails{
        void onServiceProviderClicked(ServiceProvider serviceProvider);
    }
}





