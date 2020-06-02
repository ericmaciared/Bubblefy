package edu.url.salle.eric.macia.bubblefy.controller.fragments;

import android.content.res.TypedArray;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
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
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.igalata.bubblepicker.BubblePickerListener;
import com.igalata.bubblepicker.adapter.BubblePickerAdapter;
import com.igalata.bubblepicker.model.BubbleGradient;
import com.igalata.bubblepicker.model.PickerItem;
import com.igalata.bubblepicker.rendering.BubblePicker;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import edu.url.salle.eric.macia.bubblefy.R;
import edu.url.salle.eric.macia.bubblefy.controller.adapters.BubbleTrackListAdapter;
import edu.url.salle.eric.macia.bubblefy.model.Confirmation;
import edu.url.salle.eric.macia.bubblefy.model.Follow;
import edu.url.salle.eric.macia.bubblefy.model.Playlist;
import edu.url.salle.eric.macia.bubblefy.model.Track;
import edu.url.salle.eric.macia.bubblefy.model.User;
import edu.url.salle.eric.macia.bubblefy.model.UserToken;
import edu.url.salle.eric.macia.bubblefy.restapi.callback.PlaylistCallback;
import edu.url.salle.eric.macia.bubblefy.restapi.callback.TrackCallback;
import edu.url.salle.eric.macia.bubblefy.restapi.callback.UserCallback;
import edu.url.salle.eric.macia.bubblefy.restapi.manager.ImageManager;
import edu.url.salle.eric.macia.bubblefy.restapi.manager.PlaylistManager;
import edu.url.salle.eric.macia.bubblefy.restapi.manager.TrackManager;
import edu.url.salle.eric.macia.bubblefy.restapi.manager.UserManager;

public class UserFragment extends Fragment implements PlaylistCallback, UserCallback {

    private static final String TAG = "UserFragment";

    //BubblePicker
    private BubblePicker bubblePicker;
    private ArrayList<Playlist> mPlaylist;

    //Profile
    private ImageView ivProfileImage;
    private TextView tvUsername;
    private TextView tvFollowers;
    private TextView tvFollowing;
    private Button btnFollow;

    private User mUser;
    private TypedArray colors;
    private ArrayList<Track> mTracks;
    private int followers;
    private ImageManager imageManager;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v =  inflater.inflate(R.layout.fragment_user, container, false);
        String login = getArguments().getString("login");
        getUserData(login);
        imageManager = new ImageManager();

        try {
            initViews(v);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return v;
    }

    private void initUserListened() {
        PlaylistManager.getInstance(getActivity()).getUserIdPlaylist(mUser.getLogin(), this);
    }

    private void initBubblePicker() {

        bubblePicker.setAdapter(new BubblePickerAdapter() {
            @Override
            public int getTotalCount() {
                return mPlaylist.size();
            }

            @NotNull
            @Override
            public PickerItem getItem(int i) {
                Playlist playlist = mPlaylist.get(i);
                PickerItem item = new PickerItem();
                item.setTitle(playlist.getName());
                item.setGradient(new BubbleGradient(colors.getColor((i * 2) % 8, 0),
                        colors.getColor((i * 2) % 8 + 1, 0), BubbleGradient.VERTICAL));
                item.setTextColor(ContextCompat.getColor(getContext(),android.R.color.white));
                if(playlist.getThumbnail() != null) {
                    ImageManager im = new ImageManager();
                    item.setBackgroundImage(im.getDrawable(Objects.requireNonNull(getActivity()).getApplicationContext(), playlist.getThumbnail()));
                }
                item.setTypeface(Typeface.DEFAULT);
                return item;
            }
        });



        bubblePicker.setListener(new BubblePickerListener() {
            @Override
            public void onBubbleSelected(@NotNull PickerItem pickerItem) {
                if (pickerItem.isSelected()){
                    goToPlaylist(pickerItem);
                }
                goToPlaylist(pickerItem);
            }

            @Override
            public void onBubbleDeselected(@NotNull PickerItem pickerItem) {

            }
        });

        bubblePicker.setCenterImmediately(true);
    }

    public void goToPlaylist(PickerItem pickerItem){
        Playlist selectedPlaylist;
        for(int i = 0; i < mPlaylist.size(); i++){
            if(pickerItem.getTitle() == mPlaylist.get(i).getName()){
                selectedPlaylist = mPlaylist.get(i);
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, new PlaylistFragment(selectedPlaylist));
                transaction.addToBackStack(null);
                transaction.commit();
                break;
            }
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        bubblePicker.onPause();
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
        bubblePicker = (BubblePicker) v.findViewById(R.id.user_picker);
        colors = getResources().obtainTypedArray(R.array.colors);

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
        initUserListened();
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
    public void onExternalUserPlaylistReceived(ArrayList<Playlist> playlist) {
        mPlaylist = playlist;
        initBubblePicker();
        bubblePicker.onResume();
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

    }
}
