package edu.url.salle.eric.macia.bubblefy.restapi.callback;


import edu.url.salle.eric.macia.bubblefy.model.Playlist;

public interface PlaylistCallback{

    void onPlaylistCreated(Playlist playlist);
    void onPlaylistFailure(Throwable throwable);
}
