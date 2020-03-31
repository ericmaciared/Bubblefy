package edu.url.salle.eric.macia.bubblefy.controller.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import edu.url.salle.eric.macia.bubblefy.R;
import edu.url.salle.eric.macia.bubblefy.model.Search;
import edu.url.salle.eric.macia.bubblefy.restapi.callback.SearchCallback;
import edu.url.salle.eric.macia.bubblefy.restapi.manager.SearchManager;


public class SearchActivity extends AppCompatActivity implements SearchCallback {

    private Search mSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        SearchManager.getInstance(this).getSearchResult("Hola", this);
    }

    @Override
    public void onSearchReceived(Search search) {
        mSearch = search;
        System.out.println(mSearch);
    }

    @Override
    public void onNoSearch(Throwable throwable) {

    }

    @Override
    public void onFailure(Throwable throwable) {

    }
}
