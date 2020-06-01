package edu.url.salle.eric.macia.bubblefy.controller.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.igalata.bubblepicker.BubblePickerListener;
import com.igalata.bubblepicker.adapter.BubblePickerAdapter;
import com.igalata.bubblepicker.model.PickerItem;
import com.igalata.bubblepicker.rendering.BubblePicker;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import edu.url.salle.eric.macia.bubblefy.R;
import edu.url.salle.eric.macia.bubblefy.controller.adapters.BubbleTrackListAdapter;
import edu.url.salle.eric.macia.bubblefy.model.Confirmation;
import edu.url.salle.eric.macia.bubblefy.model.Track;
import edu.url.salle.eric.macia.bubblefy.restapi.callback.TrackCallback;
import edu.url.salle.eric.macia.bubblefy.restapi.manager.TrackManager;

public class HomeFragment extends Fragment implements TrackCallback {

    private BubblePicker bubblePicker;

    private String[] name = {
            "Colores",
            "YHLQMDLG",
            "Blue",
            "Chill",
            "Party"
    };

    private RecyclerView recyclerView;
    private BubbleTrackListAdapter mAdapter;
    private ArrayList<Track> mTracks;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, container, false);

        initListenAgain(v);
        initBubblePicker(v);

        return v;
    }

    private void initListenAgain(View v) {
        recyclerView = (RecyclerView) v.findViewById(R.id.recyclerView);

        LinearLayoutManager manager = new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL,
                false);
        mAdapter = new BubbleTrackListAdapter(getActivity(), null);

        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(mAdapter);

        TrackManager.getInstance(getActivity()).getUserLikedTracks(this);

        mAdapter.setOnItemClickListener(new BubbleTrackListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                trackClicked(position);
            }
        });
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

    public void trackClicked(int position){
        //TODO Position de la canço triada del mTracks
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
}
