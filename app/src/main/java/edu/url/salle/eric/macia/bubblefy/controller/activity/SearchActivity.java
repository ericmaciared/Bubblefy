package edu.url.salle.eric.macia.bubblefy.controller.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.ArrayList;

import edu.url.salle.eric.macia.bubblefy.R;
import edu.url.salle.eric.macia.bubblefy.controller.adapters.TrackListAdapter;
import edu.url.salle.eric.macia.bubblefy.model.Playlist;
import edu.url.salle.eric.macia.bubblefy.model.Search;
import edu.url.salle.eric.macia.bubblefy.model.Track;
import edu.url.salle.eric.macia.bubblefy.restapi.callback.PlaylistCallback;
import edu.url.salle.eric.macia.bubblefy.restapi.callback.SearchCallback;
import edu.url.salle.eric.macia.bubblefy.restapi.manager.PlaylistManager;
import edu.url.salle.eric.macia.bubblefy.restapi.manager.SearchManager;


public class SearchActivity extends AppCompatActivity implements SearchCallback, RadioGroup.OnCheckedChangeListener, PlaylistCallback {

    RadioGroup radioGroup;
    RadioButton radioButton1;
    RadioButton radioButton2;
    RadioButton radioButton3;
    TextView textToShow;
    private RecyclerView mRecyclerView;
    private ArrayList<Track> mTracks;
    private Button buttonLogin;
    private EditText searchText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        radioGroup = findViewById(R.id.group_buttons);
        radioButton1 = findViewById(R.id.radio_song);
        radioButton2 = findViewById(R.id.radio_playlists);
        radioButton3 = findViewById(R.id.radio_users);
        textToShow = findViewById(R.id.textprova);
        buttonLogin = findViewById(R.id.search_button);
        radioGroup.setOnCheckedChangeListener(this);
        searchText = findViewById(R.id.search_text);
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSearch();
            }
        });

        initViews();
    }

    public void getSearch(){
        SearchManager.getInstance(this).getSearchResult((searchText.getText()).toString(), this);
    }

    @Override
    public void onSearchReceived(Search search) {
        mTracks = (ArrayList) search.getTracks();
        TrackListAdapter adapter = new TrackListAdapter(this, mTracks);
        mRecyclerView.setAdapter(adapter);

        Playlist playlist = new Playlist();
        playlist.setName("Playlist");
        playlist.setPublicAccessible(true);
        PlaylistManager manager = new PlaylistManager(this);
        manager.createPlaylist(playlist, this);
    }


    private void initViews() {
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        LinearLayoutManager manager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        TrackListAdapter adapter = new TrackListAdapter(this, null);
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.setAdapter(adapter);
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

    @Override
    public void onPlaylistCreated(Playlist playlist) {

    }

    @Override
    public void onPlaylistFailure(Throwable throwable) {

    }
}