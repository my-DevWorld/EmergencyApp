package com.example.emergencyalertapp.models.patient;

import android.os.Parcel;
import android.os.Parcelable;

public class PatientProfile implements Parcelable {
    private String fullName;
    private String dateOfBirth;
    private String gender;
    private String phoneNum;
    private String residentialAddress;


    public PatientProfile() {
    }

    public PatientProfile(String fullName, String dateOfBirth,
                          String gender, String phoneNum,
                          String residentialAddress) {
        this.fullName = fullName;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.phoneNum = phoneNum;
        this.residentialAddress = residentialAddress;
    }

    protected PatientProfile(Parcel in) {
        fullName = in.readString();
        dateOfBirth = in.readString();
        gender = in.readString();
        phoneNum = in.readString();
        residentialAddress = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(fullName);
        dest.writeString(dateOfBirth);
        dest.writeString(gender);
        dest.writeString(phoneNum);
        dest.writeString(residentialAddress);
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

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
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

    @Override
    public String toString() {
        return "PatientProfile{" +
                "fullName='" + fullName + '\'' +
                ", dateOfBirth='" + dateOfBirth + '\'' +
                ", gender='" + gender + '\'' +
                ", phoneNum='" + phoneNum + '\'' +
                ", residentialAddress='" + residentialAddress + '\'' +
                '}';
    }
}
