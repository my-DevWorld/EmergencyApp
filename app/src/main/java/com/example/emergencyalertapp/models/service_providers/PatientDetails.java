package com.example.emergencyalertapp.models.service_providers;

import android.os.Parcel;
import android.os.Parcelable;

public class PatientDetails implements Parcelable {
    private String patientID;
    private String userName;
    private String fullName;
    private String allergies;
    private String bloodType;
    private String height;
    private String weight;
    private boolean onMedication;
    private String treatment;
    private String patientPhoneNum;
    private String patientResAddress;

    public PatientDetails() {
    }

    public PatientDetails(String patientID, String userName, String fullName,
                          String allergies, String bloodType,
                          String height, String weight,
                          boolean onMedication, String treatment,
                          String patientPhoneNum, String patientResAddress) {
        this.patientID = patientID;
        this.userName = userName;
        this.fullName = fullName;
        this.allergies = allergies;
        this.bloodType = bloodType;
        this.height = height;
        this.weight = weight;
        this.onMedication = onMedication;
        this.treatment = treatment;
        this.patientPhoneNum = patientPhoneNum;
        this.patientResAddress = patientResAddress;
    }


    protected PatientDetails(Parcel in) {
        patientID = in.readString();
        userName = in.readString();
        fullName = in.readString();
        allergies = in.readString();
        bloodType = in.readString();
        height = in.readString();
        weight = in.readString();
        onMedication = in.readByte() != 0;
        treatment = in.readString();
        patientPhoneNum = in.readString();
        patientResAddress = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(patientID);
        dest.writeString(userName);
        dest.writeString(fullName);
        dest.writeString(allergies);
        dest.writeString(bloodType);
        dest.writeString(height);
        dest.writeString(weight);
        dest.writeByte((byte) (onMedication ? 1 : 0));
        dest.writeString(treatment);
        dest.writeString(patientPhoneNum);
        dest.writeString(patientResAddress);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<PatientDetails> CREATOR = new Creator<PatientDetails>() {
        @Override
        public PatientDetails createFromParcel(Parcel in) {
            return new PatientDetails(in);
        }

        @Override
        public PatientDetails[] newArray(int size) {
            return new PatientDetails[size];
        }
    };

    public String getPatientID() {
        return patientID;
    }

    public void setPatientID(String patientID) {
        this.patientID = patientID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getAllergies() {
        return allergies;
    }

    public void setAllergies(String allergies) {
        this.allergies = allergies;
    }

    public String getBloodType() {
        return bloodType;
    }

    public void setBloodType(String bloodType) {
        this.bloodType = bloodType;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public boolean isOnMedication() {
        return onMedication;
    }

    public void setOnMedication(boolean onMedication) {
        this.onMedication = onMedication;
    }

    public String getTreatment() {
        return treatment;
    }

    public void setTreatment(String treatment) {
        this.treatment = treatment;
    }

    public String getPatientPhoneNum() {
        return patientPhoneNum;
    }

    public void setPatientPhoneNum(String patientPhoneNum) {
        this.patientPhoneNum = patientPhoneNum;
    }

    public String getPatientResAddress() {
        return patientResAddress;
    }

    public void setPatientResAddress(String patientResAddress) {
        this.patientResAddress = patientResAddress;
    }

    @Override
    public String toString() {
        return "PatientContact{" +
                "patientID='" + patientID + '\'' +
                "userName='" + userName + '\'' +
                ", fullName='" + fullName + '\'' +
                ", allergies='" + allergies + '\'' +
                ", bloodType='" + bloodType + '\'' +
                ", height='" + height + '\'' +
                ", weight='" + weight + '\'' +
                ", onMedication=" + onMedication +
                ", treatment='" + treatment + '\'' +
                ", patientPhoneNum='" + patientPhoneNum + '\'' +
                ", patientResAddress='" + patientResAddress + '\'' +
                '}';
    }
}




































