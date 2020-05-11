package edu.url.salle.eric.macia.bubblefy.restapi.callback;


import java.util.ArrayList;
import java.util.List;

import edu.url.salle.eric.macia.bubblefy.model.Playlist;

public interface PlaylistCallback extends FailureCallback{

    void onPlaylistCreated(Playlist playlist);
    void onPlaylistFailure(Throwable throwable);
    void onUserPlaylistReceived(ArrayList<Playlist> playlist);
    void onPlaylistUpdated(Playlist playlist);
    void onNoPlaylist(Throwable throwable);
}
