package edu.url.salle.eric.macia.bubblefy.restapi.callback;

import edu.url.salle.eric.macia.bubblefy.model.Track;

public interface ListCallback {
    void onTrackSelected(Track track);
    void onTrackSelected(int index);
}
