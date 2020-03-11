package edu.url.salle.eric.macia.bubblefy.Model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

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

    @SerializedName("followers")
    private Integer followers;

    @SerializedName("following")
    private Integer following;

    @SerializedName("id")
    private Integer id;

    @SerializedName("imageUrl")
    private String imageUrl;

    @SerializedName("langKey")
    private String langKey;

    @SerializedName("lastModifiedBy")
    private String lastModifiedBy;
    
    @SerializedName("lastModifiedDate")
    private String lastModifiedDate;

    @SerializedName("lastName")
    private String lastName;

    @SerializedName("login")
    private String login;

    @SerializedName("playlists")
    private Integer playlists;

    @SerializedName("tracks")
    private Integer tracks;
}
