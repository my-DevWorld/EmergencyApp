package com.example.emergencyalertapp.models.patient;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

public class User implements Parcelable {
    private String email;
    private String userID;
    private String username;
    private String category;
    private String avatar;
    private boolean recordsAvailable;
    private @ServerTimestamp Date timeStamp;

    public User() {
    }

    public User(String email, String userID,
                String username, String category,
                String avatar, boolean recordsAvailable, Date timeStamp) {
        this.email = email;
        this.userID = userID;
        this.username = username;
        this.category = category;
        this.avatar = avatar;
        this.recordsAvailable = recordsAvailable;
        this.timeStamp = timeStamp;
    }


    protected User(Parcel in) {
        email = in.readString();
        userID = in.readString();
        username = in.readString();
        category = in.readString();
        avatar = in.readString();
        recordsAvailable = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(email);
        dest.writeString(userID);
        dest.writeString(username);
        dest.writeString(category);
        dest.writeString(avatar);
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public boolean isRecordsAvailable() {
        return recordsAvailable;
    }

    public void setRecordsAvailable(boolean recordsAvailable) {
        this.recordsAvailable = recordsAvailable;
    }

    public Date getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Date timeStamp) {
        this.timeStamp = timeStamp;
    }

    @Override
    public String toString() {
        return "User{" +
                "email='" + email + '\'' +
                ", userID='" + userID + '\'' +
                ", username='" + username + '\'' +
                ", category='" + category + '\'' +
                ", avatar='" + avatar + '\'' +
                ", recordsAvailable=" + recordsAvailable +
                ", timeStamp=" + timeStamp +
                '}';
    }
}
