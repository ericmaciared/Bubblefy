package edu.url.salle.eric.macia.bubblefy.controller.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

import edu.url.salle.eric.macia.bubblefy.R;
import edu.url.salle.eric.macia.bubblefy.controller.activity.MainActivity;
import edu.url.salle.eric.macia.bubblefy.model.Track;

public class PlaybackFragment extends Fragment{
    private static final String PLAY_VIEW = "PlayIcon";
    private static final String STOP_VIEW = "StopIcon";

    private ArrayList<Track> queue;

    private ImageView ivTrackImg;
    private TextView tvTrackTitle;
    private TextView tvTrackAuthor;

    private SeekBar seekBar;
    private Handler mHandler;
    private Runnable mRunnable;

    private ImageButton ibArrowDown;
    private ImageButton ibPlayPause;
    private ImageButton ibRandom;
    private ImageButton ibRepeat;
    private ImageButton ibNextSong;
    private ImageButton ibPreviousSong;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v =  inflater.inflate(R.layout.fragment_playback, container, false);

        MainActivity.showNavigation(false);
        MainActivity.showPlayback(false);

        initViews(v);

        return v;
    }

    private void initViews(View v) {
        this.ivTrackImg = (ImageView) v.findViewById(R.id.track_img);
        this.tvTrackTitle = (TextView) v.findViewById(R.id.track_title);
        this.tvTrackAuthor = (TextView) v.findViewById(R.id.track_author);

        //ivTrackImg.setImageDrawable(MainActivity.ivPhoto.getDrawable());
        tvTrackTitle.setText(MainActivity.tvTitle.getText());
        tvTrackAuthor.setText(MainActivity.tvAuthor.getText());

        this.seekBar = (SeekBar) v.findViewById(R.id.music_seek_bar);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser){
                    MainActivity.mediaPlayer.seekTo(progress);
                    updateSeekBar();
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        mHandler = new Handler();

        updateSeekBar();

        this.ibArrowDown = (ImageButton) v.findViewById(R.id.arrow_down);
        ibArrowDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.showNavigation(true);
                MainActivity.showPlayback(true);
                if (ibPlayPause.getTag().equals(PLAY_VIEW)){
                    MainActivity.pauseAudio();
                }
                else MainActivity.playAudio();
                getParentFragmentManager().popBackStackImmediate();
            }
        });


        this.ibPlayPause = (ImageButton) v.findViewById(R.id.playback_play_pause);
        ibPlayPause.setTag(MainActivity.btnPlayStop.getTag());
        if (ibPlayPause.getTag().equals(PLAY_VIEW)) {
            ibPlayPause.setImageResource(R.drawable.ic_play3);
        } else {
            ibPlayPause.setImageResource(R.drawable.ic_pause2);
        }
        ibPlayPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ibPlayPause.getTag().equals(PLAY_VIEW)) {
                    MainActivity.mediaPlayer.start();
                    ibPlayPause.setTag(STOP_VIEW);
                    ibPlayPause.setImageResource(R.drawable.ic_pause2);
                } else {
                    MainActivity.mediaPlayer.pause();
                    ibPlayPause.setTag(PLAY_VIEW);
                    ibPlayPause.setImageResource(R.drawable.ic_play3);
                }
            }
        });

        this.ibNextSong = (ImageButton) v.findViewById(R.id.playback_forward);
        ibNextSong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.nextTrack();
                updateTrack();
            }
        });

        this.ibPreviousSong = (ImageButton) v.findViewById(R.id.playback_backward);
        ibPreviousSong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.previousTrack();
                updateTrack();
            }
        });

        this.ibRandom = (ImageButton) v.findViewById(R.id.playback_random);
        ibRandom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.random = !MainActivity.random;
                Toast toast =  Toast.makeText(getActivity(), "Random", Toast.LENGTH_SHORT);
                toast.show();
            }
        });

        this.ibRepeat = (ImageButton) v.findViewById(R.id.playback_repeat);
        ibRepeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.repeat = !MainActivity.repeat;
            }
        });
    }

    private void updateTrack() {
        this.tvTrackTitle.setText(MainActivity.currentSong.getName());
        this.tvTrackAuthor.setText(MainActivity.currentSong.getUserLogin());
    }

    //SEEKBAR
    public void updateSeekBar() {
        seekBar.setMax(MainActivity.currentSong.getDuration()/1000);
        seekBar.setProgress(MainActivity.mediaPlayer.getCurrentPosition());

        if(MainActivity.mediaPlayer.isPlaying()) {
            mRunnable = new Runnable() {
                @Override
                public void run() {
                    updateSeekBar();
                }
            };
            mHandler.postDelayed(mRunnable, 1000);
        }
    }
}
