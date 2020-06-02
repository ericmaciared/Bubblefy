package edu.url.salle.eric.macia.bubblefy.restapi.manager;

import android.content.Context;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;

import edu.url.salle.eric.macia.bubblefy.model.Playlist;
import edu.url.salle.eric.macia.bubblefy.model.UserToken;
import edu.url.salle.eric.macia.bubblefy.restapi.callback.PlaylistCallback;
import edu.url.salle.eric.macia.bubblefy.restapi.service.PlaylistService;
import edu.url.salle.eric.macia.bubblefy.utils.Constants;
import edu.url.salle.eric.macia.bubblefy.utils.Session;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class PlaylistManager {

    private static final String TAG = PlaylistManager.class.getName();

    private Context mContext;
    private PlaylistService mService;
    private static PlaylistManager sPlaylistManager;
    private Retrofit mRetrofit;

    public static PlaylistManager getInstance (Context context) {
        if (sPlaylistManager == null) {
            sPlaylistManager = new PlaylistManager(context);
        }

        return sPlaylistManager;
    }

    public PlaylistManager(Context context) {
        mContext = context;

        mRetrofit = new Retrofit.Builder()
                .baseUrl(Constants.NETWORK.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        mService = mRetrofit.create(PlaylistService.class);
    }


    public void createPlaylist(Playlist playlist, final PlaylistCallback callback) {
        UserToken userToken = Session.getInstance(mContext).getUserToken();
        Call<Playlist> call = mService.createPlaylist(playlist, "Bearer " + userToken.getIdToken());
        call.enqueue(new Callback<Playlist>() {
            @Override
            public void onResponse(Call<Playlist> call, Response<Playlist> response) {
                int code = response.code();

                if (response.isSuccessful()) {
                    callback.onPlaylistCreated(response.body());
                } else {
                    try {
                        callback.onPlaylistFailure(new Throwable(response.errorBody().string()));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<Playlist> call, Throwable t) {
                callback.onPlaylistFailure(t);
            }
        });


    }

    public void getOwnPlaylist(final PlaylistCallback callback) {
        UserToken userToken = Session.getInstance(mContext).getUserToken();
        Call<ArrayList<Playlist>> call = mService.getOwnPlaylists("Bearer " + userToken.getIdToken());
        call.enqueue(new Callback<ArrayList<Playlist>>() {
            @Override
            public void onResponse(Call<ArrayList<Playlist>> call, Response<ArrayList<Playlist>> response) {
                int code = response.code();
                if (response.isSuccessful()) {
                    callback.onUserPlaylistReceived((ArrayList<Playlist>) response.body());
                } else {
                    Log.d(TAG, "Error Not Successful: " + code);
                    callback.onNoPlaylist(new Throwable("ERROR " + code + ", " + response.raw().message()));
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Playlist>> call, Throwable t) {
                Log.d(TAG, "Error Failure: " + t.getStackTrace());
                callback.onFailure(new Throwable("ERROR " + t.getStackTrace()));
            }
        });

    }


    public void getUserIdPlaylist(String id, final PlaylistCallback callback) {
        UserToken userToken = Session.getInstance(mContext).getUserToken();
        Call<ArrayList<Playlist>> call = mService.getUserIdPlaylists(id, "Bearer " + userToken.getIdToken());
        call.enqueue(new Callback<ArrayList<Playlist>>() {
            @Override
            public void onResponse(Call<ArrayList<Playlist>> call, Response<ArrayList<Playlist>> response) {
                int code = response.code();
                if (response.isSuccessful()) {
                    callback.onExternalUserPlaylistReceived((ArrayList<Playlist>) response.body());
                } else {
                    Log.d(TAG, "Error Not Successful: " + code);
                    callback.onNoPlaylist(new Throwable("ERROR " + code + ", " + response.raw().message()));
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Playlist>> call, Throwable t) {
                Log.d(TAG, "Error Failure: " + t.getStackTrace());
                callback.onFailure(new Throwable("ERROR " + t.getStackTrace()));
            }
        });

    }


    public void getFollowingPlaylist(final PlaylistCallback callback) {
        UserToken userToken = Session.getInstance(mContext).getUserToken();
        Call<ArrayList<Playlist>> call = mService.getFollowingPlaylists("Bearer " + userToken.getIdToken());
        call.enqueue(new Callback<ArrayList<Playlist>>() {
            @Override
            public void onResponse(Call<ArrayList<Playlist>> call, Response<ArrayList<Playlist>> response) {
                int code = response.code();
                if (response.isSuccessful()) {
                    callback.onUserFollowingPlaylistReceived((ArrayList<Playlist>) response.body());
                } else {
                    Log.d(TAG, "Error Not Successful: " + code);
                    callback.onNoPlaylist(new Throwable("ERROR " + code + ", " + response.raw().message()));
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Playlist>> call, Throwable t) {
                Log.d(TAG, "Error Failure: " + t.getStackTrace());
                callback.onFailure(new Throwable("ERROR " + t.getStackTrace()));
            }
        });


    }


    public void updatePlaylist(Playlist playlist, final PlaylistCallback callback) {
        UserToken userToken = Session.getInstance(mContext).getUserToken();
        Call<Playlist> call = mService.updatePlaylist(playlist, "Bearer " + userToken.getIdToken());
        call.enqueue(new Callback<Playlist>() {
            @Override
            public void onResponse(Call<Playlist> call, Response<Playlist> response) {
                int code = response.code();

                if (response.isSuccessful()) {
                    callback.onPlaylistUpdated(response.body());
                } else {
                    try {
                        callback.onPlaylistFailure(new Throwable(response.errorBody().string()));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<Playlist> call, Throwable t) {
                callback.onPlaylistFailure(t);
            }
        });

    }

    public void followPlaylist(int id, final PlaylistCallback callback) {
        UserToken userToken = Session.getInstance(mContext).getUserToken();
        Call<ResponseBody> call = mService.followPlaylist(id, "Bearer " + userToken.getIdToken());
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                int code = response.code();

                if (response.isSuccessful()) {
                    callback.onFollowSuccess();
                } else {
                    try {
                        callback.onFailure(new Throwable(response.errorBody().string()));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                callback.onPlaylistFailure(t);
            }
        });

    }
}
