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
import edu.url.salle.eric.macia.bubblefy.controller.music.TrendingSorter;
import edu.url.salle.eric.macia.bubblefy.model.Confirmation;
import edu.url.salle.eric.macia.bubblefy.model.Genre;
import edu.url.salle.eric.macia.bubblefy.model.Playlist;
import edu.url.salle.eric.macia.bubblefy.model.Track;
import edu.url.salle.eric.macia.bubblefy.restapi.callback.GenreCallback;
import edu.url.salle.eric.macia.bubblefy.restapi.callback.TrackCallback;
import edu.url.salle.eric.macia.bubblefy.restapi.manager.GenreManager;
import edu.url.salle.eric.macia.bubblefy.restapi.manager.ImageManager;
import edu.url.salle.eric.macia.bubblefy.restapi.manager.TrackManager;

public class HomeFragment extends Fragment implements TrackCallback {

    private static final String TAG = "HomeFragment";

    //Bubble Picker
    private BubblePicker bubblePicker;
    private ArrayList<Track> mTrendingTracks;
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

        if (MainActivity.mediaPlayer.isPlaying()) {
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
        TrackManager.getInstance(getActivity()).getAllTracks(this);

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
                return Math.min(mTrendingTracks.size(), 30);
            }

            @NotNull
            @Override
            public PickerItem getItem(int i) {
                Track track = mTrendingTracks.get(i);
                PickerItem item = new PickerItem();
                item.setTitle(track.getName());
                item.setGradient(new BubbleGradient(colors.getColor((i * 2) % 15, 0),
                        colors.getColor((i * 2) % 15 + 1, 0), BubbleGradient.VERTICAL));
                item.setTextColor(ContextCompat.getColor(getContext(), android.R.color.white));
                if(track.getThumbnail() != null) {
                    ImageManager im = new ImageManager();
                    item.setBackgroundImage(im.getDrawable(Objects.requireNonNull(getActivity()).getApplicationContext(), track.getThumbnail()));
                }
                item.setTypeface(Typeface.DEFAULT);
                return item;
            }
        });


        bubblePicker.setListener(new BubblePickerListener() {
            @Override
            public void onBubbleSelected(@NotNull PickerItem pickerItem) {

            }

            @Override
            public void onBubbleDeselected(@NotNull PickerItem pickerItem) {
                trendingTrackClicked(pickerItem);
            }
        });

        bubblePicker.setCenterImmediately(true);
    }

    public void trackClicked(int position) {
        MainActivity.addSongList(mTracks.get(position));
    }

    public void trendingTrackClicked(PickerItem pickerItem) {
        Track selectedTrack;
        for(int i = 0; i < mTrendingTracks.size(); i++){
            if(pickerItem.getTitle() == mTrendingTracks.get(i).getName()){
                selectedTrack = mTrendingTracks.get(i);
                MainActivity.addSongList(selectedTrack);
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

    @Override
    public void onTracksReceived(List<Track> tracks) {
        mTrendingTracks = new ArrayList<>();
        mTrendingTracks.addAll(tracks);
        mTrendingTracks.sort(new TrendingSorter());
        initBubblePicker(v);
        bubblePicker.onResume();
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

        mAdapter.setOnItemClickListener(new BubbleTrackListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                trackClicked(position);
            }
        });
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
}
