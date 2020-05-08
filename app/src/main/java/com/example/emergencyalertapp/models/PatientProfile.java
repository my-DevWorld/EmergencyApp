package com.example.emergencyalertapp.models;

import android.os.Parcel;
import android.os.Parcelable;

public class PatientProfile implements Parcelable {
    private String name;
    private String dateOfBirth;
    private String gender;
    private String phoneNum;
    private String residentialAddress;
    private String weight;
    private String height;
    private String bloodGroup;
    private String allergies;

    public PatientProfile() {
    }

    public PatientProfile(String name, String dateOfBirth,
                          String gender, String phoneNum,
                          String residentialAddress, String weight,
                          String height, String bloodGroup, String allergies) {
        this.name = name;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.phoneNum = phoneNum;
        this.residentialAddress = residentialAddress;
        this.weight = weight;
        this.height = height;
        this.bloodGroup = bloodGroup;
        this.allergies = allergies;
    }

    protected PatientProfile(Parcel in) {
        name = in.readString();
        dateOfBirth = in.readString();
        gender = in.readString();
        phoneNum = in.readString();
        residentialAddress = in.readString();
        weight = in.readString();
        height = in.readString();
        bloodGroup = in.readString();
        allergies = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(dateOfBirth);
        dest.writeString(gender);
        dest.writeString(phoneNum);
        dest.writeString(residentialAddress);
        dest.writeString(weight);
        dest.writeString(height);
        dest.writeString(bloodGroup);
        dest.writeString(allergies);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<PatientProfile> CREATOR = new Creator<PatientProfile>() {
        @Override
        public PatientProfile createFromParcel(Parcel in) {
            return new PatientProfile(in);
        }

        @Override
        public PatientProfile[] newArray(int size) {
            return new PatientProfile[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
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

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

    public String getAllergies() {
        return allergies;
    }

    public void setAllergies(String allergies) {
        this.allergies = allergies;
    }
}
