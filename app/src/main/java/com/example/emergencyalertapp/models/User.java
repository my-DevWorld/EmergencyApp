package com.example.emergencyalertapp.models;

import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable {
    private String userID;
    private String category;
    private String dateCreated;
    private String timeZone;
    private boolean recordsAvailable;

    public User() {
    }

    public User(String userID, String category, String dateCreated, String timeZone, boolean recordsAvailable) {
        this.userID = userID;
        this.category = category;
        this.dateCreated = dateCreated;
        this.timeZone = timeZone;
        this.recordsAvailable = recordsAvailable;
    }

    protected User(Parcel in) {
        userID = in.readString();
        category = in.readString();
        dateCreated = in.readString();
        timeZone = in.readString();
        recordsAvailable = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(userID);
        dest.writeString(category);
        dest.writeString(dateCreated);
        dest.writeString(timeZone);
        dest.writeByte((byte) (recordsAvailable ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

        public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }

    public boolean isRecordsAvailable() {
        return recordsAvailable;
    }

    public void setRecordsAvailable(boolean recordsAvailable) {
        this.recordsAvailable = recordsAvailable;
    }
}
