package edu.url.salle.eric.macia.bubblefy.restapi.service;

import edu.url.salle.eric.macia.bubblefy.model.Playlist;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface PlaylistService {
    @POST("playlists")
    Call<Playlist> createPlaylist(@Body Playlist playlist, @Header("Authorization") String token);
}
