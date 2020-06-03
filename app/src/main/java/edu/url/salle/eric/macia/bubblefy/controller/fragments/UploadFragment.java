package edu.url.salle.eric.macia.bubblefy.controller.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import edu.url.salle.eric.macia.bubblefy.R;
import edu.url.salle.eric.macia.bubblefy.controller.activity.MainActivity;
import edu.url.salle.eric.macia.bubblefy.controller.dialogs.StateDialog;
import edu.url.salle.eric.macia.bubblefy.model.Confirmation;
import edu.url.salle.eric.macia.bubblefy.model.Genre;
import edu.url.salle.eric.macia.bubblefy.model.Track;
import edu.url.salle.eric.macia.bubblefy.restapi.callback.GenreCallback;
import edu.url.salle.eric.macia.bubblefy.restapi.callback.TrackCallback;
import edu.url.salle.eric.macia.bubblefy.restapi.manager.CloudinaryManager;
import edu.url.salle.eric.macia.bubblefy.restapi.manager.GenreManager;
import edu.url.salle.eric.macia.bubblefy.utils.Constants;

import static android.app.Activity.RESULT_OK;


public class UploadFragment extends Fragment implements GenreCallback, TrackCallback {

    private EditText etTitle;
    private Spinner mSpinner;
    private TextView mFilename;
    private TextView mImagename;
    private Button btnFind, btnFindImg, btnCancel, btnAccept;

    private ArrayList<String> mGenres;
    private ArrayList<Genre> mGenresObjs;
    private Uri mFileUri;
    private Uri mImageUri = null;
    private boolean hasImage = false;

    private Context mContext;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v =  inflater.inflate(R.layout.fragment_upload, container, false);

        MainActivity.showPlayback(false);
        MainActivity.showNavigation(false);

        initViews(v);
        getData();
        return v;
    }


    private void initViews(View v) {
        etTitle = (EditText) v.findViewById(R.id.create_song_title);
        mFilename = (TextView) v.findViewById(R.id.create_song_file_name);
        mImagename = (TextView) v.findViewById(R.id.create_image_file_name);

        mSpinner = (Spinner) v.findViewById(R.id.create_song_genre);

        btnFind = (Button) v.findViewById(R.id.create_song_file);
        btnFind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getAudioFromStorage();
            }
        });

        btnFindImg = (Button) v.findViewById(R.id.create_image_file);
        btnFindImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getImageFromStorage();
            }
        });

        btnCancel = (Button) v.findViewById(R.id.create_song_cancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //getActivity().finish();
            }
        });

        btnAccept = (Button) v.findViewById(R.id.create_song_accept);
        btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkParameters()) {
                    etTitle.setFocusable(false);
                    showStateDialog(false);
                    uploadToCloudinary();
                    if(mImageUri != null){
                        uploadImageToCloudinary();
                    }
                }
            }
        });

    }

    private void getData() {
        GenreManager.getInstance(getContext()).getAllGenres(this);
    }

    private boolean checkParameters() {
        if (!etTitle.getText().toString().equals("")) {
            if (mFileUri != null) {
                return true;
            }
        }
        return false;
    }

    private void showStateDialog(boolean completed) {
        StateDialog.getInstance(getContext()).showStateDialog(completed);
    }

    private void getAudioFromStorage() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setType("audio/mpeg");
        startActivityForResult(Intent.createChooser(intent, "Choose a song"), Constants.STORAGE.SONG_SELECTED);
    }

    private void getImageFromStorage() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setType("image/jpeg");
        startActivityForResult(Intent.createChooser(intent, "Choose a song"), Constants.STORAGE.IMAGE_SELECTED);
    }

    private void uploadToCloudinary() {
        Genre genre = new Genre();
        for (Genre g: mGenresObjs) {
            if (g.getName().equals(mSpinner.getSelectedItem().toString())) {
                genre = g;
            }
        }
        CloudinaryManager.getInstance(getContext(), this).uploadAudioFile(mFileUri, etTitle.getText().toString(), genre, hasImage);
    }

    private void uploadImageToCloudinary() {
                CloudinaryManager.getInstance(getContext(), this).uploadImageFile(mImageUri, etTitle.getText().toString());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.STORAGE.SONG_SELECTED && resultCode == RESULT_OK) {
            mFileUri = data.getData();
            mFilename.setText("Song Selected");
        }
        if (requestCode == Constants.STORAGE.IMAGE_SELECTED && resultCode == RESULT_OK) {
            mImageUri = data.getData();
            mImagename.setText("Image Selected");
            hasImage = true;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    /**********************************************************************************************
     *   *   *   *   *   *   *   *   GenreCallback   *   *   *   *   *   *   *   *   *
     **********************************************************************************************/

    @Override
    public void onGenresReceive(ArrayList<Genre> genres) {
        mGenresObjs = genres;
        mGenres = (ArrayList<String>) genres.stream().map(Genre -> Genre.getName()).collect(Collectors.toList());
        ArrayAdapter<String> adapter = new ArrayAdapter(getContext(), R.layout.support_simple_spinner_dropdown_item, mGenres);
        mSpinner.setAdapter(adapter);
    }

    @Override
    public void onTracksByGenre(ArrayList<Track> tracks) {

    }

    @Override
    public void onFailure(Throwable throwable) {

    }

    /**********************************************************************************************
     *   *   *   *   *   *   *   *   TrackCallback   *   *   *   *   *   *   *   *   *
     **********************************************************************************************/

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
        StateDialog.getInstance(getContext()).showStateDialog(true);
        Thread watchDialog = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (StateDialog.getInstance(mContext).isDialogShown()){}
                    //getActivity().finish();
                } catch (Exception e) {
                }
            }
        });
        watchDialog.start();
    }
}
