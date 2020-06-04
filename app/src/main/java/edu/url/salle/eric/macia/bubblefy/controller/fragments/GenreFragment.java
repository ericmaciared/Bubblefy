package edu.url.salle.eric.macia.bubblefy.controller.fragments;

import android.content.res.TypedArray;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.util.ArrayList;

import edu.url.salle.eric.macia.bubblefy.R;
import edu.url.salle.eric.macia.bubblefy.controller.activity.MainActivity;
import edu.url.salle.eric.macia.bubblefy.controller.adapters.TrackListAdapter;
import edu.url.salle.eric.macia.bubblefy.controller.callbacks.TrackListCallback;
import edu.url.salle.eric.macia.bubblefy.model.Genre;
import edu.url.salle.eric.macia.bubblefy.model.Track;
import edu.url.salle.eric.macia.bubblefy.restapi.callback.GenreCallback;
import edu.url.salle.eric.macia.bubblefy.restapi.manager.GenreManager;
import edu.url.salle.eric.macia.bubblefy.restapi.manager.TrackManager;


public class GenreFragment extends Fragment
        implements GenreCallback, TrackListCallback {

    private static final String TAG = "SearchFragment";
    private String genre_name;
    private int genre_ID;
    private ArrayList<Track> mTracks;
    private TextView tGenreName;
    private RecyclerView mRecyclerView;
    private View v;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v =  inflater.inflate(R.layout.fragment_genre, container, false);
        genre_name = getArguments().getString("genre_name");
        genre_ID = getArguments().getInt("id");

        if (MainActivity.mediaPlayer.isPlaying()){
            MainActivity.showNavigation(true);
            MainActivity.showPlayback(true);
        }
        GenreManager.getInstance(getActivity()).getTracksByGenre(genre_ID,this);

        return v;
    }

    private void initViews() throws IOException {
        tGenreName = (TextView) v.findViewById(R.id.genre_name_text);
        mRecyclerView = (RecyclerView) v.findViewById(R.id.genre_tracks);
        mRecyclerView.setVisibility(View.VISIBLE);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
        TrackListAdapter adapter = new TrackListAdapter(this, getActivity(), mTracks);
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
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

        //Genre name
        if(genre_name != null) {
            tGenreName.setText(genre_name);
        } else{
            tGenreName.setText("No name");
        }
    }

    public void trackClicked(int position){
        //MainActivity.addSongList((ArrayList<Track>) playlist.getTracks(), position);
    }

    @Override
    public void onTrackSelected(Track track) {
        MainActivity.addSongList(mTracks,
                mTracks.indexOf(track));
    }

    @Override
    public void onTrackSelected(int index) {
        MainActivity.addSongList(mTracks, index);
    }

    public void trackOptions(int position){
        BottomSheetDialog trackOptions = new BottomSheetDialog(mTracks.get(position));
        trackOptions.show(getChildFragmentManager(), "trackBottomSheet");
    }

    @Override
    public void onGenresReceive(ArrayList<Genre> genres) {

    }

    @Override
    public void onTracksByGenre(ArrayList<Track> tracks) {
        mTracks = tracks;
        try{
            initViews();
        }catch (Exception ignored){
        };
    }

    @Override
    public void onFailure(Throwable throwable) {

    }
}
