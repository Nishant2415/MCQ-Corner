package com.scorpions.mcqcorner.model;

public class ProfileModel {

    private String username, website, mobileNo, email, profilePic;

    public ProfileModel() { }

    public ProfileModel(String username, String website, String mobileNo, String email, String profilePic) {
        this.username = username;
        this.website = website;
        this.mobileNo = mobileNo;
        this.email = email;
        this.profilePic = profilePic;
    }

    public String getUsername() {
        return username;
    }

    public String getWebsite() {
        return website;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public String getEmail() {
        return email;
    }

    public String getProfilePic() {
        return profilePic;
    }
}
