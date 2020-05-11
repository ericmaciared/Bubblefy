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
        implements TrackCallback, PlaylistCallback {

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

    private int currentTrack = 0;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.fragment_add_song_to_playlist, container, false);

        getData();

        return view;
    }

    public void getData(){
        TrackManager.getInstance(getActivity()).getOwnTracks(this);
    }

    private void initViews(View v) {


        buttonCreate = v.findViewById(R.id.search_button);
        checkBox = v.findViewById(R.id.checkbox);
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
                createPlaylist();
            }
        });
    }


    public void createPlaylist(){
        if((playlistName.getText()).toString().equals("")){
            Toast toast = null;
            toast =  Toast.makeText(getContext(),
                    "Missing name",
                    Toast.LENGTH_SHORT);
            toast.show();
        }else{
            playlist = new Playlist();
            playlist.setName((playlistName.getText()).toString());
            //playlist.setTracks(playlistSelected);
            playlist.setPublicAccessible(checkBox.isChecked());

            PlaylistManager.getInstance(getActivity()).createPlaylist(playlist, this);

        }
    }


    @Override
    public void onTracksReceived(List<Track> tracks) {

    }

    @Override
    public void onNoTracks(Throwable throwable) {

    }

    @Override
    public void onPersonalTracksReceived(List<Track> tracks) {
    }

    @Override
    public void onUserTracksReceived(List<Track> tracks) {

    }

    @Override
    public void onUserLikedTracksReceived(List<Track> tracks) {

    }

    @Override
    public void onLikeOperationSuccess(Confirmation confirmation) {

    }

    @Override
    public void onLikeOperationFailure(Throwable throwable) {

    }

    @Override
    public void onReceiveLikeSuccess(Confirmation confirmation) {

    }

    @Override
    public void onReceiveLikeFailure(Throwable throwable) {

    }

    @Override
    public void onCreateTrack() {

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
}
