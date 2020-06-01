package edu.url.salle.eric.macia.bubblefy.controller.fragments;

import android.content.Intent;
import android.drm.DrmStore;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import edu.url.salle.eric.macia.bubblefy.R;
import edu.url.salle.eric.macia.bubblefy.controller.adapters.SelectPlaylistAdapter;
import edu.url.salle.eric.macia.bubblefy.controller.adapters.SelectTrackAdapter;
import edu.url.salle.eric.macia.bubblefy.model.Confirmation;
import edu.url.salle.eric.macia.bubblefy.model.Playlist;
import edu.url.salle.eric.macia.bubblefy.model.Track;
import edu.url.salle.eric.macia.bubblefy.restapi.callback.PlaylistCallback;
import edu.url.salle.eric.macia.bubblefy.restapi.callback.TrackCallback;
import edu.url.salle.eric.macia.bubblefy.restapi.manager.CloudinaryManager;
import edu.url.salle.eric.macia.bubblefy.restapi.manager.PlaylistManager;
import edu.url.salle.eric.macia.bubblefy.restapi.manager.TrackManager;
import edu.url.salle.eric.macia.bubblefy.utils.Constants;

import static android.app.Activity.RESULT_OK;


public class AddSongToPlaylistFragment extends Fragment
        implements PlaylistCallback {

    private static final String TAG = "TestPlaybackActivity";
    private static final String PLAY_VIEW = "PlayIcon";
    private static final String STOP_VIEW = "StopIcon";

    private RecyclerView mRecyclerView;
    private ArrayList<Playlist> mPlaylist;
    private ArrayList<Playlist> playlistSelected;
    private Button buttonCreate;
    private EditText playlistName;
    private CheckBox checkBox;
    private boolean searchPerformed = false;
    private View view;
    private Playlist playlist;
    private Button btnFindImg;
    private Uri mImageUri = null;
    private boolean hasImage = false;
    private Track track;
    private int received = 0;

    private int currentTrack = 0;

    public AddSongToPlaylistFragment(Track track) {
        this.track = track;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.fragment_add_song_to_playlist, container, false);

        getData();

        return view;
    }

    public void getData(){
        PlaylistManager.getInstance(getActivity()).getOwnPlaylist(this);

    }

    private void initViews(View v) {


        buttonCreate = v.findViewById(R.id.search_button);
        mRecyclerView = (RecyclerView) v.findViewById(R.id.recyclerView);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
        SelectPlaylistAdapter adapter = new SelectPlaylistAdapter(getContext(), mPlaylist);
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(adapter);

        buttonCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playlistSelected = adapter.getSelectedPlaylist();
                addSong();
            }
        });
    }


    public void addSong(){
        if(playlistSelected.size() == 0){
            Toast toast = null;
            toast =  Toast.makeText(getContext(),
                    "You must select a playlist",
                    Toast.LENGTH_SHORT);
            toast.show();
        }else{

            for(int i = 0; i<playlistSelected.size(); i++){
                playlistSelected.get(i).getTracks().add(track);
                PlaylistManager.getInstance(getActivity()).updatePlaylist(playlistSelected.get(i), this);
            }
        }
    }


    @Override
    public void onFailure(Throwable throwable) {

    }

    @Override
    public void onPlaylistCreated(Playlist playlist) {

    }

    @Override
    public void onPlaylistFailure(Throwable throwable) {

    }

    @Override
    public void onUserPlaylistReceived(ArrayList<Playlist> playlist) {
        mPlaylist = playlist;
        initViews(view);
    }

    @Override
    public void onUserFollowingPlaylistReceived(ArrayList<Playlist> playlist) {

    }

    @Override
    public void onPlaylistUpdated(Playlist playlist) {
        received++;
        if(received == playlistSelected.size()){
            Toast toast = null;
            toast =  Toast.makeText(getContext(),
                    "All playlists updated",
                    Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    @Override
    public void onNoPlaylist(Throwable throwable) {

    }

    @Override
    public void onFollowSuccess() {

    }
}
