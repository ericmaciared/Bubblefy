package edu.url.salle.eric.macia.bubblefy.controller.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.igalata.bubblepicker.BubblePickerListener;
import com.igalata.bubblepicker.adapter.BubblePickerAdapter;
import com.igalata.bubblepicker.model.PickerItem;
import com.igalata.bubblepicker.rendering.BubblePicker;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import edu.url.salle.eric.macia.bubblefy.R;
import edu.url.salle.eric.macia.bubblefy.controller.adapters.BubbleTrackListAdapter;
import edu.url.salle.eric.macia.bubblefy.model.Track;
import edu.url.salle.eric.macia.bubblefy.restapi.callback.TrackCallback;
import edu.url.salle.eric.macia.bubblefy.restapi.manager.TrackManager;

public class MainActivity extends AppCompatActivity implements TrackCallback {

    private BubblePicker bubblePicker;

    private String[] name = {
            "Colores",
            "YHLQMDLG",
            "Blue",
            "Chill",
            "Party"
    };

    private RecyclerView recyclerView;
    private ArrayList<Track> mTracks;
    private ImageButton ibtnHome;
    private ImageButton ibtnSearch;
    private ImageButton ibtnProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        initListenAgain();
        initBubblePicker();

    }

    private void initListenAgain() {
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        LinearLayoutManager manager = new LinearLayoutManager(this, RecyclerView.HORIZONTAL,
                false);
        BubbleTrackListAdapter adapter = new BubbleTrackListAdapter(this, null);

        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);

        TrackManager.getInstance(this).getUserLikedTracks(this);
    }

    private void initBubblePicker() {
        bubblePicker = (BubblePicker) findViewById(R.id.picker);

        bubblePicker.setAdapter(new BubblePickerAdapter() {
            @Override
            public int getTotalCount() {
                return name.length;
            }

            @NotNull
            @Override
            public PickerItem getItem(int i) {
                PickerItem item = new PickerItem();
                //item.setColor(120);
                item.setTitle(name[i]);
                //item.setTextColor(255);
                //item.setOverlayAlpha(1);
                return item;
            }

        });

        bubblePicker.setListener(new BubblePickerListener() {
            @Override
            public void onBubbleSelected(@NotNull PickerItem pickerItem) {
                Toast.makeText(MainActivity.this, "You have clicked "+
                        pickerItem.getTitle(), Toast.LENGTH_SHORT).show();
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

    private void initViews(){
        ibtnHome = (ImageButton) findViewById(R.id.home_button);
        ibtnSearch = (ImageButton) findViewById(R.id.search_button);
        ibtnProfile = (ImageButton) findViewById(R.id.profile_button);

        ibtnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                return;
            }
        });

        ibtnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadSearchScreen();
            }
        });
    }

    private void loadSearchScreen() {
        Intent intent = new Intent(this, SearchActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        bubblePicker.onResume();
    }

    @Override
    protected void onPause() {
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
        ArrayList<Track> mTracks = new ArrayList<Track>();
        mTracks = (ArrayList<Track>) tracks;

        BubbleTrackListAdapter adapter = new BubbleTrackListAdapter(this, mTracks);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onFailure(Throwable throwable) {

    }
}
