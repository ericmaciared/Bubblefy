package edu.url.salle.eric.macia.bubblefy.controller.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import edu.url.salle.eric.macia.bubblefy.R;
import edu.url.salle.eric.macia.bubblefy.model.Search;
import edu.url.salle.eric.macia.bubblefy.restapi.callback.SearchCallback;
import edu.url.salle.eric.macia.bubblefy.restapi.manager.SearchManager;


public class SearchActivity extends AppCompatActivity implements SearchCallback, RadioGroup.OnCheckedChangeListener {

    RadioGroup radioGroup;
    RadioButton radioButton1;
    RadioButton radioButton2;
    RadioButton radioButton3;
    TextView textToShow;
    private Search mSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        radioGroup = findViewById(R.id.group_buttons);
        radioButton1 = findViewById(R.id.radio_song);
        radioButton2 = findViewById(R.id.radio_playlists);
        radioButton3 = findViewById(R.id.radio_users);
        textToShow = findViewById(R.id.textprova);

        radioGroup.setOnCheckedChangeListener(this);

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

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if(radioButton1.isChecked()){
            textToShow.setText("hola");}
        else if(radioButton2.isChecked()){
            textToShow.setText("fina ara");}
        else
            textToShow.setText("adeu");
    }
}
