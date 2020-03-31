package edu.url.salle.eric.macia.bubblefy.controller.callbacks;

import edu.url.salle.eric.macia.bubblefy.model.Track;

public interface TrackListCallback {
    void onTrackSelected(Track track);
    void onTrackSelected(int index);
}