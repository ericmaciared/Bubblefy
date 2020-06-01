package edu.url.salle.eric.macia.bubblefy.controller.fragments;

import android.nfc.Tag;
import android.os.Bundle;
import android.os.Handler;
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
import edu.url.salle.eric.macia.bubblefy.model.Follow;
import edu.url.salle.eric.macia.bubblefy.model.Playlist;
import edu.url.salle.eric.macia.bubblefy.model.Track;
import edu.url.salle.eric.macia.bubblefy.model.User;
import edu.url.salle.eric.macia.bubblefy.model.UserToken;
import edu.url.salle.eric.macia.bubblefy.restapi.callback.TrackCallback;
import edu.url.salle.eric.macia.bubblefy.restapi.callback.UserCallback;
import edu.url.salle.eric.macia.bubblefy.restapi.manager.UserManager;

public class UserFragment extends Fragment implements TrackCallback, UserCallback {

    private static final String TAG = "UserFragment";

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
    private TextView tvUsername;
    private TextView tvFollowers;
    private TextView tvFollowing;
    private Button btnFollow;

    Handler handler = new Handler();
    Runnable refresh;

    private User mUser;

    private int followers;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v =  inflater.inflate(R.layout.fragment_user, container, false);
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
        tvFollowers = (TextView) v.findViewById(R.id.num_followers_text);
        tvFollowing = (TextView) v.findViewById(R.id.num_following_text);
        tvUsername = (TextView) v.findViewById(R.id.username_text);
        btnFollow = (Button) v.findViewById(R.id.follow_button);

        btnFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                UserManager.getInstance(getActivity().getApplicationContext())
                        .followUser(mUser.getLogin(), UserFragment.this);
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
        followers = mUser.getFollowers();
        setUserFields();
    }

    @Override
    public void onCheckFollowReceived(Follow follow) {
        if (follow.getFollowed()){
            btnFollow.setText("Following");
        } else {
            btnFollow.setText("Follow");
        }
    }

    @Override
    public void onUserFollowed(Follow follow) {
        if (follow.getFollowed()){
            btnFollow.setText("Following");
            followers++;
            tvFollowers.setText(String.valueOf(followers));
        } else {
            btnFollow.setText("Follow");
            followers--;
            tvFollowers.setText(String.valueOf(followers));
        }
    }

    private void setUserFields(){
        //Profile
        if(mUser != null) {
            //Profile Picture
            if(mUser.getImageUrl() == null || mUser.getImageUrl() == "") {
                Picasso.get().load("https://image.flaticon.com/icons/png/512/64/64572.png").into(ivProfileImage);
            }
            else{
                try {
                    Picasso.get()
                            .load(mUser.getImageUrl())
                            .into(ivProfileImage);
                } catch (IllegalArgumentException ia){
                    String text = "Picture not accessible";
                    Toast toast =  Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT);
                    toast.show();
                }

            }

            //Username
            if(mUser.getLogin() != null) {
                tvUsername.setText(mUser.getLogin());
            } else{
                tvUsername.setText("No name");
            }

            //Followers and Following
            tvFollowers.setText(String.valueOf(followers));
            tvFollowing.setText(String.valueOf(mUser.getFollowing()));

            //Following button
            UserManager.getInstance(getActivity().getApplicationContext()).checkUserFollowed(mUser.getLogin(), this);

        }
    }
}
