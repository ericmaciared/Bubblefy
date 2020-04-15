package edu.url.salle.eric.macia.bubblefy.restapi.callback;

import java.util.List;

import edu.url.salle.eric.macia.bubblefy.model.Track;

public interface TrackCallback extends FailureCallback {
    void onTracksReceived(List<Track> tracks);
    void onNoTracks(Throwable throwable);
    void onPersonalTracksReceived(List<Track> tracks);
    void onUserTracksReceived(List<Track> tracks);
    void onUserLikedTracksReceived(List<Track> tracks);
}
