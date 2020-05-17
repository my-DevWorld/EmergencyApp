package com.example.emergencyalertapp.models.patient;

import android.os.Parcel;
import android.os.Parcelable;

public class EmergencyContact implements Parcelable{
    private String contactName;
    private String contactRelationship;
    private String contactPhoneNumb;
    private String contactResidentialAddress;

    public EmergencyContact() {
    }

    public EmergencyContact(String contactName, String contactRelationship, String contactPhoneNumb, String contactResidentialAddress) {
        this.contactName = contactName;
        this.contactRelationship = contactRelationship;
        this.contactPhoneNumb = contactPhoneNumb;
        this.contactResidentialAddress = contactResidentialAddress;
    }

    protected EmergencyContact(Parcel in) {
        contactName = in.readString();
        contactRelationship = in.readString();
        contactPhoneNumb = in.readString();
        contactResidentialAddress = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(contactName);
        dest.writeString(contactRelationship);
        dest.writeString(contactPhoneNumb);
        dest.writeString(contactResidentialAddress);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<EmergencyContact> CREATOR = new Creator<EmergencyContact>() {
        @Override
        public EmergencyContact createFromParcel(Parcel in) {
            return new EmergencyContact(in);
        }

        @Override
        public EmergencyContact[] newArray(int size) {
            return new EmergencyContact[size];
        }
    };

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactRelationship() {
        return contactRelationship;
    }

    public void setContactRelationship(String contactRelationship) {
        this.contactRelationship = contactRelationship;
    }

    public String getContactPhoneNumb() {
        return contactPhoneNumb;
    }

    public void setContactPhoneNumb(String contactPhoneNumb) {
        this.contactPhoneNumb = contactPhoneNumb;
    }

    public String getContactResidentialAddress() {
        return contactResidentialAddress;
    }

    public void setContactResidentialAddress(String contactResidentialAddress) {
        this.contactResidentialAddress = contactResidentialAddress;
    }
}
