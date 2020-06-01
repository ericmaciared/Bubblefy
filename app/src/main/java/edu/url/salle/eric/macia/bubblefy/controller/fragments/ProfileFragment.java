package edu.url.salle.eric.macia.bubblefy.controller.fragments;

import android.app.WallpaperManager;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.igalata.bubblepicker.BubblePickerListener;
import com.igalata.bubblepicker.adapter.BubblePickerAdapter;
import com.igalata.bubblepicker.model.BubbleGradient;
import com.igalata.bubblepicker.model.PickerItem;
import com.igalata.bubblepicker.rendering.BubblePicker;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import edu.url.salle.eric.macia.bubblefy.R;
import edu.url.salle.eric.macia.bubblefy.controller.activity.LoginActivity;
import edu.url.salle.eric.macia.bubblefy.controller.activity.MainActivity;
import edu.url.salle.eric.macia.bubblefy.controller.adapters.BubbleTrackListAdapter;
import edu.url.salle.eric.macia.bubblefy.model.Confirmation;
import edu.url.salle.eric.macia.bubblefy.model.Follow;
import edu.url.salle.eric.macia.bubblefy.model.Playlist;
import edu.url.salle.eric.macia.bubblefy.model.Track;
import edu.url.salle.eric.macia.bubblefy.model.User;
import edu.url.salle.eric.macia.bubblefy.model.UserToken;
import edu.url.salle.eric.macia.bubblefy.restapi.callback.TrackCallback;
import edu.url.salle.eric.macia.bubblefy.restapi.callback.UserCallback;
import edu.url.salle.eric.macia.bubblefy.restapi.manager.ImageManager;
import edu.url.salle.eric.macia.bubblefy.restapi.manager.TrackManager;
import edu.url.salle.eric.macia.bubblefy.restapi.manager.UserManager;
import edu.url.salle.eric.macia.bubblefy.utils.Session;

public class ProfileFragment extends Fragment implements TrackCallback, UserCallback {

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
    private String login;
    private User mUser;
    private List<Track> mTracks;
    private TypedArray colors;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v =  inflater.inflate(R.layout.fragment_profile, container, false);
        login = getArguments().getString("login");

        colors = getResources().obtainTypedArray(R.array.colors);
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
        TrackManager.getInstance(getActivity()).getUserLikedTracks(this);
    }

    private void initBubblePicker(View v) {
        bubblePicker = (BubblePicker) v.findViewById(R.id.profile_picker);
        bubblePicker.setAdapter(new BubblePickerAdapter() {

            @Override
            public int getTotalCount() {
                return (name.length);
            }

            @NotNull
            @Override
            public PickerItem getItem(int i) {
                PickerItem item = new PickerItem();
                item.setTitle(name[i]);
                item.setGradient(new BubbleGradient(colors.getColor((i * 2) % 8, 0),
                        colors.getColor((i * 2) % 8 + 1, 0), BubbleGradient.VERTICAL));
                item.setTextColor(ContextCompat.getColor(getContext(),android.R.color.white));
                item.setBackgroundImage(getResources().getDrawable(R.drawable.colores));
                item.setTypeface(Typeface.DEFAULT);
                return item;
            }


        });

        /*
        bubblePicker.setAdapter(new BubblePickerAdapter() {
            @Override
            public int getTotalCount() {
                return mTracks.size();
            }

            @NotNull
            @Override
            public PickerItem getItem(int i) {
                Track track = mTracks.get(i);
                PickerItem item = new PickerItem();
                item.setTitle(track.getName());
                item.setGradient(new BubbleGradient(colors.getColor((i * 2) % 8, 0),
                        colors.getColor((i * 2) % 8 + 1, 0), BubbleGradient.VERTICAL));
                item.setTextColor(ContextCompat.getColor(getContext(),android.R.color.white));
                if(track.getThumbnail() != null) {
                    ImageManager im = new ImageManager();
                    item.setBackgroundImage(im.getDrawable(Objects.requireNonNull(getActivity()).getApplicationContext(), track.getThumbnail()));
                }
                item.setTypeface(Typeface.DEFAULT);
                return item;
            }
        });
        */

        bubblePicker.setListener(new BubblePickerListener() {
            @Override
            public void onBubbleSelected(@NotNull PickerItem pickerItem) {
                if (pickerItem.isSelected()){
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
        getUserData(login);
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

        //Bubble Picker
        bubblePicker = (BubblePicker) v.findViewById(R.id.profile_picker);

        //Followers and Following
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
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, new UploadFragment());
                transaction.addToBackStack(null);
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

        //Profile Information
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

    /*
    @Override
    public void onUserLikedTracksReceived(List<Track> tracks) {
        mTracks = (ArrayList<Track>) tracks;
        ArrayList<PickerItem> listItems = new ArrayList<>();
        for(int i = 0; i < tracks.size(); i++) {
            PickerItem item = new PickerItem();
            Track track = mTracks.get(i);
            item.setTitle(track.getName());
            item.setGradient(new BubbleGradient(colors.getColor((i * 2) % 8, 0),
                    colors.getColor((i * 2) % 8 + 1, 0), BubbleGradient.VERTICAL));
            item.setTextColor(ContextCompat.getColor(getContext(), android.R.color.white));
            if (track.getThumbnail() != null) {
                ImageManager im = new ImageManager();
                item.setBackgroundImage(im.getDrawable(Objects.requireNonNull(getActivity()).getApplicationContext(), track.getThumbnail()));
            }
            item.setTypeface(Typeface.DEFAULT);
            listItems.add(item);
        }
        bubblePicker.setItems(listItems);
    }
    */

    @Override
    public void onUserLikedTracksReceived(List<Track> tracks) {
        bubblePicker = (BubblePicker) getView().findViewById(R.id.profile_picker);
        mTracks = (ArrayList<Track>) tracks;
        bubblePicker.setAdapter(new BubblePickerAdapter() {
            @Override
            public int getTotalCount() {
                return mTracks.size();
            }

            @NotNull
            @Override
            public PickerItem getItem(int i) {
                Track track = mTracks.get(i);
                PickerItem item = new PickerItem();
                item.setTitle(track.getName());
                item.setGradient(new BubbleGradient(colors.getColor((i * 2) % 8, 0),
                        colors.getColor((i * 2) % 8 + 1, 0), BubbleGradient.VERTICAL));
                item.setTextColor(ContextCompat.getColor(getContext(),android.R.color.white));
                if(track.getThumbnail() != null) {
                    ImageManager im = new ImageManager();
                    item.setBackgroundImage(im.getDrawable(Objects.requireNonNull(getActivity()).getApplicationContext(), track.getThumbnail()));
                }
                item.setTypeface(Typeface.DEFAULT);
                return item;
            }
        });

        //initBubblePicker(getView());
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
        setUserFields();
    }

    @Override
    public void onCheckFollowReceived(Follow follow) {

    }

    @Override
    public void onUserFollowed(Follow follow) {

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
                tUsername.setText(mUser.getLogin());
            } else{
                tUsername.setText("No name");
            }

            //Followers and Following
            tFollowers.setText(String.valueOf(mUser.getFollowers()));
            tFollowing.setText(String.valueOf(mUser.getFollowing()));

            //Following button
            UserManager.getInstance(getActivity().getApplicationContext()).checkUserFollowed(mUser.getLogin(), this);

        }
        else{
        }

    }
}
