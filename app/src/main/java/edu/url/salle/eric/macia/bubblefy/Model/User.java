package edu.url.salle.eric.macia.bubblefy.Model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class User implements Serializable {

    @SerializedName("activated")
    private Boolean activated;

    @SerializedName("authorities")
    private List<String> authorities = null;

    @SerializedName("createdBy")
    private String createdBy;

    @SerializedName("createdDate")
    private String createdDate;

    @SerializedName("email")
    private String email;

    @SerializedName("firstName")
    private String firstName;

    @SerializedName("lastName")
    private String ;

}
