package com.example.emergencyalertapp.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import com.example.emergencyalertapp.models.service_providers.ServiceProvider;

public class SharedPreference {
    public interface Key{
        String NAME = "EmergencyAlert";
        String NUMBER_OF_CONTACT = "number-of-contacts";
        String SERVICE_PROVIDER = "service-provider";
    }

    private SharedPreferences sharedPreferences;
    private static SharedPreference sharedPreference;
    private Context context;

    public SharedPreference(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences ( Key.NAME, Context.MODE_PRIVATE );
    }

    public static SharedPreference getInstance(Context context){
        if(sharedPreference == null){
            sharedPreference = new SharedPreference(context);
        }
        return sharedPreference;
    }

    public synchronized String getNumberOnContact(){
        return this.sharedPreferences.getString(Key.NUMBER_OF_CONTACT, "1");
    }

    public synchronized void setNumberOnContact(String numberOnContact){
        Runnable runnable = () -> this.sharedPreferences.edit().putString(Key.NUMBER_OF_CONTACT, numberOnContact).apply();
        AsyncTask.execute(runnable);
    }
}



























