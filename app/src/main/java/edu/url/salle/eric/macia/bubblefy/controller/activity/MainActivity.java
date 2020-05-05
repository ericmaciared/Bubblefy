package edu.url.salle.eric.macia.bubblefy.controller.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.media.Image;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.w3c.dom.Text;

import java.util.ArrayList;

import edu.url.salle.eric.macia.bubblefy.R;
import edu.url.salle.eric.macia.bubblefy.controller.fragments.BottomSheetDialog;
import edu.url.salle.eric.macia.bubblefy.controller.fragments.HomeFragment;
import edu.url.salle.eric.macia.bubblefy.controller.fragments.PlaybackFragment;
import edu.url.salle.eric.macia.bubblefy.controller.fragments.ProfileFragment;
import edu.url.salle.eric.macia.bubblefy.controller.fragments.SearchFragment;
import edu.url.salle.eric.macia.bubblefy.controller.fragments.UploadFragment;
import edu.url.salle.eric.macia.bubblefy.model.Track;

public class MainActivity extends FragmentActivity implements BottomSheetDialog.BottomSheetListener {
    private static final String PLAY_VIEW = "PlayIcon";
    private static final String STOP_VIEW = "StopIcon";

    public static BottomNavigationView bottomNavigationView;

    public static ArrayList<Track> queue;
    public static int currentTrack = 0;
    public static MediaPlayer mediaPlayer;

    public static LinearLayout playbackLayout;
    public static LinearLayout playbackButton;
    public static ImageButton btnPlayStop;
    public static TextView tvTitle;
    public static TextView tvAuthor;
    public static ImageView ivPhoto;

    private static int activity = 0;
    Fragment selectedFragment = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        initPlayer();
        setInitialFragment();

    }

    private void initPlayer() {
        tvTitle = (TextView) findViewById(R.id.track_title);
        tvAuthor = (TextView) findViewById(R.id.track_author);
        ivPhoto = (ImageView) findViewById(R.id.track_img);

        playbackLayout = (LinearLayout) findViewById(R.id.playback);
        playbackLayout.setVisibility(View.GONE);

        playbackButton = (LinearLayout) findViewById(R.id.info_section);
        playbackButton.setOnClickListener(v -> {
            loadPlaybackFragment();
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

    private void initViews() {
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setVisibility(View.VISIBLE);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()){
                    case R.id.nav_home:
                        activity = 0;
                        selectedFragment = new HomeFragment();
                        break;
                    case R.id.nav_search:
                        activity  = 1;
                        selectedFragment = new SearchFragment();
                        break;
                    case R.id.nav_profile:
                        activity = 2;
                        selectedFragment = new ProfileFragment();
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
        playbackLayout.setVisibility(View.GONE);
        bottomNavigationView.setVisibility(View.GONE);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                new PlaybackFragment(activity)).commit();
    }

    public static void playAudio() {
        playbackLayout.setVisibility(View.VISIBLE);
        mediaPlayer.start();
        btnPlayStop.setImageResource(R.drawable.ic_pause);
        btnPlayStop.setTag(STOP_VIEW);
    }

    public static void pauseAudio() {
        mediaPlayer.pause();
        btnPlayStop.setImageResource(R.drawable.ic_play);
        btnPlayStop.setTag(PLAY_VIEW);
    }

    @Override
    public void onButtonClicked(String text) {

    }
}


