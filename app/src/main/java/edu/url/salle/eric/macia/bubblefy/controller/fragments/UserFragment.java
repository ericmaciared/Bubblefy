package edu.url.salle.eric.macia.bubblefy.controller.fragments;

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
import androidx.fragment.app.FragmentTransaction;

import com.igalata.bubblepicker.BubblePickerListener;
import com.igalata.bubblepicker.adapter.BubblePickerAdapter;
import com.igalata.bubblepicker.model.PickerItem;
import com.igalata.bubblepicker.rendering.BubblePicker;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import edu.url.salle.eric.macia.bubblefy.R;
import edu.url.salle.eric.macia.bubblefy.model.Confirmation;
import edu.url.salle.eric.macia.bubblefy.model.Playlist;
import edu.url.salle.eric.macia.bubblefy.model.Track;
import edu.url.salle.eric.macia.bubblefy.model.User;
import edu.url.salle.eric.macia.bubblefy.model.UserToken;
import edu.url.salle.eric.macia.bubblefy.restapi.callback.TrackCallback;
import edu.url.salle.eric.macia.bubblefy.restapi.callback.UserCallback;
import edu.url.salle.eric.macia.bubblefy.restapi.manager.UserManager;
import edu.url.salle.eric.macia.bubblefy.utils.Session;

public class UserFragment extends Fragment implements TrackCallback, UserCallback {

    //BubblePicker
    private BubblePicker bubblePicker;
    private ArrayList<Playlist> mPlaylists;
    private String[] name = {
            "Colores",
            "YHLQMDLG",
            "Blue",
            "Chill",
            "Party"
    };

    //Profile
    private ImageView ivProfileImage;
    private TextView tUsername;
    private TextView tFollowers;
    private TextView tFollowing;
    private ImageButton ibtnConfig;
    private ImageButton ibtnUpload;
    private ImageButton ibtnNewPlaylist;

    private User mUser;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v =  inflater.inflate(R.layout.fragment_profile, container, false);
        String login = getArguments().getString("login");
        getUserData(login);

        initUserListened(v);
        initBubblePicker(v);

        try {
            initViews(v);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return v;
    }

    private void initUserListened(View v) {
        //TrackManager.getInstance(getActivity()).getUserLikedTracks(this);
        // Will need to implement the most listened playlists / genres
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
        //User Information
        ivProfileImage = (ImageView) v.findViewById(R.id.profile_image);
        tFollowers = (TextView) v.findViewById(R.id.num_followers_text);
        tFollowing = (TextView) v.findViewById(R.id.num_following_text);
        tUsername = (TextView) v.findViewById(R.id.username_text);

        //Upload, Config and New Playlist Buttons
        ibtnConfig = (ImageButton) v.findViewById(R.id.config_button);
        ibtnUpload = (ImageButton) v.findViewById(R.id.upload_button);
        ibtnNewPlaylist = (ImageButton) v.findViewById(R.id.new_playlist_button);

        ibtnConfig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                return;
            }
        });
        ibtnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, new UploadFragment());
                transaction.commit();
            }
        });
        ibtnNewPlaylist.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, new NewPlaylistFragment());
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        setUserFields();
    }

    private void getUserData(String login){
        UserManager.getInstance(getActivity().getApplicationContext())
                .getUserData(login, this);
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


    @Override
    public void onLoginSuccess(UserToken userToken) {

    }

    @Override
    public void onLoginFailure(Throwable throwable) {

    }

    @Override
    public void onRegisterSuccess() {

    }

    @Override
    public void onRegisterFailure(Throwable throwable) {

    }

    @Override
    public void onUserInfoReceived(User userData) {
        mUser = userData;
    }

    private void setUserFields(){
        //Profile
        if(mUser != null) {
            //Profile Picture
            if(mUser.getImageUrl() == null) {
                Picasso.get().load("https://image.flaticon.com/icons/png/512/64/64572.png").into(ivProfileImage);
            }
            else{
                Picasso.get()
                        .load(mUser.getImageUrl())
                        .into(ivProfileImage);
            }

            //Username
            if(mUser.getLogin() != null) {
                tUsername.setText(mUser.getLogin());
            } else{
                tUsername.setText("No name");
            }

            //Followers and Following
            if(mUser.getFollowers() != null) {
                tFollowers.setText(String.valueOf(mUser.getFollowers()));
            }
            else{
                tFollowers.setText("0");
            }
            if(mUser.getFollowing() != null) {
                tFollowers.setText(String.valueOf(mUser.getFollowing()));
            } else {
                tFollowing.setText("0");
            }

        }
        else{
            String text = "No picture URL found";
            Toast toast =  Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT);
            toast.show();
        }

    }
}
