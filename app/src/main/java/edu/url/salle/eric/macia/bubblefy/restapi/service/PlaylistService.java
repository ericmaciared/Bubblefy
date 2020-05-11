package edu.url.salle.eric.macia.bubblefy.restapi.service;

import java.util.ArrayList;

import edu.url.salle.eric.macia.bubblefy.model.Playlist;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;

public interface PlaylistService {
    @POST("playlists")
    Call<Playlist> createPlaylist(@Body Playlist playlist, @Header("Authorization") String token);

    @GET("me/playlists")
    Call<ArrayList<Playlist>> getOwnPlaylists(@Header("Authorization") String token);

    @PUT("playlists")
    Call<Playlist> updatePlaylist(@Body Playlist playlist, @Header("Authorization") String token);
}
