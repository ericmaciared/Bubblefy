package edu.url.salle.eric.macia.bubblefy.controller.fragments;

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

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;

import edu.url.salle.eric.macia.bubblefy.R;
import edu.url.salle.eric.macia.bubblefy.controller.activity.MainActivity;
import edu.url.salle.eric.macia.bubblefy.controller.adapters.TrackListAdapter;
import edu.url.salle.eric.macia.bubblefy.controller.callbacks.TrackListCallback;
import edu.url.salle.eric.macia.bubblefy.model.Playlist;
import edu.url.salle.eric.macia.bubblefy.model.Track;
import edu.url.salle.eric.macia.bubblefy.restapi.callback.PlaylistCallback;
import edu.url.salle.eric.macia.bubblefy.restapi.manager.PlaylistManager;
import edu.url.salle.eric.macia.bubblefy.restapi.manager.UserManager;

public class PlaylistFragment extends Fragment implements TrackListCallback, PlaylistCallback {
    private Playlist playlist;
    private ImageView ivProfileImage;
    private TextView tPlaylistName;
    private Button btnFollow;
    private RecyclerView mRecyclerView;

    public PlaylistFragment(Playlist playlist) {
        this.playlist = playlist;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v =  inflater.inflate(R.layout.fragment_playlist, container, false);

        if (MainActivity.mediaPlayer.isPlaying()){
            MainActivity.showNavigation(true);
            MainActivity.showPlayback(true);
        }

        ivProfileImage = (ImageView) v.findViewById(R.id.playlist_image);
        tPlaylistName = (TextView) v.findViewById(R.id.playlist_name_text);
        btnFollow = (Button) v.findViewById(R.id.follow_button);
        mRecyclerView = (RecyclerView) v.findViewById(R.id.tracks);

        mRecyclerView.setVisibility(View.VISIBLE);

        if(!playlist.getTracks().isEmpty()) {

            LinearLayoutManager manager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
            TrackListAdapter adapter = new TrackListAdapter(this, getActivity(), (ArrayList<Track>) playlist.getTracks());
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
        }
        else{
            Toast toast =  Toast.makeText(getActivity(), "No songs found", Toast.LENGTH_SHORT);
            toast.show();
        }

        btnFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                followFunction();
            }
        });


        try {
            initViews();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return v;
    }

    private void initViews() throws IOException {

        //Profile Picture
        if(playlist.getThumbnail() == null) {
            Picasso.get().load("https://image.flaticon.com/icons/png/512/2404/2404593.png").into(ivProfileImage);
        }
        else{
            Picasso.get()
                    .load(playlist.getThumbnail())
                    .into(ivProfileImage);
        }

        //Playlist name
        if(playlist.getName() != null) {
            tPlaylistName.setText(playlist.getName());
        } else{
            tPlaylistName.setText("No name");
        }


        if(playlist.isFollowed()){
            btnFollow.setText("UNFOLLOW");
        }else{
            btnFollow.setText("FOLLOW");
        }

    }

    public void trackClicked(int position){
        //MainActivity.addSongList((ArrayList<Track>) playlist.getTracks(), position);
    }

    @Override
    public void onTrackSelected(Track track) {
        MainActivity.addSongList((ArrayList<Track>) playlist.getTracks(),
                playlist.getTracks().indexOf(track));
    }

    @Override
    public void onTrackSelected(int index) {
        MainActivity.addSongList((ArrayList<Track>) playlist.getTracks(), index);
    }

    public void followFunction(){
        PlaylistManager.getInstance(getActivity()).followPlaylist(playlist.getId(), this);
    }

    public void trackOptions(int position){
        BottomSheetDialog trackOptions = new BottomSheetDialog(playlist.getTracks().get(position));
        trackOptions.show(getChildFragmentManager(), "trackBottomSheet");
    }

    @Override
    public void onPlaylistCreated(Playlist playlist) {

    }

    @Override
    public void onPlaylistFailure(Throwable throwable) {

    }

    @Override
    public void onUserPlaylistReceived(ArrayList<Playlist> playlist) {

    }

    @Override
    public void onUserFollowingPlaylistReceived(ArrayList<Playlist> playlist) {

    }

    @Override
    public void onPlaylistUpdated(Playlist playlist) {

    }

    @Override
    public void onNoPlaylist(Throwable throwable) {

    }

    @Override
    public void onFollowSuccess() {
        if(playlist.isFollowed()){
            playlist.setFollowed(false);
            btnFollow.setText("FOLLOW");
        }else{
            playlist.setFollowed(true);
            btnFollow.setText("UNFOLLOW");
        }
    }

    @Override
    public void onFailure(Throwable throwable) {

    }
}
