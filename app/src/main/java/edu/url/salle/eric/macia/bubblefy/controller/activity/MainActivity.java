package edu.url.salle.eric.macia.bubblefy.controller.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import edu.url.salle.eric.macia.bubblefy.R;

public class MainActivity extends AppCompatActivity {

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
}
