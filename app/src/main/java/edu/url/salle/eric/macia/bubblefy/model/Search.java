package edu.url.salle.eric.macia.bubblefy.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Search {

    @SerializedName("playlists")
    private List<Playlist> playlists = null;

    @SerializedName("users")
    private List<User> users = null;

    @SerializedName("tracks")
    private List<Track> tracks = null;

    public List<Playlist> getPlaylists() {
        return playlists;
    }

    public void setPlaylists(List<Playlist> playlists) {
        this.playlists = playlists;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public List<Track> getTracks() {
        return tracks;
    }

    public void setTracks(List<Track> tracks) {
        this.tracks = tracks;
    }
}
