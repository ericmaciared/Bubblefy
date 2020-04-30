package edu.url.salle.eric.macia.bubblefy.restapi.callback;

import java.util.ArrayList;

import edu.url.salle.eric.macia.bubblefy.model.Genre;
import edu.url.salle.eric.macia.bubblefy.model.Track;

public interface GenreCallback extends FailureCallback{
    void onGenresReceive(ArrayList<Genre> genres);
    void onTracksByGenre(ArrayList<Track> tracks);
}
