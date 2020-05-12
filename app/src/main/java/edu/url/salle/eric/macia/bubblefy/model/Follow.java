package edu.url.salle.eric.macia.bubblefy.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Follow implements Serializable {

    @SerializedName("followed")
    private Boolean followed;

    public Boolean getFollowed() {
        return followed;
    }
}
