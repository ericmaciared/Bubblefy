package edu.url.salle.eric.macia.bubblefy.model;

import com.google.gson.annotations.SerializedName;

public class Confirmation {

    @SerializedName("liked")
    private boolean liked;

    public Confirmation(boolean liked) {
        this.liked = liked;
    }

    public boolean isLiked() {
        return liked;
    }

    public void setLiked(boolean liked) {
        this.liked = liked;
    }
}