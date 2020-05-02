package edu.url.salle.eric.macia.bubblefy.controller.fragments;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import edu.url.salle.eric.macia.bubblefy.R;
import edu.url.salle.eric.macia.bubblefy.controller.activity.MainActivity;
import edu.url.salle.eric.macia.bubblefy.controller.adapters.PlaylistListAdapter;
import edu.url.salle.eric.macia.bubblefy.controller.adapters.TrackListAdapter;
import edu.url.salle.eric.macia.bubblefy.controller.adapters.UserListAdapter;
import edu.url.salle.eric.macia.bubblefy.controller.callbacks.TrackListCallback;
import edu.url.salle.eric.macia.bubblefy.model.Confirmation;
import edu.url.salle.eric.macia.bubblefy.model.Playlist;
import edu.url.salle.eric.macia.bubblefy.model.Search;
import edu.url.salle.eric.macia.bubblefy.model.Track;
import edu.url.salle.eric.macia.bubblefy.model.User;
import edu.url.salle.eric.macia.bubblefy.restapi.callback.PlaylistCallback;
import edu.url.salle.eric.macia.bubblefy.restapi.callback.SearchCallback;
import edu.url.salle.eric.macia.bubblefy.restapi.callback.TrackCallback;
import edu.url.salle.eric.macia.bubblefy.restapi.manager.SearchManager;


