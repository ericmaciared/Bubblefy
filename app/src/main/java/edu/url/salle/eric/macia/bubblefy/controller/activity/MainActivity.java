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
                        UserFragment fragUser = new UserFragment();
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
        //updateSessionMusicData(offset);
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
        if (queue.size() == 0) return;
        if (queue.size() == 1){
            updateTrack(queue.get(0));
            return;
        }
        if (repeat){
            updateTrack(queue.get(0));
        } else if (random){
            int aux = ran.nextInt(queue.size());
            queue.add(0, queue.get(aux));
            queue.remove(1);
            queue.remove(aux+1);
        } else {
            queue.remove(0);
            updateTrack(queue.get(0));
        }
    }

    public static void previousTrack() {
        updateTrack(queue.get(0));
    }

    public static void addTrackToQueue(Track track){
        queue.add(track);
    }

    @Override
    public void onButtonClicked(String text) {

    }
}
