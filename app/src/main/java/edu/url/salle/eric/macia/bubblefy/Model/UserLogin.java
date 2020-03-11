package edu.url.salle.eric.macia.bubblefy.Model;

import com.google.gson.annotations.SerializedName;

public class UserLogin {

    @SerializedName("username")
    private String username;
    @SerializedName("password")
    private String password;
    @SerializedName("rememberMe")
    private boolean rememberMe;

    public UserLogin(String username, String password, boolean rememberMe) {
        this.username = username;
        this.password = password;
        this.rememberMe = rememberMe;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isRememberMe() {
        return rememberMe;
    }

    public void setRememberMe(boolean rememberMe) {
        this.rememberMe = rememberMe;
    }
}
