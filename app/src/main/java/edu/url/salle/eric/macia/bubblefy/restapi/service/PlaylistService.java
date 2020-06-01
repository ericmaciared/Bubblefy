package edu.url.salle.eric.macia.bubblefy.restapi.service;

import java.util.ArrayList;

import edu.url.salle.eric.macia.bubblefy.model.Playlist;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface PlaylistService {
    @POST("playlists")
    Call<Playlist> createPlaylist(@Body Playlist playlist, @Header("Authorization") String token);

    @GET("me/playlists")
    Call<ArrayList<Playlist>> getOwnPlaylists(@Header("Authorization") String token);

    @GET("me/playlists/following")
    Call<ArrayList<Playlist>> getFollowingPlaylists(@Header("Authorization") String token);

    @PUT("playlists")
    Call<Playlist> updatePlaylist(@Body Playlist playlist, @Header("Authorization") String token);

    @PUT("playlists/{id}/follow")
    Call<ResponseBody> followPlaylist(@Path("id") int id, @Header("Authorization") String token);
}
