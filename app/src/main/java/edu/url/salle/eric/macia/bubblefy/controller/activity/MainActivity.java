package edu.url.salle.eric.macia.bubblefy.controller.activity;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.Random;

import edu.url.salle.eric.macia.bubblefy.R;
import edu.url.salle.eric.macia.bubblefy.controller.fragments.BottomSheetDialog;
import edu.url.salle.eric.macia.bubblefy.controller.fragments.HomeFragment;
import edu.url.salle.eric.macia.bubblefy.controller.fragments.PlaybackFragment;
import edu.url.salle.eric.macia.bubblefy.controller.fragments.ProfileFragment;
import edu.url.salle.eric.macia.bubblefy.controller.fragments.SearchFragment;
import edu.url.salle.eric.macia.bubblefy.controller.fragments.UserFragment;
import edu.url.salle.eric.macia.bubblefy.model.Track;
import edu.url.salle.eric.macia.bubblefy.utils.Session;

public class MainActivity extends FragmentActivity implements BottomSheetDialog.BottomSheetListener {
    private static final String PLAY_VIEW = "PlayIcon";
    private static final String STOP_VIEW = "StopIcon";

    public static BottomNavigationView bottomNavigationView;

    public static Track currentSong;
    public static ArrayList<Track> queue;
    public static ArrayList<Track> songs;
    public static int currentSongIndex;
    public static MediaPlayer mediaPlayer;
    public static boolean random;
    public static boolean repeat;

    public static LinearLayout playbackLayout;
    public static LinearLayout playbackButton;
    public static ImageButton btnPlayStop;
    public static TextView tvTitle;
    public static TextView tvAuthor;
    public static ImageView ivPhoto;

    public static boolean showPlaybackFragment;

    Fragment selectedFragment = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        initPlayer();
        setInitialFragment();
    }

    //VIEWS
    private void initViews() {
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setVisibility(View.VISIBLE);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()){
                    case R.id.nav_home:
                        selectedFragment = new HomeFragment();
                        break;
                    case R.id.nav_search:
                        selectedFragment = new SearchFragment();
                        break;
                    case R.id.nav_profile:
                        Bundle bundle = new Bundle();
                        bundle.putString("login", Session.getInstance(getApplicationContext()).getUser().getLogin());
                        ProfileFragment fragUser = new ProfileFragment();
                        fragUser.setArguments(bundle);
                        selectedFragment = fragUser;
                        break;
                }

                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        selectedFragment).commit();

                return true;
            }
        });
    }

    private void setInitialFragment() {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                new HomeFragment()).commit();
    }

    private void loadPlaybackFragment() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, new PlaybackFragment());
        if (!showPlaybackFragment) transaction.addToBackStack(null);
        transaction.commit();
        showPlaybackFragment = true;
    }

    public static void showNavigation(boolean show) {
        if (show) bottomNavigationView.setVisibility(View.VISIBLE);
        else bottomNavigationView.setVisibility(View.GONE);
    }

    public static void showPlayback(boolean show) {
        if (show) playbackLayout.setVisibility(View.VISIBLE);
        else playbackLayout.setVisibility(View.GONE);
    }


    //MUSIC
    private void initPlayer() {
        random = false;
        repeat = false;
        queue = new ArrayList<Track>();
        songs = new ArrayList<Track>();
        currentSongIndex = -1;

        tvTitle = (TextView) findViewById(R.id.track_title);
        tvAuthor = (TextView) findViewById(R.id.track_author);
        ivPhoto = (ImageView) findViewById(R.id.track_img);

        playbackLayout = (LinearLayout) findViewById(R.id.playback);
        playbackLayout.setVisibility(View.GONE);

        playbackButton = (LinearLayout) findViewById(R.id.info_section);
        playbackButton.setOnClickListener(v -> {
            loadPlaybackFragment();
        });

        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                playAudio();
                if (showPlaybackFragment) loadPlaybackFragment();
                int audioSessionId = mediaPlayer.getAudioSessionId();
            }
        });
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                nextTrack();
            }
        });

        btnPlayStop = (ImageButton) findViewById(R.id.play_pause);
        btnPlayStop.setTag(PLAY_VIEW);
        btnPlayStop.setOnClickListener(v -> {
            if (btnPlayStop.getTag().equals(PLAY_VIEW)) {
                playAudio();
            } else {
                pauseAudio();
            }
        });
    }

    public static void playAudio() {
        if (!showPlaybackFragment) playbackLayout.setVisibility(View.VISIBLE);
        mediaPlayer.start();
        btnPlayStop.setImageResource(R.drawable.ic_pause);
        btnPlayStop.setTag(STOP_VIEW);
    }

    public static void pauseAudio() {
        mediaPlayer.pause();
        btnPlayStop.setImageResource(R.drawable.ic_play);
        btnPlayStop.setTag(PLAY_VIEW);
    }

    public static void updateTrack(Track track){
        MainActivity.tvAuthor.setText(track.getUserLogin());
        MainActivity.tvTitle.setText(track.getName());
        //TODO: Add image

        MainActivity.currentSong = track;
        try {
            mediaPlayer.stop();
            mediaPlayer.reset();
            mediaPlayer.setDataSource(track.getUrl());
            mediaPlayer.prepareAsync();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static void nextTrack() {
        Random ran = new Random();

        if (repeat) {
            MainActivity.updateTrack(currentSong);
        }
        else{
            //If there are songs in queue we play them first in order.
            if (queue.size() > 0) {
                MainActivity.updateTrack(queue.get(0));
                queue.remove(0);
            }
            //If there are no songs in the queue we will check the size of the song list and random
            else{
                if (random){
                    MainActivity.currentSongIndex = ran.nextInt(songs.size());
                }
                else {
                    if (MainActivity.currentSongIndex + 1 == MainActivity.songs.size()){
                        MainActivity.currentSongIndex = 0;
                    }
                    else{
                        MainActivity.currentSongIndex += 1;
                    }
                }
                MainActivity.updateTrack(MainActivity.songs.get(currentSongIndex));
            }
        }
    }

    public static void previousTrack() { MainActivity.updateTrack(currentSong);}

    public static void addTrackToQueue(Track track){
        MainActivity.queue.add(track);
    }

    public static void addSongList(ArrayList<Track> list, int currentSongIndex){
        MainActivity.songs = list;
        MainActivity.currentSongIndex = currentSongIndex;
        updateTrack(MainActivity.songs.get(MainActivity.currentSongIndex));
    }

    public static void addSongList(Track track){
        ArrayList<Track> aux = new ArrayList<Track>();
        aux.add(track);
        MainActivity.songs = aux;
        MainActivity.songs.add(track);
        MainActivity.currentSongIndex = 0;
        updateTrack(MainActivity.songs.get(MainActivity.currentSongIndex));
    }

    @Override
    public void onButtonClicked(String text) {

    }
}
