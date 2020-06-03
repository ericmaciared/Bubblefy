package edu.url.salle.eric.macia.bubblefy.controller.fragments;

import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.igalata.bubblepicker.BubblePickerListener;
import com.igalata.bubblepicker.adapter.BubblePickerAdapter;
import com.igalata.bubblepicker.model.BubbleGradient;
import com.igalata.bubblepicker.model.PickerItem;
import com.igalata.bubblepicker.rendering.BubblePicker;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import edu.url.salle.eric.macia.bubblefy.R;
import edu.url.salle.eric.macia.bubblefy.controller.activity.MainActivity;
import edu.url.salle.eric.macia.bubblefy.controller.adapters.BubbleTrackListAdapter;
import edu.url.salle.eric.macia.bubblefy.controller.adapters.PlaylistListAdapter;
import edu.url.salle.eric.macia.bubblefy.controller.adapters.TrackListAdapter;
import edu.url.salle.eric.macia.bubblefy.controller.adapters.UserListAdapter;
import edu.url.salle.eric.macia.bubblefy.controller.callbacks.TrackListCallback;
import edu.url.salle.eric.macia.bubblefy.model.Confirmation;
import edu.url.salle.eric.macia.bubblefy.model.Genre;
import edu.url.salle.eric.macia.bubblefy.model.Playlist;
import edu.url.salle.eric.macia.bubblefy.model.Search;
import edu.url.salle.eric.macia.bubblefy.model.Track;
import edu.url.salle.eric.macia.bubblefy.model.User;
import edu.url.salle.eric.macia.bubblefy.restapi.callback.GenreCallback;
import edu.url.salle.eric.macia.bubblefy.restapi.callback.PlaylistCallback;
import edu.url.salle.eric.macia.bubblefy.restapi.callback.SearchCallback;
import edu.url.salle.eric.macia.bubblefy.restapi.callback.TrackCallback;
import edu.url.salle.eric.macia.bubblefy.restapi.manager.GenreManager;
import edu.url.salle.eric.macia.bubblefy.restapi.manager.SearchManager;


