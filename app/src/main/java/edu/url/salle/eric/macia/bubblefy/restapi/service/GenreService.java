package edu.url.salle.eric.macia.bubblefy.restapi.service;

import java.util.List;

import edu.url.salle.eric.macia.bubblefy.model.Genre;
import edu.url.salle.eric.macia.bubblefy.model.Track;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface GenreService {

    @GET("genres/{id}")
    Call<Genre> getGenreById(@Path("id") Integer id, @Header("Authorization") String token);

    @GET("genres")
    Call<List<Genre>> getAllGenres(@Header("Authorization") String token);

    @GET("genres/{id}/tracks")
    Call<List<Track>> getTracksByGenre(@Path("id") Integer id, @Header("Authorization") String token);
}
