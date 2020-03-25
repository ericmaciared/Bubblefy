package edu.url.salle.eric.macia.bubblefy.restapi.manager;

import android.content.Context;

import java.io.IOException;

import edu.url.salle.eric.macia.bubblefy.model.Playlist;
import edu.url.salle.eric.macia.bubblefy.model.UserToken;
import edu.url.salle.eric.macia.bubblefy.restapi.callback.PlaylistCallback;
import edu.url.salle.eric.macia.bubblefy.restapi.service.PlaylistService;
import edu.url.salle.eric.macia.bubblefy.utils.Constants;
import edu.url.salle.eric.macia.bubblefy.utils.Session;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class PlaylistManager {

    private static final String TAG = PlaylistManager.class.getName();

    private Context mContext;
    private PlaylistService mService;
    private Retrofit mRetrofit;

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



}
