package edu.url.salle.eric.macia.bubblefy.controller.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.ContentView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.List;

import edu.url.salle.eric.macia.bubblefy.R;
import edu.url.salle.eric.macia.bubblefy.model.Confirmation;
import edu.url.salle.eric.macia.bubblefy.model.Track;
import edu.url.salle.eric.macia.bubblefy.restapi.callback.TrackCallback;
import edu.url.salle.eric.macia.bubblefy.restapi.manager.TrackManager;

public class BottomSheetDialog extends BottomSheetDialogFragment implements TrackCallback {
    private BottomSheetListener mListener;
    private Track track;


    public BottomSheetDialog(Track track) {
        this.track = track;
    }

    TextView tvTitle;
    Button buttonLike;
    Button buttonAddQueue;
    Button buttonShare;
    Button buttonAddPlaylist;
    Button buttonShuffle;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.track_option_sheet, container, false);
        tvTitle = v.findViewById(R.id.title);
        tvTitle.setText(track.getName());
        buttonLike = v.findViewById(R.id.like_button);
        buttonAddQueue = v.findViewById(R.id.add_queue);
        buttonShare= v.findViewById(R.id.share);
        buttonAddPlaylist = v.findViewById(R.id.add_to_playlist);
        buttonShuffle = v.findViewById(R.id.random_playback);
        TrackManager.getInstance(getContext()).getTrackLike(track.getId(), this);

        buttonLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setLike(track.getId());
            }
        });


        buttonAddQueue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //IMPLEMENTAR
            }
        });

        buttonShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //IMPLEMENTAR
            }
        });

        buttonAddPlaylist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //IMPLEMENTAR
            }
        });

        buttonShuffle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //IMPLEMENTAR
            }
        });

        return v;
    }

    public void setLike(int id){
        TrackManager.getInstance(getContext()).setTrackLike(track.getId(), this);
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
        if (confirmation.isLiked()){
            buttonLike.setText("Dislike");
        }
        else{
            buttonLike.setText("Like");
        }
    }

    @Override
    public void onLikeOperationFailure(Throwable throwable) {

    }

    @Override
    public void onReceiveLikeSuccess(Confirmation confirmation) {
        if (confirmation.isLiked()){
            buttonLike.setText("Dislike");
        }
        else{
        buttonLike.setText("Like");
        }
    }

    @Override
    public void onReceiveLikeFailure(Throwable throwable) {

    }

    @Override
    public void onFailure(Throwable throwable) {

    }

    public interface BottomSheetListener{
        void onButtonClicked(String text);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            mListener = (BottomSheetListener) context;
        }
        catch(ClassCastException e){
            throw new ClassCastException(context.toString() + "must implement BottomSheetListener");
        }
    }
}
