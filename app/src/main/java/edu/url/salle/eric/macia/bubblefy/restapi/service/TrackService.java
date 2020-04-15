package edu.url.salle.eric.macia.bubblefy.restapi.service;

import java.util.List;

import edu.url.salle.eric.macia.bubblefy.model.Track;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;

public interface TrackService {

    @GET("tracks")
    Call<List<Track>> getAllTracks(@Header("Authorization") String token);

    @GET("me/tracks")
    Call<List<Track>> getOwnTracks(@Header("Authorization") String token);

    @GET("users/{login}/tracks")
    Call<List<Track>> getUserTracks(@Path("login") String login, @Header("Authorization") String token);

    @GET("me/tracks/liked")
    Call<List<Track>> getUserLikedTracks(@Header("Authorization") String token);

}
