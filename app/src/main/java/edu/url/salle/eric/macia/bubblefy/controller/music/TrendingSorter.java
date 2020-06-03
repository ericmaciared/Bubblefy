package edu.url.salle.eric.macia.bubblefy.controller.music;

import java.util.Comparator;

import edu.url.salle.eric.macia.bubblefy.model.Track;

public class TrendingSorter implements Comparator<Track>
{
    @Override
    public int compare(Track o1, Track o2) {
        return o1.getPlays().compareTo(o2.getPlays());
    }
}
