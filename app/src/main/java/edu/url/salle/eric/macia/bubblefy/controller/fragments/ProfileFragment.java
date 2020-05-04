package edu.url.salle.eric.macia.bubblefy.controller.fragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.igalata.bubblepicker.BubblePickerListener;
import com.igalata.bubblepicker.adapter.BubblePickerAdapter;
import com.igalata.bubblepicker.model.PickerItem;
import com.igalata.bubblepicker.rendering.BubblePicker;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import edu.url.salle.eric.macia.bubblefy.R;
import edu.url.salle.eric.macia.bubblefy.controller.adapters.BubbleTrackListAdapter;
import edu.url.salle.eric.macia.bubblefy.model.Confirmation;
import edu.url.salle.eric.macia.bubblefy.model.Playlist;
import edu.url.salle.eric.macia.bubblefy.model.Track;
import edu.url.salle.eric.macia.bubblefy.restapi.callback.TrackCallback;
import edu.url.salle.eric.macia.bubblefy.restapi.manager.TrackManager;
import edu.url.salle.eric.macia.bubblefy.utils.Session;

public class ProfileFragment extends Fragment implements TrackCallback{

    //BubblePicker
    private BubblePicker bubblePicker;
    private RecyclerView recyclerView;
    private ArrayList<Playlist> mPlaylists;
    private String[] name = {
            "Colores",
            "YHLQMDLG",
            "Blue",
            "Chill",
            "Party"
    };

    //NavBar
    private ImageButton ibtnHome;
    private ImageButton ibtnSearch;
    private ImageButton ibtnProfile;

    //Profile
    private ImageView ivProfileImage;
    private TextView tFollowers;
    private TextView tFollowing;
    private ImageButton ibtnConfig;
    private ImageButton ibtnUpload;



    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v =  inflater.inflate(R.layout.fragment_profile, container, false);

        try {
            initViews(v);
        } catch (IOException e) {
            e.printStackTrace();
        }

        initListenAgain(v);
        initBubblePicker(v);
        return v;
    }

    private void initListenAgain(View v) {
        recyclerView = (RecyclerView) v.findViewById(R.id.recyclerView);

        LinearLayoutManager manager = new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL,
                false);
        BubbleTrackListAdapter adapter = new BubbleTrackListAdapter(getActivity(), null);

        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);

        TrackManager.getInstance(getActivity()).getUserLikedTracks(this);
    }

    private void initBubblePicker(View v) {
        bubblePicker = (BubblePicker) v.findViewById(R.id.picker);

        bubblePicker.setAdapter(new BubblePickerAdapter() {
            @Override
            public int getTotalCount() {
                return name.length;
            }

            @NotNull
            @Override
            public PickerItem getItem(int i) {
                PickerItem item = new PickerItem();
                item.setTitle(name[i]);
                return item;
            }

        });

        bubblePicker.setListener(new BubblePickerListener() {
            @Override
            public void onBubbleSelected(@NotNull PickerItem pickerItem) {
                if (pickerItem.isSelected()){
                    pickerItem.setTitle("TEST");
                }
            }

            @Override
            public void onBubbleDeselected(@NotNull PickerItem pickerItem) {

            }
        });

        bubblePicker.setCenterImmediately(true);
    }

    @Override
    public void onResume() {
        super.onResume();
        bubblePicker.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        bubblePicker.onPause();
    }

    private void initViews(View v) throws IOException {

        //Profile Picture
        ivProfileImage = (ImageView) v.findViewById(R.id.profile_image);
        if(Session.sSession.getUser().getImageUrl() != null){
            URL url = new URL(Session.sSession.getUser().getImageUrl());
            Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            ivProfileImage.setImageBitmap(bmp);
        }
        else{
            String text = "No picture URL found";
            Toast toast =  Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT);
            toast.show();
        }

        //Followers and Following
        tFollowers = (TextView) v.findViewById(R.id.num_followers_text);
        tFollowing = (TextView) v.findViewById(R.id.num_following_text);
        tFollowers.setText(Session.sSession.getUser().getFollowers());
        tFollowing.setText(Session.sSession.getUser().getFollowing());

        //Upload and Config Buttons
        ibtnConfig = (ImageButton) v.findViewById(R.id.config_button);
        ibtnUpload = (ImageButton) v.findViewById(R.id.upload_button);
        ibtnConfig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                return;
            }
        });
        ibtnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                return;
            }
        });
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
        ArrayList<Track> mTracks = new ArrayList<Track>();
        mTracks = (ArrayList<Track>) tracks;

        BubbleTrackListAdapter adapter = new BubbleTrackListAdapter(getActivity(), mTracks);
        recyclerView.setAdapter(adapter);
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

    @Override
    public void onFailure(Throwable throwable) {

    }


}
