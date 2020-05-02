package edu.url.salle.eric.macia.bubblefy.controller.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

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
import edu.url.salle.eric.macia.bubblefy.controller.fragments.ProfileFragment;
import edu.url.salle.eric.macia.bubblefy.controller.fragments.SearchFragment;
import edu.url.salle.eric.macia.bubblefy.controller.fragments.UploadFragment;
import edu.url.salle.eric.macia.bubblefy.model.Track;

public class MainActivity extends AppCompatActivity implements BottomSheetDialog.BottomSheetListener {
    private static final String PLAY_VIEW = "PlayIcon";
    private static final String STOP_VIEW = "StopIcon";

    public static ArrayList<Track> queue;
    public static int currentTrack = 0;
    public static MediaPlayer mediaPlayer;

    public static LinearLayout playbackLayout;
    public static ImageButton btnPlayStop;
    public static TextView tvTitle;
    public static TextView tvAuthor;
    public static ImageView ivPhoto;

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

    private void initViews() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;

                switch (item.getItemId()){
                    case R.id.nav_home:
                        selectedFragment = new HomeFragment();
                        break;
                    case R.id.nav_search:
                        selectedFragment = new SearchFragment();
                        break;
                    case R.id.nav_profile:
                        selectedFragment = new UploadFragment();
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

    @Override
    public void onButtonClicked(String text) {

    }
}


