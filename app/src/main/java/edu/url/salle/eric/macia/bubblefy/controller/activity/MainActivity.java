package edu.url.salle.eric.macia.bubblefy.controller.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.igalata.bubblepicker.BubblePickerListener;
import com.igalata.bubblepicker.adapter.BubblePickerAdapter;
import com.igalata.bubblepicker.model.PickerItem;
import com.igalata.bubblepicker.rendering.BubblePicker;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import edu.url.salle.eric.macia.bubblefy.R;
import edu.url.salle.eric.macia.bubblefy.model.Track;

public class MainActivity extends AppCompatActivity {

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

        ibtnHome = (ImageButton) findViewById(R.id.home_button);
        ibtnSearch = (ImageButton) findViewById(R.id.search_button);
        ibtnProfile = (ImageButton) findViewById(R.id.profile_button);

        initViews();
        initBubblePicker();

        bubblePicker.setCenterImmediately(true);
        bubblePicker.setKeepScreenOn(true);
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
                //item.setTitle(name[i]);
                //item.setTextColor(255);
                //item.setOverlayAlpha(1);
                return item;
            }
        });

        bubblePicker.setListener(new BubblePickerListener() {
            @Override
            public void onBubbleSelected(@NotNull PickerItem pickerItem) {
                Toast.makeText(MainActivity.this, "You have clicked "+
                        pickerItem.getTitle(), Toast.LENGTH_SHORT);
            }

            @Override
            public void onBubbleDeselected(@NotNull PickerItem pickerItem) {

            }
        });
    }

    private void initViews(){
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
}
