package com.example.emergencyalertapp.models.service_providers;

import android.os.Parcel;
import android.os.Parcelable;

public class ServiceProvider implements Parcelable {
    private String userID;
    private String username;
    private String fullName;
    private String email;
    private String gender;
    private String phoneNum;
    private String residentialAddress;
    private String serviceType;
    private String hospital;
    private String speciality;

    public ServiceProvider() {
    }

    public ServiceProvider(String userID, String username,
                           String fullName, String email,
                           String gender, String phoneNum,
                           String residentialAddress, String serviceType,
                           String hospital, String speciality) {
        this.userID = username;
        this.username = username;
        this.fullName = fullName;
        this.email = email;
        this.gender = gender;
        this.phoneNum = phoneNum;
        this.residentialAddress = residentialAddress;
        this.serviceType = serviceType;
        this.hospital = hospital;
        this.speciality = speciality;
    }

    protected ServiceProvider(Parcel in) {
        userID = in.readString();
        username = in.readString();
        fullName = in.readString();
        email = in.readString();
        gender = in.readString();
        phoneNum = in.readString();
        residentialAddress = in.readString();
        serviceType = in.readString();
        hospital = in.readString();
        speciality = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(userID);
        dest.writeString(username);
        dest.writeString(fullName);
        dest.writeString(email);
        dest.writeString(gender);
        dest.writeString(phoneNum);
        dest.writeString(residentialAddress);
        dest.writeString(serviceType);
        dest.writeString(hospital);
        dest.writeString(speciality);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ServiceProvider> CREATOR = new Creator<ServiceProvider>() {
        @Override
        public ServiceProvider createFromParcel(Parcel in) {
            return new ServiceProvider(in);
        }

        @Override
        public ServiceProvider[] newArray(int size) {
            return new ServiceProvider[size];
        }
    };

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getResidentialAddress() {
        return residentialAddress;
    }

    public void setResidentialAddress(String residentialAddress) {
        this.residentialAddress = residentialAddress;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public String getHospital() {
        return hospital;
    }

    public void setHospital(String hospital) {
        this.hospital = hospital;
    }

    public String getSpeciality() {
        return speciality;
    }

    public void setSpeciality(String speciality) {
        this.speciality = speciality;
    }

    @Override
    public String toString() {
        return "ServiceProvider{" +
                "userID='" + userID + '\'' +
                ", username='" + username + '\'' +
                ", fullName='" + fullName + '\'' +
                ", email='" + email + '\'' +
                ", gender='" + gender + '\'' +
                ", phoneNum='" + phoneNum + '\'' +
                ", residentialAddress='" + residentialAddress + '\'' +
                ", serviceType='" + serviceType + '\'' +
                ", hospital='" + hospital + '\'' +
                ", speciality='" + speciality + '\'' +
                '}';
    }
}























