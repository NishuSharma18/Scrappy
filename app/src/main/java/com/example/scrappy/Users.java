/*
Created by Abhinav Pandey on 03/06/23, 3:11 PM
 */
package com.example.scrappy;

public class Users {
    private String userId;
    private String userName;
    private String email;
    private String password;
    private String profilePic;
    private String phone;

    public Users() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public Users(String userId, String userName, String email, String password, String phone) {
        this.userId = userId;
        this.userName = userName;
        this.email = email;
        this.password = password;
        this.phone = phone;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public String getPhone() {
        return phone;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserId() {
        return userId;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}