public class SearchFragment extends Fragment
        implements TrackCallback, TrackListCallback, SearchCallback,
        RadioGroup.OnCheckedChangeListener, PlaylistCallback,
        BottomSheetDialog.BottomSheetListener {

    private static final String TAG = "TestPlaybackActivity";
    private static final String PLAY_VIEW = "PlayIcon";
    private static final String STOP_VIEW = "StopIcon";

    RadioGroup radioGroup;
    RadioButton radioButton1;
    RadioButton radioButton2;
    RadioButton radioButton3;
    private RecyclerView mRecyclerView;
    private ArrayList<Track> mTracks;
    private ArrayList<Playlist> mPlay;
    private ArrayList<User> mUsers;
    private Button buttonLogin;
    private EditText searchText;
    private boolean searchPerformed = false;

    private Handler mHandler;
    private Runnable mRunnable;

    private int currentTrack = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v =  inflater.inflate(R.layout.fragment_search, container, false);

        initViews(v);
        return v;
    }

    private void initViews(View v){
        mTracks = new ArrayList<>();
        mPlay = new ArrayList<>();
        mUsers = new ArrayList<>();

        radioGroup = v.findViewById(R.id.group_buttons);
        radioButton1 = v.findViewById(R.id.radio_song);
        radioButton2 = v.findViewById(R.id.radio_playlists);
        radioButton3 = v.findViewById(R.id.radio_users);
        buttonLogin = v.findViewById(R.id.search_button);
        radioGroup.setOnCheckedChangeListener(this);
        searchText = v.findViewById(R.id.search_text);
        buttonLogin.setOnClickListener(v1 -> getSearch());

        mRecyclerView = (RecyclerView) v.findViewById(R.id.recyclerView);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
        TrackListAdapter adapter = new TrackListAdapter(this, getContext(), null);
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.setAdapter(adapter);

        MainActivity.mediaPlayer = new MediaPlayer();
        MainActivity.mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        MainActivity.mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                playAudio();
                int audioSessionId = MainActivity.mediaPlayer.getAudioSessionId();
            }
        });

        mHandler = new Handler();


        /*
        btnBackward = (ImageButton)findViewById(R.id.dynamic_backward_btn);
        btnBackward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentTrack = ((currentTrack-1)%(mTracks.size()));
                currentTrack = currentTrack < 0 ? (mTracks.size()-1):currentTrack;
                updateTrack(mTracks.get(currentTrack));
            }
        });

        btnForward = (ImageButton)findViewById(R.id.dynamic_forward_btn);
        btnForward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentTrack = ((currentTrack+1)%(mTracks.size()));
                currentTrack = currentTrack >= mTracks.size() ? 0:currentTrack;

                updateTrack(mTracks.get(currentTrack));
            }
        });

         */

        /*
        mSeekBar = (SeekBar) findViewById(R.id.dynamic_seekBar);
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    mPlayer.seekTo(progress);
                }
                if (mDuration > 0) {
                    int newProgress = ((progress*100)/mDuration);
                    System.out.println("New progress: " + newProgress);
                }
            }


            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
         */
    }

    /************************SEARCH IMPLEMENTATION**********************/
    public void getSearch(){
        SearchManager.getInstance(getActivity()).getSearchResult((searchText.getText()).toString(),
                this);
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
                TrackListAdapter adapter = new TrackListAdapter(this,getActivity(), mTracks);
                mRecyclerView.setAdapter(adapter);
                adapter.setItemClickedListener(new TrackListAdapter.OnItemClickedListener() {
                    @Override
                    public void onItemClicked(int position) {
                        trackClicked(position);
                    }

                    @Override
                    public void onOptionsClicked(int position) {
                        trackOptions(position);
                    }

                });
            }
            else{
                TrackListAdapter adapter = new TrackListAdapter(this,getActivity(), null);
                mRecyclerView.setAdapter(adapter);
                text = "No songs found";
                if(!searchPerformed) text = "No search done";
                Toast toast =  Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT);
                toast.show();
            }
        }
        else if(radioButton2.isChecked()){
            if(!mPlay.isEmpty()) {
                PlaylistListAdapter adapter = new PlaylistListAdapter(getActivity(), mPlay);
                mRecyclerView.setAdapter(adapter);
                adapter.setItemClickedListener(new PlaylistListAdapter.OnItemClickedListener() {
                    @Override
                    public void onItemClicked(int position) {
                        playlistClicked(position);
                    }

                    @Override
                    public void onOptionsClicked(int position) {
                        playlistOptions(position);
                    }
                });
            }
            else{
                PlaylistListAdapter adapter = new PlaylistListAdapter(getActivity(), null);
                mRecyclerView.setAdapter(adapter);
                text = "No playlists found";
                if(!searchPerformed) text = "No search done";
                Toast toast =  Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT);
                toast.show();
            }
        }
        else{
            if(!mUsers.isEmpty()) {
                UserListAdapter adapter = new UserListAdapter(getActivity(), mUsers);
                mRecyclerView.setAdapter(adapter);
                adapter.setItemClickedListener(new UserListAdapter.OnItemClickedListener() {
                    @Override
                    public void onItemClicked(int position) {
                        userClicked(position);
                    }

                    @Override
                    public void onOptionsClicked(int position) {
                        userOptions(position);
                    }
                });
            }
            else{
                UserListAdapter adapter = new UserListAdapter(null, mUsers);
                mRecyclerView.setAdapter(adapter);
                text = "No users found";
                if(!searchPerformed) text = "No search done";
                Toast toast =  Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT);
                toast.show();
            }
        }
    }



    /******************************MUSIC PLAYBACK******************************/
    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void playAudio() {
        MainActivity.playAudio();
    }

    private void pauseAudio() {
        MainActivity.pauseAudio();
    }

    private void prepareMediaPlayer(final String url) {
        Thread connection = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    MainActivity.mediaPlayer.setDataSource(url);
                    MainActivity.mediaPlayer.prepare(); // might take long! (for buffering, etc)
                } catch (IOException e) {
                    Toast.makeText(getActivity(),"Error, couldn't play the music\n"
                            + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
        connection.start();
    }


    public void updateTrack(Track track) {
        //updateSessionMusicData(offset);
        MainActivity.tvAuthor.setText(track.getUserLogin());
        MainActivity.tvTitle.setText(track.getName());
        try {
            MainActivity.mediaPlayer.reset();
            MainActivity.mediaPlayer.setDataSource(track.getUrl());
            //mediaPlayer.pause();
            MainActivity.mediaPlayer.prepare();
        } catch(Exception e) {
        }
    }

    public void updateSessionMusicData(int offset) {
        /*int oldIndex = Session.getInstance(getApplicationContext()).getIndex();
        int size = Session.getInstance(getApplicationContext()).getTracks().size();
        int newIndex = (oldIndex + offset)%size;
        Session.getInstance(getApplicationContext()).setIndex(newIndex);
        Track newTrack = Session.getInstance(getApplicationContext()).getTracks().get(newIndex);
        Session.getInstance(getApplicationContext()).setTrack(newTrack);*/
    }



    public void trackClicked(int position){
        //IMPLEMENTAR
    }

    public void playlistClicked(int position){
        //IMPLEMENTAR
    }

    public void userClicked(int position){
        //IMPLEMENTAR
    }

    public void trackOptions(int position){
        BottomSheetDialog trackOptions = new BottomSheetDialog(mTracks.get(position));
        trackOptions.show(getChildFragmentManager(), "trackBottomSheet");
    }

    public void playlistOptions(int position){
        //IMPLEMENTAR
    }

    public void userOptions(int position){
        //IMPLEMENTAR
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
                TrackListAdapter adapter = new TrackListAdapter(this,getActivity(), mTracks);
                mRecyclerView.setAdapter(adapter);
                adapter.setItemClickedListener(new TrackListAdapter.OnItemClickedListener() {
                    @Override
                    public void onItemClicked(int position) {
                        trackClicked(position);
                    }

                    @Override
                    public void onOptionsClicked(int position) {
                        trackOptions(position);
                    }
                });
            }
            else{
                TrackListAdapter adapter = new TrackListAdapter(this,getActivity(), null);
                mRecyclerView.setAdapter(adapter);
                text = "No songs found";
                if(!searchPerformed) text = "No search done";
                Toast toast =  Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT);
                toast.show();
            }
        }
        else if(radioButton2.isChecked()){
            if(!mPlay.isEmpty()) {
                PlaylistListAdapter adapter = new PlaylistListAdapter(getActivity(), mPlay);
                mRecyclerView.setAdapter(adapter);
                adapter.setItemClickedListener(new PlaylistListAdapter.OnItemClickedListener() {
                    @Override
                    public void onItemClicked(int position) {
                        playlistClicked(position);
                    }

                    @Override
                    public void onOptionsClicked(int position) {
                        playlistOptions(position);
                    }
                });
            }
            else{
                PlaylistListAdapter adapter = new PlaylistListAdapter(getActivity(), null);
                mRecyclerView.setAdapter(adapter);
                text = "No playlists found";
                if(!searchPerformed) text = "No search done";
                Toast toast =  Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT);
                toast.show();
            }
        }
        else{
            if(!mUsers.isEmpty()) {
                UserListAdapter adapter = new UserListAdapter(getActivity(), mUsers);
                mRecyclerView.setAdapter(adapter);
                adapter.setItemClickedListener(new UserListAdapter.OnItemClickedListener() {
                    @Override
                    public void onItemClicked(int position) {
                        userClicked(position);
                    }

                    @Override
                    public void onOptionsClicked(int position) {
                        userOptions(position);
                    }
                });
            }
            else{
                UserListAdapter adapter = new UserListAdapter(null, mUsers);
                mRecyclerView.setAdapter(adapter);
                text = "No users found";
                if(!searchPerformed) text = "No search done";
                Toast toast =  Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT);
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

    @Override
    public void onButtonClicked(String text) {

    }

    @Override
    public void onTrackSelected(Track track) {
        updateTrack(track);
    }

    @Override
    public void onTrackSelected(int index) {
        currentTrack = index;
        updateTrack(mTracks.get(currentTrack));
    }

    @Override
    public void onTracksReceived(List<Track> tracks) {
        mTracks = (ArrayList) tracks;
        TrackListAdapter adapter = new TrackListAdapter(this, getActivity(), mTracks);
        mRecyclerView.setAdapter(adapter);
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
}