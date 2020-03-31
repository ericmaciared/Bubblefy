package edu.url.salle.eric.macia.bubblefy.restapi.callback;

import edu.url.salle.eric.macia.bubblefy.model.Search;

public interface SearchCallback extends FailureCallback{
    void onSearchReceived(Search search);
    void onNoSearch(Throwable throwable);
}
