package edu.url.salle.eric.macia.bubblefy.restapi.manager;

import android.content.Context;
import android.net.Uri;

import androidx.appcompat.app.AppCompatActivity;

import com.cloudinary.android.MediaManager;
import com.cloudinary.android.callback.ErrorInfo;
import com.cloudinary.android.callback.UploadCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import edu.url.salle.eric.macia.bubblefy.model.Genre;
import edu.url.salle.eric.macia.bubblefy.model.Playlist;
import edu.url.salle.eric.macia.bubblefy.model.Track;
import edu.url.salle.eric.macia.bubblefy.restapi.callback.PlaylistCallback;
import edu.url.salle.eric.macia.bubblefy.restapi.callback.TrackCallback;
import edu.url.salle.eric.macia.bubblefy.utils.CloudinaryConfigs;

public class CloudinaryManager extends AppCompatActivity {

    private static CloudinaryManager sManager;
    private Context mContext;
    private String mFileName;
    private Genre mGenre;
    private boolean hasImg;
    private boolean songReady = false;
    private Track savedTrack = new Track();
    private boolean fromPlaylist = false;
    private Playlist playlist;

    private TrackCallback mCallback;
    private PlaylistCallback pCallback;

    public static CloudinaryManager getInstance(Context context, TrackCallback callback) {
        if (sManager == null) {
            sManager = new CloudinaryManager(context, callback);
        }
        return sManager;
    }

    public static CloudinaryManager getInstance(Context context, PlaylistCallback callback, boolean prova) {
        if (sManager == null) {
            sManager = new CloudinaryManager(context, callback);
        }
        return sManager;
    }

    public CloudinaryManager(Context context, TrackCallback callback) {
        mContext = context;
        mCallback = callback;
        MediaManager.init(mContext, CloudinaryConfigs.getConfigurations());
    }

    public CloudinaryManager(Context context, PlaylistCallback callback) {
        mContext = context;
        pCallback = callback;
        MediaManager.init(mContext, CloudinaryConfigs.getConfigurations());
    }

    public synchronized void uploadAudioFile(Uri fileUri, String fileName, Genre genre, boolean hasImage) {
        mGenre = genre;
        mFileName = fileName;
        hasImg = hasImage;
        Map<String, Object> options = new HashMap<>();
        options.put("public_id", fileName);
        options.put("folder", "sallefy/songs/mobile");
        options.put("resource_type", "video");

        MediaManager.get().upload(fileUri)
                .unsigned(fileName)
                .options(options)
                .callback(new CloudinaryCallback())
                .dispatch();
    }

    public synchronized void uploadImageFile(Uri fileUri, String fileName) {

        mFileName = fileName;
        Map<String, Object> options = new HashMap<>();
        options.put("public_id", fileName);
        options.put("folder", "sallefy/images/mobile");
        options.put("resource_type", "auto");

        MediaManager.get().upload(fileUri)
                .unsigned(fileName)
                .options(options)
                .callback(new CloudinaryCallback())
                .dispatch();
    }


    public synchronized void uploadImageFilePlaylist(Uri fileUri, String fileName, Playlist playlist) {
        fromPlaylist = true;
        this.playlist = playlist;
        mFileName = fileName;
        Map<String, Object> options = new HashMap<>();
        options.put("public_id", fileName);
        options.put("folder", "sallefy/images/playlist");
        options.put("resource_type", "auto");

        MediaManager.get().upload(fileUri)
                .unsigned(fileName)
                .options(options)
                .callback(new CloudinaryCallback())
                .dispatch();
    }

    private class CloudinaryCallback implements UploadCallback {

        @Override
        public void onStart(String requestId) {
        }
        @Override
        public void onProgress(String requestId, long bytes, long totalBytes) {
            Double progress = (double) bytes/totalBytes;
        }
        @Override
        public void onSuccess(String requestId, Map resultData) {
            if(!fromPlaylist) {
                if (!hasImg) {
                    Track track = new Track();
                    track.setId(null);
                    track.setName(mFileName);
                    //track.setUser(Session.getInstance(mContext).getUser());
                    //track.setUserLogin(Session.getInstance(mContext).getUser().getLogin());
                    track.setUrl((String) resultData.get("url"));
                    ArrayList<Genre> genres = new ArrayList<>();
                    genres.add(mGenre);
                    track.setGenres(genres);

                    TrackManager.getInstance(mContext).createTrack(track, mCallback);
                } else {
                    if (!songReady) {
                        Track track = new Track();
                        track.setId(null);
                        track.setName(mFileName);
                        //track.setUser(Session.getInstance(mContext).getUser());
                        //track.setUserLogin(Session.getInstance(mContext).getUser().getLogin());
                        if (((String) resultData.get("url")).contains("sallefy/songs/mobile")) {
                            track.setUrl((String) resultData.get("url"));
                        } else {
                            track.setThumbnail((String) resultData.get("url"));
                        }
                        ArrayList<Genre> genres = new ArrayList<>();
                        genres.add(mGenre);
                        track.setGenres(genres);
                        savedTrack = track;
                        songReady = true;
                    } else {
                        if (((String) resultData.get("url")).contains("sallefy/songs/mobile")) {
                            savedTrack.setUrl((String) resultData.get("url"));
                        } else {
                            savedTrack.setThumbnail((String) resultData.get("url"));
                        }
                        TrackManager.getInstance(mContext).createTrack(savedTrack, mCallback);
                        songReady = false;
                        hasImg = false;
                    }
                }
            }
            if(fromPlaylist){
                playlist.setThumbnail((String) resultData.get("url"));
                PlaylistManager.getInstance(mContext).createPlaylist(playlist, pCallback);
            }


        }
        @Override
        public void onError(String requestId, ErrorInfo error) {
        }
        @Override
        public void onReschedule(String requestId, ErrorInfo error) {
        }
    }
}
