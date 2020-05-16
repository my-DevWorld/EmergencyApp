package com.example.emergencyalertapp.models;

import android.os.Parcel;
import android.os.Parcelable;

public class MedicalRecord implements Parcelable {
    private String weight;
    private String height;
    private String bloodGroup;
    private String allergies;

    public MedicalRecord() {
    }

    public MedicalRecord(String weight, String height, String bloodGroup, String allergies) {
        this.weight = weight;
        this.height = height;
        this.bloodGroup = bloodGroup;
        this.allergies = allergies;
    }

    protected MedicalRecord(Parcel in) {
        weight = in.readString();
        height = in.readString();
        bloodGroup = in.readString();
        allergies = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(weight);
        dest.writeString(height);
        dest.writeString(bloodGroup);
        dest.writeString(allergies);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<MedicalRecord> CREATOR = new Creator<MedicalRecord>() {
        @Override
        public MedicalRecord createFromParcel(Parcel in) {
            return new MedicalRecord(in);
        }

        @Override
        public MedicalRecord[] newArray(int size) {
            return new MedicalRecord[size];
        }
    };

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
