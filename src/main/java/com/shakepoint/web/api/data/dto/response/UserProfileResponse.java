package com.shakepoint.web.api.data.dto.response;

import java.util.List;

public class UserProfileResponse {

    private String userName;
    private String userId;
    private String userSince;
    private boolean availableProfile;
    private String birthday;
    private double weight;
    private double height;
    private double purchasesTotal;
    private String email;


    private List<UserPurchaseResponse> purchases;

    public UserProfileResponse() {
    }

    public UserProfileResponse(String userName, String userId, String userSince, boolean availableProfile, String birthday, double weight, double height, double purchasesTotal, String email) {
        this.userName = userName;
        this.userId = userId;
        this.userSince = userSince;
        this.availableProfile = availableProfile;
        this.birthday = birthday;
        this.weight = weight;
        this.height = height;
        this.purchasesTotal = purchasesTotal;
        this.email = email;
    }

    public boolean isAvailableProfile() {
        return availableProfile;
    }

    public void setAvailableProfile(boolean availableProfile) {
        this.availableProfile = availableProfile;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public List<UserPurchaseResponse> getPurchases() {
        return purchases;
    }

    public void setPurchases(List<UserPurchaseResponse> purchases) {
        this.purchases = purchases;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserSince() {
        return userSince;
    }

    public void setUserSince(String userSince) {
        this.userSince = userSince;
    }

    public double getPurchasesTotal() {
        return purchasesTotal;
    }

    public void setPurchasesTotal(double purchasesTotal) {
        this.purchasesTotal = purchasesTotal;
    }

    public double getCmi() {

        if (weight > 0 && height > 0){
            //get real mts
            final double heightMts = height / 100;
            return this.weight / (Math.pow(heightMts, 2));
        }else{
            return 0;
        }
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


}