public class SearchFragment extends Fragment
        implements TrackCallback, TrackListCallback, SearchCallback,
        RadioGroup.OnCheckedChangeListener, PlaylistCallback,
        BottomSheetDialog.BottomSheetListener, GenreCallback {

    private static final String TAG = "SearchFragment";

    //Bubble Picker
    private BubblePicker bubblePicker;
    private ArrayList<Genre> mGenres;
    private TypedArray colors;

    RadioGroup radioGroup;
    RadioButton radioButton1;
    RadioButton radioButton2;
    RadioButton radioButton3;

    private RecyclerView mRecyclerView;
    private ArrayList<Track> mTracks;
    private ArrayList<Playlist> mPlay;
    private ArrayList<User> mUsers;



    private ImageButton buttonLogin;
    private EditText searchText;
    private boolean searchPerformed = false;
    private LinearLayout genresView;
    private View v;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v =  inflater.inflate(R.layout.fragment_search, container, false);

        initViews(v);

        if (MainActivity.mediaPlayer.isPlaying()) {
            MainActivity.showNavigation(true);
            MainActivity.showPlayback(true);
        }

        colors = getResources().obtainTypedArray(R.array.colors);

        return v;
    }

    private void initViews(View v){
        mTracks = new ArrayList<>();
        mPlay = new ArrayList<>();
        mUsers = new ArrayList<>();
        mGenres = new ArrayList<>();

        bubblePicker = (BubblePicker) v.findViewById(R.id.genre_picker);
        radioGroup = v.findViewById(R.id.group_buttons);
        radioButton1 = v.findViewById(R.id.radio_song);
        radioButton2 = v.findViewById(R.id.radio_playlists);
        radioButton3 = v.findViewById(R.id.radio_users);


        buttonLogin = v.findViewById(R.id.search_button);
        radioGroup.setOnCheckedChangeListener(this);
        searchText = v.findViewById(R.id.search_text);
        buttonLogin.setOnClickListener(v1 -> getSearch());

        getAllGenres();

        mRecyclerView = (RecyclerView) v.findViewById(R.id.recyclerView);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
        TrackListAdapter adapter = new TrackListAdapter(this, getContext(), null);
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.setAdapter(adapter);

        genresView = (LinearLayout) v.findViewById(R.id.genresView);
        genresView.setVisibility(View.VISIBLE);

    }

    /************************SEARCH IMPLEMENTATION**********************/
    public void getSearch(){
        SearchManager.getInstance(getActivity()).getSearchResult((searchText.getText()).toString(),
                this);
    }

    @Override
    public void onSearchReceived(Search search) {
        mTracks = (ArrayList) search.getTracks();
        mPlay = (ArrayList) search.getPlaylists();
        mUsers = (ArrayList) search.getUsers();
        searchPerformed = true;
        String text;

        genresView.setVisibility(View.GONE);
        mRecyclerView.setVisibility(View.VISIBLE);

        if(radioButton1.isChecked()){
            if(!mTracks.isEmpty()) {
                TrackListAdapter adapter = new TrackListAdapter(this,getActivity(), mTracks);
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
                TrackListAdapter adapter = new TrackListAdapter(this,getActivity(), null);
                mRecyclerView.setAdapter(adapter);
                text = "No songs found";
                if(!searchPerformed) text = "No search done";
                Toast toast =  Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT);
                toast.show();
            }
        }
        else if(radioButton2.isChecked()){
            if(!mPlay.isEmpty()) {
                PlaylistListAdapter adapter = new PlaylistListAdapter(getActivity(), mPlay);
                mRecyclerView.setAdapter(adapter);
                adapter.setItemClickedListener(new PlaylistListAdapter.OnItemClickedListener() {
                    @Override
                    public void onItemClicked(int position) {
                        playlistClicked(position);
                    }

                    @Override
                    public void onOptionsClicked(int position) {
                        playlistOptions(position);
                    }
                });
            }
            else{
                PlaylistListAdapter adapter = new PlaylistListAdapter(getActivity(), null);
                mRecyclerView.setAdapter(adapter);
                text = "No playlists found";
                if(!searchPerformed) text = "No search done";
                Toast toast =  Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT);
                toast.show();
            }
        }
        else{
            if(!mUsers.isEmpty()) {
                UserListAdapter adapter = new UserListAdapter(getActivity(), mUsers);
                mRecyclerView.setAdapter(adapter);
                adapter.setItemClickedListener(new UserListAdapter.OnItemClickedListener() {
                    @Override
                    public void onItemClicked(int position) {
                        userClicked(position);
                    }

                    @Override
                    public void onOptionsClicked(int position) {
                        userOptions(position);
                    }
                });
            }
            else{
                UserListAdapter adapter = new UserListAdapter(null, mUsers);
                mRecyclerView.setAdapter(adapter);
                text = "No users found";
                if(!searchPerformed) text = "No search done";
                Toast toast =  Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT);
                toast.show();
            }
        }
    }

    private void initBubblePicker(View v) {
        bubblePicker.setAdapter(new BubblePickerAdapter() {
            @Override
            public int getTotalCount() {
                return mGenres.size();
            }

            @NotNull
            @Override
            public PickerItem getItem(int i) {
                Genre genre = mGenres.get(i);
                PickerItem item = new PickerItem();
                item.setTitle(genre.getName());
                item.setGradient(new BubbleGradient(colors.getColor((i * 2) % 8, 0),
                        colors.getColor((i * 2) % 8 + 1, 0), BubbleGradient.VERTICAL));
                item.setTextColor(ContextCompat.getColor(getContext(),android.R.color.white));
                item.setTypeface(Typeface.DEFAULT);
                return item;
            }
        });



        bubblePicker.setListener(new BubblePickerListener() {
            @Override
            public void onBubbleSelected(@NotNull PickerItem pickerItem) {
                if (pickerItem.isSelected()){
                    goToGenre(pickerItem);
                }
                goToGenre(pickerItem);
            }

            @Override
            public void onBubbleDeselected(@NotNull PickerItem pickerItem) {

            }
        });

        bubblePicker.setCenterImmediately(true);
    }



    /******************************MUSIC PLAYBACK******************************/

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


    public void trackClicked(int position){
        //IMPLEMENTAR
    }

    public void playlistClicked(int position){
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, new PlaylistFragment(mPlay.get(position)));
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void userClicked(int position){
        Bundle bundle = new Bundle();
        bundle.putString("login", mUsers.get(position).getLogin());
        UserFragment fragUser = new UserFragment();
        fragUser.setArguments(bundle);
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragUser);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void trackOptions(int position){
        BottomSheetDialog trackOptions = new BottomSheetDialog(mTracks.get(position));
        trackOptions.show(getChildFragmentManager(), "trackBottomSheet");
    }

    public void playlistOptions(int position){
        //IMPLEMENTAR
    }

    public void userOptions(int position){
        //IMPLEMENTAR
    }

    @Override
    public void onNoSearch(Throwable throwable) {

    }

    @Override
    public void onFailure(Throwable throwable) {

    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        String text;

        if(radioButton1.isChecked()){
            if(!mTracks.isEmpty()) {
                TrackListAdapter adapter = new TrackListAdapter(this,getActivity(), mTracks);
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
                TrackListAdapter adapter = new TrackListAdapter(this,getActivity(), null);
                mRecyclerView.setAdapter(adapter);
                text = "No songs found";
                if(!searchPerformed) text = "No search done";
                Toast toast =  Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT);
                toast.show();
            }
        }
        else if(radioButton2.isChecked()){
            if(!mPlay.isEmpty()) {
                PlaylistListAdapter adapter = new PlaylistListAdapter(getActivity(), mPlay);
                mRecyclerView.setAdapter(adapter);
                adapter.setItemClickedListener(new PlaylistListAdapter.OnItemClickedListener() {
                    @Override
                    public void onItemClicked(int position) {
                        playlistClicked(position);
                    }

                    @Override
                    public void onOptionsClicked(int position) {
                        playlistOptions(position);
                    }
                });
            }
            else{
                PlaylistListAdapter adapter = new PlaylistListAdapter(getActivity(), null);
                mRecyclerView.setAdapter(adapter);
                text = "No playlists found";
                if(!searchPerformed) text = "No search done";
                Toast toast =  Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT);
                toast.show();
            }
        }
        else{
            if(!mUsers.isEmpty()) {
                UserListAdapter adapter = new UserListAdapter(getActivity(), mUsers);
                mRecyclerView.setAdapter(adapter);
                adapter.setItemClickedListener(new UserListAdapter.OnItemClickedListener() {
                    @Override
                    public void onItemClicked(int position) {
                        userClicked(position);
                    }

                    @Override
                    public void onOptionsClicked(int position) {
                        userOptions(position);
                    }
                });
            }
            else{
                UserListAdapter adapter = new UserListAdapter(null, mUsers);
                mRecyclerView.setAdapter(adapter);
                text = "No users found";
                if(!searchPerformed) text = "No search done";
                Toast toast =  Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT);
                toast.show();
            }
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

    @Override
    public void onButtonClicked(String text) {

    }

    @Override
    public void onTrackSelected(Track track) {
        MainActivity.addSongList(track);
    }

    @Override
    public void onTrackSelected(int index) {
        MainActivity.addSongList(mTracks.get(index));
    }

    @Override
    public void onTracksReceived(List<Track> tracks) {
        mTracks = (ArrayList) tracks;
        TrackListAdapter adapter = new TrackListAdapter(this, getActivity(), mTracks);
        mRecyclerView.setAdapter(adapter);
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
    public void onGenresReceive(ArrayList<Genre> genres) {
        mGenres = new ArrayList<>();
        mGenres.addAll(genres);
        initBubblePicker(v);
        bubblePicker.onResume();
    }

    @Override
    public void onTracksByGenre(ArrayList<Track> tracks) {

    }

    public void goToGenre(PickerItem pickerItem){
        Genre selectedGenre;
        for(int i = 0; i < mGenres.size(); i++){
            if(pickerItem.getTitle() == mGenres.get(i).getName()){
                selectedGenre = mGenres.get(i);
                /* TODO: CREATE GenreFragment or Adapt the Playlist one to open all the tracks from the Genre API call
                FragmentTransaction transaction = getFragmentManager().beginTransaction();

                transaction.replace(R.id.fragment_container, new PlaylistFragment(selectedGenre));
                transaction.addToBackStack(null);
                transaction.commit();
                break;
                 */
            }
        }

    }

    public void getAllGenres(){
        GenreManager.getInstance(getActivity()).getAllGenres(this);
    }
}
