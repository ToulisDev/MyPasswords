package com.familyprotection.djasdatabase.Models;

import com.google.gson.annotations.SerializedName;

public class PasswordResponse {
    @SerializedName("passwordsId")
    private String passwordsId;
    @SerializedName("passwordsSite")
    private String passwordsSite;
    @SerializedName("passwordsUsername")
    private String passwordsUsername;
    @SerializedName("passwordsPassword")
    private String passwordsPassword;
    @SerializedName("userId")
    private String userId;

    public String getPasswordsId() {
        return passwordsId;
    }

    @SuppressWarnings("unused")
    public void setPasswordsId(String passwordsId) {
        this.passwordsId = passwordsId;
    }

    public String getPasswordsSite() {
        return passwordsSite;
    }

    @SuppressWarnings("unused")
    public void setPasswordsSite(String passwordsSite) {
        this.passwordsSite = passwordsSite;
    }

    public String getPasswordsUsername() {
        return passwordsUsername;
    }

    @SuppressWarnings("unused")
    public void setPasswordsUsername(String passwordsUsername) {
        this.passwordsUsername = passwordsUsername;
    }

    public String getPasswordsPassword() {
        return passwordsPassword;
    }

    @SuppressWarnings("unused")
    public void setPasswordsPassword(String passwordsPassword) {
        this.passwordsPassword = passwordsPassword;
    }

    @SuppressWarnings("unused")
    public String getUserId() {
        return userId;
    }

    @SuppressWarnings("unused")
    public void setUserId(String userId) {
        this.userId = userId;
    }
}
