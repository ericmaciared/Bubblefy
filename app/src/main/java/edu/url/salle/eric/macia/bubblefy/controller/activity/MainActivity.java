package edu.url.salle.eric.macia.bubblefy.controller.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import edu.url.salle.eric.macia.bubblefy.R;
import edu.url.salle.eric.macia.bubblefy.controller.fragments.BottomSheetDialog;
import edu.url.salle.eric.macia.bubblefy.controller.fragments.HomeFragment;
import edu.url.salle.eric.macia.bubblefy.controller.fragments.ProfileFragment;
import edu.url.salle.eric.macia.bubblefy.controller.fragments.SearchFragment;
import edu.url.salle.eric.macia.bubblefy.controller.fragments.UploadFragment;

public class MainActivity extends AppCompatActivity implements BottomSheetDialog.BottomSheetListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        setInitialFragment();

    }

    private void initViews() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;

                switch (item.getItemId()){
                    case R.id.nav_home:
                        selectedFragment = new HomeFragment();
                        break;
                    case R.id.nav_search:
                        selectedFragment = new SearchFragment();
                        break;
                    case R.id.nav_profile:
                        selectedFragment = new UploadFragment();
                        break;
                }

                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        selectedFragment).commit();

                return true;
            }
        });
    }

    private void setInitialFragment() {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                new HomeFragment()).commit();
    }

    @Override
    public void onButtonClicked(String text) {

    }
}


