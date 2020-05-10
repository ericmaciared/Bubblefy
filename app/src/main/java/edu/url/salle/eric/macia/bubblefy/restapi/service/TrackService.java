package edu.url.salle.eric.macia.bubblefy.restapi.service;

import java.util.ArrayList;
import java.util.List;

import edu.url.salle.eric.macia.bubblefy.model.Confirmation;
import edu.url.salle.eric.macia.bubblefy.model.Track;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface TrackService {

    @GET("tracks")
    Call<List<Track>> getAllTracks(@Header("Authorization") String token);

    @GET("me/tracks")
    Call<ArrayList<Track>> getOwnTracks(@Header("Authorization") String token);

    @GET("users/{login}/tracks")
    Call<List<Track>> getUserTracks(@Path("login") String login, @Header("Authorization") String token);

    @GET("me/tracks/liked")
    Call<List<Track>> getUserLikedTracks(@Header("Authorization") String token);

    @PUT("tracks/{id}/like")
    Call<Confirmation> setTrackLike(@Path("id") int id, @Header("Authorization") String token);

    @GET("tracks/{id}/like")
    Call<Confirmation> getTrackLike(@Path("id") int id, @Header("Authorization") String token);

    @POST("tracks")
    Call<ResponseBody> createTrack(@Body Track track, @Header("Authorization") String token);
}
