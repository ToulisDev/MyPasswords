package com.familyprotection.djasdatabase.Models;

public class PasswordRequest {

    private String passwordsSite;
    private String passwordsUsername;
    private String passwordsPassword;

    @SuppressWarnings("unused")
    public String getPasswordsSite() {
        return passwordsSite;
    }

    public void setPasswordsSite(String passwordsSite) {
        this.passwordsSite = passwordsSite;
    }

    @SuppressWarnings("unused")
    public String getPasswordsUsername() {
        return passwordsUsername;
    }

    public void setPasswordsUsername(String passwordsUsername) {
        this.passwordsUsername = passwordsUsername;
    }

    @SuppressWarnings("unused")
    public String getPasswordsPassword() {
        return passwordsPassword;
    }

    public void setPasswordsPassword(String passwordsPassword) {
        this.passwordsPassword = passwordsPassword;
    }
}
