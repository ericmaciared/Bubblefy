package edu.url.salle.eric.macia.bubblefy.controller.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import edu.url.salle.eric.macia.bubblefy.R;
import edu.url.salle.eric.macia.bubblefy.controller.activity.MainActivity;

public class PlaybackFragment extends Fragment {
    private ImageView ivTrackImg;
    private TextView tvTrackTitle;
    private TextView tvTrackAuthor;

    private ImageButton ibArrowDown;
    private ImageButton ibPlayPause;

    public PlaybackFragment(int activity) {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v =  inflater.inflate(R.layout.fragment_playback, container, false);

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

        this.ibArrowDown = (ImageButton) v.findViewById(R.id.arrow_down);
        ibArrowDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.playbackLayout.setVisibility(View.VISIBLE);
                MainActivity.bottomNavigationView.setVisibility(View.VISIBLE);
                getActivity().getFragmentManager().popBackStack();
            }
        });


    }


}
