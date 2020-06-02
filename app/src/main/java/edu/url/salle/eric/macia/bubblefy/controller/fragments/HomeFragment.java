package edu.url.salle.eric.macia.bubblefy.controller.fragments;

import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import edu.url.salle.eric.macia.bubblefy.R;
import edu.url.salle.eric.macia.bubblefy.controller.activity.MainActivity;
import edu.url.salle.eric.macia.bubblefy.controller.adapters.BubbleTrackListAdapter;
import edu.url.salle.eric.macia.bubblefy.model.Confirmation;
import edu.url.salle.eric.macia.bubblefy.model.Genre;
import edu.url.salle.eric.macia.bubblefy.model.Playlist;
import edu.url.salle.eric.macia.bubblefy.model.Track;
import edu.url.salle.eric.macia.bubblefy.restapi.callback.GenreCallback;
import edu.url.salle.eric.macia.bubblefy.restapi.callback.TrackCallback;
import edu.url.salle.eric.macia.bubblefy.restapi.manager.GenreManager;
import edu.url.salle.eric.macia.bubblefy.restapi.manager.ImageManager;
import edu.url.salle.eric.macia.bubblefy.restapi.manager.TrackManager;

public class HomeFragment extends Fragment implements TrackCallback, GenreCallback {

    //Bubble Picker
    private BubblePicker bubblePicker;
    private ArrayList<Genre> mGenres;
    private TypedArray colors;

    private View v;
    private RecyclerView recyclerView;
    private BubbleTrackListAdapter mAdapter;
    private ArrayList<Track> mTracks;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, container, false);

        initListenAgain(v);

        if (MainActivity.mediaPlayer.isPlaying()){
            MainActivity.showNavigation(true);
            MainActivity.showPlayback(true);
        }

        colors = getResources().obtainTypedArray(R.array.colors);

        return v;
    }

    private void initListenAgain(View v) {
        recyclerView = (RecyclerView) v.findViewById(R.id.recyclerView);
        bubblePicker = (BubblePicker) v.findViewById(R.id.picker);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL,
                false);
        mAdapter = new BubbleTrackListAdapter(getActivity(), null);

        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(mAdapter);

        TrackManager.getInstance(getActivity()).getUserLikedTracks(this);
        GenreManager.getInstance(getActivity()).getAllGenres(this);

        mAdapter.setOnItemClickListener(new BubbleTrackListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                trackClicked(position);
            }
        });
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

    public void trackClicked(int position){
        MainActivity.addSongList(mTracks.get(position));
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

    public void goToGenre(PickerItem pickerItem){
        Genre selectedGenre;
        for(int i = 0; i < mGenres.size(); i++){
            if(pickerItem.getTitle() == mGenres.get(i).getName()){
                selectedGenre = mGenres.get(i);
                /*
                FragmentTransaction transaction = getFragmentManager().beginTransaction();

                transaction.replace(R.id.fragment_container, new PlaylistFragment(selectedGenre));
                transaction.addToBackStack(null);
                transaction.commit();
                break;
                 */
            }
        }

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
        mTracks = (ArrayList<Track>) tracks;
        mAdapter = new BubbleTrackListAdapter(getActivity(), mTracks);
        recyclerView.setAdapter(mAdapter);
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

    public View getV() {
        return v;
    }

    @Override
    public void onGenresReceive(ArrayList<Genre> genres) {
        mGenres = new ArrayList<>();
        mGenres.addAll(genres);
        initBubblePicker(getV());
        bubblePicker.onResume();
    }

    @Override
    public void onTracksByGenre(ArrayList<Track> tracks) {

    }
}
