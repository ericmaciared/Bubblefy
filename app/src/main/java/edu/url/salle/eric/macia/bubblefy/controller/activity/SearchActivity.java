package edu.url.salle.eric.macia.bubblefy.controller.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import edu.url.salle.eric.macia.bubblefy.R;
import edu.url.salle.eric.macia.bubblefy.controller.adapters.PlaylistListAdapter;
import edu.url.salle.eric.macia.bubblefy.controller.adapters.TrackListAdapter;
import edu.url.salle.eric.macia.bubblefy.controller.adapters.UserListAdapter;
import edu.url.salle.eric.macia.bubblefy.model.Playlist;
import edu.url.salle.eric.macia.bubblefy.model.Search;
import edu.url.salle.eric.macia.bubblefy.model.Track;
import edu.url.salle.eric.macia.bubblefy.model.User;
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
    private ArrayList<Playlist> mPlay;
    private ArrayList<User> mUsers;
    private Button buttonLogin;
    private EditText searchText;
    private boolean searchPerformed = false;
    private ImageButton ibtnHome;
    private ImageButton ibtnSearch;
    private ImageButton ibtnProfile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        radioGroup = findViewById(R.id.group_buttons);
        radioButton1 = findViewById(R.id.radio_song);
        radioButton2 = findViewById(R.id.radio_playlists);
        radioButton3 = findViewById(R.id.radio_users);
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
        mPlay = (ArrayList) search.getPlaylists();
        mUsers = (ArrayList) search.getUsers();
        searchPerformed = true;
        String text;

        if(radioButton1.isChecked()){
            if(!mTracks.isEmpty()) {
                TrackListAdapter adapter = new TrackListAdapter(this, mTracks);
                mRecyclerView.setAdapter(adapter);
            }
            else{
                TrackListAdapter adapter = new TrackListAdapter(this, null);
                mRecyclerView.setAdapter(adapter);
                text = "No songs found";
                if(!searchPerformed) text = "No search done";
                Toast toast =  Toast.makeText(SearchActivity.this, text, Toast.LENGTH_SHORT);
                toast.show();
            }
        }
        else if(radioButton2.isChecked()){
            if(!mPlay.isEmpty()) {
                PlaylistListAdapter adapter = new PlaylistListAdapter(this, mPlay);
                mRecyclerView.setAdapter(adapter);
            }
            else{
                PlaylistListAdapter adapter = new PlaylistListAdapter(this, null);
                mRecyclerView.setAdapter(adapter);
                text = "No playlists found";
                if(!searchPerformed) text = "No search done";
                Toast toast =  Toast.makeText(SearchActivity.this, text, Toast.LENGTH_SHORT);
                toast.show();
            }
        }
        else{
            if(!mUsers.isEmpty()) {
                UserListAdapter adapter = new UserListAdapter(this, mUsers);
                mRecyclerView.setAdapter(adapter);
            }
            else{
                UserListAdapter adapter = new UserListAdapter(null, mUsers);
                mRecyclerView.setAdapter(adapter);
                text = "No users found";
                if(!searchPerformed) text = "No search done";
                Toast toast =  Toast.makeText(SearchActivity.this, text, Toast.LENGTH_SHORT);
                toast.show();
            }
        }
    }


    private void initViews() {
        mTracks = new ArrayList<>();
        mPlay = new ArrayList<>();
        mUsers = new ArrayList<>();

        ibtnHome = (ImageButton) findViewById(R.id.home_button);
        ibtnSearch = (ImageButton) findViewById(R.id.search_button_down);
        ibtnProfile = (ImageButton) findViewById(R.id.profile_button);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        LinearLayoutManager manager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        TrackListAdapter adapter = new TrackListAdapter(this, null);
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.setAdapter(adapter);

        ibtnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                return;
            }
        });

        ibtnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadMainScreen();
            }
        });
    }

    private void loadMainScreen() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void onNoSearch(Throwable throwable) {

    }

    @Override
    public void onFailure(Throwable throwable) {

    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        String text;

        if(radioButton1.isChecked()){
            if(!mTracks.isEmpty()) {
                TrackListAdapter adapter = new TrackListAdapter(this, mTracks);
                mRecyclerView.setAdapter(adapter);
            }
            else{
                TrackListAdapter adapter = new TrackListAdapter(this, null);
                mRecyclerView.setAdapter(adapter);
                text = "No songs found";
                if(!searchPerformed) text = "No search done";
                Toast toast =  Toast.makeText(SearchActivity.this, text, Toast.LENGTH_SHORT);
                toast.show();
            }
        }
        else if(radioButton2.isChecked()){
            if(!mPlay.isEmpty()) {
                PlaylistListAdapter adapter = new PlaylistListAdapter(this, mPlay);
                mRecyclerView.setAdapter(adapter);
            }
            else{
                PlaylistListAdapter adapter = new PlaylistListAdapter(this, null);
                mRecyclerView.setAdapter(adapter);
                text = "No playlists found";
                if(!searchPerformed) text = "No search done";
                Toast toast =  Toast.makeText(SearchActivity.this, text, Toast.LENGTH_SHORT);
                toast.show();
            }
        }
        else{
            if(!mUsers.isEmpty()) {
                UserListAdapter adapter = new UserListAdapter(this, mUsers);
                mRecyclerView.setAdapter(adapter);
            }
            else{
                UserListAdapter adapter = new UserListAdapter(null, mUsers);
                mRecyclerView.setAdapter(adapter);
                text = "No users found";
                if(!searchPerformed) text = "No search done";
                Toast toast =  Toast.makeText(SearchActivity.this, text, Toast.LENGTH_SHORT);
                toast.show();
            }
        }
    }

    @Override
    public void onPlaylistCreated(Playlist playlist) {

    }

    @Override
    public void onPlaylistFailure(Throwable throwable) {

    }
}