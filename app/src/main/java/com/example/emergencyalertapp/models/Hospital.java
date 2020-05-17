package com.example.emergencyalertapp.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.firestore.GeoPoint;

public class Hospital implements Parcelable {
    private String hospitalName;
    private String region;
    private String specialty;
    private String type;
    private GeoPoint geoLocation;

    public Hospital() {
    }

    public Hospital(String hospitalName, String region, String specialty, String type, GeoPoint geoLocation) {
        this.hospitalName = hospitalName;
        this.region = region;
        this.specialty = specialty;
        this.type = type;
        this.geoLocation = geoLocation;
    }

    protected Hospital(Parcel in) {
        hospitalName = in.readString();
        region = in.readString();
        specialty = in.readString();
        type = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(hospitalName);
        dest.writeString(region);
        dest.writeString(specialty);
        dest.writeString(type);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Hospital> CREATOR = new Creator<Hospital>() {
        @Override
        public Hospital createFromParcel(Parcel in) {
            return new Hospital(in);
        }

        @Override
        public Hospital[] newArray(int size) {
            return new Hospital[size];
        }
    };

    public String getHospitalName() {
        return hospitalName;
    }

    public void setHospitalName(String hospitalName) {
        this.hospitalName = hospitalName;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getSpecialty() {
        return specialty;
    }

    public void setSpecialty(String specialty) {
        this.specialty = specialty;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public GeoPoint getGeoLocation() {
        return geoLocation;
    }

    public void setGeoLocation(GeoPoint geoLocation) {
        this.geoLocation = geoLocation;
    }

    @Override
    public String toString() {
        return "Hospital{" +
                "hospitalName='" + hospitalName + '\'' +
                ", region='" + region + '\'' +
                ", specialty='" + specialty + '\'' +
                ", type='" + type + '\'' +
                ", geoLocation=" + geoLocation +
                '}';
    }
}
